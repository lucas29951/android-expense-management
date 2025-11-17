package com.labdevs.controldegastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.labdevs.controldegastos.data.entity.Cuenta;

import java.util.List;

public class CuentaAdapter extends RecyclerView.Adapter<CuentaAdapter.ListAccountaHolder> {

    private final MainActivity mainActivity;

    private List<Cuenta> cuentas;
    public static final String saldoFormat = "%1$.0f";
    private AppViewModel viewModel;
    private boolean isDeleteAccountIcon = false;

    public CuentaAdapter(MainActivity mainActivity, List<Cuenta> cuentas, AppViewModel viewModel) {
        this.mainActivity = mainActivity;
        this.cuentas = cuentas;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ListAccountaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainActivity).inflate(R.layout.list_item_account, parent, false);
        return new ListAccountaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAccountaHolder holder, int position) {
        Cuenta cuenta = cuentas.get(position);
        setAccountIcon(cuenta.tipo, holder);
        holder.tvAccountName.setText(cuenta.nombre);
        holder.getTvAccountBalance.setText(String.format(saldoFormat, cuenta.saldo));

        if (!isDeleteAccountIcon) {
            holder.modifyDeleteAccountIcon.setImageResource(R.drawable.edit_24px);
            holder.modifyDeleteAccountIcon.setOnClickListener(view -> {
                viewModel.hasExecutedOnce(false);
                viewModel.setCuentaSelecionada(cuenta);
                viewModel.setModificarCuenta(true);
            });
        } else {
            holder.modifyDeleteAccountIcon.setImageResource(R.drawable.delete_24px);
            holder.modifyDeleteAccountIcon.setOnClickListener(view -> {
                viewModel.hasExecutedOnce(false);
                viewModel.setCuentaSelecionadaEliminar(cuenta);
            });
        }
    }

    public void chageAccountIconFunction() {
        isDeleteAccountIcon = !isDeleteAccountIcon;
        notifyDataSetChanged();
    }

    private void setAccountIcon(String tipo, ListAccountaHolder holder) {
        String lowerCase = tipo.toLowerCase();
        if (lowerCase.contains("efectivo")) {
            holder.accountIcon.setImageResource(R.drawable.attach_money_24px);
        } else if (lowerCase.contains("tarjeta de credito")) {
            holder.accountIcon.setImageResource(R.drawable.credit_card_24px);
        } else if (lowerCase.contains("tarjeta de debito")) {
            holder.accountIcon.setImageResource(R.drawable.payments_24px);
        } else {
            holder.accountIcon.setImageResource(R.drawable.cat_ico_ahorros);
        }
    }

    @Override
    public int getItemCount() {
        return cuentas != null ? cuentas.size() : 0;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
        notifyDataSetChanged();
    }

    public class ListAccountaHolder extends RecyclerView.ViewHolder {

        ImageView accountIcon;
        TextView tvAccountName;
        TextView getTvAccountBalance;
        ImageView modifyDeleteAccountIcon;

        public ListAccountaHolder(@NonNull View itemView) {
            super(itemView);

            accountIcon = itemView.findViewById(R.id.account_icon);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            getTvAccountBalance = itemView.findViewById(R.id.tv_account_balance);
            modifyDeleteAccountIcon = itemView.findViewById(R.id.modify_delete_account_icon);

        }
    }


}
