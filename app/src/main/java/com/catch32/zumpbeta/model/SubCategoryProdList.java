package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class SubCategoryProdList {
    private String subCategoryProdId;
    private String subCategoryProdName;
    private String subCategoryParentProdId;
    private String subCategoryId;
    private String subCategoryProdSku;
    private String subCategoryProdMrp;
    private String subCategoryProdBrand;
    private String subCategoryProdBrandNm;
    private String subCategoryParentDistId;
    private String subCategoryProdTrp;
    private String subCategoryProdBasic; //SB20200211
    private String subCategoryProdGST; //SB20200709
    private String ProdCount; //SB20200709
    private String offer;
    private String qty;
    private String color;
    private String moq;
    //private String mba; //SB20191221
    private String moa; //SB20200110

    public String getSubCategoryProdId() {
        return subCategoryProdId;
    }

    public void setSubCategoryProdId(String subCategoryProdId) {
        this.subCategoryProdId = subCategoryProdId;
    }

    public String getSubCategoryProdName() {
        return subCategoryProdName;
    }

    public void setSubCategoryProdName(String subCategoryProdName) {
        this.subCategoryProdName = subCategoryProdName;
    }

    public String getSubCategoryParentProdId() {
        return subCategoryParentProdId;
    }

    public void setSubCategoryParentProdId(String subCategoryParentProdId) {
        this.subCategoryParentProdId = subCategoryParentProdId;
    }


    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryProdSku() {
        return subCategoryProdSku;
    }

    public void setSubCategoryProdSku(String subCategoryProdSku) {
        this.subCategoryProdSku = subCategoryProdSku;
    }

    public String getSubCategoryProdMrp() {
        return subCategoryProdMrp;
    }

    public void setSubCategoryProdMrp(String subCategoryProdMrp) {
        this.subCategoryProdMrp = subCategoryProdMrp;
    }

    public String getSubCategoryProdBrand() {
        return subCategoryProdBrand;
    }

    public void setSubCategoryProdBrand(String subCategoryProdBrand) {
        this.subCategoryProdBrand = subCategoryProdBrand;
    }

    public String getSubCategoryProdBrandNm() {
        return subCategoryProdBrandNm;
    }

    public void setSubCategoryProdBrandNm(String subCategoryProdBrandNm) {
        this.subCategoryProdBrandNm = subCategoryProdBrandNm;
    }

    public String getSubCategoryParentDistId() {
        return subCategoryParentDistId;
    }

    public void setSubCategoryParentDistId(String subCategoryParentDistId) {
        this.subCategoryParentDistId = subCategoryParentDistId;
    }

    public String getSubCategoryProdTrp() {
        return subCategoryProdTrp;
    }

    public void setSubCategoryProdTrp(String subCategoryProdTrp) {
        this.subCategoryProdTrp = subCategoryProdTrp;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }
    ////////////SB20191221////////////////
    /*public String getMba() {
        return mba;
    }
    public void setMba(String mba) {
        this.mba = mba;
    }*/
    public String getMoa() {
        return moa;
    }
    public void setMoa(String moa) {
        this.moa = moa;
    }

    public String getSubCategoryProdBasic() { return subCategoryProdBasic;} //SB20200211
    public void setSubCategoryProdBasic(String subCategoryProdBasic) {this.subCategoryProdBasic = subCategoryProdBasic;}//SB20200211
    public String getSubCategoryProdGST() { return subCategoryProdGST;} //SB20200211
    public void setSubCategoryProdGST(String subCategoryProdGST) {this.subCategoryProdGST = subCategoryProdGST;}//SB20200709
    public String getProdCount() { return ProdCount;} //SB20200211
    public void setProdCount(String ProdCount) {this.ProdCount = ProdCount;}//SB20200709
    /////////////////////////////////////////

    @Override
    public String toString() {
        return "SubCategoryProdList{" +
                "subCategoryProdId='" + subCategoryProdId + '\'' +
                ", subCategoryProdName='" + subCategoryProdName + '\'' +
                ", subCategoryParentProdId='" + subCategoryParentProdId + '\'' +
                ", subCategoryId='" + subCategoryId + '\'' +
                ", subCategoryProdSku='" + subCategoryProdSku + '\'' +
                ", subCategoryProdMrp='" + subCategoryProdMrp + '\'' +
                ", subCategoryProdBrand='" + subCategoryProdBrand + '\'' +
                ", subCategoryProdBrandNm='" + subCategoryProdBrandNm + '\'' +
                ", subCategoryParentDistId='" + subCategoryParentDistId + '\'' +
                ", subCategoryProdTrp='" + subCategoryProdTrp + '\'' +
                ", subCategoryProdBasic='" + subCategoryProdBasic+ '\'' +
                ", subCategoryProdGST='" + subCategoryProdGST+ '\'' + //SB20200709
                ", ProdCount='" + ProdCount+ '\'' + //SB20200709
                ", offer='" + offer + '\'' +
                ", qty='" + qty + '\'' +
                ", color='" + color + '\'' +
                ", moq='" + moq + '\'' +
              //  ", mba='" + mba + '\'' + //SB20191221
                ", moa='" + moa + '\'' + //SB20200110
                '}';
    }
}
