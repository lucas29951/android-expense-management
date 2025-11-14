package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.labdevs.controldegastos.adapters.CategoriaSpinnerAdapter;
import com.labdevs.controldegastos.adapters.CuentaSpinnerAdapter;
import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.databinding.FragmentTransactionBinding;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionFragment extends Fragment {

    private @NonNull FragmentTransactionBinding binding;
    private AppViewModel viewModel;
    private CategoriaSpinnerAdapter categoriasAdapter;
    private ArrayAdapter<CharSequence> tipoTransaccionesAdapter;
    private CuentaSpinnerAdapter cuentaOrigenAdapter;
    private CuentaSpinnerAdapter cuentaDestinoAdapter;
    private List<Cuenta> listaCuentas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        categoriasAdapter = new CategoriaSpinnerAdapter(requireContext(), viewModel.listarCategorias());

        setupCategoriaSpinnerAdapter();

        listaCuentas = viewModel.getListaCuentas();
        Map<String, List<Cuenta>> mapCuentasNombresDuplicados = listaCuentas.stream().collect(Collectors.groupingBy(cuenta -> cuenta.nombre));
        // dame un map que tenga cuentas (List<Cuenta>>) duplicadas (> 1)
        mapCuentasNombresDuplicados = mapCuentasNombresDuplicados.entrySet().stream().filter(entry -> entry.getValue().size() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        setupCuentaOrigenSpinnerAdapter(listaCuentas, mapCuentasNombresDuplicados);
        setupCuentaDestinoSpinnerAdapter(listaCuentas, mapCuentasNombresDuplicados);
    }

    private void setupCategoriaSpinnerAdapter() {
        tipoTransaccionesAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.transaction_type_array,
                android.R.layout.simple_spinner_item);
        tipoTransaccionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setupCuentaDestinoSpinnerAdapter(List<Cuenta> listaCuentas, Map<String, List<Cuenta>> mapCuentasNombresDuplicados) {
        cuentaDestinoAdapter = new CuentaSpinnerAdapter(requireContext(), listaCuentas, mapCuentasNombresDuplicados);
    }

    private void setupCuentaOrigenSpinnerAdapter(List<Cuenta> listaCuentas, Map<String, List<Cuenta>> mapCuentasNombresDuplicados) {
        cuentaOrigenAdapter = new CuentaSpinnerAdapter(requireContext(), listaCuentas, mapCuentasNombresDuplicados);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater, container, false);

        viewModel.setAppBarTitle(getString(R.string.transaction_title));
        viewModel.setAppBarNavIcon(true);

        binding.spCategory.setAdapter(categoriasAdapter);
        binding.spTransactionType.setAdapter(tipoTransaccionesAdapter);
        binding.spOriginAccount.setAdapter(cuentaOrigenAdapter);
        binding.spDestinationAccount.setAdapter(cuentaDestinoAdapter);

        setupSpinnersListeners();

        return binding.getRoot();
    }

    public enum tipoTransaccion {
        GASTO, INGRESO, TRANSFERENCIA;
    }

    private void setupSpinnersListeners() {
        binding.spTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == tipoTransaccion.TRANSFERENCIA.ordinal()) {
                    binding.spDestinationAccount.setVisibility(View.VISIBLE);
                } else {
                    if (binding.spDestinationAccount.getVisibility() == View.VISIBLE) {
                        binding.spDestinationAccount.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        binding.spOriginAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cuentaDestinoAdapter.updateList(listaCuentas.stream().filter(cuenta -> cuenta.id != id).toList());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}