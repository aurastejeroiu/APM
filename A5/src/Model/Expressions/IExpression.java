package Model.Expressions;

import Exceptions.DivByZeroException;
import Exceptions.WrongOpException;
import Model.ADT.IMyDict;
import Model.ADT.IMyHeap;
import Model.Value.IValue;

public interface IExpression {
    public IValue eval(IMyDict<String, IValue> symTbl, IMyHeap<IValue> heap) throws DivByZeroException, WrongOpException;
    String toString();
}
