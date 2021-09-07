package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class SubCategoryBrandList {

    private String subCategoryCompName;

    private String subCategoryCompId;

    private String subCategoryBrandId;

    private String subCategoryBrandName;

    public String getSubCategoryCompName() {
        return subCategoryCompName;
    }

    public void setSubCategoryCompName(String subCategoryCompName) {
        this.subCategoryCompName = subCategoryCompName;
    }

    public String getSubCategoryCompId() {
        return subCategoryCompId;
    }

    public void setSubCategoryCompId(String subCategoryCompId) {
        this.subCategoryCompId = subCategoryCompId;
    }

    public String getSubCategoryBrandId() {
        return subCategoryBrandId;
    }

    public void setSubCategoryBrandId(String subCategoryBrandId) {
        this.subCategoryBrandId = subCategoryBrandId;
    }

    public String getSubCategoryBrandName() {
        return subCategoryBrandName;
    }

    public void setSubCategoryBrandName(String subCategoryBrandName) {
        this.subCategoryBrandName = subCategoryBrandName;
    }

    @Override
    public String toString() {
        return "SubCategoryBrandList{" +
                "subCategoryCompName='" + subCategoryCompName + '\'' +
                ", subCategoryCompId='" + subCategoryCompId + '\'' +
                ", subCategoryBrandId='" + subCategoryBrandId + '\'' +
                ", subCategoryBrandName='" + subCategoryBrandName + '\'' +
                '}';
    }
}
