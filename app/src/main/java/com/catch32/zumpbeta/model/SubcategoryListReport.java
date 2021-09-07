package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 28, 2019
 */
public class SubcategoryListReport {
    private String sub_cat_nm;
    private String sub_cat_agent_id;
    private String sub_cat_id;

    public String getSub_cat_nm() {
        return sub_cat_nm;
    }

    public void setSub_cat_nm(String sub_cat_nm) {
        this.sub_cat_nm = sub_cat_nm;
    }

    public String getSub_cat_agent_id() {
        return sub_cat_agent_id;
    }

    public void setSub_cat_agent_id(String sub_cat_agent_id) {
        this.sub_cat_agent_id = sub_cat_agent_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    @Override
    public String toString() {
        return "SubcategoryListReport{" +
                "sub_cat_nm='" + sub_cat_nm + '\'' +
                ", sub_cat_agent_id='" + sub_cat_agent_id + '\'' +
                ", sub_cat_id='" + sub_cat_id + '\'' +
                '}';
    }
}
