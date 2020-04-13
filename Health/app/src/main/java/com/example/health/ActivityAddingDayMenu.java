package com.example.health;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityAddingDayMenu extends AppCompatActivity {

    private TextView tv_date;
    private TextView tv_valueCalories;
    private TextView tv_valueProtein;
    private TextView tv_valueFat;
    private TextView tv_valueCarbohydrate;
    private EditText et_weightProduct;
    private AutoCompleteTextView actv_addProduct;
    private ListView lv_products;

    private double valueProtein;
    private double valueFat;
    private double valueCarbohydrate;
    private double valueCalories;
    private double weight;
    private String dateText;
    private Calendar date;
    private ArrayAdapter<String> adapter;
    private ArrayList<ValueUserFoodMenuHelper> products;
    private ArrayList<String> nameProducts;
    private ArrayList<String> nameProductsHelper;
    private TableNutrControl tableNutrControl;
    private TableProduct tableProduct;
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_day_menu);
        tv_date = findViewById(R.id.tv_date_adm);
        tv_valueCalories = findViewById(R.id.tv_valueCalories_adm);
        tv_valueProtein = findViewById(R.id.tv_valueProtein_adm);
        tv_valueFat = findViewById(R.id.tv_valueFat_adm);
        tv_valueCarbohydrate = findViewById(R.id.tv_valueCarbohydrate_adm);
        et_weightProduct = findViewById(R.id.et_weightProduct_adm);
        actv_addProduct = findViewById(R.id.actv_addProduct_adm);
        lv_products = findViewById(R.id.lv_products_adm);

        nameProducts = new ArrayList<>();
        tableNutrControl = new TableNutrControl(this);
        tableProduct = new TableProduct(this);
        Intent intent = getIntent();
        dateText = intent.getStringExtra("date");
        date = Calendar.getInstance();
        try {
            date.setTime(format.parse(dateText));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_date.setText(dateText);
        products = tableNutrControl.selectProducts(date);
        for (int i = 0; i < products.size(); i++) {
            nameProducts.add(products.get(i).getProductName());
        }

        nameProductsHelper = tableProduct.selectProductNames();
        actv_addProduct.addTextChangedListener(textWatcher);
        actv_addProduct.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameProductsHelper));

        adapter = new ArrayAdapter<>(this,
                R.layout.item_products, nameProducts);
        lv_products.setOnItemClickListener(itemProductsListener);
        lv_products.setAdapter(adapter);

        updateCaloriesAndPFC();
    }

    public void onClickAddProduct(View v) {
        String nameProduct = String.valueOf(actv_addProduct.getText());
        String weightProduct = String.valueOf(et_weightProduct.getText());

        //проверка введеных данных
        String errorMessage = "";
        boolean correctly = true;
        if (tableNutrControl.isExistProduct(nameProduct, weightProduct, date)) {
            correctly = false;
            errorMessage = "Данный продукт уже в списке";
        }
        if (!weightProduct.matches("\\d{1,4}")) {
            correctly = false;
            errorMessage = "Вес порции - целое число от 0 до 9999 г.";
        }
        if (!correctly) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ошибка")
                    .setMessage(errorMessage)
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
            return;
        }

        //Если продукта нет в базе, то добавляем в базу, иначе добавляем сразу в список
        if (!tableProduct.isExistRecord(nameProduct)) {
            showDialogAddNewProduct();
            return;
        } else {
            Product product = tableProduct.selectProduct(nameProduct);

            ValueUserFoodMenuHelper buffer = new ValueUserFoodMenuHelper();
            buffer.setWeight(Integer.valueOf(weightProduct));
            buffer.setProductName(product.getName());
            buffer.setProductCalories(product.getCalories());
            buffer.setProductProtein(product.getProtein());
            buffer.setProductFat(product.getFat());
            buffer.setProductCarbohyd(product.getCarbohydrate());

            tableNutrControl.insertProduct(buffer, date);
            products.add(buffer);
            nameProducts.add(nameProduct);
            adapter.notifyDataSetChanged();
            updateCaloriesAndPFC();

            actv_addProduct.setText("");
            et_weightProduct.setText("");
        }
    }

    private void showDialogAddNewProduct() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        final EditText et_nameProduct = dialogView.findViewById(R.id.et_nameProduct);
        final EditText et_calories = dialogView.findViewById(R.id.et_calories);
        final EditText et_protein = dialogView.findViewById(R.id.et_protein);
        final EditText et_fat = dialogView.findViewById(R.id.et_fat);
        final EditText et_carbohydrate = dialogView.findViewById(R.id.et_carbohydrate);
        final TextView tv_error = dialogView.findViewById(R.id.tv_errorAddProduct);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("Ок", null);
        et_nameProduct.setText(actv_addProduct.getText());

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameProduct = String.valueOf(et_nameProduct.getText());
                String calories = String.valueOf(et_calories.getText());
                String protein = String.valueOf(et_protein.getText());
                String fat = String.valueOf(et_fat.getText());
                String carbohydrate = String.valueOf(et_carbohydrate.getText());

                String errorMessage = "";
                boolean correctly = true;

                if (!nameProduct.matches(".+")) {
                    correctly = false;
                    errorMessage += "Название должно содержать минимум 1 символ\n";
                } else if (!(calories.matches("\\d{1,3}\\.\\d{1,2}") ||
                        calories.matches("\\d{1,3}"))) {
                    correctly = false;
                    errorMessage += "Кол-во кКал должено быть введено в формате XX.XX\n";
                } else if (!(protein.matches("\\d{1,3}\\.\\d{1,2}") ||
                        protein.matches("\\d{1,3}"))) {
                    correctly = false;
                    errorMessage += "Кол-во белков должено быть введено в формате XX.XX\n";
                } else if (!(fat.matches("\\d{1,3}\\.\\d{1,2}") ||
                        fat.matches("\\d{1,3}"))) {
                    correctly = false;
                    errorMessage += "Кол-во жиров должено быть введено в формате XX.XX\n";
                } else if (!(carbohydrate.matches("\\d{1,3}\\.\\d{1,2}") ||
                        carbohydrate.matches("\\d{1,3}"))) {
                    correctly = false;
                    errorMessage += "Кол-во углеводов должено быть введено в формате XX.XX\n";
                } else if (tableProduct.isExistRecord(nameProduct)) {
                    correctly = false;
                    errorMessage += "Продукт с таким именем существует в базе";
                }

                if (!correctly) {
                    tv_error.setText(errorMessage);
                    return;
                }

                Product product = new Product();
                product.setName(nameProduct);
                product.setCalories(Double.parseDouble(calories));
                product.setProtein(Double.parseDouble(protein));
                product.setFat(Double.parseDouble(fat));
                product.setCarbohydrate(Double.parseDouble(carbohydrate));
                tableProduct.insertProduct(product);

                nameProductsHelper.add(nameProduct);
                actv_addProduct.setAdapter(new ArrayAdapter<>(ActivityAddingDayMenu.this, android.R.layout.simple_list_item_1, nameProductsHelper));

                dialog.dismiss();
            }
        });
    }

    private void updateCaloriesAndPFC() {
        valueProtein = 0;
        valueFat = 0;
        valueCarbohydrate = 0;
        valueCalories = 0;
        weight = 0;

        for (int i = 0; i < products.size(); i++) {
            weight = ((double) products.get(i).getWeight()) / 100;
            valueCalories += products.get(i).getProductCalories() * weight;
            valueProtein += products.get(i).getProductProtein() * weight;
            valueFat += products.get(i).getProductFat() * weight;
            valueCarbohydrate += products.get(i).getProductCarbohyd() * weight;
        }

        tv_valueCalories.setText(String.format("%.2f", valueCalories));
        tv_valueProtein.setText(String.format("%.2f", valueProtein));
        tv_valueFat.setText(String.format("%.2f", valueFat));
        tv_valueCarbohydrate.setText(String.format("%.2f", valueCarbohydrate));
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private AdapterView.OnItemClickListener itemProductsListener =  new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {
            final ValueUserFoodMenuHelper item = products.get(position);
            String information =
                    "Вес порции " + item.getWeight() + " г.\n" +
                            "кКал " + String.format("%.2f", item.getProductCalories() * item.getWeight() / 100)  + "\n" +
                            "Белки " + String.format("%.2f", item.getProductProtein() * item.getWeight() / 100) + " г.\n" +
                            "Жиры " + String.format("%.2f", item.getProductFat() * item.getWeight() / 100) + " г.\n" +
                            "Углев. " + String.format("%.2f", item.getProductCarbohyd() * item.getWeight() / 100) + " г.";

            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_information_about_product, null);
            TextView tv_nameProduct = dialogView.findViewById(R.id.tv_nameProduct_dialog);
            TextView tv_infoCaloriesPFC = dialogView.findViewById(R.id.tv_infoCaloriesPFC_dialog);
            tv_nameProduct.setText(item.getProductName());
            tv_infoCaloriesPFC.setText(information);

            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddingDayMenu.this);
            builder.setView(dialogView)
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                nameProducts.remove(position);
                                products.remove(position);
                                tableNutrControl.deleteProduct(item, date);
                                if (products.size() == 0) {
                                    finish();
                                }
                                updateCaloriesAndPFC();
                                adapter.notifyDataSetChanged();

                            dialog.cancel();
                        }
                    }).show();
        }
    };
}
