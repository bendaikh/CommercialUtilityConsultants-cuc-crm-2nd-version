package mycrm.services;

import mycrm.models.*;

import java.util.List;

public interface CustomerNoteService {

    List<CustomerNote> findAll();

    List<CustomerNote> findLatest5();

    List<CustomerNote> findAllIncompleteByTaggedUser(User taggedUser);

    List<CustomerNote> findAllIncompleteByTaggedUserOrderByDueDateAsc(User taggedUser);

    CustomerNote findById(Long id);

    List<CustomerNote> findByCustomer(Customer customer);

    List<CustomerNote> findByCustomerOrderByDateCreatedDesc(Customer customer);

    CustomerNote save(CustomerNote customerNote);

    CustomerNote edit(CustomerNote customerNote);

    void deleteById(Long id);

    void updateCustomerNote(long id, User completedBy);

    List<CustomerNote> findByGasCustomerContractOrderByDateCreatedDesc(GasCustomerContract gasCustomerContract);

    List<CustomerNote> findByElecCustomerContractOrderByDateCreatedDesc(ElecCustomerContract elecCustomerContract);

    List<CustomerNote> findTop3ByGasCustomerContractOrderByDateCreatedDesc(GasCustomerContract gasCustomerContract);

    List<CustomerNote> findTop3ByElecCustomerContractOrderByDateCreatedDesc(ElecCustomerContract elecCustomerContract);

    void deleteByCustomer(Customer customer);

    List<CustomerNote> findAllIncompleteByAdminOrderByDueDateAsc(List<User> adminStaff);

    List<CustomerNote> findTop3ByUtilityContractOrderByDateCreatedDesc(UtilityContract contract);

    List<CustomerNote> findByUtilityContractOrderByDateCreatedDesc(UtilityContract utilityContract);

    List<CustomerNote> findByMerchantServicesContractOrderByDateCreatedDesc(MerchantServicesContract merchantServicesContract);
    int countTaggedNotesByUser(User user);

    List<CustomerNote> findByTaggedUser(User user);
}
