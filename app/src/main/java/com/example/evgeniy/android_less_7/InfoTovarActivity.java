package com.example.evgeniy.android_less_7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeniy.android_less_7.ListTovarActivity;
import com.example.evgeniy.android_less_7.model.Category;
import com.example.evgeniy.android_less_7.model.Data;
import com.example.evgeniy.android_less_7.model.SupplierData;
import com.example.evgeniy.android_less_7.service.GetCategoryService;
import com.example.evgeniy.android_less_7.service.GetDataService;
import com.example.evgeniy.android_less_7.service.GetSupplierService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoTovarActivity extends AppCompatActivity {
    private LinearLayout llt;
    private RadioGroup rbg;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView c11;
    private TextView logview;
    private RadioGroup radio;
    private String s_id;
    private String sup_id;
    private int id;
    private String data_id;
    private String category_name;
    private Integer selected_sup;
    public ArrayAdapter<String> adapter;
    public List<String> btn_adapter = new ArrayList<String>();
    public List<String> categorys = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tovar);
        data_id = getIntent().getStringExtra("data_id");
        s_id = getIntent().getStringExtra("s_id");

        if (data_id.equals("0")) {
            ImageButton sup_btn = (ImageButton) findViewById(R.id.imageButton3);
            sup_btn.setVisibility(View.INVISIBLE);
            GetSupplierlist();
            GetListCategory();
            SetSpinner();
        } else {
            getDataById(data_id);
        }

    }

    public void getDataById(String data_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://php7.demo20277.atservers.net/web/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetDataService service = retrofit.create(GetDataService.class);
        Call<List<Data>> repos = service.getDataById(String.valueOf(data_id));


        repos.enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                final List<Data> data = response.body();
                int size = data.size();
                EditText t1 = (EditText) findViewById(R.id.editText);
                EditText t2 = (EditText) findViewById(R.id.editText3);
                EditText t3 = (EditText) findViewById(R.id.editText4);
                EditText c11 = (EditText) findViewById(R.id.editText5);

                for (int i = 0; i < data.size(); i++) {
                    id = data.get(i).getId();
                    String T1 = data.get(i).getT1();
                    String T2 = data.get(i).getT2();
                    String T3 = data.get(i).getT3();
                    String C11 = data.get(i).getC11();

                    t1.setText(T1);
                    t2.setText(T2);
                    t3.setText(T3);
                    c11.setText(C11);

                    sup_id = data.get(i).getSup_id();
                }
            }

            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {

            }
        });
    }

    public void saveGood(View view) {
        final EditText t11 = (EditText) findViewById(R.id.editText);
        EditText t22 = (EditText) findViewById(R.id.editText3);
        EditText t33 = (EditText) findViewById(R.id.editText4);
        EditText c111 = (EditText) findViewById(R.id.editText5);
        RadioGroup radio = (RadioGroup) findViewById(R.id.RRG);

        String tx1 = t11.getText().toString();
        String tx2 = t22.getText().toString();
        String tx3 = t33.getText().toString();
        String cn1 = c111.getText().toString();

        if (data_id.equals("0")) {
            Toast.makeText(getBaseContext(),"11",Toast.LENGTH_SHORT).show();
            selected_sup = radio.getCheckedRadioButtonId();
        }else{
            selected_sup = 0;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://php7.demo20277.atservers.net/web/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetDataService service = retrofit.create(GetDataService.class);
            Call<List<Data>> repos = service.saveGood(data_id, tx1, tx2, tx3, cn1, s_id,selected_sup.toString(), category_name);
            repos.enqueue(new Callback<List<Data>>() {
                @Override
                public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                    Toast.makeText(getBaseContext(),"Товар тспешно сохранен",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<Data>> call, Throwable t) {
                    Toast.makeText(getBaseContext(),"Ошибка",Toast.LENGTH_SHORT).show();
                }
            });
    }

    //Информация о поставщике
    public void SupplierList(View view) {
        Intent intent = new Intent(InfoTovarActivity.this, SupplierActivity.class);
        intent.putExtra("sup_id", sup_id);
        startActivity(intent);
    }

    //Список поставщиков
    private void GetSupplierlist() {
        llt = (LinearLayout) findViewById(R.id.activity_info_tovar);
        rbg = (RadioGroup) findViewById(R.id.RRG);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://php7.demo20277.atservers.net/web/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetSupplierService service = retrofit.create(GetSupplierService.class);
        Call<List<SupplierData>> repos = service.getSupplierDetails();
        repos.enqueue(new Callback<List<SupplierData>>() {
            @Override
            public void onResponse(Call<List<SupplierData>> call, Response<List<SupplierData>> response) {
                List<SupplierData> sup_list = response.body();
                RadioGroup r_g = new RadioGroup(InfoTovarActivity.this);
                r_g.setId(id);
                llt.addView(r_g);

                for (int i = 0; i < sup_list.size(); i++) {
                    id = sup_list.get(i).getId();
                    String Name = sup_list.get(i).getName();
                    String Data = sup_list.get(i).getData();
                    RadioButton btn = new RadioButton(InfoTovarActivity.this);
                    btn.setText(Name);
                    btn.setId(sup_list.get(i).getId());
                    rbg.addView(btn);
                }
            }

            @Override
            public void onFailure(Call<List<SupplierData>> call, Throwable t) {

            }
        });

    }

    protected void GetListCategory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://php7.demo20277.atservers.net/web/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCategoryService service = retrofit.create(GetCategoryService.class);
        final Call<List<Category>> repos = service.getCategoryDetails();
        repos.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> category = response.body();
                llt = (LinearLayout) findViewById(R.id.activity_category_settings);

                for (int i = 0; i < response.body().size(); i++) {
                    final String c_name = category.get(i).getName();
                    final int c_id = category.get(i).getId();
                    categorys.add(c_name);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    private void SetSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);//Заполняем снипер
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String c_name = parent.getSelectedItem().toString();
                category_name = c_name;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void DeleteItem(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://php7.demo20277.atservers.net/web/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetDataService service = retrofit.create(GetDataService.class);
        Call<List<Data>> repos = service.deleteItemdata(data_id);
        repos.enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {

            }

            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
