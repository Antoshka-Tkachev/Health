package com.example.health;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserProfile {

    private long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private float height;
    private float weight;
    private int age;
    private String gender;
    private String dateOfBirth;
    private int remember;

    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");


    private static UserProfile instance;

    private UserProfile(){}

    public static UserProfile getInstance(){
        if(instance == null){		//если объект еще не создан
            instance = new UserProfile();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getRemember() {
        return remember;
    }

    public void setRemember(int remember) {
        this.remember = remember;
    }

    public DateFormat getFormat() {
        return format;
    }

    public int ageCalculation(String textDOB) {

        Date dob = new Date();
        try {
            dob = format.parse(textDOB);
        } catch (ParseException e) {
            Log.d("EXCEPTION", e.getMessage());
        }

        Calendar thisDate = Calendar.getInstance();
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.setTime(dob);

        int year1 = thisDate.get(Calendar.YEAR);
        int year2 = dateOfBirth.get(Calendar.YEAR);
        int age = year1 - year2;
        int month1 = thisDate.get(Calendar.MONTH);
        int month2 = dateOfBirth.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = thisDate.get(Calendar.DAY_OF_MONTH);
            int day2 = dateOfBirth.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }

        return age;
    }

}
