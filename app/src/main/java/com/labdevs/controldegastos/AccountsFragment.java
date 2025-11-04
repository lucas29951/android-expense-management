package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
    private ManageAccountFragment manageAccountFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccountsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        manageAccountFrag = new ManageAccountFragment(viewModel);

        viewModel.setAppBarTitle(getString(R.string.title_cuentas));

        binding.btnAddAccount.setOnClickListener(v -> loadManageAccountFragment());

        setupRecyclerView();
        viewModel.listarCuentas().observe(getViewLifecycleOwner(), cuentas -> cuentaAdapter.setCuentas(cuentas));

        return binding.getRoot();
    }

    private void loadManageAccountFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction().replace(R.id.fragment_layout, manageAccountFrag);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void setupRecyclerView() {
        cuentaAdapter = new CuentaAdapter((MainActivity) getActivity(), viewModel.getListaCuentas());
        binding.rvAccountsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAccountsList.setAdapter(cuentaAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // nav icon de app bar se vuelve a esconder
        viewModel.setAppBarNavIcon(false);
    }
}