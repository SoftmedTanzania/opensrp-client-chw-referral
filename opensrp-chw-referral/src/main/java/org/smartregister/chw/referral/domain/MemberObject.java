package org.smartregister.chw.referral.domain;

import com.google.gson.annotations.SerializedName;

import org.smartregister.chw.referral.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.io.Serializable;

public class MemberObject implements Serializable {
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String gender;
    private String uniqueId;
    private String age;
    private String relationalid;
    private String details;
    private String baseEntityId;
    private String relationalId;
    private String primaryCareGiver;
    private String familyHead;
    private String familyBaseEntityId;
    private String familyName;
    private String phoneNumber;
    private String familyHeadPhoneNumber;
    private String otherPhoneNumber;
    private String servicesBeforeReferral;
    private String servicesBeforeReferralOther;

    @SerializedName("chw_referral_hf")
    private String chwReferralHf;


    @SerializedName("chw_referral_service")
    private String chwReferralService;

    @SerializedName("chw_referral_service_id")
    private String chwReferralServiceId;


    @SerializedName("chw_referral_date")
    private String chwReferralDate;


    @SerializedName("problem")
    private String problem;

    @SerializedName("problem_other")
    private String problemOther;


    @SerializedName("referral_type")
    private String referralType;

    @SerializedName("referral_status")
    private String referralStatus;

    @SerializedName("referral_appointment_date")
    private String referralAppointmentDate;


    @SerializedName("is_emergency_referral")
    private boolean isEmergencyReferral;
    private boolean isClosed;

    public MemberObject(CommonPersonObjectClient client) {
        firstName = client.getColumnmaps().get(DBConstants.KEY.FIRST_NAME) != null ? client.getColumnmaps().get(DBConstants.KEY.FIRST_NAME) : "";
        middleName = client.getColumnmaps().get(DBConstants.KEY.MIDDLE_NAME) != null ? client.getColumnmaps().get(DBConstants.KEY.MIDDLE_NAME) : "";
        lastName = client.getColumnmaps().get(DBConstants.KEY.LAST_NAME) != null ? client.getColumnmaps().get(DBConstants.KEY.LAST_NAME) : "";
        address = client.getColumnmaps().get(DBConstants.KEY.VILLAGE_TOWN) != null ? client.getColumnmaps().get(DBConstants.KEY.VILLAGE_TOWN) : "";
        gender = client.getColumnmaps().get(DBConstants.KEY.GENDER) != null ? client.getColumnmaps().get(DBConstants.KEY.GENDER) : "";
        age = client.getColumnmaps().get(DBConstants.KEY.DOB) != null ? client.getColumnmaps().get(DBConstants.KEY.DOB) : "";
        uniqueId = client.getColumnmaps().get(DBConstants.KEY.UNIQUE_ID);
        baseEntityId = client.getColumnmaps().get(DBConstants.KEY.BASE_ENTITY_ID);
        relationalId = client.getColumnmaps().get(DBConstants.KEY.RELATIONAL_ID);
        primaryCareGiver = client.getColumnmaps().get(DBConstants.KEY.PRIMARY_CARE_GIVER);
        familyHead = client.getColumnmaps().get(DBConstants.KEY.FAMILY_HEAD);
        familyBaseEntityId = client.getColumnmaps().get(DBConstants.KEY.RELATIONAL_ID);
        relationalid = client.getColumnmaps().get(DBConstants.KEY.RELATIONAL_ID);
        details = client.getColumnmaps().get(DBConstants.KEY.DETAILS);
        isEmergencyReferral = Boolean.parseBoolean(client.getColumnmaps().get(DBConstants.KEY.IS_EMERGENCY_REFERRAL));
        familyName = client.getColumnmaps().get(DBConstants.KEY.FAMILY_NAME);
        phoneNumber = client.getColumnmaps().get(DBConstants.KEY.PHONE_NUMBER);
        familyHeadPhoneNumber = client.getColumnmaps().get(DBConstants.KEY.FAMILY_HEAD_PHONE_NUMBER);
        otherPhoneNumber = client.getColumnmaps().get(DBConstants.KEY.OTHER_PHONE_NUMBER);
        chwReferralHf = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_HF);
        chwReferralServiceId = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_SERVICE_ID);
        chwReferralDate = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_DATE);
        problem = client.getColumnmaps().get(DBConstants.KEY.PROBLEM);
        problemOther = client.getColumnmaps().get(DBConstants.KEY.PROBLEM_OTHER);
        referralType = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_TYPE);
        referralStatus = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_STATUS);
        referralAppointmentDate = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_APPOINTMENT_DATE);
        servicesBeforeReferral = client.getColumnmaps().get(DBConstants.KEY.SERVICE_BEFORE_REFERRAL);
        chwReferralService = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_SERVICE);
        servicesBeforeReferralOther = client.getColumnmaps().get(DBConstants.KEY.SERVICE_BEFORE_REFERRAL_OTHER);
        isClosed = Boolean.parseBoolean(client.getColumnmaps().get(DBConstants.KEY.IS_CLOSED));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getPrimaryCareGiver() {
        return primaryCareGiver;
    }

    public void setPrimaryCareGiver(String primaryCareGiver) {
        this.primaryCareGiver = primaryCareGiver;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
    }

    public String getFamilyBaseEntityId() {
        return familyBaseEntityId;
    }

    public void setFamilyBaseEntityId(String familyBaseEntityId) {
        this.familyBaseEntityId = familyBaseEntityId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getChwReferralHf() {
        return chwReferralHf;
    }

    public void setChwReferralHf(String chwReferralHf) {
        this.chwReferralHf = chwReferralHf;
    }

    public String getChwReferralService() {
        return chwReferralService;
    }

    public void setChwReferralService(String chwReferralService) {
        this.chwReferralService = chwReferralService;
    }

    public String getChwReferralDate() {
        return chwReferralDate;
    }

    public void setChwReferralDate(String chwReferralDate) {
        this.chwReferralDate = chwReferralDate;
    }

    public String getChwReferralServiceId() {
        return chwReferralServiceId;
    }

    public void setChwReferralServiceId(String chwReferralServiceId) {
        this.chwReferralServiceId = chwReferralServiceId;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemOther() {
        return problemOther;
    }

    public void setProblemOther(String problemOther) {
        this.problemOther = problemOther;
    }

    public String getReferralType() {
        return referralType;
    }

    public void setReferralType(String referralType) {
        this.referralType = referralType;
    }

    public boolean getIsEmergencyReferral() {
        return isEmergencyReferral;
    }

    public void setEmergencyReferral(boolean emergencyReferral) {
        this.isEmergencyReferral = emergencyReferral;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean closed) {
        isClosed = closed;
    }

    public String getReferralStatus() {
        return referralStatus;
    }

    public void setReferralStatus(String referralStatus) {
        this.referralStatus = referralStatus;
    }

    public String getReferralAppointmentDate() {
        return referralAppointmentDate;
    }

    public void setReferralAppointmentDate(String referralAppointmentDate) {
        this.referralAppointmentDate = referralAppointmentDate;
    }

    public String getRelationalid() {
        return relationalid;
    }

    public void setRelationalid(String relationalid) {
        this.relationalid = relationalid;
    }

    public String getOtherPhoneNumber() {
        return otherPhoneNumber;
    }

    public void setOtherPhoneNumber(String otherPhoneNumber) {
        this.otherPhoneNumber = otherPhoneNumber;
    }

    public String getFamilyHeadPhoneNumber() {
        return familyHeadPhoneNumber;
    }

    public void setFamilyHeadPhoneNumber(String familyHeadPhoneNumber) {
        this.familyHeadPhoneNumber = familyHeadPhoneNumber;
    }

    public String getServicesBeforeReferral() {
        return servicesBeforeReferral;
    }

    public void setServicesBeforeReferral(String servicesBeforeReferral) {
        this.servicesBeforeReferral = servicesBeforeReferral;
    }

    public String getServicesBeforeReferralOther() {
        return servicesBeforeReferralOther;
    }

    public void setServicesBeforeReferralOther(String servicesBeforeReferralOther) {
        this.servicesBeforeReferralOther = servicesBeforeReferralOther;
    }
}
