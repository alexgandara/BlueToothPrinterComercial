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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import zj.com.cn.bluetooth.sdk.R;
import zj.com.command.sdk.Command;
import zj.com.command.sdk.PrintPicture;
import zj.com.command.sdk.PrinterCommand;
import zj.com.customize.sdk.Other;

import static global.factura.qrpos.DbBitmapUtility.getBytes;
import static global.factura.qrpos.DbBitmapUtility.getImage;

public class Modificar2_archivadas extends Activity implements OnClickListener{
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

	//QRcode
	private static final int QR_WIDTH = 350;
	private static final int QR_HEIGHT = 350;
/*******************************************************************************************************/
	private static final String CHINESE = "GBK";
	private static final String THAI = "CP874";
	private static final String KOREAN = "EUC-KR";
	private static final String BIG5 = "BIG5";

/*********************************************************************************/
	private TextView mTitle;
	EditText editText;
	ImageView imageViewPicture;
	private static boolean is58mm = true;
	private RadioButton width_58mm, width_80;
	private RadioButton thai, big5, Simplified, Korean;
	private CheckBox hexBox;
	private Button sendButton = null;
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
    public String _detalle="";
    public String Mcorreo="";

    public double _igv_global;
    public String _datos_qr="";


	public String _base01="";
	public String _base02="";
	public String _base03="";
	public String _base04="";


	EditText serie, folio, ruc, razon_social, direccion, moneda, fecha, correo;

    String _serie, _folio, _ruc, _razon_social, _direccion, _moneda, _fecha, _correo;

    int _myId;
    connectionDB db;
    Button Modificar, Eliminar, Salir, Detalle, Imprimir, Documentos, DesArchivar;

	// alex

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main2_archivadas);


		Bundle b = getIntent().getExtras();
		if (b!=null) {
			_myId = b.getInt("id");
			Toast.makeText(Modificar2_archivadas.this,"ID a Modifcar :"+_myId, Toast.LENGTH_SHORT ).show();

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
                Mserie = cursor.getString(1);
                Mfolio = cursor.getInt(2);
                Mruc = cursor.getString(3);
                Mrazon_social = cursor.getString(4);
                Mdireccion = cursor.getString(5);
                Mmoneda = cursor.getString(6);
                Mfecha = cursor.getString(7);
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
            double precio, cantidad, precio_sin_igv, subtotal, total, igv;
            String _salto ="\n";
            _detalle="";

            total=0;
            subtotal=0;
            _total=0;
            _subtotal=0;
            _igv=0;



            if (c.moveToFirst()) {
                do {
                    id = c.getInt(0);
                    producto = c.getString(1);
                    descripcion = c.getString(2);
                    unidad = c.getString(3);
                    cantidad = c.getDouble(4);
                    precio = c.getDouble(5);
                    igv = c.getDouble(6);
                    _igv_global=igv;



                    precio_sin_igv=precio/(1+igv);
                    subtotal=precio_sin_igv*cantidad;
                    _subtotal=_subtotal+subtotal;
                    _total=_total+(cantidad*precio);




                    item.add(id+" - "+producto+" - "+descripcion+" - "+unidad+" - "+cantidad+" "+precio);
                    //"CANT.  UNI    PRECIO     IMPORTE"+_salto;
                    _detalle=_detalle+descripcion.toUpperCase()+_salto;
                    _detalle=_detalle+_cantidad(cantidad)+" "+unidad+"    "+_cantidad(precio_sin_igv)+"     "+_cantidad(subtotal)+_salto;


                } while (c.moveToNext());
                _igv=_total-_subtotal;


            }





        }


        Modificar = (Button) findViewById(R.id.button_Modificar);
        Eliminar = (Button) findViewById(R.id.button_Eliminar);
        Detalle = (Button) findViewById(R.id.button_Detalle);
//        Imprimir = (Button) findViewById(R.id.button_Imprimir);
        Documentos = (Button) findViewById(R.id.button_Documentos);
		DesArchivar = (Button) findViewById(R.id.button_Activar);

        Salir = (Button) findViewById(R.id.button_Salir);



        serie = (EditText) findViewById(R.id.editText_Serie);
        folio = (EditText) findViewById(R.id.editText_Folio);
        ruc = (EditText) findViewById(R.id.editText_Ruc);
        razon_social = (EditText) findViewById(R.id.editText_RazonSocial);
        direccion = (EditText) findViewById(R.id.editText_Direccion);
        moneda = (EditText) findViewById(R.id.editText_Moneda);
        fecha = (EditText) findViewById(R.id.editText_Fecha);




        Modificar.setOnClickListener(new OnClickListener() {
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


		DesArchivar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				DesArchivar(_myId);
			}
		});





		Detalle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modificar2_archivadas.this,MainActivity_Detalle.class );
                intent.putExtra("id",_myId);
                startActivity(intent);
            }
        });


        Documentos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modificar2_archivadas.this,init_alfilPOS.class );
                startActivity(intent);
            }
        });






        Eliminar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar(_myId);

            }
        });









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


		imageViewPicture.setEnabled(false);
	//	width_58mm.setEnabled(false);
//		width_80.setEnabled(false);
	//	hexBox.setEnabled(false);
		sendButton.setEnabled(false);


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
			Intent serverIntent = new Intent(Modificar2_archivadas.this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			break;
		}
		case R.id.btn_close:{
			mService.stop();

			imageViewPicture.setEnabled(false);
		//	width_58mm.setEnabled(false);
		//	width_80.setEnabled(false);
		//	hexBox.setEnabled(false);
			sendButton.setEnabled(false);


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
					Toast.makeText(Modificar2_archivadas.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
				}
			} else {
				// manda Imprimir el Ticket POS con QR
				String msg = Imprimir_Ticket();
				if(msg.length()>0){

					Print_BMP();


					String _buttom="";
					String _salto ="\n";

					String _linea = "================================";

					SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
					SendDataByte(Command.LF);

					createImage(_datos_qr);




                    _buttom=_buttom+_base01+_salto;
                    _buttom=_buttom+_base02+_salto;
                    _buttom=_buttom+_base03+_salto;
                    _buttom=_buttom+_base04+_salto;

					_buttom=_buttom+_salto;
					_buttom=_buttom+"Representacion    Impresa     de"+_salto;
					_buttom=_buttom+"del   Comprobante    de    Venta"+_salto;
					_buttom=_buttom+"Electronica  autorizado mediante"+_salto;
					_buttom=_buttom+"la   Resolucion   155-2017/SUNAT"+_salto;
					_buttom=_buttom+_linea+_salto;
					_buttom=_buttom+_salto+_salto;

					SendDataByte(PrinterCommand.POS_Print_Text(_buttom, CHINESE, 0, 0, 0, 0));
					SendDataByte(Command.LF);



				}
			}
			break;
		}


		case R.id.btn_printpicture:{
			GraphicalPrint();
			break;
			}
		case R.id.imageViewPictureUSB:{



			Intent loadpicture = new Intent(
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(loadpicture, REQUEST_CHOSE_BMP);







			break;
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
				Intent serverIntent = new Intent(Modificar2_archivadas.this, init_alfilPOS.class);
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

					Escribir_bmp(_myId, bitmap);

					if (null != bitmap) {
						imageViewPicture.setImageBitmap(bitmap);
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

			String data = "    Conexion Esteblacida...  \n";
			String data2 = "<<<   www.factura.global  >>>\n\n\n";

		//	SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 1, 1, 0));
			SendDataByte(PrinterCommand.POS_Print_Text(data+data2, CHINESE, 0, 0, 0, 0));
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
			SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
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

		new AlertDialog.Builder(Modificar2_archivadas.this).setTitle(getText(R.string.btn_prtcode))
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				else if(which == 4)
				{
					if(str.length() == 0)
					{
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.msg_error), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Modificar2_archivadas.this, getText(R.string.empty1), Toast.LENGTH_SHORT).show();
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
			Toast.makeText(Modificar2_archivadas.this, getText(R.string.empty1), Toast.LENGTH_SHORT).show();
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
		new AlertDialog.Builder(Modificar2_archivadas.this).setTitle(getText(R.string.chosecommand))
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
			new AlertDialog.Builder(Modificar2_archivadas.this).setTitle(getText(R.string.chosecommand))
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
	            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
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
        Toast.makeText(Modificar2_archivadas.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

        Intent intent = new Intent(this,init_alfilPOS.class );
        startActivity(intent);

    }




	private void Archivar(int _id) {

		ContentValues valoresCabecera = new ContentValues();
		valoresCabecera.put("archivada", 1);


		db = new connectionDB(this);

		String _alcance = "WHERE _id="+_id;

		db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
		Toast.makeText(Modificar2_archivadas.this,"Se Archivo el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

		Intent intent = new Intent(this,init_alfilPOS.class );
		startActivity(intent);

	}












	private void Eliminar(int _id) {



        db = new connectionDB(this);



        db.getWritableDatabase().delete(TABLE,TABLE_ID + "=?",new String[] { String.valueOf(_id) });
        Toast.makeText(Modificar2_archivadas.this,"Se Elimino el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

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
        Toast.makeText(Modificar2_archivadas.this,"Se Modifico el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

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

            if (!_licencia.equals("WESQ-WERS-TYVS")) {
                if (_folio_nuevo>999) {
                    _folio_nuevo=9999999;
                    Mrazon_social=_licencia+"  LICENCIA    AGOTADA      llame   a    factura global    954 879 529    sopote@factura.global          - - - - - - - - - - - - - - - ";
                }
            }



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
        String _ruc_empresa="";


        //∫ db = new connectionDB(this);
        Cursor cursor =  db.getReg_TicketPos(_myId);
        if (cursor.moveToFirst()) {
            do {



                _linea01 = cursor.getString(1);
                _linea02 = cursor.getString(2);
                _linea03 = cursor.getString(3);
                _linea04 = cursor.getString(4);
                _linea05 = cursor.getString(5);
                _linea06 = cursor.getString(6);
                _ruc_empresa = cursor.getString(7);
                _base01 = cursor.getString(8);
				_base02 = cursor.getString(9);
				_base03 = cursor.getString(10);
				_base04 = cursor.getString(11);

            } while (cursor.moveToNext());

        }

        // me traigo la cabecera


        String _salida = "";
        String _cadena = "";
        String _salto ="\n";

        String _linea = "================================";


       // _salida=_salida+_salto;
        _salida=_salida+_linea+_salto;



        _salida=_salida+_linea01+_salto;
        _salida=_salida+_salto;
        _salida=_salida+_linea02+_salto;

        if (_linea03.trim().length()>0) {
            _salida=_salida+_linea03+_salto;
        }

        if (_linea04.trim().length()>0) {
            _salida=_salida+_linea04+_salto;
        }

        if (_linea05.trim().length()>0) {
            _salida=_salida+_linea05+_salto;
        }

        if (_linea06.trim().length()>0) {
            _salida=_salida+_linea06+_salto;
        }



        _salida=_salida+_linea+_salto;

        String _naturaleza =  db.get_Naturaleza(Mserie);

        _cadena="LA SERIE NO HA SIDO DEFINIDA";


        if (_naturaleza.equals("03"))  {
            _cadena="BOLETA ELECTRONICA";
        }

        if (_naturaleza.equals("01"))  {
            _cadena="FACTURA ELECTRONICA";
        }

        if (_naturaleza.equals("07"))  {
            _cadena="NOTA DE CREDITO ELECTRONICA"+_salto;

        }

        if (_naturaleza.equals("08"))  {
            _cadena="NOTA DE DEBITO ELECTRONICA";
        }


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
        _salida=_salida+_salto;
        _salida=_salida+_salto;

        _salida=_salida+_linea+_salto;
        _salida=_salida+"                SUBTOTAL:"+_cantidad(_subtotal)+_salto;
        _salida=_salida+"              IGV "+_igv_global+" %:"+_cantidad(_igv)+_salto;
        _salida=_salida+"                   TOTAL:"+_cantidad(_total)+_salto;
        _salida=_salida+_salto;
        _salida=_salida+_salto;
        _salida=_salida+"TIPO DE PAGO:"+"CONTADO"+_salto;
        _salida=_salida+_salto;


        String _buttom_temp="";
		_buttom_temp=_buttom_temp+_base01+_salto;
		_buttom_temp=_buttom_temp+_base02+_salto;
		_buttom_temp=_buttom_temp+_base03+_salto;
		_buttom_temp=_buttom_temp+_base04+_salto;

		_buttom_temp=_buttom_temp+_salto;
		_buttom_temp=_buttom_temp+"Representacion    Impresa     de"+_salto;
		_buttom_temp=_buttom_temp+"del   Comprobante    de    Venta"+_salto;
		_buttom_temp=_buttom_temp+"Electronica  autorizado mediante"+_salto;
		_buttom_temp=_buttom_temp+"la   Resolucion   155-2017/SUNAT"+_salto;
		_buttom_temp=_buttom_temp+_linea+_salto;
		_buttom_temp=_buttom_temp+_salto+_salto;






		ClipboardManager clipboard = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		}
		ClipData clip = ClipData.newPlainText("recibo", _salida+_buttom_temp);
		clipboard.setPrimaryClip(clip);
		Toast.makeText(Modificar2_archivadas.this,"Es Registro fue enviado a Memoria"+_salida, Toast.LENGTH_LONG ).show();

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

	private void DesArchivar(int _id) {

		ContentValues valoresCabecera = new ContentValues();
		valoresCabecera.put("archivada", 0);


		db = new connectionDB(this);

		String _alcance = "WHERE _id="+_id;

		db.getWritableDatabase().update(TABLE, valoresCabecera, TABLE_ID + "=?",new String[] { String.valueOf(_id) });
		Toast.makeText(Modificar2_archivadas.this,"Se Des-Archivo el Reg: :"+_id, Toast.LENGTH_SHORT ).show();

		Intent intent = new Intent(this,init_alfilPOS.class );
		startActivity(intent);

	}




}