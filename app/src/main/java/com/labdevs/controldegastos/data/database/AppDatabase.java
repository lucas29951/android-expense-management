package com.labdevs.controldegastos.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import android.content.res.AssetManager;

import com.labdevs.controldegastos.data.entity.Categoria;
import com.labdevs.controldegastos.data.entity.Cuenta;
import com.labdevs.controldegastos.data.entity.GastoRecurrente;
import com.labdevs.controldegastos.data.entity.Transaccion;

import com.labdevs.controldegastos.data.dao.CategoriaDAO;
import com.labdevs.controldegastos.data.dao.CuentaDAO;
import com.labdevs.controldegastos.data.dao.GastoRecurrenteDAO;
import com.labdevs.controldegastos.data.dao.TransaccionDAO;

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
                    INSTANCIA = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "control_gastos.db"
                            ).allowMainThreadQueries().createFromAsset("app.db")
                            .build();
                }
            }
        }
        //dummy select required for pre-populate the db
        INSTANCIA.query("select 1", null);
        return INSTANCIA;
    }
}
