package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labdevs.controldegastos.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {

    private @NonNull FragmentTransactionBinding binding;
    private AppViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater,container,false);


        viewModel.setAppBarTitle(getString(R.string.transaction_title));
        viewModel.setAppBarNavIcon(true);


        return binding.getRoot();
    }
}