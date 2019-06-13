package com.example.proyek_mdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {
    private RVClickListener mylistener;
    private ArrayList<Berita> listBerita;

    public BeritaAdapter(ArrayList<Berita> listBerita,RVClickListener rvcl) {
        this.listBerita = listBerita;
        this.mylistener = rvcl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvJudul,tvJam,tvDeskripsi,tvSumber;
        ImageView iv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul=itemView.findViewById(R.id.tvJudul);
            tvJam=itemView.findViewById(R.id.tvJam);
            tvDeskripsi=itemView.findViewById(R.id.tvDeskripsi);
            tvSumber=itemView.findViewById(R.id.tvSumber);
            iv = itemView.findViewById(R.id.iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mylistener.recyclerViewListClicked(v,ViewHolder.this.getLayoutPosition());
                }
            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.rowitem,viewGroup,false);
        return new ViewHolder(v);
    }

    RequestQueue rq;
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.tvJudul.setText(listBerita.get(i).getJudul());
        viewHolder.tvDeskripsi.setText(listBerita.get(i).getDeskripsi());
        viewHolder.tvSumber.setText(listBerita.get(i).getSumber());
        viewHolder.tvJam.setText(listBerita.get(i).getTanggal().substring(0,10));
        String urlgambar = "https://proyeksoa.herokuapp.com/hoax/getImage?url="+listBerita.get(i).getGambar();

        ImageRequest ir = new ImageRequest(urlgambar, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                viewHolder.iv.setImageBitmap(response);
            }
        }, 0, 0, null,Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        Volley.newRequestQueue(viewHolder.tvJam.getContext()).add(ir);
    }

    @Override
    public int getItemCount() {
        return (listBerita!=null)?listBerita.size():0;
    }
}
