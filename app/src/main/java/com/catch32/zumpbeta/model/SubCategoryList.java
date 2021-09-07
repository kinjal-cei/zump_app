package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class SubCategoryList {

    private String subCategoryName;
    private String subCategoryParentId;
    private String subCategoryId;

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryParentId() {
        return subCategoryParentId;
    }

    public void setSubCategoryParentId(String subCategoryParentId) {
        this.subCategoryParentId = subCategoryParentId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    @Override
    public String toString() {
        return "SubCategoryList{" +
                "subCategoryName='" + subCategoryName + '\'' +
                ", subCategoryParentId='" + subCategoryParentId + '\'' +
                ", subCategoryId='" + subCategoryId + '\'' +
                '}';
    }
}
