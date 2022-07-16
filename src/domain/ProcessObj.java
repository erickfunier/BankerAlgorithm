package domain;

public class ProcessObj {
    private final int id;
    private final int[] resources;

    public ProcessObj(int id, int[] resources) {
        this.id = id;
        this.resources = resources;
    }

    public int getId() {
        return id;
    }

    public int getResource(int index) {
        return resources[index];
    }

    public void setResource(int index, int value) {
        resources[index] = value;
    }
}
