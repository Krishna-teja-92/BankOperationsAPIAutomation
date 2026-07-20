package pojo;

public class CustomerDetails {
    public String firstName, lastName, middleName, status;
    public long customerNumber;
    public CustomerAddress customerAddress;
    public ContactDetails contactDetails;

    public CustomerDetails(){}

    public CustomerDetails(String fName, String lName, String mName, long cNum, String status,
                           CustomerAddress addr, ContactDetails contact) {
        this.firstName = fName;
        this.lastName = lName;
        this.middleName = mName;
        this.customerNumber = cNum;
        this.status = status;
        this.customerAddress = addr;
        this.contactDetails = contact;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddress = customerAddress;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }
}
