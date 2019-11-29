package global.factura.qrpos;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.AlreadyBoundException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import zj.com.cn.bluetooth.sdk.R;
import zj.com.command.sdk.Command;
import zj.com.command.sdk.PrintPicture;
import zj.com.command.sdk.PrinterCommand;
import zj.com.customize.sdk.Other;

import static global.factura.qrpos.DbBitmapUtility.getBytes;
import static global.factura.qrpos.DbBitmapUtility.getImage;

public class Modificar2 extends Activity implements OnClickListener{
/******************************************************************************************************/
	// Debugging
	private static final String TAG = "Main_Activity";
	private static final boolean DEBUG = true;
/******************************************************************************************************/
	// Message types sent from the BluetoothService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_CONNECTION_LOST = 6;
	public static final int MESSAGE_UNABLE_CONNECT = 7;
/*******************************************************************************************************/
	// Key names received from the BluetoothService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_CHOSE_BMP = 3;
	private static final int REQUEST_CAMER = 4;
	private RequestQueue mQueue;

	//QRcode
	private static final int QR_WIDTH = 350;
	private static final int QR_HEIGHT = 350;
/*******************************************************************************************************/
	private static final String CHINESE = "GBK";
	private static final String THAI = "CP874";
	private static final String KOREAN = "EUC-KR";
	private static final String BIG5 = "BIG5";

	private static String _texto_nube="";
    private static String _mensaje_error="";

	private static String _almacen_m="";
	private static String _razon_social_cliente_m="";
	private static String _direccion_cliente_m="";
	private static String _correo_cliente_m="";
	private static String _telefono_cliente_m="";
	private static final int numero_registros_por_bajar=0;




	/*********************************************************************************/
	private TextView mTitle;
	EditText editText;
	ImageView imageViewPicture;
	private static boolean is58mm = true;
	private RadioButton width_58mm, width_80;
	private RadioButton thai, big5, Simplified, Korean;
	private CheckBox hexBox;
	private Button sendButton = null;
	private Button sendCierre = null;
	private Button sendNube = null;
	private Button sendNube_Clientes = null;
	private Button recibeNube_Clientes = null;
    private Button recibeNube_Productos = null;
	private Button testButton = null;
	private Button printbmpButton = null;
	private Button btnScanButton = null;
	private Button btnClose = null;
	private Button btn_BMP = null;
	private Button btn_ChoseCommand = null;
	private Button btn_prtsma = null;
	private Button btn_prttableButton = null;
	private Button btn_prtcodeButton = null;
	private Button btn_scqrcode = null;
	private Button btn_camer = null;
	private Button btn_alfilPOS = null;





/******************************************************************************************************/
	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the services
	private BluetoothService mService = null;

/***************************   指                 令****************************************************************/
    final String[] items = { "复位打印机", "打印并走纸", "标准ASCII字体", "压缩ASCII字体", "正常大小",
			"二倍高倍宽", "三倍高倍宽", "四倍高倍宽", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",
			"取消顺时针旋转90°", "选择顺时针旋转90°", "走纸到切刀位置并切纸", "蜂鸣指令", "标准钱箱指令",
			"实时弹钱箱指令", "进入字符模式", "进入中文模式", "打印自检页", "禁止按键", "取消禁止按键" ,
			"设置汉字字符下划线", "取消汉字字符下划线", "进入十六进制模式" };
    final String[] itemsen = { "Print Init", "Print and Paper", "Standard ASCII font", "Compressed ASCII font", "Normal size",
    "Double high power wide", "Twice as high power wide", "Three times the high-powered wide", "Off emphasized mode", "Choose bold mode", "Cancel inverted Print", "Invert selection Print", "Cancel black and white reverse display", "Choose black and white reverse display",
	"Cancel rotated clockwise 90 °", "Select the clockwise rotation of 90 °", "Feed paper Cut", "Beep", "Standard CashBox",
	"Open CashBox", "Char Mode", "Chinese Mode", "Print SelfTest", "DisEnable Button", "Enable Button" ,
	"Set Underline", "Cancel Underline", "Hex Mode" };
	final byte[][] byteCommands = {
			{ 0x1b, 0x40, 0x0a },// 复位打印机
			{ 0x0a }, //打印并走纸
			{ 0x1b, 0x4d, 0x00 },// 标准ASCII字体
			{ 0x1b, 0x4d, 0x01 },// 压缩ASCII字体
			{ 0x1d, 0x21, 0x00 },// 字体不放大
			{ 0x1d, 0x21, 0x11 },// 宽高加倍
			{ 0x1d, 0x21, 0x22 },// 宽高加倍
			{ 0x1d, 0x21, 0x33 },// 宽高加倍
			{ 0x1b, 0x45, 0x00 },// 取消加粗模式
			{ 0x1b, 0x45, 0x01 },// 选择加粗模式
			{ 0x1b, 0x7b, 0x00 },// 取消倒置打印
			{ 0x1b, 0x7b, 0x01 },// 选择倒置打印
			{ 0x1d, 0x42, 0x00 },// 取消黑白反显
			{ 0x1d, 0x42, 0x01 },// 选择黑白反显
			{ 0x1b, 0x56, 0x00 },// 取消顺时针旋转90°
			{ 0x1b, 0x56, 0x01 },// 选择顺时针旋转90°
			{ 0x0a, 0x1d, 0x56, 0x42, 0x01, 0x0a },//切刀指令
			{ 0x1b, 0x42, 0x03, 0x03 },//蜂鸣指令
			{ 0x1b, 0x70, 0x00, 0x50, 0x50 },//钱箱指令
			{ 0x10, 0x14, 0x00, 0x05, 0x05 },//实时弹钱箱指令
			{ 0x1c, 0x2e },// 进入字符模式
			{ 0x1c, 0x26 }, //进入中文模式
			{ 0x1f, 0x11, 0x04 }, //打印自检页
			{ 0x1b, 0x63, 0x35, 0x01 }, //禁止按键
			{ 0x1b, 0x63, 0x35, 0x00 }, //取消禁止按键
			{ 0x1b, 0x2d, 0x02, 0x1c, 0x2d, 0x02 }, //设置下划线
			{ 0x1b, 0x2d, 0x00, 0x1c, 0x2d, 0x00 }, //取消下划线
			{ 0x1f, 0x11, 0x03 }, //打印机进入16进制模式
	};
/***************************条                          码***************************************************************/
	final String[] codebar = { "UPC_A", "UPC_E", "JAN13(EAN13)", "JAN8(EAN8)",
							   "CODE39", "ITF", "CODABAR", "CODE93", "CODE128", "QR Code" };
	final byte[][] byteCodebar = {
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
			{ 0x1b, 0x40 },// 复位打印机
	};
/******************************************************************************************************/
// variables para factura global

    public  static DecimalFormat df;
    public  static DecimalFormatSymbols simb;

    public static final String TABLE_ID = "_id";
    public static final String SERIE = "serie";
    public static final String FOLIO = "folio";
    public static final String FECHA = "fecha";
    public static final String RUC = "ruc";
    public static final String RAZON_SOCIAL = "razon_social";
    public static final String DIRECCION = "direccion";
    public static final String MONEDA = "moneda";
    public static final String GRAVADO = "gravado";
    public static final String EXCENTO = "excento";
    public static final String INAFECTO = "inafecto";
    public static final String SUBTOTAL = "subtotal";
    public static final String IGV = "igv";
    public static final String TOTAL = "total";
    public static final String CORREO = "correo";

    private static final String DATABASE = "miniPOS";
    private static final String TABLE = "cabecera";

    public double _subtotal=0;
	public double _icbper=0;
	public double _impuesto_icbper=0;
    public double _total=0;
    public double _igv=0;
    public int    Mid   =0;
    public String Mserie="";
    public int Mfolio=0;
    public String Mruc="";
    public String Mrazon_social="";
    public String Mdireccion="";
    public String Mmoneda="";
    public String Mfecha="";
    public String Rfecha=null;

    public String _detalle="";
    public String Mcorreo="";

    public double _igv_global;
    public String _datos_qr="";


	public String _base01="";
	public String _base02="";
	public String _base03="";
	public String _base04="";
	public String _base05="";
	public String _base06="";

	public int _recibo_global;



	EditText serie, folio, ruc, razon_social, direccion, moneda, fecha, correo;

    String _serie, _folio, _ruc, _razon_social, _direccion, _moneda, _fecha, _correo;

    int _myId;
    connectionDB db;
    Button Modificar, Eliminar, Salir, Detalle, Imprimir, Documentos, Archivar, Enviar_Correo, Reset;

	// alex

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main2);


		Bundle b = getIntent().getExtras();
		if (b!=null) {








			_myId = b.getInt("id");

		//	_recibo_global=_myId;

			Toast.makeText(Modificar2.this,"ID a Modifcar :"+_recibo_global, Toast.LENGTH_SHORT ).show();





		} else {
			Toast.makeText(Modificar2.this,"tiene nullo :"+_recibo_global, Toast.LENGTH_SHORT ).show();

			_myId=_recibo_global;
		}


		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_title);
		mTitle = (TextView) findViewById(R.id.title_right_text);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();





        db = new connectionDB(this);
		Toast.makeText(Modificar2.this,"buscara :"+_myId, Toast.LENGTH_SHORT ).show();

        Cursor cursor =  db.getReg(_myId);
        if (cursor.moveToFirst()) {
            do {


                serie = (EditText) findViewById(R.id.editText_Serie);
                folio = (EditText) findViewById(R.id.editText_Folio);
                ruc = (EditText) findViewById(R.id.editText_Ruc);
                razon_social = (EditText) findViewById(R.id.editText_RazonSocial);
                direccion = (EditText) findViewById(R.id.editText_Direccion);
                moneda = (EditText) findViewById(R.id.editText_Moneda);
                fecha = (EditText) findViewById(R.id.editText_Fecha);
                correo = (EditText) findViewById(R.id.editText_Correo);

                Mid = cursor.getInt(0);
				_recibo_global = Mid;

                Mserie = cursor.getString(1);
                Mfolio = cursor.getInt(2);
                Mruc = cursor.getString(3);
                Mrazon_social = cursor.getString(4);
                Mdireccion = cursor.getString(5);
                Mmoneda = cursor.getString(6);
                Mfecha = cursor.getString(7);

             //   Rfecha = stringToDate(Mfecha);

				Rfecha = Mfecha;

                Mcorreo = cursor.getString(8);

                serie.setText(Mserie);
                folio.setText(""+Mfolio);

                ruc.setText(Mruc);
                razon_social.setText(Mrazon_social);
                direccion.setText(Mdireccion);
                moneda.setText(Mmoneda);
       //         fecha.setText(Mfecha);
       //         correo.setText(Mcorreo);



                //  Toast.makeText(Modificar.this,"Razon Social :"+Mrazon_social, Toast.LENGTH_SHORT ).show();

            } while (cursor.moveToNext());

            List<String> item = null;
            Cursor c = db.getNotes_detalle(Mid);

            item = new ArrayList<String>();
            String producto = "",  descripcion="", unidad="";
            int id, folio;
            double precio, cantidad, precio_sin_igv, subtotal, total, igv,_subtotal_con_igv;
            String _salto ="\n";
            _detalle="";

            total=0;
            subtotal=0;
            _total=0;
            _subtotal=0;
            _icbper=0;
            _igv=0;
			_subtotal_con_igv=0;


			Cursor cursor2 =  db.getReg_icbper(_myId);

			if (cursor2.moveToFirst()) {
				do {



					_impuesto_icbper = Double.parseDouble(cursor2.getString(1));

				} while (cursor2.moveToNext());

			}






			int _linea=0;
			int _id_unico=0;



            if (c.moveToFirst()) {
                do {
                	_linea++;
                    id = c.getInt(0);
                    producto = c.getString(1);
                    descripcion = c.getString(2);
                    unidad = c.getString(3);
                    cantidad = c.getDouble(4);
                    precio = c.getDouble(5);
                    igv = c.getDouble(6);
					_id_unico = c.getInt(8);
                    _igv_global=igv;

                    precio_sin_igv=precio/(1+igv);

                    subtotal=precio_sin_igv*cantidad;
                    _subtotal=_subtotal+subtotal;
                    _total=_total+(cantidad*precio);
                    _subtotal_con_igv=precio*cantidad;

                    if (producto.trim().equals("BOLSA")) {
                    	_icbper=_icbper+(_impuesto_icbper*cantidad);
					}






                    item.add(id+" - "+producto+" - "+descripcion+" - "+unidad+" - "+cantidad+" "+precio+" "+_linea);
                    //"CANT.  UNI    PRECIO     IMPORTE"+_salto;
                    _detalle=_detalle+descripcion.toUpperCase()+_salto;
                    _detalle=_detalle+_cantidad(cantidad)+" "+unidad+"    "+_cantidad(precio)+"     "+_cantidad(_subtotal_con_igv)+_salto;


                    // actualizar numero de linea
					update_linea_detalle(_id_unico,_linea);

					// gandara

                } while (c.moveToNext());


                _igv=_total-_subtotal;
				_total=_total+_icbper;



            }


            // actualizar cabecera ALEX

			Modificar_Cabecera(_recibo_global,_subtotal,0.00,0.00, _igv,_subtotal,_total);

        }


        Modificar = (Button) findViewById(R.id.button_Modificar);
        Eliminar = (Button) findViewById(R.id.button_Eliminar);
        Detalle = (Button) findViewById(R.id.button_Detalle);
//        Imprimir = (Button) findViewById(R.id.button_Imprimir);
        Documentos = (Button) findViewById(R.id.button_Documentos);
		Archivar = (Button) findViewById(R.id.button_Archivar);
		Enviar_Correo = (Button) findViewById(R.id.button_Correo);
		Reset = (Button) findViewById(R.id.button_Reset);
	//	Cierre = (Button) findViewById(R.id.Button_Cierre);

        Salir = (Button) findViewById(R.id.button_Salir);



        serie = (EditText) findViewById(R.id.editText_Serie);
        folio = (EditText) findViewById(R.id.editText_Folio);
        ruc = (EditText) findViewById(R.id.editText_Ruc);
        razon_social = (EditText) findViewById(R.id.editText_RazonSocial);
        direccion = (EditText) findViewById(R.id.editText_Direccion);
        moneda = (EditText) findViewById(R.id.editText_Moneda);
        fecha = (EditText) findViewById(R.id.editText_Fecha);




        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar(_myId,
                        serie.getText().toString(),
                        folio.getText().toString(),
                        ruc.getText().toString(),
                        razon_social.getText().toString(),
                        direccion.getText().toString(),
                        moneda.getText().toString(),
                        correo.getText().toString());
            }
        });


		Archivar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Archivar(_myId);
			}
		});




		Detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modificar2.this,MainActivity_Detalle.class );
                intent.putExtra("id",_myId);
                startActivity(intent);
            }
        });


        Documentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modificar2.this,init_alfilPOS.class );
                startActivity(intent);
            }
        });

		Reset.setOnClickListener(new View.OnClickListener() {



			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Modificar2.this,init_alfilPOS.class );
				startActivity(intent);
			}
		});




        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar(_myId);

            }
        });



/*
		Enviar_Correo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i("Send email", "");
				String[] TO = {"alejandro.gandara33@gmail.com"};
				String[] CC = {""};
				Intent emailIntent = new Intent(Intent.ACTION_SEND);

				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
				emailIntent.putExtra(Intent.EXTRA_CC, CC);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

				try {
					startActivity(Intent.createChooser(emailIntent, "Send mail..."));
					finish();
			//		Log.i("Finished sending email...", "");
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(Modificar2.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
				}
			}


		});*/









    }

	@Override
	public void onStart() {
		super.onStart();

		// If Bluetooth is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the session
		} else {
			if (mService == null)
				KeyListenerInit();//监听
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();

		if (mService != null) {

			if (mService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth services
				mService.start();
			}
		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (DEBUG)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (DEBUG)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth services
		if (mService != null)
			mService.stop();
		if (DEBUG)
			Log.e(TAG, "--- ON DESTROY ---");
	}

/*****************************************************************************************************/
	private void KeyListenerInit() {



		sendButton = (Button) findViewById(R.id.Send_Button);
		sendButton.setOnClickListener(this);

		sendCierre = (Button) findViewById(R.id.Send_Cierre);
		sendCierre.setOnClickListener(this);

		sendNube = (Button) findViewById(R.id.Send_Nube);
		sendNube.setOnClickListener(this);


		sendNube_Clientes = (Button) findViewById(R.id.Send_Nube_Clientes);
		sendNube_Clientes.setOnClickListener(this);

		recibeNube_Clientes = (Button) findViewById(R.id.Descargar_Nube_Clientes);
		recibeNube_Clientes.setOnClickListener(this);

		recibeNube_Productos = (Button) findViewById(R.id.Descargar_Nube_Productos);
		recibeNube_Productos.setOnClickListener(this);


		btnScanButton = (Button)findViewById(R.id.button_scan);
		btnScanButton.setOnClickListener(this);



		imageViewPicture = (ImageView) findViewById(R.id.imageViewPictureUSB);
		imageViewPicture.setOnClickListener(this);

		btnClose = (Button)findViewById(R.id.btn_close);
		btnClose.setOnClickListener(this);


		btn_camer = (Button)findViewById(R.id.btn_dyca);
		btn_camer.setOnClickListener(this);

		btn_scqrcode = (Button)findViewById(R.id.btn_scqr);
		btn_scqrcode.setOnClickListener(this);




//		ALEX



	//	Bitmap bm = getImageFromAssetsFile("demo.bmp");

		Bitmap bm = _get_logo(1);



		if (null == bm) {
			Bitmap bm_local = getImageFromAssetsFile("demo.bmp");
			if (null != bm_local) {
				imageViewPicture.setImageBitmap(bm_local);
			}

		}



			if (null != bm) {
			imageViewPicture.setImageBitmap(bm);
			}


		imageViewPicture.setEnabled(true);
	//	width_58mm.setEnabled(false);
//		width_80.setEnabled(false);
	//	hexBox.setEnabled(false);
		sendButton.setEnabled(false);
		sendCierre.setEnabled(false);
		sendNube.setEnabled(false);
		sendNube_Clientes.setEnabled(false);
		recibeNube_Clientes.setEnabled(false);
		recibeNube_Productos.setEnabled(false);



		btnClose.setEnabled(false);


		btn_camer.setEnabled(false);
		btn_scqrcode.setEnabled(false);
	//	Simplified.setEnabled(false);


		mService = new BluetoothService(this, mHandler);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_scan:{
			Intent serverIntent = new Intent(Modificar2.this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			break;
		}
		case R.id.btn_close:{
			mService.stop();

			imageViewPicture.setEnabled(true);
		//	width_58mm.setEnabled(false);
		//	width_80.setEnabled(false);
		//	hexBox.setEnabled(false);
			sendButton.setEnabled(false);
			sendCierre.setEnabled(false);
			sendNube.setEnabled(false);
			sendNube_Clientes.setEnabled(false);
			recibeNube_Clientes.setEnabled(false);
			recibeNube_Productos.setEnabled(false);


			btnClose.setEnabled(false);


			btn_camer.setEnabled(false);
			btn_scqrcode.setEnabled(false);
			btnScanButton.setEnabled(true);
		//	Simplified.setEnabled(false);

			btnScanButton.setText(getText(R.string.connect));
			break;
		}

		case R.id.Send_Button:{

			if (false) {
				String str = " asd fa sf asf asd ";
				if(str.length() > 0){
					str = Other.RemoveChar(str, ' ').toString();
					if (str.length() <= 0)
						return;
					if ((str.length() % 2) != 0) {
						Toast.makeText(getApplicationContext(), getString(R.string.msg_state),
								Toast.LENGTH_SHORT).show();
						return;
					}
					byte[] buf = Other.HexStringToBytes(str);
					SendDataByte(buf);
				}else{
					Toast.makeText(Modificar2.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
				}
			} else {





				// manda Imprimir el Ticket POS con QR
				String msg = Imprimir_Ticket();
				String _impresora="normal";

				if(msg.length()>0){


					// primera impresion
					Cursor cursor =  db.getReg_tickect(_myId);

					if (cursor.moveToFirst()) {
						do {



							_impresora = cursor.getString(1);

						} while (cursor.moveToNext());

					}


					if (_impresora.trim().equals("normal")) {
						Print_BMP();
					}



					String _buttom="";
					String _salto ="\n";

					String _linea = "================================";

					SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
					SendDataByte(Command.LF);


				//	if (_impresora.trim().equals("normal")) {
				//		createImage(_datos_qr);
				//	}



                    _buttom=_buttom+_base01+_salto;
                    _buttom=_buttom+_base02+_salto;
                    _buttom=_buttom+_base03+_salto;
                    _buttom=_buttom+_base04+_salto;
					_buttom=_buttom+_base05+_salto;
					_buttom=_buttom+_base06+_salto;

					_buttom=_buttom+_salto;
				//	_buttom=_buttom+"Representacion    Impresa     de"+_salto;
				//	_buttom=_buttom+"del   Comprobante    de    Venta"+_salto;
				//	_buttom=_buttom+"Electronica  autorizado mediante"+_salto;
				//	_buttom=_buttom+"la   Resolucion   155-2017/SUNAT"+_salto;
					_buttom=_buttom+_linea+_salto;
					_buttom=_buttom+_salto+_salto;

					if (_impresora.trim().equals("normal")) {
				//		createImage(_datos_qr);
					}



					try { Thread.sleep(1000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }

					SendDataByte(PrinterCommand.POS_Print_Text(_buttom, CHINESE, 0, 0, 0, 0));



					try { Thread.sleep(1000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }

					if (_impresora.trim().equals("rapida")) {
					//	createImage(_datos_qr);
					//	SendDataByte(Command.LF);
					//	SendDataByte(Command.LF);
					}


					SendDataByte(Command.LF);


					try { Thread.sleep(1000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }





					try { Thread.sleep(12000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }


					// segundo envio




					// Toast.makeText(this, "tipos de impresion:"+_impresora, Toast.LENGTH_LONG).show();

					if (_impresora.trim().equals("normal")) {
						Print_BMP();
					}

					_buttom="";

					// String _linea = "================================";

					SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));

					try { Thread.sleep(1000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }

					SendDataByte(Command.LF);

					try { Thread.sleep(1000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }

			//		if (_impresora.trim().equals("normal")) {
			//			createImage(_datos_qr);
			//		}

					_buttom=_buttom+_base01+_salto;
					_buttom=_buttom+_base02+_salto;
					_buttom=_buttom+_base03+_salto;
					_buttom=_buttom+_base04+_salto;
					_buttom=_buttom+_base05+_salto;
					_buttom=_buttom+_base06+_salto;

					_buttom=_buttom+_salto;
			//		_buttom=_buttom+"Representacion    Impresa     de"+_salto;
			//		_buttom=_buttom+"del   Comprobante    de    Venta"+_salto;
			//		_buttom=_buttom+"Electronica  autorizado mediante"+_salto;
			//		_buttom=_buttom+"la   Resolucion   155-2017/SUNAT"+_salto;
					_buttom=_buttom+_linea+_salto;
					_buttom=_buttom+_salto+_salto;


					if (_impresora.trim().equals("normal")) {
					//	createImage(_datos_qr);
					}




					SendDataByte(PrinterCommand.POS_Print_Text(_buttom, CHINESE, 0, 0, 0, 0));
					try { Thread.sleep(1000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }

					if (_impresora.trim().equals("rapida")) {
			//			createImage(_datos_qr);
			//			SendDataByte(Command.LF);
			//			SendDataByte(Command.LF);
					}


					SendDataByte(Command.LF);
					try { Thread.sleep(1000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }


					try { Thread.sleep(8000); }
					catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }


					mService.stop();

					imageViewPicture.setEnabled(true);
					//	width_58mm.setEnabled(false);
					//	width_80.setEnabled(false);
					//	hexBox.setEnabled(false);
					sendButton.setEnabled(false);
					sendCierre.setEnabled(false);
					sendNube.setEnabled(false);
					sendNube_Clientes.setEnabled(false);
					recibeNube_Clientes.setEnabled(false);
					recibeNube_Productos.setEnabled(false);


					btnClose.setEnabled(false);

					btn_camer.setEnabled(false);
					btn_scqrcode.setEnabled(false);
					btnScanButton.setEnabled(true);
					//	Simplified.setEnabled(false);

					btnScanButton.setText(getText(R.string.connect));


					//Intent serverIntent = new Intent(Modificar2.this, Modificar2.class);
					//startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);









					Intent intent = new Intent(this, Main_Activity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish(); // call this to finish the current activity




					break;




				}
			}
			break;
		}




            case R.id.Send_Cierre:{

/////////////////////////////////////////////////////////////////////

				if (false) {
					String str = " asd fa sf asf asd ";
					if(str.length() > 0){
						str = Other.RemoveChar(str, ' ').toString();
						if (str.length() <= 0)
							return;
						if ((str.length() % 2) != 0) {
							Toast.makeText(getApplicationContext(), getString(R.string.msg_state),
									Toast.LENGTH_SHORT).show();
							return;
						}
						byte[] buf = Other.HexStringToBytes(str);
						SendDataByte(buf);
					}else{
						Toast.makeText(Modificar2.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
					}
				} else {


					// manda Imprimir el Ticket POS con QR
					String msg = _Reporte_cierre(Mfecha);
					if(msg.length()>0){


						// primera impresion


					//	Print_BMP();

						String _buttom="";
						String _salto ="\n";

						String _linea = "================================";

						SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
						SendDataByte(Command.LF);

						Intent intent = new Intent(this, Main_Activity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish(); // call this to finish the current activity

						break;

					}

				}
				break;

/////////////////////////////////////////////////////////////////////

        }




            case R.id.btn_printpicture:{
			GraphicalPrint();
			break;
			}


			case R.id.button_Reset: {
				mService.stop();

				imageViewPicture.setEnabled(true);
				//	width_58mm.setEnabled(false);
				//	width_80.setEnabled(false);
				//	hexBox.setEnabled(false);
				sendButton.setEnabled(false);
				sendCierre.setEnabled(false);
				sendNube.setEnabled(false);
				sendNube_Clientes.setEnabled(false);
				recibeNube_Clientes.setEnabled(false);
				recibeNube_Productos.setEnabled(false);



				btnClose.setEnabled(false);


				btn_camer.setEnabled(false);
				btn_scqrcode.setEnabled(false);
				btnScanButton.setEnabled(true);
				//	Simplified.setEnabled(false);

				btnScanButton.setText(getText(R.string.connect));
				break;

			//	Toast.makeText(this, "la impresora fue reseteada", Toast.LENGTH_SHORT).show();


			}

		case R.id.imageViewPictureUSB:{



			Intent loadpicture = new Intent(
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(loadpicture, REQUEST_CHOSE_BMP);







			break;
		}


			case R.id.Send_Nube:{




					Toast.makeText(this, "Lista para subir a la nube", Toast.LENGTH_LONG).show();



				if (false) {
					String str = " asd fa sf asf asd ";
					if(str.length() > 0){
						str = Other.RemoveChar(str, ' ').toString();
						if (str.length() <= 0)
							return;
						if ((str.length() % 2) != 0) {
							Toast.makeText(getApplicationContext(), getString(R.string.msg_state),
									Toast.LENGTH_SHORT).show();
							return;
						}
						byte[] buf = Other.HexStringToBytes(str);
						SendDataByte(buf);
					}else{
						Toast.makeText(Modificar2.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
					}
				} else {


					// manda Imprimir el Ticket POS con QR
					String msg = _Reporte_Nube(Mfecha);
					if(msg.length()>0){


						// primera impresion


						//	Print_BMP();

						String _buttom="";
						String _salto ="\n";

						String _linea = "================================";

						SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
						SendDataByte(Command.LF);

				//		Intent intent = new Intent(this, Main_Activity.class);
				//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				//		startActivity(intent);
				//		finish(); // call this to finish the current activity

				//		break;

					}

				}
				break;

/////////////////////////////////////////////////////////////////////






			}


			case R.id.Send_Nube_Clientes:{




				Toast.makeText(this, "Lista para subir a la nube", Toast.LENGTH_LONG).show();



				if (false) {
					String str = " asd fa sf asf asd ";
					if(str.length() > 0){
						str = Other.RemoveChar(str, ' ').toString();
						if (str.length() <= 0)
							return;
						if ((str.length() % 2) != 0) {
							Toast.makeText(getApplicationContext(), getString(R.string.msg_state),
									Toast.LENGTH_SHORT).show();
							return;
						}
						byte[] buf = Other.HexStringToBytes(str);
						SendDataByte(buf);
					}else{
						Toast.makeText(Modificar2.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
					}
				} else {


					// manda Imprimir el Ticket POS con QR
					String msg = _Reporte_Nube_Clientes(Mfecha);
					if(msg.length()>0){


						// primera impresion


						//	Print_BMP();

						String _buttom="";
						String _salto ="\n";

						String _linea = "================================";

						SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
						SendDataByte(Command.LF);

						//		Intent intent = new Intent(this, Main_Activity.class);
						//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						//		startActivity(intent);
						//		finish(); // call this to finish the current activity

						//		break;

					}

				}
				break;

/////////////////////////////////////////////////////////////////////






			}




			case R.id.Descargar_Nube_Clientes:{




				Toast.makeText(this, "Lista para subir a la nube", Toast.LENGTH_LONG).show();



				if (false) {
					String str = " asd fa sf asf asd ";
					if(str.length() > 0){
						str = Other.RemoveChar(str, ' ').toString();
						if (str.length() <= 0)
							return;
						if ((str.length() % 2) != 0) {
							Toast.makeText(getApplicationContext(), getString(R.string.msg_state),
									Toast.LENGTH_SHORT).show();
							return;
						}
						byte[] buf = Other.HexStringToBytes(str);
						SendDataByte(buf);
					}else{
						Toast.makeText(Modificar2.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
					}
				} else {


					// manda Imprimir el Ticket POS con QR
					String msg = _Reporte_Descarga_Nube_Clientes(Mfecha);
					if(msg.length()>0){


						String _buttom="";
						String _salto ="\n";

						String _linea = "================================";

						SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
						SendDataByte(Command.LF);


					}

				}
				break;

/////////////////////////////////////////////////////////////////////






			}



			case R.id.Descargar_Nube_Productos:{




				Toast.makeText(this, "Lista para subir a la nube", Toast.LENGTH_LONG).show();



				if (false) {
					String str = " asd fa sf asf asd ";
					if(str.length() > 0){
						str = Other.RemoveChar(str, ' ').toString();
						if (str.length() <= 0)
							return;
						if ((str.length() % 2) != 0) {
							Toast.makeText(getApplicationContext(), getString(R.string.msg_state),
									Toast.LENGTH_SHORT).show();
							return;
						}
						byte[] buf = Other.HexStringToBytes(str);
						SendDataByte(buf);
					}else{
						Toast.makeText(Modificar2.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
					}
				} else {


					// manda Imprimir el Ticket POS con QR
					String msg = _Reporte_Descarga_Nube_Productos(Mfecha);
					if(msg.length()>0){


						String _buttom="";
						String _salto ="\n";

						String _linea = "================================";

						SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
						SendDataByte(Command.LF);


					}

				}
				break;

/////////////////////////////////////////////////////////////////////






			}







			case R.id.btn_scqr:{
			createImage("just test");
			break;
		}
		case R.id.btn_dyca:{
			dispatchTakePictureIntent(REQUEST_CAMER);
			break;
		}

			case R.id.btn_alfilPOS:{
				Intent serverIntent = new Intent(Modificar2.this, init_alfilPOS.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				break;
            }
		default:
			break;
		}
	}
		
/*****************************************************************************************************/
	/*
	 * SendDataString
	 */
	private void SendDataString(String data) {
		
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (data.length() > 0) {				
			try {
				mService.write(data.getBytes("GBK"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 *SendDataByte 
	 */
	private void SendDataByte(byte[] data) {
		
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}			
		mService.write(data);
	}

	/****************************************************************************************************/
	@SuppressLint("HandlerLeak") 
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (DEBUG)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					btnScanButton.setText(getText(R.string.Connecting));
					Print_Test();//
					btnScanButton.setEnabled(false);

					imageViewPicture.setEnabled(true);

					sendButton.setEnabled(true);
					sendCierre.setEnabled(true);
					sendNube.setEnabled(isNetworkAvailable());
					sendNube_Clientes.setEnabled(isNetworkAvailable());
					recibeNube_Clientes.setEnabled(isNetworkAvailable());
					recibeNube_Productos.setEnabled(isNetworkAvailable());


					btnClose.setEnabled(true);



					btn_camer.setEnabled(true);
					btn_scqrcode.setEnabled(true);


					break;
				case BluetoothService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				
				break;
			case MESSAGE_READ:
				
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                Toast.makeText(getApplicationContext(), "Device connection was lost",
                               Toast.LENGTH_SHORT).show();



				imageViewPicture.setEnabled(false);

				sendButton.setEnabled(false);
				sendCierre.setEnabled(false);
				sendNube.setEnabled(false);
				sendNube_Clientes.setEnabled(false);
				recibeNube_Clientes.setEnabled(false);
				recibeNube_Productos.setEnabled(false);


				btnClose.setEnabled(false);


				btn_camer.setEnabled(false);
				btn_scqrcode.setEnabled(false);


                break;
            case MESSAGE_UNABLE_CONNECT:     //无法连接设备
            	Toast.makeText(getApplicationContext(), "Unable to connect device",
                        Toast.LENGTH_SHORT).show();
            	break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (DEBUG)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:{
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {
					// Get the device MAC address
					String address = data.getExtras().getString(
							DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					// Get the BLuetoothDevice object
					if (BluetoothAdapter.checkBluetoothAddress(address)) {
						BluetoothDevice device = mBluetoothAdapter
								.getRemoteDevice(address);
						// Attempt to connect to the device
						mService.connect(device);
					}
				}
				break;
			}
			case REQUEST_ENABLE_BT:{
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {
					// Bluetooth is now enabled, so set up a session
					KeyListenerInit();
				} else {
					// User did not enable Bluetooth or an error occured
					Log.d(TAG, "BT not enabled");
					Toast.makeText(this, R.string.bt_not_enabled_leaving,
							Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			}
			case REQUEST_CHOSE_BMP:{
	        	if (resultCode == Activity.RESULT_OK){


					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaColumns.DATA };
		
					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();
		
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();
		
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(picturePath, opts);
					opts.inJustDecodeBounds = false;
					if (opts.outWidth > 1200) {
						opts.inSampleSize = opts.outWidth / 1200;
					}

					Bitmap bitmap = BitmapFactory.decodeFile(picturePath, opts);

			//		Toast.makeText(this, "parce que sige por aqui", Toast.LENGTH_LONG).show();


					// excribir en discp

					int _myId = 1;



					if (null == bitmap) {
						Toast.makeText(this, " << NO SE PUDO ESCRIBIR >> de Permisos de Acceso a Memoria del Dispositivo, valla a configuracion de Aplicaciones y a alfiPOS para derle Permisos de Acceso", Toast.LENGTH_LONG).show();
						Toast.makeText(this, " << NO SE PUDO ESCRIBIR >> de Permisos de Acceso a Memoria del Dispositivo, valla a configuracion de Aplicaciones y a alfiPOS para derle Permisos de Acceso", Toast.LENGTH_LONG).show();

					//	imageViewPicture.setImageBitmap(bitmap);


					} else {
						Escribir_bmp(_myId, bitmap);
					}

	        	}else{
					Toast.makeText(this, getString(R.string.msg_statev1), Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case REQUEST_CAMER:{
	        	if (resultCode == Activity.RESULT_OK){
	        		handleSmallCameraPhoto(data);
	        	}else{
	        		Toast.makeText(this, getText(R.string.camer), Toast.LENGTH_SHORT).show();
	        	}
	        	break;
	        }
		}
	}

/****************************************************************************************************/
	/**
	 * 连接成功后打印测试页
	 */
	private void Print_Test(){
		String lang = getString(R.string.strLang);
  		if((lang.compareTo("en")) == 0){
			String msg = "ok\n\n";

			String data = "...\n";
		//	String data2 = "<<<   www.factura.global  >>>\n\n\n";

		//	SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 1, 1, 0));
			SendDataByte(PrinterCommand.POS_Print_Text(data, CHINESE, 0, 0, 0, 0));
		//	SendDataByte(PrinterCommand.POS_Set_Cut(1));
			SendDataByte(PrinterCommand.POS_Set_PrtInit());
		}
  	}
	
	/**
	 * 打印测试页
	// * @param mPrinter
	 */
	private void BluetoothPrintTest() {
		String msg = "";
		String lang = getString(R.string.strLang);
		if((lang.compareTo("en")) == 0 ){
			msg = "La Conexion entre el DispositivoAndorid ha sido establecida.\nalfilPOS es una Solucion de     FACTURA GLOBAL SAC\nAlejandro Gandara 954 879 529 \ncorreo:  soporte@factura.global\nweb:  www.factura.global \n\n\n";
			SendDataString(msg);
		}
	}
	
	/*
	 * 打印图片
	 */
	private void Print_BMP(){

	//	byte[] buffer = PrinterCommand.POS_Set_PrtInit();
		Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable())
				.getBitmap();


		mBitmap = _get_logo(1);



		int nMode = 0;
		int nPaperWidth = 384;
		if(true)
			nPaperWidth = 384;

		else if (false)
			nPaperWidth = 576;
		if(mBitmap != null)
		{
			/**
			 * Parameters:
			 * mBitmap  要打印的图片
			 * nWidth   打印宽度（58和80）
			 * nMode    打印模式
			 * Returns: byte[]
			 */
			byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
		//	SendDataByte(buffer);
			SendDataByte(Command.ESC_Init);
			SendDataByte(Command.LF);
			SendDataByte(data);
			SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(10));
			SendDataByte(PrinterCommand.POS_Set_Cut(1));
			SendDataByte(PrinterCommand.POS_Set_PrtInit());
		}		
	}

	/**
	 * 打印自定义表格
	 */
	@SuppressLint("SimpleDateFormat") 
	private void PrintTable(){

		String lang = getString(R.string.strLang);
		if((lang.compareTo("cn")) == 0){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		String date = str + "\n\n\n\n\n\n";	
		if(is58mm){

			Command.ESC_Align[2] = 0x02;
			byte[][] allbuf;
			try {
				allbuf = new byte[][]{

						Command.ESC_Init, Command.ESC_Three,
						String.format("┏━━┳━━━┳━━┳━━━━┓\n").getBytes("GBK"),
						String.format("┃发站┃%-4s┃到站┃%-6s┃\n","深圳","成都").getBytes("GBK"),
						String.format("┣━━╋━━━╋━━╋━━━━┫\n").getBytes("GBK"),
						String.format("┃件数┃%2d/%-3d┃单号┃%-8d┃\n",1,222,555).getBytes("GBK"),
						String.format("┣━━┻┳━━┻━━┻━━━━┫\n").getBytes("GBK"),
						String.format("┃收件人┃%-12s┃\n","【送】测试/测试人").getBytes("GBK"),
						String.format("┣━━━╋━━┳━━┳━━━━┫\n").getBytes("GBK"),
						String.format("┃业务员┃%-2s┃名称┃%-6s┃\n","测试","深圳").getBytes("GBK"),
						String.format("┗━━━┻━━┻━━┻━━━━┛\n").getBytes("GBK"),
						Command.ESC_Align, "\n".getBytes("GBK")
				};
				byte[] buf = Other.byteArraysToBytes(allbuf);
				SendDataByte(buf);
				SendDataString(date);
				SendDataByte(Command.GS_V_m_n);
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else {

			Command.ESC_Align[2] = 0x02;
			byte[][] allbuf;
			try {
				allbuf = new byte[][]{

						Command.ESC_Init, Command.ESC_Three,
						String.format("┏━━┳━━━━━━━┳━━┳━━━━━━━━┓\n").getBytes("GBK"),
						String.format("┃发站┃%-12s┃到站┃%-14s┃\n", "深圳", "成都").getBytes("GBK"),
						String.format("┣━━╋━━━━━━━╋━━╋━━━━━━━━┫\n").getBytes("GBK"),
						String.format("┃件数┃%6d/%-7d┃单号┃%-16d┃\n", 1, 222, 55555555).getBytes("GBK"),
						String.format("┣━━┻┳━━━━━━┻━━┻━━━━━━━━┫\n").getBytes("GBK"),
						String.format("┃收件人┃%-28s┃\n", "【送】测试/测试人").getBytes("GBK"),
						String.format("┣━━━╋━━━━━━┳━━┳━━━━━━━━┫\n").getBytes("GBK"),
						String.format("┃业务员┃%-10s┃名称┃%-14s┃\n", "测试", "深圳").getBytes("GBK"),
						String.format("┗━━━┻━━━━━━┻━━┻━━━━━━━━┛\n").getBytes("GBK"),
						Command.ESC_Align, "\n".getBytes("GBK")
				};
				byte[] buf = Other.byteArraysToBytes(allbuf);
				SendDataByte(buf);
				SendDataString(date);
				SendDataByte(Command.GS_V_m_n);
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		}else if((lang.compareTo("en")) == 0){
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd/ HH:mm:ss ");
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			String str = formatter.format(curDate);
			String date = str + "\n\n\n\n\n\n";	
			if(is58mm){

				Command.ESC_Align[2] = 0x02;
				byte[][] allbuf;
				try {
					allbuf = new byte[][]{

							Command.ESC_Init, Command.ESC_Three,
							String.format("┏━━┳━━━┳━━┳━━━━┓\n").getBytes("GBK"),
							String.format("┃XXXX┃%-6s┃XXXX┃%-8s┃\n","XXXX","XXXX").getBytes("GBK"),
							String.format("┣━━╋━━━╋━━╋━━━━┫\n").getBytes("GBK"),
							String.format("┃XXXX┃%2d/%-3d┃XXXX┃%-8d┃\n",1,222,555).getBytes("GBK"),
							String.format("┣━━┻┳━━┻━━┻━━━━┫\n").getBytes("GBK"),
							String.format("┃XXXXXX┃%-18s┃\n","【XX】XXXX/XXXXXX").getBytes("GBK"),
							String.format("┣━━━╋━━┳━━┳━━━━┫\n").getBytes("GBK"),
							String.format("┃XXXXXX┃%-2s┃XXXX┃%-8s┃\n","XXXX","XXXX").getBytes("GBK"),
							String.format("┗━━━┻━━┻━━┻━━━━┛\n").getBytes("GBK"),
							Command.ESC_Align, "\n".getBytes("GBK")
					};
					byte[] buf = Other.byteArraysToBytes(allbuf);
					SendDataByte(buf);
					SendDataString(date);
					SendDataByte(Command.GS_V_m_n);
				} catch (UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}else {

				Command.ESC_Align[2] = 0x02;
				byte[][] allbuf;
				try {
					allbuf = new byte[][]{

							Command.ESC_Init, Command.ESC_Three,
							String.format("┏━━┳━━━━━━━┳━━┳━━━━━━━━┓\n").getBytes("GBK"),
							String.format("┃XXXX┃%-14s┃XXXX┃%-16s┃\n", "XXXX", "XXXX").getBytes("GBK"),
							String.format("┣━━╋━━━━━━━╋━━╋━━━━━━━━┫\n").getBytes("GBK"),
							String.format("┃XXXX┃%6d/%-7d┃XXXX┃%-16d┃\n", 1, 222, 55555555).getBytes("GBK"),
							String.format("┣━━┻┳━━━━━━┻━━┻━━━━━━━━┫\n").getBytes("GBK"),
							String.format("┃XXXXXX┃%-34s┃\n", "【XX】XXXX/XXXXXX").getBytes("GBK"),
							String.format("┣━━━╋━━━━━━┳━━┳━━━━━━━━┫\n").getBytes("GBK"),
							String.format("┃XXXXXX┃%-12s┃XXXX┃%-16s┃\n", "XXXX", "XXXX").getBytes("GBK"),
							String.format("┗━━━┻━━━━━━┻━━┻━━━━━━━━┛\n").getBytes("GBK"),
							Command.ESC_Align, "\n".getBytes("GBK")
					};
					byte[] buf = Other.byteArraysToBytes(allbuf);
					SendDataByte(buf);
					SendDataString(date);
					SendDataByte(Command.GS_V_m_n);
				} catch (UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			}
	}

	/**
	 * 打印自定义小票
	 */
	@SuppressLint("SimpleDateFormat") 
	private void Print_Ex(){

		String lang = getString(R.string.strLang);
		if((lang.compareTo("cn")) == 0){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		String date = str + "\n\n\n\n\n\n";	
		if (is58mm) {

			try {
				byte[] qrcode = PrinterCommand.getBarCommand("热敏打印机!", 0, 3, 6);//
				Command.ESC_Align[2] = 0x01;
				SendDataByte(Command.ESC_Align);
				SendDataByte(qrcode);

				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x11;
				SendDataByte(Command.GS_ExclamationMark);
				SendDataByte("NIKE专卖店\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x00;
				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x00;
				SendDataByte(Command.GS_ExclamationMark);
				SendDataByte("门店号: 888888\n单据  S00003333\n收银员：1001\n单据日期：xxxx-xx-xx\n打印时间：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
				SendDataByte("品名       数量    单价    金额\nNIKE跑鞋   10.00   899     8990\nNIKE篮球鞋 10.00   1599    15990\n".getBytes("GBK"));
				SendDataByte("数量：                20.00\n总计：                16889.00\n付款：                17000.00\n找零：                111.00\n".getBytes("GBK"));
				SendDataByte("公司名称：NIKE\n公司网址：www.xxx.xxx\n地址：深圳市xx区xx号\n电话：0755-11111111\n服务专线：400-xxx-xxxx\n================================\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x01;
				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x11;
				SendDataByte(Command.GS_ExclamationMark);
				SendDataByte("谢谢惠顾,欢迎再次光临!\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x00;
				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x00;
				SendDataByte(Command.GS_ExclamationMark);
				
				SendDataByte("(以上信息为测试模板,如有苟同，纯属巧合!)\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x02;
				SendDataByte(Command.ESC_Align);
				SendDataString(date);
				SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
				SendDataByte(Command.GS_V_m_n);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				byte[] qrcode = PrinterCommand.getBarCommand("热敏打印机!", 0, 3, 6);
				Command.ESC_Align[2] = 0x01;
				SendDataByte(Command.ESC_Align);
				SendDataByte(qrcode);

				Command.ESC_Align[2] = 0x01;
				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x11;
				SendDataByte(Command.GS_ExclamationMark);
				SendDataByte("NIKE专卖店\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x00;
				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x00;
				SendDataByte(Command.GS_ExclamationMark);
				SendDataByte("门店号: 888888\n单据  S00003333\n收银员：1001\n单据日期：xxxx-xx-xx\n打印时间：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
				SendDataByte("品名            数量    单价    金额\nNIKE跑鞋        10.00   899     8990\nNIKE篮球鞋      10.00   1599    15990\n".getBytes("GBK"));
				SendDataByte("数量：                20.00\n总计：                16889.00\n付款：                17000.00\n找零：                111.00\n".getBytes("GBK"));
				SendDataByte("公司名称：NIKE\n公司网址：www.xxx.xxx\n地址：深圳市xx区xx号\n电话：0755-11111111\n服务专线：400-xxx-xxxx\n===========================================\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x01;
				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x11;
				SendDataByte(Command.GS_ExclamationMark);
				SendDataByte("谢谢惠顾,欢迎再次光临!\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x00;
				SendDataByte(Command.ESC_Align);
				Command.GS_ExclamationMark[2] = 0x00;
				SendDataByte(Command.GS_ExclamationMark);
				SendDataByte("(以上信息为测试模板,如有苟同，纯属巧合!)\n".getBytes("GBK"));
				Command.ESC_Align[2] = 0x02;
				SendDataByte(Command.ESC_Align);
				SendDataString(date);
				SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
				SendDataByte(Command.GS_V_m_n);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}else if((lang.compareTo("en")) == 0){
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd/ HH:mm:ss ");
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			String str = formatter.format(curDate);
			String date = str + "\n\n\n\n\n\n";	
			if (is58mm) {

				try {
					byte[] qrcode = PrinterCommand.getBarCommand("Zijiang Electronic Thermal Receipt Printer!", 0, 3, 6);//
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					SendDataByte(qrcode);

					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("NIKE Shop\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Number:  888888\nReceipt  S00003333\nCashier：1001\nDate：xxxx-xx-xx\nPrint Time：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
					SendDataByte("Name    Quantity    price  Money\nShoes   10.00       899     8990\nBall    10.00       1599    15990\n".getBytes("GBK"));
					SendDataByte("Quantity：             20.00\ntotal：                16889.00\npayment：              17000.00\nKeep the change：      111.00\n".getBytes("GBK"));
					SendDataByte("company name：NIKE\nSite：www.xxx.xxx\naddress：ShenzhenxxAreaxxnumber\nphone number：0755-11111111\nHelpline：400-xxx-xxxx\n================================\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Welcome again!\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);
					
					SendDataByte("(The above information is for testing template, if agree, is purely coincidental!)\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x02;
					SendDataByte(Command.ESC_Align);
					SendDataString(date);
					SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
					SendDataByte(Command.GS_V_m_n);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					byte[] qrcode = PrinterCommand.getBarCommand("Zijiang Electronic Thermal Receipt Printer!", 0, 3, 8);
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					SendDataByte(qrcode);

					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("NIKE Shop\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Number: 888888\nReceipt  S00003333\nCashier：1001\nDate：xxxx-xx-xx\nPrint Time：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
					SendDataByte("Name                    Quantity price  Money\nNIKErunning shoes        10.00   899     8990\nNIKEBasketball Shoes     10.00   1599    15990\n".getBytes("GBK"));
					SendDataByte("Quantity：               20.00\ntotal：                  16889.00\npayment：                17000.00\nKeep the change：                111.00\n".getBytes("GBK"));
					SendDataByte("company name：NIKE\nSite：www.xxx.xxx\naddress：shenzhenxxAreaxxnumber\nphone number：0755-11111111\nHelpline：400-xxx-xxxx\n================================================\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Welcome again!\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("(The above information is for testing template, if agree, is purely coincidental!)\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x02;
					SendDataByte(Command.ESC_Align);
					SendDataString(date);
					SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
					SendDataByte(Command.GS_V_m_n);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 打印条码、二维码
	 */
	private void printBarCode() {
		
		new AlertDialog.Builder(Modificar2.this).setTitle(getText(R.string.btn_prtcode))
		.setItems(codebar, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SendDataByte(byteCodebar[which]);
				String str = " sdf sdf gs dfg ";
				if(which == 0)
				{
					if(str.length() == 11 || str.length() == 12)
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 65, 3, 168, 0, 2);
						SendDataByte(new byte[]{0x1b, 0x61, 0x00});
						SendDataString("UPC_A\n");
						SendDataByte(code);
					}else {
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				else if(which == 1)
				{
					if(str.length() == 6 || str.length() == 7)
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 66, 3, 168, 0, 2);
						SendDataByte(new byte[]{0x1b, 0x61, 0x00});
						SendDataString("UPC_E\n");
						SendDataByte(code);
					}else {
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				else if(which == 2)
				{
					if(str.length() == 12 || str.length() == 13)
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 67, 3, 168, 0, 2);
						SendDataByte(new byte[]{0x1b, 0x61, 0x00});
						SendDataString("JAN13(EAN13)\n");
						SendDataByte(code);
					}else {
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				else if(which == 3)
				{
					if(str.length() >0 )
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 68, 3, 168, 0, 2);
						SendDataByte(new byte[]{0x1b, 0x61, 0x00});
						SendDataString("JAN8(EAN8)\n");
						SendDataByte(code);
					}else {
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				else if(which == 4)
				{
					if(str.length() == 0)
					{
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 69, 3, 168, 1, 2);
						SendDataString("CODE39\n");
						SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
						SendDataByte(code);
					}
				}
				else if(which == 5)
				{
					if(str.length() == 0)
					{
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 70, 3, 168, 1, 2);
						SendDataString("ITF\n");
						SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
						SendDataByte(code);
					}
				}
				else if(which == 6)
				{
					if(str.length() == 0)
					{
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 71, 3, 168, 1, 2);
						SendDataString("CODABAR\n");
						SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
						SendDataByte(code);
					}
				}
				else if(which == 7)
				{
					if(str.length() == 0)
					{
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 72, 3, 168, 1, 2);
						SendDataString("CODE93\n");
						SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
						SendDataByte(code);
					}
				}
				else if(which == 8)
				{
					if(str.length() == 0)
					{
						Toast.makeText(Modificar2.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						byte[] code = PrinterCommand.getCodeBarCommand(str, 73, 3, 168, 1, 2);
						SendDataString("CODE128\n");
						SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
						SendDataByte(code);
					}
				}
				else if(which == 9)
				{
					if(str.length() == 0)
					{
						Toast.makeText(Modificar2.this, getText(R.string.empty1), Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						byte[] code = PrinterCommand.getBarCommand(str, 1, 3, 8);
						SendDataString("QR Code\n");
						SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
						SendDataByte(code);
					}
				}
			}
		}).create().show();	
	}

	/**
	 * public static Bitmap createAppIconText(Bitmap icon, String txt, boolean is58mm, int hight)
	 * Bitmap  icon     源图
	 * String txt       要转换的字符串  
	 * boolean is58mm   打印宽度(58和80)
	 * int hight        转换后的图片高度
	 */
	private void GraphicalPrint(){

		String txt_msg = " dfg sdf df g";
		if(txt_msg.length() == 0){
			Toast.makeText(Modificar2.this, getText(R.string.empty1), Toast.LENGTH_SHORT).show();
			return;
		}else{
			Bitmap bm1 = getImageFromAssetsFile(".\\demo.bmp");
			if(true){
				
				Bitmap bmp = Other.createAppIconText(bm1,txt_msg,25,is58mm,200);
				int nMode = 0;
				int nPaperWidth = 384;
				
				if(bmp != null)
				{
					byte[] data = PrintPicture.POS_PrintBMP(bmp, nPaperWidth, nMode);
					SendDataByte(Command.ESC_Init);
					SendDataByte(Command.LF);
					SendDataByte(data);
					SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
					SendDataByte(PrinterCommand.POS_Set_Cut(1));
					SendDataByte(PrinterCommand.POS_Set_PrtInit());
				}
			}
			else if (false){
				Bitmap bmp = Other.createAppIconText(bm1,txt_msg,25,false,200);
				int nMode = 0;
				
				int nPaperWidth = 576;
				if(bmp != null)
				{
					byte[] data = PrintPicture.POS_PrintBMP(bmp, nPaperWidth, nMode);
					SendDataByte(Command.ESC_Init);
					SendDataByte(Command.LF);
					SendDataByte(data);
					SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
					SendDataByte(PrinterCommand.POS_Set_Cut(1));
					SendDataByte(PrinterCommand.POS_Set_PrtInit());
				}
			}
		}
	}
	
	/**
	 * 打印指令测试
	 */
	private void CommandTest(){

		String lang = getString(R.string.strLang);
		if((lang.compareTo("cn")) == 0){
		new AlertDialog.Builder(Modificar2.this).setTitle(getText(R.string.chosecommand))
		.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SendDataByte(byteCommands[which]);
				try {
					if(which == 16 || which == 17 || which == 18 || which == 19 || which == 22
					|| which == 23 || which == 24|| which == 0 || which == 1 || which == 27){
						return ;
					}else {
						SendDataByte("热敏票据打印机ABCDEFGabcdefg123456,.;'/[{}]!\n热敏票据打印机ABCDEFGabcdefg123456,.;'/[{}]!\n热敏票据打印机ABCDEFGabcdefg123456,.;'/[{}]!\n热敏票据打印机ABCDEFGabcdefg123456,.;'/[{}]!\n热敏票据打印机ABCDEFGabcdefg123456,.;'/[{}]!\n热敏票据打印机ABCDEFGabcdefg123456,.;'/[{}]!\n".getBytes("GBK"));
					}
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).create().show();	
		}else if((lang.compareTo("en")) == 0){
			new AlertDialog.Builder(Modificar2.this).setTitle(getText(R.string.chosecommand))
			.setItems(itemsen, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					SendDataByte(byteCommands[which]);
					try {
						if(which == 16 || which == 17 || which == 18 || which == 19 || which == 22
						|| which == 23 || which == 24|| which == 0 || which == 1 || which == 27){
							return ;
						}else {
							SendDataByte("Thermal Receipt Printer ABCDEFGabcdefg123456,.;'/[{}]!\nThermal Receipt PrinterABCDEFGabcdefg123456,.;'/[{}]!\nThermal Receipt PrinterABCDEFGabcdefg123456,.;'/[{}]!\nThermal Receipt PrinterABCDEFGabcdefg123456,.;'/[{}]!\nThermal Receipt PrinterABCDEFGabcdefg123456,.;'/[{}]!\nThermal Receipt PrinterABCDEFGabcdefg123456,.;'/[{}]!\n".getBytes("GBK"));
						}
						
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).create().show();	
			}
	}
/************************************************************************************************/	
	/* 
	 * 生成QR图 
	 */
    private void createImage(String _codigo_qr) {
	        try {
	            // 需要引入zxing包
	            QRCodeWriter writer = new QRCodeWriter();

	            String text = _codigo_qr;

	            Log.i(TAG, "生成的文本：" + text);
	            if (text == null || "".equals(text) || text.length() < 1) {
	            	Toast.makeText(this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
	            	return;
	            }

	            // 把输入的文本转为二维码
	            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
	                    QR_WIDTH, QR_HEIGHT);

	            System.out.println("w:" + martix.getWidth() + "h:"
	                    + martix.getHeight());

	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
	            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
	                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	            for (int y = 0; y < QR_HEIGHT; y++) {
	                for (int x = 0; x < QR_WIDTH; x++) {
	                    if (bitMatrix.get(x, y)) {
	                        pixels[y * QR_WIDTH + x] = 0xff000000;
	                    } else {
	                        pixels[y * QR_WIDTH + x] = 0xffffffff;
	                    }

	                }
	            }

	            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
	                    Bitmap.Config.ARGB_8888);

	            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	            
	            byte[] data = PrintPicture.POS_PrintBMP(bitmap, 384, 0);
	            SendDataByte(data);
	            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(15));
				SendDataByte(PrinterCommand.POS_Set_Cut(1));
				SendDataByte(PrinterCommand.POS_Set_PrtInit());
	        } catch (WriterException e) {
	            e.printStackTrace();
	        }
	    }
//************************************************************************************************//
  	/*
  	 * 调用系统相机
  	 */
  	private void dispatchTakePictureIntent(int actionCode) {
  	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
  	    startActivityForResult(takePictureIntent, actionCode);
  	}
  	
  	private void handleSmallCameraPhoto(Intent intent) {
  	    Bundle extras = intent.getExtras();
  	    Bitmap mImageBitmap = (Bitmap) extras.get("data");
  	    imageViewPicture.setImageBitmap(mImageBitmap);
  	}

    /****************************************************************************************************/
	 /**
	 * 加载assets文件资源
	 */
	private Bitmap getImageFromAssetsFile(String fileName) {
			Bitmap image = null;
			AssetManager am = getResources().getAssets();
			try {
				InputStream is = am.open(fileName);
				image = BitmapFactory.decodeStream(is);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return image;

		}

    public static String _cantidad(double _numero) {
        String _regreso;
        simb = new DecimalFormatSymbols();
        simb.setDecimalSeparator('.');
        simb.setGroupingSeparator(',');
        df = new DecimalFormat("##0.00", simb);
        _regreso=padLeft(df.format(_numero),6);
        return _regreso;

        // El resultado sería el siguiente: 94.751.890,37
    }

	public static String _folio_str(String _str) {


		double d = Double.parseDouble(_str); // returns double primitive

		String _regreso;
		simb = new DecimalFormatSymbols();
		simb.setDecimalSeparator('.');
		simb.setGroupingSeparator(',');
		df = new DecimalFormat("######0", simb);
		_regreso=padLeft(df.format(d),6);
		return _regreso;

		// El resultado sería el siguiente: 94.751.890,37
	}



    public static String _dinero(double _numero) {
        String _regreso;
        simb = new DecimalFormatSymbols();
        simb.setDecimalSeparator('.');
        simb.setGroupingSeparator(',');
        df = new DecimalFormat("###,###.00", simb);

        _regreso=padLeft(df.format(_numero),11);
        return _regreso;

        // El resultado sería el siguiente: 94.751.890,37
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }


    private void Modificar(int _id,
                           String _serie,
                           String _folio,
                           String _ruc,
                           String _razon_social,
                           String _direccion,
                           String _moneda,
                           String _correo) {
        // String _fecha) {


        ContentValues valoresCabecera = new ContentValues();
        valoresCabecera.put(SERIE, _serie);
        valoresCabecera.put(FOLIO, _folio);
        //     valoresCabecera.put(FECHA, _fecha);
        valoresCabecera.put(RUC, _ruc);
        valoresCabecera.put(RAZON_SOCIAL, _razon_social);
        valoresCabecera.put(DIRECCION, _direccion);
        valoresCabecera.put(MONEDA, _moneda);
        valoresCabecera.put(CORREO, _correo);

        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Modificar2.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }


	private void Modificar_Cabecera(int _id,
						   Double _gravado,
						   Double _exonerada,
						   Double _inafecta,
						   Double _igv,
						   Double _subtotal,
						   Double _total) {
		// String _fecha) {

		//    public static final String TABLE_ID = "_id";
//    public static final String SERIE = "serie";
//    public static final String FOLIO = "folio";
//    public static final String FECHA = "fecha";
//    public static final String RUC = "ruc";
//    public static final String RAZON_SOCIAL = "razon_social";
//    public static final String DIRECCION = "direccion";
//    public static final String MONEDA = "moneda";
//    public static final String GRAVADO = "gravado";
//    public static final String EXCENTO = "excento";
//    public static final String INAFECTO = "inafecto";
//    public static final String SUBTOTAL = "subtotal";
//    public static final String IGV = "igv";
//    public static final String TOTAL = "total";
//    public static final String CORREO = "correo";
//    public static final String SERIE_REL = "serie_rel";
//    public static final String FOLIO_REL = "folio_rel";
//    public static final String ARCHIVADA = "archivada";
//    public static final String ENVIADO_CORREO = "enviado_correo";
//    public static final String ENVIADO_NUBE = "enviado_nube";




		ContentValues valoresCabecera = new ContentValues();
		valoresCabecera.put(GRAVADO, _gravado);
		valoresCabecera.put(EXCENTO, _exonerada);
		valoresCabecera.put(INAFECTO, _inafecta);
		valoresCabecera.put(IGV, _igv);
		valoresCabecera.put(SUBTOTAL, _subtotal);
		valoresCabecera.put(TOTAL, _total);


		db = new connectionDB(this);

		String _alcance = "WHERE _id="+_id;

		db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
		Toast.makeText(Modificar2.this,"Se Modifico el Reg. Cabecera :"+_id, Toast.LENGTH_SHORT ).show();

		//Intent intent = new Intent(this,init_alfilPOS.class );
		//startActivity(intent);

	}



	private void Archivar(int _id) {

		ContentValues valoresCabecera = new ContentValues();
		valoresCabecera.put("archivada", 1);


		db = new connectionDB(this);

		String _alcance = "WHERE _id="+_id;

		db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
		Toast.makeText(Modificar2.this,"Se Archivo el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

		Intent intent = new Intent(this,init_alfilPOS.class );
		startActivity(intent);

	}












	private void Eliminar(int _id) {



        db = new connectionDB(this);



        db.getWritableDatabase().delete(TABLE,TABLE_ID + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Modificar2.this,"Se Elimino el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }


    private int sumar_Serie (String _serie) {


        db = new connectionDB(this);

        db = new connectionDB(this);
        Cursor c = db.getReg_num_Serie(_serie);
        int _folio_a_modificar = 0;

        if (c.moveToFirst()) {
            do {
                _folio_a_modificar = c.getInt(2);


            } while (c.moveToNext());
        }

        ContentValues valoresSerie = new ContentValues();
        valoresSerie.put("serie", _serie);
        valoresSerie.put("folio", _folio_a_modificar + 1);


        db.getWritableDatabase().update("series", valoresSerie, "serie" + "=?", new String[]{String.valueOf(_serie)});
        //     Toast.makeText(Modificar.this, "Se Asigno un Nuevo Folio:" + _serie, Toast.LENGTH_SHORT).show();

        //     Intent intent = new Intent(this, MainActivity_Series.class);
        //     startActivity(intent);

        return _folio_a_modificar + 1;


    }




    private void Modificar_Print (int _id,
                                  String _serie,
                                  String _folio,
                                  String _ruc,
                                  String _razon_social,
                                  String _direccion,
                                  String _moneda,
                                  String _correo) {
        // String _fecha) {


        ContentValues valoresCabecera = new ContentValues();
        valoresCabecera.put(SERIE, _serie);
        valoresCabecera.put(FOLIO, _folio);
        //     valoresCabecera.put(FECHA, _fecha);
        valoresCabecera.put(RUC, _ruc);
        valoresCabecera.put(RAZON_SOCIAL, _razon_social);
        valoresCabecera.put(DIRECCION, _direccion);
        valoresCabecera.put(MONEDA, _moneda);
        valoresCabecera.put(CORREO, _correo);

        db = new connectionDB(this);

        String _alcance = "WHERE _id="+_id;

        db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Modificar2.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }




    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private String Imprimir_Ticket () {


        int _folio_nuevo=0;
        String _licencia="";

        if (Mfolio == 0) {


            _folio_nuevo=sumar_Serie(Mserie);
            Cursor cursor =  db.getReg_Licencia(_myId);

            if (cursor.moveToFirst()) {
                do {



                    _licencia = cursor.getString(1);

                } while (cursor.moveToNext());

            }

//            if (!_licencia.equals("WESQ-WERS-TYVS")) {
//                if (_folio_nuevo>999) {
//                    _folio_nuevo=9999999;
//                    Mrazon_social=_licencia+"  LICENCIA    AGOTADA      llame   a    factura global    954 879 529    sopote@factura.global          - - - - - - - - - - - - - - - ";
//                }
//            }



            Modificar_Print(Mid,
                    Mserie,
                    _folio_nuevo+"",
                    Mruc,
                    Mrazon_social,
                    Mdireccion,
                    Mmoneda,
                    Mcorreo);

        } else {
            _folio_nuevo=Mfolio;

        }


        String _linea01="";
        String _linea02="";
        String _linea03="";
        String _linea04="";
        String _linea05="";
        String _linea06="";
		String _linea07="";
		String _linea08="";
		String _linea09="";

		String _ruc_empresa="";





        Cursor cursor =  db.getReg_TicketPos(_myId);
        if (cursor.moveToFirst()) {
            do {



                _linea01 = cursor.getString(1);
                _linea02 = cursor.getString(2);
                _linea03 = cursor.getString(3);
                _linea04 = cursor.getString(4);
                _linea05 = cursor.getString(5);
                _linea06 = cursor.getString(6);
				_linea07 = cursor.getString(7);
				_linea08 = cursor.getString(8);
				_linea09 = cursor.getString(9);

				_ruc_empresa = cursor.getString(10);
                _base01 = cursor.getString(11);
				_base02 = cursor.getString(12);
				_base03 = cursor.getString(13);
				_base04 = cursor.getString(14);
				_base05 = cursor.getString(15);
				_base06 = cursor.getString(16);


            } while (cursor.moveToNext());

        }

        // me traigo la cabecera


        String _salida = "";
        String _cadena = "";
        String _salto ="\n";

        String _linea = "================================";


       // _salida=_salida+_salto;
        _salida=_salida+_linea+_salto;


		if (_linea01 != null ) {
			_salida = _salida + _linea01 + _salto;
		}

        _salida=_salida+_salto;

		if (_linea02 != null ) {
			_salida = _salida + _linea02 + _salto;
		}

		if (_linea03 != null ) {
			if (_linea03.trim().length() > 0) {
				_salida = _salida + _linea03 + _salto;
			}
		}



		if (_linea04 != null ) {
			if (_linea04.trim().length() > 0) {
				_salida = _salida + _linea04 + _salto;
			}
		}


		if (_linea05 != null ) {
			if (_linea05.trim().length() > 0) {
				_salida = _salida + _linea05 + _salto;
			}
		}


		if (_linea06 != null ) {
			if (_linea06.trim().length() > 0) {
				_salida = _salida + _linea06 + _salto;
			}
		}


		if (_linea07 != null ) {
			if (_linea07.trim().length() > 0) {
				_salida = _salida + _linea07 + _salto;
			}
		}


		if (_linea08 != null ) {
			if (_linea08.trim().length() > 0) {
				_salida = _salida + _linea08 + _salto;
			}
		}


		if (_linea09 != null ) {
			if (_linea09.trim().length() > 0) {
				_salida = _salida + _linea09 + _salto;
			}
		}




		_salida=_salida+_linea+_salto;

        String _naturaleza =  db.get_Naturaleza(Mserie);

        _cadena="NOTA DE PEDIDO";



        _salida=_salida+_cadena+_salto;



        _salida=_salida+"FOLIO:"+Mserie+"-"+_folio_nuevo+_salto;
        _salida=_salida+"F. EMISION:"+Mfecha+_salto;
        _salida=_salida+"MONEDA:"+"SOLES"+_salto;
        _salida=_salida+"TIPO DE VTA:"+"VTA INTERNA"+_salto;
        _salida=_salida+"CLIENTE:"+Mrazon_social.toUpperCase()+_salto;
        _salida=_salida+"RUC/DNI:"+Mruc+_salto;
        _salida=_salida+"DIRECCION:"+Mdireccion.toUpperCase()+_salto;
        _salida=_salida+"CORREO:"+Mcorreo+_salto;
        _salida=_salida+_linea+_salto;
        _salida=_salida+"DESCRIPCION"+_salto;
        _salida=_salida+"CANT.  UNI    PRECIO     IMPORTE"+_salto;
        _salida=_salida+_linea+_salto;
        _salida=_salida+_detalle;
       // _salida=_salida+_salto;
       // _salida=_salida+_salto;

        _salida=_salida+_linea+_salto;
     //   _salida=_salida+"                SUBTOTAL:"+_cantidad(_subtotal)+_salto;
     //   _salida=_salida+"              IGV "+_igv_global+" %:"+_cantidad(_igv)+_salto;
	//	_salida=_salida+"                 ICBPER: "+_cantidad(_icbper)+_salto;
        _salida=_salida+"                   TOTAL:"+_cantidad(_total)+_salto;
    //    _salida=_salida+_salto;
     //   _salida=_salida+_salto;
        _salida=_salida+"TIPO DE PAGO:"+"CONTADO"+_salto;
      //  _salida=_salida+_salto;


        String _buttom_temp="";

		if (_base01 != null ) {
			_buttom_temp = _buttom_temp + _base01 + _salto;
		}

		if (_base02 != null ) {
			_buttom_temp = _buttom_temp + _base02 + _salto;
		}



		if (_base03 != null ) {
			if (_base03.trim().length() > 0) {
				_buttom_temp = _buttom_temp + _base03 + _salto;
			}
		}


		if (_base04 != null ) {
			if (_base04.trim().length() > 0) {
				_buttom_temp = _buttom_temp + _base04 + _salto;
			}
		}


		if (_base05 != null ) {
			if (_base05.trim().length() > 0) {
				_buttom_temp = _buttom_temp + _base05 + _salto;
			}
		}


		if (_base06 != null ) {
			if (_base06.trim().length() > 0) {
				_buttom_temp = _buttom_temp + _base06 + _salto;
			}
		}



		_buttom_temp=_buttom_temp+_salto;
//		_buttom_temp=_buttom_temp+"Representacion    Impresa     de"+_salto;
//		_buttom_temp=_buttom_temp+"del   Comprobante    de    Venta"+_salto;
//		_buttom_temp=_buttom_temp+"Electronica  autorizado mediante"+_salto;
//		_buttom_temp=_buttom_temp+"la   Resolucion   155-2017/SUNAT"+_salto;
		_buttom_temp=_buttom_temp+_linea+_salto;
		_buttom_temp=_buttom_temp+_salto+_salto;






		ClipboardManager clipboard = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		}
		ClipData clip = ClipData.newPlainText("recibo", _salida+_buttom_temp);
	//	clipboard.setPrimaryClip(clip);
	//	Toast.makeText(Modificar2.this,"Es Registro fue enviado a Memoria "+_salida, Toast.LENGTH_LONG ).show();

		String _tipo_documento_adquiriente="1";
		if (Mruc.length()>8) {
			_tipo_documento_adquiriente="6";
		}

		 _datos_qr = _ruc_empresa+"|"+_naturaleza+"|"+
				 Mserie+"-"+_folio_nuevo+"|"+
				 _cantidad(_igv).trim()+"|"+_cantidad(_total).trim()+"|"+Mfecha+"|"+
				 _tipo_documento_adquiriente+"|"+
				 Mruc+"|";


		return _salida;


    }



/*
	public void writeBMPaddEntry( String name, byte[] image) throws SQLiteException{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues cv = new  ContentValues();
		cv.put(KEY_NAME,    name);
		cv.put(KEY_IMAGE,   image);
		database.insert( DB_TABLE, null, cv );
	}

*/


	private void Escribir_bmp(int _id, Bitmap _imagen)

	{

		ContentValues valoresEmpresa = new ContentValues();

		valoresEmpresa.put("logo", getBytes(_imagen));


		db = new connectionDB(this);



		db.getWritableDatabase().update("empresa", valoresEmpresa, "_id" + "=?",new String[] { String.valueOf(_id) });
		Toast.makeText(this,"Se Modifico la Imagen del LOGO: :"+_id, Toast.LENGTH_SHORT ).show();

		Intent intent = new Intent(this,init_alfilPOS.class );
		startActivity(intent);

	}




	private Bitmap _get_logo(int _id) {

		byte[] image=null;

		db = new connectionDB(this);
		Cursor cursor =  db.getReg_Image(_id);
		if (cursor.moveToFirst()) {
			do {
				image = cursor.getBlob(1);
			} while (cursor.moveToNext());
		}


		if (null == image ) {
			image = getBytes(getImageFromAssetsFile("demo.bmp"));
		}



		return getImage(image);
	}



	//////////////////////////////


	private String _Reporte_cierre (String _fecha) {


		int _folio_nuevo=0;
		String _licencia="";


		String _linea01="";
		String _linea02="";
		String _linea03="";
		String _linea04="";

		String _ruc_empresa="";



		Cursor cursor =  db.getReg_TicketPos(_myId);


		if (cursor.moveToFirst()) {
			do {


				_linea01 = cursor.getString(1);
				_linea02 = cursor.getString(2);
				_linea03 = cursor.getString(3);
				_linea04 = cursor.getString(4);


				_ruc_empresa = cursor.getString(10);
				_base01 = cursor.getString(11);
				_base02 = cursor.getString(12);
				_base03 = cursor.getString(13);
				_base04 = cursor.getString(14);
				_base05 = cursor.getString(15);
				_base06 = cursor.getString(16);


			} while (cursor.moveToNext());

		}




		// limpiar todos los ventas de productos

		Cursor c_prod0 = db.getNotes_productos();

		String _prod_a_limpiar="";
		double _ventas_pro=0.00;
		double _existencias_pro=0.00;

//
// {TABLE_ID, 0
// ID_PRODUCTO, 1
// DESCRIPCION_PRODUCTO, 2
// UNIDAD_PRODUCTO, 3
// PRECIO_PRODUCTO, 4
// PRECIO_PRODUCTO_MAYOREO, 5
// IGV_PRODUCTO, 6
// EXISTENCIA, 7
// VENTAS_PRO, 8
// SALDO_PRO   9};

	/*	if (c_prod0.moveToFirst()) {
			do {
				_prod_a_limpiar = c_prod0.getString(1);
				_existencias_pro = c_prod0.getDouble(7);
				_ventas_pro = c_prod0.getDouble(8);
				update_existencia(_prod_a_limpiar,_existencias_pro, 0.00);


			} while (c_prod0.moveToNext());


		}

*/






		// me traigo la cabecera


		String _salida = "";
		String _cadena = "";
		String _salto ="\n";

		String _linea =  "================================";
		String _titulo = "== REPORTE  DE CIERRE DE CAJA ==";


		// _salida=_salida+_salto;
		_salida=_salida+_linea+_salto;
		_salida=_salida+_titulo+_salto+_salto;


		if (_linea01 != null ) {
			_salida = _salida + _linea01 + _salto;
		}

		_salida=_salida+_salto;

		if (_linea02 != null ) {
			_salida = _salida + _linea02 + _salto;
		}

		if (_linea03 != null ) {
			if (_linea03.trim().length() > 0) {
				_salida = _salida + _linea03 + _salto;
			}
		}

		if (_linea04 != null ) {
			if (_linea04.trim().length() > 0) {
				_salida = _salida + _linea04 + _salto;
			}
		}






		_salida=_salida+_linea+_salto;

        Cursor c = db.getNotes_Documentos(Mfecha);

        int id=0;
        String _serie="";
        String _folio="";
        String _del_folio="";
        String _al_folio="";
        String _detalle_corte;
        double _gravado=0;
        double _excento=0;
        double _exonerado=0;
        double _inafecto=0;
        double _subtotal=0;
        double _igv=0;
        double _total=0;




		_detalle_corte="SERIE FOLIO       IGV      TOTAL "+_salto;

        if (c.moveToFirst()) {
            do {
// TABLE_ID 0, SERIE 1, FOLIO 2, GRAVADO 3, EXCENTO 4, INAFECTO 5, SUBTOTAL 6, IGV 7, TOTAL 8




				id = c.getInt(0);

				_serie = c.getString(1);
                _folio = c.getString(2);

           //     if (_serie==Mserie) {
                    _gravado  = _gravado+c.getDouble(3);
                    _excento  = _excento+c.getDouble(4);
                    _inafecto = _inafecto+c.getDouble(5);
                    _subtotal = _subtotal+c.getDouble(6);
                    _igv      = _igv+c.getDouble(7);
                    _total    = _total+c.getDouble(8);


           //     }2



				_detalle_corte=_detalle_corte+_serie+_folio_str(_folio)+_dinero(c.getDouble(7))+_dinero(c.getDouble(8))+_salto;


				//	Toast.makeText(Modificar2.this,"ID a Modifcar :   "+id+"   "+_gravado, Toast.LENGTH_SHORT ).show();


				// ir al documento acumular las ventas





            } while (c.moveToNext());



		//	_detalle_corte=_detalle_corte+_salto+"Serie           :"+Mserie+_salto;
			//_detalle_corte=_detalle_corte+"Del Folio       :"+_del_folio+_salto;
			//_detalle_corte=_detalle_corte+"Al Folio        :"+_al_folio+_salto+_salto;
			_detalle_corte=_detalle_corte+_linea+_salto+_salto;
			_detalle_corte=_detalle_corte+"Monto Gravado   :"+_dinero(_gravado)+_salto;
			_detalle_corte=_detalle_corte+"Monto Excento   :"+_dinero(_excento)+_salto;
			_detalle_corte=_detalle_corte+"Monto Inafecto  :"+_dinero(_inafecto)+_salto;
			_detalle_corte=_detalle_corte+"Sub Total       :"+_dinero(_subtotal)+_salto;
			_detalle_corte=_detalle_corte+"IGV             :"+_dinero(_igv)+_salto;
			_detalle_corte=_detalle_corte+"TOTAL           :"+_dinero(_total)+_salto;




        }


        // actualizar saldos de ventas




		// ponerlas ventas en cero

		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		// poner las existencias a cero
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		Cursor c_prod = db.getNotes_productos();

		_prod_a_limpiar="";
		_ventas_pro=0.00;

		//
		// {TABLE_ID, 0
		// ID_PRODUCTO, 1
		// DESCRIPCION_PRODUCTO, 2
		// UNIDAD_PRODUCTO, 3
		// PRECIO_PRODUCTO, 4
		// PRECIO_PRODUCTO_MAYOREO, 5
		// IGV_PRODUCTO, 6
		// EXISTENCIA, 7
		// VENTAS_PRO, 8
		// SALDO_PRO   9};

		if (c_prod.moveToFirst()) {
			do {
				_prod_a_limpiar = c_prod.getString(1);
			//	_ventas_pro = c_prod.getDouble(8);
				update_ventas(_prod_a_limpiar, 0.00);


			} while (c_prod.moveToNext());


		}









		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		// traer todo los documentos de ventas para actualizar los saldos de ventas de los productos
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		Cursor c_cabecera = db.getNotes_Documentos(Mfecha);

	//	int id=0;

		if (c_cabecera.moveToFirst()) {
			do {
				id = c_cabecera.getInt(0);

				Cursor c_detalle = db.getNotes_detalle(id);
				//  String columnas[] = {TABLE_ID, 0
				// PRODUCTO, 1
				// DESCRIPCION, 2
				// UNIDAD, 3
				// CANTIDAD, 4
				// PRECIO_PRODUCTO, 5
				// DET_IGV   6
				// };

				int id_detalle=0;
				String producto="";
				double _ventas_detalle=0.00;



				if (c_detalle.moveToFirst()) {
					do {
						id_detalle = c_detalle.getInt(0);
						producto = c_detalle.getString(1);
						_ventas_detalle = c_detalle.getDouble(4);

						acumular_ventas(producto, _ventas_detalle);


					} while (c_detalle.moveToNext());

					c_detalle.close();

				}

			} while (c_cabecera.moveToNext());

		}






		// DESPLEGAR PRODUCTOS Y SUS SALDOS

		// limpiar todos los saldos de productos

		Cursor c_prod_clean = db.getNotes_productos();

		String _producto="";
		String _descripcion="";
		double _existencia=0.00;
		double _ventas=0.00;
		double _saldo=0.00;
		double _mov=0.00;

		if (c_prod_clean.moveToFirst()) {
			do {
				_producto="";
				_descripcion="";
				_existencia=0.00;
				_ventas=0.00;
				_saldo=0.00;


				_producto = c_prod_clean.getString(1);
				_descripcion = c_prod_clean.getString(2);
				_existencia = c_prod_clean.getDouble(7);
				_ventas = c_prod_clean.getDouble(8);
				_saldo = c_prod_clean.getDouble(9);



				if ((_existencia!=0)  || (_ventas!=0)  || (_saldo!=0)) {
					_detalle_corte=_detalle_corte+_salto+"Prod.:"+_producto+_salto+"Desc.:"+_descripcion+_salto+
					"Saldo Inicial  :"+_dinero(_existencia)+_salto+
					"Ventas         :"+_dinero(_ventas)+_salto+
					"Saldo FInal    :"+_dinero(_saldo)+_salto;
				}






			} while (c_prod_clean.moveToNext());


		}







		//   _salida=_salida+"SERIE         : "+Mserie+_salto;
		_salida=_salida+"FECHA DE CORTE: "+Mfecha+_salto;
		_salida=_salida+"MONEDA        : "+"SOLES"+_salto;
		_salida=_salida+_linea+_salto;
		_salida=_salida+_linea+_salto;
		_salida=_salida+_detalle_corte;
		_salida=_salida+_salto;
		_salida=_salida+_salto;
		// _salida=_salida+_salto;
		// _salida=_salida+_salto;



		return _salida;


	}




	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();

	}



	private String _Reporte_Nube (String _fecha) {


		int _folio_nuevo=0;
		String _licencia="";


		String _linea01="";
		String _linea02="";
		String _linea03="";
		String _linea04="";
		String _api_key="";
		String _api_key2="";

		String _ruc_empresa="";



		Cursor cursor =  db.getReg_TicketPos(_myId);


		if (cursor.moveToFirst()) {
			do {


				_linea01 = cursor.getString(1);
				_linea02 = cursor.getString(2);
				_linea03 = cursor.getString(3);
				_linea04 = cursor.getString(4);


				_ruc_empresa = cursor.getString(10);
				_base01 = cursor.getString(11);
				_base02 = cursor.getString(12);
				_base03 = cursor.getString(13);
				_base04 = cursor.getString(14);
				_base05 = cursor.getString(15);
				_base06 = cursor.getString(16);



			} while (cursor.moveToNext());

		}

		// me traigo la cabecera


		String _api_key_doctos="";
		String _api_key_movtos="";
		String _api_key_productos="";
		String _api_key_clientes="";
		String _api_key_almacen="";
		String _api_key_movimientos="";
		String _api_key_00="";
		String _api_key_01="";
		String _api_key_02="";








		Cursor cursor2 =  db.getReg_Apiskey(1);

		//   String columnas[] = {"_id","api_key_doctos", "api_key_movtos",
		// "api_key_productos", "api_key_clientes", "api_key_almacen",
		// "api_key_movimientos","api_key_00", "api_key_01", "api_key_02" };


		if (cursor2.moveToFirst()) {
			do {


				_api_key_doctos = cursor2.getString(1);
				_api_key_movtos = cursor2.getString(2);
				_api_key_productos = cursor2.getString(3);
				_api_key_clientes = cursor2.getString(4);
				_api_key_almacen = cursor2.getString(5);
				_api_key_movimientos = cursor2.getString(6);
				_api_key_00 = cursor2.getString(7);
				_api_key_01 = cursor2.getString(8);
				_api_key_02 = cursor2.getString(9);







			} while (cursor2.moveToNext());

		}


		String _almacen=_api_key_01;

		String _salida = "";
		String _cadena = "";
		String _salto ="\n";










		String _linea = "================================";
		String _titulo ="=  REPORTE DE ENVIO A LA NUBE  =";


		// _salida=_salida+_salto;
		_salida=_salida+_linea+_salto;
		_salida=_salida+_titulo+_salto+_salto;


		if (_linea01 != null ) {
			_salida = _salida + _linea01 + _salto;
		}

		_salida=_salida+_salto;

		if (_linea02 != null ) {
			_salida = _salida + _linea02 + _salto;
		}

		if (_linea03 != null ) {
			if (_linea03.trim().length() > 0) {
				_salida = _salida + _linea03 + _salto;
			}
		}

		if (_linea04 != null ) {
			if (_linea04.trim().length() > 0) {
				_salida = _salida + _linea04 + _salto;
			}
		}






		_salida=_salida+_linea+_salto;

		Cursor c = db.getNotes_Documentos_Nube(Mfecha);

		int id=0;
		String _serie="";
		String _folio="";
		String _del_folio="";
		String _al_folio="";
		String _razon_social_nube="";
		String _fecha_nube="";
		String _ruc_nube="";
		String _direccion_nube="";
		String _moneda_nube="";
		String _correo_nube="";
		String _serie_rel_nube="";
		String _folio_rel_nube="";
		String _serie_completa="";
		String _serie_completa_sunat="";
		String _tipo_doc="";






		String _detalle_corte;
		double _gravado=0;
		double _excento=0;
		double _exonerado=0;
		double _inafecto=0;
		double _subtotal=0;
		double _igv=0;
		double _total=0;

		double _total_gravado=0;
		double _total_excento=0;
		double _total_exonerado=0;
		double _total_inafecto=0;
		double _total_subtotal=0;
		double _total_igv=0;
		double _total_total=0;



		int _nube;


		// prepara la direccion de la nube

		RequestQueue queue = Volley.newRequestQueue(this);

		String url = _api_key_doctos;

        _texto_nube="Si";

		_detalle_corte="SERIE FOLIO      TOTAL    "+_salto;

		if (c.moveToFirst()) {
			do {
// TABLE_ID 0, SERIE 1, FOLIO 2, GRAVADO 3, EXCENTO 4, INAFECTO 5, SUBTOTAL 6, IGV 7, TOTAL 8




				id = c.getInt(0);

				_serie = c.getString(1);
				_folio = c.getString(2);

				//     if (_serie==Mserie) {
				_gravado  = c.getDouble(3);
				_excento  = c.getDouble(4);
				_inafecto = c.getDouble(5);
				_subtotal = c.getDouble(6);
				_igv      = c.getDouble(7);
				_total    = c.getDouble(8);
				_nube    = c.getInt(8);
				_razon_social_nube = c.getString(10);
				_fecha_nube = c.getString(11);

				_ruc_nube = c.getString(12);
				_direccion_nube = c.getString(13);
				_moneda_nube = c.getString(14);
				_correo_nube = c.getString(15);
				_folio_rel_nube = c.getString(16);
				_serie_rel_nube = c.getString(17);


				_total_gravado=_total_gravado+_gravado;
				_total_excento=_total_excento+_excento;
				_total_exonerado=_total_exonerado+_exonerado;
				_total_inafecto=_total_inafecto+_inafecto;
				_total_subtotal=_total_subtotal+_subtotal;
				_total_igv=_total_igv+_igv;
				_total_total=_total_total+_total;




				int _folio_int = Integer.parseInt(_folio);


				String _naturaleza =  db.get_Naturaleza(_serie);
				_folio=lPadZero(_folio_int,7);


				_serie_completa_sunat=_ruc_empresa+"-"+_naturaleza+"-"+_serie+"-"+_folio;
				_serie_completa=_serie+"/19-"+_folio;











				// esto sube a la nube
				Map<String, String> params = new HashMap();

//				params.put("serie", _serie);
//				params.put("folio", _folio);
//				params.put("fecha", _fecha_nube);
//				params.put("ruc_emisor", _ruc_empresa);
//				params.put("ruc_receptor", _ruc_nube);
//				params.put("razon_social", _razon_social_nube);
//				params.put("direccion", _direccion_nube);
//				params.put("moneda", _moneda_nube);
//				params.put("correo", _correo_nube);
//				params.put("serie_rel", _serie_rel_nube);
//				params.put("folio_rel", _folio_rel_nube);
//				params.put("gravado", ""+_gravado);
//				params.put("excento", ""+_excento);
//				params.put("inafecto", ""+_inafecto);
//				params.put("subtotal", ""+_subtotal);
//				params.put("igv", ""+_igv);
//				params.put("icbper", ""+0.00);
//				params.put("total", ""+_total);
//				params.put("serie_completa", _serie_completa);
//				params.put("enviado_nube", "1");


		//		params.put("id", );
				params.put("emp", "1");
				params.put("id_app", ""+id);
				params.put("emp_div", "1");
				params.put("fch", _fecha_nube);
				params.put("hor", "00:00:00");
				params.put("eje", "2019");
				params.put("ser", "4");  // ser 4 = ·F001
				params.put("num_doc", ""+_folio_int);
			//	params.put("ser_cnt", ""+_folio_int);
				params.put("num_fac", _serie_completa);  // "num_fac": "F001/19-000001",": "F001/19-000001",
				params.put("serie_completa", _serie_completa_sunat);
				params.put("alm", _almacen);
				params.put("alm_temp", _almacen);
				params.put("cli_temp", _ruc_nube);
				params.put("serie_temp", _serie);
				params.put("raz_soc_temp", _razon_social_nube);
				params.put("dir_temp", _direccion_nube);
				params.put("fha_temp", "12/12/2585");
				params.put("subido_erp", "0");





				_texto_nube="No";

                _mensaje_error="";


				if (_folio_int>0) {



					JSONObject parameters = new JSONObject(params);

					JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Actualizacion a la Nube Exitosa de Cabecera! ", Toast.LENGTH_SHORT).show();


						}

						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "HAY UNA ERROR en actualizacion de Cabecera: (Intente de Nuevo) "+error.toString(), Toast.LENGTH_LONG).show();
                                _mensaje_error="HAY UN ERROR en actualizacion de Cabecera (Intente de Nuevo) :"+error.toString();

								if (_texto_nube.equals("Si"))  {
									SendDataByte(PrinterCommand.POS_Print_Text(_mensaje_error, CHINESE, 0, 0, 0, 0));
									SendDataByte(Command.LF);
									_texto_nube="No";


								}




						}
					});

				Volley.newRequestQueue(this).add(jsonRequest);
				}

				if (_texto_nube.equals("No")) {
					if (_folio_int>0) {
						_detalle_corte=_detalle_corte+_serie+_folio_str(_folio)+_dinero(c.getDouble(8))+"      "+_salto;
						//       _detalle_corte=_detalle_corte+_mensaje_error+_salto;
						_texto_nube="Si";


					}




                } else {
					if (_folio_int>0) {
						_detalle_corte=_detalle_corte+_serie+_folio_str(_folio)+_dinero(c.getDouble(8))+"      "+_salto;
					}

                }



				// Toast.makeText(getApplicationContext(), "Se logro escibir en la nube", Toast.LENGTH_SHORT).show();


				// vamos a taer los datos de nuevo para saber a el id de cada trasaccion y poderle poner detalle




//				String _titulo ="================================"+_salto+
//						"REP DESCARGA CLIENTES DE LA NUBE"+_salto+
//						"ALMACEN:"+_almacen+_salto+
//						"api facturas: "+_api_key_clientes+_salto+
						//		"api productos"+_api_key_productos+_salto+
						//		"api almacen"+_api_key_almacen+_salto+
						//		"api movimientos"+_api_key_movimientos+_salto+
						//		"api 00"+_api_key_00+_salto+
						//		"api 01"+_api_key_01+_salto+
						//		"api 02"+_api_key_02+_salto+

//						"================================"+_salto;


				// _salida=_salida+_salto;
				//	_salida=_salida+_salto;
				_salida=_salida+_titulo+_salto;


				if (_linea01 != null ) {
					_salida = _salida + _linea01 + _salto;
				}

				_salida=_salida+_salto;

				if (_linea02 != null ) {
					_salida = _salida + _linea02 + _salto;
				}

				if (_linea03 != null ) {
					if (_linea03.trim().length() > 0) {
						_salida = _salida + _linea03 + _salto;
					}
				}

				if (_linea04 != null ) {
					if (_linea04.trim().length() > 0) {
						_salida = _salida + _linea04 + _salto;
					}
				}






				_salida=_salida+_linea+_salto;






				String _ruc_cliente="";
		//		String _detalle_corte;
				final String _id_factura_velneo="";


				//	Toast.makeText(getApplicationContext(), " antes al json "+url, Toast.LENGTH_SHORT ).show();


				mQueue = Volley.newRequestQueue(this);


				JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									JSONArray jsonArray = response.getJSONArray("vta_fac_g");
										//	Toast.makeText(getApplicationContext(), response.toString()+",,,"+jsonArray.length() , Toast.LENGTH_LONG ).show();
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject employee = jsonArray.getJSONObject(i);


											String _id_velneo = employee.getString("id");
											String _id_app = employee.getString("id_app");
											String _num_fact = employee.getString("num_fac");


											int numero_registros_por_bajar=jsonArray.length();

											update_cabecera_ventas(_id_app,_id_velneo);



											//		if (_almacen_m.trim().equals(_almacen)) {
//											if (db.exits_Client(_ruc_cliente_m)==0) {//
//												agrega_Clientes(_ruc_cliente_m,
//														_razon_social_m,
//														_direccion_m,
//														_correo_m,
//														_telefono_m);

//										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});

				mQueue.add(request);




			////////////////////////////////////////////////


				// traer numero de id de velneo de cada _id_velneo



				String _factura_velneo = db._id_factura_velneo(Integer.toString(id));

				Toast.makeText(getApplicationContext(), "id factura de velneo:"+_factura_velneo+" id de la app:"+id, Toast.LENGTH_SHORT).show();




				// subir el detalle

				Cursor detalle = db.getNotes_detalle(id);
				String producto = "",  descripcion="", unidad="", _id_velneo;
				int  folio;
				double precio, cantidad, precio_sin_igv, subtotal, total, igv,_subtotal_con_igv;



				double total_det=0;
				double subtotal_det=0;
				double _total_det=0;
				double _subtotal_det=0;
				double _igv_det=0;
				double _subtotal_con_igv_det=0;
				double precio_sin_igv_det=0;

				double _precio=0;
				double _cantidad=0;

				_icbper=0;

				String _producto="";
				String _descripcion="";
				String _unidad="";




				Cursor cursor3 =  db.getReg_icbper(_myId);

				if (cursor3.moveToFirst()) {
					do {



						_impuesto_icbper = Double.parseDouble(cursor3.getString(1));

					} while (cursor3.moveToNext());

				}




				RequestQueue queue2 = Volley.newRequestQueue(this);
				String url2 = _api_key_movtos;

				int _lineas=0;
				int _linea_detalle=0;
				String _linea_detalle_string="";




				if (detalle.moveToFirst()) {
					do {
						id = detalle.getInt(0);
						_producto = detalle.getString(1);
						_descripcion = detalle.getString(2);
						_unidad = detalle.getString(3);
						cantidad = detalle.getDouble(4);
						_precio = detalle.getDouble(5);
						igv = detalle.getDouble(6);
						_linea_detalle=detalle.getInt(7);
						_id_velneo=detalle.getString(3);

                        _linea_detalle_string=Integer.toString(_linea_detalle);

						_igv_global=igv;
						_lineas++;

						// TABLE_ID  0
						// PRODUCTO  1
						// DESCRIPCION  2
						// UNIDAD       3
						// CANTIDAD     4
						// PRECIO_PRODUCTO  5
						// DET_IGV          6
						// LINEA            7
						// "_id"            8

						precio_sin_igv_det=_precio/(1+igv);

						subtotal_det=precio_sin_igv_det*cantidad;
						_subtotal_det=_subtotal_det+subtotal_det;
						_total_det=_total_det+(cantidad*_precio);
						_subtotal_con_igv_det=_precio*cantidad;

				//		_detalle_corte=_detalle_corte+_producto+" "+_descripcion+" "+_salto;


						if (_producto.trim().equals("BOLSA")) {
							_icbper=(_impuesto_icbper*cantidad);
						}



						// esto sube a la nube
						Map<String, String> params_det = new HashMap();





                        params_det.put("emp", "1");
                        params_det.put("emp_div", "1");
						params_det.put("vta_fac_num_lin", _linea_detalle_string);
						params_det.put("tip_mov", "V");
						params_det.put("art", ""+37);
						params_det.put("dsc_edt", _descripcion);
						params_det.put("can", ""+cantidad);
						params.put("pre", ""+_precio);
					//	params.put("imp", ""+_subtotal_det);

						params_det.put("id_temp", ""+_id_velneo);
						params_det.put("ref_temp", ""+_producto);
						params_det.put("desc_temp", ""+_descripcion);
						params_det.put("alm_temp", ""+_almacen);
						params_det.put("uni_temp", ""+_unidad);
						params_det.put("can_temp", ""+cantidad);
						params_det.put("pre_temp", ""+_precio);
						params_det.put("can_temp", ""+_total_det);
						params_det.put("serie_completa", _serie_completa_sunat);
						params_det.put("subido_erp", "0");




						_texto_nube="Si";

						JSONObject parameters2 = new JSONObject(params_det);

						JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.POST, url2, parameters2, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								Toast.makeText(getApplicationContext(), "Actualizacion a la Nube Exitosa del Detalle!", Toast.LENGTH_SHORT).show();


							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								error.printStackTrace();
								Toast.makeText(getApplicationContext(), "HAY UN ERROR en actualizacion del Detalle:"+error.toString(), Toast.LENGTH_LONG).show();
                                _mensaje_error="HAY UNA ERROR en actualizacion del DETALLE :"+error.toString();

                                SendDataByte(PrinterCommand.POS_Print_Text(_mensaje_error, CHINESE, 0, 0, 0, 0));
                                SendDataByte(Command.LF);


								//TODO: handle failure
							}
						});

						Volley.newRequestQueue(this).add(jsonRequest2);

                        if (_texto_nube.equals("No")) {
                            _detalle_corte=_detalle_corte+_mensaje_error+_salto;
                            _texto_nube="Si";




                        } else {

                        }




                    } while (detalle.moveToNext());
					_igv=_total-_subtotal;


				}








			} while (c.moveToNext());







		}

		_detalle_corte=_detalle_corte+_linea+_salto+_salto;
		_detalle_corte=_detalle_corte+"Monto Gravado   :"+_dinero(_total_gravado)+_salto;
		_detalle_corte=_detalle_corte+"Monto Excento   :"+_dinero(_total_excento)+_salto;
		_detalle_corte=_detalle_corte+"Monto Inafecto  :"+_dinero(_total_inafecto)+_salto;
		_detalle_corte=_detalle_corte+"Sub Total       :"+_dinero(_total_subtotal)+_salto;
		_detalle_corte=_detalle_corte+"IGV             :"+_dinero(_total_igv)+_salto;
		_detalle_corte=_detalle_corte+"TOTAL           :"+_dinero(_total_total)+_salto;




		//   _salida=_salida+"SERIE         : "+Mserie+_salto;
		_salida=_salida+"FECHA DE CORTE: "+Mfecha+_salto;
		_salida=_salida+"MONEDA        : "+"SOLES"+_salto;
		_salida=_salida+_linea+_salto;
		_salida=_salida+_linea+_salto;
		_salida=_salida+_detalle_corte;
		_salida=_salida+_salto;
		_salida=_salida+_salto;
		// _salida=_salida+_salto;
		// _salida=_salida+_salto;



		return _salida;


	}


	public static String lPadZero(int in, int fill){

		boolean negative = false;
		int value, len = 0;

		if(in >= 0){
			value = in;
		} else {
			negative = true;
			value = - in;
			in = - in;
			len ++;
		}

		if(value == 0){
			len = 1;
		} else{
			for(; value != 0; len ++){
				value /= 10;
			}
		}

		StringBuilder sb = new StringBuilder();

		if(negative){
			sb.append('-');
		}

		for(int i = fill; i > len; i--){
			sb.append('0');
		}

		sb.append(in);

		return sb.toString();
	}



	private String _Reporte_Nube_Clientes (String _fecha) {


		int _folio_nuevo=0;
		String _licencia="";


		String _linea01="";
		String _linea02="";
		String _linea03="";
		String _linea04="";
		String _api_key="";
		String _api_key2="";

		String _ruc_empresa="";



		Cursor cursor =  db.getReg_TicketPos(_myId);


		if (cursor.moveToFirst()) {
			do {


				_linea01 = cursor.getString(1);
				_linea02 = cursor.getString(2);
				_linea03 = cursor.getString(3);
				_linea04 = cursor.getString(4);


				_ruc_empresa = cursor.getString(10);
				_base01 = cursor.getString(11);
				_base02 = cursor.getString(12);
				_base03 = cursor.getString(13);
				_base04 = cursor.getString(14);
				_base05 = cursor.getString(15);
				_base06 = cursor.getString(16);



			} while (cursor.moveToNext());

		}

		// me traigo la cabecera


		String _api_key_doctos="";
		String _api_key_movtos="";
		String _api_key_productos="";
		String _api_key_clientes="";
		String _api_key_almacen="";
		String _api_key_movimientos="";
		String _api_key_00="";
		String _api_key_01="";
		String _api_key_02="";




		Cursor cursor2 =  db.getReg_Apiskey(1);

		//   String columnas[] = {"_id","api_key_doctos", "api_key_movtos",
		// "api_key_productos", "api_key_clientes", "api_key_almacen",
		// "api_key_movimientos","api_key_00", "api_key_01", "api_key_02" };


		if (cursor2.moveToFirst()) {
			do {


				_api_key_doctos = cursor2.getString(1);
				_api_key_movtos = cursor2.getString(2);
				_api_key_productos = cursor2.getString(3);
				_api_key_clientes = cursor2.getString(4);
				_api_key_almacen = cursor2.getString(5);
				_api_key_movimientos = cursor2.getString(6);
				_api_key_00 = cursor2.getString(7);
				_api_key_01 = cursor2.getString(8);
				_api_key_02 = cursor2.getString(9);






			} while (cursor2.moveToNext());

		}



		String _salida = "";
		String _cadena = "";
		String _salto ="\n";





		String _almacen="";

		_almacen=_api_key_01;



		String _linea="";

		String _titulo ="================================"+_salto+
						"REP. ENVIO DE CLIENTES A LA NUBE"+_salto+
						"ALMACEN:"+_almacen+_salto+
						"api clientes: "+_api_key_clientes+_salto+
				//		"api productos"+_api_key_productos+_salto+
				//		"api almacen"+_api_key_almacen+_salto+
				//		"api movimientos"+_api_key_movimientos+_salto+
				//		"api 00"+_api_key_00+_salto+
				//		"api 01"+_api_key_01+_salto+
				//		"api 02"+_api_key_02+_salto+

						"================================"+_salto;


		// _salida=_salida+_salto;
	//	_salida=_salida+_salto;
		_salida=_salida+_titulo+_salto;


		if (_linea01 != null ) {
			_salida = _salida + _linea01 + _salto;
		}

		_salida=_salida+_salto;

		if (_linea02 != null ) {
			_salida = _salida + _linea02 + _salto;
		}

		if (_linea03 != null ) {
			if (_linea03.trim().length() > 0) {
				_salida = _salida + _linea03 + _salto;
			}
		}

		if (_linea04 != null ) {
			if (_linea04.trim().length() > 0) {
				_salida = _salida + _linea04 + _salto;
			}
		}






		_salida=_salida+_linea+_salto;

		Cursor c = db.getNotes_clientes();

		int id=0;
		String _ruc_cliente="";
		String _tipo_identificacion_cliente="";
		String _razon_social_cliente="";
		String _direccion_cliente="";
		String _correo_cliente="";
		String _telefono_cliente="";







		String _detalle_corte;

		int _nube;


		// prepara la direccion de la nube

		RequestQueue queue = Volley.newRequestQueue(this);

		String url = _api_key_clientes;

		_texto_nube="Si";

		int _folio_int = 0;

		_detalle_corte="SEC RUC       RAZON SOCIAL"+_salto;
	//	_detalle_corte=url+_salto;

		if (c.moveToFirst()) {
			do {
				// 0 "_id",
				// 1 "ruc_cliente",
				// 2 "razon_social_cliente",
				// 3 "direccion_cliente",
				// 4 "correo_cliente",
				// 5 "telefono_cliente",
				// 6 "tipo_identidad"};

				_folio_int++;




				id = c.getInt(0);

				_ruc_cliente = c.getString(1);
				_razon_social_cliente = c.getString(2);
				_direccion_cliente = c.getString(3);
				_correo_cliente = c.getString(4);
				_telefono_cliente = c.getString(5);
				_tipo_identificacion_cliente = c.getString(6);






				// esto sube a la nube
				Map<String, String> params = new HashMap();



			//	params.put("ALMACEN", _almacen);
				params.put("cif", _ruc_cliente);
			//	params.put("TIPO_IDENT", _tipo_identificacion_cliente);
				params.put("nom_fis", _razon_social_cliente);
				params.put("nom_com", _razon_social_cliente);
				params.put("dir", _direccion_cliente);
				params.put("eml", _correo_cliente);
				params.put("tlf", _telefono_cliente);
				params.put("key", _almacen);
				// "es_clt": true,
				params.put("es_clt", "true");
				//params.put("serie_completa", _serie_completa_sunat);


				_texto_nube="No";

				_mensaje_error="";


				JSONObject parameters = new JSONObject(params);

				JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Toast.makeText(getApplicationContext(), "Actualizacion a la Nube Exitosa de Clientes!", Toast.LENGTH_SHORT).show();


					}

					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						Toast.makeText(getApplicationContext(), "HAY UNA ERROR en actualizacion de Clientes: (Intente de Nuevo) "+error.toString(), Toast.LENGTH_LONG).show();
						_mensaje_error="HAY UN ERROR en actualizacion de Clientes (Intente de Nuevo) :"+error.toString();

						if (_texto_nube.equals("Si"))  {
							SendDataByte(PrinterCommand.POS_Print_Text(_mensaje_error, CHINESE, 0, 0, 0, 0));
							SendDataByte(Command.LF);
							_texto_nube="No";


						}




					}
					});

				Volley.newRequestQueue(this).add(jsonRequest);

				if (_texto_nube.equals("No")) {
					if (_folio_int>0) {
						_detalle_corte=_detalle_corte+_folio_int+" "+_ruc_cliente+" "+_razon_social_cliente+_salto;
						//       _detalle_corte=_detalle_corte+_mensaje_error+_salto;
						_texto_nube="Si";


					}




				} else {
					if (_folio_int>0) {
						_detalle_corte=_detalle_corte+_folio_int+" "+_ruc_cliente+" "+_razon_social_cliente+_salto;
					}

				}

			} while (c.moveToNext());


		}

		_detalle_corte=_detalle_corte+_linea+_salto+_salto;


		_salida=_salida+_linea+_salto;
		_salida=_salida+_detalle_corte;
		_salida=_salida+_salto;

		return _salida;


	}




	private String _Reporte_Descarga_Nube_Clientes (String _fecha) {


		int _folio_nuevo=0;
		String _licencia="";


		String _linea01="";
		String _linea02="";
		String _linea03="";
		String _linea04="";
		String _api_key="";
		String _api_key2="";

		String _ruc_empresa="";



		Cursor cursor =  db.getReg_TicketPos(_myId);


		if (cursor.moveToFirst()) {
			do {


				_linea01 = cursor.getString(1);
				_linea02 = cursor.getString(2);
				_linea03 = cursor.getString(3);
				_linea04 = cursor.getString(4);


				_ruc_empresa = cursor.getString(10);
				_base01 = cursor.getString(11);
				_base02 = cursor.getString(12);
				_base03 = cursor.getString(13);
				_base04 = cursor.getString(14);
				_base05 = cursor.getString(15);
				_base06 = cursor.getString(16);



			} while (cursor.moveToNext());

		}

		// me traigo la cabecera


		String _api_key_doctos="";
		String _api_key_movtos="";
		String _api_key_productos="";
		String _api_key_clientes="";
		String _api_key_almacen="";
		String _api_key_movimientos="";
		String _api_key_00="";
		String _api_key_01="";
		String _api_key_02="";








		Cursor cursor2 =  db.getReg_Apiskey(1);

		//   String columnas[] = {"_id","api_key_doctos", "api_key_movtos",
		// "api_key_productos", "api_key_clientes", "api_key_almacen",
		// "api_key_movimientos","api_key_00", "api_key_01", "api_key_02" };


		if (cursor2.moveToFirst()) {
			do {


				_api_key_doctos = cursor2.getString(1);
				_api_key_movtos = cursor2.getString(2);
				_api_key_productos = cursor2.getString(3);
				_api_key_clientes = cursor2.getString(4);
				_api_key_almacen = cursor2.getString(5);
				_api_key_movimientos = cursor2.getString(6);
				_api_key_00 = cursor2.getString(7);
				_api_key_01 = cursor2.getString(8);
				_api_key_02 = cursor2.getString(9);






			} while (cursor2.moveToNext());

		}



		String _salida = "";
		String _cadena = "";
		String _salto ="\n";





		final String _almacen=_api_key_01;




		String _linea="";

		String _titulo ="================================"+_salto+
				        "REP DESCARGA CLIENTES DE LA NUBE"+_salto+
				"ALMACEN:"+_almacen+_salto+
				"api clientes: "+_api_key_clientes+_salto+
				//		"api productos"+_api_key_productos+_salto+
				//		"api almacen"+_api_key_almacen+_salto+
				//		"api movimientos"+_api_key_movimientos+_salto+
				//		"api 00"+_api_key_00+_salto+
				//		"api 01"+_api_key_01+_salto+
				//		"api 02"+_api_key_02+_salto+

				"================================"+_salto;


		// _salida=_salida+_salto;
		//	_salida=_salida+_salto;
		_salida=_salida+_titulo+_salto;


		if (_linea01 != null ) {
			_salida = _salida + _linea01 + _salto;
		}

		_salida=_salida+_salto;

		if (_linea02 != null ) {
			_salida = _salida + _linea02 + _salto;
		}

		if (_linea03 != null ) {
			if (_linea03.trim().length() > 0) {
				_salida = _salida + _linea03 + _salto;
			}
		}

		if (_linea04 != null ) {
			if (_linea04.trim().length() > 0) {
				_salida = _salida + _linea04 + _salto;
			}
		}






		_salida=_salida+_linea+_salto;


		String url = _api_key_clientes;



		String _ruc_cliente="";

		String _detalle_corte;

	//	Toast.makeText(getApplicationContext(), " antes al json "+url, Toast.LENGTH_SHORT ).show();


		mQueue = Volley.newRequestQueue(this);


		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("ent_m");

						//	Toast.makeText(getApplicationContext(), response.toString()+",,,"+jsonArray.length() , Toast.LENGTH_LONG ).show();

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject employee = jsonArray.getJSONObject(i);

							//	String _almacen_m = employee.getString("almacen");
								String _ruc_cliente_m = employee.getString("cif");
								String _razon_social_m = employee.getString("nom_fis");
								String _direccion_m = employee.getString("dir");
								String _correo_m = employee.getString("eml");
								String _telefono_m = employee.getString("tlf");
								String _key_m = employee.getString("key");
							//	String _tipo_identificacion_m = employee.getString("tipo_ident");


								//	mTextViewResult.append(_producto + ", " + _descripcion + "\n\n");

								int numero_registros_por_bajar=jsonArray.length();

								if (_key_m.contains(_almacen)) {
									if (db.exits_Client(_ruc_cliente_m)==0) {
										agrega_Clientes(_ruc_cliente_m,
												_razon_social_m,
												_direccion_m,
												_correo_m,
												_telefono_m);
									}

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		});

		mQueue.add(request);









		Cursor c2 = db.getNotes_clientes();

		int id=0;
		String _ruc_cliente_local="";
		String _tipo_identificacion_cliente_local="";
		String _razon_social_cliente_local="";
		String _direccion_cliente_local="";
		String _correo_cliente_local="";
		String _telefono_cliente_local="";



		int _nube;


		// prepara la direccion de la nube

		RequestQueue queue = Volley.newRequestQueue(this);

		url = _api_key_clientes;

		_texto_nube="Si";

		int _folio_int = 0;

		_detalle_corte="SEC RUC       RAZON SOCIAL"+_salto;
		_detalle_corte="Numero de Reg por Bajar:"+numero_registros_por_bajar+_salto;

		//	_detalle_corte=url+_salto;

		if (c2.moveToFirst()) {
			do {
				// 0 "_id",
				// 1 "ruc_cliente",
				// 2 "razon_social_cliente",
				// 3 "direccion_cliente",
				// 4 "correo_cliente",
				// 5 "telefono_cliente",
				// 6 "tipo_identidad"};

				_folio_int++;


				id = c2.getInt(0);

				_ruc_cliente_local = c2.getString(1);
				_razon_social_cliente_local = c2.getString(2);
				_direccion_cliente_local = c2.getString(3);
				_correo_cliente_local = c2.getString(4);
				_telefono_cliente_local = c2.getString(5);
				_tipo_identificacion_cliente_local = c2.getString(6);


				_texto_nube="No";

				_mensaje_error="";

				_detalle_corte=_detalle_corte+_folio_int+" "+_ruc_cliente_local+" "+_razon_social_cliente_local+_salto;


			} while (c2.moveToNext());


		}

		_detalle_corte=_detalle_corte+_linea+_salto+_salto;


		_salida=_salida+_linea+_salto;
		_salida=_salida+_detalle_corte;
		_salida=_salida+_salto;

		return _salida;


	}




		private void agrega_Clientes(
			String _Ruc,
			String	_Razon_Social,
			String	_Direccion,
			String	_Correo,
			String	_Telefono) {


		db = new connectionDB(this);
		db.addNotes_Clientes(_Razon_Social, _Ruc, _Direccion,  _Telefono, _Correo);
		//db.close();

	}


	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////

	private String _Reporte_Descarga_Nube_Productos (String _fecha) {


		int _folio_nuevo=0;
		String _licencia="";


		String _linea01="";
		String _linea02="";
		String _linea03="";
		String _linea04="";
		String _api_key="";
		String _api_key2="";

		String _ruc_empresa="";



		Cursor cursor =  db.getReg_TicketPos(_myId);


		if (cursor.moveToFirst()) {
			do {


				_linea01 = cursor.getString(1);
				_linea02 = cursor.getString(2);
				_linea03 = cursor.getString(3);
				_linea04 = cursor.getString(4);


				_ruc_empresa = cursor.getString(10);
				_base01 = cursor.getString(11);
				_base02 = cursor.getString(12);
				_base03 = cursor.getString(13);
				_base04 = cursor.getString(14);
				_base05 = cursor.getString(15);
				_base06 = cursor.getString(16);



			} while (cursor.moveToNext());

		}

		// me traigo la cabecera


		String _api_key_doctos="";
		String _api_key_movtos="";
		String _api_key_productos="";
		String _api_key_clientes="";
		String _api_key_almacen="";
		String _api_key_movimientos="";
		String _api_key_00="";
		String _api_key_01="";
		String _api_key_02="";


		Cursor cursor2 =  db.getReg_Apiskey(1);

		if (cursor2.moveToFirst()) {
			do {

				_api_key_doctos = cursor2.getString(1);
				_api_key_movtos = cursor2.getString(2);
				_api_key_productos = cursor2.getString(3);
				_api_key_clientes = cursor2.getString(4);
				_api_key_almacen = cursor2.getString(5);
				_api_key_movimientos = cursor2.getString(6);
				_api_key_00 = cursor2.getString(7);
				_api_key_01 = cursor2.getString(8);
				_api_key_02 = cursor2.getString(9);

			} while (cursor2.moveToNext());

		}


		String _salida = "";
		String _cadena = "";
		String _salto ="\n";

		final String _almacen=_api_key_01;

		String _linea="";

		String _titulo ="================================"+_salto+
				        "REP DESCARGA PROD. DE LA NUBE"+_salto+
				"ALMACEN:"+_almacen+_salto+
				"api clientes: "+_api_key_productos+_salto+
				"api existencias: "+_api_key_almacen+_salto+
				//		"api productos"+_api_key_productos+_salto+
				//		"api almacen"+_api_key_almacen+_salto+
				//		"api movimientos"+_api_key_movimientos+_salto+
				//		"api 00"+_api_key_00+_salto+
				//		"api 01"+_api_key_01+_salto+
				//		"api 02"+_api_key_02+_salto+

				"================================"+_salto;


		// _salida=_salida+_salto;
		//	_salida=_salida+_salto;
		_salida=_salida+_titulo+_salto;


		if (_linea01 != null ) {
			_salida = _salida + _linea01 + _salto;
		}

		_salida=_salida+_salto;

		if (_linea02 != null ) {
			_salida = _salida + _linea02 + _salto;
		}

		if (_linea03 != null ) {
			if (_linea03.trim().length() > 0) {
				_salida = _salida + _linea03 + _salto;
			}
		}

		if (_linea04 != null ) {
			if (_linea04.trim().length() > 0) {
				_salida = _salida + _linea04 + _salto;
			}
		}

		_salida=_salida+_linea+_salto;

		String url = _api_key_productos;

		String _ruc_cliente="";

		String _detalle_corte;


		///////////////////////////////////////////////////////////////////////////////////
		// bajar productos de la nube
		///////////////////////////////////////////////////////////////////////////////////

		mQueue = Volley.newRequestQueue(this);

		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("art_m");
//							Toast.makeText(getApplicationContext(), response.toString()+",,,"+jsonArray.length() , Toast.LENGTH_LONG ).show();
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject employee = jsonArray.getJSONObject(i);
								String _id = employee.getString("id");
								String _ref = employee.getString("ref");
								String _name = employee.getString("name");
								String _precio = employee.getString("pvp");
								String _precio_mayoreo = employee.getString("pvp_tpv");



								int numero_registros_por_bajar=jsonArray.length();

									if (db.exits_Productos(_ref)==0) {
										agrega_Productos(_ref,_name, _precio, _precio_mayoreo, _id);
									}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		});

		mQueue.add(request);


		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		// poner las existencias a cero y ventas a cero
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		Cursor c_prod = db.getNotes_productos();

		String _prod_a_limpiar="";
		double _ventas_pro=0.00;

		//
		// {TABLE_ID, 0
		// ID_PRODUCTO, 1
		// DESCRIPCION_PRODUCTO, 2
		// UNIDAD_PRODUCTO, 3
		// PRECIO_PRODUCTO, 4
		// PRECIO_PRODUCTO_MAYOREO, 5
		// IGV_PRODUCTO, 6
		// EXISTENCIA, 7
		// VENTAS_PRO, 8
		// SALDO_PRO   9};

		if (c_prod.moveToFirst()) {
			do {
				_prod_a_limpiar = c_prod.getString(1);
			//	_ventas_pro = c_prod.getDouble(8);
				update_existencia(_prod_a_limpiar,0.00, 0.00);


			} while (c_prod.moveToNext());


		}






		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
		// traer las existecias de la nube
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////

		url=_api_key_almacen;

		JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("exs_g");

						//	Toast.makeText(getApplicationContext(), response.toString()+",,,"+jsonArray.length() , Toast.LENGTH_LONG ).show();

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject employee = jsonArray.getJSONObject(i);

								// "alm": "1SGDA",

								String _ref = employee.getString("ref");
								String _name = employee.getString("name");
								double _existencias = employee.getDouble("exs");
								String _alm = employee.getString("alm");
								if (_alm.trim().equals(_almacen)) {
									double total_ventas_Productos=db.total_ventas_Productos(_ref);
									update_existencia(_ref,_existencias, total_ventas_Productos);

								}





							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		});

		mQueue.add(request2);






		Cursor c2 = db.getNotes_productos();

	//	int id=0;
		String _producto="";
		String _descripcion="";



		int _nube;


		// prepara la direccion de la nube

		RequestQueue queue = Volley.newRequestQueue(this);

		url = _api_key_clientes;

		_texto_nube="Si";

		int _folio_int = 0;
		int id=0;

	//	_detalle_corte="SEC PROD  DESCRIPCION"+_salto;
		_detalle_corte="LOS PRODUCTO HAN SIDO DESCARGADOS"+_salto;
	//	_detalle_corte="Numero de Reg por Bajar:"+numero_registros_por_bajar+_salto;

		//	_detalle_corte=url+_salto;

		double _exs=0;

		if (c2.moveToFirst()) {
			do {

				_folio_int++;
				id = c2.getInt(0);
				_producto = c2.getString(1);
				_descripcion = c2.getString(2);
				_exs = c2.getDouble(7);
				_texto_nube="No";
				_mensaje_error="";
			//	_detalle_corte=_detalle_corte+_folio_int+" "+_producto+" saldo "+_exs+_salto;


			} while (c2.moveToNext());


		}

		_detalle_corte=_detalle_corte+_linea+_salto+_salto;


		_salida=_salida+_linea+_salto;
		_salida=_salida+_detalle_corte;
		_salida=_salida+_salto;

		return _salida;


	}

	private void agrega_Productos(
			String _ref,
			String	_name,
			String _precio,
			String _precio_mayoreo,
			String _id_velneo
			) {


		db = new connectionDB(this);
		db.addNotes_Productos(_ref, _name, "NIU",  _precio, _precio_mayoreo, ".18", _id_velneo);

		//db.close();

	}



	private void update_existencia (String _prod,  Double _existencias, Double _ventas) {

		ContentValues valoresProductos = new ContentValues();
		valoresProductos.put("existencia", _existencias);
		valoresProductos.put("saldo_pro", _existencias-_ventas);

		valoresProductos.put("ventas_pro", _ventas);


		db = new connectionDB(this);
		String _alcance = "WHERE _id="+_prod;
		db.getWritableDatabase().update("productos", valoresProductos, "producto" + "=?",new String[] { String.valueOf(_prod) });

	}

	private void update_ventas (String _prod,   Double _ventas) {

		ContentValues valoresProductos = new ContentValues();
	//	valoresProductos.put("existencia", _existencias);
	//	valoresProductos.put("saldo_pro", _existencias-_ventas);

		valoresProductos.put("ventas_pro", _ventas);


		db = new connectionDB(this);
		String _alcance = "WHERE _id="+_prod;
		db.getWritableDatabase().update("productos", valoresProductos, "producto" + "=?",new String[] { String.valueOf(_prod) });

	}



	private void acumular_ventas (String _prod,  Double _ventas) {



		double total_ventas_Productos=db.total_ventas_Productos(_prod);
		double total_existencias_Productos=db.total_existecias_Productos(_prod);

		ContentValues valoresProductos = new ContentValues();

		double _ventas_acumuladas_total=_ventas+total_ventas_Productos;


//		valoresProductos.put("existencia", total_existencias_Productos);
		valoresProductos.put("ventas_pro", _ventas_acumuladas_total);
		valoresProductos.put("saldo_pro", total_existencias_Productos-_ventas_acumuladas_total);


		db = new connectionDB(this);
		String _alcance = "WHERE _id="+_prod;
		db.getWritableDatabase().update("productos", valoresProductos, "producto" + "=?",new String[] { String.valueOf(_prod) });

	}


	private void update_cabecera_ventas (String  _id_app,   String _id_velneo) {

		ContentValues valoresCabecera = new ContentValues();
		valoresCabecera.put("_id_velneo", _id_velneo);

		db = new connectionDB(this);
		String _alcance = "WHERE _id="+_id_app;
		db.getWritableDatabase().update("cabecera", valoresCabecera, "_id" + "=?",new String[] { String.valueOf(_id_app) });

	}



	private void update_linea_detalle (int  _id, int linea) {

		ContentValues valoresDetalle = new ContentValues();
		valoresDetalle.put("linea", linea);

		db = new connectionDB(this);
		//	String _alcance = "WHERE _id="+_id;
		db.getWritableDatabase().update("detalle", valoresDetalle, "_id" + "=?",new String[] { String.valueOf(_id) });

	}





}