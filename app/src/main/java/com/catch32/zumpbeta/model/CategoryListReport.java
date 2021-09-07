package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 28, 2019
 */
public class CategoryListReport {
    private String cat_nm;
    private String cat_agent_id;
    private String cat_id;

    public String getCat_nm() {
        return cat_nm;
    }

    public void setCat_nm(String cat_nm) {
        this.cat_nm = cat_nm;
    }

    public String getCat_agent_id() {
        return cat_agent_id;
    }

    public void setCat_agent_id(String cat_agent_id) {
        this.cat_agent_id = cat_agent_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    @Override
    public String toString() {
        return "CategoryListReport{" +
                "cat_nm='" + cat_nm + '\'' +
                ", cat_agent_id='" + cat_agent_id + '\'' +
                ", cat_id='" + cat_id + '\'' +
                '}';
    }
}
