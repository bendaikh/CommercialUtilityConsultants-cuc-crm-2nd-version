package mycrm.models;

import java.util.ArrayList;
import java.util.List;

public class NotedCallback {
    private Contract contract;
    private List<CustomerNote> customerNotes = new ArrayList<>();

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public List<CustomerNote> getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(List<CustomerNote> customerNotes) {
        this.customerNotes = customerNotes;
    }

    @Override
    public String toString() {
        return "NotedCallback{" +
                "contract=" + contract +
                ", customerNotes=" + customerNotes +
                '}';
    }
}
