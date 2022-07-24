package mycrm.services;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.models.TpsCheck;
import mycrm.models.TpsContact;

import java.util.List;

public interface TpsCheckService {
    void checkCustomerNumbers(Customer customer);

    void checkContactNumbers(Contact contact);

    TpsCheck save(TpsCheck tpsCheck);

    List<TpsCheck> findByCustomer(Customer customer);

    List<TpsContact> buildContactList(List<Contact> contactList);

    TpsCheck findLatestTpsCheck(String number);

    boolean customerWithinCheckPeriod(Customer customer);

    String customerContactNumberStyle(Customer customer);

    String customerMobileNumberStyle(Customer customer);
}
