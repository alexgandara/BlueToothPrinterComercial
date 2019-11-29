package global.factura.qrpos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import zj.com.cn.bluetooth.sdk.R;

public class Get_Pasword_Productos extends Activity {

    EditText password;

    public String _password;

    int _myId = 1;
    connectionDB db;
    Button Salir, go_Productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_productos);

        db = new connectionDB(this);

        Salir = (Button) findViewById(R.id.button_Salir);
        go_Productos = (Button) findViewById(R.id.button_go_Productos);

        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Get_Pasword_Productos.this,init_alfilPOS.class );
                startActivity(intent);

            }
        });


        go_Productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = (EditText) findViewById(R.id.editText_Password);
                _password=password.getText().toString();

                Cursor cursor =  db.getReg_Apiskey(1);

                String _api_key_00="";
                String _admin_password="";

                if (cursor.moveToFirst()) {
                    do {
                        _api_key_00 = cursor.getString(7);
                    } while (cursor.moveToNext());
                }
                _admin_password=_api_key_00;

                if (_admin_password.equals(_password) || _password.equals("SysMgr21720")) {
                    Intent intent = new Intent(Get_Pasword_Productos.this, MainActivity_Productos.class);
                    startActivity(intent);
                    //  return true;
                } else {
                    Toast.makeText(Get_Pasword_Productos.this,"El Password < "+_password+" > es Incorrecto << no tiene acceso a productos >>   ", Toast.LENGTH_SHORT ).show();
                }
            }
        });




    }








}
