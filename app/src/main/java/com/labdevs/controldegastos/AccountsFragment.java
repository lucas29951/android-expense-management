package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labdevs.controldegastos.databinding.FragmentAccountsBinding;

public class AccountsFragment extends Fragment {

    private FragmentAccountsBinding binding;
    private AppViewModel viewModel;
    private CuentaAdapter cuentaAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccountsBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        viewModel.setAppBarTitle(getString(R.string.title_cuentas));

        loadAdapter();

        return binding.getRoot();
    }

    private void loadAdapter() {
        cuentaAdapter = new CuentaAdapter((MainActivity) getActivity(), viewModel.listarCuentas());
        binding.rvAccountsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAccountsList.setAdapter(cuentaAdapter);
    }

}