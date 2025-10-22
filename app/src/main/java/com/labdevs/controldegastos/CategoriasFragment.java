package com.labdevs.controldegastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.labdevs.controldegastos.data.entity.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriasFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoriaAdaptador adaptador;
    private List<Categoria> listaCategorias;
    private Button btnAgregar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup vistaContenedor, @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_categorias, vistaContenedor, false);
        recyclerView = vista.findViewById(R.id.recycler_categorias);

        listaCategorias = obtenerCategorias();   //  lista de datos de prueba

        // LayoutManager para lista vertical o horizontal (o GridLayoutManager para una cuadrícula)
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new CategoriaAdaptador(listaCategorias);
        recyclerView.setAdapter(adaptador);

        // Probar con usar un ViewModel compartido para comunicar fragmentos sin pasar por la Activity (útil cuando querés compartir datos además de navegar).
        btnAgregar = vista.findViewById(R.id.btn_agregar_categoria);
        btnAgregar.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                Fragment nuevoFragment = new CategoriasABMFragment();
                activity.setCurrentFragment(nuevoFragment);
            }
        });

        return vista;
    }

    private List<Categoria> obtenerCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria("Electrónica", null, false));
        categorias.add(new Categoria("Ropa", null, false));
        categorias.add(new Categoria("Alimentos", null, false));
        return categorias;
    }
}