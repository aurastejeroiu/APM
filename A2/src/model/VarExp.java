package model;

public class VarExp extends Exp {
    private String id;

    public VarExp(String id) {
        this.id = id;
    }

    @Override
    public int eval(MyIDictionary <String, Integer> symTable) {
        Integer x = symTable.get(id);
//        if(x == null)
//            throw new UnknownVariableException("Unknown Variable Exception at: " + this.toString() + "\nThere is no such " + this.id + " variable");
        return symTable.get(id);
    }

    @Override
    public String toString() {
        return id;
    }
}