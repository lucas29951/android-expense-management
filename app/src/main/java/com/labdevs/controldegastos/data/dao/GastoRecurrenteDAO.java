package com.labdevs.controldegastos.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.labdevs.controldegastos.data.entity.GastoRecurrente;

import java.util.List;

@Dao
public interface GastoRecurrenteDAO {
    @Insert
    void insertar(GastoRecurrente gr);

    @Update
    void actualizar(GastoRecurrente gr);

    @Delete
    void eliminar(GastoRecurrente gr);

    @Query("SELECT * FROM gastos_recurrentes")
    List<GastoRecurrente> listarTodos();

    @Query("SELECT * FROM gastos_recurrentes WHERE id = :id")
    GastoRecurrente buscarPorId(int id);
}