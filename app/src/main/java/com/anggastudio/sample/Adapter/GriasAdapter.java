package com.anggastudio.sample.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;

import java.util.List;

public class GriasAdapter extends  RecyclerView.Adapter<GriasAdapter.ViewHolder>{
    private List<Grias> griasList;
    private Context context;

    public GriasAdapter(List<Grias> griasList,Context context){
        this.griasList = griasList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardgrias,parent,false);
        return new GriasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GriasAdapter.ViewHolder holder, int position) {
        holder.txtcaragria.setText(griasList.get(position).getNroLado());
    }

    @Override
    public int getItemCount() {
        return griasList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        private TextView txtcaragria;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView    = itemView.findViewById(R.id.cardgria);
            txtcaragria = itemView.findViewById(R.id.txtcaragria);
        }
    }
}
