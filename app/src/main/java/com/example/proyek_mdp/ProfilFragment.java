package com.example.proyek_mdp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {


    public ProfilFragment() {
        // Required empty public constructor
    }

    Button btnLogout,btnSave;
    EditText etNama,etEmail,etPassword;
    String url_profile = "https://proyeksoa.herokuapp.com/hoax/getProfile?id_user=";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        btnLogout = v.findViewById(R.id.btnLogout);
        btnSave = v.findViewById(R.id.btnSave);
        etNama = v.findViewById(R.id.etNamaLengkap);
        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    etPassword.setText("");
                }

                return false;
            }
        });
        SharedPreferences sp1 = getContext().getSharedPreferences("Login",MODE_PRIVATE);
        final String id_user = sp1.getString("currentUser",null);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp= getContext().getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("currentUser",null);
                Ed.commit();
                HomeFragment hf = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,hf).commit();
                BottomNavigationView nv =getActivity().findViewById(R.id.navBar);
                nv.setSelectedItemId(R.id.nav_home);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url_update = "https://proyeksoa.herokuapp.com/hoax/updateProfile?id_user="+id_user;
                Map params = new HashMap();
                params.put("nama_lengkap",etNama.getText().toString());
                params.put("password",etPassword.getText().toString());
                params.put("email",etEmail.getText().toString());

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.PUT, url_update, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "woiwoi", Toast.LENGTH_SHORT).show();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });

                Volley.newRequestQueue(getContext()).add(jsonRequest);
            }
        });

        url_profile+=id_user;

        Map params = new HashMap();

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.GET, url_profile, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray berita = response.getJSONArray("profile");
                    for (int i = 0;i<berita.length();i++){
                        JSONObject index = (JSONObject) berita.get(i);
                        String nama = index.getString("nama");
                        String email = index.getString("email");
                        String password = index.getString("password");
                        etNama.setText(nama.toString());
                        etEmail.setText(email.toString());
                        etPassword.setText(password.toString());
                    }

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



        return v;
    }

}
