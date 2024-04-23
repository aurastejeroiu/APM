package Controller;
import Model.PrgState;
import Model.Value.IValue;
import Model.Value.RefValue;
import Repository.IRepo;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    IRepo repo;
    ExecutorService executor;

    public Controller(IRepo _repo){repo=_repo;}
    Map<Integer, IValue> conservativeGarbageCollector(List<Integer> symTblAddr, Map<Integer, IValue> heap){
        return heap.entrySet().stream().filter(e->symTblAddr.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }
    public void addProgram(PrgState newPrg) {
        repo.addPrg(newPrg);
    }


    void oneStepForAllPrg(List<PrgState> prgList) throws Exception {
        //print the prg list into the log file
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> {
                    return p.oneStep();
                }))
                .collect(Collectors.toList());

        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    return null;
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.repo.setPrgStateList(prgList);
    }

    public void allStep()  throws Exception {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletePrg(repo.getPrgStateList());
        while (prgList.size() > 0){
            oneStepForAllPrg(prgList);
            prgList = removeCompletePrg(repo.getPrgStateList());
        }
        executor.shutdownNow();
        repo.setPrgStateList(prgList);
    }

    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues){
        return symTableValues.stream()
                .filter(v-> v instanceof RefValue)
                .map(v-> {RefValue v1 = (RefValue)v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

    public List<PrgState> removeCompletePrg(List<PrgState> prgStateList){
        return prgStateList.stream().filter(PrgState::isNotCompleted).collect(Collectors.toList());
    }

}
