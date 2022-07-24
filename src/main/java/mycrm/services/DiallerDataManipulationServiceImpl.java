package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.Contact;
import mycrm.models.Customer;
import mycrm.models.EnergyContract;
import mycrm.models.Plugin;
import mycrm.models.User;
import mycrm.upload.DiallerContact;
import mycrm.upload.DiallerExportEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Primary
public class DiallerDataManipulationServiceImpl implements DiallerDataManipulationService {
    private static Logger logger = LogManager.getLogger();

    private final PluginService pluginService;

    private final UserService userService;
    private final ElecContractService elecContractService;
    private final GasContractService gasContractService;
    private final ContactService contactService;
    @Value("${dialler.provider.name}")
    private String diallerProviderName;

    @Autowired
    public DiallerDataManipulationServiceImpl(UserService userService,
                                              ElecContractService elecContractService,
                                              GasContractService gasContractService,
                                              ContactService contactService, PluginService pluginService) {
        this.userService = userService;
        this.elecContractService = elecContractService;
        this.gasContractService = gasContractService;
        this.contactService = contactService;
        this.pluginService = pluginService;
    }


    @Override
    public Set<DiallerExportEntity> getAllDiallerData() {
        List<User> agent = userService.findAllActiveUsers();

        Set<DiallerExportEntity> diallerEntities = new HashSet<>();
        agent.forEach(user -> {
            // check if they're a broker
            if (user.getBroker() != null && StringUtils.hasText(user.getDiallerAgentReference())) {
                logger.info("Getting Dialler CALLBACKS for: {}", user.getDiallerAgentReference());
                diallerEntities.addAll(getAllCallbacks(user.getBroker(), user.getDiallerAgentReference()));
            }
        });

        logger.info("Getting Dialler POTENTIALS");
        diallerEntities.addAll(getAllPotentials());

        if (diallerEntities.isEmpty()) {
            logger.info("Full data export list was empty.");
        }

        return diallerEntities;
    }

    @Override
    public Set<DiallerExportEntity> getLatestDiallerData() {
        List<User> agent = userService.findAllActiveUsers();

        Set<DiallerExportEntity> latestDiallerEntities = new HashSet<>();
        agent.forEach(user -> {
            // check if they're a broker
            if (user.getBroker() != null && StringUtils.hasText(user.getDiallerAgentReference())) {
                logger.info("Getting latest CALLBACKS");
                latestDiallerEntities.addAll(getLatestCallbacks(user.getBroker(), user.getDiallerAgentReference()));
            }
        });

        logger.info("Getting latest Dialler POTENTIALS");

        latestDiallerEntities.addAll(getLatestPotentials());

        if (latestDiallerEntities.isEmpty()) {
            logger.info("No latest Dialler data to upload.");
        }

        return latestDiallerEntities;
    }

    private Set<DiallerExportEntity> getLatestCallbacks(Broker broker, String diallerAgentReference) {
        Set<DiallerExportEntity> diallerCallbacks = new HashSet<>();

        // CALLBACKS
        gasContractService.findAllLatestContactableDiallerGasContracts("CALLBACK", broker).forEach(gasCustomerContract -> {
            diallerCallbacks.addAll(convertToDiallerExportEntity(gasCustomerContract, diallerAgentReference));
        });
        elecContractService.findAllLatestContactableDiallerElectricContracts("CALLBACK", broker).forEach(electricCustomerContract -> {
            diallerCallbacks.addAll(convertToDiallerExportEntity(electricCustomerContract, diallerAgentReference));
        });

        return diallerCallbacks;
    }

    private Set<DiallerExportEntity> getLatestPotentials() {
        Set<DiallerExportEntity> diallerPotentials = new HashSet<>();
        // POTENTIALS
        gasContractService.findAllLatestContactableDiallerGasContracts("POTENTIAL", null).forEach(gasCustomerContract -> {
            diallerPotentials.addAll(convertToDiallerExportEntity(gasCustomerContract, ""));
        });
        elecContractService.findAllLatestContactableDiallerElectricContracts("POTENTIAL", null).forEach(electricCustomerContract -> {
            diallerPotentials.addAll(convertToDiallerExportEntity(electricCustomerContract, ""));
        });

        return diallerPotentials;
    }

    private Set<DiallerExportEntity> getAllCallbacks(Broker broker, String diallerAgentReference) {
        Set<DiallerExportEntity> diallerCallbacks = new HashSet<>();

        // CALLBACKS
        gasContractService.findAllContactableDiallerGasContracts("CALLBACK", broker).forEach(gasCustomerContract -> {
            diallerCallbacks.addAll(convertToDiallerExportEntity(gasCustomerContract, diallerAgentReference));
        });
        elecContractService.findAllContactableDiallerElectricContracts("CALLBACK", broker).forEach(electricCustomerContract -> {
            diallerCallbacks.addAll(convertToDiallerExportEntity(electricCustomerContract, diallerAgentReference));
        });

        return diallerCallbacks;
    }

    private Set<DiallerExportEntity> getAllPotentials() {
        Set<DiallerExportEntity> diallerPotentials = new HashSet<>();
        // POTENTIALS
        gasContractService.findAllContactableDiallerGasContracts("POTENTIAL", null).forEach(gasCustomerContract -> {
            diallerPotentials.addAll(convertToDiallerExportEntity(gasCustomerContract, ""));
        });
        elecContractService.findAllContactableDiallerElectricContracts("POTENTIAL", null).forEach(electricCustomerContract -> {
            diallerPotentials.addAll(convertToDiallerExportEntity(electricCustomerContract, ""));
        });

        return diallerPotentials;
    }

    private Set<DiallerExportEntity> convertToDiallerExportEntity(EnergyContract energyContract, String diallerAgentReference) {

        Set<DiallerExportEntity> diallerExportEntities = new HashSet<>();

        // first build an entity from the customer details
        Customer customer = energyContract.getCustomer();
        if (StringUtils.hasText(customer.getMobileNumber()) || StringUtils.hasText(customer.getContactNumber())) {
            diallerExportEntities.add(buildDiallerExportEntity(energyContract, diallerAgentReference, new DiallerContact(customer.getFirstName(), customer.getLastName(),
                    customer.getContactNumber(), customer.getMobileNumber())));
        }

        // now go through the list of contacts and use their name
        List<Contact> contactList = contactService.findByCustomer(customer);
        contactList.forEach(contact -> {
            if (StringUtils.hasText(contact.getMobileNumber()) || StringUtils.hasText(contact.getContactNumber())) {
                diallerExportEntities.add(buildDiallerExportEntity(energyContract, diallerAgentReference,
                        new DiallerContact(extractFirstNames(contact.getContactName()), extractLastName(contact.getContactName()), contact.getContactNumber(),
                                contact.getMobileNumber())));
            }
        });

        return diallerExportEntities;
    }

    private DiallerExportEntity buildDiallerExportEntity(EnergyContract energyContract, String diallerAgentReference, DiallerContact diallerContact) {
        Plugin plugin = pluginService.getPluginByApi(diallerProviderName);
        String primaryDataset = plugin.getPrimaryDataset();

        DiallerExportEntity diallerExportEntity = new DiallerExportEntity();
        diallerExportEntity.setUniqueId(validateString(energyContract.getId().toString()));
        diallerExportEntity.setContractType(validateString(energyContract.getSupplyType()));
        diallerExportEntity.setLeadOwner("");
        diallerExportEntity.setSalutation("");
        diallerExportEntity.setFirstName(validateString(diallerContact.getFirstName()));
        diallerExportEntity.setLastName(validateString(diallerContact.getLastName()));
        diallerExportEntity.setPhone(cleanNumber(diallerContact.getContactNumber()));
        diallerExportEntity.setMobile(cleanNumber(diallerContact.getMobileNumber()));
        diallerExportEntity.setAddr1(validateString(energyContract.getCustomerSite().getSiteAddr()));
        diallerExportEntity.setAddr2(validateString(energyContract.getCustomerSite().getSiteAddr1()));
        diallerExportEntity.setCity(validateString(energyContract.getCustomerSite().getSiteCity()));
        diallerExportEntity.setCountry("United Kingdom");
        diallerExportEntity.setPostCode(mergePostCode(energyContract));
        diallerExportEntity.setEmail("");
        diallerExportEntity.setCompany(validateString(energyContract.getCustomer().getBusinessName()));
        diallerExportEntity.setLeadSource("");
        diallerExportEntity.setLeadStatus(energyContract.getLogType());
        diallerExportEntity.setCustomerReference(validateString(energyContract.getCustomer().getId().toString()));
        if (energyContract.getLogType().equals("CALLBACK")) {
            diallerExportEntity.setOutcomeCode("109");
            diallerExportEntity.setAgentSpecific("1");
            diallerExportEntity.setAgentRef(diallerAgentReference);
            diallerExportEntity.setCallback(convertDateToString(energyContract.getCallbackDateTime()));
        } else {
            diallerExportEntity.setOutcomeCode("");
            diallerExportEntity.setAgentSpecific("");
            diallerExportEntity.setAgentRef("");
            diallerExportEntity.setCallback("");
        }

        diallerExportEntity.setCrmLink(buildCrmLink(energyContract));
        diallerExportEntity.setCampaign("BUKCommunicationsTAClUC");
        diallerExportEntity.setDataset(primaryDataset);
        diallerExportEntity.setDateCreated(LocalDateTime.now());

        // logger.info("Dialler Entity {}", diallerExportEntity.toString());

        return diallerExportEntity;
    }

    private String extractFirstNames(String stContactName) {

        if (StringUtils.hasText(stContactName)) {
            StringBuilder firstNames = new StringBuilder();

            String[] splitContactName = stContactName.trim().split(" ");
            int lengthOfContactName = splitContactName.length;

            for (int i = 0; i < (lengthOfContactName - 1); i++) {
                firstNames.append(splitContactName[i] + " ");
            }

            return validateString(firstNames.toString());
        }
        return null;
    }

    private String extractLastName(String stContactName) {
        if (StringUtils.hasText(stContactName)) {
            String lastName = null;

            String[] splitContactName = stContactName.trim().split(" ");
            int lengthOfContactName = splitContactName.length;

            for (int i = 0; i < lengthOfContactName; i++) {
                lastName = splitContactName[i];
            }

            return validateString(lastName);
        }
        return null;
    }

    private String buildCrmLink(EnergyContract energyContract) {
        if (energyContract.getLogType().equals("CALLBACK")) {
            if (energyContract.getSupplyType().equals("GAS")) {
                return "http://mbserver:5668/admin/customer/editgascontract/" + energyContract.getId();
            }
            if (energyContract.getSupplyType().equals("ELEC")) {
                return "http://mbserver:5668/admin/customer/editeleccontract/" + energyContract.getId();
            }
        }
        if (energyContract.getLogType().equals("POTENTIAL")) {
            return "http://mbserver:5668/admin/customer/view/" + energyContract.getCustomer().getId();
        }
        return "";
    }

    private String convertDateToString(Date callbackDateTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (callbackDateTime != null) {
            return dateFormat.format(callbackDateTime);
        }
        return "";
    }

    private String validateString(String str) {
        if (StringUtils.hasText(str)) {
            return str;
        }
        return "";
    }

    private String cleanNumber(String str) {
        if (StringUtils.hasText(str)) {
            String cleanedNumber = str;

            cleanedNumber = cleanedNumber.replaceAll(" ", "");
            cleanedNumber = cleanedNumber.replaceAll("\\+44", "0");

            // check if it's a number
            try {
                Double.parseDouble(cleanedNumber);
            } catch (NumberFormatException nfe) {
                return "";
            }

            if (cleanedNumber.startsWith("1")) {
                cleanedNumber = "0" + cleanedNumber;
//                01	Geographic area codes.
            }

            if (cleanedNumber.startsWith("2")) {
                cleanedNumber = "0" + cleanedNumber;
//                02	Geographic area codes (introduced in 2000).
            }

            if (cleanedNumber.startsWith("3")) {
                cleanedNumber = "0" + cleanedNumber;
//                03	Non-geographic numbers charged at standard geographic area code rates (introduced 2007).
            }

            if (cleanedNumber.startsWith("4")) {
                cleanedNumber = "0" + cleanedNumber;
//                 04 Reserved.
            }

            if (cleanedNumber.startsWith("5")) {
                cleanedNumber = "0" + cleanedNumber;
//                 05 Corporate numbering and VoIP services(some VoIP services use 08or geographic numbers).
//                 Freephone(toll free) on 0500 until June 2017.
            }

            if (cleanedNumber.startsWith("6")) {
                cleanedNumber = "0" + cleanedNumber;
//                 06 Was reserved for possible use by personal numbering(PNS) instead of 070
//                 following consumer confusion with mobile phones.
            }

            if (cleanedNumber.startsWith("7")) {
                cleanedNumber = "0" + cleanedNumber;
//                07 Mostly for mobile phones on 071 xx, 072 xx, 073 xx, 074 xx, 075 xx, 07624, 077 xx, 078 xx,
//                and 079 xx.Personal numbering on 070. Pagers on 076 xx.
            }

            if (cleanedNumber.startsWith("8")) {
                cleanedNumber = "0" + cleanedNumber;
//                08 Freephone(toll free) on 080, and Special Services (formerly known as local and national rate)
//                on 084 and 087.
            }

            if (cleanedNumber.startsWith("9")) {
                cleanedNumber = "0" + cleanedNumber;
//                09 Premium Rate services(PRS).
            }

//            00 International not formatted

            return cleanedNumber;
        }
        return "";
    }

    private String mergePostCode(EnergyContract energyContract) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.hasText(energyContract.getCustomerSite().getSitePostcodeOut())) {
            stringBuilder.append(energyContract.getCustomerSite().getSitePostcodeOut());
        }
        if (StringUtils.hasText(energyContract.getCustomerSite().getSitePostcodeIn())) {
            stringBuilder.append(energyContract.getCustomerSite().getSitePostcodeIn());
        }
        return stringBuilder.toString();
    }
}
