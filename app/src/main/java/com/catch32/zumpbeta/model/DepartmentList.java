package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class DepartmentList {

    private String name;

    private String hostel;

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "DepartmentList{" +
                "name='" + name + '\'' +
                ", hostel='" + hostel + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
