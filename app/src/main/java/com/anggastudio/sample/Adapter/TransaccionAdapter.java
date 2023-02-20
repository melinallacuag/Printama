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
import com.anggastudio.sample.WebApiSVEN.Models.Optran;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;

import java.util.List;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.ViewHolder>{

    private List<Optran> optranList;
    private Context context;

    public TransaccionAdapter(List<Optran> optranList, Context context){
        this.optranList = optranList;
        this.context    = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtransacciones,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionAdapter.ViewHolder holder, int position) {
        holder.txtcara.setText(optranList.get(position).getCara());
        holder.txtmanguera.setText(optranList.get(position).getManguera());
        holder.txtproducto.setText(optranList.get(position).getProducto());
        holder.txtprecio.setText(String.valueOf(optranList.get(position).getPrecio()));
        holder.txtgalones.setText(String.valueOf(optranList.get(position).getGalones()));
        holder.txtsoles.setText(String.valueOf(optranList.get(position).getSoles()));
    }

    @Override
    public int getItemCount() {
        return optranList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        private TextView txtcara;
        private TextView txtmanguera;
        private TextView txtproducto;
        private TextView txtprecio;
        private TextView txtgalones;
        private TextView txtsoles;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView      = itemView.findViewById(R.id.cardtransaccion);
            txtcara       = itemView.findViewById(R.id.griatextcara);
            txtmanguera   = itemView.findViewById(R.id.griatextmanguera);
            txtproducto   = itemView.findViewById(R.id.griatextproducto);
            txtprecio     = itemView.findViewById(R.id.griatextprecio);
            txtgalones    = itemView.findViewById(R.id.griatextgalones);
            txtsoles      = itemView.findViewById(R.id.griatextsoles);
        }
    }
}
