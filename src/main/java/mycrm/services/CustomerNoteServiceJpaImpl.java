package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.CustomerNote;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.User;
import mycrm.models.UtilityContract;
import mycrm.repositories.CustomerNoteRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Primary
public class CustomerNoteServiceJpaImpl implements CustomerNoteService {

    private final CustomerNoteRepository customerNotesRepo;

    @Autowired
    public CustomerNoteServiceJpaImpl(CustomerNoteRepository customerNotesRepo) {
        this.customerNotesRepo = customerNotesRepo;
    }

    @Override
    public List<CustomerNote> findAll() {
        return this.customerNotesRepo.findAll();
    }

    @Override
    public List<CustomerNote> findLatest5() {
        return this.customerNotesRepo.findLatest5Posts(new PageRequest(0, 5));
    }

    @Override
    public CustomerNote findById(Long id) {
        return this.customerNotesRepo.findOne(id);
    }

    @Override
    public CustomerNote save(CustomerNote customerNote) {
        return this.customerNotesRepo.save(customerNote);
    }

    @Override
    public CustomerNote edit(CustomerNote customerNote) {
        return this.customerNotesRepo.save(customerNote);
    }

    @Override
    public void deleteById(Long id) {
        this.customerNotesRepo.delete(id);
    }

    @Override
    public List<CustomerNote> findByCustomer(Customer customer) {
        return this.customerNotesRepo.findByCustomer(customer);
    }

    @Override
    public List<CustomerNote> findByCustomerOrderByDateCreatedDesc(Customer customer) {
        return this.customerNotesRepo.findByCustomerOrderByDateCreatedDesc(customer);
    }

    @Override
    public List<CustomerNote> findAllIncompleteByTaggedUser(User taggedUser) {
        return this.customerNotesRepo.findAllIncompleteByTaggedUser(taggedUser);
    }

    @Override
    public List<CustomerNote> findAllIncompleteByTaggedUserOrderByDueDateAsc(User taggedUser) {
        return this.customerNotesRepo.findAllIncompleteByTaggedUserOrderByDueDateAsc(taggedUser);
    }

    @Override
    public void updateCustomerNote(long id, User completedBy) {
        DateTime dateTime = new org.joda.time.DateTime();

        this.customerNotesRepo.updateCustomerNote(id, dateTime.toDate(), completedBy);
    }

    @Override
    public List<CustomerNote> findByGasCustomerContractOrderByDateCreatedDesc(GasCustomerContract gasCustomerContract) {
        return this.customerNotesRepo.findByGasCustomerContractOrderByDateCreatedDesc(gasCustomerContract);
    }

    @Override
    public List<CustomerNote> findByElecCustomerContractOrderByDateCreatedDesc(ElecCustomerContract elecCustomerContract) {
        return this.customerNotesRepo.findByElecCustomerContractOrderByDateCreatedDesc(elecCustomerContract);
    }

    @Override
    public List<CustomerNote> findTop3ByGasCustomerContractOrderByDateCreatedDesc(GasCustomerContract gasCustomerContract) {
        Pageable pageable = new PageRequest(0, 3);
        return this.customerNotesRepo.findTop3ByGasCustomerContractOrderByDateCreatedDesc(gasCustomerContract, pageable);
    }

    @Override
    public List<CustomerNote> findTop3ByElecCustomerContractOrderByDateCreatedDesc(ElecCustomerContract elecCustomerContract) {
        Pageable pageable = new PageRequest(0, 3);
        return this.customerNotesRepo.findTopByElecCustomerContractOrderByDateCreatedDesc(elecCustomerContract, pageable);
    }

    @Override
    public void deleteByCustomer(Customer customer) {
        this.customerNotesRepo.deleteByCustomer(customer);
    }

    @Override
    public List<CustomerNote> findAllIncompleteByAdminOrderByDueDateAsc(List<User> adminStaff) {
        List<CustomerNote> customerNotes = new ArrayList<>();
        adminStaff.forEach(admin -> customerNotes.addAll(this.customerNotesRepo.findAllIncompleteByTaggedUserOrderByDueDateAsc(admin)));
        customerNotes.sort(Comparator.comparing(CustomerNote::getDueByDate, Comparator.nullsLast(Comparator.naturalOrder())));
        return customerNotes;
    }

    @Override
    public List<CustomerNote> findTop3ByUtilityContractOrderByDateCreatedDesc(UtilityContract contract) {
        Pageable pageable = new PageRequest(0, 3);
        return this.customerNotesRepo.findTopByUtilityContractOrderByDateCreatedDesc(contract, pageable);
    }

    @Override
    public List<CustomerNote> findByUtilityContractOrderByDateCreatedDesc(UtilityContract utilityContract) {
        return this.customerNotesRepo.findByUtilityContractOrderByDateCreatedDesc(utilityContract);
    }

    @Override
    public int countTaggedNotesByUser(User user) {
        return this.customerNotesRepo.countTaggedNotesByUser(user);
    }

    @Override
    public List<CustomerNote> findByTaggedUser(User user) {
        return this.customerNotesRepo.findByTaggedUser(user);
    }
}
