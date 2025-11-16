package com.labdevs.controldegastos.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorias",
        indices = {@Index(value = {"nombre"}, unique = true)})
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String icono = "cat_ico_default";
    public boolean esDefault;

    public Categoria(String nombre, String icono, boolean esDefault) {
        this.nombre = nombre;
        this.icono = (icono != null) ? icono : "cat_ico_default";
        this.esDefault = esDefault;
    }
}
