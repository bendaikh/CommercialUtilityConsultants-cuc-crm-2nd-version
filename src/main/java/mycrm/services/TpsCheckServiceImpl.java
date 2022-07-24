package mycrm.services;

import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.models.TpsCheck;
import mycrm.models.TpsContact;
import mycrm.repositories.TpsCheckRepository;
import mycrm.rest.clients.TpsCheckRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Primary
public class TpsCheckServiceImpl implements TpsCheckService {
    private final TpsCheckRestClient tpsCheckRestClient;
    private final TpsCheckRepository tpsCheckRepository;
    private final ContactService contactService;

    @Autowired
    public TpsCheckServiceImpl(TpsCheckRestClient tpsCheckRestClient, TpsCheckRepository tpsCheckRepository,
                               ContactService contactService) {
        this.tpsCheckRestClient = tpsCheckRestClient;
        this.tpsCheckRepository = tpsCheckRepository;
        this.contactService = contactService;
    }

    @Override
    public void checkCustomerNumbers(Customer customer) {
        if (StringUtils.hasText(customer.getContactNumber())) {
            conductTpsCall(customer.getContactNumber());
        }
        if (StringUtils.hasText(customer.getMobileNumber())) {
            conductTpsCall(customer.getMobileNumber());
        }
    }

    @Override
    public void checkContactNumbers(Contact contact) {
        if (StringUtils.hasText(contact.getContactNumber())) {
            conductTpsCall(contact.getContactNumber());
        }
        if (StringUtils.hasText(contact.getMobileNumber())) {
            conductTpsCall(contact.getMobileNumber());
        }
    }

    private List<TpsCheck> findOkChecksByNumber(String number) {
        // find latest tps check with 200 status code
        return this.tpsCheckRepository.findOkChecksByNumber(number);
    }

    @Override
    public TpsCheck save(TpsCheck tpsCheck) {
        return this.tpsCheckRepository.save(tpsCheck);
    }

    @Override
    public List<TpsCheck> findByCustomer(Customer customer) {
        List<TpsCheck> tpsChecks = new ArrayList<>();

        if (StringUtils.hasText(customer.getMobileNumber())) {
            tpsChecks.addAll(findOkChecksByNumber(customer.getMobileNumber()));
        }
        if (StringUtils.hasText(customer.getContactNumber())) {
            tpsChecks.addAll(findOkChecksByNumber(customer.getContactNumber()));
        }

        List<Contact> contacts = contactService.findByCustomer(customer);
        contacts.forEach(contact -> {
            if (StringUtils.hasText(contact.getContactNumber())) {
                tpsChecks.addAll(findOkChecksByNumber(contact.getContactNumber()));
            }
            if (StringUtils.hasText(contact.getMobileNumber())) {
                tpsChecks.addAll(findOkChecksByNumber(contact.getMobileNumber()));
            }
        });

        // remove duplicates
        Set<TpsCheck> tpsCheckSet = tpsChecks.stream().collect(Collectors.toSet());

        return tpsCheckSet
                .stream()
                .sorted(Comparator.comparing(TpsCheck::getDateCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public TpsCheck findLatestTpsCheck(String number) {
        return this.tpsCheckRepository.findLatestTpsCheck(number);
    }

    @Override
    public boolean customerWithinCheckPeriod(Customer customer) {
        if (StringUtils.hasText(customer.getContactNumber())) {
            TpsCheck latestTpsCheck = findLatestTpsCheck(customer.getContactNumber());
            if (latestTpsCheck != null) {
                return withinCheckPeriod(latestTpsCheck);
            }
        }

        if (StringUtils.hasText(customer.getMobileNumber())) {
            TpsCheck latestTpsCheck = findLatestTpsCheck(customer.getMobileNumber());
            if (latestTpsCheck != null) {
                return withinCheckPeriod(latestTpsCheck);
            }
        }
        return false;
    }

    @Override
    public String customerContactNumberStyle(Customer customer) {
        if (StringUtils.hasText(customer.getContactNumber())) {
            TpsCheck latestTpsCheck = findLatestTpsCheck(customer.getContactNumber());
            if (latestTpsCheck != null) {
                return getStatusStyle(latestTpsCheck.getMessage());
            }
        }
        return getStatusStyle("");
    }

    @Override
    public String customerMobileNumberStyle(Customer customer) {
        if (StringUtils.hasText(customer.getMobileNumber())) {
            TpsCheck latestTpsCheck = findLatestTpsCheck(customer.getMobileNumber());
            if (latestTpsCheck != null) {
                return getStatusStyle(latestTpsCheck.getMessage());
            }
        }
        return getStatusStyle("");
    }

    @Override
    public List<TpsContact> buildContactList(List<Contact> contactList) {
        List<TpsContact> tpsContacts = new ArrayList<>();
        contactList.forEach(contact -> {
            TpsContact tpsContact = new TpsContact();
            tpsContact.setContactNumberStyle(getStatusStyle(""));
            tpsContact.setMobileNumberStyle(getStatusStyle(""));

            tpsContact.setContact(contact);
            if (StringUtils.hasText(contact.getContactNumber())) {
                TpsCheck latestTpsCheck = findLatestTpsCheck(contact.getContactNumber());
                if (latestTpsCheck != null) {
                    tpsContact.setContactNumberTpsCheck(latestTpsCheck);
                    tpsContact.setWithinCheckPeriod(withinCheckPeriod(latestTpsCheck));
                    tpsContact.setContactNumberStyle(getStatusStyle(latestTpsCheck.getMessage()));
                }
            }
            if (StringUtils.hasText(contact.getMobileNumber())) {
                TpsCheck latestTpsCheck = findLatestTpsCheck(contact.getMobileNumber());
                if (latestTpsCheck != null) {
                    tpsContact.setMobileNumberTpsCheck(latestTpsCheck);
                    tpsContact.setWithinCheckPeriod(withinCheckPeriod(latestTpsCheck));
                    tpsContact.setMobileNumberStyle(getStatusStyle(latestTpsCheck.getMessage()));
                }
            }
            tpsContacts.add(tpsContact);
        });
        return tpsContacts;
    }

    private void conductTpsCall(String number) {
        TpsCheck check = tpsCheckRestClient.callTpsApiService(number);
        this.save(check);
    }

    private String getStatusStyle(String message) {
        String style;
        switch (message) {
            case "safe":
                style = "success";
                break;
            case "listed":
                style = "danger";
                break;
            default:
                style = "text-muted";
                break;
        }
        return style;
    }

    // check if its within 28 days else need to do it again
    private boolean withinCheckPeriod(TpsCheck tpsCheck) {
        return tpsCheck.getDateCreated() != null && getNumberOfDays(tpsCheck.getDateCreated()) <= 28;
    }

    private long getNumberOfDays(Date date) {
        LocalDate now = LocalDate.now();
        LocalDate givenDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysInBetween = DAYS.between(givenDate, now);

        return daysInBetween;
    }


}
