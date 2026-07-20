package pojo;

public class ContactDetails {
    public String emailId, homePhone, workPhone;

    public ContactDetails(){}

    public ContactDetails(String email, String hPhone, String wPhone) {
        this.emailId = email;
        this.homePhone = hPhone;
        this.workPhone = wPhone;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }
}
