package com.labdevs.controldegastos.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.labdevs.controldegastos.data.entity.Categoria;

import java.util.List;

@Dao
public interface CategoriaDAO {
    @Insert
    void insertar(Categoria c);

    @Update
    void actualizar(Categoria c);

    @Delete
    void eliminar(Categoria c);

    @Query("SELECT * FROM categorias")
    LiveData<List<Categoria>> listarTodas();

    @Query("SELECT * FROM categorias WHERE id = :id")
    Categoria buscarPorId(int id);

    @Query("SELECT * FROM categorias WHERE nombre = :nombre")
    Categoria buscarPorNombre(String nombre);
}
