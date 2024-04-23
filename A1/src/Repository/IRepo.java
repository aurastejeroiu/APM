package Repository;
import Model.Entity;
import java.util.Vector;

public interface IRepo {
    void RepoAdd(Entity e);
    Vector<Entity> RepoGetEntitiesGreaterThan(int weight);
}

