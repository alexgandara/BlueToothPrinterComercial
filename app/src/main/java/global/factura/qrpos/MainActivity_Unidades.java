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

public class MainActivity_Unidades extends Activity {

    ListView lista;
    connectionDB db;
    List<String> item = null;



    public static final String ID_UNIDAD = "unidad";
    public static final String DESCRIPCION_UNIDAD = "descripcion_unidad";

    Button Agregar_Unidades, Salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unidades_qr);



        lista = (ListView) findViewById(R.id.listView_Unidades);

        Agregar_Unidades = (Button) findViewById(R.id.Button_Agregar_Unidades);
        Salir = (Button) findViewById(R.id.Button_Salir);

        showNotes_Unidades();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(MainActivity.this,"Posicion:"+i, Toast.LENGTH_SHORT ).show();
                int _id= Integer.parseInt(item.get(i).split(" ")[0]);
                // Toast.makeText(MainActivity_Series.this,"Transaccion:"+_id, Toast.LENGTH_SHORT ).show();
                Intent intent = new Intent(MainActivity_Unidades.this,Unidades.class);
                intent.putExtra("id",_id);
                startActivity(intent);




            }
        });



        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Unidades.this, init_alfilPOS.class);
                startActivity(intent);
                //  return true;

            }
        });



        Agregar_Unidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Unidades.this, addDatos_Unidades.class);
                startActivity(intent);
                //  return true;

            }
        });



        //       if (getSupportActionBar()!=null) {
 //           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
 //           getSupportActionBar().setDisplayShowHomeEnabled(true);


  //      }

    }


    private void showNotes_Unidades() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_unidades();
        item = new ArrayList<String>();
        String _unidad="",  _descripcion_unidad="";

        int _id;

        if (c.moveToFirst()) {
            do {
                _id = c.getInt(0);
                _unidad = c.getString(1);
                _descripcion_unidad = c.getString(2);


                item.add(_id+" - "+_unidad+" - "+_descripcion_unidad);
            } while (c.moveToNext());

        }

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,item);
        lista.setAdapter(adaptador);

    }





}
