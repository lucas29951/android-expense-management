package com.labdevs.controldegastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.labdevs.controldegastos.data.model.ItemResume;

import java.util.Date;


public class ResumeAdapter extends ListAdapter<ItemResume, ResumeAdapter.ListResumeItemHolder> {


    private final MainActivity mainActivity;

    protected ResumeAdapter(MainActivity mainActivity) {
        super(DIFF_CALLBACK);
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ListResumeItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainActivity).inflate(R.layout.list_item_resume, parent, false);
        return new ListResumeItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListResumeItemHolder holder, int position) {
        ItemResume item = getItem(position);
        Context context = holder.itemView.getContext();
        int idIcono = context.getResources().getIdentifier(item.icono, "drawable", context.getPackageName());
        holder.ivIcon.setImageResource(idIcono);
        holder.tvTitleTransaction.setText(item.tipo_transaccion);
        holder.tvCategoryTransaction.setText(item.nombre);
        holder.tvAmountTransaction.setText(String.format(mainActivity.getString(R.string.total_amount_format),item.monto));
    }

    public static class ListResumeItemHolder extends RecyclerView.ViewHolder{

        ImageView ivIcon;
        TextView tvTitleTransaction;
        TextView tvCategoryTransaction;
        TextView tvAmountTransaction;

        public ListResumeItemHolder(@NonNull View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitleTransaction = itemView.findViewById(R.id.tv_title_transaction);
            tvCategoryTransaction = itemView.findViewById(R.id.tv_category_transaction);
            tvAmountTransaction = itemView.findViewById(R.id.tv_amount_transaction);

        }
    }

    private static final DiffUtil.ItemCallback<ItemResume> DIFF_CALLBACK = new DiffUtil.ItemCallback<ItemResume>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemResume oldItem, @NonNull ItemResume newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemResume oldItem, @NonNull ItemResume newItem) {
            return oldItem.tipo_transaccion.equals(newItem.tipo_transaccion)
                    && oldItem.nombre.equals(newItem.nombre)
                    && compareDate(oldItem,newItem)
                    && oldItem.monto == newItem.monto;
        }

        private boolean compareDate(ItemResume oldItem, ItemResume newItem) {
            Date oldItemFecha = oldItem.fecha_hora;
            Date newItemFecha = newItem.fecha_hora;
            return oldItemFecha.equals(newItemFecha);
        }
    };

}
