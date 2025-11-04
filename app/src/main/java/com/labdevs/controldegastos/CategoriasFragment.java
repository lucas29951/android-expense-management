package com.labdevs.controldegastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.labdevs.controldegastos.data.entity.Categoria;

import java.util.ArrayList;

public class CategoriasFragment extends Fragment {

    private CategoriaViewModel viewModel;
    private RecyclerView recyclerView;
    private CategoriasAdaptador adaptador;
    private Button btnAgregar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup vistaContenedor, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_categorias, vistaContenedor, false);
        recyclerView = vista.findViewById(R.id.recycler_categorias);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new CategoriasAdaptador(new ArrayList<>(), this);
        recyclerView.setAdapter(adaptador);

        viewModel = new ViewModelProvider(requireActivity()).get(CategoriaViewModel.class);
        viewModel.getTodas().observe(getViewLifecycleOwner(), categorias -> {
            adaptador.setListaCategorias(categorias);
        });

        btnAgregar = vista.findViewById(R.id.btn_agregar_categoria);
        btnAgregar.setOnClickListener(v -> {
            viewModel.crearCategoria();
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.setCurrentFragment(new CategoriasABMFragment());
            }
        });

        return vista;
    }

    public void categoriaClickListener(Categoria categoria) {
        if (!categoria.esDefault) {
            viewModel.seleccionarCategoria(categoria);
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.setCurrentFragment(new CategoriasABMFragment());
            }
        } else {
            Toast.makeText(getContext(), "No se puede modificar la categoria " + categoria.nombre, Toast.LENGTH_SHORT).show();
        }
    }
}