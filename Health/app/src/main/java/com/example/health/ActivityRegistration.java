package com.example.health;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityRegistration extends AppCompatActivity {

    private UserProfile userProfile;
    private EditText et_login;
    private EditText et_password;
    private EditText et_replayPassword;
    private EditText et_firstName;
    private EditText et_lastName;
    private EditText et_height;
    private EditText et_weight;
    private EditText et_dateOfBirth;
    private RadioButton rbtn_genderBoy;
    private RadioButton rbtn_genderGirl;

    private String login;
    private String password;
    private String replayPassword;
    private String firstName;
    private String lastName;
    private String height;
    private String weight;
    private String dateOfBirth;
    private int age;

    private boolean correctly;
    private String errorMessage;

    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private TableUserProfiles tableUserProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userProfile = UserProfile.getInstance();
        tableUserProfiles = new TableUserProfiles(this);

        et_login = findViewById(R.id.et_loginReg);
        et_password = findViewById(R.id.et_passwordReg);
        et_replayPassword = findViewById(R.id.et_replayPasswordReg);
        et_firstName = findViewById(R.id.et_firstNameReg);
        et_lastName = findViewById(R.id.et_lastNameReg);
        et_height = findViewById(R.id.et_heightReg);
        et_weight = findViewById(R.id.et_weightReg);
        et_dateOfBirth = findViewById(R.id.et_dateOfBirthReg);
        rbtn_genderBoy = findViewById(R.id.rbtn_genderBoyReg);
        rbtn_genderGirl = findViewById(R.id.rbtn_genderGirlReg);

    }

    public void onClickSignUpReg(View v) {

        if (!checkOfEnteredData()) {
            return;
        }

        setInfoUserProfile();

        tableUserProfiles.signUp();
        tableUserProfiles.selectTable();
        tableUserProfiles.close();

        Intent intent = new Intent(this, ActivityHome.class);
        startActivity(intent);
        //finish();
    }

    private boolean checkOfEnteredData() {

        correctly = true;
        errorMessage = "";

        login = et_login.getText().toString();
        password = et_password.getText().toString();
        replayPassword = et_replayPassword.getText().toString();
        firstName = et_firstName.getText().toString();
        lastName = et_lastName.getText().toString();
        height = et_height.getText().toString();
        weight = et_weight.getText().toString();
        dateOfBirth = et_dateOfBirth.getText().toString();

        if (!login.matches("\\S+")){
            correctly = false;
            errorMessage += "Логин не может иметь пробелы\n";
        } else if (tableUserProfiles.isLoginExist(login)){
            correctly = false;
            errorMessage += "Данный логин занят\n";
        }

        if (password.equals(replayPassword)) {
            if (!password.matches("\\w+")) {
                correctly = false;
                errorMessage += "Пароль должен состоять только из букв, цифр и знака \'_\'\n";
            }
        } else {
            correctly = false;
            errorMessage += "Пароли не совпадают\n";
        }

        if (!firstName.matches("[\\w&&[^\\d_]]+")){
            correctly = false;
            errorMessage += "Некорректно введено имя\n";
        }

        if (!lastName.matches("[\\w&&[^\\d_]]+")){
            correctly = false;
            errorMessage += "Некорректно введена фамилия\n";
        }

        if (!(height.matches("\\d{2,3}\\.\\d") || height.matches("\\d{2,3}"))) {
            correctly = false;
            errorMessage += "Рост должен быть введен в формате ХХХ.Х\n";
        }

        if (!(weight.matches("\\d{1,3}\\.\\d") || weight.matches("\\d{1,3}"))){
            correctly = false;
            errorMessage += "Вес должен быть введен в формате XX.Х\n";
        }

        if (!dateOfBirth.matches("\\d{2}\\.\\d{2}\\.\\d{4}")){
            correctly = false;
            errorMessage += "Дата должена быть введена в формате ДД.MM.ГГГГ\n";
        } else {
            String[] date = dateOfBirth.split("\\.");
            if(Integer.parseInt(date[0]) > 31  || Integer.parseInt(date[1]) > 12 ||
                    Integer.parseInt(date[0]) == 0 || Integer.parseInt(date[1]) == 0) {
                correctly = false;
                errorMessage += "Введенной даты рождения не существует\n";
            }

            Date dob  = null;
            try {
                dob = format.parse(et_dateOfBirth.getText().toString());
            } catch (Exception e) {
                Log.d("EXCEPTION", e.getMessage());
            }
            Calendar thisDate = Calendar.getInstance();
            Calendar dateOfBirth = Calendar.getInstance();
            dateOfBirth.setTime(dob);
            if (dateOfBirth.after(thisDate)) {
                correctly = false;
                errorMessage += "Указана дата рождения в будущем\n";
            }
        }

        if(!(rbtn_genderBoy.isChecked() || rbtn_genderGirl.isChecked())) {
            correctly = false;
            errorMessage += "Выберите пол\n";
        }

        if(!correctly) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ошибка")
                    .setMessage(errorMessage)
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }

        return correctly;
    }

    public void setInfoUserProfile() {

        userProfile.setLogin(et_login.getText().toString());
        userProfile.setPassword(et_password.getText().toString());
        userProfile.setFirstName(et_firstName.getText().toString());
        userProfile.setLastName(et_lastName.getText().toString());
        userProfile.setHeight(Float.parseFloat(et_height.getText().toString()));
        userProfile.setWeight(Float.parseFloat(et_weight.getText().toString()));
        userProfile.setDateOfBirth(et_dateOfBirth.getText().toString());

        if (rbtn_genderBoy.isChecked()) {
            userProfile.setGender("М");
        } else {
            userProfile.setGender("Ж");
        }

        age = userProfile.ageCalculation(et_dateOfBirth.getText().toString());
        userProfile.setAge(age);

        userProfile.setRemember(1);
    }

}
