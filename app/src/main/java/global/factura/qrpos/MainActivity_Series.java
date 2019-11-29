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

public class MainActivity_Series extends Activity {

    ListView lista;
    connectionDB db;
    List<String> item = null;

    Button Agregar, Salir;


    public static final String ID    = "id";
    public static final String SERIE = "serie";
    public static final String FOLIO = "folio";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_series_qr);


        lista = (ListView) findViewById(R.id.listView_Series);
        Agregar = (Button) findViewById(R.id.Button_Agregar_Series);
        Salir = (Button) findViewById(R.id.Button_Salir);





        showNotes_Series();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(MainActivity.this,"Posicion:"+i, Toast.LENGTH_SHORT ).show();
                int _id= Integer.parseInt(item.get(i).split(" ")[0]);
               // Toast.makeText(MainActivity_Series.this,"Transaccion:"+_id, Toast.LENGTH_SHORT ).show();
                Intent intent = new Intent(MainActivity_Series.this,Series.class);
                intent.putExtra("id",_id);
                startActivity(intent);




            }
        });



        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Series.this, addDatos_Series.class);

                startActivity(intent);

            }
        });


        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Series.this,init_alfilPOS.class);
                startActivity(intent);

            }
        });






        /*       if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }*/


    }




    private void showNotes_Series() {


        db = new connectionDB(this);
        Cursor c = db.getNotes_series();
        item = new ArrayList<String>();
        String _id="", _serie="",  _folio="";
        int id, folio;

        if (c.moveToFirst()) {
            do {
                id = c.getInt(0);
                _serie = c.getString(1);
                folio = c.getInt(2);

                item.add(id+" - "+_serie+" - "+folio);
            } while (c.moveToNext());

        }

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,item);
        lista.setAdapter(adaptador);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu ) {
        getMenuInflater().inflate(R.menu.menu_series, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Salir) {
            Intent intent = new Intent(MainActivity_Series.this,init_alfilPOS.class);
            startActivity(intent);
        }

        if (item.getItemId()==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }



}
