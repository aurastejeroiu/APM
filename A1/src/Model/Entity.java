package Model;

abstract public class Entity implements IEntity {
    private int weight;

    protected Entity(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }
}
