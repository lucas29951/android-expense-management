package com.labdevs.controldegastos.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.labdevs.controldegastos.data.dao.CategoriaDAO;
import com.labdevs.controldegastos.data.database.AppDatabase;
import com.labdevs.controldegastos.data.entity.Categoria;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriaRepository {
    private CategoriaDAO categoriaDAO;
    private ExecutorService ejecutor = Executors.newSingleThreadExecutor();

    public CategoriaRepository(Application application) {
        AppDatabase db = AppDatabase.obtenerInstancia(application);
        categoriaDAO = db.CategoriaDAO();
    }

    public ExecutorService getEjecutor() {
        return ejecutor;
    }

    public LiveData<List<Categoria>> obtenerTodas() {
        return categoriaDAO.listarTodas();
    }

    public int contarPorNombre(String nombre) {
        return categoriaDAO.contarPorNombre(nombre);
    }

    public void insertar(Categoria c) {
        ejecutor.execute(() -> categoriaDAO.insertar(c));
    }

    public void actualizar(Categoria c) {
        ejecutor.execute(() -> categoriaDAO.actualizar(c));
    }

    public void eliminar(Categoria c) {
        ejecutor.execute(() -> categoriaDAO.eliminar(c));
    }
}
