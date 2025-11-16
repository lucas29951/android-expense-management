package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;


public class FiltroPeriodoFragment extends Fragment {

    private LocalDate fecha;

    public FiltroPeriodoFragment(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filtro_periodo, container, false);
        changeDateFormat(view);
        return view;
    }

    private void changeDateFormat(View view) {
        MaterialButton filtroDesde = view.findViewById(R.id.filtroFechaDesde);
        MaterialButton filtroHasta = view.findViewById(R.id.filtroFechaHasta);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        YearMonth month = YearMonth.from(fecha);
        filtroDesde.setText(fecha.format(formato));
        filtroHasta.setText(month.atEndOfMonth().format(formato));
    }
}