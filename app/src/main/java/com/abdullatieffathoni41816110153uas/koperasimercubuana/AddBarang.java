package com.abdullatieffathoni41816110153uas.koperasimercubuana;

import android.app.Notification;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * @author Abdul Latief Fathoni 41816110153
 * 17 July 2018
 * purpose final exam mercu buana
 *
 * */

public class AddBarang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barang);

        final EditText input_barang = findViewById(R.id.input_barang);
        final EditText input_merk = findViewById(R.id.input_merk);
        final EditText input_harga = findViewById(R.id.input_harga);
        Button simpan = findViewById(R.id.btn_simpan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Tambah Barang");
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // show keypad while open tambah barang
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post("http://koperasi-umb-api.herokuapp.com/barang/add")
                .addBodyParameter("nama_barang", input_barang.getText().toString())
                .addBodyParameter("merk_barang", input_merk.getText().toString())
                .addBodyParameter("harga_barang", input_harga.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            // TODO : create validation
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Toast.makeText(AddBarang.this, message, Toast.LENGTH_SHORT).show();
                            // TODO : create intent to main activity if success
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddBarang.this, "Error JSON Exception", Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(AddBarang.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void simpanBarang(View view) {
//        Toast.makeText(this, input_barang.getText().toString(), Toast.LENGTH_SHORT).show();
//
    };

}
