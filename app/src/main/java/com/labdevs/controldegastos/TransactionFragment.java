package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.labdevs.controldegastos.adapters.CategoriaSpinnerAdapter;
import com.labdevs.controldegastos.adapters.CuentaSpinnerAdapter;
import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.databinding.FragmentTransactionBinding;

import java.util.List;

public class TransactionFragment extends Fragment {

    private @NonNull FragmentTransactionBinding binding;
    private AppViewModel viewModel;
    private CategoriaSpinnerAdapter categoriasAdapter;
    private ArrayAdapter<CharSequence> tipoTransaccionesAdapter;
    private CuentaSpinnerAdapter cuentaOrigenAdapter;
    private CuentaSpinnerAdapter cuentaDestinoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        categoriasAdapter = new CategoriaSpinnerAdapter(requireContext(), viewModel.listarCategorias());

        setupCategoriaSpinnerAdapter();

        List<Cuenta> listaCuentas = viewModel.getListaCuentas();
        setupCuentaOrigenSpinnerAdapter(listaCuentas);
        setupCuentaDestinoSpinnerAdapter(listaCuentas);
    }

    private void setupCategoriaSpinnerAdapter() {
        tipoTransaccionesAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.transaction_type_array,
                android.R.layout.simple_spinner_item);
        tipoTransaccionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setupCuentaDestinoSpinnerAdapter(List<Cuenta> listaCuentas) {
        cuentaDestinoAdapter = new CuentaSpinnerAdapter(requireContext(),listaCuentas);
    }

    private void setupCuentaOrigenSpinnerAdapter(List<Cuenta> listaCuentas) {
        cuentaOrigenAdapter = new CuentaSpinnerAdapter(requireContext(),listaCuentas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater,container,false);

        viewModel.setAppBarTitle(getString(R.string.transaction_title));
        viewModel.setAppBarNavIcon(true);

        binding.spCategory.setAdapter(categoriasAdapter);
        binding.spTransactionType.setAdapter(tipoTransaccionesAdapter);
        binding.spOriginAccount.setAdapter(cuentaOrigenAdapter);
        binding.spDestinationAccount.setAdapter(cuentaDestinoAdapter);

        return binding.getRoot();
    }

}