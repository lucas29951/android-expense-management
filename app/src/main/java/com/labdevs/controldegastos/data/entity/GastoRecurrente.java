package com.labdevs.controldegastos.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "gastos_recurrentes",
        foreignKeys = {
                @ForeignKey(entity = Cuenta.class, parentColumns = "id", childColumns = "cuenta_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Categoria.class, parentColumns = "id", childColumns = "categoria_id", onDelete = ForeignKey.CASCADE)})
public class GastoRecurrente {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public double monto;
    public String frecuencia;
    public Date fecha_inicio;
    public Date fecha_fin;
    public int cuenta_id;
    public int categoria_id;

    public GastoRecurrente(double monto, String frecuencia, Date fecha_inicio, Date fecha_fin, int cuenta_id, int categoria_id) {
        this.monto = monto;
        this.frecuencia = frecuencia;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.cuenta_id = cuenta_id;
        this.categoria_id = categoria_id;
    }
}