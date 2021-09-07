package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 04, 2019
 */
public class Status {
    private String statusName;


    @Override
    public String toString() {
        return "Status{" +
                "statusName='" + statusName + '\'' +
                '}';
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
