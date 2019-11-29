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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;

/**
 * Created by Alejandro on 3/15/2018.
 */

public class addDatos_Detalle extends Activity {

    EditText producto, descripcion, unidad, cantidad, precio, igv;
    Spinner spinner_Productos, spinner_Unidad, spinner_Precios;


    List<String> _lista_productos = null;
    List<String> _lista_precios = null;

    //String[] _lista_precios = {"menudeo", "mayoreo"};


    Button Add, Salir;
    String _producto, _descripcion, _unidad,_cantidad, _precio, _igv;


    String _producto_seleccionado;
    String _descripcion_seleccionado;
    String _precio_seleccionado;
    String _precio__mayoreo_seleccionado;
    String _igv_seleccionado;
    String _unidad_seleccionado;

    connectionDB db;
    int _myId;

    public int    Mid;
    public int    Mcabecera_id;
    public String Mproducto;
    public String Mdescripcion;
    public String Munidad;
    public Double Mcantidad;
    public Double Mprecio;
    public Double Migv;
    public boolean _primera = true;

    public String _PRECIO_SELECCIONADO, _PRECIO_MAYOREO_SELECCIONADO;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserta_detalle);








        Bundle b = getIntent().getExtras();
        if (b!=null) {
            _myId = b.getInt("id");

        }

        Add = (Button) findViewById(R.id.button_Agregar);
        Salir = (Button) findViewById(R.id.button_Salir);

        spinner_Productos = (Spinner) findViewById(R.id.spinner_Productos);
        producto = (EditText) findViewById(R.id.editText_Producto);
        descripcion = (EditText) findViewById(R.id.editText_Descripcion);
        unidad = (EditText) findViewById(R.id.editText_Unidad);
        cantidad = (EditText) findViewById(R.id.editText_Cantidad);
        spinner_Precios = (Spinner) findViewById(R.id.spinner_Precio);
        precio = (EditText) findViewById(R.id.editText_Precio);
        igv = (EditText) findViewById(R.id.editText_Igv);


        // llenar datos del spiner de productos
        showNotes_Productos();
        ArrayAdapter<String> adapter_productos = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_dropdown_item , _lista_productos);
        spinner_Productos.setAdapter(adapter_productos);

        spinner_Productos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int index, long l) {
                //    if (_primera) {
                //         _primera=false;
                //     } else {
                String _producto = _lista_productos.get(index).split("/")[0];
                //  String _producto_seleccionado = _lista_productos.get(index).split("/")[1];
                //  String _descripcion_seleccionado = _lista_productos.get(index).split("/")[2];
                //  String _unidad_seleccionado = _lista_productos.get(index).split("/")[3];
                //  String _precio_seleccionado = _lista_productos.get(index).split("/")[4];
                //  String _precio__mayoreo_seleccionado = _lista_productos.get(index).split("/")[5];
                //  String _igv_seleccionado = _lista_productos.get(index).split("/")[6];

                //  _PRECIO_SELECCIONADO=_precio_seleccionado;
                //  _PRECIO_MAYOREO_SELECCIONADO=_precio__mayoreo_seleccionado;

                Toast.makeText(addDatos_Detalle.this,_producto, Toast.LENGTH_SHORT).show();

                Cursor cursor =  db.getReg_Productos_codigo(_producto);
                if (cursor.moveToFirst()) {
                    do {



                        _producto_seleccionado = cursor.getString(1);
                        _descripcion_seleccionado = cursor.getString(2);
                        _precio_seleccionado = String.valueOf(cursor.getDouble (3));
                        _precio__mayoreo_seleccionado = String.valueOf(cursor.getDouble (4));
                        _igv_seleccionado = String.valueOf(cursor.getDouble(5));
                        _unidad_seleccionado = cursor.getString(6);



                        producto.setText(_producto_seleccionado);
                        descripcion.setText(_descripcion_seleccionado);
                        unidad.setText(_unidad_seleccionado);
                        precio.setText(_precio_seleccionado);
                        cantidad.setText("1");
                        igv.setText(_igv_seleccionado);


                        // Toast.makeText(addDatos_Detalle.this,_id+", "+_producto, Toast.LENGTH_SHORT).show();

                    } while (cursor.moveToNext());

                }











                /*             Cursor cursor = db.getReg_Detalle(_id);
                    if (cursor.moveToFirst()) {
                        do {

                            //     producto = (EditText) findViewById(R.id.editText_Producto);
                            //     descripcion = (EditText) findViewById(R.id.editText_Descripcion);
                            //     unidad = (EditText) findViewById(R.id.editText_Unidad);
                            //      editText_Cantidad = (EditText) findViewById(R.id.editText_Cantidad);
                            //     precio = (EditText) findViewById(R.id.editText_Precio);


                            Mcabecera_id = cursor.getInt(1);
                            Mproducto = cursor.getString(2);
                            Mdescripcion = cursor.getString(3);
                            Munidad = cursor.getString(4);
                            Mcantidad = cursor.getDouble(5);
                            Mprecio = cursor.getDouble(6);
                            Migv = cursor.getDouble(7);

                            producto.setText(Mproducto);
                            descripcion.setText(Mdescripcion);
                            unidad.setText(Munidad);
                            //        cantidad.setText(Mcantidad.toString());
                            precio.setText(Mprecio.toString());
                            igv.setText(Migv.toString());

                            //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

                        } while (cursor.moveToNext());


                    }*/


                //      Toast.makeText(addDatos_Detalle.this, "Transaccion:" + _id + " " + _producto_seleccionado+" "+_unidad_seleccionado, Toast.LENGTH_SHORT).show();
                //       }
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });

        // llenar datos del spiner de precios
        showNotes_precios();
        ArrayAdapter<String> adapter_precios = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_dropdown_item, _lista_precios);
        spinner_Precios.setAdapter(adapter_precios);


        spinner_Precios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                int _id_precios = Integer.parseInt(_lista_precios.get(i).split(" ")[0]);
                if (_id_precios==1) {
                    precio.setText(_precio_seleccionado);
                }

                if (_id_precios==2) {

                    precio.setText(_precio__mayoreo_seleccionado);


                }



            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatos_Detalle();


            }
        });




    }


    private void addDatos_Detalle() {

        _producto        = producto.getText().toString();
        _descripcion     = descripcion.getText().toString();
        _unidad          = unidad.getText().toString();
        _cantidad        = cantidad.getText().toString();
        _precio          = precio.getText().toString();
        _igv             = igv.getText().toString();

        db = new connectionDB(this);
        db.addNotes_Detalle(_myId, _producto,_descripcion, _unidad, _cantidad, _precio, _igv);
        //    db.close();
        Intent intent = new Intent(this,MainActivity_Detalle.class );
        intent.putExtra("id",_myId);
        startActivity(intent);

    }

    private void showNotes_Productos() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_productos();
        _lista_productos = new ArrayList<String>();
        String _producto="",  _descripcion_producto="", _unidad_producto, _precio, _precio_mayoreo, _igv;
        double _existencia=0;
        double _ventas=0;
        double _saldo=0;





        // {TABLE_ID,ID_PRODUCTO, DESCRIPCION_PRODUCTO, UNIDAD, PRECIO_PRODUCTO, IGV_PRODUCTO};

        int _id, folio;
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
                _producto = c.getString(1);
                _descripcion_producto = c.getString(2);
                _unidad_producto = c.getString(3);
                _precio = c.getString(4);
                _precio_mayoreo = c.getString(5);
                _igv = c.getString(6);
                _existencia=c.getDouble(7);
                _ventas=c.getDouble(8);

                _saldo=_existencia-_ventas;


                if (_saldo!=0) {
                    _lista_productos.add(_producto+"/"+
                            _descripcion_producto+"/"+
                            _saldo+"/"+
                            _precio+"/"+
                            _precio_mayoreo+"/"+
                            _igv);

                }


            } while (c.moveToNext());

        }

        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _Salir();


            }
        });


    }




    private void showNotes_precios() {
        _lista_precios = new ArrayList<String>();
        _lista_precios.add("1 Menudeo");
        _lista_precios.add("2 Mayoreo");
    }


    private void _Salir() {
        Intent intent = new Intent(this, init_alfilPOS.class);
        startActivity(intent);
    }





}
