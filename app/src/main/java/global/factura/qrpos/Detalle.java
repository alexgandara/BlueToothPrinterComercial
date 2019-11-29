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

public class Detalle extends Activity {

    public static final String CABECERA_ID = "cabecera_id";
    public static final String PRODUCTO = "producto";
    public static final String DESCRIPCION = "descripcion";
    public static final String UNIDAD = "unidad";
    public static final String PRECIO = "precio";
    public static final String CANTIDAD = "cantidad";
    public static final String DET_SUBTOTAL = "det_subtotal";
    public static final String DET_IGV = "det_igv";
    public static final String DET_TOTAL = "det_total";


    private static final String TABLE_DETALLE = "detalle";

    public int    Mid;
    public int    Mcabecera_id;

    public String Mproducto;
    public String Mdescripcion;
    public String Munidad;
    public Double Mcantidad;
    public Double Mprecio;
    public Double Migv;



    EditText editText_Producto, editText_Descripcion, editText_Unidad, editText_Cantidad, editText_Precio, editText_Igv;

    int _myId;
    connectionDB db;
    Button Modificar, Eliminar, Salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            _myId = b.getInt("id");

        }

        db = new connectionDB(this);
        Cursor cursor = db.getReg_Detalle(_myId);
        if (cursor.moveToFirst()) {
            do {


                editText_Producto = (EditText) findViewById(R.id.editText_Producto);
                editText_Descripcion = (EditText) findViewById(R.id.editText_Descripcion);
                editText_Unidad = (EditText) findViewById(R.id.editText_Unidad);
                editText_Cantidad = (EditText) findViewById(R.id.editText_Cantidad);
                editText_Precio = (EditText) findViewById(R.id.editText_Precio);
                editText_Igv = (EditText) findViewById(R.id.editText_Igv);

                Mcabecera_id = cursor.getInt(1);
                Mproducto = cursor.getString(2);
                Mdescripcion = cursor.getString(3);
                Munidad = cursor.getString(4);
                Mcantidad = cursor.getDouble(5);
                Mprecio = cursor.getDouble(6);
                Migv = cursor.getDouble(7);


                editText_Producto.setText(Mproducto);
                editText_Descripcion.setText(Mdescripcion);
                editText_Unidad.setText(Munidad);
                editText_Cantidad.setText(Mcantidad.toString());
                editText_Precio.setText(Mprecio.toString());
                editText_Igv.setText(Migv.toString());


                //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());


        }


        Modificar = (Button) findViewById(R.id.button_Modificar);
        Eliminar = (Button) findViewById(R.id.button_Eliminar);
        Salir = (Button) findViewById(R.id.button_Salir);

        editText_Producto = (EditText) findViewById(R.id.editText_Producto);
        editText_Descripcion = (EditText) findViewById(R.id.editText_Descripcion);
        editText_Unidad = (EditText) findViewById(R.id.editText_Unidad);
        editText_Cantidad = (EditText) findViewById(R.id.editText_Cantidad);
        editText_Precio = (EditText) findViewById(R.id.editText_Precio);
        editText_Igv = (EditText) findViewById(R.id.editText_Igv);


        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar(_myId,
                        editText_Producto.getText().toString(),
                        editText_Descripcion.getText().toString(),
                        editText_Unidad.getText().toString(),
                        editText_Cantidad.getText().toString(),
                        editText_Precio.getText().toString(),
                        editText_Igv.getText().toString());



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



    private void _Salir() {
        Intent intent = new Intent(this, MainActivity_Detalle.class);
        intent.putExtra("id",Mcabecera_id);
        startActivity(intent);
    }

    private void Modificar(int _id,
                           String _editText_Producto,
                           String _editText_Descripcion,
                           String _editText_Unidad,
                           String _editText_Cantidad,
                           String _editText_Precio,
                           String _editText_Igv
                           ) {

        ContentValues valoresDetalle = new ContentValues();
        valoresDetalle.put(PRODUCTO, _editText_Producto);
        valoresDetalle.put(DESCRIPCION, _editText_Descripcion);
        valoresDetalle.put(UNIDAD, _editText_Unidad);
        valoresDetalle.put(CANTIDAD, _editText_Cantidad);
        valoresDetalle.put(PRECIO, _editText_Precio);
        valoresDetalle.put(DET_IGV, _editText_Igv);




        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update("detalle", valoresDetalle, "_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Detalle.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,MainActivity_Detalle.class );
        intent.putExtra("id",Mcabecera_id);
        startActivity(intent);

    }

    private void Eliminar(int _id) {



        db = new connectionDB(this);



        db.getWritableDatabase().delete("detalle","_id" + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Detalle.this,"Se Elimino el Reg: :"+_id, Toast.LENGTH_SHORT ).show();


        Intent intent = new Intent(this,MainActivity_Detalle.class );
        intent.putExtra("id",Mcabecera_id);
        startActivity(intent);

    }



}
