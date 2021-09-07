package com.catch32.zumpbeta.model;

/**
 * @author SB
 * @version Dec 31, 2019
 */
public class NotificationList {
    private String notify_dt;
    private String notify_flag;
    private String notification;

    //private List<NotificationList> notifyList = null;


    public String getNotifyDt() {
        return notify_dt;
    }
    public void setNotifyDt(String notify_dt) {
        this.notify_dt = notify_dt;
    }

    public String getNotifyFlag() {
        return notify_flag;
    }
    public void setNotifyFlag(String notify_flag) {
        this.notify_flag = notify_flag;
    }


    public String getNotification() {
        return notification;
    }
    public void setNotification(String notification) {
        this.notification = notification;
    }

    /*public List<NotificationList> getNotificationList() {return notifyList;}
    public void setNotificationList(List<NotificationList> notifyList) {this.notifyList = notifyList;}*/


    @Override
    public String toString() {
        return "NotificationList{" +
                "notification='" + notification + '\'' +
                ", notify_dt='" + notify_dt + '\'' +
                ", notify_flag='" + notify_flag + '\'' +
                ", notify_read_dt='" + notify_flag + '\'' +
                '}';
    }
}