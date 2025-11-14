package com.labdevs.controldegastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.databinding.ListSpinnerItemCuentaBinding;

import java.util.List;
import java.util.Map;

public class CuentaSpinnerAdapter extends BaseAdapter {

    private List<Cuenta> listaCuentas;
    private final Map<String, List<Cuenta>> mapCuentasNombresDuplicados;
    private LayoutInflater lInflater;

    public CuentaSpinnerAdapter(Context context, List<Cuenta> listaCuentas, Map<String, List<Cuenta>> mapCuentasNombresDuplicados) {
        lInflater = LayoutInflater.from(context);
        this.listaCuentas = listaCuentas;
        this.mapCuentasNombresDuplicados = mapCuentasNombresDuplicados;
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

        binding.cuentaSpinnerText1.setText(cuenta.nombre);

        // si el nombre es duplicado se indica el tipo para poder diferenciarlos
        if (mapCuentasNombresDuplicados.get(cuenta.nombre)!=null) {
            binding.cuentaSpinnerText2.setText(cuenta.tipo);
        }

        return binding.getRoot();
    }

    public void updateList(List<Cuenta> listaCuentas){
        this.listaCuentas = listaCuentas;
        notifyDataSetChanged();
    }

}
