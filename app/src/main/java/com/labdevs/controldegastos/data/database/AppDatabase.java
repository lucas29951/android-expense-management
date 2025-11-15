package com.labdevs.controldegastos.data.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.labdevs.controldegastos.data.entity.Categoria;
import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.data.entity.GastoRecurrente;
import com.labdevs.controldegastos.data.entity.Transaccion;

import com.labdevs.controldegastos.data.dao.CategoriaDAO;
import com.labdevs.controldegastos.data.dao.CuentaDAO;
import com.labdevs.controldegastos.data.dao.GastoRecurrenteDAO;
import com.labdevs.controldegastos.data.dao.TransaccionDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                Categoria.class,
                Cuenta.class,
                GastoRecurrente.class,
                Transaccion.class
        },
        version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoriaDAO CategoriaDAO();
    public abstract CuentaDAO CuentaDAO();
    public abstract GastoRecurrenteDAO GastoRecurrenteDAO();
    public abstract TransaccionDAO TransaccionDAO();
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // --- Singleton ---
    private static volatile AppDatabase INSTANCIA;

    public static AppDatabase obtenerInstancia(Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "control_gastos.db")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        AppDatabase database = obtenerInstancia(context);
                                        CategoriaDAO catDAO = database.CategoriaDAO();

                                        // categorias por defecto
                                        catDAO.insertar(new Categoria("Comida", "cat_ico_comida", true));
                                        catDAO.insertar(new Categoria("Entretenimiento", "cat_ico_entretenimiento", true));
                                        catDAO.insertar(new Categoria("Salario", "cat_ico_salario", true));
                                        catDAO.insertar(new Categoria("Shopping", "cat_ico_shopping", true));
                                        catDAO.insertar(new Categoria("Transporte", "cat_ico_transporte", true));
                                        catDAO.insertar(new Categoria("Viaje", "cat_ico_viajes", true));
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        //dummy select required for pre-populate the db
        INSTANCIA.query("select 1", null);
        return INSTANCIA;
    }
}
