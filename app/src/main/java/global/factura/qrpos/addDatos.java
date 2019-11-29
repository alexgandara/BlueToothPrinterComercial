package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;


/**
 * Created by Alejandro on 3/12/2018.
    alex alex 
 */

public class addDatos extends Activity {
    EditText serie, folio, ruc, razon_social, direccion, moneda, correo;
    Button Add;
    String _serie, _folio, _ruc, _razon_social, _direccion, _moneda, _correo;
    connectionDB db;

    List<String> _lista_series = null;
    List<String> _lista_clientes = null;

    Spinner spinner_Series, spinner_Clientes ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the window layout
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.inserta_datos);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.custom_title);


           Add = (Button) findViewById(R.id.button_Agregar);
           serie = (EditText) findViewById(R.id.editText_Serie);
           folio = (EditText) findViewById(R.id.editText_Folio);
           ruc = (EditText) findViewById(R.id.editText_Ruc);
           razon_social = (EditText) findViewById(R.id.editText_RazonSocial);
           direccion = (EditText) findViewById(R.id.editText_Direccion);
           correo = (EditText) findViewById(R.id.editText_Correo);
           moneda = (EditText) findViewById(R.id.editText_Moneda);


            spinner_Series = (Spinner) findViewById(R.id.spinner_Series);

            spinner_Clientes = (Spinner) findViewById(R.id.spinner_Clientes);




        // llenar datos del spiner de series
        showNotes_Series();
        ArrayAdapter<String> adapter_series = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_dropdown_item, _lista_series);
        spinner_Series.setAdapter(adapter_series);


        spinner_Series.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                String _serie_elegida = _lista_series.get(i).split("/")[0];
                serie.setText(_serie_elegida);
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });
        /// termian spinig de series





        // llenar datos del spiner de clientes
        showNotes_Clientes();
        ArrayAdapter<String> adapter_clientes = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_dropdown_item, _lista_clientes);
        spinner_Clientes.setAdapter(adapter_clientes);
        spinner_Clientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                String _ruc_elegida = _lista_clientes.get(i).split("/")[0];
                String _razon_social_elegida = _lista_clientes.get(i).split("/")[1];
                String _direccion_elegida = _lista_clientes.get(i).split("/")[2];
                String _correo_elegida = _lista_clientes.get(i).split("/")[3];
                ruc.setText(_ruc_elegida);
                razon_social.setText(_razon_social_elegida);
                direccion.setText(_direccion_elegida);
                correo.setText(_correo_elegida);



            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });









        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatos();


            }
        });

    }


    private void addDatos() {

        _serie        = serie.getText().toString();
        _folio        = "0";
        _ruc          = ruc.getText().toString();
        _razon_social = razon_social.getText().toString();
        _direccion    = direccion.getText().toString();
        _correo       = correo.getText().toString();
        _moneda       = moneda.getText().toString();



        db = new connectionDB(this);
        db.addNotes(_serie, _folio, _ruc,_razon_social,_direccion,_moneda, _correo);
        //    db.close();
        Intent intent = new Intent(this,init_alfilPOS.class );
     //   Intent intent = new Intent(this,Modificar2.class );
        startActivity(intent);

    }



    private void showNotes_Series() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_series();
        _lista_series = new ArrayList<String>();
        String _serie="", _naturaleza="";




        // {TABLE_ID,ID_PRODUCTO, DESCRIPCION_PRODUCTO, UNIDAD, PRECIO_PRODUCTO, IGV_PRODUCTO};

        int _id, _folio;
        //   _lista_productos.add("0"+"/"+
        //                        "     "+"/"+
        //                        "     "+"/"+
        //                        "     "+"/"+
        //                        "     "+"/"+
        //                       "     "+"/"+
        //                      "     "+"/");

        if (c.moveToFirst()) {
            do {
                _id = c.getInt(0);
                _serie = c.getString(1);
                _folio = c.getInt(2);
                _naturaleza = c.getString(3);

                if (_naturaleza.equals("01")) {
                    _lista_series.add(_serie+"/ "+
                            _naturaleza+"  /  "+_folio);
                }

                if (_naturaleza.equals("03")) {
                    _lista_series.add(_serie+"/ "+
                            _naturaleza+"  /  "+_folio);
                }





            } while (c.moveToNext());

        }



    }





    private void showNotes_Clientes() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_clientes();
        _lista_clientes = new ArrayList<String>();
        String _cliente="", _razon_social="", _direccion="",_correo="", _telefono="";




        //    public static final String DIRECCION_CLIENTE = "direccion_cliente";
//    public static final String CORREO_CLIENTE = "correo_cliente";
//    public static final String TELEFONO_CLIENTE = "telefono_cliente";




        int _id, _folio;

        if (c.moveToFirst()) {
            do {
                _id = c.getInt(0);
                _ruc = c.getString(1);
                _razon_social = c.getString(2);
                _direccion = c.getString(3);
                _correo = c.getString(4);

               // _naturaleza = c.getString(3);

                if (_razon_social.trim().equals("")) {
                    _razon_social="---";

                }


                if (_direccion.trim().equals("")) {
                    _direccion="---";

                }


                if (_correo.trim().equals("")) {
                    _correo="---";

                }



                    _lista_clientes.add(_ruc+"/"+
                    _razon_social+"/"+
                    _direccion+"/"+_correo
                    );





            } while (c.moveToNext());

        }

/*        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _Salir();


            }
        });
*/

    }







}
