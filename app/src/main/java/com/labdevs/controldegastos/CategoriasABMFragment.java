package com.labdevs.controldegastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.labdevs.controldegastos.data.database.AppDatabase;
import com.labdevs.controldegastos.data.entity.Categoria;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CategoriasABMFragment extends Fragment {

    private CategoriaViewModel viewModel;
    private CategoriasABMAdaptador adaptador;
    private RecyclerView recyclerView;

    private Button btn_Agregar_Modificar;

    private EditText nombreCat;
    private String iconoCat = "cat_ico_default";

    public CategoriasABMFragment() {
        super(R.layout.fragment_categorias_abm);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup vistaContenedor, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_categorias_abm, vistaContenedor, false);
        recyclerView = vista.findViewById(R.id.recycler_iconos);

        // TextEdit - editor de nombre
        nombreCat = vista.findViewById(R.id.nombre_categoria);

        // GridLayoutManager - selector de iconos
        int numColumnas = 6;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumnas));
        List<String> listaRecursos = getNombresRecursos();
        adaptador = new CategoriasABMAdaptador(listaRecursos, this);
        recyclerView.setAdapter(adaptador);

        viewModel = new ViewModelProvider(requireActivity()).get(CategoriaViewModel.class);
        AppDatabase db = AppDatabase.obtenerInstancia(getContext());

        btn_Agregar_Modificar = vista.findViewById(R.id.btn_agregar_modificar);
        btn_Agregar_Modificar.setOnClickListener(v -> validarYVerificar());

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Toolbar
        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);

        // cambia los elementos
        toolbar.setTitle(R.string.categiria_title);     // titulo

        toolbar.setNavigationIcon(R.drawable.arrow_back_24px);  // atras
        toolbar.setNavigationOnClickListener(v -> volverACategorias());

        toolbar.getMenu().clear();                         // limpia los elementos de menú
        toolbar.inflateMenu(R.menu.menu_abm_categoria);    // borrar
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete) {
                eliminarCategoria();
                return true;
            }
            return false;
        });

        viewModel.getCategoriaSeleccionada().observe(getViewLifecycleOwner(), c -> {
            if (viewModel.isCategoriaExistente()) {
                nombreCat.setText(c.nombre);
                iconoCat = c.icono;
                adaptador.notifyDataSetChanged();
            }
        });

        viewModel.isNombreValido().observe(getViewLifecycleOwner(), esValido -> {
            if (esValido == null) return;
            if (esValido) {             // si el nombre no existe en la BD
                guardarOActualizar();
            } else if (!esValido) {
                if (viewModel.isCategoriaExistente()) {
                    Categoria c = viewModel.getCategoriaSeleccionada().getValue();
                    String nombre = nombreCat.getText().toString().trim();
                    if (nombre.equalsIgnoreCase(c.nombre)) {
                        guardarOActualizar();
                    } else {
                        nombreCat.setError("La categoría ya existe");
                    }
                } else {
                    nombreCat.setError("La categoría ya existe");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setNavigationIcon(null);
        toolbar.getMenu().clear();
    }

    private List<String> getNombresRecursos() {
        List<String> recursos = new ArrayList<>();

        Field[] drawables = R.drawable.class.getDeclaredFields();
        for (Field field : drawables) {
            String nombre = field.getName();
            if (nombre.startsWith("cat_ico_") && (!nombre.equalsIgnoreCase("cat_ico_default"))) {
                recursos.add(nombre);
            }
        }

        return recursos;
    }

    public void iconoSeleccionado(String nombre) {
        iconoCat = nombre;
        adaptador.notifyDataSetChanged();
    }

    public String getIconoSeleccionado() {
        return iconoCat;
    }

    private void validarYVerificar() {
        String nombre = nombreCat.getText().toString().trim();
        // validacion
        if (nombre.isEmpty()) {
            nombreCat.setError("El nombre no puede estar vacio");
            return;
        }
        // verificacion
        viewModel.verificacionPorBD(nombre);
    }

    private void guardarOActualizar() {
        if (viewModel.isCategoriaExistente()) {
            actualizarCategoria();
        } else {
            guardarCategoria();
        }
    }

    private void guardarCategoria() {
        Categoria c = new Categoria("", null, false);
        c.nombre = nombreCat.getText().toString().trim();
        c.icono = iconoCat;
        viewModel.insertar(c);
        volverACategorias();
    }

    private void actualizarCategoria() {
        Categoria c = viewModel.getCategoriaSeleccionada().getValue();
        c.nombre = nombreCat.getText().toString().trim();
        c.icono = iconoCat;
        viewModel.actualizar(c);
        volverACategorias();
    }

    private void eliminarCategoria() {
        if (viewModel.isCategoriaExistente()) {
            Categoria c = viewModel.getCategoriaSeleccionada().getValue();
            viewModel.eliminar(c);
        }
        volverACategorias();
    }

    private void volverACategorias() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setCurrentFragment(new CategoriasFragment());
        }
    }
}