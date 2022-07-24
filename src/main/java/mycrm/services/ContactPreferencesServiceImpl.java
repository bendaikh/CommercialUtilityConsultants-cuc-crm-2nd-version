package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.DoNotContactNumber;
import mycrm.repositories.DoNotContactNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Primary
public class ContactPreferencesServiceImpl implements ContactPreferencesService {

    private final DoNotContactNumberRepository doNotContactNumberRepository;

    @Autowired
    public ContactPreferencesServiceImpl(DoNotContactNumberRepository doNotContactNumberRepository) {
        this.doNotContactNumberRepository = doNotContactNumberRepository;
    }

    @Override
    public List<DoNotContactNumber> findByNumber(String number) {
        return this.doNotContactNumberRepository.findByNumber(number);
    }

    @Override
    public Set<DoNotContactNumber> checkCustomer(Customer customer) {
        List<DoNotContactNumber> doNotContactNumbers = new ArrayList<>();
        if (StringUtils.hasText(customer.getContactNumber())) {
            doNotContactNumbers.addAll(findByNumber(customer.getContactNumber()));
        }
        if (StringUtils.hasText(customer.getMobileNumber())) {
            doNotContactNumbers.addAll(findByNumber(customer.getMobileNumber()));
        }
        return new HashSet(doNotContactNumbers);
    }
}
