package com.labdevs.controldegastos.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "transacciones",
        foreignKeys = {
                @ForeignKey(entity = Categoria.class, parentColumns = "id", childColumns = "id_categoria", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Cuenta.class, parentColumns = "id", childColumns = "id_cuenta_origen", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Cuenta.class, parentColumns = "id", childColumns = "id_cuenta_destino", onDelete = ForeignKey.CASCADE)})
public class Transaccion {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public double monto;
    public Date fecha_hora;
    public String comentario;
    public String tipo_transaccion;
    public int id_categoria;
    public int id_cuenta_origen;
    public int id_cuenta_destino;

    public Transaccion(double monto, Date fecha_hora, String comentario, String tipo_transaccion, int id_categoria, int id_cuenta_origen, int id_cuenta_destino) {
        this.monto = monto;
        this.fecha_hora = fecha_hora;
        this.comentario = comentario;
        this.tipo_transaccion = tipo_transaccion;
        this.id_categoria = id_categoria;
        this.id_cuenta_origen = id_cuenta_origen;
        this.id_cuenta_destino = id_cuenta_destino;
    }
}
