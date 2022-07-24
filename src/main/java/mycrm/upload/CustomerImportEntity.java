package mycrm.upload;

import com.opencsv.bean.CsvBindByName;

public class CustomerImportEntity {

    @CsvBindByName(column = "Business Name", required = false)
    private String businessName;

    @CsvBindByName(column = "Address Line 1", required = false)
    private String businessAddressOne;

    @CsvBindByName(column = "Address Line 2", required = false)
    private String businessAddressTwo;

    @CsvBindByName(column = "City", required = false)
    private String businessCity;

    //needs space in between postcode to split
    //max 8 characters
    @CsvBindByName(column = "Postcode", required = false)
    private String businessPostcode;

    @CsvBindByName(column = "LTD Registration Number", required = false)
    private String ltdRegistrationNumber;

    //make sure this is a number
    @CsvBindByName(column = "Company Registration Number", required = false)
    private String companyRegistrationNumber;

    @CsvBindByName(column = "Sole Trader", required = false)
    private Boolean soleTrader;

    @CsvBindByName(column = "ST Contact Name", required = false)
    private String stContactName;

    @CsvBindByName(column = "ST Address Line 1", required = false)
    private String stAddressOne;

    @CsvBindByName(column = "ST Address Line 2", required = false)
    private String stAddressTwo;

    @CsvBindByName(column = "ST City", required = false)
    private String stCity;

    @CsvBindByName(column = "ST Postcode", required = false)
    private String stPostcode;

    //change site and customer to 50 digits
    @CsvBindByName(column = "Contact Number", required = false)
    private String contactNumber;

    @CsvBindByName(column = "Mobile Number", required = false)
    private String mobileNumber;

    @CsvBindByName(column = "Email Address", required = false)
    private String emailAddress;

    @CsvBindByName(column = "MPR", required = false)
    private String mpr;

    @CsvBindByName(column = "MPAN Line One", required = false)
    private String mpanLineOne;

    @CsvBindByName(column = "MPAN Line Two", required = false)
    private String mpanLineTwo;

    //if missing then wont upload gas
    @CsvBindByName(column = "Gas Meter Utility", required = false)
    private String gasMeterUtility;

    @CsvBindByName(column = "Gas Supplier", required = false)
    private String gasSupplier;

    @CsvBindByName(column = "Gas Unit Rate", required = false)
    private String gasUnitRate;

    @CsvBindByName(column = "Gas Standing Charge", required = false)
    private String gasStandingCharge;

    @CsvBindByName(column = "Gas Standing Charge Frequency", required = false)
    private String gasStandingChargeFrequency;

    @CsvBindByName(column = "Gas Start Date", required = false)
    private String gasStartDate;

    @CsvBindByName(column = "Gas End Date", required = false)
    private String gasEndDate;

    //remove comma
    @CsvBindByName(column = "Gas Estimated Annual Quantity", required = false)
    private String gasEstimatedAnnualQuantity;

    //if missing then wont upload electricity
    @CsvBindByName(column = "Electric Meter Utility", required = false)
    private String electricMeterUtility;

    @CsvBindByName(column = "Electricity Supplier", required = false)
    private String electricitySupplier;

    @CsvBindByName(column = "Electricity Day Rate", required = false)
    private String electricityDayRate;

    @CsvBindByName(column = "Electricity Night Rate", required = false)
    private String electricityNightRate;

    @CsvBindByName(column = "Electricity Evening/Weekend Rate", required = false)
    private String electricityEveningWeekendRate;

    @CsvBindByName(column = "Electricity Feed In Tariff", required = false)
    private String electricityFeedInTariff;

    @CsvBindByName(column = "Electricity Standing Charge", required = false)
    private String electricityStandingCharge;

    @CsvBindByName(column = "Electricity Standing Charge Frequency", required = false)
    private String electricityStandingChargeFrequency;

    @CsvBindByName(column = "Electricity Start Date", required = false)
    private String electricityStartDate;

    @CsvBindByName(column = "Electricity End Date", required = false)
    private String electricityEndDate;

    //remove comma
    @CsvBindByName(column = "Electricity Estimated Annual Quantity", required = false)
    private String electricityEstimatedAnnualQuantity;

    @CsvBindByName(column = "Campaign", required = false)
    private String campaign;

    private String errorMessage;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddressOne() {
        return businessAddressOne;
    }

    public void setBusinessAddressOne(String businessAddressOne) {
        this.businessAddressOne = businessAddressOne;
    }

    public String getBusinessAddressTwo() {
        return businessAddressTwo;
    }

    public void setBusinessAddressTwo(String businessAddressTwo) {
        this.businessAddressTwo = businessAddressTwo;
    }

    public String getBusinessCity() {
        return businessCity;
    }

    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    public String getBusinessPostcode() {
        return businessPostcode;
    }

    public void setBusinessPostcode(String businessPostcode) {
        this.businessPostcode = businessPostcode;
    }

    public String getLtdRegistrationNumber() {
        return ltdRegistrationNumber;
    }

    public void setLtdRegistrationNumber(String ltdRegistrationNumber) {
        this.ltdRegistrationNumber = ltdRegistrationNumber;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
        this.companyRegistrationNumber = companyRegistrationNumber;
    }

    public Boolean getSoleTrader() {
        return soleTrader;
    }

    public void setSoleTrader(Boolean soleTrader) {
        this.soleTrader = soleTrader;
    }

    public String getStContactName() {
        return stContactName;
    }

    public void setStContactName(String stContactName) {
        this.stContactName = stContactName;
    }

    public String getStAddressOne() {
        return stAddressOne;
    }

    public void setStAddressOne(String stAddressOne) {
        this.stAddressOne = stAddressOne;
    }

    public String getStAddressTwo() {
        return stAddressTwo;
    }

    public void setStAddressTwo(String stAddressTwo) {
        this.stAddressTwo = stAddressTwo;
    }

    public String getStCity() {
        return stCity;
    }

    public void setStCity(String stCity) {
        this.stCity = stCity;
    }

    public String getStPostcode() {
        return stPostcode;
    }

    public void setStPostcode(String stPostcode) {
        this.stPostcode = stPostcode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMpr() {
        return mpr;
    }

    public void setMpr(String mpr) {
        this.mpr = mpr;
    }

    public String getMpanLineOne() {
        return mpanLineOne;
    }

    public void setMpanLineOne(String mpanLineOne) {
        this.mpanLineOne = mpanLineOne;
    }

    public String getMpanLineTwo() {
        return mpanLineTwo;
    }

    public void setMpanLineTwo(String mpanLineTwo) {
        this.mpanLineTwo = mpanLineTwo;
    }

    public String getGasSupplier() {
        return gasSupplier;
    }

    public void setGasSupplier(String gasSupplier) {
        this.gasSupplier = gasSupplier;
    }

    public String getGasUnitRate() {
        return gasUnitRate;
    }

    public void setGasUnitRate(String gasUnitRate) {
        this.gasUnitRate = gasUnitRate;
    }

    public String getGasStandingCharge() {
        return gasStandingCharge;
    }

    public void setGasStandingCharge(String gasStandingCharge) {
        this.gasStandingCharge = gasStandingCharge;
    }

    public String getGasStandingChargeFrequency() {
        return gasStandingChargeFrequency;
    }

    public void setGasStandingChargeFrequency(String gasStandingChargeFrequency) {
        this.gasStandingChargeFrequency = gasStandingChargeFrequency;
    }

    public String getGasStartDate() {
        return gasStartDate;
    }

    public void setGasStartDate(String gasStartDate) {
        this.gasStartDate = gasStartDate;
    }

    public String getGasEndDate() {
        return gasEndDate;
    }

    public void setGasEndDate(String gasEndDate) {
        this.gasEndDate = gasEndDate;
    }

    public String getGasEstimatedAnnualQuantity() {
        return gasEstimatedAnnualQuantity;
    }

    public void setGasEstimatedAnnualQuantity(String gasEstimatedAnnualQuantity) {
        this.gasEstimatedAnnualQuantity = gasEstimatedAnnualQuantity;
    }

    public String getElectricitySupplier() {
        return electricitySupplier;
    }

    public void setElectricitySupplier(String electricitySupplier) {
        this.electricitySupplier = electricitySupplier;
    }

    public String getElectricityDayRate() {
        return electricityDayRate;
    }

    public void setElectricityDayRate(String electricityDayRate) {
        this.electricityDayRate = electricityDayRate;
    }

    public String getElectricityNightRate() {
        return electricityNightRate;
    }

    public void setElectricityNightRate(String electricityNightRate) {
        this.electricityNightRate = electricityNightRate;
    }

    public String getElectricityEveningWeekendRate() {
        return electricityEveningWeekendRate;
    }

    public void setElectricityEveningWeekendRate(String electricityEveningWeekendRate) {
        this.electricityEveningWeekendRate = electricityEveningWeekendRate;
    }

    public String getElectricityFeedInTariff() {
        return electricityFeedInTariff;
    }

    public void setElectricityFeedInTariff(String electricityFeedInTariff) {
        this.electricityFeedInTariff = electricityFeedInTariff;
    }

    public String getElectricityStandingCharge() {
        return electricityStandingCharge;
    }

    public void setElectricityStandingCharge(String electricityStandingCharge) {
        this.electricityStandingCharge = electricityStandingCharge;
    }

    public String getElectricityStandingChargeFrequency() {
        return electricityStandingChargeFrequency;
    }

    public void setElectricityStandingChargeFrequency(String electricityStandingChargeFrequency) {
        this.electricityStandingChargeFrequency = electricityStandingChargeFrequency;
    }

    public String getElectricityStartDate() {
        return electricityStartDate;
    }

    public void setElectricityStartDate(String electricityStartDate) {
        this.electricityStartDate = electricityStartDate;
    }

    public String getElectricityEndDate() {
        return electricityEndDate;
    }

    public void setElectricityEndDate(String electricityEndDate) {
        this.electricityEndDate = electricityEndDate;
    }

    public String getElectricityEstimatedAnnualQuantity() {
        return electricityEstimatedAnnualQuantity;
    }

    public void setElectricityEstimatedAnnualQuantity(String electricityEstimatedAnnualQuantity) {
        this.electricityEstimatedAnnualQuantity = electricityEstimatedAnnualQuantity;
    }

    public String getGasMeterUtility() {
        return gasMeterUtility;
    }

    public void setGasMeterUtility(String gasMeterUtility) {
        this.gasMeterUtility = gasMeterUtility;
    }

    public String getElectricMeterUtility() {
        return electricMeterUtility;
    }

    public void setElectricMeterUtility(String electricMeterUtility) {
        this.electricMeterUtility = electricMeterUtility;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }
}
