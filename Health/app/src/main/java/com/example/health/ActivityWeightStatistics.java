package com.example.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class ActivityWeightStatistics extends AppCompatActivity {

    private AdapterWeightHistory adapterWeightHistory;
    private RecyclerView rv_history;
    private TableValueWeight tableValueWeight;
    private List<ValueWeightHelper> history;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_statistics);
        rv_history = findViewById(R.id.rv_historyWeightStat);
        tableValueWeight = new TableValueWeight(this);
        history = tableValueWeight.selectFullInfo();
        layoutManager = new LinearLayoutManager(this);
        adapterWeightHistory = new AdapterWeightHistory(history, this);
        rv_history.setLayoutManager(layoutManager);
        rv_history.setHasFixedSize(true); //Тк знаем размер списка
        rv_history.setAdapter(adapterWeightHistory);
        itemTouchHelper.attachToRecyclerView(rv_history);
    }

    public void onClickBackscapeWeightStat(View v) {
        ActivityWeightStatistics.super.onBackPressed();
    }

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
                    if (position == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWeightStatistics.this);
                        builder.setTitle("Ошибка")
                                .setMessage("Нельзя удалять последнюю запись")
                                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        adapterWeightHistory.notifyDataSetChanged();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWeightStatistics.this);
                    builder.setTitle("Подтверждение")
                            .setMessage("Вы точно хотите удалить запись?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    tableValueWeight.deleteRecord(history.get(position));
                                    history.remove(position);
                                    adapterWeightHistory.notifyDataSetChanged();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    adapterWeightHistory.notifyDataSetChanged();
                                    dialog.cancel();
                                }
                            }).show();
                    adapterWeightHistory.notifyDataSetChanged();
                }
            }
    );

}
