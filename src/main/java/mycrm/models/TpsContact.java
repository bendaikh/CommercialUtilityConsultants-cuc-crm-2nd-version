package mycrm.models;

public class TpsContact {
    private Contact contact;
    private TpsCheck contactNumberTpsCheck;
    private TpsCheck mobileNumberTpsCheck;
    private String contactNumberStyle;
    private String mobileNumberStyle;
    private boolean withinCheckPeriod;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public TpsCheck getContactNumberTpsCheck() {
        return contactNumberTpsCheck;
    }

    public void setContactNumberTpsCheck(TpsCheck contactNumberTpsCheck) {
        this.contactNumberTpsCheck = contactNumberTpsCheck;
    }

    public TpsCheck getMobileNumberTpsCheck() {
        return mobileNumberTpsCheck;
    }

    public void setMobileNumberTpsCheck(TpsCheck mobileNumberTpsCheck) {
        this.mobileNumberTpsCheck = mobileNumberTpsCheck;
    }

    public String getContactNumberStyle() {
        return contactNumberStyle;
    }

    public void setContactNumberStyle(String contactNumberStyle) {
        this.contactNumberStyle = contactNumberStyle;
    }

    public String getMobileNumberStyle() {
        return mobileNumberStyle;
    }

    public void setMobileNumberStyle(String mobileNumberStyle) {
        this.mobileNumberStyle = mobileNumberStyle;
    }

    public boolean isWithinCheckPeriod() {
        return withinCheckPeriod;
    }

    public void setWithinCheckPeriod(boolean withinCheckPeriod) {
        this.withinCheckPeriod = withinCheckPeriod;
    }

    @Override
    public String toString() {
        return "TpsContact{" +
                ", contactNumberTpsCheck=" + contactNumberTpsCheck +
                ", mobileNumberTpsCheck=" + mobileNumberTpsCheck +
                ", contactNumberClass='" + contactNumberStyle + '\'' +
                ", mobileNumberClass='" + mobileNumberStyle + '\'' +
                ", withinCheckPeriod=" + withinCheckPeriod +
                '}';
    }
}
