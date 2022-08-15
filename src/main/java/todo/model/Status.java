package todo.model;

public enum Status {
    TODO(0), PROGRESS(1), COMPLETED(2);

    private int numVal;

    Status(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
