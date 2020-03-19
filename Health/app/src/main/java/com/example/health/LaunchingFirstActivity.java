package com.example.health;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.Date;

class LaunchingFirstActivity extends AsyncTask<Void, Void, String> {

    private Activity splashScreen;
    private TableUserProfiles tableUserProfiles;

    LaunchingFirstActivity(Activity splashScreen) {
        this.splashScreen = splashScreen;
        tableUserProfiles = new TableUserProfiles(splashScreen.getApplicationContext());
    }

    @Override
    protected String doInBackground(Void... voids) {
        Date start = new Date();
        if (tableUserProfiles.isRememberedUserExist()) {
            tableUserProfiles.reSignIn();
            tableUserProfiles.close();

            Date end = new Date();
            long time = end.getTime() - start.getTime();
            if (time >= 2500) {
                return "0";
            } else {
                try {
                    Thread.sleep(2500 - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "0";
            }

        } else {
            tableUserProfiles.close();

            Date end = new Date();
            long time = end.getTime() - start.getTime();
            if (time >= 2500) {
                return "1";
            } else {
                try {
                    Thread.sleep(2500 - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "1";
            }

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equals("0")) {
            Intent intent = new Intent(splashScreen, ActivityHome.class);
            splashScreen.startActivity(intent);
            splashScreen.finish();
        } else {
            Intent intent = new Intent(splashScreen, ActivityAuthorization.class);
            splashScreen.startActivity(intent);
            splashScreen.finish();
        }
    }

}