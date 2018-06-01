package org.androidtown.shaketest;

/**
 * Data Format
 */
public class ContactData {

    private String name;
    private String phoneNum;
    private String email;
    private int template_no;

    public ContactData () {

    }

    public ContactData (String name, String phoneNum, String email, int template_no) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.template_no = template_no;
    }

    public void setName (String name) {
        this.name = name;
    }
    public void setPhoneNum (String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public void setTemplate_no (int template_no) {
        this.template_no = template_no;
    }

    public String getName () {return this.name;}
    public String getPhoneNum () {return this.phoneNum;}
    public String getEmail () {return this.email;}
    public int getTemplate_no () {return this.template_no;}
}
