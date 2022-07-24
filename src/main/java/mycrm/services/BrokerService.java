package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerContractPackage;
import mycrm.models.BrokerTransferUpdate;
import mycrm.models.Customer;

import java.util.List;

public interface BrokerService {

    List<Broker> findAll();

    List<Broker> findLatest5();

    Broker findById(Long id);

    Broker save(Broker broker);

    Broker edit(Broker broker);

    void deleteById(Long id);

    List<Customer> findCustomersByBroker(Broker broker);

    BrokerContractPackage findBrokerContracts(Broker broker);

    BrokerContractPackage findBrokerContractStats(Broker broker);

    boolean transferBrokerContracts(BrokerTransferUpdate brokerTransferUpdate);
}
