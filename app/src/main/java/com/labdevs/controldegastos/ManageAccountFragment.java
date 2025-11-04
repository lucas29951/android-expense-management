package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return binding.getRoot();
    }
}