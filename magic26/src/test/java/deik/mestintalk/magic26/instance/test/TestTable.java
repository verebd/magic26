package deik.mestintalk.magic26.instance.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import deik.mestintalk.magic26.instance.Group;
import deik.mestintalk.magic26.instance.Table;

public class TestTable {
  
  private Table t1;
  private Table t2;
  private static List<Integer> possibleNumbers;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    possibleNumbers = new ArrayList<>();
    for(int i = 1; i <= 12; i++){
      possibleNumbers.add(i);
    }
  }

  @Before
  public void setUp() throws Exception {
    t1 = new Table();
    t2 = new Table();   
  }

  @Test
  public void testInitTable() {    
    t1.initTable();
        
    assertEquals(4, t1.getMatrix().length);
    assertEquals(4, t1.getMatrix()[0].length);
    assertEquals(4, t1.getMatrix()[1].length);
    assertEquals(4, t1.getMatrix()[2].length);
    assertEquals(4, t1.getMatrix()[3].length);
    
    assertEquals(0, t1.getMatrix()[0][0]);
    assertEquals(0, t1.getMatrix()[0][3]);
    assertEquals(0, t1.getMatrix()[3][0]);
    assertEquals(0, t1.getMatrix()[3][3]);
    
    Set<Integer> numbers = new HashSet<>();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
          continue;
        }
        numbers.add(t1.getMatrix()[i][j]);
        assertTrue(possibleNumbers.contains(t1.getMatrix()[i][j]));
      }
    }
    assertEquals(12, numbers.size());    
  }

  @Test
  public void testFitness(){
    int k = 0;
    for (int i = 0; i < 4; i++){
      for (int j = 0; j < 4; j++){
        if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
          t1.getMatrix()[i][j] = 0;
        } else {
          t1.getMatrix()[i][j] = possibleNumbers.get(k);
          k++;
        }        
      }
    }
    
    int f = t1.fitness();
    assertEquals(18, (int) t1.getGroupSums().get(Group.ROW1));
    assertEquals(34, (int) t1.getGroupSums().get(Group.ROW2));
    assertEquals(24, (int) t1.getGroupSums().get(Group.COLUMN1));
    assertEquals(28, (int) t1.getGroupSums().get(Group.COLUMN2));
    assertEquals(26, (int) t1.getGroupSums().get(Group.GROUP_A));
    assertEquals(26, (int) t1.getGroupSums().get(Group.GROUP_B));
    assertEquals(26, (int) t1.getGroupSums().get(Group.GROUP_C));
    
    assertEquals(3, f);
  }
  
  @Test
  public void testCrossover(){
    int k = 0;
    for (int i = 0; i < 4; i++){
      for (int j = 0; j < 4; j++){
        if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
          t1.getMatrix()[i][j] = 0;
        } else {
          t1.getMatrix()[i][j] = possibleNumbers.get(k);
          k++;
        }        
      }
    }
    k = 11;
    for (int i = 0; i < 4; i++){
      for (int j = 0; j < 4; j++){
        if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
          t2.getMatrix()[i][j] = 0;
        } else {
          t2.getMatrix()[i][j] = possibleNumbers.get(k);
          k--;
        }        
      }
    }
    
    Table t = t1.crossover(t2);
    for (int i = 0; i < 4; i++){
      for (int j = 0; j < 4; j++){
        if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
          assertEquals(0, t.getMatrix()[i][j]);
        } else {
          assertTrue(t.getMatrix()[i][j] == t1.getMatrix()[i][j] || t.getMatrix()[i][j] == t2.getMatrix()[i][j]);          
        }        
      }
    }    
  }
  
  @Test
  public void testMutation(){
    int k = 0;
    for (int i = 0; i < 4; i++){
      for (int j = 0; j < 4; j++){
        if ((i == 0 || i == 3) && (j == 0 || j == 3)) {
          t1.getMatrix()[i][j] = 0;
        } else {
          t1.getMatrix()[i][j] = possibleNumbers.get(k);
          k++;
        }        
      }
    }
    
    double prob = 0;  
    Table t = t1.mutation(prob);
    assertEquals(t, t1);
    
    prob = 1;  
    Table t2 = t1.mutation(prob);
    assertNotEquals(t2, t1);
  }
}
