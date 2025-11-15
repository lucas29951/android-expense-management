package com.labdevs.controldegastos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.labdevs.controldegastos.data.entity.Categoria;
import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.data.model.ItemInforme;
import com.labdevs.controldegastos.data.model.ItemResume;
import com.labdevs.controldegastos.data.repositories.CategoriaRepository;
import com.labdevs.controldegastos.data.repositories.CuentaRepository;
import com.labdevs.controldegastos.data.repositories.TransaccionRepository;

import java.time.LocalDate;
import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private final MutableLiveData<LocalDate> filtroFecha = new MutableLiveData<>();
    private final TransaccionRepository transaccionRepo;
    private final CategoriaRepository categoriaRepo;
    private MutableLiveData<ErrorET> error = new MutableLiveData<>();
    private boolean transaccionValida;
    private final LiveData<List<Cuenta>> allCuentas;
    private CuentaRepository cuentaRepo;
    private MutableLiveData<Cuenta> cuentaSelecionadaEliminar = new MutableLiveData<>();
    private MutableLiveData<Cuenta> cuentaSelecionada = new MutableLiveData<>();
    private boolean modificarCuenta;
    private boolean cuentaValida;
    private boolean hasExecutedOnce;
    private MutableLiveData<String> appBarTitle = new MutableLiveData<>();
    private MutableLiveData<Boolean> appBarNavIcon = new MutableLiveData<>();

    public AppViewModel(@NonNull Application application) {
        super(application);
        transaccionRepo = new TransaccionRepository(application);
        cuentaRepo = new CuentaRepository(application);
        categoriaRepo = new CategoriaRepository(application);
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

    public Cuenta buscarCuenta(int id){
        return cuentaRepo.buscarPor(id);
    }

    public void insertar(int id, String nombre, String saldo, String tipo) {
        cuentaValida = false;
        if (nombre.isEmpty()) {
            hasExecutedOnce = false;
            error.setValue(new ErrorET("El nombre es obligario", R.id.et_account_name));
            return;
        }
        if (saldo.isEmpty() || !saldo.matches("\\d+") || saldo.length() > 12) {
            hasExecutedOnce = false;
            error.setValue(new ErrorET("Rango de saldo invalido!", R.id.et_initial_balance));
            return;
        }
        if (!modificarCuenta){
            if (cuentaRepo.contarPor(nombre,tipo) > 0){
                hasExecutedOnce = false;
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

    public boolean hasExecutedOnce(){
        return hasExecutedOnce;
    }

    public void hasExecutedOnce(boolean hasExecutedOnce){
        this.hasExecutedOnce = hasExecutedOnce;
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

    // --- Vista Resumen ---

    public LiveData<List<ItemResume>> listarResumeItems(String tipoTrans){
        return transaccionRepo.listarItemsResume(tipoTrans);
    }

    public double sumarSaldoCuentas(){
        return cuentaRepo.sumarSaldoCuentas();
    }

    public List<Categoria> listarCategorias(){
        return categoriaRepo.listarCategorias();
    }

    public void insertarTransaccion(TransactionFragment.TransaccionWrapper transaccion){
        transaccionValida = false;
        if (transaccion.monto.isEmpty()){
            error.setValue(new ErrorET("El monto es obligario", R.id.et_amount));
            return;
        } else if (!transaccion.monto.matches("\\d+")) {
            error.setValue(new ErrorET("Monto invalido (no se permite caracteres extaños)", R.id.et_amount));
            return;
        } else if (transaccion.monto.length() > 12) {
            error.setValue(new ErrorET("Rango de monto invalido!", R.id.et_amount));
            return;
        }
        transaccionValida = true;
    }

    public boolean isTransaccionValida() {
        return transaccionValida;
    }
}
