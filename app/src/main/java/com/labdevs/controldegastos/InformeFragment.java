package com.labdevs.controldegastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.labdevs.controldegastos.data.entity.Transaccion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InformeFragment extends Fragment {

    private LocalDate fecha;
    private FiltroGeneralFragment filtroGen;
    private Fragment filtroPeriodo;
    private AppViewModel viewModel;
    private final String[] nombres = {"Salud", "Hogar", "Alimentos", "Mascotas", "Transporte", "Refaccion Casa"};
    private List<PieEntry> values = new ArrayList<>();

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

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        Button trailingButton = view.findViewById(R.id.trailingButton);
        Button mainButton = view.findViewById(R.id.mainButton);
        trailingButton.setOnClickListener(v -> showMenu(view, v, R.menu.menu_boton_informe));
        mainButton.setOnClickListener(v -> showMenu(view, v, R.menu.menu_boton_informe));

        loadFiltroGenFragment();

        loadChart(view.findViewById(R.id.graficoInforme), viewModel.getAllTransaciones());

        return view;
    }

    private void loadChart(PieChart chart, List<Transaccion> values) {
        Map<Integer, List<Transaccion>> map = values.stream().collect(Collectors.groupingBy(t -> t.id_categoria));
        List<Entry> entries = map.entrySet().stream().map(e -> new Entry(e.getValue().stream().mapToDouble(t -> t.monto).sum(), e.getKey())).toList();
        this.values = entries.stream().map(e -> new PieEntry((float) e.monto, nombres[e.idCat])).toList();
        loadChart(chart);
    }

    public record Entry(double monto, int idCat) {
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