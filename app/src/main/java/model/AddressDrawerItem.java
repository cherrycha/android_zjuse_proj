package model;

public class AddressDrawerItem {
    private String address;
    private String phone_no;
    private String icon_name;

    public AddressDrawerItem(){

    }
    public AddressDrawerItem(String address,String phone_no,String icon_name){
        setAddress(address);
        setPhone_no(phone_no);
        setIcon_name(icon_name);
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

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public String getIconName() {
        return icon_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
