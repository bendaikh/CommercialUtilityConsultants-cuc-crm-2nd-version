package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface BrokerTransferHistoryService {
    void save(BrokerTransferHistory brokerTransferHistory);

    List<String> findLatestGasBrokerTransferHistory(GasCustomerContract gasCustomerContract);

    List<String> findLatestElectricBrokerTransferHistory(ElecCustomerContract elecCustomerContract);

    List<String> findLatestUtilityBrokerTransferHistory(UtilityContract utilityContract);

    List<String> findLatestMerchantServicesBrokerTransferHistory(MerchantServicesContract merchantServicesContract);

    List<String> findLatestBroadbandBrokerTransferHistory(BroadbandContract broadbandContract);

    List<String> findLatestLandlineBrokerTransferHistory(LandlineContract landlineContract);
    List<String> findLatestVoipBrokerTransferHistory(VoipContract voipContract);
    List<String> findLatestWaterBrokerTransferHistory(WaterContract waterContract);


}
