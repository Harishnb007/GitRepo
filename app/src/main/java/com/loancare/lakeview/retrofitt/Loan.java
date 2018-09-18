package com.loancare.lakeview.retrofitt;



public class Loan
{
    public String property_address;
    public String loan_number;

    public String getLoan_number()
    {
        return loan_number;
    }

    public void setLoan_number(String loan_number)
    {
        this.loan_number = loan_number;
    }

    public String getProperty_address()
    {
        return property_address;
    }

    public void setProperty_address(String property_address)
    {
        this.property_address = property_address;
    }

}
