package com.labdevs.controldegastos;

import static com.labdevs.controldegastos.TransactionFragment.TipoTransaccion.*;
import static com.labdevs.controldegastos.utils.UiUtils.Formats.getAmountFormatedStr;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.labdevs.controldegastos.data.database.Converters;
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

    private MutableLiveData<LocalDate> filtroFecha;
    private final TransaccionRepository transaccionRepo;
    private final CategoriaRepository categoriaRepo;
    private MutableLiveData<ErrorET> error = new MutableLiveData<>();
    private boolean transaccionValida;
    private ErrorET errorTransaccion;
    private final LiveData<List<Cuenta>> allCuentas;
    private CuentaRepository cuentaRepo;
    private MutableLiveData<Cuenta> cuentaSelecionadaEliminar = new MutableLiveData<>();
    private MutableLiveData<Cuenta> cuentaSelecionada = new MutableLiveData<>();
    private boolean modificarCuenta;
    private boolean cuentaValida;
    private final LiveData<Double> saldoCuentas;
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
        saldoCuentas = cuentaRepo.sumarSaldoCuentas();
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

    public boolean isCuentaValida() {
        return cuentaValida;
    }

    public Cuenta buscarCuenta(int id) {
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
        if (!modificarCuenta) {
            if (cuentaRepo.contarPor(nombre, tipo) > 0) {
                hasExecutedOnce = false;
                error.setValue(new ErrorET("Ya existe cuenta con ese mismo nombre y tipo", R.id.et_account_name));
                return;
            }
        }
        cuentaValida = true;

        Cuenta cuenta = new Cuenta(nombre, tipo, Double.parseDouble(saldo));
        if (modificarCuenta) {
            cuenta.id = id;
            cuentaRepo.actualizar(cuenta);
        } else {
            cuentaRepo.insertar(cuenta);
        }
        // una vez que se termina de actulizar o dar de alta una cuenta -> luego, el boton SIEMPRE tiene la funcionalidad de alta
        modificarCuenta = false;
    }

    public void eliminar(Cuenta cuenta) {
        cuentaRepo.elimiar(cuenta);
    }

    public boolean hasExecutedOnce() {
        return hasExecutedOnce;
    }

    public void hasExecutedOnce(boolean hasExecutedOnce) {
        this.hasExecutedOnce = hasExecutedOnce;
    }

    // --- Vista Informe ---

    public List<ItemInforme> listarTransacciones(TransaccionRepository.FiltrosTransacciones filtros) {
        return transaccionRepo.listarTransacFiltradasPorFecha(filtros);
    }

    public LiveData<LocalDate> getFiltroFecha() {
        // se crea un observer nuevo, ya que dos fragment distintos no pueden tener el mismo filtro de fecha
        // para evitar que queden los cambios de uno en otro
        return filtroFecha = new MutableLiveData<>();
    }

    public void setFiltroFecha(LocalDate filtroFecha) {
        this.filtroFecha.setValue(filtroFecha);
    }

    public record ErrorET(String message, int etId) {
    }

    // --- Vista Resumen ---

    public LiveData<List<ItemResume>> listarResumeItems(TransaccionRepository.FiltrosTransacciones filtro) {
        return transaccionRepo.listarItemsResume(filtro);
    }

    public LiveData<Double> getSaldoCuentas() {
        return saldoCuentas;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepo.listarCategoriasList();
    }

    public void insertarTransaccion(TransactionFragment.TransaccionWrapper transaccionWrapper) {
        transaccionValida = false;
        if (transaccionWrapper.monto.isEmpty()) {
            errorTransaccion = new ErrorET("El monto es obligario", R.id.et_amount);
            return;
        } else if (!transaccionWrapper.monto.matches("\\d+")) {
            errorTransaccion = new ErrorET("Monto invalido (no se permite caracteres extaños)", R.id.et_amount);
            return;
        } else if (transaccionWrapper.monto.length() > 12) {
            errorTransaccion = new ErrorET("Rango de monto invalido!", R.id.et_amount);
            return;
        }

        // aun no esta valida la transaccion
        // falta validar que no se supere el saldo en un gasto/transferencia
        // pero se crea un contenedor para los datos
        Transaccion transaccion = new Transaccion(Double.parseDouble(transaccionWrapper.monto),
                Converters.toDate(transaccionWrapper.fecha_hora),
                transaccionWrapper.comentario,
                transaccionWrapper.tipo_transaccion,
                transaccionWrapper.id_categoria,
                transaccionWrapper.id_cuenta_origen,
                transaccionWrapper.id_cuenta_destino);

        Cuenta cuentaOrigen = cuentaRepo.buscarPor(transaccion.id_cuenta_origen);
        Cuenta cuentaDestino = cuentaOrigen;
        if (transaccion.tipo_transaccion.equals(getString(GASTO.ordinal()))) {
            if (cuentaOrigen.saldo < transaccion.monto) {
                errorTransaccion = new ErrorET("El gasto no debe de superar el saldo actual de la cuenta : nombre: " + cuentaOrigen.nombre + " saldo: " + getAmountFormatedStr(cuentaOrigen.saldo),
                        R.id.et_amount);
                return;
            }
        } else if (transaccion.tipo_transaccion.equals(getString(TRANSFERENCIA.ordinal()))) {
            cuentaDestino = cuentaRepo.buscarPor(transaccion.id_cuenta_destino);
            if (cuentaOrigen.saldo < transaccion.monto) {
                errorTransaccion = new ErrorET("La transferencia no debe de superar el saldo actual de la cuenta : nombre: " + cuentaOrigen.nombre + " saldo: " + getAmountFormatedStr(cuentaOrigen.saldo),
                        R.id.et_amount);
                return;
            }
        }
        transaccionValida = true;


        actulizarSaldoCuenta(transaccion, cuentaOrigen, cuentaDestino);
        transaccionRepo.insertarOActualizar(transaccion);
        errorTransaccion = null;
    }

    private void actulizarSaldoCuenta(Transaccion transaccion, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        if (transaccion.tipo_transaccion.equals(getString(GASTO.ordinal()))) {
            cuentaOrigen.saldo -= transaccion.monto;
        } else if (transaccion.tipo_transaccion.equals(getString(INGRESO.ordinal()))) {
            cuentaOrigen.saldo += transaccion.monto;
        } else { //TRANSFERENCIA
            cuentaOrigen.saldo -= transaccion.monto;
            cuentaDestino.saldo += transaccion.monto;
            cuentaRepo.actualizar(cuentaDestino);
            cuentaRepo.actualizar(cuentaOrigen);
            return;
        }
        cuentaRepo.actualizar(cuentaOrigen);
    }

    public boolean isTransaccionValida() {
        return transaccionValida;
    }

    public ErrorET getErrorTransaccion() {
        return errorTransaccion;
    }
}
