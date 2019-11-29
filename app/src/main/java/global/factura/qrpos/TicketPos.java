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

public class TicketPos extends Activity {

    EditText linea01, linea02, linea03, linea04, linea05, linea06, linea07, linea08, linea09,  base01, base02, base03, base04, base05, base06;


    String _linea01, _linea02, _linea03, _linea04, _linea05, _linea06, _linea07, _linea08, _linea09,  _base01, _base02, _base03, _base04, _base05, _base06;

    int _myId = 1;
    connectionDB db;
    Button Modificar, Salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketpos_qr);






        db = new connectionDB(this);
        Cursor cursor =  db.getReg_TicketPos(_myId);
        if (cursor.moveToFirst()) {
            do {

                linea01 = (EditText) findViewById(R.id.editText_Linea01 );
                linea02 = (EditText) findViewById(R.id.editText_Linea02 );
                linea03 = (EditText) findViewById(R.id.editText_Linea03 );
                linea04 = (EditText) findViewById(R.id.editText_Linea04 );
                linea05 = (EditText) findViewById(R.id.editText_Linea05 );
                linea06 = (EditText) findViewById(R.id.editText_Linea06 );
                linea07 = (EditText) findViewById(R.id.editText_Linea07 );
                linea08 = (EditText) findViewById(R.id.editText_Linea08 );
                linea09 = (EditText) findViewById(R.id.editText_Linea09 );



     //           base01 = (EditText) findViewById(R.id.editText_Base01 );
     //           base02 = (EditText) findViewById(R.id.editText_Base02 );
     //           base03 = (EditText) findViewById(R.id.editText_Base03 );
     //           base04 = (EditText) findViewById(R.id.editText_Base04 );
     //           base05 = (EditText) findViewById(R.id.editText_Base05 );
     //           base06 = (EditText) findViewById(R.id.editText_Base06 );



                _linea01 = cursor.getString(1);
                _linea02 = cursor.getString(2);
                _linea03 = cursor.getString(3);
                _linea04 = cursor.getString(4);
                _linea05 = cursor.getString(5);
                _linea06 = cursor.getString(6);
                _linea07 = cursor.getString(7);
                _linea08 = cursor.getString(8);
                _linea09 = cursor.getString(9);




       //         _base01 = cursor.getString(8);
       //         _base02 = cursor.getString(9);
       //         _base03 = cursor.getString(10);
       //         _base04 = cursor.getString(11);
       //         _base03 = cursor.getString(12);
       //         _base04 = cursor.getString(13);



                linea01.setText(_linea01);
                linea02.setText(_linea02);
                linea03.setText(_linea03);
                linea04.setText(_linea04);
                linea05.setText(_linea05);
                linea06.setText(_linea06);
                linea07.setText(_linea07);
                linea08.setText(_linea08);
                linea09.setText(_linea09);



         //       base01.setText(_base01);
         //       base02.setText(_base02);
         //       base03.setText(_base03);
         //       base04.setText(_base04);
         //       base05.setText(_base05);
         //       base06.setText(_base06);




             //    Toast.makeText(this,"La Empresas fue Modificada", Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

        }




        Modificar = (Button) findViewById(R.id.button_Modificar);
        Salir = (Button) findViewById(R.id.button_Salir);


       // linea01 = (EditText) findViewById(R.id.editText_Linea01);
        linea02 = (EditText) findViewById(R.id.editText_Linea02);
        linea03 = (EditText) findViewById(R.id.editText_Linea03);
        linea04 = (EditText) findViewById(R.id.editText_Linea04);
        linea05 = (EditText) findViewById(R.id.editText_Linea05);
        linea06 = (EditText) findViewById(R.id.editText_Linea06);
        linea07 = (EditText) findViewById(R.id.editText_Linea07);
        linea08 = (EditText) findViewById(R.id.editText_Linea08);
        linea09 = (EditText) findViewById(R.id.editText_Linea09);


        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar_ticketPos(_myId,
                        linea01.getText().toString(),
                        linea02.getText().toString(),
                        linea03.getText().toString(),
                        linea04.getText().toString(),
                        linea05.getText().toString(),
                        linea06.getText().toString(),
                        linea07.getText().toString(),
                        linea08.getText().toString(),
                        linea09.getText().toString());}

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
                Intent intent = new Intent(TicketPos.this,Empresas.class );
                startActivity(intent);

            }
        });




    }


    private void Modificar_ticketPos(int _id,
                           String _linea01,
                           String _linea02,
                           String _linea03,
                           String _linea04,
                           String _linea05,
                           String _linea06,
                           String _linea07,
                           String _linea08,
                           String _linea09
                      //     String _base01,
                      //     String _base02,
                      //     String _base03,
                      //     String _base04,
                       //    String _base05,
                       //    String _base06
    ) {

        ContentValues valoresTicket = new ContentValues();

        valoresTicket.put("linea01", _linea01);
        valoresTicket.put("linea02", _linea02);
        valoresTicket.put("linea03", _linea03);
        valoresTicket.put("linea04", _linea04);
        valoresTicket.put("linea05", _linea05);
        valoresTicket.put("linea06", _linea06);
        valoresTicket.put("linea07", _linea07);
        valoresTicket.put("linea08", _linea08);
        valoresTicket.put("linea09", _linea09);
   //     valoresTicket.put("base01", _base01);
   //     valoresTicket.put("base02", _base02);
   //     valoresTicket.put("base03", _base03);
   //     valoresTicket.put("base04", _base04);
   //     valoresTicket.put("base05", _base05);
   //     valoresTicket.put("base06", _base06);



       db = new connectionDB(this);



        db.getWritableDatabase().update("empresa", valoresTicket, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }







}
