package deik.mestintalk.magic26.ga;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

import deik.mestintalk.magic26.instance.*;

/**
 * GA is a class for the genetic algorithm implementation.
 * 
 * @author Dóra Veréb
 *
 */

public class GA {

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  private int initialSize;
  private double prob;
  private boolean elitian;

  public GA(int initialSize, double prob, boolean elitian) {
    this.initialSize = initialSize;
    this.prob = prob;
    this.elitian = elitian;
  }

  /**
   * Initialize the population.
   * 
   * @return a set with tables
   */
  public LinkedHashSet<Table> initialPopulation() {
    LinkedHashSet<Table> result = new LinkedHashSet<>();
    while (result.size() < initialSize) {
      result.add(new Table().initTable());
    }
    return result;
  }

  /**
   * Generates next population.
   * 
   * @param population
   * @return the next population
   */
  public LinkedHashSet<Table> nextPopulation(LinkedHashSet<Table> population) {
    int sfit = 0;
    for (Table t : population) {
      sfit += t.fitness();
    }
    System.out.println("sfit: " + sfit);
    LinkedHashSet<Table> newPopulation = new LinkedHashSet<>();

    if (elitian) {
      Table best = population.iterator().next();
      for (Table t : population) {
        if (best.fitness() < t.fitness()) {
          best = t;
        }
      }
      System.out.println("best:\n" + best.toString());
      newPopulation.add(best);
    }

    while (newPopulation.size() < population.size()) {
      ArrayList<Table> selection = new ArrayList<>();
      while (selection.size() < 2) {
        int rnd = GA.RANDOM.nextInt(sfit);
        Table selected = null;
        for (Table t : population) {
          if (t.fitness() > rnd) {
            selected = t;
            break;
          } else {
            rnd -= t.fitness();
          }
        }
        selection.add(selected);
      }
      Table t = selection.get(0).crossover(selection.get(1));
      t = t.mutation(prob);
      newPopulation.add(t);
    }
    return newPopulation;
  }

  public static void main(String[] args) {

    GA ga = new GA(20, 0.1, true);
    LinkedHashSet<Table> population = ga.initialPopulation();
    while (true) {
      Table best = population.iterator().next();
      for (Table t : population) {
        if (best.fitness() < t.fitness()) {
          best = t;
        }
      }

      if (best.fitness() == 7) {
        System.out.println("Final best: " + best.toString());
        return;
      }

      population = ga.nextPopulation(population);
    }
  }
}


