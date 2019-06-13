package com.example.proyek_mdp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.ViewHolder> {


    private ArrayList<Komentar> listKomentar;
    private RVClickListener myListener;

    public KomentarAdapter(ArrayList<Komentar> listKomentar, RVClickListener rvcl) {
        this.listKomentar = listKomentar;
        this.myListener = rvcl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNama,tvKomentar,tvTanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvKomentar = itemView.findViewById(R.id.tvKomentar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListener.recyclerViewListClicked(v,ViewHolder.this.getLayoutPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public KomentarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.rowitem_komentar,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KomentarAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvNama.setText(listKomentar.get(i).getNamalengkap());
        viewHolder.tvKomentar.setText(listKomentar.get(i).getKomentar());
        viewHolder.tvTanggal.setText(listKomentar.get(i).getTanggal());
    }

    @Override
    public int getItemCount() {
        return (listKomentar!=null)?listKomentar.size():0;
    }
}
