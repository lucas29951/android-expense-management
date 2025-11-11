package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labdevs.controldegastos.databinding.FragmentResumeBinding;

public class ResumeFragment extends Fragment {

    private FragmentResumeBinding binding;
    private AppViewModel viewModel;
    private ResumeAdapter resumeAdapter;
    private String tipoTrans;
    private final String[] tiposTransaccion = {"ingreso","gasto"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        tipoTrans = "ingreso";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResumeBinding.inflate(inflater,container,false);

        viewModel.setAppBarTitle(getString(R.string.resume_title));

        binding.segmentedButtonsResume.setOnSelectedOptionChangeCallback(index -> {
            viewModel.listarResumeItems(getIndex(index)).observe(getViewLifecycleOwner(), items -> resumeAdapter.submitList(items));
            setTitleList(index);
            return null;
        });

        binding.tvCardTotalAmount.setText("$ 0");
        viewModel.listarResumeItems(tipoTrans).observe(getViewLifecycleOwner(), items -> resumeAdapter.submitList(items));

        setupRecycleView();

        return binding.getRoot();
    }

    private void setTitleList(Integer index) {
        if (index==0){
            setTitleList(getActivity().getString(R.string.income_section_title));
        } else {
            setTitleList(getActivity().getString(R.string.expenses_section_title));
        }
    }

    private void setTitleList(String text) {
        binding.tvRecent.setText(text);
    }

    private String getIndex(Integer index) {
        if(index==0)
            return tiposTransaccion[index++];
        else
            return tiposTransaccion[index--];
    }


    private void setupRecycleView() {
        resumeAdapter = new ResumeAdapter((MainActivity) getActivity());
        binding.rvExpensesList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvExpensesList.setAdapter(resumeAdapter);
    }
}