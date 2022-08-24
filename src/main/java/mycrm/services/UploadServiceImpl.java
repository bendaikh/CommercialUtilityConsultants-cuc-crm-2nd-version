package mycrm.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import mycrm.exceptions.ImportTooLargeException;
import mycrm.models.Customer;
import mycrm.models.CustomerSite;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.Supplier;
import mycrm.upload.CustomerImportCsvWriter;
import mycrm.upload.CustomerImportEntity;
import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Service
@Primary
public class UploadServiceImpl implements UploadService {

    private static final Logger logger = LogManager.getLogger();
    //    private final Pattern firstPart = Pattern.compile("[A-Z]{1,2}[0-9R][0-9A-Z]?");
//    private final Pattern secondPart = Pattern.compile("[0-9][A-Z-[CIKMOV]]{2}");
    private final CustomerService customerService;
    private final CustomerSiteService customerSiteService;
    private final SupplierService supplierService;

    private final NewCustomerService newCustomerService;
    private final BrokerService brokerService;
    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final CustomerImportCsvWriter customerImportCsvWriter;


    @Value("${default.broker.id}")
    private Long defaultBrokerId;
    @Value("${default.supplier.id}")
    private Long defaultSupplierId;
    @Value("${max.import.size}")
    private Long maxImportSize;

    @Autowired
    public UploadServiceImpl(NewCustomerService newCustomerService, CustomerService customerService, CustomerSiteService customerSiteService, SupplierService supplierService,
                             BrokerService brokerService, GasContractService gasContractService, ElecContractService elecContractService,
                             CustomerImportCsvWriter customerImportCsvWriter) {
        this.newCustomerService = newCustomerService;
        this.customerService = customerService;
        this.customerSiteService = customerSiteService;
        this.supplierService = supplierService;
        this.brokerService = brokerService;
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.customerImportCsvWriter = customerImportCsvWriter;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws Exception {

        File file = convertMultiPartToFile(multipartFile);

        List<CustomerImportEntity> unsuccessfulImports = new ArrayList<>();

        try (Reader reader = new FileReader(file)) {

            CsvToBean<CustomerImportEntity> csvToBean = new CsvToBeanBuilder<CustomerImportEntity>(reader)
                    .withType(CustomerImportEntity.class)
                    .build();

            List<CustomerImportEntity> customerList = csvToBean.parse();

            if (customerList.size() <= maxImportSize) {

                Iterator<CustomerImportEntity> customerEntityClone = customerList.iterator();

                while (customerEntityClone.hasNext()) {
                    CustomerImportEntity customerEntity = customerEntityClone.next();

//                logger.info("Business Name: {}", customerEntity.getBusinessName());
//                logger.info("Address Line 1: {}", customerEntity.getBusinessAddressOne());
//                logger.info("Address Line 2: {}", customerEntity.getBusinessAddressTwo());
//                logger.info("City: {}", customerEntity.getBusinessCity());
//                logger.info("Postcode: {}", customerEntity.getBusinessPostcode());
//                logger.info("LTD Registration Number: {}", customerEntity.getLtdRegistrationNumber());
//                logger.info("Company Registration Number: {}", customerEntity.getCompanyRegistrationNumber());
//                logger.info("Sole Trader: {}", customerEntity.getSoleTrader());
//                logger.info("ST Contact Name: {}", customerEntity.getStContactName());
//                logger.info("ST Address Line 1: {}", customerEntity.getStAddressOne());
//                logger.info("ST Address Line 2: {}", customerEntity.getStAddressTwo());
//                logger.info("ST City: {}", customerEntity.getStCity());
//                logger.info("ST Postcode: {}", customerEntity.getStPostcode());
//                logger.info("Contact Number: {}", customerEntity.getContactNumber());
//                logger.info("Mobile Number: {}", customerEntity.getMobileNumber());
//                logger.info("Email Address: {}", customerEntity.getEmailAddress());
//                logger.info("MPR: {}", customerEntity.getMpr());
//                logger.info("MPAN Line One: {}", customerEntity.getMpanLineOne());
//                logger.info("MPAN Line Two: {}", customerEntity.getMpanLineTwo());
//                logger.info("Gas Supplier: {}", customerEntity.getGasSupplier());
//                logger.info("Gas Unit Rate: {}", customerEntity.getGasUnitRate());
//                logger.info("Gas Standing Charge: {}", customerEntity.getGasStandingCharge());
//                logger.info("Gas Standing Charge Frequency: {}", customerEntity.getGasStandingChargeFrequency());
//                logger.info("Gas Start Date: {}", convertStringToDate(customerEntity.getGasStartDate()));
//                logger.info("Gas End Date: {}", customerEntity.getGasEndDate());
//                logger.info("Gas Estimated Annual Quantity: {}", customerEntity.getGasEstimatedAnnualQuantity());
//                logger.info("Electricity Supplier: {}", customerEntity.getElectricitySupplier());
//                logger.info("Electricity Day Rate: {}", customerEntity.getElectricityDayRate());
//                logger.info("Electricity Night Rate: {}", customerEntity.getElectricityNightRate());
//                logger.info("Electricity Evening/Weekend Rate: {}", customerEntity.getElectricityEveningWeekendRate());
//                logger.info("Electricity Feed In Tariff: {}", customerEntity.getElectricityFeedInTariff());
//                logger.info("Electricity Standing Charge: {}", customerEntity.getElectricityStandingCharge());
//                logger.info("Electricity Standing Charge Frequency: {}", customerEntity.getElectricityStandingChargeFrequency());
//                logger.info("Electricity Start Date: {}", customerEntity.getElectricityStartDate());
//                logger.info("Electricity End Date: {}", customerEntity.getElectricityEndDate());
//                logger.info("Electricity Estimated Annual Quantity: {}", customerEntity.getElectricityEstimatedAnnualQuantity());
                    logger.info("Campaign: {}", formatString(customerEntity.getCampaign()));

                    //search by business name and postcode first before creating new dude
                    try {

                        Customer searchedCustomer =
                                customerService.findByBusinessNameAndBusinessPostcodeOutAndBusinessPostcodeIn(
                                        customerEntity.getBusinessName(),
                                        getFirstPartPostcode(customerEntity.getBusinessPostcode()),
                                        getSecondPartPostcode(customerEntity.getBusinessPostcode()));

                        if (searchedCustomer == null) {
                            Customer savedCustomer = createNewCustomer(customerEntity);
                            newCustomerService.updateCustomerReferenceNumber(savedCustomer);

                            CustomerSite savedCustomerSite = createNewCustomerSite(savedCustomer, customerEntity);

                            if (StringUtils.hasText(customerEntity.getGasMeterUtility())) {
                                createGasContract(savedCustomer, savedCustomerSite, customerEntity);
                            }
                            if (StringUtils.hasText(customerEntity.getElectricMeterUtility())) {
                                createElectricityContract(savedCustomer, savedCustomerSite, customerEntity);
                            }

                        } else {
                            logger.info("Duplicate customer found {} so adding to unsuccessful import list", customerEntity.getBusinessName());
                            customerEntity.setErrorMessage("DUPLICATE");
                            unsuccessfulImports.add(customerEntity);
                        }


                    } catch (Exception e) {
                        logger.info("Error uploading {} so adding to unsuccessful import list, error={}", customerEntity.getBusinessName(), e);
                        customerEntity.setErrorMessage(e.getMessage());
                        unsuccessfulImports.add(customerEntity);
                    }
                }

            } else {
                throw new ImportTooLargeException("Import spreadsheet shouldn't be more than " + maxImportSize + " records");
            }

        }

        String linkToImports = customerImportCsvWriter.buildCsv(unsuccessfulImports);

        return linkToImports;
    }

    private Customer createNewCustomer(CustomerImportEntity customerEntity) {
        Customer newCustomer = new Customer();
        newCustomer.setBusinessName(formatString(customerEntity.getBusinessName()));
        newCustomer.setBusinessAddr(formatString(customerEntity.getBusinessAddressOne()));
        newCustomer.setBusinessAddr1(formatString(customerEntity.getBusinessAddressTwo()));
        newCustomer.setBusinessCity(formatString(customerEntity.getBusinessCity()));
        newCustomer.setBusinessPostcodeOut(getFirstPartPostcode(customerEntity.getBusinessPostcode()));
        newCustomer.setBusinessPostcodeIn(getSecondPartPostcode(customerEntity.getBusinessPostcode()));
        newCustomer.setLtdRegNo(customerEntity.getLtdRegistrationNumber());
        newCustomer.setSoleTrader(isSoleTrader(customerEntity.getSoleTrader()));
        newCustomer.setFirstName(extractFirstNames(customerEntity.getStContactName()));
        newCustomer.setLastName(extractLastName(customerEntity.getStContactName()));
        newCustomer.setStAddr(formatString(customerEntity.getStAddressOne()));
        newCustomer.setStAddr1(formatString(customerEntity.getStAddressTwo()));
        newCustomer.setStCity(formatString(customerEntity.getStCity()));
        newCustomer.setStPostcodeOut(getFirstPartPostcode(customerEntity.getStPostcode()));
        newCustomer.setStPostcodeIn(getSecondPartPostcode(customerEntity.getStPostcode()));
        newCustomer.setContactNumber(customerEntity.getContactNumber());
        newCustomer.setMobileNumber(customerEntity.getMobileNumber());
        newCustomer.setEmailAddress(customerEntity.getEmailAddress());

        return customerService.save(newCustomer);
    }


    private CustomerSite createNewCustomerSite(Customer customer, CustomerImportEntity customerEntity) {

        String mpanLineOne = customerEntity.getMpanLineOne();

        String mpanLineOnePartOne = (StringUtils.hasText(mpanLineOne) && mpanLineOne.substring(0, 1) != null ? mpanLineOne.substring(0, 1) : "");
        String mpanLineOnePartTwo = (StringUtils.hasText(mpanLineOne) && mpanLineOne.substring(2, 3) != null ? mpanLineOne.substring(2, 3) : "");
        String mpanLineOnePartThree = (StringUtils.hasText(mpanLineOne) && mpanLineOne.substring(4, 6) != null ? mpanLineOne.substring(4, 6) : "");

        CustomerSite customerSite = new CustomerSite();
        customerSite.setCustomer(customer);
        customerSite.setSiteName(customer.getBusinessName());
        customerSite.setSiteAddr(customer.getBusinessAddr());
        customerSite.setSiteAddr1(customer.getBusinessAddr1());
        customerSite.setSiteCity(customer.getBusinessCity());
        customerSite.setSitePostcodeIn(customer.getBusinessPostcodeIn());
        customerSite.setSitePostcodeOut(customer.getBusinessPostcodeOut());
        customerSite.setSiteTelephone(customer.getContactNumber());
        customerSite.setMpr(customerEntity.getMpr());
        customerSite.setMpanLineOne(mpanLineOnePartOne);
        customerSite.setMpanLineTwo(mpanLineOnePartTwo);
        customerSite.setMpanLineThree(mpanLineOnePartThree);
        customerSite.setMpanBottomLine(customerEntity.getMpanLineTwo());
        customerSite.setPropertyType("COMMERCIAL");

        return customerSiteService.save(customerSite);
    }

    private ElecCustomerContract createElectricityContract(Customer customer, CustomerSite savedCustomerSite, CustomerImportEntity customerEntity) {
        String mpanLineOne = customerEntity.getMpanLineOne();
        String mpanBottomLine = StringUtils.hasText(customerEntity.getMpanLineTwo()) ? customerEntity.getMpanLineTwo() : "Pending";

        String mpanLineOnePartOne = (StringUtils.hasText(mpanLineOne) && mpanLineOne.substring(0, 1) != null ? mpanLineOne.substring(0, 1) : "");
        String mpanLineOnePartTwo = (StringUtils.hasText(mpanLineOne) && mpanLineOne.substring(2, 3) != null ? mpanLineOne.substring(2, 3) : "");
        String mpanLineOnePartThree = (StringUtils.hasText(mpanLineOne) && mpanLineOne.substring(4, 6) != null ? mpanLineOne.substring(4, 6) : "");

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setAccountNumber(mpanBottomLine); //MPAN Bottom Line
        elecCustomerContract.setCustomer(customer);
        elecCustomerContract.setCustomerSite(savedCustomerSite);
        elecCustomerContract.setLogType("POTENTIAL");
        elecCustomerContract.setSupplier(getSupplier(customerEntity.getElectricitySupplier()));
        elecCustomerContract.setBroker(brokerService.findById(defaultBrokerId));
        elecCustomerContract.setUnitRate(convertStringToDouble(customerEntity.getElectricityFeedInTariff()));
        elecCustomerContract.setDayRate(convertStringToDouble(customerEntity.getElectricityDayRate()));
        elecCustomerContract.setEveningWeekendRate(convertStringToDouble(customerEntity.getElectricityEveningWeekendRate()));
        elecCustomerContract.setNightRate(convertStringToDouble(customerEntity.getElectricityNightRate()));
        elecCustomerContract.setStandingCharge(convertStringToDouble(customerEntity.getElectricityStandingCharge()));
        elecCustomerContract.setStandingChargeFrequency(customerEntity.getElectricityStandingChargeFrequency());
        elecCustomerContract.setStartDate(convertStringToDate(customerEntity.getElectricityStartDate()));
        elecCustomerContract.setEndDate(convertStringToDate(customerEntity.getElectricityEndDate()));
        elecCustomerContract.setEstimatedAnnualQuantity(convertStringToLong(customerEntity.getElectricityEstimatedAnnualQuantity()));
        elecCustomerContract.setCampaign(formatString(customerEntity.getCampaign()));
        elecCustomerContract.setMpanLineOne(mpanLineOnePartOne);
        elecCustomerContract.setMpanLineTwo(mpanLineOnePartTwo);
        elecCustomerContract.setMpanLineThree(mpanLineOnePartThree);

        return elecContractService.save(elecCustomerContract);
    }

    private GasCustomerContract createGasContract(Customer customer, CustomerSite savedCustomerSite, CustomerImportEntity customerEntity) {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();

        String mpr = (StringUtils.hasText(customerEntity.getMpr()) ? customerEntity.getMpr() : "Pending");

        gasCustomerContract.setAccountNumber(mpr);
        gasCustomerContract.setCustomer(customer);
        gasCustomerContract.setCustomerSite(savedCustomerSite);
        gasCustomerContract.setLogType("POTENTIAL");
        gasCustomerContract.setSupplier(getSupplier(customerEntity.getGasSupplier()));
        gasCustomerContract.setBroker(brokerService.findById(defaultBrokerId));
        gasCustomerContract.setUnitRate(convertStringToDouble(customerEntity.getGasUnitRate()));
        gasCustomerContract.setStandingCharge(convertStringToDouble(customerEntity.getGasStandingCharge()));
        gasCustomerContract.setStandingChargeFrequency(customerEntity.getGasStandingChargeFrequency());
        gasCustomerContract.setStartDate(convertStringToDate(customerEntity.getGasStartDate()));
        gasCustomerContract.setEndDate(convertStringToDate(customerEntity.getGasEndDate()));
        gasCustomerContract.setEstimatedAnnualQuantity(convertStringToLong(customerEntity.getGasEstimatedAnnualQuantity()));
        gasCustomerContract.setCampaign(formatString(customerEntity.getCampaign()));

        return gasContractService.save(gasCustomerContract);
    }

    private String formatString(String value) {

        if (StringUtils.hasText(value)) {
            String formattedValue;

            formattedValue = value.trim();
            formattedValue = WordUtils.capitalizeFully(formattedValue);

            return formattedValue;
        }
        return null;
    }

    private String extractFirstNames(String stContactName) {

        if (StringUtils.hasText(stContactName)) {
            StringBuilder firstNames = new StringBuilder();

            String[] splitContactName = stContactName.trim().split(" ");
            int lengthOfContactName = splitContactName.length;

            for (int i = 0; i < (lengthOfContactName - 1); i++) {
                firstNames.append(splitContactName[i] + " ");
            }

            return formatString(firstNames.toString());
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

            return formatString(lastName);
        }
        return null;
    }

    private String getFirstPartPostcode(String postcode) {
        if (StringUtils.hasText(postcode) && StringUtils.hasText(postcode.split(" ")[0])) {
            String firstPartPostcode = postcode.split(" ")[0];
            firstPartPostcode = firstPartPostcode.toUpperCase();
            firstPartPostcode = firstPartPostcode.substring(0, Math.min(firstPartPostcode.length(), 4));

            return firstPartPostcode;
        }
        return "";
    }

    private String getSecondPartPostcode(String postcode) {
        try {
            if (StringUtils.hasText(postcode)) {
                if (StringUtils.hasText(postcode.split(" ")[1])) {
                    String secondPartPostcode = postcode.split(" ")[1];
                    secondPartPostcode = secondPartPostcode.toUpperCase();
                    secondPartPostcode = secondPartPostcode.substring(0, Math.min(secondPartPostcode.length(), 4));

                    return secondPartPostcode;
                }
                return "";
            }
        } catch (Exception e) {
            return "";
        }

        return "";
    }

    private boolean isSoleTrader(Boolean soleTrader) {
        return soleTrader != null && soleTrader;
    }

    private Date convertStringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Date parsedDate = null;

        if (StringUtils.hasText(date)) {
            try {
                parsedDate = formatter.parse(date);
            } catch (ParseException e) {
                logger.info("Error converting string to date {}", e);
            }
        }

        return parsedDate;
    }

    private Supplier getSupplier(String supplierName) {
        if (StringUtils.hasText(supplierName)) {
            if (supplierService.findByBusinessName(supplierName.trim()) != null) {
                return supplierService.findByBusinessName(supplierName.trim());
            }
        }

        return supplierService.findById(defaultSupplierId);
    }


    private long convertStringToLong(String value) {
        return StringUtils.hasText(value) ? Long.parseLong(value.replaceAll(",", "")) : 0L;
    }

    private double convertStringToDouble(String value) {
        return StringUtils.hasText(value) ? Double.parseDouble(value) : 0.0;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {

        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
