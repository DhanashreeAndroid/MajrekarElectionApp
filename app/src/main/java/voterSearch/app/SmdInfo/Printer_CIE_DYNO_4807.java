package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.cie.btp.Barcode;
import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;
import com.cie.btp.FontStyle;
import com.cie.btp.FontType;
import com.cie.btp.PrintColumnParam;
import com.cie.btp.PrintImageColumn;
import com.cie.btp.PrinterWidth;
import com.printer.ZQPrinter;

import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_CONN_DEVICE_NAME;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_CONN_STATE_CONNECTED;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_CONN_STATE_CONNECTING;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_CONN_STATE_LISTEN;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_CONN_STATE_NONE;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_MESSAGES;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_MSG;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_NAME;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_NOTIFICATION_ERROR_MSG;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_NOTIFICATION_MSG;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_NOT_CONNECTED;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_NOT_FOUND;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_SAVED;
import static com.cie.btp.BtpConsts.RECEIPT_PRINTER_STATUS;

public class Printer_CIE_DYNO_4807 extends Activity {
    DBAdapter mDbAdapter;
    final Context context = this;
    TextView txtPreview;
    Button btnPrint, btnSelectPrinter;

    ProgressDialog pdWorkInProgress;

    private static final int BARCODE_WIDTH = 384;
    private static final int BARCODE_HEIGHT = 100;
    private static final int QRCODE_WIDTH = 100;

    public CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;
    private int imageAlignment = 1;

    String Printer_Address;

    SharedPreferences pref;
    String Print_Data;
    String Print_Activity;
    String Seperate_printData[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        setContentView(R.layout.printer);

        pdWorkInProgress = new ProgressDialog(this);
        pdWorkInProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            mPrinter.initService(Printer_CIE_DYNO_4807.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtPreview = (TextView) findViewById(R.id.txtPreview);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnSelectPrinter = findViewById(R.id.btnSelctPrinter);
        Typeface font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));

        //---------------------------------------------------------------------------------------------------------------------------------
        pref = getApplicationContext().getSharedPreferences("Printer", 0);
        Print_Data = pref.getString("Print_Data", "");
        Print_Activity = pref.getString("Print_Activity", "");

        Seperate_printData = Print_Data.split("----------- Cut Here -----------");
        //---------------------------------------------------------------------------------------------------------------------------------
        if (Print_Activity.equals("Family_Member_Listing")) {
            SpannableStringBuilder str = new SpannableStringBuilder();
            int len = Seperate_printData.length;
            for (int i = 0; i < len; i++) {
                str.append(Seperate_printData[i]);

                if (i == len - 1) {
                    break;
                }

                Spanned red = Html.fromHtml("<font color='#FF0000'>----------- Cut Here -----------</font>");
                //SpannableString redSpannable= new SpannableString(red);
                //redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
                str.append(red + "\n\n");

            }
            txtPreview.setText(str, BufferType.SPANNABLE);
        } else {
            txtPreview.setText(Print_Data);
        }

        txtPreview.setTypeface(font);


        //Bundle extras=getIntent().getExtras();
        //Printer_Address = extras.getString("Printer_Add");



        btnSelectPrinter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrinter.selectPrinter(Printer_CIE_DYNO_4807.this);
            }
        });
        btnPrint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPrinter.connectToPrinter();
            }
        });

    }

    @Override
    protected void onResume() {
        DebugLog.logTrace();
        mPrinter.onActivityResume();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        DebugLog.logTrace();
        mPrinter.onActivityPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DebugLog.logTrace("onDestroy");
        mPrinter.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIPT_PRINTER_MESSAGES);
        LocalBroadcastManager.getInstance(this).registerReceiver(ReceiptPrinterMessageReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(ReceiptPrinterMessageReceiver);
        } catch (Exception e) {
            DebugLog.logException(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPrinter.onActivityRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final BroadcastReceiver ReceiptPrinterMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DebugLog.logTrace("Printer Message Received");
            Bundle b = intent.getExtras();

            switch (b.getInt(RECEIPT_PRINTER_STATUS)) {
                case RECEIPT_PRINTER_CONN_STATE_NONE:
                    Toast.makeText(getApplicationContext(), R.string.printer_not_conn, Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_CONN_STATE_LISTEN:
                    Toast.makeText(getApplicationContext(), R.string.ready_for_conn, Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_CONN_STATE_CONNECTING:
                    Toast.makeText(getApplicationContext(), R.string.printer_connecting, Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_CONN_STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(), R.string.printer_connected, Toast.LENGTH_SHORT).show();
                    new AsyncPrint().execute();
                    break;
                case RECEIPT_PRINTER_CONN_DEVICE_NAME:
                    Toast.makeText(getApplicationContext(), b.getString(RECEIPT_PRINTER_NAME), Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_NOTIFICATION_ERROR_MSG:
                    String n = b.getString(RECEIPT_PRINTER_MSG);
                    Toast.makeText(getApplicationContext(), n, Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_NOTIFICATION_MSG:
                    String m = b.getString(RECEIPT_PRINTER_MSG);
                    Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_NOT_CONNECTED:
                    Toast.makeText(getApplicationContext(), "Status : Printer Not Connected", Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_NOT_FOUND:
                    Toast.makeText(getApplicationContext(), "Status : Printer Not Found", Toast.LENGTH_SHORT).show();
                    break;
                case RECEIPT_PRINTER_SAVED:
                    Toast.makeText(getApplicationContext(), R.string.printer_saved, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    private class AsyncPrint extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnPrint.setEnabled(false);

            pdWorkInProgress.setIndeterminate(true);
            pdWorkInProgress.setMessage("Printing ...");
            pdWorkInProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            pdWorkInProgress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);

            mPrinter.resetPrinter();

			/*mPrinter.setAlignmentCenter();
			mPrinter.setBoldOn();
			mPrinter.setCharRightSpacing(10);
			mPrinter.printTextLine("\nMY COMPANY BILL\n");
			mPrinter.setBoldOff();
			mPrinter.setCharRightSpacing(0);
			mPrinter.printTextLine("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			mPrinter.pixelLineFeed(50);*/
            // Bill Header End

            // Bill Details Start

            mPrinter.setAlignmentLeft();
            mPrinter.setFontStyle(false, false, FontStyle.NORMAL, FontType.FONT_A);

            if (Print_Activity.equals("Family_Member_Listing")) {
                for (int i = 0; i < Seperate_printData.length; i++) {
                    mPrinter.printTextLine(Seperate_printData[i] + "\n");

                    if (i == Seperate_printData.length - 1) {
                        break;
                    }
                    mPrinter.printTextLine("---------Cut Here--------\n");
                }
            } else {
                mPrinter.printTextLine(Print_Data + "\n");
            }

            mPrinter.printLineFeed();
			/*mPrinter.setFontStyle(true,true, FontStyle.DOUBLE_HEIGHT, FontType.FONT_A);
			mPrinter.printTextLine("    Thank you ! Visit Again   \n");*/
            //mPrinter.setFontStyle(false,false, FontStyle.NORMAL, FontType.FONT_A);
			/*mPrinter.printLineFeed();
			mPrinter.printTextLine("******************************\n");
			mPrinter.printLineFeed();

			mPrinter.printTextLine("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			mPrinter.printLineFeed();*/

			/*mPrinter.setAlignmentCenter();

			mPrinter.printQRcode("http://www.coineltech.com/",QRCODE_WIDTH, imageAlignment);
			mPrinter.printLineFeed();

			mPrinter.printBarcode("1234567890123", Barcode.CODE_128,BARCODE_WIDTH, BARCODE_HEIGHT, imageAlignment);
			//mPrinter.setAlignmentCenter();
			mPrinter.setCharRightSpacing(10);
			mPrinter.printTextLine("  1234567890123\n");

			mPrinter.printUnicodeText(" SDK can print in any language that the android device supports and display. \n" +
					" English - English \n" +
					" kannada - ಕನ್ನಡ \n" +
					" Hindi - हिंदी \n" +
					" Tamil - தமிழ் \n" +
					" Telugu - తెలుగు \n" +
					" Marathi - मराठी \n" +
					" Malayalam - മലയാളം \n" +
					" Gujarati - ગુજરાતી \n" +
					" Urdu -  اردو" +
					"\n");*/

			/*String[] sCol1 = {"ABC","DEFG","H","IJKLM","XYZ"};
			PrintColumnParam pcp1stCol = new PrintColumnParam(sCol1,33, Layout.Alignment.ALIGN_NORMAL,22, Typeface.create(Typeface.SANS_SERIF,Typeface.NORMAL));
			String[] sCol2 = {":",":",":",":",":"};
			PrintColumnParam pcp2ndCol = new PrintColumnParam(sCol2,33,Layout.Alignment.ALIGN_CENTER,22);
			String[] sCol3 = {"₹1.00","₹20.00","₹300.00","₹4,000.00","₹50,000.89"};
			PrintColumnParam pcp3rdCol = new PrintColumnParam(sCol3,33,Layout.Alignment.ALIGN_OPPOSITE,22);
			mPrinter.PrintTable(pcp1stCol,pcp2ndCol,pcp3rdCol);

			Bitmap c1 = BitmapFactory.decodeResource(getResources(), R.drawable.c1_100);
			Bitmap c2 = BitmapFactory.decodeResource(getResources(), R.drawable.c2_100);
			PrintImageColumn pic1 = new PrintImageColumn(c1, 50, Layout.Alignment.ALIGN_NORMAL);
			PrintImageColumn pic2 = new PrintImageColumn(c2, 50, Layout.Alignment.ALIGN_OPPOSITE);
			mPrinter.PrintImageTable(pic1, pic2);
			mPrinter.printLineFeed();

			pic1 = new PrintImageColumn(c1, 50, Layout.Alignment.ALIGN_OPPOSITE);
			pic2 = new PrintImageColumn(c2, 50, Layout.Alignment.ALIGN_NORMAL);
			mPrinter.PrintImageTable(pic1, pic2);
			mPrinter.printLineFeed();

			pic1 = new PrintImageColumn(c1, 50, Layout.Alignment.ALIGN_CENTER);
			pic2 = new PrintImageColumn(c2, 50, Layout.Alignment.ALIGN_CENTER);
			mPrinter.PrintImageTable(pic1, pic2);
			mPrinter.printLineFeed();*/

            //Clearance for Paper tear
            mPrinter.printLineFeed();
            mPrinter.printLineFeed();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //wait for printing to complete
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mPrinter.disconnectFromPrinter();
            btnPrint.setEnabled(true);
            pdWorkInProgress.cancel();
        }
    }


}
