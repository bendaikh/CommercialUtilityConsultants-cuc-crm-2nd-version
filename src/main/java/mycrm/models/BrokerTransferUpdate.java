package mycrm.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mycrm.configuration.LogType;

@Getter
@Setter
@ToString
public class BrokerTransferUpdate {
    private Broker previousBroker;

    private Broker newBroker;

    private String supplyType;

    private LogType logType;
}
