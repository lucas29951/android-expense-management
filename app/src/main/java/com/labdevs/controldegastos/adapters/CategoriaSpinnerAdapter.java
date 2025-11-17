package com.labdevs.controldegastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.labdevs.controldegastos.R;
import com.labdevs.controldegastos.data.entity.Categoria;
import com.labdevs.controldegastos.databinding.ListSpinnerItemCategoriaBinding;

import java.util.List;

public class CategoriaSpinnerAdapter extends BaseAdapter {

    private final List<Categoria> listaCategorias;
    private final Context context;
    private LayoutInflater lInflater;

    public CategoriaSpinnerAdapter(Context context, List<Categoria> listaCategorias) {
        this.context = context;
        lInflater = LayoutInflater.from(context);
        this.listaCategorias = listaCategorias;
    }

    @Override
    public int getCount() {
        return listaCategorias.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCategorias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaCategorias.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Categoria categoria = listaCategorias.get(position);
        ListSpinnerItemCategoriaBinding binding = ListSpinnerItemCategoriaBinding.inflate(lInflater,parent,false);

        int idIcono = context.getResources().getIdentifier(categoria.icono, "drawable", context.getPackageName());
        binding.categorySpinnerIcon.setImageResource(idIcono);

        binding.categorySpinnerText.setText(categoria.nombre);

        return binding.getRoot();
    }
}
