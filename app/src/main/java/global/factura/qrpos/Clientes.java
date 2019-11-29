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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zj.com.cn.bluetooth.sdk.R;
import zj.com.command.sdk.Command;
import zj.com.command.sdk.PrinterCommand;

/**
 * Created by Alejandro on 3/19/2018.
 */

public class Clientes extends Activity {
    // variables para table productos
    public static final String RAZON_SOCIAL_CLIENTE = "razon_social_cliente";
    public static final String RUC_CLIENTE = "ruc_cliente";
    public static final String DIRECCION_CLIENTE = "direccion_cliente";
    public static final String CORREO_CLIENTE = "correo_cliente";
    public static final String TELEFONO_CLIENTE = "telefono_cliente";

    private static final String TABLE_PRODUCTOS = "productos";

    public  int _id;
    public String Mruc;
    public String Mrazon_social;
    public String Mdireccion;
    public String Mcorreo;
    public String Mtelefono;

    public String _base01="";
    public String _base02="";
    public String _base03="";
    public String _base04="";
    public String _base05="";
    public String _base06="";


    EditText editText_Ruc, editText_Razon_Social, editText_Direccion, editText_Correo, editText_Telefono;

    int _myId;
    connectionDB db;
    Button Modificar, Eliminar, Salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_qr);

        Bundle b = getIntent().getExtras();
        if (b!=null) {
            _myId = b.getInt("id");

        }

        db = new connectionDB(this);
        Cursor cursor =  db.getReg_Clientes(_myId);
        if (cursor.moveToFirst()) {
            do {

                editText_Ruc = (EditText) findViewById(R.id.editText_Ruc);
                editText_Razon_Social = (EditText) findViewById(R.id.editText_Razon_Social);
                editText_Direccion = (EditText) findViewById(R.id.editText_Direccion);
                editText_Correo = (EditText) findViewById(R.id.editText_Correo);
                editText_Telefono = (EditText) findViewById(R.id.editText_Telefono);


                Mruc = cursor.getString(1);
                Mrazon_social = cursor.getString(2);
                Mdireccion = cursor.getString (3);
                Mcorreo = cursor.getString (4);
                Mtelefono = cursor.getString(5);


                editText_Ruc.setText(Mruc);
                editText_Razon_Social.setText(Mrazon_social);
                editText_Direccion.setText(Mdireccion.toString());
                editText_Correo.setText(Mcorreo.toString());
                editText_Telefono.setText(Mtelefono.toString());


                //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

        }


        Modificar = (Button) findViewById(R.id.button_Modificar);
        Eliminar = (Button) findViewById(R.id.button_Eliminar);
        Salir = (Button) findViewById(R.id.button_Salir);

        editText_Ruc = (EditText) findViewById(R.id.editText_Ruc);
        editText_Razon_Social = (EditText) findViewById(R.id.editText_Razon_Social);
        editText_Direccion = (EditText) findViewById(R.id.editText_Direccion);
        editText_Correo = (EditText) findViewById(R.id.editText_Correo);
        editText_Telefono = (EditText) findViewById(R.id.editText_Telefono);

        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar(_myId,
                        editText_Ruc.getText().toString(),
                        editText_Razon_Social.getText().toString(),
                        editText_Direccion.getText().toString(),
                        editText_Correo.getText().toString(),
                        editText_Telefono.getText().toString());


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

    private void _Subir_Clientes() {

    }

    private void _Salir() {
        Intent intent = new Intent(this, MainActivity_Clientes.class);
        startActivity(intent);
    }

    private void Modificar(int _id,
                           String _editText_Ruc,
                           String _editText_Razon_Social,
                           String _editText_Direccion,
                           String _editText_Correo,
                           String _editText_Telefono) {

        ContentValues valoresClientes = new ContentValues();
        valoresClientes.put(RUC_CLIENTE, _editText_Ruc);
        valoresClientes.put(RAZON_SOCIAL_CLIENTE, _editText_Razon_Social);
        valoresClientes.put(DIRECCION_CLIENTE, _editText_Direccion);
        valoresClientes.put(CORREO_CLIENTE, _editText_Correo);
        valoresClientes.put(TELEFONO_CLIENTE, _editText_Telefono);



        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update("clientes", valoresClientes, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Clientes.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,MainActivity_Clientes.class );
        startActivity(intent);

    }

    private void Eliminar(int _id) {



        db = new connectionDB(this);



        db.getWritableDatabase().delete("clientes","_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Clientes.this,"Se Elimino el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,MainActivity_Clientes.class );
        startActivity(intent);

    }




}
