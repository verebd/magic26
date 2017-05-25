package deik.mestintalk.magic26.instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Table is a class which contains fields and methods for the fitness, crossing and mutate tables.
 * 
 * @author Dora Vereb
 *
 */
public class Table implements Instance, InstanceFactory {

  public static final int MAGIC_NUMBER = 26;
  public static final int MAX_NUMBER = 12;
  private static final Random RANDOM = new Random(System.currentTimeMillis());

  private int[][] matrix;
  private HashMap<Group, Integer> groupSums;

  public Table() {
    this.matrix = new int[4][4];
    this.groupSums = new HashMap<>();
  }

  public int[][] getMatrix() {
    return matrix;
  }

  public HashMap<Group, Integer> getGroupSums() {
    return groupSums;
  }

  /**
   * Returns a list of Integers from 1 to 12 in random order. This list is used for initialize a new
   * table.
   * 
   * @return A list of Integers containing the numbers between 1-12 (including) in random order.
   */
  private List<Integer> createNumbersForInit() {
    List<Integer> numbers = new ArrayList<>();
    for (int i = 1; i <= MAX_NUMBER; i++) {
      numbers.add(i);
    }
    Collections.shuffle(numbers, RANDOM);
    return numbers;
  }

  /**
   * Initialize a new table.
   * 
   * @return A new table which contains the Integers from 1 to 12 in random order.
   */
  public Table initTable() {
    List<Integer> numbers = createNumbersForInit();
    int listIndex = 0;
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
          continue;
        }
        this.matrix[i][j] = numbers.get(listIndex);
        listIndex++;
      }
    }
    return this;
  }

  /**
   * Calculates the sum of the numbers in all seven matrix groups. Adds properties to the groupSums
   * field with group names as keys and sums as values.
   */
  private void calculateGroupSums() {
    int row1Sum = this.matrix[1][0] + this.matrix[1][1] + this.matrix[1][2] + this.matrix[1][3];
    int row2Sum = this.matrix[2][0] + this.matrix[2][1] + this.matrix[2][2] + this.matrix[2][3];
    int column1Sum = this.matrix[0][1] + this.matrix[1][1] + this.matrix[2][1] + this.matrix[3][1];
    int column2Sum = this.matrix[0][2] + this.matrix[1][2] + this.matrix[2][2] + this.matrix[3][2];
    int groupaSum = this.matrix[1][0] + this.matrix[2][0] + this.matrix[1][3] + this.matrix[2][3];
    int groupbSum = this.matrix[0][1] + this.matrix[0][2] + this.matrix[3][1] + this.matrix[3][2];
    int groupcSum = this.matrix[1][1] + this.matrix[1][2] + this.matrix[2][1] + this.matrix[2][2];

    this.groupSums.put(Group.ROW1, row1Sum);
    this.groupSums.put(Group.ROW2, row2Sum);
    this.groupSums.put(Group.COLUMN1, column1Sum);
    this.groupSums.put(Group.COLUMN2, column2Sum);
    this.groupSums.put(Group.GROUP_A, groupaSum);
    this.groupSums.put(Group.GROUP_B, groupbSum);
    this.groupSums.put(Group.GROUP_C, groupcSum);
  }

  /**
   * Calculates the fitness value of the table. If sum of the numbers in a matrix group equals to
   * 26, the fitness value is incremented by 1. So the maximum fitness value is 7 as there are 7
   * groups in the matrix.
   * 
   * @return the fitness value of a table.
   */
  @Override
  public int fitness() {
    calculateGroupSums();
    int f = 0;

    if (this.groupSums.get(Group.ROW1) == MAGIC_NUMBER) {
      f++;
    }
    if (this.groupSums.get(Group.ROW2) == MAGIC_NUMBER) {
      f++;
    }
    if (this.groupSums.get(Group.COLUMN1) == MAGIC_NUMBER) {
      f++;
    }
    if (this.groupSums.get(Group.COLUMN2) == MAGIC_NUMBER) {
      f++;
    }
    if (this.groupSums.get(Group.GROUP_A) == MAGIC_NUMBER) {
      f++;
    }
    if (this.groupSums.get(Group.GROUP_B) == MAGIC_NUMBER) {
      f++;
    }
    if (this.groupSums.get(Group.GROUP_C) == MAGIC_NUMBER) {
      f++;
    }
    return f;
  }

  /**
   * Returns the index of the given number in the table matrix or null if the number is not in the
   * matrix. As the table matrix is two-dimensional, the returned index is in an array. The first
   * element contains the row index and the second one contains the coloumn index.
   * 
   * @param n the given number
   * @return the index of the given number
   */
  private int[] getIndex(int n) {
    int[] arr = new int[2];
    for (int i = 0; i < this.matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        if (this.matrix[i][j] == n) {
          arr[0] = i;
          arr[1] = j;
          return arr;
        }
      }
    }
    return null;
  }

  /**
   * Returns if the given index contains 0 or not.
   * 
   * @param arr the given index. The arr[0] contains the row index, the arr[1] contains the coloumn
   *        index.
   * @return If the given matrix element contains true or false;
   */
  private boolean isPlaceFree(int[] arr) {
    if (this.matrix[arr[0]][arr[1]] != 0) {
      return false;
    }
    return true;
  }

  /**
   * Crosses two tables. Crossing method: Every even number is taken from the current table and
   * every uneven number is taken from the given table. The numbers are supposed to placed to the
   * index where they were in the origin table. If that index is already taken, the numbers are
   * taken from the other table. Those numbers which cannot taken from either table are collected
   * into a list and placed at the end of the method to the free places.
   * 
   * @return a new table
   */
  @Override
  public Table crossover(Table t1) {
    Table t = new Table();
    List<Integer> numbers = createNumbersForInit();
    List<Integer> missingNumbers = new ArrayList<>();


    for (Integer n : numbers) {
      int[] index = new int[2];
      if (n % 2 == 0) {
        index = this.getIndex(n);
        if (!t.isPlaceFree(index)) {
          index = t1.getIndex(n);
        }
      } else {
        index = t1.getIndex(n);
        if (!t.isPlaceFree(index)) {
          index = this.getIndex(n);
        }
      }

      if (!t.isPlaceFree(index)) {
        missingNumbers.add(n);
      } else {
        t.matrix[index[0]][index[1]] = n;
      }
    }

    if (missingNumbers.size() != 0) {
      int z = 0;
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
            continue;
          }
          if (t.matrix[i][j] == 0) {
            t.matrix[i][j] = missingNumbers.get(z);
            z++;
          }
        }
      }
    }

    return t;
  }

  /**
   * Switches two numbers in the matrix randomly on a random probability.
   * 
   * @return a new table
   */
  @Override
  public Table mutation(double prob) {
    Table t = new Table();
    for (int i = 0; i < this.matrix.length; i++) {
      for (int j = 0; j < this.matrix.length; j++) {
        t.matrix[i][j] = this.matrix[i][j];
      }
    }
    if (RANDOM.nextDouble() < prob) {
      int n1 = RANDOM.nextInt(12) + 1;
      int n2 = RANDOM.nextInt(12) + 1;
      int[] a1 = getIndex(n1);
      int[] a2 = getIndex(n2);
      t.matrix[a1[0]][a1[1]] = n2;
      t.matrix[a2[0]][a2[1]] = n1;
    }
    return t;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.deepHashCode(matrix);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Table other = (Table) obj;
    if (!Arrays.deepEquals(matrix, other.matrix))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuffer table = new StringBuffer(fitness() + "\n");
    int n = matrix.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        table.append("  " + matrix[i][j] + "  ");
      }
      table.append('\n');
    }
    return table.toString();
  }
}
