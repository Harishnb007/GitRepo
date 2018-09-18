package com.loancare.lakeview.retrofitt;

/**
 * Created by 886016 on 3/15/2018.
 */

public class ForgetPasswordInput {

    private String ssn;
    private String loanNumber;

    public ForgetPasswordInput(String ssn,String loanNumber)
    {
        this.ssn = ssn;
        this.loanNumber = loanNumber;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }
}
