package global.factura.qrpos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.channels.AlreadyBoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alejandro on 3/12/2018.  Alex Gandara
 */


public class connectionDB extends SQLiteOpenHelper {




    // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA cierre de caja  "cierre_caja"

    public static final String CIERRE_TABLE_ID = "_id";
    public static final String CIERRE_SERIE = "cierre_serie";
    public static final String CIERRE_DEL_FOLIO = "cierre_del_folio";
    public static final String CIERRE_AL_FOLIO = "cierre_al_folio";
    public static final String CIERRE_FECHA = "cierre_fecha";
    public static final String CIERRE_GRAVADO = "cierre_gravado";
    public static final String CIERRE_EXCENTO = "cierre_excento";
    public static final String CIERRE_INAFECTO = "cierre_inafecto";
    public static final String CIERRE_SUBTOTAL = "cierre_subtotal";
    public static final String CIERRE_IGV = "cierre_igv";
    public static final String CIERRE_TOTAL = "cierre_total";







    // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA historial de sincronizacion "sycro"


    public static final String SYNCRO_FECHA = "syncro_fecha";
    public static final String SYNCRO_DOCTOS = "syncro_doctos";
    public static final String SYNCRO_GRAVADO = "syncro_gravado";
    public static final String SYNCRO_EXCENTO = "syncro_excento";
    public static final String SYNCRO_INAFECTO = "syncro_inafecto";
    public static final String SYNCRO_SUBTOTAL = "syncro_subtotal";
    public static final String SYNCRO_IGV = "syncro_igv";
    public static final String SYNCRO_TOTAL = "syncro_total";
    public static final String SYNCRO_ARCHIVADA = "syncro_archivada";
    public static final String SYNCRO_ENVIADO_NUBE = "syncro_enviado_nube";
    public static final String SYNCRO_REVISADA = "syncro_revisada";





    // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA CABECERA DE FACTURA  "cabecera"

    public static final String TABLE_ID = "_id";
    public static final String ID_VELNEO = "_id_velneo";
    public static final String SERIE = "serie";
    public static final String FOLIO = "folio";
    public static final String FECHA = "fecha";
    public static final String RUC = "ruc";
    public static final String RAZON_SOCIAL = "razon_social";
    public static final String DIRECCION = "direccion";
    public static final String MONEDA = "moneda";
    public static final String GRAVADO = "gravado";
    public static final String EXCENTO = "excento";
    public static final String INAFECTO = "inafecto";
    public static final String SUBTOTAL = "subtotal";
    public static final String IGV = "igv";
    public static final String TOTAL = "total";
    public static final String CORREO = "correo";
    public static final String SERIE_REL = "serie_rel";
    public static final String FOLIO_REL = "folio_rel";
    public static final String ARCHIVADA = "archivada";
    public static final String ENVIADO_CORREO = "enviado_correo";
    public static final String ENVIADO_NUBE = "enviado_nube";






    // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA DETALLE DE FACTURA  "detalle"

    public static final String CABECERA_ID = "cabecera_id";
    public static final String LINEA = "linea";
    public static final String PRODUCTO = "producto";
    public static final String DESCRIPCION = "descripcion";
    public static final String UNIDAD = "unidad";
    public static final String PRECIO = "precio";
    public static final String CANTIDAD = "cantidad";
    public static final String DET_SUBTOTAL = "det_subtotal";
    public static final String DET_IGV = "det_igv";
    public static final String DET_TOTAL = "det_total";
    public static final String COLOR = "color";
    public static final String TALLA = "talla";

    // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA COLORES  "colores"

    public static final String COLOR_ID = "color_id";
    public static final String NOMBRE_COLOR = "color";



    // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA TALLAS  "tallas"

    public static final String TALLA_ID = "talla_id";
    public static final String NOMBRE_TALLA = "talla";


    // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA EMPRESAS SERA UN SOLO REGISTRO DONDE IRA LA INFORMACION DE LA COMPAÑIA   "empresa"

    public static final String RAZON_SOCIAL_EMPRESA = "razon_social_empresa";
    public static final String RUC_EMPRESA = "ruc_empresa";
    public static final String DIRECCION_EMPRESA_1 = "direccion_empresa_1";
    public static final String DIRECCION_EMPRESA_2 = "direccion_empresa_2";
    public static final String CORREO_EMPRESA = "correo_empresa";
    public static final String TELEFONO_EMPRESA = "telefono_empresa";
    public static final String PORCENTAJE_IGV = "porcentaje_igv";
    public static final String LICENCIA = "licencia";
    public static final String LINEA01 = "linea01";
    public static final String LINEA02 = "linea02";
    public static final String LINEA03 = "linea03";
    public static final String LINEA04 = "linea04";
    public static final String LINEA05 = "linea05";
    public static final String LINEA06 = "linea06";
    public static final String LINEA07 = "linea07";
    public static final String LINEA08 = "linea08";
    public static final String LINEA09 = "linea09";

    public static final String BASE01 = "base01";
    public static final String BASE02 = "base02";
    public static final String BASE03 = "base03";
    public static final String BASE04 = "base04";
    public static final String BASE05 = "base05";
    public static final String BASE06 = "base06";
    public static final String API_KEY_DOCTOS = "api_key_doctos";
    public static final String API_KEY_MOVTOS = "api_key_movtos";
    public static final String API_KEY_PRODUCTOS = "api_key_productos";
    public static final String API_KEY_CLIENTES = "api_key_clientes";
    public static final String API_KEY_ALMACEN = "api_key_almacen";
    public static final String API_KEY_MOVIMIENTOS = "api_key_movimientos";
    public static final String API_KEY_00 = "api_key_00";
    public static final String API_KEY_01 = "api_key_01";
    public static final String API_KEY_02 = "api_key_02";
    public static final String API_KEY_03 = "api_key_03";
    public static final String API_KEY_04 = "api_key_04";
    public static final String API_KEY_05 = "api_key_05";
    public static final String API_KEY_06 = "api_key_06";
    public static final String API_KEY_07 = "api_key_07";
    public static final String API_KEY_08 = "api_key_08";
    public static final String API_KEY_09 = "api_key_09";
    public static final String API_KEY_10 = "api_key_10";
    public static final String IMPRESORA = "tipo_impresora";
    public static final String IMPUESTO_IBCPER = "impuesto_icbper";
    public static final String ALMACEN = "almacen";
    public static final String SALDO_INICIAL_CAJA = "saldo_inicial_caja";
    public static final String VENTAS = "ventas";
    public static final String SALIDAS = "salidas";
    public static final String SALDO_FINAL_CAJA = "saldo_final_caja";

    public static final String LOGO = "logo";

    // variables para table clientes
    public static final String RAZON_SOCIAL_CLIENTE = "razon_social_cliente";
    public static final String RUC_CLIENTE = "ruc_cliente";
    public static final String DIRECCION_CLIENTE = "direccion_cliente";
    public static final String CORREO_CLIENTE = "correo_cliente";
    public static final String TELEFONO_CLIENTE = "telefono_cliente";
    public static final String TIPO_INDENTIDAD = "tipo_identidad";


    // variables para table unidades de medida
    public static final String ID_UNIDAD = "unidad";
    public static final String DESCRIPCION_UNIDAD = "descripcion_unidad";


    // variables para table productos
    public static final String ID_PRODUCTO = "producto";
    public static final String DESCRIPCION_PRODUCTO = "descripcion_producto";
    public static final String UNIDAD_PRODUCTO = "unidad";
    public static final String PRECIO_PRODUCTO = "precio";
    public static final String PRECIO_PRODUCTO_MAYOREO = "precio_mayoreo";
    public static final String IGV_PRODUCTO = "igv";
    public static final String EXISTENCIA = "existencia";
    public static final String VENTAS_PRO = "ventas_pro";
    public static final String SALDO_PRO = "saldo_pro";
   // public static final String ID_VELNEO = "_id_velneo";



    /*

    EXS
    PDT_REC
    PDT_SRV
    EXS_DIS
    EXS_TEO

     */


    // variables para table movimientos
    public static final String FECHA_MOV = "fecha";
    public static final String HORA_MOV = "hora";
    public static final String PRODUCTO_MOV = "producto";
    public static final String PRECIO_PRODUCTO_MOV = "precio";
    public static final String COSTO_PRODUCTO_MOV = "costo";
    public static final String TIPO_MOV = "tipo_mov";
    public static final String SALDO = "saldo";





    private static final String DATABASE = "miniPOS8";
    private static final String TABLE = "cabecera";

    // tablas
    private static final String TABLE_DET = "detalle";
    private static final String TABLE_EMPRESA = "empresa";
    private static final String TABLE_CLIENTES = "clientes";
    private static final String TABLE_UNIDADES = "unidades";
    private static final String TABLE_PRODUCTOS = "productos";
    private static final String TABLE_SYNCRO = "syncro";
    private static final String TABLE_CIERRE = "cierre";
    private static final String TABLE_MOVIMIENTOS = "movimientos";
    private static final String TABLE_COLORES = "colores";
    private static final String TABLE_TALLAS = "tallas";


    public connectionDB(Context context) {
        super(context, DATABASE, null, 1);
    }


    public connectionDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TABLE = "cabecera";

        db.execSQL("CREATE TABLE "+ TABLE + " (" +
                TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                SERIE +" TEXT, "+
                FOLIO +" INT DEFAULT 0,"+
                FECHA +" DATE,"+
                RUC +" TEXT,"+
                RAZON_SOCIAL +" TEXT,"+
                DIRECCION + " TEXT,"+
                MONEDA +" TEXT,"+
                CORREO +" TEXT DEFAULT 'usuario@dominio.com',"+
                SERIE_REL +" TEXT DEFAULT '',"+
                FOLIO_REL +" TEXT DEFAULT '',"+
                GRAVADO +" DOUBLE,"+
                EXCENTO +" DOUBLE,"+
                INAFECTO+" DOUBLE,"+
                SUBTOTAL+" DOUBLE,"+
                IGV+" DOUBLE,"+
                TOTAL+" DOUBLE,"+
                ENVIADO_CORREO+" INTEGER DEFAULT 0,"+
                ENVIADO_NUBE+" INTEGER DEFAULT 0,"+
                ID_VELNEO+" TEXT, "+
                ARCHIVADA+" INTEGER DEFAULT 0)"
        );





        // definicion de indice para controlar la historia

        ///  EJEMPLO db.execSQL("CREATE INDEX idx_contacts_email ON contacts (email)";

         /// CREATE INDEX po_parent ON purchaseorder(parent_po) WHERE parent_po IS NOT NULL;

        db.execSQL("CREATE INDEX "+
                "idx_cabecera_activos" +
                " ON "+TABLE+" (" +
                ARCHIVADA+
                ") "+
                " WHERE "+
                ARCHIVADA+" =0"
                );

        db.execSQL("CREATE INDEX "+
                "idx_cabecera_archivados" +
                " ON "+TABLE+" (" +
                ARCHIVADA+
                ") "+
                " WHERE "+
                ARCHIVADA+" =1"
                );






        // TABLE DETALLE
        db.execSQL("CREATE TABLE "+ TABLE_DET + " (" +
                TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CABECERA_ID +" INTEGER,"+
                LINEA +" INTEGER,"+
                PRODUCTO +" TEXT,"+
                DESCRIPCION +" TEXT,"+
                COLOR +" TEXT,"+
                TALLA  + " TEXT DEFAULT 'ST',"+
                UNIDAD + " TEXT DEFAULT 'NIU',"+
                PRECIO +" DOUBLE,"+
                CANTIDAD +" DOUBLE,"+
                DET_SUBTOTAL+" DOUBLE,"+
                DET_IGV+" DOUBLE,"+
                DET_TOTAL+" DOUBLE,"+
                " FOREIGN KEY("+ CABECERA_ID + ") REFERENCES "+TABLE+"("+TABLE_ID+"))");


        db.execSQL("CREATE TABLE series (id INTEGER PRIMARY KEY AUTOINCREMENT, serie TEXT, naturaleza TEXT, folio INTEGER)");

        db.beginTransaction();
        try {
            ContentValues valuesSeries = new ContentValues();
            for (int i = 0; i < 1; i++) {
                valuesSeries.put("serie", "C001");
                valuesSeries.put("naturaleza", "01");
                valuesSeries.put("folio", 0);

                db.insert("series", null, valuesSeries);









            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }






        db.execSQL("CREATE TABLE "+ TABLE_EMPRESA + " (" +
                TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                RAZON_SOCIAL_EMPRESA +" TEXT,"+
                RUC_EMPRESA +" TEXT,"+
                DIRECCION_EMPRESA_1 + " TEXT,"+
                DIRECCION_EMPRESA_2 + " TEXT,"+
                CORREO_EMPRESA + " TEXT,"+
                TELEFONO_EMPRESA + " TEXT,"+
                LOGO + " BLOB,"+
                LICENCIA + " TEXT, "+
                LINEA01 + " TEXT NOT NULL, "+
                LINEA02 + " TEXT NOT NULL, "+
                LINEA03 + " TEXT NOT NULL, "+
                LINEA04 + " TEXT NOT NULL, "+
                LINEA05 + " TEXT NOT NULL, "+
                LINEA06 + " TEXT NOT NULL, "+
                LINEA07 + " TEXT NOT NULL, "+
                LINEA08 + " TEXT NOT NULL, "+
                LINEA09 + " TEXT NOT NULL, "+
                BASE01 + " TEXT NOT NULL, "+
                BASE02 + " TEXT NOT NULL, "+
                BASE03 + " TEXT NOT NULL, "+
                BASE04 + " TEXT NOT NULL, "+
                BASE05 + " TEXT NOT NULL, "+
                BASE06 + " TEXT NOT NULL, "+
                API_KEY_DOCTOS + " TEXT NOT NULL, "+
                API_KEY_MOVTOS + " TEXT NOT NULL, "+
                API_KEY_ALMACEN + " TEXT NOT NULL, "+
                API_KEY_MOVIMIENTOS + " TEXT NOT NULL, "+
                API_KEY_CLIENTES + " TEXT NOT NULL, "+
                API_KEY_PRODUCTOS + " TEXT NOT NULL, "+
                API_KEY_00 + " TEXT NOT NULL, "+
                API_KEY_01 + " TEXT NOT NULL, "+
                API_KEY_02 + " TEXT NOT NULL, "+
                API_KEY_03 + " TEXT NOT NULL, "+
                API_KEY_04 + " TEXT NOT NULL, "+
                API_KEY_05 + " TEXT NOT NULL, "+
                API_KEY_06 + " TEXT NOT NULL, "+
                API_KEY_07 + " TEXT NOT NULL, "+
                API_KEY_08 + " TEXT NOT NULL, "+
                API_KEY_09 + " TEXT NOT NULL, "+
                API_KEY_10 + " TEXT NOT NULL, "+
                IMPRESORA + " TEXT NOT NULL, "+
                IMPUESTO_IBCPER+" TEXT NOT NULL)"


                



        );




        db.beginTransaction();
        try {
            ContentValues valuesEmpresa = new ContentValues();
            for (int i = 0; i < 1; i++) {

                valuesEmpresa.put(RAZON_SOCIAL_EMPRESA, "MI EMPRESA SAC");
                valuesEmpresa.put(RUC_EMPRESA,    "12345678901");
                valuesEmpresa.put(DIRECCION_EMPRESA_1, "DIRECCION 1");
                valuesEmpresa.put(DIRECCION_EMPRESA_2, "DIRECCION 2");
                valuesEmpresa.put(CORREO_EMPRESA, "mcorreo@miempresa.com");
                valuesEmpresa.put(TELEFONO_EMPRESA, "(+) 01 111-222-333");
                valuesEmpresa.put(LICENCIA, "PUBLICA");
                valuesEmpresa.put(LINEA01, " L A  G R A N  E M P R E S A   ");
//                                          ===============================
                valuesEmpresa.put(LINEA02, "   DIR1: AV. PRINCIPAL 00000   ");
                valuesEmpresa.put(LINEA03, "  DIR1:  AV. SECUNDARIA 00000  ");
                valuesEmpresa.put(LINEA04, "  ventas@lagranempresa.com.pe  ");
                valuesEmpresa.put(LINEA05, "TEL: 111-111-111   123-125-452 ");
                valuesEmpresa.put(LINEA06, "   www.lagranempresa.com-mx    ");
                valuesEmpresa.put(LINEA07, "");
                valuesEmpresa.put(LINEA08, "");
                valuesEmpresa.put(LINEA09, "");
                valuesEmpresa.put(BASE01, " consulte su comprobante fiscal ");
                valuesEmpresa.put(BASE02, "  en www.factura.elect.com.pe   ");
                valuesEmpresa.put(BASE03, "     para ventas al mayoreo     ");
                valuesEmpresa.put(BASE04, "");
                valuesEmpresa.put(BASE05, "");
                valuesEmpresa.put(BASE06, "");
                valuesEmpresa.put(API_KEY_DOCTOS, "");
                valuesEmpresa.put(API_KEY_MOVTOS, "");
                valuesEmpresa.put(API_KEY_ALMACEN, "");
                valuesEmpresa.put(API_KEY_MOVIMIENTOS, "");
                valuesEmpresa.put(API_KEY_CLIENTES, "");
                valuesEmpresa.put(API_KEY_PRODUCTOS, "");
                valuesEmpresa.put(API_KEY_00, "admin");
                valuesEmpresa.put(API_KEY_01, "");
                valuesEmpresa.put(API_KEY_02, "");
                valuesEmpresa.put(API_KEY_03, "");
                valuesEmpresa.put(API_KEY_04, "");
                valuesEmpresa.put(API_KEY_05, "");
                valuesEmpresa.put(API_KEY_06, "");
                valuesEmpresa.put(API_KEY_07, "");
                valuesEmpresa.put(API_KEY_08, "");
                valuesEmpresa.put(API_KEY_09, "");
                valuesEmpresa.put(API_KEY_10, "");
                valuesEmpresa.put(IMPRESORA, "normal");
                valuesEmpresa.put(IMPUESTO_IBCPER,".10");
                db.insert(TABLE_EMPRESA, null, valuesEmpresa);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }


        db.execSQL("CREATE TABLE "+ TABLE_CLIENTES + " (" +
                TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                RAZON_SOCIAL_CLIENTE +" TEXT DEFAULT '',"+
                RUC_CLIENTE +" TEXT DEFAULT '00000000',"+
                DIRECCION_CLIENTE + " TEXT DEFAULT '',"+
                CORREO_CLIENTE + " TEXT DEFAULT '',"+
                TIPO_INDENTIDAD + " TEXT DEFAULT '1',"+
                TELEFONO_CLIENTE + " TEXT DEFAULT '')");

        // CREATE INDEX
        db.execSQL("CREATE INDEX RUC ON "+ TABLE_CLIENTES + " (" +
                RUC_CLIENTE + ")");




        db.beginTransaction();
        try {
            ContentValues valuesClientes = new ContentValues();
            for (int i = 0; i < 1; i++) {
                valuesClientes.put(RAZON_SOCIAL_CLIENTE, "SIN DNI");
                valuesClientes.put(RUC_CLIENTE, "00000000");
                valuesClientes.put(DIRECCION_CLIENTE, "SIN DIRECCION");
                valuesClientes.put(CORREO_CLIENTE, "SIN CORREO-");
                valuesClientes.put(TELEFONO_CLIENTE, "SIN TELEFONO");
                valuesClientes.put(TIPO_INDENTIDAD, "1");

                db.insert(TABLE_CLIENTES, null, valuesClientes);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }



        db.execSQL("CREATE TABLE "+ TABLE_UNIDADES + " (" +
                TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ID_UNIDAD +" TEXT,"+
                DESCRIPCION_UNIDAD +" TEXT)");


        db.beginTransaction();
        try {
            ContentValues valuesUnidades = new ContentValues();
            for (int i = 0; i < 1; i++) {
                valuesUnidades.put(UNIDAD, "UNI");
                valuesUnidades.put(DESCRIPCION_UNIDAD, "Unidades");
                db.insert(TABLE_UNIDADES, null, valuesUnidades);

                valuesUnidades.put(UNIDAD, "KGM");
                valuesUnidades.put(DESCRIPCION_UNIDAD, "Kilogramo");
                db.insert(TABLE_UNIDADES, null, valuesUnidades);

                valuesUnidades.put(UNIDAD, "MTR");
                valuesUnidades.put(DESCRIPCION_UNIDAD, "Metro");
                db.insert(TABLE_UNIDADES, null, valuesUnidades);

                valuesUnidades.put(UNIDAD, "LTR");
                valuesUnidades.put(DESCRIPCION_UNIDAD, "Litro");
                db.insert(TABLE_UNIDADES, null, valuesUnidades);




            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }





        db.execSQL("CREATE TABLE "+ TABLE_PRODUCTOS + " (" +
                TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ID_PRODUCTO +" TEXT,"+
                ID_VELNEO + " TEXT,"+
                DESCRIPCION_PRODUCTO +" TEXT,"+
                UNIDAD_PRODUCTO + " TEXT DEFAULT 'NIU',"+
                PRECIO_PRODUCTO + " TEXT,"+
                PRECIO_PRODUCTO_MAYOREO + " TEXT,"+
                EXISTENCIA + " DOUBLE,"+
                IGV_PRODUCTO + " DOUBLE DEFAULT .18,"+
                VENTAS_PRO + " DOUBLE,"+
                SALDO_PRO + " DOUBLE)");

        // CREATE INDEX
        db.execSQL("CREATE INDEX ID_PRODUCTO ON "+ TABLE_PRODUCTOS + " (" +
                ID_PRODUCTO + ")");


        db.beginTransaction();
        try {
            ContentValues valuesProductos = new ContentValues();
            for (int i = 0; i < 1; i++) {
                valuesProductos.put(ID_PRODUCTO, "001");
                valuesProductos.put(DESCRIPCION_PRODUCTO, "PRODUCTO GENERICO");
                valuesProductos.put(UNIDAD_PRODUCTO, "UNI");
                valuesProductos.put(PRECIO_PRODUCTO, 1.00);
                valuesProductos.put(PRECIO_PRODUCTO_MAYOREO, 1.00);
                valuesProductos.put(EXISTENCIA, 0.00);
              //  valuesProductos.put(IGV_PRODUCTO, .18);


                db.insert(TABLE_PRODUCTOS, null, valuesProductos);


                valuesProductos.put(ID_PRODUCTO, "BOLSA");
                valuesProductos.put(DESCRIPCION_PRODUCTO, "BOLSA DE PLASTICO");
                valuesProductos.put(UNIDAD_PRODUCTO, "UNI");
                valuesProductos.put(PRECIO_PRODUCTO, 0.10);
                valuesProductos.put(PRECIO_PRODUCTO_MAYOREO, 0.10);
                valuesProductos.put(EXISTENCIA, 0.00);
                valuesProductos.put(IGV_PRODUCTO, .18);


                db.insert(TABLE_PRODUCTOS, null, valuesProductos);




            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }





        // TABLE = "sycro";

        db.execSQL("CREATE TABLE "+ TABLE_SYNCRO + " (" +
                SYNCRO_FECHA +" DATE PRIMARY KEY,"+
                SYNCRO_DOCTOS +" DOUBLE,"+
                SYNCRO_GRAVADO +" DOUBLE,"+
                SYNCRO_EXCENTO +" DOUBLE,"+
                SYNCRO_INAFECTO+" DOUBLE,"+
                SYNCRO_SUBTOTAL+" DOUBLE,"+
                SYNCRO_IGV+" DOUBLE,"+
                SYNCRO_TOTAL+" DOUBLE,"+
                SYNCRO_ENVIADO_NUBE+" INTEGER DEFAULT 0,"+
                SYNCRO_REVISADA+" INTEGER DEFAULT 0,"+
                SYNCRO_ARCHIVADA+" INTEGER DEFAULT 0)"
        );


        // TABLE = "cierre";


        db.execSQL("CREATE TABLE "+ TABLE_CIERRE + " (" +
                CIERRE_TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CIERRE_SERIE +" TEXT, "+
                CIERRE_DEL_FOLIO +" INT DEFAULT 0,"+
                CIERRE_AL_FOLIO +" INT DEFAULT 0,"+
                CIERRE_FECHA +" DATE,"+
                CIERRE_GRAVADO +" DOUBLE,"+
                CIERRE_EXCENTO +" DOUBLE,"+
                CIERRE_INAFECTO+" DOUBLE,"+
                CIERRE_SUBTOTAL+" DOUBLE,"+
                CIERRE_IGV+" DOUBLE,"+
                CIERRE_TOTAL+" DOUBLE)"
        );

        db.beginTransaction();
        try {
            ContentValues valuesCierre = new ContentValues();
            for (int i = 0; i < 1; i++) {
                valuesCierre.put(CIERRE_SERIE, "FXXX");
                valuesCierre.put(CIERRE_DEL_FOLIO, "00000");
                valuesCierre.put(CIERRE_DEL_FOLIO, "99999");

                db.insert(TABLE_CIERRE, null, valuesCierre);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }



    // TABLA PARA movimiento




        // variables para table movimientos

        db.execSQL("CREATE TABLE "+ TABLE_MOVIMIENTOS + " (" +
                TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FECHA_MOV +" DATE,"+
                HORA_MOV +" TEXT,"+
                PRODUCTO_MOV + " TEXT,"+
                COSTO_PRODUCTO_MOV + " DOUBLE,"+
                PRECIO_PRODUCTO_MOV + " DOUBLE,"+
                TIPO_MOV + " TEXT,"+
                SALDO + " DOUBLE)");



        // TABLA PARA colores

       // variables para table colores


        //   // DEFINICION DE CONSTANTES PARA CAMPOS DE TABLA COLORES  "colores"
        //
        //    public static final String COLOR_ID = "color_id";
        //    public static final String NOMBRE_COLOR = "color";

        db.execSQL("CREATE TABLE "+ TABLE_COLORES + " (" +
                COLOR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                NOMBRE_COLOR +" TEXT)");


        db.execSQL("CREATE TABLE "+ TABLE_TALLAS + " (" +
                TALLA_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                NOMBRE_TALLA +" TEXT)");



    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);
        onCreate(db);

    }



    public void close() {
        this.close();

    }


    public void addNotes (String _serie,
                              String _folio,
                              String _ruc,
                              String _razon_social,
                              String _direccion,
                              String _moneda,
                               String _correo) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        ContentValues valoresCabecera = new ContentValues();
        valoresCabecera.put(SERIE, _serie);
        valoresCabecera.put(FOLIO, _folio);
        valoresCabecera.put(FECHA, dateFormat.format(date));
        valoresCabecera.put(RUC, _ruc);
        valoresCabecera.put(RAZON_SOCIAL, _razon_social);
        valoresCabecera.put(DIRECCION, _direccion);
        valoresCabecera.put(MONEDA, _moneda);
        valoresCabecera.put(CORREO, _correo);
   //     valoresCabecera.put(GRAVADO, _gravado);
   //     valoresCabecera.put(EXCENTO, _excento);
   //     valoresCabecera.put(INAFECTO, _inafecto);
   //     valoresCabecera.put(SUBTOTAL, _subtotal);
   //     valoresCabecera.put(IGV, _igv);
   //     valoresCabecera.put(TOTAL, _total);

        this.getWritableDatabase().insert(TABLE,null, valoresCabecera);

    }


    public void addNotes_Notas (String _serie,
                          String _folio,
                          String _ruc,
                          String _razon_social,
                          String _direccion,
                          String _moneda,
                                String _serie_rel,
                                String _folio_rel
                            ) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        ContentValues valoresCabecera = new ContentValues();
        valoresCabecera.put(SERIE, _serie);
        valoresCabecera.put(FOLIO, _folio);
        valoresCabecera.put(FECHA, dateFormat.format(date));
        valoresCabecera.put(RUC, _ruc);
        valoresCabecera.put(RAZON_SOCIAL, _razon_social);
        valoresCabecera.put(DIRECCION, _direccion);
        valoresCabecera.put(MONEDA, _moneda);
        valoresCabecera.put(SERIE_REL, _serie_rel);
        valoresCabecera.put(FOLIO_REL, _folio_rel);

        //     valoresCabecera.put(GRAVADO, _gravado);
        //     valoresCabecera.put(EXCENTO, _excento);
        //     valoresCabecera.put(INAFECTO, _inafecto);
        //     valoresCabecera.put(SUBTOTAL, _subtotal);
        //     valoresCabecera.put(IGV, _igv);
        //     valoresCabecera.put(TOTAL, _total);

        this.getWritableDatabase().insert(TABLE,null, valoresCabecera);

    }



    public void addNotes_Detalle (int _id, String _producto,
                          String _descripcion,
                          String _unidad,
                          String _cantidad,
                          String _precio,
                          String _igv

    ) {



        ContentValues valoresDetalle = new ContentValues();
        valoresDetalle.put(CABECERA_ID, _id);
        valoresDetalle.put(PRODUCTO, _producto);
        valoresDetalle.put(DESCRIPCION, _descripcion);
        valoresDetalle.put(UNIDAD, _unidad);
        valoresDetalle.put(CANTIDAD, _cantidad);
        valoresDetalle.put(PRECIO_PRODUCTO, _precio);
        valoresDetalle.put(DET_IGV, _igv);

        this.getWritableDatabase().insert(TABLE_DET,null, valoresDetalle);

    }


    public void addNotes_Clientes (String _razon_social_cliente,
                                   String _ruc_cliente,
                                   String _direccion_cliente,
                                   String _telefono_cliente,
                                   String _correo_cliente
                                   ) {



        ContentValues valoresClietes = new ContentValues();
        valoresClietes.put(RAZON_SOCIAL_CLIENTE, _razon_social_cliente);
        valoresClietes.put(RUC_CLIENTE, _ruc_cliente);
        valoresClietes.put(DIRECCION_CLIENTE, _direccion_cliente);
        valoresClietes.put(CORREO_CLIENTE, _correo_cliente);
        valoresClietes.put(TELEFONO_CLIENTE, _telefono_cliente);


        this.getWritableDatabase().insert(TABLE_CLIENTES,null, valoresClietes);

    }



    public void addNotes_Productos (String _producto,
                                   String _descripcion_producto,
                                   String _unidad,
                                   String _precio,
                                   String _precio_mayoreo,
                                   String _igv,
                                   String _id_velneo

    ) {



        ContentValues valoresProductos = new ContentValues();
        valoresProductos.put(PRODUCTO, _producto);
        valoresProductos.put(DESCRIPCION_PRODUCTO, _descripcion_producto);
        valoresProductos.put(UNIDAD_PRODUCTO, _unidad);
        valoresProductos.put(PRECIO_PRODUCTO, _precio);
        valoresProductos.put(PRECIO_PRODUCTO_MAYOREO, _precio_mayoreo);
        valoresProductos.put(IGV_PRODUCTO, _igv);
        valoresProductos.put(ID_VELNEO, _id_velneo);


        this.getWritableDatabase().insert(TABLE_PRODUCTOS,null, valoresProductos);

    }


    public void addNotes_Unidades (String _unidad,
                                    String _descripcion_unidad

    ) {



        ContentValues valoresUnidades = new ContentValues();
        valoresUnidades.put(ID_UNIDAD, _unidad);
        valoresUnidades.put(DESCRIPCION_UNIDAD, _descripcion_unidad);



        this.getWritableDatabase().insert(TABLE_UNIDADES,null, valoresUnidades);

    }






    public void addNotes_Cierre (String _serie
    ) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        ContentValues valoresCierre = new ContentValues();
        valoresCierre.put(CIERRE_SERIE, _serie);
        valoresCierre.put(CIERRE_FECHA, dateFormat.format(date));

        this.getWritableDatabase().insert(TABLE_CIERRE,null, valoresCierre);

    }




    public Cursor getNotes() {
        String columnas[] = {TABLE_ID, SERIE, FOLIO, RUC,  RAZON_SOCIAL, FECHA, SERIE_REL, FOLIO_REL, ARCHIVADA};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas, null, null, null, null, " _id DESC ");
        return c;
    }


    public Cursor getNotes_Activas() {

        int _criterio=0; // activas

        String columnas[] = {TABLE_ID, SERIE, FOLIO, RUC,  RAZON_SOCIAL, FECHA, SERIE_REL, FOLIO_REL, ARCHIVADA, ID_VELNEO};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas,
                ARCHIVADA + "=?",  new String[] { String.valueOf(_criterio) },
                null, null, " _id DESC ");
        return c;
    }


    public Cursor getNotes_Archivadas() {

        int _criterio=1; // activas

        String columnas[] = {TABLE_ID, SERIE, FOLIO, RUC,  RAZON_SOCIAL, FECHA, SERIE_REL, FOLIO_REL, ARCHIVADA};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas,
                ARCHIVADA + "=?",  new String[] { String.valueOf(_criterio) },
                null, null, " _id DESC ");
        return c;
    }




    public Cursor getNotes_detalle(int _id) {
        String columnas[] = {TABLE_ID, PRODUCTO, DESCRIPCION, UNIDAD, CANTIDAD, PRECIO_PRODUCTO,  DET_IGV, LINEA, "_id" };
        Cursor c = this.getReadableDatabase().query(TABLE_DET, columnas, "cabecera_id" + "=?",
                new String[] { String.valueOf(_id) },  null, null, null, null);
        //  Cursor c = this.getReadableDatabase().query(TABLE_DET, columnas, null, null, null, null, null);
        return c;
    }


    public Cursor getNotes_series() {
        String columnas[] = {"id", "serie", "folio", "naturaleza"};
        Cursor c = this.getReadableDatabase().query("series", columnas, null, null, null, null, null);
        return c;
    }


    public String get_Naturaleza(String _serie) {
        String _regreso="00";

        String columnas[] = {"id", "serie", "folio", "naturaleza"};
        Cursor c = this.getReadableDatabase().query("series", columnas, "serie" + "=?",
                new String[] { _serie }, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                _regreso = c.getString(3);

            } while (c.moveToNext());
        }
        return _regreso;
    }





    public Cursor getNotes_clientes() {
        String columnas[] = {"_id", "ruc_cliente", "razon_social_cliente", "direccion_cliente", "correo_cliente", "telefono_cliente", "tipo_identidad"};
        Cursor c = this.getReadableDatabase().query("clientes", columnas, null, null, null, null, null);
        return c;
    }






//    public static final String TABLE_ID = "_id";
//    public static final String SERIE = "serie";
//    public static final String FOLIO = "folio";
//    public static final String FECHA = "fecha";
//    public static final String RUC = "ruc";
//    public static final String RAZON_SOCIAL = "razon_social";
//    public static final String DIRECCION = "direccion";
//    public static final String MONEDA = "moneda";
//    public static final String GRAVADO = "gravado";
//    public static final String EXCENTO = "excento";
//    public static final String INAFECTO = "inafecto";
//    public static final String SUBTOTAL = "subtotal";
//    public static final String IGV = "igv";
//    public static final String TOTAL = "total";
//    public static final String CORREO = "correo";
//    public static final String SERIE_REL = "serie_rel";
//    public static final String FOLIO_REL = "folio_rel";
//    public static final String ARCHIVADA = "archivada";
//    public static final String ENVIADO_CORREO = "enviado_correo";
//    public static final String ENVIADO_NUBE = "enviado_nube";




    public Cursor getNotes_Documentos(String _fecha) {
        String columnas[] = {TABLE_ID, SERIE, FOLIO, GRAVADO, EXCENTO, INAFECTO, SUBTOTAL, IGV, TOTAL};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas, FECHA + "=?",
                new String[] { _fecha },  null, null, "serie,folio", null);
        //  Cursor c = this.getReadableDatabase().query(TABLE_DET, columnas, null, null, null, null, null);
        return c;
    }

  //  TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
  //  SERIE +" TEXT, "+
  //  FOLIO +" INT DEFAULT 0,"+
  //  FECHA +" DATE,"+
  //  RUC +" TEXT,"+
  //  RAZON_SOCIAL +" TEXT,"+
  //  DIRECCION + " TEXT,"+
  //  MONEDA +" TEXT,"+
  //  CORREO +" TEXT DEFAULT 'usuario@dominio.com',"+
  //  SERIE_REL +" TEXT,"+
  //  FOLIO_REL +" TEXT,"+
  //  GRAVADO +" DOUBLE,"+
   // EXCENTO +" DOUBLE,"+
  //  INAFECTO+" DOUBLE,"+
  //  SUBTOTAL+" DOUBLE,"+
  //  IGV+" DOUBLE,"+
  //  TOTAL+" DOUBLE,"+
  //  ENVIADO_CORREO+" INTEGER DEFAULT 0,"+
  //  ENVIADO_NUBE+" INTEGER DEFAULT 0,"+
  //  ARCHIVADA+" INTEGER DEFAULT 0)"


    public Cursor getNotes_Documentos_Nube(String _fecha) {
        String columnas[] = {TABLE_ID, SERIE, FOLIO, GRAVADO, EXCENTO, INAFECTO, SUBTOTAL, IGV, TOTAL, ENVIADO_NUBE,
                RAZON_SOCIAL, FECHA, RUC, DIRECCION, MONEDA, CORREO, SERIE_REL, FOLIO_REL};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas, FECHA + "=?",
                new String[] { _fecha },  null, null, "serie,folio", null);
        //  Cursor c = this.getReadableDatabase().query(TABLE_DET, columnas, null, null, null, null, null);
        return c;
    }



//    public Cursor getNotes_Documentos(String _criterio) {

 //      // int _criterio=0; // activas

   //     _criterio="F001";




     //   String columnas[] = {TABLE_ID, SERIE, FOLIO, GRAVADO, EXCENTO, INAFECTO, SUBTOTAL, IGV, TOTAL};
      //  Cursor c = this.getReadableDatabase().query(TABLE, columnas,
        //        SERIE + "=?",  new String[] { _criterio },
          //      null, null, " _id DESC ");
      //  return c;
  //  }





    public Cursor getNotes_productos() {
        String columnas[] = {TABLE_ID,ID_PRODUCTO, DESCRIPCION_PRODUCTO, UNIDAD_PRODUCTO, PRECIO_PRODUCTO, PRECIO_PRODUCTO_MAYOREO, IGV_PRODUCTO, EXISTENCIA, VENTAS_PRO, SALDO_PRO,ID_VELNEO};
        Cursor c = this.getReadableDatabase().query("productos", columnas, null, null, null, null, "descripcion_producto");
        return c;
    }


    public Cursor getNotes_unidades() {
        String columnas[] = {TABLE_ID,ID_UNIDAD, DESCRIPCION_UNIDAD};
        Cursor c = this.getReadableDatabase().query("unidades", columnas, null, null, null, null, null);
        return c;
    }


//        db.execSQL("CREATE TABLE "+ TABLE_CIERRE + " (" +
//    CIERRE_TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
//    CIERRE_SERIE +" TEXT, "+
//    CIERRE_DEL_FOLIO +" INT DEFAULT 0,"+
//    CIERRE_AL_FOLIO +" INT DEFAULT 0,"+
//    CIERRE_FECHA +" DATE,"+
//    CIERRE_GRAVADO +" DOUBLE,"+
//    CIERRE_EXCENTO +" DOUBLE,"+
//    CIERRE_INAFECTO+" DOUBLE,"+
//    CIERRE_SUBTOTAL+" DOUBLE,"+
//    CIERRE_IGV+" DOUBLE,"+
//    CIERRE_TOTAL+" DOUBLE)"
//            );



    public Cursor getNotes_Cierre() {
        String columnas[] = {CIERRE_TABLE_ID, CIERRE_SERIE, CIERRE_FECHA, CIERRE_DEL_FOLIO, CIERRE_AL_FOLIO };
        Cursor c = this.getReadableDatabase().query("cierre", columnas, null, null, null, null, null);
        return c;
    }




    public Cursor getReg(int _id) {
        String columnas[] = {TABLE_ID, SERIE, FOLIO, RUC, RAZON_SOCIAL, DIRECCION, MONEDA, FECHA, CORREO};

        Cursor c = this.getReadableDatabase().query(TABLE, columnas,
                TABLE_ID + "=?",  new String[] { String.valueOf(_id) },
                null, null, null, null);
        return c;


    }


    public Cursor getReg_Notas(int _id) {
        String columnas[] = {TABLE_ID, SERIE, FOLIO, RUC, RAZON_SOCIAL, DIRECCION, MONEDA, FECHA, CORREO, SERIE_REL, FOLIO_REL};

        Cursor c = this.getReadableDatabase().query(TABLE, columnas, TABLE_ID + "=?",
                new String[] { String.valueOf(_id) },  null, null, null, null);
        return c;


    }



    public Cursor getReg_Serie(int _id) {
        String columnas[] = {"id", "serie", "naturaleza", "folio" };

        Cursor c = this.getReadableDatabase().query("series", columnas, "id" + "=?",
                new String[] { String.valueOf(_id) },  null, null, null, null);
        return c;
    }



    public Cursor getReg_num_Serie(String _serie) {
        String columnas[] = {"id", "serie", "folio"};

        Cursor c = this.getReadableDatabase().query("series", columnas, "serie" + "=?",
                new String[] { String.valueOf(_serie) },  null, null, null, null);
        return c;
    }






    public Cursor getReg_Empresas(int _id) {
        String columnas[] = {"_id","razon_social_empresa", "ruc_empresa",   "direccion_empresa_1", "impuesto_icbper", "telefono_empresa", "licencia", "tipo_impresora"};

        Cursor c = this.getReadableDatabase().query("empresa", columnas, null, null,  null, null, null, null);
                return c;
    }

    public Cursor getReg_TicketPos(int _id) {
        String columnas[] = {"_id","linea01", "linea02", "linea03", "linea04", "linea05", "linea06","linea07", "linea08", "linea09",
                "ruc_empresa",
                "base01", "base02", "base03", "base04", "base05", "base06","direccion_empresa_1"};

        Cursor c = this.getReadableDatabase().query("empresa", columnas, null, null,  null, null, null, null);
        return c;
    }






    public Cursor getReg_Apiskey(int _id) {
        String columnas[] = {"_id","api_key_doctos", "api_key_movtos", "api_key_productos", "api_key_clientes", "api_key_almacen", "api_key_movimientos","api_key_00", "api_key_01", "api_key_02" };

        Cursor c = this.getReadableDatabase().query("empresa", columnas, null, null,  null, null, null, null);
        return c;
    }


    public Cursor getReg_Licencia(int _id) {
        String columnas[] = {"_id","licencia"};

        Cursor c = this.getReadableDatabase().query("empresa", columnas, null, null,  null, null, null, null);
        return c;
    }


    public Cursor getReg_tickect(int _id) {
        String columnas[] = {"_id","tipo_impresora"};

        Cursor c = this.getReadableDatabase().query("empresa", columnas, null, null,  null, null, null, null);
        return c;
    }



    public Cursor getReg_icbper(int _id) {
        String columnas[] = {"_id","impuesto_icbper"};

        Cursor c = this.getReadableDatabase().query("empresa", columnas, null, null,  null, null, null, null);
        return c;
    }



    public Cursor getReg_Clientes(int _id) {
        String columnas[] = {"_id", "ruc_cliente","razon_social_cliente","direccion_cliente","correo_cliente","telefono_cliente" };

        Cursor c = this.getReadableDatabase().query("clientes", columnas, "_id" + "=?",
                new String[] { String.valueOf(_id) },  null, null, null, null);
        return c;
    }


    public Cursor getReg_Productos(int _id) {
        String columnas[] = {"_id", "producto","descripcion_producto","precio", "precio_mayoreo", "igv", "unidad", "existencia", "ventas_pro", "saldo_pro"};

        Cursor c = this.getReadableDatabase().query("productos", columnas, "_id" + "=?",
                new String[] { String.valueOf(_id) },  null, null, null, null);
        return c;
    }


    public Cursor getReg_Productos_codigo(String _producto) {
        String columnas[] = {"_id", "producto","descripcion_producto","precio", "precio_mayoreo", "igv", "unidad", "existencia", "ventas_pro", "saldo_pro" };

        Cursor c = this.getReadableDatabase().query("productos", columnas, "producto" + "=?",
                new String[] { String.valueOf(_producto) },  null, null, null, null);
        return c;
    }



    public Cursor getReg_Unidades(int _id) {
        String columnas[] = {"_id", "unidad","descripcion_unidad"};

        Cursor c = this.getReadableDatabase().query("unidades", columnas, "_id" + "=?",
                new String[] { String.valueOf(_id) },  null, null, null, null);
        return c;
    }



    //public static final String CABECERA_ID = "cabecera_id";
    //public static final String PRODUCTO = "producto";
    //public static final String DESCRIPCION = "descripcion";
    //public static final String UNIDAD = "unidad";
    //public static final String PRECIO = "precio";
    //public static final String CANTIDAD = "cantidad";
    //public static final String DET_SUBTOTAL = "det_subtotal";
    //public static final String DET_IGV = "det_igv";
    //public static final String DET_TOTAL = "det_total";


    public Cursor getReg_Detalle(int _id) {
        String columnas[] = {"_id", "cabecera_id","producto", "descripcion", "unidad","cantidad", "precio","det_igv"};

        Cursor c = this.getReadableDatabase().query("detalle", columnas, "_id" + "=?",
                new String[] { String.valueOf(_id) },  null, null, null, null);
        return c;
    }


    public void addNotes_Series (String _Serie,
                                 String _Folio,
                                 String _Naturaleza

    ) {



        ContentValues valoresSeries = new ContentValues();
        valoresSeries.put("serie", _Serie);
        valoresSeries.put("folio", _Folio);
        valoresSeries.put("naturaleza", _Naturaleza);



        this.getWritableDatabase().insert("series",null, valoresSeries);

    }


    public Cursor getReg_Image(int _id) {
        String columnas[] = {"_id","logo"};

        Cursor c = this.getReadableDatabase().query("empresa", columnas, null, null,  null, null, null, null);
        return c;
    }





    public int exits_Client(String _ruc) {
        Cursor cursor = null;
        int _resultado=0;
            String columnas[] = {"_id", "ruc_cliente"};
            cursor = this.getReadableDatabase().query("clientes", columnas, "ruc_cliente" + "=?",
                     new String[] { String.valueOf(_ruc) },
                    null, null, null, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                _resultado=1;
            }
            return _resultado;
    }




    public int exits_Productos(String _prod) {
        Cursor cursor = null;
        int _resultado=0;
        String columnas[] = {"producto"};
        cursor = this.getReadableDatabase().query("productos", columnas, "producto" + "=?",
                new String[] { String.valueOf(_prod) },
                null, null, null, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            _resultado=1;
        }
        return _resultado;
    }


    public double total_ventas_Productos(String _prod) {
        Cursor cursor = null;
        double _resultado=0;
        String columnas[] = {"producto", "ventas_pro"};
        cursor = this.getReadableDatabase().query("productos", columnas, "producto" + "=?",
                new String[] { String.valueOf(_prod) },
                null, null, null, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            _resultado = cursor.getDouble(1);

        }
        return _resultado;
    }


    public double total_existecias_Productos(String _prod) {
        Cursor cursor = null;
        double _resultado=0;
        String columnas[] = {"producto", "existencia"};
        cursor = this.getReadableDatabase().query("productos", columnas, "producto" + "=?",
                new String[] { String.valueOf(_prod) },
                null, null, null, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            _resultado = cursor.getDouble(1);

        }
        return _resultado;
    }


    public String _id_factura_velneo(String _id_app) {
        Cursor cursor = null;

        String _resultado="";

        String columnas[] = {"_id_velneo"};
        cursor = this.getReadableDatabase().query("cabecera", columnas, "_id" + "=?",
                new String[] { String.valueOf(_id_app) },
                null, null, null, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            _resultado = cursor.getString(0);

        }
        return _resultado;
    }






//    public Cursor getNotes_detalle(int _id) {
//        String columnas[] = {TABLE_ID, PRODUCTO, DESCRIPCION, UNIDAD, CANTIDAD, PRECIO_PRODUCTO,  DET_IGV};
//        Cursor c = this.getReadableDatabase().query(TABLE_DET, columnas, "cabecera_id" + "=?",
//                new String[] { String.valueOf(_id) },  null, null, null, null);
//        //  Cursor c = this.getReadableDatabase().query(TABLE_DET, columnas, null, null, null, null, null);
//        return c;
//    }






}
