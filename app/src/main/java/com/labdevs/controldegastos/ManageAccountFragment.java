package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.labdevs.controldegastos.databinding.FragmentManageAccountBinding;

public class ManageAccountFragment extends Fragment {

    private FragmentManageAccountBinding binding;
    private AppViewModel viewModel;

    public ManageAccountFragment(AppViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageAccountBinding.inflate(inflater,container,false);

        viewModel.setAppBarTitle(getString(R.string.manege_title_1));
        viewModel.setAppBarNavIcon(true);

        binding.selectType.setOnClickListener(imageView -> showMenu(imageView));

        return binding.getRoot();
    }

    private void showMenu(View button) {
        PopupMenu popup = new PopupMenu(getActivity(),button);
        popup.getMenuInflater().inflate(R.menu.menu_boton_tipo_cuenta,popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.menu_tipo_cuenta_op_1){
                changeTypeLabelText(getString(R.string.menu_tipo_cuenta_op_texto_1));
                return true;
            } else if (itemId == R.id.menu_tipo_cuenta_op_2){
                changeTypeLabelText(getString(R.string.menu_tipo_cuenta_op_texto_2));
                return true;
            } else if(itemId == R.id.menu_tipo_cuenta_op_3){
                changeTypeLabelText(getString(R.string.menu_tipo_cuenta_op_texto_3));
                return true;
            } else if (itemId == R.id.menu_tipo_cuenta_op_4){
                changeTypeLabelText(getString(R.string.menu_tipo_cuenta_op_texto_4));
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void changeTypeLabelText(String text) {
        binding.tvSelectType.setText(text);
    }
}