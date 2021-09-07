package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 28, 2019
 */
public class ProfileData {
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String genderVal;
    private String name;
    private String emailId;
    private String middleName;
    private String mobileNo;
    private String gstn; //SB20191216
    private String route; //SB2020200108
    private String ph_no; //SB2020200108
    private String outlet_type; //SB2020200108
    private String po_nm; //SB2020200108
    private String street; //SB2020200108
    private String city; //SB2020200108
    private String owner_nm; //SB2020200208

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderVal() {
        return genderVal;
    }

    public void setGenderVal(String genderVal) {
        this.genderVal = genderVal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGstn() {
        return gstn;
    } //SB20191216
    public void setGstnNo(String gstn) {
        this.gstn = gstn;
    } //SB20191216
    public String getPhNo() {
        return ph_no;
    } //SB20191216
    public void setPhNo(String ph_no) {
        this.ph_no = ph_no;
    } //SB20191216
    public String getOutletType() {
        return outlet_type;
    } //SB20191216
    public void setOutletType(String outlet_type) {
        this.outlet_type = outlet_type;
    } //SB20191216
    public String getPoNm() {
        return po_nm;
    } //SB20191216
    public void setPoNm(String po_nm) {
        this.po_nm = po_nm;
    } //SB20191216
    public String getStreet() {
        return street;
    } //SB20191216
    public void setStreet(String street) {
        this.street = street;
    } //SB20191216
    public String getCity() {
        return city;
    } //SB20191216
    public void setCity(String city) {
        this.city = city;
    } //SB20191216
    public String getOwnerNm() {
        return owner_nm;
    } //SB20191216
    public void setOwnerNm(String owner_nm) {
        this.owner_nm = owner_nm;
    } //SB20191216

    @Override
    public String toString() {
        return "ProfileData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", genderVal='" + genderVal + '\'' +
                ", name='" + name + '\'' +
                ", emailId='" + emailId + '\'' +
                ", middleName='" + middleName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", gstn='" + gstn + '\'' +
                ", route='" + route + '\'' +
                ", ph_no='" + ph_no + '\'' +
                ", outlet_type='" + outlet_type + '\'' +
                ", po_nm='" + po_nm + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", owner_nm='" + owner_nm + '\'' +
                '}';
    }
}
