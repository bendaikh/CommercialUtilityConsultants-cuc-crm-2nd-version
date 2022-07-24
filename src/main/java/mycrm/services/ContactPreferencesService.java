package mycrm.services;

import mycrm.models.Customer;
import mycrm.models.DoNotContactNumber;

import java.util.List;
import java.util.Set;

public interface ContactPreferencesService {
    List<DoNotContactNumber> findByNumber(String number);

    Set<DoNotContactNumber> checkCustomer(Customer customer);
}
