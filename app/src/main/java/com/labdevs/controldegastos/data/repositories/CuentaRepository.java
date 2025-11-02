package com.labdevs.controldegastos.data.repositories;

import android.app.Application;

import com.labdevs.controldegastos.data.dao.CuentaDAO;
import com.labdevs.controldegastos.data.database.AppDatabase;
import com.labdevs.controldegastos.data.entity.Cuenta;

import java.util.List;

public class CuentaRepository {

    private final CuentaDAO cuentaDAO;

    public CuentaRepository(Application application) {
        AppDatabase db = AppDatabase.obtenerInstancia(application);
        cuentaDAO = db.CuentaDAO();
    }

    public void insertarOActualizar(Cuenta cuenta){
        cuentaDAO.insertOrUpdate(cuenta);
    }

    public void elimiar(Cuenta cuenta){
        cuentaDAO.delete(cuenta);
    }

    public List<Cuenta> listarCuentas(){
        return cuentaDAO.listAll();
    }
}
