package com.labdevs.controldegastos.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.labdevs.controldegastos.data.entity.Cuenta;

import java.util.List;

@Dao
public interface CuentaDAO {
    @Insert
    void insertar(Cuenta c);

    @Update
    void actualizar(Cuenta c);

    @Delete
    void eliminar(Cuenta c);

    @Query("SELECT * FROM cuentas")
    List<Cuenta> listarTodas();

    @Query("SELECT * FROM cuentas WHERE id = :id")
    Cuenta buscarPorId(int id);
}