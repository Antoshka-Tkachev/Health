package com.example.health;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterNutrControlDays extends RecyclerView.Adapter<AdapterNutrControlDays.DayViewHolder>{

    private ArrayList<String> date;
    private Activity context;

    public AdapterNutrControlDays(Activity context, ArrayList<String> date) {
        this.context = context;
        this.date = date;
    }

    @Override
    public AdapterNutrControlDays.DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForItem = R.layout.item_food_menus;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idForItem, parent, false);

        AdapterNutrControlDays.DayViewHolder dayViewHolder = new AdapterNutrControlDays.DayViewHolder(view);
        return dayViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterNutrControlDays.DayViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        int position;

        public DayViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name_menu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityAddingDayMenu.class);
                    intent.putExtra("date", textView.getText());
                    context.startActivity(intent);
                }
            });
        }

        void bind(int position) {
            textView.setText(date.get(position));
        }

    }
}
