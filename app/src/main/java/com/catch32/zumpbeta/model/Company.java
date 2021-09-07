package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 28, 2019
 */
public class Company {
    private String comp_nm;
    private String comp_id;

    public String getComp_nm() {
        return comp_nm;
    }

    public void setComp_nm(String comp_nm) {
        this.comp_nm = comp_nm;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    @Override
    public String toString() {
        return "Company{" +
                "comp_nm='" + comp_nm + '\'' +
                ", comp_id='" + comp_id + '\'' +
                '}';
    }
}
