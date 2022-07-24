package mycrm.services;

import mycrm.models.BrokerTransferHistory;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.UtilityContract;

import java.util.List;

public interface BrokerTransferHistoryService {
    void save(BrokerTransferHistory brokerTransferHistory);

    List<String> findLatestGasBrokerTransferHistory(GasCustomerContract gasCustomerContract);

    List<String> findLatestElectricBrokerTransferHistory(ElecCustomerContract elecCustomerContract);

    List<String> findLatestUtilityBrokerTransferHistory(UtilityContract utilityContract);

    List<String> findLatestMerchantServicesBrokerTransferHistory(MerchantServicesContract merchantServicesContract);
}
