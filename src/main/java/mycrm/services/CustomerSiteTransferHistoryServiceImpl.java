package mycrm.services;

import mycrm.models.CustomerSite;
import mycrm.models.CustomerSiteTransferHistory;
import mycrm.repositories.CustomerSiteTransferHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Primary
public class CustomerSiteTransferHistoryServiceImpl implements CustomerSiteTransferHistoryService {
    private final CustomerSiteTransferHistoryRepository customerSiteTransferHistoryRepository;

    @Autowired
    public CustomerSiteTransferHistoryServiceImpl(CustomerSiteTransferHistoryRepository customerSiteTransferHistoryRepository) {
        this.customerSiteTransferHistoryRepository = customerSiteTransferHistoryRepository;
    }

    @Override
    public CustomerSiteTransferHistory save(CustomerSiteTransferHistory customerSiteTransferHistory) {
        return customerSiteTransferHistoryRepository.save(customerSiteTransferHistory);
    }

    @Override
    public List<String> findCustomerSiteTransferHistoryByCustomerSite(CustomerSite customerSite) {
        List<String> updateHistoryList = new ArrayList<>();
        List<CustomerSiteTransferHistory> updateHistory =
                customerSiteTransferHistoryRepository.findCustomerSiteTransferHistoryByCustomerSite(customerSite.getId());

        updateHistory.forEach(history -> {
            updateHistoryList.add(transferMessage(history));
        });

        return updateHistoryList;
    }

    private String transferMessage(CustomerSiteTransferHistory history) {
        String builder = "This site was moved from " +
                history.getPreviousBusinessName() +
                " " +
                "by " +
                history.getCreatedBy().getFirstName() +
                " " +
                history.getCreatedBy().getLastName() +
                " " +
                "on " +
                convertDateToString(history.getDateCreated());
        return builder;
    }

    private String convertDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH);
        return formatter.format(date);
    }
}
