package com.labdevs.controldegastos.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorias")
public class Categoria {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombre;
    public String icono;

    public Categoria(String nombre, String icono) {
        this.nombre = nombre;
        this.icono = icono;
    }
}
