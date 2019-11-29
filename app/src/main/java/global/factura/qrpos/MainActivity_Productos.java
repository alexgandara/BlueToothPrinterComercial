package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;

public class MainActivity_Productos extends Activity {

    ListView lista;
    connectionDB db;
    List<String> item = null;

    Button Agregar_Productos, Salir;

    // variables para table productos
    public static final String ID_PRODUCTO = "producto";
    public static final String DESCRIPCION_PRODUCTO = "descripcion_producto";
    public static final String PRECIO_PRODUCTO = "precio";
    public static final String PRECIO_PRODUCTO_MAYOREO = "precio_mayoreo";
    public static final String GRAVADO_PRODUCTO = "gravado";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos_qr);

        lista = (ListView) findViewById(R.id.listView_Productos);
        Agregar_Productos = (Button) findViewById(R.id.Button_Agregar_Productos);
        Salir = (Button) findViewById(R.id.Button_Salir);




        showNotes_Productos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(MainActivity.this,"Posicion:"+i, Toast.LENGTH_SHORT ).show();
                int _id= Integer.parseInt(item.get(i).split(" ")[0]);
                // Toast.makeText(MainActivity_Series.this,"Transaccion:"+_id, Toast.LENGTH_SHORT ).show();
                Intent intent = new Intent(MainActivity_Productos.this,Productos.class);
                intent.putExtra("id",_id);
                startActivity(intent);


            }
        });




        Agregar_Productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Productos.this, addDatos_Productos.class);
                startActivity(intent);
                //  return true;

            }
        });



        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Productos.this, init_alfilPOS.class);
                startActivity(intent);
                //  return true;

            }
        });



 /*       if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        } */

    }


    private void showNotes_Productos() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_productos();
        item = new ArrayList<String>();
        String _producto="",  _descripcion_producto="", _unidad="", _id_velneo="";
        double _existencia=0;
        double _ventas=0;
        double _saldo=0;



        int _id, folio;

        if (c.moveToFirst()) {
            do {
                _id = c.getInt(0);
                _producto = c.getString(1);
                _descripcion_producto = c.getString(2);
                _unidad = c.getString(3);
                _existencia= c.getDouble(7);
                _ventas= c.getDouble(8);
                _saldo= c.getDouble(9);
                _id_velneo= c.getString(10);


                item.add(_id+" - "+_producto+" - "+_descripcion_producto+" - Exis:"+_existencia+" Vtas:"+_ventas+" Sdo:"+_saldo+" ID:"+_id_velneo);
            } while (c.moveToNext());

        }

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,item);
        lista.setAdapter(adaptador);

    }









}
