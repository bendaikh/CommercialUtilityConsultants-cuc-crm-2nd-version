package mycrm.models;

public class CustomerSiteWithContracts {
    private CustomerSite site;
    private GasCustomerContract gasContract;
    private ElecCustomerContract electricContract;
    private UtilityContract solarContract;
    private UtilityContract waterContract;
    private UtilityContract voipContract;
    private UtilityContract landlineContract;
    private UtilityContract mobileContract;
    private UtilityContract broadbandContract;
    private MerchantServicesContract merchantServicesContract;

    public CustomerSite getSite() {
        return site;
    }

    public void setSite(CustomerSite site) {
        this.site = site;
    }

    public GasCustomerContract getGasContract() {
        return gasContract;
    }

    public void setGasContract(GasCustomerContract gasContract) {
        this.gasContract = gasContract;
    }

    public ElecCustomerContract getElectricContract() {
        return electricContract;
    }

    public void setElectricContract(ElecCustomerContract electricContract) {
        this.electricContract = electricContract;
    }

    public UtilityContract getSolarContract() {
        return solarContract;
    }

    public void setSolarContract(UtilityContract solarContract) {
        this.solarContract = solarContract;
    }

    public UtilityContract getWaterContract() {
        return waterContract;
    }

    public void setWaterContract(UtilityContract waterContract) {
        this.waterContract = waterContract;
    }

    public UtilityContract getVoipContract() {
        return voipContract;
    }

    public void setVoipContract(UtilityContract voipContract) {
        this.voipContract = voipContract;
    }

    public UtilityContract getLandlineContract() {
        return landlineContract;
    }

    public void setLandlineContract(UtilityContract landlineContract) {
        this.landlineContract = landlineContract;
    }

    public UtilityContract getMobileContract() {
        return mobileContract;
    }

    public void setMobileContract(UtilityContract mobileContract) {
        this.mobileContract = mobileContract;
    }

    public UtilityContract getBroadbandContract() {
        return broadbandContract;
    }

    public void setBroadbandContract(UtilityContract broadbandContract) {
        this.broadbandContract = broadbandContract;
    }

    public MerchantServicesContract getMerchantServicesContract() {
        return merchantServicesContract;
    }

    public void setMerchantServicesContract(MerchantServicesContract merchantServicesContract) {
        this.merchantServicesContract = merchantServicesContract;
    }
}
