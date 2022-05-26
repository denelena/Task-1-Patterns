package ru.netology;

/******************************************
 * Disclaimer: Like many developers I personally find Lombok evil, dangerous, counter-productive and plain abominable thing.
 * Therefore I absolutely refuse to utilize this questionable technique in my java code.
 *****************************************/
public class UserDataEntry {

    private String city;
    private String fullName;
    private String phoneNum;
    private String initialDate;
    private String rescheduledDate;

    public UserDataEntry(String city, String fullName, String phoneNum, String initialDate, String rescheduledDate) {
        this.city = city;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.initialDate = initialDate;
        this.rescheduledDate = rescheduledDate;
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getInitialDate() { return initialDate; }

    public String getRescheduledDate() { return rescheduledDate; }
}
