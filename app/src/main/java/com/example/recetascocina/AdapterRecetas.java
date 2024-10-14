package com.example.recetascocina;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterRecetas extends RecyclerView.Adapter<AdapterRecetas.EnviadoViewHolder> {

    private List<Object[]> enviadoList;
    private Context context;

    public AdapterRecetas(Context context, List<Object[]> enviadoList) {
        this.context = context;
        this.enviadoList = enviadoList;
    }

    @NonNull
    @Override
    public EnviadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta, parent, false);
        return new EnviadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRecetas.EnviadoViewHolder holder, int position) {
        Object[] recibido = enviadoList.get(position);

        String idReceta=(String) recibido[0];
        String tituloReceta = (String) recibido[1];
        String imgReceta=(String) recibido[2];
        String userReceta = (String) recibido[3];
        String userImgReceta = (String) recibido[4];

        holder.usuario.setText(userReceta);
        holder.titulo.setText(tituloReceta);

        if(!imgReceta.isEmpty()){
            String urlReceta=Urls.BASE_URL+imgReceta;
            Glide.with(context)
                    .load(urlReceta)
                    .into(holder.imgReceta);
        }

        if(!userImgReceta.isEmpty()){
            String urlUsuario=Urls.BASE_URL+userImgReceta;

            Glide.with(context)
                    .load(urlUsuario)
                    .into(holder.imgUsuario);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, Receta.class);
            intent.putExtra("idReceta", idReceta);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return enviadoList.size();
    }

    public static class EnviadoViewHolder extends RecyclerView.ViewHolder {
        TextView usuario, titulo;
        ImageView imgReceta,imgUsuario;

        public EnviadoViewHolder(View itemView) {
            super(itemView);
            usuario = itemView.findViewById(R.id.txv_IUsuario);
            titulo = itemView.findViewById(R.id.txv_ITitulo);
            imgReceta = itemView.findViewById(R.id.img_IReceta);
            imgUsuario = itemView.findViewById(R.id.img_IUsuario);
        }
    }

    public void setEnviadoList(List<Object[]> enviadoList) {
        this.enviadoList = enviadoList;
        notifyDataSetChanged();
    }
}
