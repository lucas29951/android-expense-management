package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;

public class FiltroGeneralFragment extends Fragment {

    private LocalDate fecha;
    private TipoFiltroGen tipoFiltro;
    private AppViewModel viewModel;

    public FiltroGeneralFragment(LocalDate fecha, TipoFiltroGen tipoFiltro) {
        this.fecha = fecha;
        this.tipoFiltro = tipoFiltro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filtro_general, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        setButtonsListener(view);
        changeDateFormat(view);
        return view;
    }


    private void setButtonsListener(View view) {
        setButtonsListener(v -> changeDateFormat(view, fecha.minus(1, getTemporalAmount())), view.findViewById(R.id.botonFiltroGen1));
        setButtonsListener(v -> changeDateFormat(view, fecha.plus(1, getTemporalAmount())), view.findViewById(R.id.botonFiltroGen2));
    }

    private void setButtonsListener(View.OnClickListener listener, MaterialButton button) {
        button.setOnClickListener(listener);
    }


    private TemporalUnit getTemporalAmount() {
        switch (tipoFiltro) {
            case SEMANAL -> {
                return ChronoUnit.WEEKS;
            }
            case MENSUAL -> {
                return ChronoUnit.MONTHS;
            }
            case ANUAL -> {
                return ChronoUnit.YEARS;
            }
        }
        return null;
    }


    private void changeDateFormat(View view, LocalDate fecha) {
        this.fecha = fecha;
        cambiarFiltroFecha();
        changeDateFormat(view);
    }

    private void cambiarFiltroFecha() {
        viewModel.setFiltroFecha(fecha);
    }

    private void changeDateFormat(View view) {
        TextView textoFecha = view.findViewById(R.id.textoFecha);
        textoFecha.setText(fecha.query(tipoFiltro.getQuery(fecha)));
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoFiltroGen getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(TipoFiltroGen tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public enum TipoFiltroGen {
        SEMANAL, MENSUAL, ANUAL;

        private static final DateTimeFormatter formatoSemanal = DateTimeFormatter.ofPattern("dd.MM.yy");
        private static final DateTimeFormatter formatoMensual = DateTimeFormatter.ofPattern("LLL. yy");
        private static final DateTimeFormatter formatoAnual = DateTimeFormatter.ofPattern("yyyy");

        public TemporalQuery<String> getQuery(LocalDate fecha) {
            TemporalQuery<String> query = formatoMensual::format;
            switch (this) {
                case SEMANAL:
                    query = temporal -> {
                        TemporalField dayOfWeek = WeekFields.ISO.dayOfWeek();
                        return fecha.with(dayOfWeek, dayOfWeek.range().getMinimum()).format(formatoSemanal) + " ~ " + fecha.with(dayOfWeek, dayOfWeek.range().getMaximum()).format(formatoSemanal);
                    };
                    break;
                case ANUAL:
                    query = formatoAnual::format;
                    break;
            }
            return query;
        }
    }

}