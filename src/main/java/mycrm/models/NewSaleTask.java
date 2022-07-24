package mycrm.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NewSaleTask {
    private Long id;

    private Customer customer;

    private CustomerSite customerSite;

    private Date startDate;

    private String supplyType;

    private boolean welcomePackSentToCustomer;

    private boolean previousSupplyTerminated;
}
