package com.loancare.lakeview.Utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation
{
    public  static  boolean isValidEmail (String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public  static boolean isValidName (String name)
    {
        String Name_Pattern ="^[a-zA-z]+$";
        Pattern pattern = Pattern.compile(Name_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();

    }

    public  static boolean isValidPhone (String name)
    {
        if(name.length()==10)
        {
            return true;
        }
         return false;
    }

    public  static boolean isValidPassword (String pass)
    {
        if(pass!=null&&pass.length()>5)
        {
            return true;
        }

        return false;
    }

    public  static boolean isValidEmpty (String val)
    {
        if(val==null || val.length()==0)
        {
            return true;
        }
        return false;
    }


    public static boolean isEmpty(String val) {
        if (val == null ||val.length()==0) {
            return true;
        }
        return false;
    }

}
