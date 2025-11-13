package com.labdevs.controldegastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.databinding.ListSpinnerItemCuentaBinding;

import java.util.List;

public class CuentaSpinnerAdapter extends BaseAdapter {

    private final List<Cuenta> listaCuentas;
    private LayoutInflater lInflater;

    public CuentaSpinnerAdapter(Context context, List<Cuenta> listaCuentas) {
        lInflater = LayoutInflater.from(context);
        this.listaCuentas = listaCuentas;
    }

    @Override
    public int getCount() {
        return listaCuentas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCuentas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaCuentas.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cuenta cuenta = listaCuentas.get(position);
        ListSpinnerItemCuentaBinding binding = ListSpinnerItemCuentaBinding.inflate(lInflater, parent, false);

        // TODO: implementar logica para el caso de nombres de cuentas duplicadas
        binding.cuentaSpinnerText1.setText(cuenta.nombre);

        return binding.getRoot();
    }
}
