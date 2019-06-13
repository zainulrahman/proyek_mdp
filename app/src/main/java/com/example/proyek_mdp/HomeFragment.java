package com.example.proyek_mdp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView rview;
    private BeritaAdapter adapterBerita;
    private Button btnShowbiz,btnTekno,btnBisnis,btnBola,btnAll;
    private ArrayList<Berita> listBerita;

    public HomeFragment() {


    }


    void revertWarna(){
        btnTekno.setTextColor(getResources().getColor(R.color.black));
        btnBisnis.setTextColor(getResources().getColor(R.color.black));
        btnShowbiz.setTextColor(getResources().getColor(R.color.black));
        btnBola.setTextColor(getResources().getColor(R.color.black));
        btnAll.setTextColor(getResources().getColor(R.color.black));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        btnShowbiz = view.findViewById(R.id.btnShowbiz);
        btnTekno = view.findViewById(R.id.btnTekno);
        btnBisnis = view.findViewById(R.id.btnBisnis);
        btnBola = view.findViewById(R.id.btnBola);
        btnAll = view.findViewById(R.id.btnAll);


        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertWarna();
                btnAll.setTextColor(getResources().getColor(R.color.hijauterang));
                loadBerita(0);
            }
        });

        btnTekno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertWarna();
                btnTekno.setTextColor(getResources().getColor(R.color.hijauterang));
                loadBerita(4);
            }
        });

        btnShowbiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertWarna();
                btnShowbiz.setTextColor(getResources().getColor(R.color.hijauterang));
                loadBerita(2);
            }
        });

        btnBisnis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertWarna();
                btnBisnis.setTextColor(getResources().getColor(R.color.hijauterang));
                loadBerita(1);
            }
        });

        btnBola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertWarna();
                btnBola.setTextColor(getResources().getColor(R.color.hijauterang));
                loadBerita(3);
            }
        });

        rview = view.findViewById(R.id.rview_fragment);
        listBerita = new ArrayList<Berita>();
        btnAll.setTextColor(getResources().getColor(R.color.hijauterang));
        loadBerita(0);
        return view;
    }

    void loadBerita(int id){
        listBerita.clear();
        String url = "https://proyeksoa.herokuapp.com/hoax/getAllBerita";
        if(id == 1){
            url = "https://proyeksoa.herokuapp.com/hoax/filterBerita?id_kategori=1";
        }
        if(id == 2){
            url = "https://proyeksoa.herokuapp.com/hoax/filterBerita?id_kategori=2";
        }
        if(id == 3){
            url = "https://proyeksoa.herokuapp.com/hoax/filterBerita?id_kategori=3";
        }
        if(id == 4){
            url = "https://proyeksoa.herokuapp.com/hoax/filterBerita?id_kategori=4";
        }
        //select db
        Map params = new HashMap();

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.GET, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray berita = response.getJSONArray("articles");
                    for (int i = 0;i<berita.length();i++){
                        JSONObject index = (JSONObject) berita.get(i);
                        String judul = index.getString("judul");
                        String id_kategori = index.getString("id_kategori");
                        String nama_kategori="";
                        if(id_kategori.equals("1")){
                            nama_kategori = "Bisnis";
                        }
                        if(id_kategori.equals("2")){
                            nama_kategori = "Showbiz";
                        }
                        if(id_kategori.equals("3")){
                            nama_kategori = "Bola";
                        }
                        if(id_kategori.equals("4")){
                            nama_kategori = "Tekno";
                        }
                        String tanggal = index.getString("tanggal");
                        String sumber = index.getString("domain");
                        String url = index.getString("url");
                        int id_db = index.getInt("id");
                        listBerita.add(new Berita(judul,nama_kategori,tanggal,"Sumber : "+sumber,url,id_db));
                    }
                    adapterBerita = new BeritaAdapter(listBerita, new RVClickListener() {
                        @Override
                        public void recyclerViewListClicked(View v, int posisi) {
                            Intent intent = new Intent(getContext(),DetailBeritaActivity.class);
                            int id = listBerita.get(posisi).getId_db();
                            intent.putExtra("id_berita",id+"");
                            startActivity(intent);
                        }
                    });
                    RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
                    rview.setLayoutManager(lm);
                    rview.setAdapter(adapterBerita);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getContext()).add(jsonRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}
