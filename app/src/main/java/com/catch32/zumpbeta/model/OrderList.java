package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 19, 2019
 */
public class OrderList {

    private String id;
    private Object user_nm;
    private String time;
    private String flag;
    private String ordno;
    private String subcategory;
    private String desc;
    private String description;
    private String prod_nm;
    private String prod_mrp;
    private String prod_trp;
    private Object prod_qty;
    private String TAP; //SB20191221

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getUser_nm() {
        return user_nm;
    }

    public void setUser_nm(Object user_nm) {
        this.user_nm = user_nm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProd_nm() {
        return prod_nm;
    }

    public void setProd_nm(String prod_nm) {
        this.prod_nm = prod_nm;
    }

    public String getProd_mrp() {
        return prod_mrp;
    }

    public void setProd_mrp(String prod_mrp) {
        this.prod_mrp = prod_mrp;
    }

    public String getProd_trp() {
        return prod_trp;
    }

    public void setProd_trp(String prod_trp) {
        this.prod_trp = prod_trp;
    }

    public Object getProd_qty() {
        return prod_qty;
    }

    public void setProd_qty(Object prod_qty) {
        this.prod_qty = prod_qty;
    }

    public String getTap() {return TAP;} //SB20191221
    public void setTap(String TAP) {this.TAP = TAP;} //SB20191221

    @Override
    public String toString() {
        return "OrderList{" +
                "id='" + id + '\'' +
                ", user_nm=" + user_nm +
                ", time='" + time + '\'' +
                ", flag='" + flag + '\'' +
                ", ordno='" + ordno + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", desc='" + desc + '\'' +
                ", description='" + description + '\'' +
                ", prod_nm='" + prod_nm + '\'' +
                ", prod_mrp='" + prod_mrp + '\'' +
                ", prod_trp='" + prod_trp + '\'' +
                ", prod_qty=" + prod_qty +
                ", Tap=" + TAP +
                '}';
    }
}