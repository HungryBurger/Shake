package org.androidtown.shaketest;

public class ContactInformation {
    String text1, text2;
    int image;

    public String getText1() {
        return text1;
    }
    public void setText1(String text1) {
        this.text1 = text1;
    }
    public String getText2() {
        return text2;
    }
    public void setText2(String text2) {
        this.text2 = text2;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public ContactInformation(String text1, String text2, int image) {
        this.text1 = text1;
        this.text2 = text2;
        this.image = image;
    }
}
