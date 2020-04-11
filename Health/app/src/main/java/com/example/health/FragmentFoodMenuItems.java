package com.example.health;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentFoodMenuItems extends Fragment implements View.OnClickListener {

    private ValueUserFoodMenu userFoodMenu;
    private ArrayList<ValueUserFoodMenuHelper> productsInFoodMenu;
    private ArrayList<String> productNames;
    private ArrayList<String> productNamesHelper;
    private ArrayAdapter<String> adapter;
    private TableProduct tableProduct;
    private TableUserFoodMenu tableUserFoodMenu;
    private int dayOfWeek;
    private int modeEating;
    private double valueProtein;
    private double valueFat;
    private double valueCarbohydrate;
    private double valueCalories;
    private double weight;

    private AutoCompleteTextView actv_addProduct;
    private EditText et_weightProduct;
    private ListView lv_products;
    private TextView tv_dayOfWeek;
    private TextView tv_valueProtein;
    private TextView tv_valueFat;
    private TextView tv_valueCarbohydrate;
    private TextView tv_valueCalories;
    private Button btn_addProduct;
    private ToggleButton tb_breakfast;
    private ToggleButton tb_lunch;
    private ToggleButton tb_dinner;
    private View view;

    public static FragmentFoodMenuItems newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("pos", position);
        FragmentFoodMenuItems f = new FragmentFoodMenuItems();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food_menu_items, parent, false);

        lv_products = view.findViewById(R.id.lv_products_fmp);
        actv_addProduct = view.findViewById(R.id.actv_addProduct_fmp);
        et_weightProduct = view.findViewById(R.id.et_weightProduct);
        tv_dayOfWeek = view.findViewById(R.id.tv_dayOfWeek_fmp);
        tv_valueProtein = view.findViewById(R.id.tv_valueProtein_fmp);
        tv_valueFat = view.findViewById(R.id.tv_valueFat_fmp);
        tv_valueCarbohydrate = view.findViewById(R.id.tv_valueCarbohydrate_fmp);
        tv_valueCalories = view.findViewById(R.id.tv_valueCalories_fmp);

        btn_addProduct = view.findViewById(R.id.btn_addProduct_fmp);
        tb_breakfast = view.findViewById(R.id.tb_breakfast_fmp);
        tb_lunch = view.findViewById(R.id.tb_lunch_fmp);
        tb_dinner = view.findViewById(R.id.tb_dinner_fmp);

        btn_addProduct .setOnClickListener(this);
        tb_breakfast.setOnClickListener(this);
        tb_lunch.setOnClickListener(this);
        tb_dinner.setOnClickListener(this);

        setInitialInformation();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        tb_breakfast.setChecked(true);
        tb_lunch.setChecked(false);
        tb_dinner.setChecked(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        productNamesHelper = tableProduct.selectProductNames();

        actv_addProduct.addTextChangedListener(textWatcher);
        actv_addProduct.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productNamesHelper));
    }

    private void setInitialInformation() {
        userFoodMenu = ValueUserFoodMenu.getInstance();
        tableProduct = new TableProduct(getActivity());
        tableUserFoodMenu = new TableUserFoodMenu(getActivity());

        modeEating = 1;

        this.dayOfWeek = getArguments().getInt("pos");
        tv_dayOfWeek.setText(CalendarText.getNameDayOfWeek(dayOfWeek));

        updateListViewProducts();
        updateCaloriesAndPFC();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_breakfast_fmp:
                onClickBreakfast();
                break;
            case R.id.tb_lunch_fmp:
                onClickLunch();
                break;
            case R.id.tb_dinner_fmp:
                onClickDinner();
                break;
            case R.id.btn_addProduct_fmp:
                onClickAddProduct();
                break;

        }
    }

    private void onClickBreakfast() {
        if (modeEating == 1) {
            tb_breakfast.setChecked(true);
            return;
        }
        modeEating = 1;
        tb_breakfast.setChecked(true);
        tb_lunch.setChecked(false);
        tb_dinner.setChecked(false);

        actv_addProduct.setText("");
        et_weightProduct.setText("");
        updateListViewProducts();
        updateCaloriesAndPFC();
    }

    private void onClickLunch() {
        if (modeEating == 2) {
            tb_lunch.setChecked(true);
            return;
        }
        modeEating = 2;
        tb_breakfast.setChecked(false);
        tb_lunch.setChecked(true);
        tb_dinner.setChecked(false);

        actv_addProduct.setText("");
        et_weightProduct.setText("");
        updateListViewProducts();
        updateCaloriesAndPFC();
    }

    private void onClickDinner() {
        if (modeEating == 3) {
            tb_dinner.setChecked(true);
            return;
        }
        modeEating = 3;
        tb_breakfast.setChecked(false);
        tb_lunch.setChecked(false);
        tb_dinner.setChecked(true);

        actv_addProduct.setText("");
        et_weightProduct.setText("");
        updateListViewProducts();
        updateCaloriesAndPFC();
    }

    private void onClickAddProduct() {
        String nameProduct = String.valueOf(actv_addProduct.getText());
        String weightProduct = String.valueOf(et_weightProduct.getText());

        if (!weightProduct.matches("\\d{1,4}")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ошибка")
                    .setMessage("Вес порции - целое число от 0 до 9999 г.")
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
            return;
        }

        if (!tableProduct.isExistRecord(nameProduct)) {
            showDialogAddNewProduct();
            return;
        } else {
            Product product = tableProduct.selectProduct(nameProduct);

            ValueUserFoodMenuHelper buffer = new ValueUserFoodMenuHelper();
            buffer.setDayOfWeek(dayOfWeek);
            buffer.setEating(modeEating);
            buffer.setWeight(Integer.valueOf(weightProduct));
            buffer.setProductName(product.getName());
            buffer.setProductCalories(product.getCalories());
            buffer.setProductProtein(product.getProtein());
            buffer.setProductFat(product.getFat());
            buffer.setProductCarbohyd(product.getCarbohydrate());

            tableUserFoodMenu.insertProduct(buffer);
            userFoodMenu.getProductsInFoodMenu().add(buffer);
            productsInFoodMenu.add(buffer);
            addItemListViewProducts(nameProduct);
            updateCaloriesAndPFC();

            actv_addProduct.setText("");
            et_weightProduct.setText("");
        }
    }

    private void updateCaloriesAndPFC() {
        valueProtein = 0;
        valueFat = 0;
        valueCarbohydrate = 0;
        valueCalories = 0;
        weight = 0;

        for (int i = 0; i < userFoodMenu.getProductsInFoodMenu().size(); i++) {
            if (userFoodMenu.getProductsInFoodMenu().get(i).getDayOfWeek() == dayOfWeek &&
                    userFoodMenu.getProductsInFoodMenu().get(i).getEating() == modeEating) {
                weight = ((double) userFoodMenu.getProductsInFoodMenu().get(i).getWeight()) / 100;
                valueCalories += userFoodMenu.getProductsInFoodMenu().get(i).getProductCalories() * weight;
                valueProtein += userFoodMenu.getProductsInFoodMenu().get(i).getProductProtein() * weight;
                valueFat += userFoodMenu.getProductsInFoodMenu().get(i).getProductFat() * weight;
                valueCarbohydrate += userFoodMenu.getProductsInFoodMenu().get(i).getProductCarbohyd() * weight;
            }
        }

        tv_valueCalories.setText(String.format("%.2f", valueCalories));
        tv_valueProtein.setText(String.format("%.2f", valueProtein));
        tv_valueFat.setText(String.format("%.2f", valueFat));
        tv_valueCarbohydrate.setText(String.format("%.2f", valueCarbohydrate));
    }

    private void updateListViewProducts() {
        productsInFoodMenu = new ArrayList<>();
        productNames = new ArrayList<>();
        for (int i = 0; i < userFoodMenu.getProductsInFoodMenu().size(); i++) {
            if (userFoodMenu.getProductsInFoodMenu().get(i).getDayOfWeek() == dayOfWeek &&
                    userFoodMenu.getProductsInFoodMenu().get(i).getEating() == modeEating) {
                productsInFoodMenu.add(userFoodMenu.getProductsInFoodMenu().get(i));
                productNames.add(userFoodMenu.getProductsInFoodMenu().get(i).getProductName());
            }
        }
        adapter = new ArrayAdapter<>(getActivity(),
                R.layout.item_products, productNames);
        lv_products.setOnItemClickListener(itemProductsListener);
        lv_products.setAdapter(adapter);
    }

    private void addItemListViewProducts(String nameProduct) {
        productNames.add(nameProduct);
        adapter.notifyDataSetChanged();
    }

    private void showDialogAddNewProduct() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        final EditText et_nameProduct = dialogView.findViewById(R.id.et_nameProduct);
        final EditText et_calories = dialogView.findViewById(R.id.et_calories);
        final EditText et_protein = dialogView.findViewById(R.id.et_protein);
        final EditText et_fat = dialogView.findViewById(R.id.et_fat);
        final EditText et_carbohydrate = dialogView.findViewById(R.id.et_carbohydrate);
        final TextView tv_error = dialogView.findViewById(R.id.tv_errorAddProduct);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                productNamesHelper.add(nameProduct);
                actv_addProduct.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productNamesHelper));

                dialog.dismiss();
            }
        });
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
            final ValueUserFoodMenuHelper item = productsInFoodMenu.get(position);
            String information =
                    "Вес порции " + item.getWeight() + " г.\n" +
                    "кКал " + String.format("%.2f", item.getProductCalories() * item.getWeight() / 100)  + "\n" +
                    "Белки " + String.format("%.2f", item.getProductProtein() * item.getWeight() / 100) + " г.\n" +
                    "Жиры " + String.format("%.2f", item.getProductFat() * item.getWeight() / 100) + " г.\n" +
                    "Углев. " + String.format("%.2f", item.getProductCarbohyd() * item.getWeight() / 100) + " г.";

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_information_about_product, null);
            TextView tv_nameProduct = dialogView.findViewById(R.id.tv_nameProduct_dialog);
            TextView tv_infoCaloriesPFC = dialogView.findViewById(R.id.tv_infoCaloriesPFC_dialog);
            tv_nameProduct.setText(item.getProductName());
            tv_infoCaloriesPFC.setText(information);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(dialogView)
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int bufferPosition = -1;
                            for (int i = 0; i < userFoodMenu.getProductsInFoodMenu().size(); i++) {
                                if (item.getDayOfWeek() == userFoodMenu.getProductsInFoodMenu().get(i).getDayOfWeek() &&
                                    item.getEating() == userFoodMenu.getProductsInFoodMenu().get(i).getEating() &&
                                    item.getProductName().equals(userFoodMenu.getProductsInFoodMenu().get(i).getProductName()) &&
                                    item.getWeight() == userFoodMenu.getProductsInFoodMenu().get(i).getWeight()) {
                                    bufferPosition = i;
                                    break;
                                }
                            }

                            if (bufferPosition != -1) {
                                productNames.remove(position);
                                productsInFoodMenu.remove(position);
                                userFoodMenu.getProductsInFoodMenu().remove(bufferPosition);
                                tableUserFoodMenu.deleteRecord(item);
                                if (userFoodMenu.getProductsInFoodMenu().size() == 0) {
                                    tableUserFoodMenu.updatePosition(userFoodMenu.getNumber());
                                    getActivity().finish();
                                }
                                updateCaloriesAndPFC();
                                adapter.notifyDataSetChanged();
                            }
                            dialog.cancel();
                        }
                    }).show();
        }
    };
}
