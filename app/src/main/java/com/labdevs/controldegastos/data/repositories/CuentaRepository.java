package com.labdevs.controldegastos.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

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


    public void insertar(Cuenta cuenta){
        AppDatabase.databaseWriteExecutor.execute(() -> cuentaDAO.insert(cuenta));
    }

    public void actualizar(Cuenta cuenta){
        AppDatabase.databaseWriteExecutor.execute(() -> cuentaDAO.update(cuenta));
    }

    public void elimiar(Cuenta cuenta) {
        AppDatabase.databaseWriteExecutor.execute(() -> cuentaDAO.delete(cuenta));
    }

    public LiveData<List<Cuenta>> listarCuentas() {
        return cuentaDAO.listAll();
    }

    public List<Cuenta> allCuentas() {
        return cuentaDAO.listarTodas();
    }

    public int contarPor(String nombre, String tipo) {
        return cuentaDAO.contarPor(nombre, tipo);
    }

    public Cuenta buscarPor(int id){
        return cuentaDAO.buscarPorId(id);
    }

    public LiveData<Double> sumarSaldoCuentas(){
        return cuentaDAO.sumPorSaldo();
    }
}
