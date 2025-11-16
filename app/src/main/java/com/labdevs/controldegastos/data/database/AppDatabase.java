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
        version = 2
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

                                        CuentaDAO cuentasDAO = database.CuentaDAO();
                                        cuentasDAO.insertOrUpdate(new Cuenta("Juan","Efectivo",2250000.0));
                                        cuentasDAO.insertOrUpdate(new Cuenta("Maria","Tarjeta de Credito",1100000.0));
                                        cuentasDAO.insertOrUpdate(new Cuenta("Jose","Tarjeta de Credito",2001.0));
                                        cuentasDAO.insertOrUpdate(new Cuenta("test","Tarjeta de Credito",1000.0));

                                        // categorias por defecto
                                        catDAO.insertar(new Categoria("Comida", "cat_ico_comida", true));
                                        catDAO.insertar(new Categoria("Entretenimiento", "cat_ico_entretenimiento", true));
                                        catDAO.insertar(new Categoria("Salario", "cat_ico_salario", true));
                                        catDAO.insertar(new Categoria("Shopping", "cat_ico_shopping", true));
                                        catDAO.insertar(new Categoria("Transporte", "cat_ico_transporte", true));
                                        catDAO.insertar(new Categoria("Viaje", "cat_ico_viajes", true));

                                        TransaccionDAO transaccionesDAO = database.TransaccionDAO();
                                        transaccionesDAO.insertOrUpdate(new Transaccion(194107.0,Converters.toDate("2025-09-15 07:12:31"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(144158.0,Converters.toDate("2025-09-15 18:42:07"),"NULL","Gasto",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(139968.0,Converters.toDate("2025-07-10 12:15:56"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(39344.0,Converters.toDate("2025-07-11 23:59:34"),"NULL","Gasto",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(160825.0,Converters.toDate("2025-07-19 06:01:48"),"NULL","Ingreso",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(8347.0,Converters.toDate("2025-08-04 11:36:52"),"NULL","Ingreso",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(81363.0,Converters.toDate("2025-09-05 07:25:09"),"NULL","Gasto",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(145180.0,Converters.toDate("2025-09-13 22:47:27"),"NULL","Ingreso",4,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(29078.0,Converters.toDate("2025-08-13 15:59:03"),"NULL","Gasto",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(188322.0,Converters.toDate("2025-07-17 07:14:59"),"NULL","Gasto",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(189556.0,Converters.toDate("2025-09-12 08:05:11"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(52446.0,Converters.toDate("2025-09-29 16:18:30"),"NULL","Gasto",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(169319.0,Converters.toDate("2025-09-10 13:11:17"),"NULL","Ingreso",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(118202.0,Converters.toDate("2025-07-03 09:50:29"),"NULL","Gasto",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(36978.0,Converters.toDate("2025-08-30 21:38:08"),"NULL","Ingreso",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(114629.0,Converters.toDate("2025-08-01 14:09:22"),"NULL","Ingreso",4,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(39749.0,Converters.toDate("2025-09-28 19:33:45"),"NULL","Gasto",4,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(75043.0,Converters.toDate("2025-07-25 00:11:09"),"NULL","Ingreso",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(68029.0,Converters.toDate("2025-09-15 17:29:55"),"NULL","Gasto",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(46251.0,Converters.toDate("2025-07-27 10:44:13"),"NULL","Ingreso",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(195677.0,Converters.toDate("2025-08-03 07:37:05"),"NULL","Gasto",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(198022.0,Converters.toDate("2025-07-06 07:23:12"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(38991.0,Converters.toDate("2025-09-14 07:01:17"),"NULL","Gasto",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(78387.0,Converters.toDate("2025-08-17 20:27:56"),"NULL","Ingreso",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(187148.0,Converters.toDate("2025-07-18 12:38:22"),"NULL","Gasto",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(118286.0,Converters.toDate("2025-09-11 06:48:05"),"NULL","Ingreso",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(180058.0,Converters.toDate("2025-07-19 09:28:59"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(191084.0,Converters.toDate("2025-08-07 14:54:11"),"NULL","Gasto",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(79066.0,Converters.toDate("2025-08-28 07:03:43"),"NULL","Gasto",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(23639.0,Converters.toDate("2025-07-25 11:21:40"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(111267.0,Converters.toDate("2025-09-15 17:10:36"),"NULL","Ingreso",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(83742.0,Converters.toDate("2025-08-24 08:12:55"),"NULL","Gasto",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(191895.0,Converters.toDate("2025-08-25 13:55:08"),"NULL","Ingreso",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(188580.0,Converters.toDate("2025-08-28 22:02:44"),"NULL","Gasto",4,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(63443.0,Converters.toDate("2025-09-29 10:17:13"),"NULL","Ingreso",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(185577.0,Converters.toDate("2025-08-27 16:47:39"),"NULL","Gasto",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(138386.0,Converters.toDate("2025-09-17 07:59:21"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(93646.0,Converters.toDate("2025-07-10 23:12:05"),"NULL","Gasto",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(104984.0,Converters.toDate("2025-09-04 20:31:59"),"NULL","Ingreso",2,2,2));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(50382.0,Converters.toDate("2025-08-05 15:08:40"),"NULL","Gasto",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(19338.0,Converters.toDate("2025-09-04 07:34:57"),"NULL","Ingreso",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(37851.0,Converters.toDate("2025-08-12 18:26:33"),"NULL","Ingreso",5,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(48394.0,Converters.toDate("2025-08-11 07:52:28"),"NULL","Gasto",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(85146.0,Converters.toDate("2025-08-18 07:46:12"),"NULL","Ingreso",1,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(161789.0,Converters.toDate("2025-09-16 13:42:56"),"NULL","Gasto",4,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(38177.0,Converters.toDate("2025-09-17 19:04:41"),"NULL","Ingreso",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(103990.0,Converters.toDate("2025-07-25 06:39:19"),"NULL","Gasto",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(95708.0,Converters.toDate("2025-07-22 09:07:35"),"NULL","Ingreso",3,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(67685.0,Converters.toDate("2025-09-05 21:55:48"),"NULL","Gasto",4,1,1));
                                        transaccionesDAO.insertOrUpdate(new Transaccion(17990.0,Converters.toDate("2025-08-12 17:50:59"),"NULL","Ingreso",2,2,2));
                                    });
                                }
                            })
                            .allowMainThreadQueries().build();
                }
            }
        }
        //dummy select required for pre-populate the db
//        INSTANCIA.query("select 1", null);
        return INSTANCIA;
    }
}
