package com.labdevs.controldegastos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.data.repositories.TransaccionRepository;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private final TransaccionRepository transaccionRepo;
    private final List<Transaccion> transaciones;

    public AppViewModel(@NonNull Application application) {
        super(application);
        transaccionRepo = new TransaccionRepository(application);
        transaciones = transaccionRepo.getAllTransacciones();
    }

    public List<Transaccion> getAllTransaciones() {
        return transaciones;
    }

    public void insertar(Transaccion transaccion){
        transaccionRepo.insertar(transaccion);
    }

    public void eliminar(Transaccion t){
        transaccionRepo.eliminar(t);
    }
}
