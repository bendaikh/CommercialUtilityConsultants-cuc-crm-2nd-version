package mycrm.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSearch {

    private String q;

    private String logType;

    private String supplyType;

    private Supplier supplier;

    private String fromDate;

    private String toDate;

    private Broker broker;

    private Broker pendingBroker;

    private int monthsRemaining;

    private String monthRemainingDate;

    private User loggedInUser;

    private boolean customerContractSearch;

    private boolean renewalSearch;

    private boolean leadSearch;

    private boolean callbackSearch;

    private boolean lostRenewalSearch;

    private boolean myCustomerSearch;

    private boolean denyExternalBrokerAccess;

    private boolean showInternalBrokerOwnContractsOnly;

    private boolean showExternalBrokerOwnContractsOnly;

    private boolean showInternalBrokerOwnAndPendingContracts;

    private boolean showExternalBrokerOwnAndPendingContracts;

    private String campaign;

    private boolean lostRenewal;

    public ContractSearch() {
    }

    @Override
    public String toString() {
        return "ContractSearch{" +
                "q='" + q + '\'' +
                ", logType='" + logType + '\'' +
                ", supplyType='" + supplyType + '\'' +
                ", supplier=" + supplier +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", broker=" + broker +
                ", pendingBroker=" + pendingBroker +
                ", monthsRemaining=" + monthsRemaining +
                ", monthRemainingDate='" + monthRemainingDate + '\'' +
                ", loggedInUser=" + loggedInUser +
                ", customerContractSearch=" + customerContractSearch +
                ", renewalSearch=" + renewalSearch +
                ", leadSearch=" + leadSearch +
                ", callbackSearch=" + callbackSearch +
                ", lostRenewalSearch=" + lostRenewalSearch +
                ", myCustomerSearch=" + myCustomerSearch +
                ", denyExternalBrokerAccess=" + denyExternalBrokerAccess +
                ", showInternalBrokerOwnContractsOnly=" + showInternalBrokerOwnContractsOnly +
                ", showExternalBrokerOwnContractsOnly=" + showExternalBrokerOwnContractsOnly +
                ", showInternalBrokerOwnAndPendingContracts=" + showInternalBrokerOwnAndPendingContracts +
                ", showExternalBrokerOwnAndPendingContracts=" + showExternalBrokerOwnAndPendingContracts +
                ", campaign='" + campaign + '\'' +
                ", lostRenewal=" + lostRenewal +
                '}';
    }
}
