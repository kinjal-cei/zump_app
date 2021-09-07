package com.catch32.zumpbeta.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class NewOrder {

    private String loginSuccess;
    private String categoryListFlag;
    @SerializedName("categoryList")
    private List<CategoryListReport> categoryList = null;
    private String subCategoryListFlag;
    private List<SubCategoryList> subCategoryList = null;
    private String subCategoryBrandListFlag;
    private List<SubCategoryBrandList> subCategoryBrandList = null;
    private List<DepartmentList> departmentList = null;
    private List<SubCategoryProdList> subCategoryProdList = null;
    private String newComplainRegister;
    private String newDraftRegister;
    private String subCategoryProdListFlag;
    private String draftOrder;
    private String MOQ_COMP;
    private String DelvSch;
    private String MinBill, MinBillText; //SB20191212

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getCategoryListFlag() {
        return categoryListFlag;
    }

    public void setCategoryListFlag(String categoryListFlag) {
        this.categoryListFlag = categoryListFlag;
    }

    public List<CategoryListReport> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryListReport> categoryList) {
        this.categoryList = categoryList;
    }

    public String getSubCategoryListFlag() {
        return subCategoryListFlag;
    }

    public void setSubCategoryListFlag(String subCategoryListFlag) {
        this.subCategoryListFlag = subCategoryListFlag;
    }

    public List<SubCategoryList> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategoryList> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public String getSubCategoryBrandListFlag() {
        return subCategoryBrandListFlag;
    }

    public void setSubCategoryBrandListFlag(String subCategoryBrandListFlag) {
        this.subCategoryBrandListFlag = subCategoryBrandListFlag;
    }

    public List<SubCategoryBrandList> getSubCategoryBrandList() {
        return subCategoryBrandList;
    }

    public void setSubCategoryBrandList(List<SubCategoryBrandList> subCategoryBrandList) {
        this.subCategoryBrandList = subCategoryBrandList;
    }

    public List<DepartmentList> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentList> departmentList) {
        this.departmentList = departmentList;
    }

    public List<SubCategoryProdList> getSubCategoryProdList() {
        return subCategoryProdList;
    }

    public void setSubCategoryProdList(List<SubCategoryProdList> subCategoryProdList) {
        this.subCategoryProdList = subCategoryProdList;
    }

    public String getNewComplainRegister() {
        return newComplainRegister;
    }

    public void setNewComplainRegister(String newComplainRegister) {
        this.newComplainRegister = newComplainRegister;
    }

    public String getNewDraftRegister() {
        return newDraftRegister;
    }

    public void setNewDraftRegister(String newDraftRegister) {
        this.newDraftRegister = newDraftRegister;
    }

    public String getSubCategoryProdListFlag() {
        return subCategoryProdListFlag;
    }

    public void setSubCategoryProdListFlag(String subCategoryProdListFlag) {
        this.subCategoryProdListFlag = subCategoryProdListFlag;
    }

    public String getDraftOrder() {
        return draftOrder;
    }

    public void setDraftOrder(String draftOrder) {
        this.draftOrder = draftOrder;
    }

    public String getMOQ_COMP() {
        return MOQ_COMP;
    }

    public void setMOQ_COMP(String MOQ_COMP) {
        this.MOQ_COMP = MOQ_COMP;
    }

    public String getDelvSch() {
        return DelvSch;
    }

    public void setDelvSch(String delvSch) {
        DelvSch = delvSch;
    }
    ///////SB20191212: Added MBA////////////
    public String getMinBill() {return MinBill;}
    public void setMba(String MinBill) {
        this.MinBill = MinBill;
    }
    public String getMinBillText() {return MinBillText;}
    public void setMinBillText(String MinBill) {
        this.MinBillText = MinBillText;
    }
    /////////////////////////////////////////

    @Override
    public String toString() {
        return "NewOrder{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", categoryListFlag='" + categoryListFlag + '\'' +
                ", categoryList=" + categoryList +
                ", subCategoryListFlag='" + subCategoryListFlag + '\'' +
                ", subCategoryList=" + subCategoryList +
                ", subCategoryBrandListFlag='" + subCategoryBrandListFlag + '\'' +
                ", subCategoryBrandList=" + subCategoryBrandList +
                ", departmentList=" + departmentList +
                ", subCategoryProdList=" + subCategoryProdList +
                ", newComplainRegister='" + newComplainRegister + '\'' +
                ", newDraftRegister='" + newDraftRegister + '\'' +
                ", subCategoryProdListFlag='" + subCategoryProdListFlag + '\'' +
                ", draftOrder='" + draftOrder + '\'' +
                ", MOQ_COMP='" + MOQ_COMP + '\'' +
                ", DelvSch='" + DelvSch + '\'' +
                ", MinBill='" + MinBill + '\'' +
                ", MinBillText='" + MinBillText + '\'' +
                '}';
    }
}
