package com.example.proyek_mdp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginForm extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmail,etPassword;
    private TextView tvSignUp;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginForm.this,RegisterActivity.class);

                startActivity(i);
            }
        });
        //pengecekan sudah login apa belum
        SharedPreferences sp1 = this.getSharedPreferences("Login",MODE_PRIVATE);
        String username = sp1.getString("currentUser",null);
        if (username!=null){
            Intent i = new Intent(LoginForm.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    String url = "https://proyeksoa.herokuapp.com/hoax/login";
    public void btnLogin(View v){
        Map params = new HashMap();
        params.put("email",etEmail.getText().toString());
        params.put("password",etPassword.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new  JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                    String id = response.getString("id");
                    if (status.equals("Logged In")){
                        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor Ed=sp.edit();
                        Ed.putString("currentUser",id);
                        Ed.commit();
                        Intent i = new Intent(LoginForm.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(LoginForm.this, "Password Salah!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginForm.this, "Email Salah!", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);

        /*Intent i = new Intent(LoginForm.this,MainActivity.class);
        startActivity(i);*/
    }


}
