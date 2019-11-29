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

public class TicketPos_Base extends Activity {

    EditText base01, base02, base03, base04, base05, base06;


    String  _base01, _base02, _base03, _base04, _base05, _base06;

    int _myId = 1;
    connectionDB db;
    Button Modificar, Salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketpos_qr_base);






        db = new connectionDB(this);
        Cursor cursor =  db.getReg_TicketPos(_myId);
        if (cursor.moveToFirst()) {
            do {




                base01 = (EditText) findViewById(R.id.editText_Base01 );
                base02 = (EditText) findViewById(R.id.editText_Base02 );
                base03 = (EditText) findViewById(R.id.editText_Base03 );
                base04 = (EditText) findViewById(R.id.editText_Base04 );
                base05 = (EditText) findViewById(R.id.editText_Base05 );
                base06 = (EditText) findViewById(R.id.editText_Base06 );







                _base01 = cursor.getString(11);
                _base02 = cursor.getString(12);
                _base03 = cursor.getString(13);
                _base04 = cursor.getString(14);
                _base05 = cursor.getString(15);
                _base06 = cursor.getString(16);





                base01.setText(_base01);
                base02.setText(_base02);
                base03.setText(_base03);
                base04.setText(_base04);
                base05.setText(_base05);
                base06.setText(_base06);




             //    Toast.makeText(this,"La Empresas fue Modificada", Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

        }




        Modificar = (Button) findViewById(R.id.button_Modificar);
        Salir = (Button) findViewById(R.id.button_Salir);



        base01 = (EditText) findViewById(R.id.editText_Base01);
        base02 = (EditText) findViewById(R.id.editText_Base02);
        base03 = (EditText) findViewById(R.id.editText_Base03);
        base04 = (EditText) findViewById(R.id.editText_Base04);
        base05 = (EditText) findViewById(R.id.editText_Base05);
        base06 = (EditText) findViewById(R.id.editText_Base06);


        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar_ticketPos(_myId,
                        base01.getText().toString(),
                        base02.getText().toString(),
                        base03.getText().toString(),
                        base04.getText().toString(),
                        base05.getText().toString(),
                        base06.getText().toString());}

           //             base01.getText().toString(),
           //             base02.getText().toString(),
           //             base03.getText().toString(),
           //             base04.getText().toString(),
           //             base05.getText().toString(),
           //             base06.getText().toString());}
        });





        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TicketPos_Base.this,Empresas.class );
                startActivity(intent);

            }
        });




    }


    private void Modificar_ticketPos(int _id,
                           String _base01,
                           String _base02,
                           String _base03,
                           String _base04,
                           String _base05,
                           String _base06
    ) {

        ContentValues valoresTicket = new ContentValues();

          valoresTicket.put("base01", _base01);
          valoresTicket.put("base02", _base02);
          valoresTicket.put("base03", _base03);
          valoresTicket.put("base04", _base04);
          valoresTicket.put("base05", _base05);
          valoresTicket.put("base06", _base06);



       db = new connectionDB(this);



        db.getWritableDatabase().update("empresa", valoresTicket, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }







}
