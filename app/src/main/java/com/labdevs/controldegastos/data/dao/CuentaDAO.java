package com.labdevs.controldegastos.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;

import com.labdevs.controldegastos.data.entity.Cuenta;

import java.util.List;

@Dao
public interface CuentaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Cuenta cuenta);

    @Delete
    void delete(Cuenta c);

    @Query("SELECT * FROM cuentas")
    List<Cuenta> listarTodas();

    @Query("SELECT * FROM cuentas WHERE id = :id")
    Cuenta buscarPorId(int id);

    @Query("SELECT * FROM cuentas")
    List<Cuenta> listAll();


}