package com.application.health;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentNutritionControl extends Fragment implements View.OnClickListener {

    private Button btn_statistics;
    private Button btn_addDay;
    private Button btn_foodMenu;
    private Button btn_calorieCalculator;
    private ImageView iv_info;
    private RecyclerView rv_listDays;
    private DatePickerDialog dateDialog;

    private ArrayList<String> dates;
    private TableNutrControl tableNutrControl;
    private AdapterNutrControlDays adapterNutrControlDays;
    private LinearLayoutManager layoutManager;
    private FragmentTransaction transaction;
    private FragmentFoodMenu fragmentFoodMenu;
    private FragmentCalorieCalculator fragmentCalorieCalculator;
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_control, container, false);

        tableNutrControl = new TableNutrControl(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentFoodMenu = new FragmentFoodMenu();
        fragmentCalorieCalculator = new FragmentCalorieCalculator();

        rv_listDays = view.findViewById(R.id.rv_listDays_NutCont);
        btn_statistics = view.findViewById(R.id.btn_statisticsNutCont);
        btn_addDay = view.findViewById(R.id.btn_addDay_NutCont);
        btn_foodMenu = view.findViewById(R.id.btn_foodMenu_NutCont);
        btn_calorieCalculator = view.findViewById(R.id.btn_calorieCalculator_NutCont);
        iv_info = getActivity().findViewById(R.id.iv_settingsProfile);

        iv_info.setImageResource(R.drawable.ic_info);

        btn_statistics.setOnClickListener(this);
        btn_addDay.setOnClickListener(this);
        btn_foodMenu.setOnClickListener(this);
        btn_calorieCalculator.setOnClickListener(this);
        iv_info.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv_titleHome = getActivity().findViewById(R.id.tv_titleHome);
        tv_titleHome.setText("Контроль питания");
    }

    @Override
    public void onResume() {
        super.onResume();
        dates = tableNutrControl.selectDates();
        adapterNutrControlDays = new AdapterNutrControlDays(getActivity(), dates);
        rv_listDays.setLayoutManager(layoutManager);
        rv_listDays.setHasFixedSize(true);
        rv_listDays.setAdapter(adapterNutrControlDays);
        itemTouchHelper.attachToRecyclerView(rv_listDays);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_statisticsNutCont:
                onClickStatistics();
                break;
            case R.id.btn_addDay_NutCont:
                onClickAddDay();
                break;
            case R.id.btn_foodMenu_NutCont:
                onClickFoodMenu();
                break;
            case R.id.btn_calorieCalculator_NutCont:
                onClickCalorieCalculator();
                break;
            case R.id.iv_settingsProfile:
                onClickInfo();
                break;
        }
    }

    private void onClickStatistics() {
        Intent intent = new Intent(getActivity(), ActivityNutrControlStatistics.class);
        startActivity(intent);
    }

    private void onClickFoodMenu() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentFoodMenu);
        transaction.commit();
    }

    private void onClickCalorieCalculator() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentCalorieCalculator);
        transaction.commit();
    }

    private void onClickAddDay() {
        Calendar date = Calendar.getInstance();
        dateDialog = new DatePickerDialog(getActivity(), dateDialogListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    private void onClickInfo() {
        Intent intent = new Intent(getActivity(), ActivityDescription.class);
        intent.putExtra("mode", ModeDescription.NUTRITION_CONTROL);
        startActivity(intent);
    }

    private DatePickerDialog.OnDateSetListener dateDialogListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month,
                              int day) {
            Calendar thisDate = Calendar.getInstance();
            Calendar chooseDate = Calendar.getInstance();
            chooseDate.set(year, month, day, 0, 0, 0);
            String textChooseDate = format.format(chooseDate.getTime());
            //проверка введенной даты
            boolean correctly = true;
            String errorMessage = "";
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(textChooseDate)) {
                    correctly = false;
                    errorMessage = "Запись с указанной датой уже создана";
                }
            }
            if (chooseDate.after(thisDate)) {
                correctly = false;
                errorMessage = "Указана дата в будущем";
            }
            if (!correctly) {
                dateDialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Ошибка")
                        .setMessage(errorMessage)
                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                return;
            }
            //добавление в бд
            tableNutrControl.insertDay(chooseDate);
            //добавление на нужную позицию 1в списке
            if (dates.size() == 0) {
                dates.add(format.format(chooseDate.getTime()));
                adapterNutrControlDays.notifyDataSetChanged();
                return;
            }
            Calendar first = Calendar.getInstance();
            Calendar last = Calendar.getInstance();
            first.clear();
            last.clear();
            try {
                first.setTime(format.parse(dates.get(0)));
                last.setTime(format.parse(dates.get(dates.size() - 1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (last.after(chooseDate)) {
                dates.add(format.format(chooseDate.getTime()));
            } else if (first.before(chooseDate)) {
                dates.add(0, format.format(chooseDate.getTime()));
            } else {
                for (int i = 1; i < dates.size(); i++) {
                    try {
                        last.setTime(format.parse(dates.get(i)));
                        first.setTime(format.parse(dates.get(i - 1)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (last.before(chooseDate) && first.after(chooseDate)) {
                        dates.add(i, format.format(chooseDate.getTime()));
                        break;
                    }
                }
            }
            adapterNutrControlDays.notifyDataSetChanged();
        }
    };

    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_IDLE,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Подтверждение")
                            .setMessage("Вы точно хотите удалить запись?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Calendar date = Calendar.getInstance();
                                    try {
                                        date.setTime(format.parse(dates.get(position)));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    tableNutrControl.deleteDate(date);
                                    dates.remove(position);
                                    adapterNutrControlDays.notifyDataSetChanged();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    adapterNutrControlDays.notifyDataSetChanged();
                                    dialog.cancel();
                                }
                            }).show();
                    adapterNutrControlDays.notifyDataSetChanged();
                }
            }
    );
}
