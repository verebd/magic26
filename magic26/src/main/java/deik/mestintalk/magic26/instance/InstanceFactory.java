package deik.mestintalk.magic26.instance;

public interface InstanceFactory {

  public Table crossover(Table t1);

  public Table mutation(double prob);

}
