package global.factura.qrpos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;

/**
 * Created by Alejandro on 3/19/2018.
 */

public class Productos extends Activity {
    // variables para table productos
    public static final String ID_PRODUCTO = "producto";
    public static final String DESCRIPCION_PRODUCTO = "descripcion_producto";
    public static final String PRECIO_PRODUCTO = "precio";
    public static final String PRECIO_PRODUCTO_MAYOREO = "precio_mayoreo";
    public static final String IGV_PRODUCTO = "igv";
    public static final String UNIDAD_PRODUCTO = "unidad";
    public static final String EXISTENCIA = "existencia";
    public static final String VENTAS = "ventas_pro";
    public static final String SALDO = "saldo_pro";


    private static final String TABLE_PRODUCTOS = "productos";

    public  int _cabecera_id;
    public String Mproducto;
    public String Mdescripcion_producto;
    public String Munidad;
    public Double Mprecio;
    public Double Mprecio_Mayoreo;
    public Double Migv;
    public Double Mexistencia;
    public Double Mventas_pro;
    public Double Msaldo_pro;





    EditText editText_Producto, editText_Descripcion_Producto, editText_Precio, editText_Precio_Mayoreo, editText_igv, editText_Unidad,
            editText_existencia;

    Spinner spinner_Unidedes;

    List<String> _lista_unidades = null;



    int _myId;
    connectionDB db;
    Button Modificar, Eliminar, Salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_qr);

        Bundle b = getIntent().getExtras();
        if (b!=null) {
            _myId = b.getInt("id");

        }

        db = new connectionDB(this);
        Cursor cursor =  db.getReg_Productos(_myId);
        if (cursor.moveToFirst()) {
            do {

                editText_Producto = (EditText) findViewById(R.id.editText_Producto);
                editText_Descripcion_Producto = (EditText) findViewById(R.id.editText_Descripcion_Producto);
                editText_Precio = (EditText) findViewById(R.id.editText_Precio);
                editText_Precio_Mayoreo = (EditText) findViewById(R.id.editText_Precio_Mayoreo);
                editText_igv = (EditText) findViewById(R.id.editText_igv);
                editText_Unidad = (EditText) findViewById(R.id.editText_Unidad);
                editText_existencia = (EditText) findViewById(R.id.editText_existencia);

                spinner_Unidedes = (Spinner) findViewById(R.id.spinner_Unidades);

                Mproducto = cursor.getString(1);
                Mdescripcion_producto = cursor.getString(2);
                Mprecio = cursor.getDouble (3);
                Mprecio_Mayoreo = cursor.getDouble (4);
                Migv = cursor.getDouble(5);
                Munidad = cursor.getString(6);
                Mexistencia = cursor.getDouble(7);
                Mventas_pro = cursor.getDouble(8);
                Msaldo_pro = cursor.getDouble(9);



                editText_Producto.setText(Mproducto);
                editText_Descripcion_Producto.setText(Mdescripcion_producto);
                editText_Precio.setText(Mprecio.toString());
                editText_Precio_Mayoreo.setText(Mprecio_Mayoreo.toString());
                editText_igv.setText(Migv.toString());
                editText_Unidad.setText(Munidad.toString());
                editText_existencia.setText(Mexistencia.toString());

                //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

        }


        Modificar = (Button) findViewById(R.id.button_Modificar);
        Eliminar = (Button) findViewById(R.id.button_Eliminar);
        Salir = (Button) findViewById(R.id.button_Salir);

        editText_Producto = (EditText) findViewById(R.id.editText_Producto);
        editText_Descripcion_Producto = (EditText) findViewById(R.id.editText_Descripcion_Producto);
        editText_Precio = (EditText) findViewById(R.id.editText_Precio);
        editText_Precio_Mayoreo = (EditText) findViewById(R.id.editText_Precio_Mayoreo);
        editText_igv = (EditText) findViewById(R.id.editText_igv);
        editText_existencia = (EditText) findViewById(R.id.editText_existencia);

    /*    // llenar datos del spiner de precios
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


*/


        Msaldo_pro=(Double.parseDouble(editText_existencia.getText().toString())-Mventas_pro);


        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar(_myId,
                        editText_Producto.getText().toString(),
                        editText_Descripcion_Producto.getText().toString(),
                        editText_Precio.getText().toString(),
                        editText_Precio_Mayoreo.getText().toString(),
                        editText_igv.getText().toString(),
                        editText_Unidad.getText().toString(),
                        editText_existencia.getText().toString(),
                        Mventas_pro.toString(),
                        Msaldo_pro.toString()
                        );



            }
        });


        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar(_myId);

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

    private void Modificar(int _id,
                           String _editText_Producto,
                           String _editText_Descripcion_Producto,
                           String _editText_Precio,
                           String _editText_Precio_Mayoreo,
                           String _editText_igv,
                           String _editText_unidad,
                           String _editText_existencia,
                           String _editText_ventas_pro,
                           String _editText_saldo_pro
                           ) {

        ContentValues valoresProductos = new ContentValues();
        valoresProductos.put(ID_PRODUCTO, _editText_Producto);
        valoresProductos.put(DESCRIPCION_PRODUCTO, _editText_Descripcion_Producto);
        valoresProductos.put(PRECIO_PRODUCTO, _editText_Precio);
        valoresProductos.put(PRECIO_PRODUCTO_MAYOREO, _editText_Precio_Mayoreo);
        valoresProductos.put(IGV_PRODUCTO, _editText_igv);
        valoresProductos.put(UNIDAD_PRODUCTO, _editText_unidad);
        valoresProductos.put(EXISTENCIA, _editText_existencia);
        valoresProductos.put(VENTAS, _editText_ventas_pro);
        valoresProductos.put(SALDO, _editText_saldo_pro);



        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update("productos", valoresProductos, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Productos.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,MainActivity_Productos.class );
        startActivity(intent);

    }

    private void Eliminar(int _id) {



        db = new connectionDB(this);



        db.getWritableDatabase().delete("productos","_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Productos.this,"Se Elimino el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

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
