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

    // --- Singleton ---
    private static volatile AppDatabase INSTANCIA;

    public static AppDatabase obtenerInstancia(Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "control_gastos.db")
                            .addCallback(cargaInicial)
                            .build();
                }
            }
        }
        return INSTANCIA;
    }

    private static final RoomDatabase.Callback cargaInicial =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        CategoriaDAO catDAO = INSTANCIA.CategoriaDAO();
                        catDAO.insertar(new Categoria("Comida", "cat_ico_comida", true));
                        catDAO.insertar(new Categoria("Transporte", "cat_ico_transporte", true));
                        catDAO.insertar(new Categoria("Shopping", "cat_ico_shopping", true));
                        catDAO.insertar(new Categoria("Entretenimiento", "cat_ico_entretenimiento", true));
                        catDAO.insertar(new Categoria("Salario", "cat_ico_salario", true));
                        catDAO.insertar(new Categoria("Freelance", "cat_ico_freelance", true));
                    });
                }
            };
}
