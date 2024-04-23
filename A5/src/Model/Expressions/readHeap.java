package Model.Expressions;
import Exceptions.DivByZeroException;
import Exceptions.WrongOpException;
import Model.ADT.IMyDict;
import Model.ADT.IMyHeap;
import Model.Value.IValue;
import Model.Value.RefValue;

public class readHeap implements IExpression{
    IExpression expression;
    public readHeap(IExpression s){
        this.expression = s;
    }
    @Override
    public IValue eval(IMyDict<String, IValue> symTbl, IMyHeap<IValue> heap) throws DivByZeroException, WrongOpException {
        if(!(expression.eval(symTbl, heap) instanceof RefValue)){
            throw new WrongOpException("Exception on readHeap: expression is not evaluated to RefValue");
        }
        IValue val = expression.eval(symTbl, heap);
        int address = ((RefValue) val).getAddress();
        return heap.lookUp(address);
    }

    @Override
    public String toString() {
        return "readHeapExp(" + expression.toString() + ")";
    }
}
