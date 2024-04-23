import Controller.Contr;
import Repository.Repo;
import View.Animals;

import javax.management.loading.ClassLoaderRepository;

public class main {
    public static void main(String[] args) {
        var repo = new Repo();
        var controller = new Contr (repo);
        var view = new Animals(controller);
        view.Run();
    }
}
