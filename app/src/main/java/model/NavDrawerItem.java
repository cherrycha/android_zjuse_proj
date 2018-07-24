package model;

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private String icon_name;

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title,String icon_name) {
        this.showNotify = showNotify;
        this.title = title;
        this.icon_name=icon_name;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public String getIconName(){
        return icon_name;
    }
    public void setIconName(String icon_name) {
        this.icon_name = icon_name;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}