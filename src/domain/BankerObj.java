package domain;

import java.util.ArrayList;
import java.util.List;

public class BankerObj {
    private int processes;
    private int resources;
    private int[] availableResources;
    private int[] maxResources;
    private List<ProcessObj> mtxAllocationA = new ArrayList<>();
    private List<ProcessObj> mtxClaimC = new ArrayList<>();
    private List<ProcessObj> mtxNeedCA = new ArrayList<>();
    private int idCounter = 0;
    private int idCounter1 = 0;

    public BankerObj(int processes, int resources, int[] maxResources) {
        this.processes = processes;
        this.resources = resources;
        availableResources = new int[resources];
        this.maxResources = maxResources;
    }

    public List<ProcessObj> getMtxAllocationA() {
        return mtxAllocationA;
    }

    public List<ProcessObj> getMtxClaimC() {
        return mtxClaimC;
    }

    public List<ProcessObj> getMtxNeedCA() {
        return mtxNeedCA;
    }

    public void addMtxAllocationA(ProcessObj processObj) {
        mtxAllocationA.add(processObj);
    }

    public void addMtxClaimC(ProcessObj processObj) {
        mtxClaimC.add(processObj);
    }

    public void addMtxNeedCA(ProcessObj processObj) {
        mtxNeedCA.add(processObj);
    }

    public int getProcesses() {
        return processes;
    }

    public int getResources() {
        return resources;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void incIdCounter() {
        idCounter++;
    }

    public int getIdCounter1() {
        return idCounter1;
    }

    public void incIdCounter1() {
        idCounter1++;
    }

    public void incAvailableResources(int index, int increment) {
        availableResources[index] += increment;
    }

    public void decAvailableResource(int index, int decrement) {
        availableResources[index] = decrement;
    }

    public int getMaxResource(int index) {
        return maxResources[index];
    }

    public int getAvailableResource(int index) {
        return availableResources[index];
    }

    public int[] getAvailableResources() {
        return availableResources;
    }

    public void removeProcess(int index) {
        mtxAllocationA.remove(index);
        mtxClaimC.remove(index);
        mtxNeedCA.remove(index);
    }

     public void setAvailableResource(int index, int value) {
        availableResources[index] = value;
     }

}
