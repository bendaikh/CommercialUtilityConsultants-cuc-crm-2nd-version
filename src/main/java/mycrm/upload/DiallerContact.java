package mycrm.upload;

public class DiallerContact {
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String mobileNumber;

    public DiallerContact() {
    }

    public DiallerContact(String firstName, String lastName, String contactNumber, String mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.mobileNumber = mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    @Override
    public String toString() {
        return "DiallerContact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
