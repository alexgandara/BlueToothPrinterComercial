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
 * Created by Alejandro on 3/19/2018.
 */

public class addDatos_Productos extends Activity {

    EditText editText_Producto, editText_Descripcion_Producto, editText_Unidad, editText_Precio, editText_Precio_Mayoreo, editText_igv;

    Spinner spinner_Unidedes;

    List<String> _lista_unidades = null;

    Button Agregar, Salir;

    public String _Producto;
    public String _Descripcion_Producto;
    public String _Unidad;
    public String _Precio;
    public String _Precio_Mayoreo;
    public String _igv;




    connectionDB db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertar_productos_qr);

        Agregar = (Button) findViewById(R.id.button_Agregar);
        Salir = (Button) findViewById(R.id.button_Salir);

        spinner_Unidedes = (Spinner) findViewById(R.id.spinner_Unidad);

        editText_Producto = (EditText) findViewById(R.id.editText_Producto);
        editText_Descripcion_Producto = (EditText) findViewById(R.id.editText_Descripcion_Producto);
        editText_Unidad = (EditText) findViewById(R.id.editText_Unidad);
        editText_Precio = (EditText) findViewById(R.id.editText_Precio);
        editText_Precio_Mayoreo = (EditText) findViewById(R.id.editText_Precio_Mayoreo);
        editText_igv = (EditText) findViewById(R.id.editText_igv);


        // llenar datos del spiner de precios
        showNotes_unidades();
        ArrayAdapter<String> adapter_unidad = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_dropdown_item, _lista_unidades);
        spinner_Unidedes.setAdapter(adapter_unidad);
        spinner_Unidedes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                String _unidad = _lista_unidades.get(i).split("/")[0];
                editText_Unidad.setText(_unidad);
           }
            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {
            }
        });



        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatos_Productos();


            }
        });


        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _Salir();
            }
        });


    }

        private void _Salir() {
            Intent intent = new Intent(this, MainActivity_Productos.class);
            startActivity(intent);
        }

        private void addDatos_Productos() {


        _Producto               = editText_Producto.getText().toString();
        _Descripcion_Producto   = editText_Descripcion_Producto.getText().toString();
        _Unidad                 = editText_Unidad.getText().toString();
        _Precio                 = editText_Precio.getText().toString();
        _Precio_Mayoreo         = editText_Precio_Mayoreo.getText().toString();
        _igv                    = editText_igv.getText().toString();



        db = new connectionDB(this);
        db.addNotes_Productos(_Producto,_Descripcion_Producto, _Unidad, _Precio, _Precio_Mayoreo, _igv,"");
        //    db.close();
        Intent intent = new Intent(this,MainActivity_Productos.class );
        startActivity(intent);

    }

    private void showNotes_unidades() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_unidades();
        _lista_unidades = new ArrayList<String>();
        String _unidad="",  _descripcion_unidad="";


        // {TABLE_ID,ID_PRODUCTO, DESCRIPCION_PRODUCTO, UNIDAD, PRECIO_PRODUCTO, IGV_PRODUCTO};

        int _id;

        if (c.moveToFirst()) {
            do {
                _id = c.getInt(0);
                _unidad = c.getString(1);
                _descripcion_unidad = c.getString(2);
                _lista_unidades.add(_unidad+"/"+
                        _descripcion_unidad);

            } while (c.moveToNext());

        }

        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _Salir();


            }
        });


    }







}
