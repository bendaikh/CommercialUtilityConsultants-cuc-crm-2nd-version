package mycrm.models;

import java.util.List;

public class CallbackSearchResult {

    private List<NotedCallback> returnedContracts;
    private long returnedContractCount;
    private long totalContractCount;
    private int totalPages;

    public List<NotedCallback> getReturnedContracts() {
        return returnedContracts;
    }

    public void setReturnedContracts(List<NotedCallback> returnedContracts) {
        this.returnedContracts = returnedContracts;
    }

    public long getReturnedContractCount() {
        return returnedContractCount;
    }

    public void setReturnedContractCount(long returnedContractCount) {
        this.returnedContractCount = returnedContractCount;
    }

    public long getTotalContractCount() {
        return totalContractCount;
    }

    public void setTotalContractCount(long totalContractCount) {
        this.totalContractCount = totalContractCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
