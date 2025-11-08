package com.labdevs.controldegastos;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.labdevs.controldegastos.data.database.AppDatabase;
import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.data.model.ItemInforme;
import com.labdevs.controldegastos.data.repositories.CuentaRepository;
import com.labdevs.controldegastos.data.repositories.TransaccionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class AppViewModel extends AndroidViewModel {

    private final MutableLiveData<LocalDate> filtroFecha = new MutableLiveData<>();
    private final TransaccionRepository transaccionRepo;
    private MutableLiveData<ErrorET> error = new MutableLiveData<>();
    private final LiveData<List<Cuenta>> allCuentas;
    private CuentaRepository cuentaRepo;
    private MutableLiveData<Cuenta> cuentaSelecionadaEliminar = new MutableLiveData<>();
    private MutableLiveData<Cuenta> cuentaSelecionada = new MutableLiveData<>();
    private boolean modificarCuenta;
    private boolean cuentaValida;
    private MutableLiveData<String> appBarTitle = new MutableLiveData<>();
    private MutableLiveData<Boolean> appBarNavIcon = new MutableLiveData<>();

    public AppViewModel(@NonNull Application application) {
        super(application);
        transaccionRepo = new TransaccionRepository(application);
        cuentaRepo = new CuentaRepository(application);
        allCuentas = cuentaRepo.listarCuentas();
        // el boton de alta tiene primero la funcionalidad de alta
        modificarCuenta = false;
    }

    // --- Error ---

    public LiveData<ErrorET> getEror() {
        return error;
    }

    // --- Appbar ---
    public LiveData<String> getAppBarTitle() {
        return appBarTitle;
    }

    public void setAppBarTitle(String title) {
        appBarTitle.setValue(title);
    }

    public LiveData<Boolean> getAppBarNavIcon() {
        return appBarNavIcon;
    }

    public void setAppBarNavIcon(Boolean enable) {
        appBarNavIcon.setValue(enable);
    }


    // --- Vista Cuentas ---
    public LiveData<List<Cuenta>> listarCuentas() {
        return allCuentas;
    }

    public List<Cuenta> getListaCuentas() {
        return cuentaRepo.allCuentas();
    }

    public LiveData<Cuenta> getCuentaSelecionada() {
        return cuentaSelecionada;
    }

    public LiveData<Cuenta> getCuentaSelecionadaEliminar() {
        return cuentaSelecionadaEliminar;
    }

    public void setCuentaSelecionadaEliminar(Cuenta cuentaSelecionada) {
        this.cuentaSelecionadaEliminar.setValue(cuentaSelecionada);
    }

    public void setCuentaSelecionada(Cuenta cuentaSelecionada) {
        this.cuentaSelecionada.setValue(cuentaSelecionada);
    }

    public void setModificarCuenta(boolean modificarCuenta) {
        this.modificarCuenta = modificarCuenta;
    }

    public boolean isCuentaValida(){
        return cuentaValida;
    }

    public void insertar(int id, String nombre, String saldo, String tipo) {
        cuentaValida = false;
        if (nombre.isEmpty()) {
            error.setValue(new ErrorET("El nombre es obligario", R.id.et_account_name));
            return;
        }
        if (saldo.isEmpty() || !saldo.matches("\\d+") || saldo.length() > 12) {
            error.setValue(new ErrorET("Rango de saldo invalido!", R.id.et_initial_balance));
            return;
        }
        if (!modificarCuenta){
            if (cuentaRepo.contarPor(nombre,tipo) > 0){
                error.setValue(new ErrorET("Ya existe cuenta con ese mismo nombre y tipo",R.id.et_account_name));
                return;
            }
        }
        cuentaValida = true;

        Cuenta cuenta = new Cuenta(nombre, tipo, Double.parseDouble(saldo));
        if (modificarCuenta){
            cuenta.id = id;
        }
        cuentaRepo.insertarOActualizar(cuenta);
        // una vez que se termina de actulizar o dar de alta una cuenta -> luego, el boton SIEMPRE tiene la funcionalidad de alta
        modificarCuenta = false;
    }

    public void eliminar(Cuenta cuenta) {
        cuentaRepo.elimiar(cuenta);
    }

    // --- Vista Informe ---
    public void insertar(Transaccion transaccion) {
        transaccionRepo.insertar(transaccion);
    }

    public void eliminar(Transaccion t) {
        transaccionRepo.eliminar(t);
    }

    public List<ItemInforme> listarTransacciones(TransaccionRepository.FiltrosTransacciones filtros) {
        return transaccionRepo.listarTransacFiltradasPorFecha(filtros);
    }

    public LiveData<LocalDate> getFiltroFecha() {
        return filtroFecha;
    }

    public void setFiltroFecha(LocalDate filtroFecha) {
        this.filtroFecha.setValue(filtroFecha);
    }

    public record ErrorET(String message, int etId) {
    }

}
