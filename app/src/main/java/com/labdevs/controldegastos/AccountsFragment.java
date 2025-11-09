package com.labdevs.controldegastos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.databinding.FragmentAccountsBinding;

public class AccountsFragment extends Fragment {

    private FragmentAccountsBinding binding;
    private AppViewModel viewModel;
    private CuentaAdapter cuentaAdapter;
    private ManageAccountFragment manageAccountFrag;
    private FragmentActivity fragmentActivity;
    private Cuenta cuentaSeleccionadaEliminar;
    private AlertDialog.Builder dialog;
    public final MenuProvider menuProvider = new AccountFragmentMenuProvider();
    private boolean seleccionarCuentaPredeterminada;

    public AccountsFragment(boolean seleccionarCuentaPredeterminada) {
        this.seleccionarCuentaPredeterminada = seleccionarCuentaPredeterminada;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        binding = FragmentAccountsBinding.inflate(inflater, container, false);
        fragmentActivity = requireActivity();

        manageAccountFrag = new ManageAccountFragment(viewModel);

        viewModel.setAppBarTitle(getString(R.string.title_cuentas));

        // top app bar menu
        fragmentActivity.addMenuProvider(menuProvider);

        if (dialog == null) {
            setupDeleteDialog();
        }

        binding.btnAddAccount.setOnClickListener(v -> loadManageAccountFragment());

        setupRecyclerView();
        viewModel.listarCuentas().observe(getViewLifecycleOwner(), cuentas -> cuentaAdapter.setCuentas(cuentas));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getCuentaSelecionada().observe(getViewLifecycleOwner(), this::loadManageAccountFragment);
        viewModel.getCuentaSelecionadaEliminar().observe(getViewLifecycleOwner(), this::setCuentaSeleccionadaEliminar);
        viewModel.getCuentaPredeterminada().observe(getViewLifecycleOwner(),this::setCuentaPredeterminada);
        Log.d("AccountFragment","on view created!");
    }

    private void setCuentaPredeterminada(Cuenta cuenta) {
        SharedPreferences preferences = fragmentActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("cuenta",cuenta.id);
        editor.apply();
        Log.d("AccountFragment","se agrego la cuenta (id:"+ cuenta.id + ") como predeterminada");
        getParentFragmentManager().popBackStack();
    }

    private void setupDeleteDialog() {
        dialog = new AlertDialog.Builder(fragmentActivity);
        dialog.setMessage(R.string.delete_account_dialog_message);
        dialog.setPositiveButton(R.string.ok, (dialog, id) -> deleteAccount(cuentaSeleccionadaEliminar));
        dialog.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
    }

    private void loadManageAccountFragment(Cuenta cuenta) {
        loadManageAccountFragment();
    }

    private void loadManageAccountFragment() {
        if (getParentFragmentManager().findFragmentByTag(ManageAccountFragment.class.getSimpleName()) == null) {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, manageAccountFrag, ManageAccountFragment.class.getSimpleName());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void setupRecyclerView() {
        cuentaAdapter = new CuentaAdapter((MainActivity) getActivity(), viewModel.getListaCuentas(), viewModel,seleccionarCuentaPredeterminada);
        binding.rvAccountsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAccountsList.setAdapter(cuentaAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // nav icon de app bar se vuelve a esconder
        viewModel.setAppBarNavIcon(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentActivity.removeMenuProvider(menuProvider);
    }

    public class AccountFragmentMenuProvider implements MenuProvider {

        private boolean isDelete;

        public AccountFragmentMenuProvider() {
            this.isDelete = true;
        }

        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_appbar, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.appBarItem1) {
                loadManageAccountFragment();
                return true;
            } else if (itemId == R.id.appBarItem2) {
                changeIconItemsRV();
                updateIcon();
                return true;
            }
            return false;
        }

        private void updateIcon() {
            isDelete = !isDelete;
            fragmentActivity.invalidateOptionsMenu();
        }

        @Override
        public void onPrepareMenu(@NonNull Menu menu) {
            MenuProvider.super.onPrepareMenu(menu);
            menu.findItem(R.id.appBarItem1).setIcon(R.drawable.add_24px);
            if (isDelete) {
                menu.findItem(R.id.appBarItem2).setIcon(R.drawable.delete_24px);
            } else {
                menu.findItem(R.id.appBarItem2).setIcon(R.drawable.close_24px);
            }
            menu.findItem(R.id.appBarItem3).setVisible(false);
        }
    }

    private void changeIconItemsRV() {
        cuentaAdapter.chageAccountIconFunction();
    }

    private void setCuentaSeleccionadaEliminar(Cuenta cuentaSeleccionadaEliminar) {
        this.cuentaSeleccionadaEliminar = cuentaSeleccionadaEliminar;
        dialog.show();
    }

    private void deleteAccount(Cuenta cuenta) {
        viewModel.eliminar(cuenta);
    }

}