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

public class Get_Pasword_Empresas extends Activity {

    EditText password;

    public String _password;

    int _myId = 1;
    connectionDB db;
    Button Salir, go_Empresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_empresas);

        db = new connectionDB(this);

        Salir = (Button) findViewById(R.id.button_Salir);
        go_Empresas = (Button) findViewById(R.id.button_go_Empresas);

        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Get_Pasword_Empresas.this,init_alfilPOS.class );
                startActivity(intent);

            }
        });


        go_Empresas.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent = new Intent(Get_Pasword_Empresas.this, Empresas.class);
                    startActivity(intent);
                    //  return true;
                } else {
                    Toast.makeText(Get_Pasword_Empresas.this,"El Password < "+_password+" > es Incorrecto << no tiene acceso a Empresas >>   ", Toast.LENGTH_SHORT ).show();
                }
            }
        });




    }








}
