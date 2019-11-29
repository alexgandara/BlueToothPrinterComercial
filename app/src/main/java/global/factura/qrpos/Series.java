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

public class Series extends Activity {

    EditText serie, folio, id, naturaleza;
    Button Modificar, Salir;
    int _myId;

    String _serie, _folio, _naturaleza;
    connectionDB db;

    public String Mserie="", Mnaturaleza="";
    public int Mfolio = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertar_serie);

        Bundle b = getIntent().getExtras();
        if (b!=null) {
            _myId = b.getInt("id");

        }


        db = new connectionDB(this);
        Cursor cursor =  db.getReg_Serie(_myId);
        if (cursor.moveToFirst()) {
            do {

                serie = (EditText) findViewById(R.id.editText_Serie);
                naturaleza = (EditText) findViewById(R.id.editText_Naturaleza);
                folio = (EditText) findViewById(R.id.editText_Folio);


                Mserie = cursor.getString(1);
                Mnaturaleza= cursor.getString(2);
                Mfolio= cursor.getInt(3);

                serie.setText(Mserie);
                naturaleza.setText(Mnaturaleza);
                folio.setText(""+Mfolio);


                //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

        }







        Modificar = (Button) findViewById(R.id.button_Modificar);
        Salir = (Button) findViewById(R.id.button_Salir);

        serie = (EditText) findViewById(R.id.editText_Serie);
        folio = (EditText) findViewById(R.id.editText_Folio);
        naturaleza = (EditText) findViewById(R.id.editText_Naturaleza);

        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar_Serie(_myId,
                                serie.getText().toString(),
                                folio.getText().toString(),
                                naturaleza.getText().toString());
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
        Intent intent = new Intent(this, MainActivity_Series.class);
        startActivity(intent);
    }


    private void Modificar_Serie (int _id,
                                  String _serie,
                                  String _folio,
                                  String _naturaleza) {

        ContentValues valoresSerie = new ContentValues();
        valoresSerie.put("serie", _serie);
        valoresSerie.put("folio", _folio);
        valoresSerie.put("naturaleza", _naturaleza);

        db = new connectionDB(this);


        db.getWritableDatabase().update("series", valoresSerie, "id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Series.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,MainActivity_Series.class );
        startActivity(intent);

    }


}
