package com.labdevs.controldegastos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.data.model.ItemInforme;
import com.labdevs.controldegastos.data.repositories.CuentaRepository;
import com.labdevs.controldegastos.data.repositories.TransaccionRepository;

import java.time.LocalDate;
import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private final MutableLiveData<LocalDate> filtroFecha = new MutableLiveData<>();
    private final TransaccionRepository transaccionRepo;
    private CuentaRepository cuentaRepo;

    public AppViewModel(@NonNull Application application) {
        super(application);
        transaccionRepo = new TransaccionRepository(application);
        cuentaRepo = new CuentaRepository(application);
    }
    // --- Vista Cuentas ---

    public List<Cuenta> listarCuentas(){
        return cuentaRepo.listarCuentas();
    }


    // --- Vista Informe ---
    public void insertar(Transaccion transaccion){
        transaccionRepo.insertar(transaccion);
    }

    public void eliminar(Transaccion t){
        transaccionRepo.eliminar(t);
    }

    public List<ItemInforme> listarTransacciones(TransaccionRepository.FiltrosTransacciones filtros){
        return transaccionRepo.listarTransacFiltradasPorFecha(filtros);
    }

    public LiveData<LocalDate> getFiltroFecha() {
        return filtroFecha;
    }

    public void setFiltroFecha(LocalDate filtroFecha) {
        this.filtroFecha.setValue(filtroFecha);
    }
}
