package com.labdevs.controldegastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.labdevs.controldegastos.data.entity.Categoria;

import java.util.List;

public class CategoriasAdaptador extends RecyclerView.Adapter<CategoriasAdaptador.CategoriaViewHolder> {

    private CategoriasFragment fragmento;
    private List<Categoria> listaCategorias;

    public CategoriasAdaptador(List<Categoria> listaCategorias, CategoriasFragment fragmento) {
        this.listaCategorias = listaCategorias;
        this.fragmento = fragmento;
    }

    public List<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
        notifyDataSetChanged();
    }

    // ViewHolder - almacena las vistas
    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreCat;
        public ImageView iconoCat;

        public CategoriaViewHolder(View v) {
            super(v);
            nombreCat = v.findViewById(R.id.tvNombre);
            iconoCat = v.findViewById(R.id.ivIcono);
        }
    }

    // Adapter - onCreateViewHolder convierte el layout (.xml) en objetos de vista (View)
    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup vistaContenedor, int tipoVista) {
        View view = LayoutInflater.from(vistaContenedor.getContext())
                .inflate(R.layout.item_categoria, vistaContenedor, false);
        return new CategoriaViewHolder(view);
    }

    // Adapter - onBindViewHolder toma los datos y los muestra en las vistas creadas (Views)
    @Override
    public void onBindViewHolder(CategoriaViewHolder viewHolder, int posicion) {
        Categoria categoria = listaCategorias.get(posicion);

        // nombre de la categora
        viewHolder.nombreCat.setText(categoria.nombre);

        // icono de la categoria
        Context contexto = viewHolder.itemView.getContext();
        int idIcono = contexto.getResources().getIdentifier(categoria.icono, "drawable", contexto.getPackageName());
        viewHolder.iconoCat.setImageResource(idIcono);

        viewHolder.itemView.setOnClickListener(v -> {
            fragmento.categoriaClickListener(categoria);
        });
    }

    // Adapter - getItemCount devuelve el tamaño de la lista de elementos
    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }
}