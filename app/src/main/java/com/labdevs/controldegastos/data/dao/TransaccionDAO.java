package com.labdevs.controldegastos.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.labdevs.controldegastos.data.entity.Transaccion;

import java.util.List;

@Dao
public interface TransaccionDAO {
    @Insert
    void insertar(Transaccion t);

    @Update
    void actualizar(Transaccion t);

    @Delete
    void eliminar(Transaccion t);

    @Query("SELECT * FROM transacciones")
    List<Transaccion> listarTodas();

    @Query("SELECT * FROM transacciones WHERE id = :id")
    Transaccion buscarPorId(int id);
}