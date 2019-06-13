package com.example.proyek_mdp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    TextView tvSearch;
    EditText etSearch;
    RecyclerView rviewSearch;
    private ArrayList<Berita> listBerita;
    private BeritaAdapter adapterBerita;
    public SearchFragment() {
        // Required empty public constructor
    }

    String url = "https://proyeksoa.herokuapp.com/hoax/searchBerita?judul=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search, container, false);

        rviewSearch = v.findViewById(R.id.rviewSearch);
        tvSearch = v.findViewById(R.id.tvSearch);
        etSearch = v.findViewById(R.id.etSearch);
        listBerita = new ArrayList<Berita>();

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listBerita.clear();
                url += etSearch.getText().toString();
                Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
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
                                String tanggal = index.getString("tanggal");
                                String sumber = index.getString("domain");
                                String url = index.getString("url");
                                int id_db = index.getInt("id");
                                listBerita.add(new Berita(judul,judul,tanggal,"Sumber : "+sumber,url,id_db));
                            }
                            adapterBerita = new BeritaAdapter(listBerita, new RVClickListener() {
                                @Override
                                public void recyclerViewListClicked(View v, int posisi) {
                                    Intent intent = new Intent(getContext(),DetailBeritaActivity.class);
                                    int id_clicked = listBerita.get(posisi).getId_db();
                                    intent.putExtra("id_berita",id_clicked+"");
                                    startActivity(intent);
                                }
                            });
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
                            rviewSearch.setLayoutManager(lm);
                            rviewSearch.setAdapter(adapterBerita);

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
                url = "https://proyeksoa.herokuapp.com/hoax/searchBerita?judul=";

            }
        });


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}
