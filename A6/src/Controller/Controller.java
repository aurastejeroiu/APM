package Controller;

import Exceptions.MyException;
import Model.ADTs.Heap;
import Model.PrgState;
import Model.Values.IValue;
import Model.Values.RefValue;
import Repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepository repo;
    private ExecutorService executor;

    public Controller(IRepository repo_) {
        repo = repo_;
    }

    public void allStep(boolean displayFlag) throws MyException, IOException, InterruptedException {
        int step = 0;
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        while(prgList.size() > 0) {
            List<Integer> symTableAddr = repo.getPrgList().stream()
                            .map(p -> getAddrFromSymTable(p.getSymTable().getContent().values()))
                                    .reduce(new ArrayList<>(), (p1, p2) -> Stream.concat(p1.stream(), p2.stream())
                                            .distinct().collect(Collectors.toList()));
            Heap heap = repo.getPrgList().get(0).getHeapTable();
            List<Integer> heapAddr = getAddrFromHeap(heap.getContent().values());
            Map<Integer, IValue> newHeap = safeGarbageCollector(symTableAddr, heapAddr, heap.getContent());
            heap.setContent(newHeap);
            oneStepForAllPrg(prgList);
            if(displayFlag) {
                displayCrtState(step);
                step = step + 1;
            }
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public void oneStepForAllPrg(List<PrgState> prgList) {
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState prg) -> (Callable<PrgState>)(prg::oneStep))
                .collect(Collectors.toList());
        List<PrgState> newPrgList;
        try {
            newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            Throwable exception = e.getCause();
                            if (exception instanceof MyException) {
                                System.out.println(exception.getMessage());
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            prgList.addAll(newPrgList);
            prgList.forEach(prg -> {
                try {
                    repo.logPrgStateExec(prg);
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                }
            });
        } catch (InterruptedException e) {
            System.out.println("ERROR: the execution was interrupted");
        }
        repo.setPrgList(prgList);
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
        );
    }

    Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapTableAddr,
                                              Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || heapTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );
    }

    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> { RefValue v_ref = (RefValue) v;
                            return v_ref.getAddress();})
                .collect(Collectors.toList());
    }

    List<Integer> getAddrFromHeap(Collection<IValue> heapTableValues) {
        return heapTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) throws MyException {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public void displayCrtState(int step) {
        System.out.print("-----------------------------------------------Step ");
        System.out.print(step);
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        repo.getPrgList().forEach(System.out::println);
    }
}