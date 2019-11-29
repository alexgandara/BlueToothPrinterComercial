package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zj.com.cn.bluetooth.sdk.R;

/**
 * Created by Alejandro on 3/21/2018.
 */

public class addDatos_Unidades extends Activity {

    EditText editText_Unidad, editText_Descripcion_Unidad;
    Button Agregar, Salir;

    public String _Unidad;
    public String _Descripcion_Unidad;

    connectionDB db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertar_unidades_qr);

        Agregar = (Button) findViewById(R.id.button_Agregar);
        Salir = (Button) findViewById(R.id.button_Salir);


        editText_Unidad = (EditText) findViewById(R.id.editText_Unidad);
        editText_Descripcion_Unidad = (EditText) findViewById(R.id.editText_Descripcion_Unidad);


        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatos_Unidades();


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
        Intent intent = new Intent(this, MainActivity_Unidades.class);
        startActivity(intent);
    }

    private void addDatos_Unidades() {


        _Unidad               = editText_Unidad.getText().toString();
        _Descripcion_Unidad   = editText_Descripcion_Unidad.getText().toString();



        db = new connectionDB(this);
        db.addNotes_Unidades(_Unidad,_Descripcion_Unidad);
        //    db.close();
        Intent intent = new Intent(this,MainActivity_Unidades.class );
        startActivity(intent);

    }






}
