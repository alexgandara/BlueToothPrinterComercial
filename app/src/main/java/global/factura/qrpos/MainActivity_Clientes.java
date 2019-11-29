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

public class MainActivity_Clientes extends Activity {

    ListView lista;
    connectionDB db;
    List<String> item = null;

    Button Agregar_Clientes, Salir;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clientes_qr);

        lista = (ListView) findViewById(R.id.listView_Clientes);
        Agregar_Clientes = (Button) findViewById(R.id.Button_Agregar_Clientes);
        Salir = (Button) findViewById(R.id.Button_Salir);




        showNotes_Clientes();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(MainActivity.this,"Posicion:"+i, Toast.LENGTH_SHORT ).show();
                int _id= Integer.parseInt(item.get(i).split(" ")[0]);
                // Toast.makeText(MainActivity_Series.this,"Transaccion:"+_id, Toast.LENGTH_SHORT ).show();
                Intent intent = new Intent(MainActivity_Clientes.this,Clientes.class);
                intent.putExtra("id",_id);
                startActivity(intent);


            }
        });




        Agregar_Clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Clientes.this, addDatos_Clientes.class);
                startActivity(intent);


            }
        });



        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Clientes.this, init_alfilPOS.class);
                startActivity(intent);
                //  return true;

            }
        });



    }


    private void showNotes_Clientes() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_clientes();
        item = new ArrayList<String>();
        String _cliente="",  _razon_social="";

        int _id, folio;

        if (c.moveToFirst()) {
            do {
                _id = c.getInt(0);
                _cliente = c.getString(1);
                _razon_social = c.getString(2);


                item.add(_id+" "+_cliente+" "+_razon_social);
            } while (c.moveToNext());

        }

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,item);
        lista.setAdapter(adaptador);

    }









}
