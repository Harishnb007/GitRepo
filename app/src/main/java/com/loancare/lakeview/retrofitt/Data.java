package com.loancare.lakeview.retrofitt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Data {

    @SerializedName("AuthorizationToken")
    @Expose
    private String authorizationToken;
    @SerializedName("Expires")
    @Expose
    private String expires;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("setup_status")
    @Expose
    private Integer setupStatus;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("middle_name")
    @Expose
    private Object middleName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ssn")
    @Expose
    private Object ssn;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("loanNumber")
    @Expose
    private Object loanNumber;
    @SerializedName("LoginId")
    @Expose
    private String loginId;
    @SerializedName("NotifyEmail")
    @Expose
    private Object notifyEmail;
    @SerializedName("discVer")
    @Expose
    private Object discVer;
    @SerializedName("is_successful")
    @Expose
    private Boolean isSuccessful;
    @SerializedName("mae_steps_completed")
    @Expose
    private String maeStepsCompleted;
    @SerializedName("resourcename")
    @Expose
    private Object resourcename;
    @SerializedName("ClientId")
    @Expose
    private Integer clientId;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("BorrowerName")
    @Expose
    private String borrowerName;
    @SerializedName("address")
    @Expose
    private okhttp3.Address address;
    @SerializedName("SecurityQuestionFlag")
    @Expose
    private Boolean securityQuestionFlag;
    @SerializedName("loans")
    @Expose
    private List<Loan> loans = null;

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSetupStatus() {
        return setupStatus;
    }

    public void setSetupStatus(Integer setupStatus) {
        this.setupStatus = setupStatus;
    }

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

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getSsn() {
        return ssn;
    }

    public void setSsn(Object ssn) {
        this.ssn = ssn;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(Object loanNumber) {
        this.loanNumber = loanNumber;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Object getNotifyEmail() {
        return notifyEmail;
    }

    public void setNotifyEmail(Object notifyEmail) {
        this.notifyEmail = notifyEmail;
    }

    public Object getDiscVer() {
        return discVer;
    }

    public void setDiscVer(Object discVer) {
        this.discVer = discVer;
    }

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getMaeStepsCompleted() {
        return maeStepsCompleted;
    }

    public void setMaeStepsCompleted(String maeStepsCompleted) {
        this.maeStepsCompleted = maeStepsCompleted;
    }

    public Object getResourcename() {
        return resourcename;
    }

    public void setResourcename(Object resourcename) {
        this.resourcename = resourcename;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public okhttp3.Address getAddress() {
        return address;
    }

    public void setAddress(okhttp3.Address address) {
        this.address = address;
    }

    public Boolean getSecurityQuestionFlag() {
        return securityQuestionFlag;
    }

    public void setSecurityQuestionFlag(Boolean securityQuestionFlag) {
        this.securityQuestionFlag = securityQuestionFlag;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

}