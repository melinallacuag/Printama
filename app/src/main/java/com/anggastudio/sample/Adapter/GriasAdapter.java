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
    final GriasAdapter.OnItemClickListener listener;
    public interface  OnItemClickListener{
        void onItemClick(Grias item);
    }

    public GriasAdapter(List<Grias> griasList,Context context,GriasAdapter.OnItemClickListener listener){
        this.griasList = griasList;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardgrias,parent,false);
        return new GriasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GriasAdapter.ViewHolder holder, int position) {
        Grias grias = griasList.get(position);
        holder.txtcaragria.setText(griasList.get(position).getNroLado());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(grias);
             //   holder.txtproductogria.setText(griasList.get(listener).getDescripcion());

            }
        });
    }

    @Override
    public int getItemCount() {
        return griasList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        private TextView txtcaragria;
        private TextView txtproductogria;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView        = itemView.findViewById(R.id.cardgria);
            txtcaragria     = itemView.findViewById(R.id.txtcaragria);
            txtproductogria = itemView.findViewById(R.id.txtproductogria);
        }
    }
}
