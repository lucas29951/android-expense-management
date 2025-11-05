package com.labdevs.controldegastos;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.labdevs.controldegastos.data.entity.Categoria;
import com.labdevs.controldegastos.data.repositories.CategoriaRepository;

import java.util.List;

public class CategoriaViewModel extends AndroidViewModel {
    private final CategoriaRepository repository;
    private final LiveData<List<Categoria>> listaCategorias;
    private final MutableLiveData<Categoria> categoriaSeleccionada = new MutableLiveData<>();
    private final MutableLiveData<Boolean> nombreValido = new MutableLiveData<>();
    private boolean categoriaExistente;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoriaRepository(application);
        listaCategorias = repository.obtenerTodas();
    }

    public LiveData<List<Categoria>> getTodas() {
        return listaCategorias;
    }

    public LiveData<Categoria> getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public LiveData<Boolean> isNombreValido() {
        return nombreValido;
    }

    public void verificacionPorBD(String nombre) {
        repository.getEjecutor().execute(() -> {
            int contador = repository.contarPorNombre(nombre);
            boolean esValido = (contador == 0);
            new Handler(Looper.getMainLooper()).post(() -> nombreValido.setValue(esValido));
        });
    }

    public boolean isCategoriaExistente() {
        return categoriaExistente;
    }

    public void seleccionarCategoria(Categoria c) {
        categoriaSeleccionada.setValue(c);
        nombreValido.setValue(null);
        categoriaExistente = true;
    }

    public void crearCategoria() {
        categoriaSeleccionada.setValue(new Categoria("", null, false));
        nombreValido.setValue(null);
        categoriaExistente = false;
    }

    public void insertar(Categoria c) {
        repository.insertar(c);
    }

    public void actualizar(Categoria c) {
        repository.actualizar(c);
    }

    public void eliminar(Categoria c) {
        repository.eliminar(c);
    }
}
