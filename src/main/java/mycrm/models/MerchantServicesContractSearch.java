package mycrm.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantServicesContractSearch {
    private String q;
    private String logType;
    private String fromDate;
    private String toDate;
    private Broker broker;
    private User loggedInUser;
    private Broker pendingBroker;
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
    private boolean lostRenewal;
    private int monthsRemaining;
    private String monthRemainingDate;
}
