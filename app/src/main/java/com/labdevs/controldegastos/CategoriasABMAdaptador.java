package com.labdevs.controldegastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoriasABMAdaptador extends RecyclerView.Adapter<CategoriasABMAdaptador.IconViewHolder> {

    private CategoriasABMFragment fragmento;
    private List<String> listaIconos;

    public CategoriasABMAdaptador(List<String> listaIconos, CategoriasABMFragment fragmento) {
        this.listaIconos = listaIconos;
        this.fragmento = fragmento;
    }

    // ViewHolder - almacena las vistas
    public static class IconViewHolder extends RecyclerView.ViewHolder {
        public ImageButton iconImage;

        public IconViewHolder(View v) {
            super(v);
            iconImage = v.findViewById(R.id.icono_seleccionable);
        }
    }

    // Adapter - onCreateViewHolder convierte el layout (.xml) en objetos de vista (View)
    @Override
    public IconViewHolder onCreateViewHolder(ViewGroup contenedorVistas, int tipoVista) {
        View view = LayoutInflater.from(contenedorVistas.getContext())
                .inflate(R.layout.item_icono, contenedorVistas, false);
        return new IconViewHolder(view);
    }

    // Adapter - onBindViewHolder toma los datos y los muestra en las vistas creadas (Views)
    @Override
    public void onBindViewHolder(IconViewHolder contenedor, int posicion) {
        final String icono = listaIconos.get(posicion);
        Context context = contenedor.itemView.getContext();
        String iconoSeleccionado = fragmento.getIconoSeleccionado();

        // ID del recurso "icono"
        int iconId = context.getResources().getIdentifier(icono, "drawable", context.getPackageName());
        if (iconId != 0) {
            contenedor.iconImage.setImageResource(iconId);
        }

        // Efecto visual para el icomo seleccionado
        if (icono.equals(iconoSeleccionado)) {
            contenedor.iconImage.setBackgroundResource(R.drawable.background_icono_seleccionado);
        } else {
            contenedor.iconImage.setBackgroundResource(R.drawable.background_icono);
        }

        // Icono seleccionable
        contenedor.iconImage.setOnClickListener(v -> {
            fragmento.iconoSeleccionado(icono);
        });
    }

    // Adapter - getItemCount devuelve el tamaño de la lista de elementos
    @Override
    public int getItemCount() {
        return listaIconos.size();
    }
}