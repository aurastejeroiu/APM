import Controller.Controller;
import Repository.IRepository;
import Repository.Repository;
import View.IUserInterface;

public class Main {
    public static void main(String[] args) {
        IRepository repository = new Repository();
        Controller controller = new Controller(repository);

        //IUserInterface console = new ConsoleUI(repository, controller);
        //console.run();
    }
}

//ConsoleUI
//IUserInterface
//Teste
//Logs+Ex