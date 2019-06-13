package com.example.proyek_mdp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
public class KomentarActivity extends AppCompatActivity {

    private RecyclerView rviewkoment;
    private KomentarAdapter adapterkomentar;
    private EditText etKomentar;
    private ArrayList<Komentar> listKomentar;
    private TextView tvKomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentar);
        Intent iget = getIntent();
        final String id = iget.getStringExtra("id");
        rviewkoment = findViewById(R.id.rviewKomentar1);
        tvKomentar = findViewById(R.id.tvComment);
        etKomentar = findViewById(R.id.etComment);
        tvKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_add_comment= "https://proyeksoa.herokuapp.com/hoax/addKomentar";
                SharedPreferences sp1 = getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                String id_user = sp1.getString("currentUser",null);
                if (id_user!=null){
                    if(etKomentar.length()>0){
                        Map params = new HashMap();
                        params.put("isi_komentar",etKomentar.getText().toString());
                        params.put("id_berita",id);
                        params.put("id_user",id_user);

                        JSONObject parameters = new JSONObject(params);

                        JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.POST, url_add_comment, parameters, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), error.getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);
                    }else{
                        Toast.makeText(KomentarActivity.this, "Komentar Tidak Boleh Kosong!!", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(getApplicationContext(), "Anda Harus Login Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),LoginForm.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        show_komentar(id);
    }

    String urlkomentar = "https://proyeksoa.herokuapp.com/hoax/getKomentar5?id_berita=";
    public void show_komentar(String id){

        urlkomentar+=id;
        listKomentar = new ArrayList<Komentar>();
        Map params = new HashMap();

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.GET, urlkomentar, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray berita = response.getJSONArray("komentar");
                    for (int i = 0;i<berita.length();i++){
                        JSONObject index = (JSONObject) berita.get(i);
                        String nama = index.getString("nama");
                        String isi_komentar = index.getString("isi_komentar");
                        String tanggal = index.getString("jam")+ " jam yang lalu";

                        listKomentar.add(new Komentar(isi_komentar,nama,tanggal));
                    }

                    adapterkomentar = new KomentarAdapter(listKomentar, new RVClickListener() {
                        @Override
                        public void recyclerViewListClicked(View v, int posisi) {

                        }
                    });
                    RecyclerView.LayoutManager lmy = new LinearLayoutManager(getApplicationContext());
                    rviewkoment.setLayoutManager(lmy);
                    rviewkoment.setAdapter(adapterkomentar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);


    }
}
