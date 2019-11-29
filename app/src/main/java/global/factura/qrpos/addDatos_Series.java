package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;

public class addDatos_Series extends Activity {





    EditText editText_Serie, editText_Folio, editText_Naturaleza;


    Button Agregar, Salir;


    public String _Serie;
    public String _Folio;
    public String _Naturaleza;

    connectionDB db;

    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.insertar_serie_qr);

        Agregar = (Button) findViewById(R.id.button_Agregar);
        Salir = (Button) findViewById(R.id.button_Salir);

        editText_Serie = (EditText) findViewById(R.id.editText_Serie);
        editText_Folio = (EditText) findViewById(R.id.editText_Folio);
        editText_Naturaleza = (EditText) findViewById(R.id.editText_Naturaleza);



        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatos_Series();


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

    private void addDatos_Series() {


        _Serie   = editText_Serie.getText().toString();
        _Folio   = editText_Folio.getText().toString();
        _Naturaleza   = editText_Naturaleza.getText().toString();


        db = new connectionDB(this);
        db.addNotes_Series(_Serie,_Folio,_Naturaleza);
        //    db.close();
        Intent intent = new Intent(this,MainActivity_Series.class );
        startActivity(intent);

    }







}
