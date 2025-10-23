package com.labdevs.controldegastos.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.data.model.ItemInforme;

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

    @Query("SELECT sum(t.monto) AS 'monto', c.nombre FROM transacciones AS t JOIN categorias AS c ON t.id_categoria = c.id WHERE t.id_cuenta_origen = :cuenta AND t.tipo_transaccion LIKE :tipoTrans AND date(t.fecha_hora) BETWEEN :fechaDesde AND :fechaHasta GROUP BY c.nombre")
    List<ItemInforme> listarPorPeriodo(int cuenta, String tipoTrans, String fechaDesde, String fechaHasta);

    @Query("SELECT sum(t.monto) AS 'monto', c.nombre FROM transacciones AS t JOIN categorias AS c ON t.id_categoria = c.id WHERE t.id_cuenta_origen = :cuenta AND t.tipo_transaccion LIKE :tipoTrans AND strftime('%m',t.fecha_hora) = :mes GROUP BY c.nombre")
    List<ItemInforme> listarPorMes(int cuenta, String tipoTrans, String mes);

    @Query("SELECT sum(t.monto) AS 'monto', c.nombre FROM transacciones AS t JOIN categorias AS c ON t.id_categoria = c.id WHERE t.id_cuenta_origen = :cuenta AND t.tipo_transaccion LIKE :tipoTrans AND strftime('%Y',t.fecha_hora) = :year GROUP BY c.nombre")
    List<ItemInforme> listarPorAnio(int cuenta, String tipoTrans, String year);
}