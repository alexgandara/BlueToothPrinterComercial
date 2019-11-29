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

public class Empresas extends Activity {

    EditText razon_social, ruc, direccion1,  telefono, correo, licencia, tipo_impresora, icbper;


    String _razon_social, _ruc, _direccion1, _telefono, _correo, _licencia, _tipo_impresora, _ICBPER;

    int _myId = 1;
    connectionDB db;
    Button Modificar, Salir, TicketPos, TicketPos_base, Apis_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas_qr);



        db = new connectionDB(this);
        Cursor cursor =  db.getReg_Empresas(_myId);
        if (cursor.moveToFirst()) {
            do {

                razon_social = (EditText) findViewById(R.id.editText_Razon_Social);
                ruc = (EditText) findViewById(R.id.editText_Ruc);
                direccion1 = (EditText) findViewById(R.id.editText_Direccion1);
     //           correo = (EditText) findViewById(R.id.editText_Correo);
                icbper = (EditText) findViewById(R.id.editText_ICBPER);
     //           telefono = (EditText) findViewById(R.id.editText_Telefono);
                licencia = (EditText) findViewById(R.id.editText_Licencia);
                tipo_impresora = (EditText) findViewById(R.id.editText_Tipo_Impresora);


                _razon_social = cursor.getString(1);
                _ruc = cursor.getString(2);
                _direccion1 = cursor.getString(3);
      //          _correo = cursor.getString(4);
                _ICBPER = cursor.getString(4);
      //          _telefono = cursor.getString(5);
                _licencia = cursor.getString(6);
                _tipo_impresora = cursor.getString(7);



                razon_social.setText(_razon_social);
                ruc.setText(_ruc);
                direccion1.setText(_direccion1);
        //        correo.setText(_correo);
                icbper.setText(_ICBPER);
       //         telefono.setText(_telefono);

                tipo_impresora.setText(_tipo_impresora);
                licencia.setText(_licencia);





             //    Toast.makeText(this,"La Empresas fue Modificada", Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

        }




        Modificar = (Button) findViewById(R.id.button_Modificar);
        Salir = (Button) findViewById(R.id.button_Salir);
        TicketPos = (Button) findViewById(R.id.button_TicketPos);
        TicketPos_base = (Button) findViewById(R.id.button_TicketPos_Buttom);
        Apis_key = (Button) findViewById(R.id.button_ApiKey);



        razon_social = (EditText) findViewById(R.id.editText_Razon_Social);
        ruc = (EditText) findViewById(R.id.editText_Ruc);
        direccion1 = (EditText) findViewById(R.id.editText_Direccion1);
        correo = (EditText) findViewById(R.id.editText_Correo);
    //    telefono = (EditText) findViewById(R.id.editText_Telefono);
        licencia = (EditText) findViewById(R.id.editText_Licencia);
        tipo_impresora = (EditText) findViewById(R.id.editText_Tipo_Impresora);

        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar_Empresa(_myId,
                        razon_social.getText().toString(),
                        ruc.getText().toString(),
                        direccion1.getText().toString(),
                        icbper.getText().toString(),
          //              telefono.getText().toString(),
                        tipo_impresora.getText().toString(),
                        licencia.getText().toString());}
        });





        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Empresas.this,init_alfilPOS.class );
                startActivity(intent);

            }
        });

       TicketPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Empresas.this,TicketPos.class );
                startActivity(intent);

            }
        });


        TicketPos_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Empresas.this,TicketPos_Base.class );
                startActivity(intent);

            }
        });


        Apis_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Empresas.this,Apis_key.class );
                startActivity(intent);

            }
        });




    }


    private void Modificar_Empresa(int _id,
                           String _razon_social,
                           String _ruc,
                           String _direccion1,
                           String _icbper,
                //           String _telefono,
                           String _tipo_impresora,
                           String _licencia
    ) {

        ContentValues valoresEmpresa = new ContentValues();

        valoresEmpresa.put("razon_social_empresa", _razon_social);
        valoresEmpresa.put("ruc_empresa", _ruc);
        valoresEmpresa.put("direccion_empresa_1", _direccion1);
        valoresEmpresa.put("impuesto_icbper", _icbper);
     //   valoresEmpresa.put("telefono_empresa", _telefono);
        valoresEmpresa.put("tipo_impresora", _tipo_impresora);
        valoresEmpresa.put("licencia", _licencia);


       db = new connectionDB(this);



        db.getWritableDatabase().update("empresa", valoresEmpresa, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }







}
