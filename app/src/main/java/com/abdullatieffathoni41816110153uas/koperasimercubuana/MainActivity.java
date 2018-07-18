package com.abdullatieffathoni41816110153uas.koperasimercubuana;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/*
* @author Abdul Latief Fathoni 41816110153
* 17 July 2018
* purpose final exam mercu buana
*
* */

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private int harga_barang;

    ArrayList<HashMap<String, String>> barangHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barangHistory = new ArrayList<>();

        lv = findViewById(R.id.rc_list_barang);

        callData();

        FloatingActionButton btn_barang = findViewById(R.id.fab_add);
        btn_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBarang.class);
                startActivity(intent);
            }
        });

    }


    private void callData(){
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        showDialog();

        AndroidNetworking.get("http://koperasi-umb-api.herokuapp.com/barang")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data_barang = response.getJSONArray("data");
                            for (int i = 0; i < data_barang.length(); i++) {
                                JSONObject c = data_barang.getJSONObject(i);

                                String id = c.getString("id");
                                String nama_barang = c.getString("nama_barang");
                                String merk_barang = c.getString("merk_barang");
                                harga_barang = c.getInt("harga_barang");

                                Locale localeID = new Locale("in", "ID");
                                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                                String harga_barang_rupiah = formatRupiah.format((double) harga_barang);

                                HashMap<String, String> data = new HashMap<>();

                                data.put("id", id);
                                data.put("nama_barang", nama_barang);
                                data.put("merk_barang", merk_barang);
                                data.put("harga_barang", harga_barang_rupiah);

                                // adding contact to contact list
                                barangHistory.add(data);

                                ListAdapter adapter = new SimpleAdapter(
                                        MainActivity.this, barangHistory,
                                        R.layout.list_data, new String[]{"nama_barang",
                                        "merk_barang", "harga_barang"}, new int[]{R.id.nama_barang, R.id.merk_barang, R.id.harga_barang});
                                lv.setAdapter(adapter);
                            }
                            Log.d(TAG, "Couldn't get json from server. " + barangHistory);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hideDialog();
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "Couldn't get json from server. " + error);
                        Toast.makeText(MainActivity.this, "Periksa sambungan internet anda", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
