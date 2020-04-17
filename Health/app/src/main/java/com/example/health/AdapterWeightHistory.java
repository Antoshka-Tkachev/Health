package com.example.health;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterWeightHistory extends RecyclerView.Adapter<AdapterWeightHistory.WeightViewHolder>{

    private Activity context;
    private List<ValueWeightHelper> history;
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");


    public AdapterWeightHistory(List<ValueWeightHelper> history, Activity context) {
        this.history = history;
        this.context = context;
    }

    @Override
    public WeightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForItem = R.layout.item_weight_history;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idForItem, parent, false);

        WeightViewHolder weightViewHolder = new WeightViewHolder(view);
        return weightViewHolder;
    }

    @Override
    public void onBindViewHolder(WeightViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class WeightViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date;
        private TextView tv_valueWeight;
        private TextView tv_difference;

        public WeightViewHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date_iwh);
            tv_valueWeight = itemView.findViewById(R.id.tv_valueWeight_iwh);
            tv_difference = itemView.findViewById(R.id.tv_difference_iwh);
        }

        void bind(int position) {
            Calendar date = Calendar.getInstance();
            date.set(history.get(position).getYear(),
                    history.get(position).getMonth() - 1,
                    history.get(position).getDay());
            tv_date.setText(format.format(date.getTime()));

            tv_valueWeight.setText(String.format("%.1f", history.get(position).getWeight()));

            if (position == history.size() - 1) {
                tv_difference.setText(String.valueOf(0));
                tv_difference.setTextColor(context.getResources().getColor(R.color.greyDark));
            } else {
                double difference
                        = history.get(position).getWeight() - history.get(position + 1).getWeight();
                if (difference < 0) {
                    tv_difference.setText(String.format("%.1f", difference));
                    tv_difference.setTextColor(context.getResources().getColor(R.color.green));
                } else if (difference > 0) {
                    String textDiff = "+" + String.format("%.1f", difference);
                    tv_difference.setText(textDiff);
                    tv_difference.setTextColor(context.getResources().getColor(R.color.redDark));
                } else {
                    tv_difference.setText(String.valueOf(0));
                    tv_difference.setTextColor(context.getResources().getColor(R.color.greyDark));
                }
            }
        }

    }
}
