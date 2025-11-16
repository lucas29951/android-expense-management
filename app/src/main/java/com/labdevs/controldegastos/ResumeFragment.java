package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.labdevs.controldegastos.data.repositories.TransaccionRepository;
import com.labdevs.controldegastos.databinding.FragmentResumeBinding;

import java.time.LocalDate;

public class ResumeFragment extends Fragment {

    private FragmentResumeBinding binding;
    private AppViewModel viewModel;
    private ResumeAdapter resumeAdapter;
    private String tipoTrans;
    private final String[] tiposTransaccion = {"ingreso","gasto"};
    private FiltroGeneralFragment filtroGen;
    private LocalDate fecha;
    private TransaccionRepository.FiltrosTransacciones filtrosTransaccion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        tipoTrans = "ingreso";
        // TODO: fecha para testing, cambiar a now()
        fecha = LocalDate.of(2025, 8, 1);
        filtroGen = new FiltroGeneralFragment(fecha, FiltroGeneralFragment.TipoFiltroGen.MENSUAL);
        filtrosTransaccion = new TransaccionRepository.FiltrosTransacciones(-1,tipoTrans, TransaccionRepository.FiltrosTransacciones.TipoFiltroFecha.MES,fecha);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResumeBinding.inflate(inflater,container,false);

        viewModel.setAppBarTitle(getString(R.string.resume_title));

        binding.segmentedButtonsResume.setOnSelectedOptionChangeCallback(index -> {
            chageTransactionType(index);
            setListResumeItemsObserver();
            setTitleList(index);
            return null;
        });

        loadFiltroGenFragment();

        binding.filterTypeButton.setOnClickListener(button -> showMenuFilters(button, R.menu.menu_boton_informe));

        viewModel.getSaldoCuentas().observe(getViewLifecycleOwner(), this::setTotalCardTitle);

        viewModel.listarResumeItems(filtrosTransaccion).observe(getViewLifecycleOwner(), items -> resumeAdapter.submitList(items));

        viewModel.getFiltroFecha().observe(getViewLifecycleOwner(),fecha -> changeTransactionDate(fecha));

        binding.fabAddTransanction.setOnClickListener(view -> loadTransactionFragment());

        setupRecycleView();

        return binding.getRoot();
    }

    private void setTotalCardTitle(Double saldo) {
        binding.tvCardTotalAmount.setText("$ "+String.format(CuentaAdapter.saldoFormat,saldo));
    }

    private void changeTransactionDate(LocalDate fecha) {
        filtrosTransaccion.setFecha(fecha);
        setListResumeItemsObserver();
    }

    private void chageTransactionType(Integer index) {
        filtrosTransaccion.setFiltroTipoTrans(getIndex(index));
    }

    private void setListResumeItemsObserver(){
        viewModel.listarResumeItems(filtrosTransaccion).observe(getViewLifecycleOwner(), items -> resumeAdapter.submitList(items));
    }

    private void loadTransactionFragment(){
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_layout,new TransactionFragment()).addToBackStack(null).commit();
    }

    private void setTitleList(Integer index) {
        if (index==0){
            setTitleList(getActivity().getString(R.string.income_section_title));
        } else {
            setTitleList(getActivity().getString(R.string.expenses_section_title));
        }
    }

    private void setTitleList(String text) {
        binding.tvRecent.setText(text);
    }

    private String getIndex(Integer index) {
        if(index==0)
            return tiposTransaccion[index++];
        else
            return tiposTransaccion[index--];
    }

    private void setupRecycleView() {
        resumeAdapter = new ResumeAdapter((MainActivity) getActivity());
        binding.rvExpensesList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvExpensesList.setAdapter(resumeAdapter);
    }

    private void showMenuFilters(View button, int menuRes) {
        PopupMenu popup = new PopupMenu(getActivity(), button);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_informe_opcion_1) {
                filtrosTransaccion.setTipoFiltroFecha(TransaccionRepository.FiltrosTransacciones.TipoFiltroFecha.PERIODO);

                changeMainButtonText("Semanal");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.SEMANAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_2) {
                filtrosTransaccion.setTipoFiltroFecha(TransaccionRepository.FiltrosTransacciones.TipoFiltroFecha.MES);

                changeMainButtonText("Mensual");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.MENSUAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_3) {
                filtrosTransaccion.setTipoFiltroFecha(TransaccionRepository.FiltrosTransacciones.TipoFiltroFecha.ANIO);

                changeMainButtonText("Anual");
                loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen.ANUAL);
                return true;
            } else if (itemId == R.id.menu_informe_opcion_4) {
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void changeMainButtonText(String str) {
        binding.filterTypeButton.setText(str);
    }

    private void loadFiltroGenFragment(FiltroGeneralFragment.TipoFiltroGen tipoFiltroGen) {
        filtroGen = new FiltroGeneralFragment(fecha, tipoFiltroGen);
        loadFiltroGenFragment();
    }

    private void loadFiltroGenFragment() {
        getChildFragmentManager().beginTransaction().replace(R.id.resume_filters_fragment, filtroGen).commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        viewModel.setAppBarNavIcon(false);
    }
}