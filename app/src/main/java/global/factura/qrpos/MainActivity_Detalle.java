package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;

public class MainActivity_Detalle extends Activity {

    ListView lista;
    connectionDB db;
    List<String> item = null;

    Button Agregar_Detalle, Ir_a_Documentos, Cierre_Caja, Sincronizar;

    int _myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detalle);
        Bundle b = getIntent().getExtras();
        if (b!=null) {
            _myId = b.getInt("id");

        }


        lista = (ListView) findViewById(R.id.listView_Lista_Detalle);
        showNotes_detalle(_myId);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int _id= Integer.parseInt(item.get(i).split(" ")[0]);

                Intent intent = new Intent(MainActivity_Detalle.this,Detalle.class);
                intent.putExtra("id",_id);
                startActivity(intent);
            }
        });





        Agregar_Detalle = (Button) findViewById(R.id.Button_Agregar_Detalle);
        Ir_a_Documentos = (Button) findViewById(R.id.Button_Ir_a_Documentos);
    //    Cierre_Caja = (Button) findViewById(R.id.Button_Cierre  );
        Sincronizar = (Button) findViewById(R.id.Button_Sincronizar);




        Agregar_Detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Detalle.this, addDatos_Detalle.class);
                intent.putExtra("id",_myId);
                startActivity(intent);
             //   return true;

            }
        });




        Ir_a_Documentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Detalle.this, init_alfilPOS.class);
                startActivity(intent);
                //  return true;

            }
        });


        //

    


/*
        Intent intent = new Intent(MainActivity_Detalle.this, addDatos_Detalle.class);
        intent.putExtra("id",_myId);
        startActivity(intent);
        */
    }

    private void showNotes_detalle(int _id) {
        db = new connectionDB(this);
        Cursor c = db.getNotes_detalle(_id);
        item = new ArrayList<String>();
        String producto = "",  descripcion="", unidad="";
        int id, folio, linea;
        double precio, cantidad;


        if (c.moveToFirst()) {
            do {
                id = c.getInt(0);
                producto = c.getString(1);
                descripcion = c.getString(2);
                unidad = c.getString(3);
                cantidad = c.getDouble(4);
                precio = c.getDouble(5);
                linea = c.getInt(7);

                item.add(id+" - "+producto+" - "+descripcion+" - "+unidad+" - "+cantidad+" "+precio+" "+linea);


            } while (c.moveToNext());

        }


        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,item);
        lista.setAdapter(adaptador);



    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu ) {
        getMenuInflater().inflate(R.menu.menu_detalle, menu );
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

  /*      if (id == R.id.addDatos_Detalle) {

            Intent intent = new Intent(MainActivity_Detalle.this, addDatos_Detalle.class);
            intent.putExtra("id",_myId);
            startActivity(intent);
            return true;

        } */

  //      if (id == R.id.Cabecera) {

  //          Intent intent = new Intent(MainActivity_Detalle.this, Modificar.class);
  //          startActivity(intent);
  //          return true;

  //      }

        if (id == R.id.Salir) {

            Intent intent = new Intent(MainActivity_Detalle.this, init_alfilPOS.class);
            startActivity(intent);
            return true;

        }


        if (item.getItemId()==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }


}
