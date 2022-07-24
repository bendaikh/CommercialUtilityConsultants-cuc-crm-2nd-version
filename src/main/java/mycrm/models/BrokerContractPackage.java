package mycrm.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BrokerContractPackage {

    private List<BrokerGasContract> gasContracts;

    private List<BrokerElectricContract> electricityContracts;

    private List<BrokerUtilityContract> solarContracts;

    private List<BrokerUtilityContract> waterContracts;

    private List<BrokerUtilityContract> voipContracts;

    private List<BrokerUtilityContract> landlineContracts;

    private List<BrokerUtilityContract> mobileContracts;

    private List<BrokerUtilityContract> broadbandContracts;

    private List<BrokerMerchantServicesContract> merchantServicesContracts;
}
