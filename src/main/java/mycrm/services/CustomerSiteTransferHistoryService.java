package mycrm.services;

import mycrm.models.CustomerSite;
import mycrm.models.CustomerSiteTransferHistory;

import java.util.List;

public interface CustomerSiteTransferHistoryService {
    CustomerSiteTransferHistory save(CustomerSiteTransferHistory customerSiteTransferHistory);

    List<String> findCustomerSiteTransferHistoryByCustomerSite(CustomerSite customerSite);
}
