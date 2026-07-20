package pojo;

public class CustomerAddress {
        public String address1, address2, city, state, zip, country;

        public CustomerAddress(){}

        public CustomerAddress(String a1, String a2, String city, String state, String zip, String country) {
            this.address1 = a1;
            this.address2 = a2;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.country = country;
        }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

