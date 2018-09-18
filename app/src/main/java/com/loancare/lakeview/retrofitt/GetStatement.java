package com.loancare.lakeview.retrofitt;

/**
 * Created by dinesh on 13/03/18.
 */

public class GetStatement {

    private  String LoanNumber;
    private  String Key;
    private  String date;

    public GetStatement(String loanNumber, String statementKey, String statementDate) {

        this.LoanNumber = loanNumber;
        this.Key = statementKey;
        this.date = statementDate;

    }


    public String getLoanNumber() {
        return LoanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        LoanNumber = loanNumber;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
