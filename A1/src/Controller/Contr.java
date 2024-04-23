package Controller;
import Model.Entity;
import Repository.Repo;

import java.util.Vector;

public class Contr implements IContr {
    private final Repo repo;

    public Contr (Repo repo) {
        this.repo = repo;
    }

    public void ControllerAdd(Entity e) {
        this.repo.RepoAdd(e);
    }

    public Vector<Entity> GetEntitiesGreaterThan(int weight) {
        return this.repo.RepoGetEntitiesGreaterThan(weight);
    }
}
