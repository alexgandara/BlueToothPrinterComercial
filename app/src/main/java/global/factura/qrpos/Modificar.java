package global.factura.qrpos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;


public class Modificar extends Activity {





    public  static DecimalFormat df;
    public  static DecimalFormatSymbols simb;

    public static final String TABLE_ID = "_id";
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

    private static final String DATABASE = "miniPOS";
    private static final String TABLE = "cabecera";

    public double _subtotal=0;
    public double _total=0;
    public double _igv=0;
    public int    Mid   =0;
    public String Mserie="";
    public int Mfolio=0;
    public String Mruc="";
    public String Mrazon_social="";
    public String Mdireccion="";
    public String Mmoneda="";
    public String Mfecha="";
    public String _detalle="";
    public String Mcorreo="";

    public double _igv_global;


    EditText serie, folio, ruc, razon_social, direccion, moneda, fecha, correo;

    String _serie, _folio, _ruc, _razon_social, _direccion, _moneda, _fecha, _correo;

    int _myId;
    connectionDB db;
    Button Modificar, Eliminar, Salir, Detalle, Imprimir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);


        Bundle b = getIntent().getExtras();
        if (b!=null) {
            _myId = b.getInt("id");

        }


        db = new connectionDB(this);
        Cursor cursor =  db.getReg(_myId);
        if (cursor.moveToFirst()) {
            do {

                serie = (EditText) findViewById(R.id.editText_Serie);
                folio = (EditText) findViewById(R.id.editText_Folio);
                ruc = (EditText) findViewById(R.id.editText_Ruc);
                razon_social = (EditText) findViewById(R.id.editText_RazonSocial);
                direccion = (EditText) findViewById(R.id.editText_Direccion);
                moneda = (EditText) findViewById(R.id.editText_Moneda);
                fecha = (EditText) findViewById(R.id.editText_Fecha);
                correo = (EditText) findViewById(R.id.editText_Correo);

                Mid = cursor.getInt(0);
                Mserie = cursor.getString(1);
                Mfolio = cursor.getInt(2);
                Mruc = cursor.getString(3);
                Mrazon_social = cursor.getString(4);
                Mdireccion = cursor.getString(5);
                Mmoneda = cursor.getString(6);
                Mfecha = cursor.getString(7);
                Mcorreo = cursor.getString(8);

                serie.setText(Mserie);
                folio.setText(""+Mfolio);

                ruc.setText(Mruc);
                razon_social.setText(Mrazon_social);
                direccion.setText(Mdireccion);
                moneda.setText(Mmoneda);
                fecha.setText(Mfecha);
                correo.setText(Mcorreo);



              //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

            List<String> item = null;
            Cursor c = db.getNotes_detalle(Mid);
            item = new ArrayList<String>();
            String producto = "",  descripcion="", unidad="";
            int id, folio;
            double precio, cantidad, precio_sin_igv, subtotal, total, igv;
            String _salto ="\n";
            _detalle="";

            total=0;
            subtotal=0;
            _total=0;
            _subtotal=0;
            _igv=0;



            if (c.moveToFirst()) {
                do {
                    id = c.getInt(0);
                    producto = c.getString(1);
                    descripcion = c.getString(2);
                    unidad = c.getString(3);
                    cantidad = c.getDouble(4);
                    precio = c.getDouble(5);
                    igv = c.getDouble(6);
                    _igv_global=igv;



                    precio_sin_igv=precio/(1+igv);
                    subtotal=precio_sin_igv*cantidad;
                    _subtotal=_subtotal+subtotal;
                    _total=_total+(cantidad*precio);




                    item.add(id+" - "+producto+" - "+descripcion+" - "+unidad+" - "+cantidad+" "+precio);
                    //"CANT.  UNI    PRECIO     IMPORTE"+_salto;
                    _detalle=_detalle+descripcion.toUpperCase()+_salto;
                    _detalle=_detalle+_cantidad(cantidad)+" "+unidad+"    "+_cantidad(precio_sin_igv)+"     "+_cantidad(subtotal)+_salto;


                } while (c.moveToNext());
                _igv=_total-_subtotal;


            }





        }




        Modificar = (Button) findViewById(R.id.button_Modificar);
        Eliminar = (Button) findViewById(R.id.button_Eliminar);
        Detalle = (Button) findViewById(R.id.button_Detalle);
        Imprimir = (Button) findViewById(R.id.button_Imprimir);
        Salir = (Button) findViewById(R.id.button_Salir);



        serie = (EditText) findViewById(R.id.editText_Serie);
        folio = (EditText) findViewById(R.id.editText_Folio);
        ruc = (EditText) findViewById(R.id.editText_Ruc);
        razon_social = (EditText) findViewById(R.id.editText_RazonSocial);
        direccion = (EditText) findViewById(R.id.editText_Direccion);
        moneda = (EditText) findViewById(R.id.editText_Moneda);
        fecha = (EditText) findViewById(R.id.editText_Fecha);




        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar(_myId,
                        serie.getText().toString(),
                        folio.getText().toString(),
                        ruc.getText().toString(),
                        razon_social.getText().toString(),
                        direccion.getText().toString(),
                        moneda.getText().toString(),
                        correo.getText().toString());
                    }
                 });


        Detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modificar.this,MainActivity_Detalle.class );
                intent.putExtra("id",_myId);
                startActivity(intent);
            }
        });




        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar(_myId);

            }
        });


        Imprimir.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {

                int _folio_nuevo=0;
                String _licencia="";

                if (Mfolio == 0) {


                    _folio_nuevo=sumar_Serie(Mserie);
                    Cursor cursor =  db.getReg_Licencia(_myId);

                    if (cursor.moveToFirst()) {
                        do {



                            _licencia = cursor.getString(1);

                        } while (cursor.moveToNext());

                    }

                    if (!_licencia.equals("WESQ-WERS-TYVS")) {
                        if (_folio_nuevo>999) {
                            _folio_nuevo=9999999;
                            Mrazon_social=_licencia+"  LICENCIA    AGOTADA      llame   a    factura global    954 879 529    sopote@factura.global          - - - - - - - - - - - - - - - ";
                        }
                    }



                    Modificar_Print(Mid,
                            Mserie,
                            _folio_nuevo+"",
                            Mruc,
                            Mrazon_social,
                            Mdireccion,
                            Mmoneda,
                            Mcorreo);

                } else {
                    _folio_nuevo=Mfolio;

                }


                String _linea01="";
                String _linea02="";
                String _linea03="";
                String _linea04="";
                String _linea05="";
                String _linea06="";
                String _linea07="";



               //∫ db = new connectionDB(this);
                Cursor cursor =  db.getReg_TicketPos(_myId);
                if (cursor.moveToFirst()) {
                    do {



                        _linea01 = cursor.getString(1);
                        _linea02 = cursor.getString(2);
                        _linea03 = cursor.getString(3);
                        _linea04 = cursor.getString(4);
                        _linea05 = cursor.getString(5);
                        _linea06 = cursor.getString(6);
                        _linea07 = cursor.getString(7);
                    } while (cursor.moveToNext());

                }

                // me traigo la cabecera


                String _salida = "";
                String _cadena = "";
                String _salto ="\n";

                String _linea = "================================";
                _salida=_salida+_salto+_salto+_salto;
                _salida=_salida+_linea+_salto;

             //   _salida=_salida+"   < M O D A   G R U M E >"+_salto;
             //   _salida=_salida+_salto;
             //   _salida=_salida+"   HUAROC DE CHAUPIS BERNA      "+_salto;
             //   _salida=_salida+"      RUC: 10232302645"+_salto;
             //   _salida=_salida+"  AV. GRAU No 580 INT.71-LIMA-"+_salto;
             //   _salida=_salida+"     LIMA- LA VICTORIA"+_salto;
             //   _salida=_salida+"     TELF.: 017151780"+_salto;
             //   _salida=_salida+"     www.modagrume.com"+_salto;

                _salida=_salida+_linea01+_salto;
                _salida=_salida+_salto;
                _salida=_salida+_linea02+_salto;

                if (_linea03.trim().length()>0) {
                    _salida=_salida+_linea03+_salto;
                }

                if (_linea04.trim().length()>0) {
                    _salida=_salida+_linea04+_salto;
                }

                if (_linea05.trim().length()>0) {
                    _salida=_salida+_linea05+_salto;
                }

                if (_linea06.trim().length()>0) {
                    _salida=_salida+_linea06+_salto;
                }

                if (_linea07.trim().length()>0) {
                    _salida=_salida+_linea07+_salto;
                }


                _salida=_salida+_linea+_salto;

                String _naturaleza =  db.get_Naturaleza(Mserie);

                _cadena="LA SERIE NO HA SIDO DEFINIDA";

                if (_naturaleza.equals("03"))  {
                    _cadena="BOLETA ELECTRONICA";
                }

                if (_naturaleza.equals("01"))  {
                    _cadena="FACTURA ELECTRONICA";
                }

                if (_naturaleza.equals("07"))  {
                    _cadena="NOTA DE CREDITO ELECTRONICA"+_salto;

                }

                if (_naturaleza.equals("08"))  {
                    _cadena="NOTA DE DEBITO ELECTRONICA";
                }


                _salida=_salida+_cadena+_salto;

                _salida=_salida+"FOLIO:"+Mserie+"-"+_folio_nuevo+_salto;
                _salida=_salida+"F. EMISION:"+Mfecha+_salto;
                _salida=_salida+"MONEDA:"+"SOLES"+_salto;
                _salida=_salida+"TIPO DE VTA:"+"VTA INTERNA"+_salto;
                _salida=_salida+"CLIENTE:"+Mrazon_social.toUpperCase()+_salto;
                _salida=_salida+"RUC/DNI:"+Mruc+_salto;
                _salida=_salida+"DIRECCION:"+Mdireccion.toUpperCase()+_salto;
                _salida=_salida+"CORREO:"+Mcorreo+_salto;
                _salida=_salida+_linea+_salto;
                _salida=_salida+"DESCRIPCION"+_salto;
                _salida=_salida+"CANT.  UNI    PRECIO     IMPORTE"+_salto;
                _salida=_salida+_linea+_salto;
                _salida=_salida+_detalle;
                _salida=_salida+_salto;
                _salida=_salida+_salto;

                _salida=_salida+_linea+_salto;
                _salida=_salida+"                SUBTOTAL:"+_cantidad(_subtotal)+_salto;
                _salida=_salida+"              IGV "+_igv_global+" %:"+_cantidad(_igv)+_salto;
                _salida=_salida+"                   TOTAL:"+_cantidad(_total)+_salto;
                _salida=_salida+_salto;
                _salida=_salida+_salto;
                _salida=_salida+"TIPO DE PAGO:"+"CONTADO"+_salto;
                _salida=_salida+_salto;

                _salida=_salida+"Puede Solicitar  su  Comprobante"+_salto;
                _salida=_salida+"en   creacionesgrume.documentos@"+_salto;
                _salida=_salida+"gmail.com                       "+_salto;

                _salida=_salida+_salto;
                _salida=_salida+"Representacion    Impresa     de"+_salto;
                _salida=_salida+"del   Comprobante    de    Venta"+_salto;
                _salida=_salida+"Electronica  autorizado mediante"+_salto;
                _salida=_salida+"la   Resolucion   155-2017/SUNAT"+_salto;
                _salida=_salida+_linea+_salto;
                _salida=_salida+_salto+_salto;


                ClipboardManager clipboard = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                }
                ClipData clip = ClipData.newPlainText("recibo", _salida);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Modificar.this,"Es Registro fue enviado a Memoria"+_salida, Toast.LENGTH_LONG ).show();







            }
        });



        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _Salir();


            }
        });




    }



    public static String _cantidad(double _numero) {
        String _regreso;
        simb = new DecimalFormatSymbols();
        simb.setDecimalSeparator('.');
        simb.setGroupingSeparator(',');
        df = new DecimalFormat("##0.00", simb);
        _regreso=padLeft(df.format(_numero),6);
        return _regreso;

        // El resultado sería el siguiente: 94.751.890,37
    }





    public static String _dinero(double _numero) {
        String _regreso;
        simb = new DecimalFormatSymbols();
        simb.setDecimalSeparator('.');
        simb.setGroupingSeparator(',');
        df = new DecimalFormat("###,###.00", simb);

        _regreso=padLeft(df.format(_numero),11);
        return _regreso;

        // El resultado sería el siguiente: 94.751.890,37
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }


    private void _Salir() {
        Intent intent = new Intent(this, init_alfilPOS.class);
        startActivity(intent);
    }




    private void Modificar(int _id,
                           String _serie,
                           String _folio,
                           String _ruc,
                           String _razon_social,
                           String _direccion,
                           String _moneda,
                           String _correo) {
                          // String _fecha) {


        ContentValues valoresCabecera = new ContentValues();
        valoresCabecera.put(SERIE, _serie);
        valoresCabecera.put(FOLIO, _folio);
   //     valoresCabecera.put(FECHA, _fecha);
        valoresCabecera.put(RUC, _ruc);
        valoresCabecera.put(RAZON_SOCIAL, _razon_social);
        valoresCabecera.put(DIRECCION, _direccion);
        valoresCabecera.put(MONEDA, _moneda);
        valoresCabecera.put(CORREO, _correo);

        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Modificar.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }



    private void Modificar_Print (int _id,
                           String _serie,
                           String _folio,
                           String _ruc,
                           String _razon_social,
                           String _direccion,
                           String _moneda,
                           String _correo) {
        // String _fecha) {


        ContentValues valoresCabecera = new ContentValues();
        valoresCabecera.put(SERIE, _serie);
        valoresCabecera.put(FOLIO, _folio);
        //     valoresCabecera.put(FECHA, _fecha);
        valoresCabecera.put(RUC, _ruc);
        valoresCabecera.put(RAZON_SOCIAL, _razon_social);
        valoresCabecera.put(DIRECCION, _direccion);
        valoresCabecera.put(MONEDA, _moneda);
        valoresCabecera.put(CORREO, _correo);

        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Modificar.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }





    private void Eliminar(int _id) {



        db = new connectionDB(this);



        db.getWritableDatabase().delete(TABLE,TABLE_ID + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Modificar.this,"Se Elimino el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }


    private int sumar_Serie (String _serie) {


        db = new connectionDB(this);

        db = new connectionDB(this);
        Cursor c = db.getReg_num_Serie(_serie);
        int _folio_a_modificar = 0;

        if (c.moveToFirst()) {
            do {
                _folio_a_modificar = c.getInt(2);


            } while (c.moveToNext());
        }

            ContentValues valoresSerie = new ContentValues();
            valoresSerie.put("serie", _serie);
            valoresSerie.put("folio", _folio_a_modificar + 1);


            db.getWritableDatabase().update("series", valoresSerie, "serie" + "=?", new String[]{String.valueOf(_serie)});
       //     Toast.makeText(Modificar.this, "Se Asigno un Nuevo Folio:" + _serie, Toast.LENGTH_SHORT).show();

       //     Intent intent = new Intent(this, MainActivity_Series.class);
       //     startActivity(intent);

            return _folio_a_modificar + 1;


        }





}
