package mycrm.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class PendingTerminationTask {
    private long id;

    private Customer customer;

    private String supplyType;

    private Date startDate;

    private Date endDate;

    private boolean currentSupplyTerminated;
}
