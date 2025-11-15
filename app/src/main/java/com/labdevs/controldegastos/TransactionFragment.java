package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.labdevs.controldegastos.adapters.CategoriaSpinnerAdapter;
import com.labdevs.controldegastos.adapters.CuentaSpinnerAdapter;
import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.databinding.FragmentTransactionBinding;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class TransactionFragment extends Fragment {

    private @NonNull FragmentTransactionBinding binding;
    private AppViewModel viewModel;
    private CategoriaSpinnerAdapter categoriasAdapter;
    private ArrayAdapter<CharSequence> tipoTransaccionesAdapter;
    private CuentaSpinnerAdapter cuentaOrigenAdapter;
    private CuentaSpinnerAdapter cuentaDestinoAdapter;
    private List<Cuenta> listaCuentas;
    public final String EDITTEXT_DATE_FORMAT = "dd/MM/yyyy";
    public final String COMPLETE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final String DATE_FORMAT = "yyyy-MM-dd";
    public final String TIME_FORMAT = "HH:mm:ss";
    private LocalDateTime defaultDate;
    private LocalDate localDate;
    private LocalTime localTime;
    public final String EDITTEXT_TIME_FORMAT = "HH:mm";
    private String editTextDate;
    private String editTextTime;
    private TransaccionWrapper transaccion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        categoriasAdapter = new CategoriaSpinnerAdapter(requireContext(), viewModel.listarCategorias());

        initializedTransactionContainer();

        setupCategoriaSpinnerAdapter();

        listaCuentas = viewModel.getListaCuentas();
        Map<String, List<Cuenta>> mapCuentasNombresDuplicados = listaCuentas.stream().collect(Collectors.groupingBy(cuenta -> cuenta.nombre));
        // dame un map que tenga cuentas (List<Cuenta>>) duplicadas (> 1)
        mapCuentasNombresDuplicados = mapCuentasNombresDuplicados.entrySet().stream().filter(entry -> entry.getValue().size() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        setupCuentaOrigenSpinnerAdapter(listaCuentas, mapCuentasNombresDuplicados);
        setupCuentaDestinoSpinnerAdapter(listaCuentas, mapCuentasNombresDuplicados);

    }

    private void initializedTransactionContainer() {
        transaccion = new TransaccionWrapper();
    }

    private void initializeDefaultTransactionDate() {
        // TODO: asignar a date de cuenta
        defaultDate = LocalDateTime.now();
        editTextDate = defaultDate.format(DateTimeFormatter.ofPattern(EDITTEXT_DATE_FORMAT));
        editTextTime = defaultDate.format(DateTimeFormatter.ofPattern(EDITTEXT_TIME_FORMAT));
    }

    private void setupCategoriaSpinnerAdapter() {
        tipoTransaccionesAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.transaction_type_array,
                android.R.layout.simple_spinner_item);
        tipoTransaccionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setupCuentaDestinoSpinnerAdapter(List<Cuenta> listaCuentas, Map<String, List<Cuenta>> mapCuentasNombresDuplicados) {
        cuentaDestinoAdapter = new CuentaSpinnerAdapter(requireContext(), listaCuentas, mapCuentasNombresDuplicados);
    }

    private void setupCuentaOrigenSpinnerAdapter(List<Cuenta> listaCuentas, Map<String, List<Cuenta>> mapCuentasNombresDuplicados) {
        cuentaOrigenAdapter = new CuentaSpinnerAdapter(requireContext(), listaCuentas, mapCuentasNombresDuplicados);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater, container, false);

        viewModel.setAppBarTitle(getString(R.string.transaction_title));
        viewModel.setAppBarNavIcon(true);

        initializeDefaultTransactionDate();

        // fecha por default
        binding.dateEditText.setText(editTextDate);
        binding.timeEditText.setText(editTextTime);

        binding.dateEditText.setOnClickListener(view -> loadDatePicker());
        binding.timeEditText.setOnClickListener(view -> loadTimePicker());

        binding.spCategory.setAdapter(categoriasAdapter);
        binding.spTransactionType.setAdapter(tipoTransaccionesAdapter);
        binding.spOriginAccount.setAdapter(cuentaOrigenAdapter);
        binding.spDestinationAccount.setAdapter(cuentaDestinoAdapter);

        setupSpinnersListeners();

        binding.btnSaveTransaction.setOnClickListener(view -> attempTransactionRegistration());

        return binding.getRoot();
    }

    private void attempTransactionRegistration() {
        transaccion.monto = binding.etAmount.getText().toString().trim();
        String descripcion = binding.etDescription.getText().toString().trim();
        transaccion.comentario = descripcion.isEmpty() ? "null" : descripcion;
        transaccion.fecha_hora = getFechaHora();
        Log.d("devtest",transaccion.toString());
        initializedTransactionContainer();
    }

    private String getFechaHora() {
        if (localDate==null && localTime==null) {
            return defaultDate.format(DateTimeFormatter.ofPattern(COMPLETE_DATE_FORMAT));
        } else if (localDate==null) {
            return defaultDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + " " + localTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        }
        return localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + " " + defaultDate.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    private void loadTimePicker() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK).build();
        timePicker.addOnPositiveButtonClickListener(view -> {
            // TODO: asignar a date de cuenta
            localTime = LocalTime.of(timePicker.getHour(), timePicker.getMinute());
            binding.timeEditText.setText(localTime.format(DateTimeFormatter.ofPattern(EDITTEXT_TIME_FORMAT)));
        });
        timePicker.show(getParentFragmentManager(), "TimePickerTransaction");
    }

    private void loadDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // TODO: asignar a date de cuenta
            localDate = Instant.ofEpochMilli(selection).atZone(TimeZone.getTimeZone("UTC").toZoneId()).toLocalDate();
            String date = localDate.format(DateTimeFormatter.ofPattern(EDITTEXT_DATE_FORMAT));
            binding.dateEditText.setText(date);
        });
        datePicker.show(getParentFragmentManager(), "DatePickerTransaction");
    }

    public enum tipoTransaccion {
        GASTO, INGRESO, TRANSFERENCIA;

        public static String getString(int ordinal) {
            return switch (ordinal) {
                case 0 -> "Gasto";
                case 1 -> "Ingreso";
                case 2 -> "Transferencia";
                default -> "";
            };
        }
    }

    private void setupSpinnersListeners() {
        binding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transaccion.id_categoria = (int) id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transaccion.tipo_transaccion = tipoTransaccion.getString((int)id);
                if (position == tipoTransaccion.TRANSFERENCIA.ordinal()) {
                    binding.spDestinationAccount.setVisibility(View.VISIBLE);
                } else {
                    if (binding.spDestinationAccount.getVisibility() == View.VISIBLE) {
                        binding.spDestinationAccount.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        binding.spOriginAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transaccion.id_cuenta_origen = (int) id;
                cuentaDestinoAdapter.updateList(listaCuentas.stream().filter(cuenta -> cuenta.id != id).toList());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.spDestinationAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transaccion.id_cuenta_destino = (int) id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public static class TransaccionWrapper {
        public int id;
        public String monto;
        public String fecha_hora;
        public String comentario;
        public String tipo_transaccion;
        public int id_categoria;
        public int id_cuenta_origen;
        public int id_cuenta_destino;

        public TransaccionWrapper() {
        }

        @Override
        public String toString() {
            return "TransaccionWrapper{" +
                    "id=" + id +
                    ", monto='" + monto + '\'' +
                    ", fecha_hora='" + fecha_hora + '\'' +
                    ", comentario='" + comentario + '\'' +
                    ", tipo_transaccion='" + tipo_transaccion + '\'' +
                    ", id_categoria=" + id_categoria +
                    ", id_cuenta_origen=" + id_cuenta_origen +
                    ", id_cuenta_destino=" + id_cuenta_destino +
                    '}';
        }
    }

}