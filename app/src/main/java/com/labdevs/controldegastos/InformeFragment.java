package com.labdevs.controldegastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

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
import com.google.android.material.button.MaterialButton;
import com.labdevs.controldegastos.data.model.ItemInforme;
import com.labdevs.controldegastos.data.repositories.TransaccionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.labdevs.controldegastos.data.repositories.TransaccionRepository.FiltrosTransacciones.*;

public class InformeFragment extends Fragment {

    private TransaccionRepository.FiltrosTransacciones filtrosTransaccion;
    private int cuentaUsuario;
    private String tipoTrans;
    private LocalDate fecha;
    private FiltroGeneralFragment filtroGen;
    private Fragment filtroPeriodo;
    private AppViewModel viewModel;
    private List<PieEntry> values = new ArrayList<>();
    private PieChart grafico;
    private TextView total;
    private final String totalValueFormat = "%1$,.2f";

    public InformeFragment() {
        super();
        // TODO: fecha para testing, cambiar a now()
        fecha = LocalDate.of(2025,8,1);

        // TODO: asociar a la cuenta del usuario
        cuentaUsuario = 1;
        // Al inicar la vista ingreso por default viene seleccionado
        tipoTrans = "ingreso";

        filtroGen = new FiltroGeneralFragment(fecha, FiltroGeneralFragment.TipoFiltroGen.MENSUAL);
        filtroPeriodo = new FiltroPeriodoFragment(fecha);
        filtrosTransaccion = new TransaccionRepository.FiltrosTransacciones(cuentaUsuario,
                tipoTrans,
                TipoFiltroFecha.MES,
                fecha);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informe, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        view.findViewById(R.id.tipoFiltroButton).setOnClickListener(v->showMenu(view, v, R.menu.menu_boton_informe));
//        mainButton.setOnClickListener(v -> showMenu(view, v, R.menu.menu_boton_informe));

        total = view.findViewById(R.id.total);

        loadFiltroGenFragment();

        grafico = view.findViewById(R.id.graficoInforme);
        initializeChart(grafico, viewModel.listarTransacciones(filtrosTransaccion));

        viewModel.getFiltroFecha().observe(getViewLifecycleOwner(), this::cambiarFiltroFecha);

        return view;
    }

    private void cambiarFiltroFecha(LocalDate fecha){
        filtrosTransaccion.setFecha(fecha);
        loadChart();
    }

    private void initializeChart(PieChart grafico, List<ItemInforme> items) {
        values = getPieEntries(items);
        Float totalValue = getFloatTotalValue(values);
        String totalValueStr = String.format(totalValueFormat, totalValue);
        setTotalText(totalValueStr);
        initializeChart(grafico,totalValueStr);
    }

    private void loadChart(){
        values = getPieEntries(viewModel.listarTransacciones(filtrosTransaccion));
        Float totalValue = getFloatTotalValue(values);
        String totalValueStr = String.format(totalValueFormat, totalValue);
        setTotalText(totalValueStr);
        changeChartCenterText(totalValueStr);
        setPieDataSet(grafico);
    }

    private void changeChartCenterText(String total) {
        grafico.setCenterText("Total\n$ "+total);
        grafico.invalidate();
    }

    private void setTotalText(String total) {
        this.total.setText("$"+total);
    }

    private Float getFloatTotalValue(List<PieEntry> entries){
        return entries.stream().map(PieEntry::getValue).reduce(0f, Float::sum);
    }

    private List<PieEntry> getPieEntries(List<ItemInforme> items){
        return items.stream().map(item -> new PieEntry((float) item.monto,item.nombreCat)).toList();
    }

    private void initializeChart(PieChart chart,String total) {
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(80f);
        chart.setNoDataText("Aun no hay datos cargados");
        chart.setExtraOffsets(10f, 10f, 10f, 10f);
        chart.setDrawEntryLabels(false);
        chart.setCenterText("Total\n"+total);
        chart.setCenterTextSize(20f);
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);

        setPieDataSet(chart);
    }

    private void setPieDataSet(PieChart chart){
        PieDataSet dataSet = new PieDataSet(values, "Categorias");
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.invalidate();
    }

    private void showMenu(View v, View button, int menuRes) {
        PopupMenu popup = new PopupMenu(getActivity(), button);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_informe_opcion_1) {
                filtrosTransaccion.setTipoFiltroFecha(TipoFiltroFecha.PERIODO);
                loadChart();

                changeMainButtonText(v, "Semanal");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.SEMANAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_2) {
                filtrosTransaccion.setTipoFiltroFecha(TipoFiltroFecha.MES);
                loadChart();

                changeMainButtonText(v, "Mensual");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.MENSUAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_3) {
                filtrosTransaccion.setTipoFiltroFecha(TipoFiltroFecha.ANIO);
                loadChart();

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
        Button button = v.findViewById(R.id.tipoFiltroButton);
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