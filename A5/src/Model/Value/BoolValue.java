package Model.Value;

import Model.Types.BoolType;
import Model.Types.IType;

public class BoolValue implements IValue{
    boolean value;
    public BoolValue(){this.value = false;}
    public BoolValue(boolean i){this.value = i;}

    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass())
            return false;
        BoolValue o_value = (BoolValue) o;
        return o_value.value == this.value;
    }

    public boolean getValue(){return this.value;}

    @Override
    public String toString(){
        return this.value ? "true" : "false";
    }

    @Override
    public IType getType(){return new BoolType();}


    public IValue deepCopy(){return new BoolValue(this.value);}
}
