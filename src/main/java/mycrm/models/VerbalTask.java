package mycrm.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class VerbalTask {
    private long id;

    private String supplyType;

    private Customer customer;

    private CustomerSite customerSite;

    private Date startDate;

    private Date endDate;

    private Supplier supplier;

    private Broker broker;

    private Date dateCreated;

    private User createdBy;

    private String accountNumber;
}
