package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerTransferHistory;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.MerchantServicesContract;
import mycrm.models.UtilityContract;
import mycrm.repositories.BrokerRepository;
import mycrm.repositories.BrokerTransferHistoryRepository;
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
public class BrokerTransferHistoryServiceImpl implements BrokerTransferHistoryService {
    private final BrokerTransferHistoryRepository brokerTransferHistoryRepository;
    private final BrokerRepository brokerRepository;

    @Autowired
    public BrokerTransferHistoryServiceImpl(BrokerTransferHistoryRepository brokerTransferHistoryRepository,
                                            BrokerRepository brokerRepository) {
        this.brokerTransferHistoryRepository = brokerTransferHistoryRepository;
        this.brokerRepository = brokerRepository;
    }

    @Override
    public void save(BrokerTransferHistory brokerTransferHistory) {
        this.brokerTransferHistoryRepository.save(brokerTransferHistory);
    }

    @Override
    public List<String> findLatestGasBrokerTransferHistory(GasCustomerContract gasCustomerContract) {
        List<String> transferMessageList = new ArrayList<>();

        List<BrokerTransferHistory> brokerTransferHistoryList =
                this.brokerTransferHistoryRepository.findBrokerTransferHistoryOrderByDateCreatedDesc(
                        gasCustomerContract.getId(),
                        gasCustomerContract.getSupplyType()
                );

        brokerTransferHistoryList.forEach(brokerTransferHistory -> {
            Broker previousBroker = brokerRepository.findOne(brokerTransferHistory.getPreviousBroker());
            transferMessageList.add(transferMessage(brokerTransferHistory, previousBroker));
        });
        return transferMessageList;
    }

    @Override
    public List<String> findLatestElectricBrokerTransferHistory(ElecCustomerContract elecCustomerContract) {
        List<String> transferMessageList = new ArrayList<>();

        List<BrokerTransferHistory> brokerTransferHistoryList =
                this.brokerTransferHistoryRepository.findBrokerTransferHistoryOrderByDateCreatedDesc(
                        elecCustomerContract.getId(),
                        elecCustomerContract.getSupplyType()
                );

        brokerTransferHistoryList.forEach(brokerTransferHistory -> {
            Broker previousBroker = brokerRepository.findOne(brokerTransferHistory.getPreviousBroker());
            transferMessageList.add(transferMessage(brokerTransferHistory, previousBroker));
        });
        return transferMessageList;
    }

    @Override
    public List<String> findLatestUtilityBrokerTransferHistory(UtilityContract utilityContract) {
        List<String> transferMessageList = new ArrayList<>();

        List<BrokerTransferHistory> brokerTransferHistoryList =
                this.brokerTransferHistoryRepository.findBrokerTransferHistoryOrderByDateCreatedDesc(
                        utilityContract.getId(),
                        utilityContract.getUtilityType()
                );

        brokerTransferHistoryList.forEach(brokerTransferHistory -> {
            Broker previousBroker = brokerRepository.findOne(brokerTransferHistory.getPreviousBroker());
            transferMessageList.add(transferMessage(brokerTransferHistory, previousBroker));
        });
        return transferMessageList;
    }

    @Override
    public List<String> findLatestMerchantServicesBrokerTransferHistory(MerchantServicesContract merchantServicesContract) {
        List<String> transferMessageList = new ArrayList<>();

        List<BrokerTransferHistory> brokerTransferHistoryList =
                this.brokerTransferHistoryRepository.findBrokerTransferHistoryOrderByDateCreatedDesc(
                        merchantServicesContract.getId(),
                        merchantServicesContract.getSupplyType()
                );

        brokerTransferHistoryList.forEach(brokerTransferHistory -> {
            Broker previousBroker = brokerRepository.findOne(brokerTransferHistory.getPreviousBroker());
            transferMessageList.add(transferMessage(brokerTransferHistory, previousBroker));
        });
        return transferMessageList;
    }

    private String transferMessage(BrokerTransferHistory brokerTransferHistory, Broker previousBroker) {
        StringBuilder builder = new StringBuilder();
        builder.append("Contract transferred from ");
        builder.append(previousBroker.getFirstName());
        builder.append(" ");
        builder.append(previousBroker.getLastName());
        builder.append(" ");
        builder.append("by ");
        builder.append(brokerTransferHistory.getCreatedBy().getFirstName());
        builder.append(" ");
        builder.append(brokerTransferHistory.getCreatedBy().getLastName());
        builder.append(" ");
        builder.append("on ");
        builder.append(convertDateToString(brokerTransferHistory.getDateCreated()));
        return builder.toString();
    }

    private String convertDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH);
        return formatter.format(date);
    }
}
