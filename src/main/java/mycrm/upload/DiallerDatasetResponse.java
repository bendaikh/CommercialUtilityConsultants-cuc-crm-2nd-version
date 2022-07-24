package mycrm.upload;

import java.util.List;

public class DiallerDatasetResponse {
    private String success;
    private int total;
    private List<DiallerDataset> list;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DiallerDataset> getList() {
        return list;
    }

    public void setList(List<DiallerDataset> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DiallerDatasetResponse{" +
                "success='" + success + '\'' +
                ", total=" + total +
                ", list=" + list +
                '}';
    }
}
