package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
 */

public class addDatos_Notas extends Activity {
    EditText serie, folio, ruc, razon_social, direccion, moneda, serie_rel, folio_rel;
    Button Add;
    Spinner spinner_Series, spinner_Clientes ;

    String _serie, _folio, _ruc, _razon_social, _direccion, _moneda, _serie_rel, _folio_rel;
    connectionDB db;


    List<String> _lista_series = null;
    List<String> _lista_clientes = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserta_datos_notas);

        Add = (Button) findViewById(R.id.button_Agregar);
        serie = (EditText) findViewById(R.id.editText_Serie);
        folio = (EditText) findViewById(R.id.editText_Folio);
        serie_rel = (EditText) findViewById(R.id.editText_Serie_Rel);
        folio_rel  = (EditText) findViewById(R.id.editText_Folio_Rel);

        ruc = (EditText) findViewById(R.id.editText_Ruc);
        razon_social = (EditText) findViewById(R.id.editText_RazonSocial);
        direccion = (EditText) findViewById(R.id.editText_Direccion);
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
//                correo.setText(_correo_elegida);



            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });








        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatos_NC();


            }
        });

    }


    private void addDatos_NC() {

        _serie        = serie.getText().toString();
        _folio        = folio.getText().toString();
        _ruc          = ruc.getText().toString();
        _razon_social = razon_social.getText().toString();
        _direccion    = direccion.getText().toString();
        _moneda       = moneda.getText().toString();
        _serie_rel        = serie_rel.getText().toString();
        _folio_rel        = folio_rel.getText().toString();



        db = new connectionDB(this);
        db.addNotes_Notas(_serie,_folio, _ruc,_razon_social,_direccion,_moneda, _serie_rel, _folio_rel);
        //    db.close();
        Intent intent = new Intent(this,init_alfilPOS.class );
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

                if (_naturaleza.equals("07")) {
                    _lista_series.add(_serie+"/ "+
                            _naturaleza+"  /  "+_folio);
                }

                if (_naturaleza.equals("08")) {
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
