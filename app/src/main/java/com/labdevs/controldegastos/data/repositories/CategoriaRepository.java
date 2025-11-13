package com.labdevs.controldegastos.data.repositories;

import android.app.Application;

import com.labdevs.controldegastos.data.dao.CategoriaDAO;
import com.labdevs.controldegastos.data.database.AppDatabase;
import com.labdevs.controldegastos.data.entity.Categoria;

import java.util.List;

public class CategoriaRepository {

    private final CategoriaDAO categoriaDAO;

    public CategoriaRepository(Application application) {
        AppDatabase db = AppDatabase.obtenerInstancia(application);
        categoriaDAO = db.CategoriaDAO();
    }

    public List<Categoria> listarCategorias(){
        return categoriaDAO.listarTodas();
    }

}
