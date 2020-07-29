package com.application.health;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivitySettingsProfile extends AppCompatActivity {

    private UserProfile userProfile;
    private ValueWeight valueWeight;
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
    private ImageView iv_userPicture;
    private Bitmap userPicture;

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
    private TableValueWeight tableValueWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);

        userProfile = UserProfile.getInstance();
        valueWeight = ValueWeight.getInstance();
        tableUserProfiles = new TableUserProfiles(this);
        tableValueWeight = new TableValueWeight(this);

        et_login = findViewById(R.id.et_loginSetProf);
        et_password = findViewById(R.id.et_passwordSetProf);
        et_replayPassword = findViewById(R.id.et_replayPasswordSetProf);
        et_firstName = findViewById(R.id.et_firstNameSetProf);
        et_lastName = findViewById(R.id.et_lastNameSetProf);
        et_height = findViewById(R.id.et_heightSetProf);
        et_weight = findViewById(R.id.et_weightSetProf);
        et_dateOfBirth = findViewById(R.id.et_dateOfBirthSetProf);
        rbtn_genderBoy = findViewById(R.id.rbtn_genderBoySetProf);
        rbtn_genderGirl = findViewById(R.id.rbtn_genderGirlSetProf);
        iv_userPicture = findViewById(R.id.iv_userPictureSetProf);

        printInitialInformation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userProfile.getUserPicture() != null) {
            iv_userPicture.setImageBitmap(userProfile.getUserPicture());
        } else {
            Bitmap bitmap = ((BitmapDrawable)iv_userPicture.getDrawable()).getBitmap();
            userProfile.setUserPicture(bitmap);
        }
    }

    public void onClickUserPictureSetProf(View v) {
        Intent intent = new Intent(this, ActivityImageSelection.class);
        startActivity(intent);
    }

    public void onClickSaveSetProf(View v) {

        if (!checkOfEnteredData()) {
            return;
        }

        //Если введенный вес не отличается от предыдущего, то запись заносить в БД веса не надо
        if (Float.parseFloat(et_weight.getText().toString()) == userProfile.getWeight()) {
            setInfoUserProfile();
            tableUserProfiles.updateRecord();
        } else {
            setInfoUserProfile();
            setInfoValueWeight();

            tableUserProfiles.updateRecord();
            if (tableValueWeight.isRecordExist()) {
                tableValueWeight.updateRecord();
            } else {
                tableValueWeight.insertRecord();
            }
        }

        finish();
    }

    private void printInitialInformation() {
        userPicture = userProfile.getUserPicture();
        et_login.setText(userProfile.getLogin());
        et_password.setText(userProfile.getPassword());
        et_replayPassword.setText(userProfile.getPassword());
        et_firstName.setText(userProfile.getFirstName());
        et_lastName.setText(userProfile.getLastName());
        et_height.setText(String.valueOf(userProfile.getHeight()));
        et_weight.setText(String.valueOf(userProfile.getWeight()));
        et_dateOfBirth.setText(userProfile.getDateOfBirth());
        if (userProfile.getGender().equals("М")) {
            rbtn_genderBoy.setChecked(true);
        } else {
            rbtn_genderGirl.setChecked(true);
        }
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
        } else if (tableUserProfiles.isLoginExist(login) && !login.equals(userProfile.getLogin())) {
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

    private void setInfoUserProfile() {

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

    }

    private void setInfoValueWeight() {
        Calendar thisDate = Calendar.getInstance();
        int day = thisDate.get(Calendar.DAY_OF_MONTH);
        int month = thisDate.get(Calendar.MONTH) + 1;
        int year = thisDate.get(Calendar.YEAR);

        valueWeight.setWeight(userProfile.getWeight());
        valueWeight.setDay(day);
        valueWeight.setMonth(month);
        valueWeight.setYear(year);
        valueWeight.setUserId(userProfile.getId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        userProfile.setUserPicture(userPicture);
    }
}
