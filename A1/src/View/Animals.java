package View;
import Controller.Contr;
import Model.Pig;
import Model.Entity;
import Model.Cow;
import Model.Bird;
import java.util.Vector;

public class Animals {
    private final Contr controller;
    public Animals(Contr controller){this.controller = controller;}
    public void Run() {
        this.controller.ControllerAdd(new Pig(150));
        this.controller.ControllerAdd(new Bird(3));
        this.controller.ControllerAdd(new Pig(150));
        this.controller.ControllerAdd(new Cow(420));
        this.controller.ControllerAdd(new Pig(2));
        this.controller.ControllerAdd(new Bird(1));
        this.controller.ControllerAdd(new Pig(400));
        this.controller.ControllerAdd(new Cow(4));
        Vector<Entity> elements = this.controller.GetEntitiesGreaterThan(200);

        for (var it : elements) {
            System.out.println(it.getWeight());
    }
}
}
