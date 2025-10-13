package com.labdevs.controldegastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;

import java.text.BreakIterator;
import java.time.LocalDate;
import java.util.ArrayList;

public class InformeFragment extends Fragment {

    private LocalDate fecha;
    private FiltroGeneralFragment filtroGen;
    private Fragment filtroPeriodo;

    public InformeFragment() {
        super();
        fecha = LocalDate.now();
        filtroGen = new FiltroGeneralFragment(fecha, FiltroGeneralFragment.TipoFiltroGen.MENSUAL);
        filtroPeriodo = new FiltroPeriodoFragment(fecha);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informe, container, false);

        Button trailingButton = view.findViewById(R.id.trailingButton);
        Button mainButton = view.findViewById(R.id.mainButton);
        trailingButton.setOnClickListener(v -> showMenu(view, v, R.menu.menu_boton_informe));
        mainButton.setOnClickListener(v -> showMenu(view, v, R.menu.menu_boton_informe));

        loadFiltroGenFragment();

        loadChart(view.findViewById(R.id.graficoInforme));

        return view;
    }

    private void loadChart(PieChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(80f);
        chart.setNoDataText("Aun no hay datos cargados");
        chart.setExtraOffsets(10f, 10f, 10f, 10f);
        chart.setDrawEntryLabels(false);
        chart.setCenterText("Total\n$1,234");
        chart.setCenterTextSize(20f);
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);

        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(30f, "Salud"));
        values.add(new PieEntry(40f, "Mascotas"));
        values.add(new PieEntry(10f, "Alimentos"));
        values.add(new PieEntry(50f, "Casa"));

        PieDataSet dataSet = new PieDataSet(values, "Categorias");
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        chart.setData(data);

    }

    private void showMenu(View v, View button, int menuRes) {
        PopupMenu popup = new PopupMenu(getActivity(), button);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_informe_opcion_1) {
                changeMainButtonText(v, "Semanal");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.SEMANAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_2) {
                changeMainButtonText(v, "Mensual");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.MENSUAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_3) {
                changeMainButtonText(v, "Anual");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.ANUAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_4) {
                changeMainButtonText(v, "Periodo");
                getChildFragmentManager().beginTransaction().replace(R.id.filtrosFragment, filtroPeriodo).commit();
                return true;
            }
            return false;
        });

        popup.show();


    }

    private void changeMainButtonText(View v, String str) {
        Button button = v.findViewById(R.id.mainButton);
        button.setText(str);
    }

    private void loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen tipoFiltroGen) {
        filtroGen = new FiltroGeneralFragment(fecha, tipoFiltroGen);
        loadFiltroGenFragment();
    }

    private void loadFiltroGenFragment() {
        getChildFragmentManager().beginTransaction().replace(R.id.filtrosFragment, filtroGen).commit();
    }

}