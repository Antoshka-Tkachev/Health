package com.application.health;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterFoodMenuForDays extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ValueUserFoodMenu userFoodMenu;

    public AdapterFoodMenuForDays() {
        userFoodMenu = ValueUserFoodMenu.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 7) {
            return 0;
        } else {
            return 7;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.item_food_menu_for_day, parent, false);
            holder = new AdapterFoodMenuForDays.DayMenusViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_food_your_menu_result, parent, false);
            holder = new AdapterFoodMenuForDays.ResultMenusViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (this.getItemViewType(position) == 0) {
            DayMenusViewHolder dayHolder = (DayMenusViewHolder) holder;
            dayHolder.bind(position);
        } else {
            ResultMenusViewHolder resultHolder = (ResultMenusViewHolder) holder;
            resultHolder.bind();
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class DayMenusViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_dayOfWeek;
        private TextView tv_breakfast;
        private TextView tv_lunch;
        private TextView tv_dinner;
        private TextView tv_calories;
        private TextView tv_protein;
        private TextView tv_fat;
        private TextView tv_carbohydrate;

        public DayMenusViewHolder(View itemView) {
            super(itemView);
            tv_dayOfWeek = itemView.findViewById(R.id.tv_dayOfWeek_ifmfd);
            tv_breakfast = itemView.findViewById(R.id.tv_breakfast_ifmfd);
            tv_lunch = itemView.findViewById(R.id.tv_lunch_ifmfd);
            tv_dinner = itemView.findViewById(R.id.tv_dinner_ifmfd);
            tv_calories = itemView.findViewById(R.id.tv_calories_ifmfd);
            tv_protein = itemView.findViewById(R.id.tv_protein_ifmfd);
            tv_fat = itemView.findViewById(R.id.tv_fat_ifmfd);
            tv_carbohydrate = itemView.findViewById(R.id.tv_carbohydrate_ifmfd);
        }

        void bind(int position) {

            int dayOfWeek = position + 1;
            double calories = 0;
            double protein = 0;
            double fat = 0;
            double carbohydrate = 0;
            int countBreakfast = 1;
            int countLunch = 1;
            int countDinner = 1;
            StringBuilder breakfast = new StringBuilder();
            StringBuilder lunch = new StringBuilder();
            StringBuilder dinner = new StringBuilder();
            tv_dayOfWeek.setText(CalendarText.getNameDayOfWeek(dayOfWeek));
            for (int i = 0; i < userFoodMenu.getProductsInFoodMenu().size(); i++) {
                if (userFoodMenu.getProductsInFoodMenu().get(i).getDayOfWeek() == dayOfWeek) {
                    switch (userFoodMenu.getProductsInFoodMenu().get(i).getEating()) {
                        case 1:
                            breakfast.append(countBreakfast).append(". ")
                                    .append(userFoodMenu.getProductsInFoodMenu().get(i).getProductName())
                                    .append("   ")
                                    .append(userFoodMenu.getProductsInFoodMenu().get(i).getWeight())
                                    .append("г.")
                                    .append("\n");
                            countBreakfast++;
                            break;
                        case 2:
                            lunch.append(countLunch).append(". ")
                                    .append(userFoodMenu.getProductsInFoodMenu().get(i).getProductName())
                                    .append("   ")
                                    .append(userFoodMenu.getProductsInFoodMenu().get(i).getWeight())
                                    .append("г.")
                                    .append("\n");
                            countLunch++;
                            break;
                        case 3:
                            dinner.append(countDinner).append(". ")
                                    .append(userFoodMenu.getProductsInFoodMenu().get(i).getProductName())
                                    .append("   ")
                                    .append(userFoodMenu.getProductsInFoodMenu().get(i).getWeight())
                                    .append("г.")
                                    .append("\n");
                            countDinner++;
                            break;
                    }
                    calories += userFoodMenu.getProductsInFoodMenu().get(i).getProductCalories() *
                        userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
                    protein += userFoodMenu.getProductsInFoodMenu().get(i).getProductProtein() *
                            userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
                    fat += userFoodMenu.getProductsInFoodMenu().get(i).getProductFat() *
                            userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
                    carbohydrate += userFoodMenu.getProductsInFoodMenu().get(i).getProductCarbohyd() *
                            userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
                }
            }

            tv_breakfast.setText(breakfast);
            tv_lunch.setText(lunch);
            tv_dinner.setText(dinner);
            tv_calories.setText(String.format("%.2f", calories));
            tv_protein.setText(String.format("%.2f", protein));
            tv_fat.setText(String.format("%.2f", fat));
            tv_carbohydrate.setText(String.format("%.2f", carbohydrate));
        }
    }

    class ResultMenusViewHolder extends RecyclerView.ViewHolder {

        private double resultProtein;
        private double resultFat;
        private double resultCarbohydrate;
        private double resultCalories;

        private TextView tv_calories;
        private TextView tv_protein;
        private TextView tv_fat;
        private TextView tv_carbohydrate;

        public ResultMenusViewHolder(View itemView) {
            super(itemView);
            tv_calories = itemView.findViewById(R.id.tv_calories_ifmr);
            tv_protein = itemView.findViewById(R.id.tv_protein_ifmr);
            tv_fat = itemView.findViewById(R.id.tv_fat_ifmr);
            tv_carbohydrate = itemView.findViewById(R.id.tv_carbohydrate_ifmr);
        }

        void bind() {
            resultProtein = 0;
            resultFat = 0;
            resultCarbohydrate = 0;
            resultCalories = 0;

            for (int i = 0; i < userFoodMenu.getProductsInFoodMenu().size(); i++) {
                resultCalories += userFoodMenu.getProductsInFoodMenu().get(i).getProductCalories() *
                        userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
                resultProtein += userFoodMenu.getProductsInFoodMenu().get(i).getProductProtein() *
                        userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
                resultFat += userFoodMenu.getProductsInFoodMenu().get(i).getProductFat() *
                        userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
                resultCarbohydrate += userFoodMenu.getProductsInFoodMenu().get(i).getProductCarbohyd() *
                        userFoodMenu.getProductsInFoodMenu().get(i).getWeight() / 100;
            }

            tv_calories.setText(String.format("%.2f", resultCalories));
            tv_protein.setText(String.format("%.2f", resultProtein));
            tv_fat.setText(String.format("%.2f", resultFat));
            tv_carbohydrate.setText(String.format("%.2f", resultCarbohydrate));
        }
    }
}