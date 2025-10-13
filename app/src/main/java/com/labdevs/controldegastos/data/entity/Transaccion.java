package com.labdevs.controldegastos.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "transacciones")
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

    public int getId_cuenta_destino() {
        return id_cuenta_destino;
    }

    public void setId_cuenta_destino(int id_cuenta_destino) {
        this.id_cuenta_destino = id_cuenta_destino;
    }

    public int getId_cuenta_origen() {
        return id_cuenta_origen;
    }

    public void setId_cuenta_origen(int id_cuenta_origen) {
        this.id_cuenta_origen = id_cuenta_origen;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getTipo_transaccion() {
        return tipo_transaccion;
    }

    public void setTipo_transaccion(String tipo_transaccion) {
        this.tipo_transaccion = tipo_transaccion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Date fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
