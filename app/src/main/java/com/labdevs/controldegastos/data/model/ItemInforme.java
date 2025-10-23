package com.labdevs.controldegastos.data.model;

import androidx.room.ColumnInfo;

public class ItemInforme {

    public double monto;
    @ColumnInfo(name = "nombre")
    public String nombreCat;


}
