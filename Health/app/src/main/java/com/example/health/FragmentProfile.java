package com.example.health;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentProfile extends Fragment {

    private UserProfile userProfile;

    private TextView tv_name;
    private TextView tv_valueAge;
    private TextView tv_valueHeight;
    private TextView tv_valueWeight;
    private TextView tv_valueGender;
    private ImageView iv_userPicture;
    private ImageView iv_settingsProfile;

    public FragmentProfile() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userProfile = UserProfile.getInstance();

        //Получаем по id
        tv_name = view.findViewById(R.id.tv_name);
        tv_valueAge = view.findViewById(R.id.tv_valueAge);
        tv_valueHeight = view.findViewById(R.id.tv_valueHeight);
        tv_valueWeight = view.findViewById(R.id.tv_valueWeight);
        tv_valueGender = view.findViewById(R.id.tv_valueGender);
        iv_userPicture = view.findViewById(R.id.iv_userPictureProf);
        iv_settingsProfile  = getActivity().findViewById(R.id.iv_settingsProfile);

        iv_settingsProfile.setImageResource(R.drawable.ic_create);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        printInitialInformation();
    }

    private void printInitialInformation() {
        //set'аем занчения из userProfile
        String name = userProfile.getFirstName() + " " + userProfile.getLastName();
        tv_name.setText(name);

        if (userProfile.getHeight() % 1 == 0) {
            tv_valueHeight.setText(String.valueOf((int)userProfile.getHeight()));
        } else {
            tv_valueHeight.setText(String.valueOf(userProfile.getHeight()));
        }

        if (userProfile.getWeight() % 1 == 0) {
            tv_valueWeight.setText(String.valueOf((int)userProfile.getWeight()));
        } else {
            tv_valueWeight.setText(String.valueOf(userProfile.getWeight()));
        }

        tv_valueAge.setText(String.valueOf(userProfile.getAge()));
        tv_valueGender.setText(String.valueOf(userProfile.getGender()));
        iv_userPicture.setImageBitmap(userProfile.getUserPicture());
        iv_settingsProfile.setVisibility(View.VISIBLE);
        iv_settingsProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySettingsProfile.class);
                startActivity(intent);
            }
        });
    }

}
