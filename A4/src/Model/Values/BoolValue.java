package Model.Values;

import Model.Types.BoolType;
import Model.Types.IType;

public class BoolValue implements IValue {
    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass())
            return false;
        return ((BoolValue) other).getValue() == value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}