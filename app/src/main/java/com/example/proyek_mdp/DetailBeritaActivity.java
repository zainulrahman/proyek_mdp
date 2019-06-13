package com.example.proyek_mdp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class DetailBeritaActivity extends AppCompatActivity {

    String url = "https://proyeksoa.herokuapp.com/hoax/berita?id=";
    TextView tvIsi,tvJudul,tvSumber,tvTerbit,tvKomentar,tvShowAll;
    EditText etKomentar;
    ImageView iv;
    int login=1;

    private RecyclerView rviewkoment;
    private KomentarAdapter adapterkomentar;
    private ArrayList<Komentar> listKomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        Intent iget = getIntent();
        final String id = iget.getStringExtra("id_berita");

        tvIsi = findViewById(R.id.tvIsi);
        tvJudul = findViewById(R.id.tvJudul);
        tvSumber = findViewById(R.id.tvSumber);
        tvTerbit = findViewById(R.id.tvTerbit);
        tvShowAll = findViewById(R.id.tvShowAll);
        tvShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailBeritaActivity.this,KomentarActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        etKomentar = findViewById(R.id.etComment);
        iv = findViewById(R.id.imageView);
        rviewkoment = findViewById(R.id.rviewkomentar);


        tvKomentar = findViewById(R.id.tvKomentar);
        tvKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url_add_comment= "https://proyeksoa.herokuapp.com/hoax/addKomentar";
                SharedPreferences sp1 = getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                String id_user = sp1.getString("currentUser",null);
                if (id_user!=null){
                    Map params = new HashMap();
                    params.put("isi_komentar",etKomentar.getText().toString());
                    params.put("id_berita",id);
                    params.put("id_user",id_user);

                    JSONObject parameters = new JSONObject(params);

                    JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.POST, url_add_comment, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(DetailBeritaActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DetailBeritaActivity.this, "Anda Harus Login Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DetailBeritaActivity.this,LoginForm.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        open_berita(id);
        show_komentar(id);
    }


    String urlkomentar = "https://proyeksoa.herokuapp.com/hoax/getKomentar?id_berita=";
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
                    RecyclerView.LayoutManager lmy = new LinearLayoutManager(DetailBeritaActivity.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void commentClick(View v){
        if (login==1){
            Toast.makeText(this, "Harus login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DetailBeritaActivity.this, LoginForm.class);
            startActivity(intent);
        }
    }
    public void open_berita(String id){
        url+=id;
        Map params = new HashMap();
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.GET, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray berita = response.getJSONArray("articles");
                    JSONObject index = (JSONObject) berita.get(0);
                    String isi = index.getString("isi");
                    String judul = index.getString("judul");
                    String domain = index.getString("domain");
                    String terbit = index.getString("tanggal").substring(0,10);
                    tvIsi.setText(isi);
                    tvJudul.setText(judul);
                    tvSumber.setText(domain);
                    tvTerbit.setText("Diterbitkan: "+terbit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DetailBeritaActivity.this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);


        String urlgambar = "gb"+id+".jpg";
        String linkgambar = "https://proyeksoa.herokuapp.com/hoax/getImage?url="+urlgambar;

        ImageRequest ir = new ImageRequest(linkgambar, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                iv.setImageBitmap(response);
            }
        }, 0, 0, null,Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        Volley.newRequestQueue(this).add(ir);
    }
}
