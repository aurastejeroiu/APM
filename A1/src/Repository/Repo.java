package Repository;
import Model.Entity;
import java.util.Vector;

public class Repo implements IRepo {
    Vector<Entity> elements = new Vector<>();

    public void RepoAdd(Entity e) {
        this.elements.add(e);
    }

    public Vector<Entity> RepoGetEntitiesGreaterThan(int weight) {
        Vector<Entity> ret = new Vector<>();
        for (Entity e : elements) {
            if (e.getWeight() > 3)
                ret.add(e);
        }
        return ret;
    }
}
