package model;

public class AddressDrawerItem {
    private String address;
    private String phone_no;
    public AddressDrawerItem(){

    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public AddressDrawerItem(String address) {
        this.address=address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
