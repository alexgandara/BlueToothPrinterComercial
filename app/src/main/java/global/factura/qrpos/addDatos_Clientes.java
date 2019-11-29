package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zj.com.cn.bluetooth.sdk.R;


/**
 * Created by Alejandro on 3/19/2018.
 */

public class addDatos_Clientes extends Activity {

    EditText editText_Ruc, editText_Razon_Social, editText_Direccion, editText_Correo, editText_Telefono;


    Button Agregar, Salir;

    public String _Ruc;
    public String _Razon_Social;
    public String _Direccion;
    public String _Correo;
    public String _Telefono;



    connectionDB db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertar_clientes_qr);

        Agregar = (Button) findViewById(R.id.button_Agregar);
        Salir = (Button) findViewById(R.id.button_Salir);


        editText_Ruc = (EditText) findViewById(R.id.editText_Ruc);
        editText_Razon_Social = (EditText) findViewById(R.id.editText_Razon_Social);
        editText_Direccion = (EditText) findViewById(R.id.editText_Direccion);
        editText_Correo = (EditText) findViewById(R.id.editText_Correo);
        editText_Telefono = (EditText) findViewById(R.id.editText_Telefono);



        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatos_Clientes();


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
            Intent intent = new Intent(this, MainActivity_Clientes.class);
            startActivity(intent);
        }

        private void addDatos_Clientes() {


        _Ruc                    = editText_Ruc.getText().toString();
        _Razon_Social           = editText_Razon_Social.getText().toString();
        _Direccion              = editText_Direccion.getText().toString();
        _Correo                 = editText_Correo.getText().toString();
        _Telefono               = editText_Telefono.getText().toString();



        db = new connectionDB(this);
        db.addNotes_Clientes(_Razon_Social, _Ruc, _Direccion,  _Telefono, _Correo);
        //    db.close();
        Intent intent = new Intent(this,MainActivity_Clientes.class );
        startActivity(intent);

    }






}
