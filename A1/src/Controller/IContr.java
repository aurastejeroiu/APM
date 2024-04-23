package Controller;
import Model.Entity;

import java.util.Vector;

public interface IContr{
    void ControllerAdd(Entity e);
    Vector<Entity> GetEntitiesGreaterThan(int weight);
}
