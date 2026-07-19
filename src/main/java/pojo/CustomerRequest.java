package pojo;

public class CustomerRequest {
    public String firstName, lastName, middleName, status;
    public int customerNumber;
    public CustomerAddress customerAddress;
    public ContactDetails contactDetails;

    public CustomerRequest(String fName, String lName, String mName, int cNum, String status,
                           CustomerAddress addr, ContactDetails contact) {
        this.firstName = fName;
        this.lastName = lName;
        this.middleName = mName;
        this.customerNumber = cNum;
        this.status = status;
        this.customerAddress = addr;
        this.contactDetails = contact;
    }
}
