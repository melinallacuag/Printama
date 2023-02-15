package com.anggastudio.sample.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anggastudio.sample.R;
import com.bumptech.glide.Glide;

import java.util.List;


public class CaraAdapter extends RecyclerView.Adapter<CaraAdapter.ViewHolder>{
    private List<Cara> caraList;
    private Context context;
    public CaraAdapter(List<Cara> caraList,Context context){
        this.caraList = caraList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardcara,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtcara.setText(caraList.get(position).getNumerocara());
    }

    @Override
    public int getItemCount() {
        return caraList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtcara;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txtcara = itemView.findViewById(R.id.txtcara);
        }
    }
}