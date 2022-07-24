package mycrm.models;

import java.util.ArrayList;
import java.util.List;

public class NotedUtilityCallback {
    private UtilityContract contract;
    private List<CustomerNote> customerNotes = new ArrayList<>();

    public UtilityContract getContract() {
        return contract;
    }

    public void setContract(UtilityContract contract) {
        this.contract = contract;
    }

    public List<CustomerNote> getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(List<CustomerNote> customerNotes) {
        this.customerNotes = customerNotes;
    }
}
