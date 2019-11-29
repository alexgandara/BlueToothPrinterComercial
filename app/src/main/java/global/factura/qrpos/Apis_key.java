package global.factura.qrpos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import zj.com.cn.bluetooth.sdk.R;

public class Apis_key extends Activity {

    EditText APIKEY_Doctos, APIKEY_Movtos, APIKEY_Almacen, APIKEY_Movimientos, APIKEY_Clientes, APIKEY_Productos, APIKEY_00, APIKEY_01, APIKEY_02;


    String _APIKEY_Doctos, _APIKEY_Movtos, _APIKEY_Almacen, _APIKEY_Movimientos, _APIKEY_Clientes, _APIKEY_Productos, _APIKEY_00, _APIKEY_01, _APIKEY_02;

    int _myId = 1;
    connectionDB db;
    Button Modificar, Salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apis_key);






        db = new connectionDB(this);
        Cursor cursor =  db.getReg_Apiskey(_myId);
        if (cursor.moveToFirst()) {
            do {



                APIKEY_Doctos = (EditText) findViewById(R.id.editText_ApiKey_Doctos );
                APIKEY_Movtos = (EditText) findViewById(R.id.editText_ApiKey_Movtos );
                APIKEY_Almacen = (EditText) findViewById(R.id.editText_ApiKey_Almacen );
                APIKEY_Movimientos = (EditText) findViewById(R.id.editText_ApiKey_Movimientos );
                APIKEY_Clientes = (EditText) findViewById(R.id.editText_ApiKey_Clientes );
                APIKEY_Productos = (EditText) findViewById(R.id.editText_ApiKey_Productos );
                APIKEY_00 = (EditText) findViewById(R.id.editText_ApiKey_00 );
                APIKEY_01 = (EditText) findViewById(R.id.editText_ApiKey_01 );
                APIKEY_02 = (EditText) findViewById(R.id.editText_ApiKey_02 );



//                String columnas[] = {"_id",, , , , , ,"api_key_00", "api_key_01", "api_key_02" };



                _APIKEY_Doctos = cursor.getString(1);       // "api_key_doctos" factura cabecera
                _APIKEY_Movtos = cursor.getString(2);       // "api_key_movtos" factura detalle
                _APIKEY_Productos = cursor.getString(3);    // "api_key_productos" productos
                _APIKEY_Clientes = cursor.getString(4);     // "api_key_clientes"  clientes
                _APIKEY_Almacen = cursor.getString(5);      // "api_key_almacen"   niveles de inventario en almancen
                _APIKEY_Movimientos = cursor.getString(6);  // "api_key_movimientos" movimientos de productos


                _APIKEY_00 = cursor.getString(7);
                _APIKEY_01 = cursor.getString(8);
                _APIKEY_02 = cursor.getString(9);







                APIKEY_Doctos.setText(_APIKEY_Doctos);
                APIKEY_Movtos.setText(_APIKEY_Movtos);
                APIKEY_Almacen.setText(_APIKEY_Almacen);
                APIKEY_Movimientos.setText(_APIKEY_Movimientos);
                APIKEY_Clientes.setText(_APIKEY_Clientes);
                APIKEY_Productos.setText(_APIKEY_Productos);
                APIKEY_00.setText(_APIKEY_00);
                APIKEY_01.setText(_APIKEY_01);
                APIKEY_02.setText(_APIKEY_02);

             //    Toast.makeText(this,"La Empresas fue Modificada", Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());
        }

        Modificar = (Button) findViewById(R.id.button_Modificar);
        Salir = (Button) findViewById(R.id.button_Salir);

        APIKEY_Doctos = (EditText) findViewById(R.id.editText_ApiKey_Doctos);
        APIKEY_Movtos = (EditText) findViewById(R.id.editText_ApiKey_Movtos);
        APIKEY_Almacen = (EditText) findViewById(R.id.editText_ApiKey_Almacen);
        APIKEY_Movimientos = (EditText) findViewById(R.id.editText_ApiKey_Movimientos);
        APIKEY_Clientes = (EditText) findViewById(R.id.editText_ApiKey_Clientes);
        APIKEY_Productos = (EditText) findViewById(R.id.editText_ApiKey_Productos);
        APIKEY_00 = (EditText) findViewById(R.id.editText_ApiKey_00);
        APIKEY_01 = (EditText) findViewById(R.id.editText_ApiKey_01);
        APIKEY_02 = (EditText) findViewById(R.id.editText_ApiKey_02);


        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar_ApisKey(_myId,
                        APIKEY_Doctos.getText().toString(),
                        APIKEY_Movtos.getText().toString(),
                        APIKEY_Almacen.getText().toString(),
                        APIKEY_Movimientos.getText().toString(),
                        APIKEY_Clientes.getText().toString(),
                        APIKEY_Productos.getText().toString(),
                        APIKEY_00.getText().toString(),
                        APIKEY_01.getText().toString(),
                        APIKEY_02.getText().toString());}

           //             base01.getText().toString(),
           //             base02.getText().toString(),
           //             base03.getText().toString(),
           //             base04.getText().toString(),
           //             base05.getText().toString(),
           //             base06.getText().toString());}
        });





        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Apis_key.this,Empresas.class );
                startActivity(intent);

            }
        });




    }





    private void Modificar_ApisKey(int _id,
                           String _APIKEY_Doctos,
                           String _APIKEY_Movtos,
                           String _APIKEY_Almacen,
                           String _APIKEY_Movimientos,
                           String _APIKEY_Clientes,
                           String _APIKEY_Productos,
                           String _APIKEY_00,
                           String _APIKEY_01,
                           String _APIKEY_02

    ) {





        ContentValues valoresApiskey = new ContentValues();


        valoresApiskey.put("api_key_doctos", _APIKEY_Doctos);
        valoresApiskey.put("api_key_movtos", _APIKEY_Movtos);
        valoresApiskey.put("api_key_productos", _APIKEY_Productos);
        valoresApiskey.put("api_key_clientes", _APIKEY_Clientes);
        valoresApiskey.put("api_key_almacen", _APIKEY_Almacen);
        valoresApiskey.put("api_key_movimientos", _APIKEY_Movimientos);
        valoresApiskey.put("api_key_00", _APIKEY_00);
        valoresApiskey.put("api_key_01", _APIKEY_01);
        valoresApiskey.put("api_key_02", _APIKEY_02);



       db = new connectionDB(this);



        db.getWritableDatabase().update("empresa", valoresApiskey, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }







}
