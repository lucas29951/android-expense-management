package com.labdevs.controldegastos.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.labdevs.controldegastos.data.dao.TransaccionDAO;
import com.labdevs.controldegastos.data.database.AppDatabase;
import com.labdevs.controldegastos.data.entity.Transaccion;

import java.util.List;


public class TransaccionRepository {

    private TransaccionDAO transaccionDAO;
    private List<Transaccion> transacciones;

    public TransaccionRepository(Application application) {
        AppDatabase db = AppDatabase.obtenerInstancia(application);
        transaccionDAO = db.TransaccionDAO();
        transacciones = transaccionDAO.listarTodas();
    }

    public List<Transaccion> getAllTransacciones() {
        return transacciones;
    }

    public void insertar(Transaccion transaccion){
        transaccionDAO.insertar(transaccion);
    }

    public void eliminar(Transaccion t){
        transaccionDAO.eliminar(t);
    }
}
