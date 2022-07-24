package mycrm.models;

import lombok.Builder;
import lombok.Getter;
import mycrm.configuration.LogType;

import java.util.List;

@Builder
@Getter
public class BrokerUtilityContract {
    private List<UtilityContract> contracts;

    private int numberOfContracts;

    private LogType logType;

    private String supplyType;
}
