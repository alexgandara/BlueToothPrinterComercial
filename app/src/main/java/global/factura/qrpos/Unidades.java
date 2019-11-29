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

/**
 * Created by Alejandro on 3/21/2018.
 */

public class Unidades extends Activity {

    public static final String ID_UNIDAD = "unidad";
    public static final String DESCRIPCION_UNIDAD = "descripcion_unidad";


    private static final String TABLE_UNIDADES = "unidades";


    public String Munidad;
    public String Mdescripcion_unidad;


    EditText editText_Unidad, editText_Descripcion_Unidad;


    int _myId;
    connectionDB db;
    Button Modificar, Eliminar, Salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidades_qr);

        Bundle b = getIntent().getExtras();
        if (b!=null) {
            _myId = b.getInt("id");

        }

        db = new connectionDB(this);
        Cursor cursor =  db.getReg_Unidades(_myId);
        if (cursor.moveToFirst()) {
            do {

                editText_Unidad = (EditText) findViewById(R.id.editText_Unidad);
                editText_Descripcion_Unidad = (EditText) findViewById(R.id.editText_Descripcion_Unidad);

                Munidad = cursor.getString(1);
                Mdescripcion_unidad = cursor.getString(2);


                editText_Unidad.setText(Munidad);
                editText_Descripcion_Unidad.setText(Mdescripcion_unidad);


                //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

        }


        Modificar = (Button) findViewById(R.id.button_Modificar);
        Eliminar = (Button) findViewById(R.id.button_Eliminar);
        Salir = (Button) findViewById(R.id.button_Salir);

        editText_Unidad = (EditText) findViewById(R.id.editText_Unidad);
        editText_Descripcion_Unidad = (EditText) findViewById(R.id.editText_Descripcion_Unidad);

        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar(_myId,
                        editText_Unidad.getText().toString(),
                        editText_Descripcion_Unidad.getText().toString());




            }
        });


        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar(_myId);

            }
        });

        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _Salir();


            }
        });



    }

    private void Modificar(int _id,
                           String _editText_Unidad,
                           String _editText_Descripcion_Unidad) {



        ContentValues valoresUnidades = new ContentValues();
        valoresUnidades.put(ID_UNIDAD, _editText_Unidad);
        valoresUnidades.put(DESCRIPCION_UNIDAD, _editText_Descripcion_Unidad);



        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update("unidades", valoresUnidades, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Unidades.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,MainActivity_Unidades.class );
        startActivity(intent);

    }





    private void _Salir() {
        Intent intent = new Intent(this, MainActivity_Unidades.class);
        startActivity(intent);
    }



    private void Eliminar(int _id) {



        db = new connectionDB(this);



        db.getWritableDatabase().delete("unidades","_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Unidades.this,"Se Elimino el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,MainActivity_Unidades.class );
        startActivity(intent);

    }






}
