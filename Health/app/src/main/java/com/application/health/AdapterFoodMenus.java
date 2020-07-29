package com.application.health;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AdapterFoodMenus extends RecyclerView.Adapter<AdapterFoodMenus.MenusViewHolder>{

    private ArrayList<String> menus;
    private boolean isYourMenu;
    private Activity context;

    public AdapterFoodMenus(Activity context, ArrayList<String> menus, boolean isYourMenu) {
        this.context = context;
        this.menus = menus;
        this.isYourMenu = isYourMenu;
    }

    @Override
    public MenusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForItem = R.layout.item_food_menus;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idForItem, parent, false);

        MenusViewHolder menusViewHolder = new MenusViewHolder(view);
        return menusViewHolder;
    }

    @Override
    public void onBindViewHolder(MenusViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    class MenusViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        int position;

        public MenusViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name_menu);
            if (isYourMenu) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityFoodMenuPager.class);
                        intent.putExtra("position", position);
                        context.startActivity(intent);
                    }
                });
            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityRecommendedMenu.class);
                        intent.putExtra("position", position);
                        context.startActivity(intent);
                    }
                });
            }
        }

        void bind(int number) {
            position = number;
            textView.setText(menus.get(position));
        }

    }
}
