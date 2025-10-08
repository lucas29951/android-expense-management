package com.labdevs.controldegastos.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cuentas")
public class Cuenta {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombre;
    public String tipo;
    public double saldo;

    public Cuenta(String nombre, String tipo, double saldo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.saldo = saldo;
    }
}
