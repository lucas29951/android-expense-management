package com.labdevs.controldegastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InformeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informe, container, false);
        Button trailingButton = view.findViewById(R.id.trailingButton);
        Button mainButton = view.findViewById(R.id.mainButton);
        trailingButton.setOnClickListener(v->showMenu(v,R.menu.menu_boton_informe));
        mainButton.setOnClickListener(v->showMenu(v,R.menu.menu_boton_informe));
        return view;
    }

    private void showMenu(View v, int menuRes) {
        PopupMenu popup = new PopupMenu(getActivity(),v);
        popup.getMenuInflater().inflate(menuRes,popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_informe_opcion_1) {
                Toast.makeText(getContext(), "Opcion 1", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_informe_opcion_2) {
                Toast.makeText(getContext(), "Opcion 2", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_informe_opcion_3) {
                Toast.makeText(getContext(), "Opcion 3", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_informe_opcion_4) {
                Toast.makeText(getContext(), "Opcion 4", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        popup.show();


    }

}