package mycrm.models;

import java.util.List;

public class CustomerSearchResult {

    private List<Customer> returnedCustomers;
    private long returnedCustomerCount;
    private long totalCustomerCount;
    private int totalPages;

    public List<Customer> getReturnedCustomers() {
        return returnedCustomers;
    }

    public void setReturnedCustomers(List<Customer> returnedCustomers) {
        this.returnedCustomers = returnedCustomers;
    }

    public long getReturnedCustomerCount() {
        return returnedCustomerCount;
    }

    public void setReturnedCustomerCount(long returnedCustomerCount) {
        this.returnedCustomerCount = returnedCustomerCount;
    }

    public long getTotalCustomerCount() {
        return totalCustomerCount;
    }

    public void setTotalCustomers(long totalCustomerCount) {
        this.totalCustomerCount = totalCustomerCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
