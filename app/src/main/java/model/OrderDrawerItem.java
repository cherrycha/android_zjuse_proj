package model;

public class OrderDrawerItem {
    private String status;
    public OrderDrawerItem(){

    }


    public OrderDrawerItem(String status) {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
