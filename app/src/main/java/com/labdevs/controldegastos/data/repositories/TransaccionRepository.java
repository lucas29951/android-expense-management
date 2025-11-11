package com.labdevs.controldegastos.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.labdevs.controldegastos.data.dao.TransaccionDAO;
import com.labdevs.controldegastos.data.database.AppDatabase;
import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.data.model.ItemInforme;
import com.labdevs.controldegastos.data.model.ItemResume;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;


public class TransaccionRepository {

    private final TransaccionDAO transaccionDAO;
    private List<Transaccion> transacciones;

    public TransaccionRepository(Application application) {
        AppDatabase db = AppDatabase.obtenerInstancia(application);
        transaccionDAO = db.TransaccionDAO();
        transacciones = transaccionDAO.listarTodas();
    }

    public List<Transaccion> getAllTransacciones() {
        return transacciones;
    }

    public void insertar(Transaccion transaccion) {
        transaccionDAO.insertar(transaccion);
    }

    public void eliminar(Transaccion t) {
        transaccionDAO.eliminar(t);
    }

    public List<ItemInforme> listarTransacFiltradasPorFecha(FiltrosTransacciones filtros) {
        int filtroCuenta = filtros.getFiltroCuenta();
        String filtroTipoTrans = filtros.getFiltroTipoTrans();
        FiltrosTransacciones.Periodo periodo = filtros.getPeriodo();
        List<ItemInforme> lista = new ArrayList<>();

        switch (filtros.getTipoFiltroFecha()) {
            case PERIODO ->
                    lista = transaccionDAO.listarPorPeriodo(filtroCuenta, filtroTipoTrans, periodo.getFechaInicio(), periodo.getFechaFin());
            case MES ->
                    lista = transaccionDAO.listarPorMes(filtroCuenta, filtroTipoTrans, periodo.getFechaFin());
            case ANIO ->
                    lista = transaccionDAO.listarPorAnio(filtroCuenta, filtroTipoTrans, periodo.getFechaFin());
        }

        return lista;
    }


    public static class FiltrosTransacciones {
        private LocalDate fecha;
        private int filtroCuenta;
        private String filtroTipoTrans;
        private TipoFiltroFecha tipoFiltroFecha;
        private Periodo periodo;

        public FiltrosTransacciones(int filtroCuenta, String filtroTipoTrans, TipoFiltroFecha tipoFiltroFecha, LocalDate fecha) {
            this.filtroCuenta = filtroCuenta;
            this.filtroTipoTrans = filtroTipoTrans;
            this.tipoFiltroFecha = tipoFiltroFecha;
            this.fecha = fecha;
        }

        public int getFiltroCuenta() {
            return filtroCuenta;
        }

        public void setFiltroCuenta(int filtroCuenta) {
            this.filtroCuenta = filtroCuenta;
        }

        public String getFiltroTipoTrans() {
            return filtroTipoTrans;
        }

        public void setFiltroTipoTrans(String filtroTipoTrans) {
            this.filtroTipoTrans = filtroTipoTrans;
        }

        public TipoFiltroFecha getTipoFiltroFecha() {
            return tipoFiltroFecha;
        }

        public void setTipoFiltroFecha(TipoFiltroFecha tipoFiltroFecha) {
            this.tipoFiltroFecha = tipoFiltroFecha;
        }

        public Periodo getPeriodo() {
            switch (tipoFiltroFecha) {
                case PERIODO:
                    TemporalField dayOfWeek = WeekFields.ISO.dayOfWeek();
                    return new Periodo(TipoFiltroFecha.PERIODO.getFecha(fecha.with(dayOfWeek, dayOfWeek.range().getMinimum())), TipoFiltroFecha.PERIODO.getFecha(fecha.with(dayOfWeek, dayOfWeek.range().getMaximum())));
                case MES:
                    return new Periodo(null,TipoFiltroFecha.MES.getFecha(fecha));
                case ANIO:
                    return new Periodo(null,TipoFiltroFecha.ANIO.getFecha(fecha));
            }
            return null;
        }

        private void setPeriodo(Periodo periodo) {
            this.periodo = periodo;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public enum TipoFiltroFecha {
            PERIODO("yyyy-MM-dd"), MES("MM"), ANIO("yyyy");

            private final String formatoFecha;

            TipoFiltroFecha(String formatoFecha) {
                this.formatoFecha = formatoFecha;
            }

            public DateTimeFormatter getFormatoFecha() {
                return DateTimeFormatter.ofPattern(formatoFecha);
            }

            public String getFecha(LocalDate fecha) {
                return fecha.format(getFormatoFecha());
            }

        }

        public static class Periodo {
            private String fechaInicio;
            private String fechaFin;

            public Periodo(String fechaInicio, String fechaFin) {
                this.fechaInicio = fechaInicio;
                this.fechaFin = fechaFin;
            }

            public String getFechaInicio() {
                return fechaInicio;
            }

            public void setFechaInicio(String fechaInicio) {
                this.fechaInicio = fechaInicio;
            }

            public String getFechaFin() {
                return fechaFin;
            }

            public void setFechaFin(String fechaFin) {
                this.fechaFin = fechaFin;
            }
        }
    }

    public LiveData<List<ItemResume>> listarItemsResume(String tipoTrans){
        return transaccionDAO.listarItemsResume(tipoTrans);
    }
}
