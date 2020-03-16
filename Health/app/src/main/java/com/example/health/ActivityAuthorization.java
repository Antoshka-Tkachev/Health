package com.example.health;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class ActivityAuthorization extends AppCompatActivity {

    private EditText et_login;
    private EditText et_password;
    private CheckBox cb_remember;

    private String login;
    private String password;

    private boolean correctly;
    private String errorMessage;

    private UserProfile userProfile;
    private TableUserProfiles tableUserProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        userProfile = UserProfile.getInstance();
        tableUserProfiles = new TableUserProfiles(this);

        et_login = findViewById(R.id.et_loginAuth);
        et_password = findViewById(R.id.et_passwordAuth);
        cb_remember = findViewById(R.id.cb_remember);
    }

    public void onClickSignInAuth(View v) {

        if (!checkOfEnteredData()) {
            return;
        }

        if (!tableUserProfiles.isUserProfileExist(login, password)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ошибка")
                .setMessage("Пользователя с такими данными не существует, " +
                        "проверьте корректность логина и пароля")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            return;
        } else {
            userProfile.setLogin(login);
            userProfile.setPassword(password);
            if (cb_remember.isChecked()) {
                userProfile.setRemember(1);
            } else {
                userProfile.setRemember(0);
            }
            tableUserProfiles.signIn();
            tableUserProfiles.close();
        }

        Intent intent = new Intent(this, ActivityHome.class);
        startActivity(intent);
        //finish();
    }

    public void onClickSignUpAuth(View v) {
        Intent intent = new Intent(this, ActivityRegistration.class);
        startActivity(intent);
        finish();
    }


    private boolean checkOfEnteredData() {

        correctly = true;
        errorMessage = "";

        login = et_login.getText().toString();
        password = et_password.getText().toString();

        if (!login.matches("\\S+")) {
            correctly = false;
            errorMessage += "Логин не может иметь пробелы\n";
        }

        if (!password.matches("\\w+")) {
            correctly = false;
            errorMessage += "Пароль должен состоять только из букв, цифр и знака \'_\'\n";
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
}
