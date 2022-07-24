package mycrm.upload;

import java.util.HashSet;
import java.util.Set;

public class DiallerDataCollection {
    private Set<DiallerExportEntity> newDiallerData = new HashSet<>();
    private Set<DiallerExportEntity> updateDiallerData = new HashSet<>();

    public DiallerDataCollection() {
    }

    public DiallerDataCollection(Set<DiallerExportEntity> newDiallerData, Set<DiallerExportEntity> updateDiallerData) {
        this.newDiallerData = newDiallerData;
        this.updateDiallerData = updateDiallerData;
    }

    public Set<DiallerExportEntity> getNewDiallerData() {
        return newDiallerData;
    }

    public void setNewDiallerData(Set<DiallerExportEntity> newDiallerData) {
        this.newDiallerData = newDiallerData;
    }

    public Set<DiallerExportEntity> getUpdateDiallerData() {
        return updateDiallerData;
    }

    public void setUpdateDiallerData(Set<DiallerExportEntity> updateDiallerData) {
        this.updateDiallerData = updateDiallerData;
    }
}
