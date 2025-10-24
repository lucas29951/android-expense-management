package com.labdevs.controldegastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomLegendAdapter extends RecyclerView.Adapter<CustomLegendAdapter.ListLegendHolder> {

    private final MainActivity mainActivity;
    private List<ItemLegend> legends;

    public CustomLegendAdapter(MainActivity mainActivity, List<ItemLegend> legends) {
        this.mainActivity = mainActivity;
        this.legends = legends;
    }

    @NonNull
    @Override
    public ListLegendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainActivity).inflate(R.layout.custom_legend_item, parent, false);
        return new ListLegendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListLegendHolder holder, int position) {
        ItemLegend itemLegend = legends.get(position);
        holder.legendColor.setColorFilter(itemLegend.getColor());
        holder.legendLabel.setText(itemLegend.label);
        holder.legendValue.setText(itemLegend.value);
        holder.legendPercentage.setText(itemLegend.percentage);
    }

    @Override
    public int getItemCount() {
        return legends.size();
    }

    public void setLegends(List<ItemLegend> legends) {
        this.legends = legends;
    }

    public class ListLegendHolder extends RecyclerView.ViewHolder {

        ImageView legendColor;
        TextView legendLabel;
        TextView legendValue;
        TextView legendPercentage;


        public ListLegendHolder(@NonNull View itemView) {
            super(itemView);

            legendColor = itemView.findViewById(R.id.legendColor);
            legendLabel = itemView.findViewById(R.id.legendLabel);
            legendValue = itemView.findViewById(R.id.legendValue);
            legendPercentage = itemView.findViewById(R.id.legendPercentage);

        }
    }


    public static class ItemLegend{

        private int color;
        private String label;
        private String value;
        private String percentage;

        public ItemLegend(int color, String label, String value, String percentage) {
            this.color = color;
            this.label = label;
            this.value = value;
            this.percentage = percentage;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }
    }



}
