package com.labdevs.controldegastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.labdevs.controldegastos.data.entity.Transaccion;

import java.util.ArrayList;

public class TransaccionAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Transaccion> transacciones;

    public TransaccionAdapter(Context context, int layout, ArrayList<Transaccion> transacciones) {
        this.context = context;
        this.layout = layout;
        this.transacciones = transacciones;
    }

    @Override
    public int getCount() {
        return this.transacciones.size();
    }

    @Override
    public Object getItem(int position) {
        return this.transacciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = LayoutInflater.from(this.context);

        view = inflater.inflate(R.layout.list_item_resume, null);

        Transaccion actual = transacciones.get(position);

        ImageView icono = (ImageView) view.findViewById(R.id.iv_icon);
        TextView titulo = (TextView) view.findViewById(R.id.tv_title_transaction);
        TextView categoria = (TextView) view.findViewById(R.id.tv_category_transaction);
        TextView monto = (TextView) view.findViewById(R.id.tv_amount_transaction);

        titulo.setText(actual.getComentario());
        categoria.setText(actual.getId_categoria());
        monto.setText((int) actual.getMonto());

        return view;
    }
}
