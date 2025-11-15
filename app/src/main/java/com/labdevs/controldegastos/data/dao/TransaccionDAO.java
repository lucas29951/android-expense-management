package com.labdevs.controldegastos.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.data.model.ItemInforme;
import com.labdevs.controldegastos.data.model.ItemResume;

import java.util.List;

@Dao
public interface TransaccionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Transaccion transaccion);

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

    @Query("SELECT t.id, t.tipo_transaccion, t.monto, t.fecha_hora, c.nombre, c.icono FROM transacciones AS t JOIN categorias AS c ON t.id_categoria = c.id WHERE t.tipo_transaccion LIKE :tipoTrans")
    LiveData<List<ItemResume>> listarItemsResume(String tipoTrans);
}