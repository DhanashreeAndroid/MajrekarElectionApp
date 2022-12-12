package voterSearch.app.SmdInfo;

import com.printer.ZQPrinter;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class Printer extends Activity{
	DBAdapter mDbAdapter;
	final Context context = this;
	TextView txtPreview;
	Button btnPrint;
	private ZQPrinter PrinterService = null;   
	String Printer_Address;
	
	SharedPreferences pref;
	String Print_Data;
	String Print_Activity;
	String Seperate_printData[];

	byte FONT_TYPE;
	private static BluetoothSocket btsocket;
	private static OutputStream outputStream;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDbAdapter = new DBAdapter(this);
		mDbAdapter.open();
		setContentView(R.layout.printer);
		
		txtPreview = (TextView) findViewById(R.id.txtPreview);
		btnPrint   = (Button) findViewById(R.id.btnPrint);
		
		Typeface font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		
		//---------------------------------------------------------------------------------------------------------------------------------	
			pref = getApplicationContext().getSharedPreferences("Printer", 0);
			Print_Data = pref.getString("Print_Data", "");
			Print_Activity = pref.getString("Print_Activity", "");
			
			Seperate_printData = Print_Data.split("----------- Cut Here -----------");
		//---------------------------------------------------------------------------------------------------------------------------------		
			if(Print_Activity.equals("Family_Member_Listing"))
			 {
				SpannableStringBuilder  str = new SpannableStringBuilder ();
				int len = Seperate_printData.length;
						for(int i = 0; i < len; i++)
						 {
							str .append(Seperate_printData[i]);
							
							if(i == len-1)
							{
								break;
							}
							
							Spanned red = Html.fromHtml("<font color='#FF0000'>----------- Cut Here -----------</font>");
							//SpannableString redSpannable= new SpannableString(red);
							//redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
							str.append(red + "\n\n");
							
						 }
				txtPreview.setText(str, BufferType.SPANNABLE);
			 }
			else
			{
				txtPreview.setText(Print_Data);
			}
			
			txtPreview.setTypeface(font);
			

		/*Bundle extras=getIntent().getExtras();
		Printer_Address = extras.getString("Printer_Add");
		
		PrinterService = new ZQPrinter();*/
		
		btnPrint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int nRet = PrinterService.Connect(Printer_Address);
				int returevlaue = ZQPrinter.AB_SUCCESS;
				
				if ( nRet == 0 )
				 {
					 returevlaue = PrinterService.GetStatus(); 
					 	
						 if(returevlaue == ZQPrinter.AB_SUCCESS)
						 {
								 if(Print_Activity.equals("Family_Member_Listing"))
								 {
									 for(int i = 0; i < Seperate_printData.length ; i++)
									 {
										 returevlaue = PrinterService.PrintText(Seperate_printData[i], ZQPrinter.ALIGNMENT_LEFT, 
								 					   ZQPrinter.FT_DEFAULT, 
								 					   ZQPrinter.TS_1WIDTH | 
								 					   ZQPrinter.TS_1HEIGHT );
										 
										if(i == Seperate_printData.length - 1 )
										{
											break;
										}
										
										returevlaue = PrinterService.PrintText("            Cut Here            ", ZQPrinter.ALIGNMENT_LEFT, 
						 					   	   	  ZQPrinter.FT_REVERSE, 
						 					          ZQPrinter.TS_1WIDTH | 
						 					          ZQPrinter.TS_1HEIGHT );
									 }
							 }
							 else
							 {
								 returevlaue = PrinterService.PrintText(Print_Data, ZQPrinter.ALIGNMENT_LEFT, 
					 					   	   ZQPrinter.FT_DEFAULT, 
					 					   	   ZQPrinter.TS_1WIDTH | 
					 					   	   ZQPrinter.TS_1HEIGHT );
							 }
							 
							 returevlaue = PrinterService.PrintImage("file:///android_assets/image2.jpg", 0, 0, 0);
							 
							 returevlaue=PrinterService.LineFeed(3);
							
						 }
						 else
						 {
							 Toast.makeText(getApplicationContext(), "Problem in connection", Toast.LENGTH_SHORT).show();
						 }
					 		
		            }
				 else
				 {
					 Toast.makeText(getApplicationContext(), "Check Printer", Toast.LENGTH_SHORT).show();
				 }
				//getImage();
				//printDemo();
			}
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			btsocket = DeviceList.getSocket();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void printDemo() {
		if(btsocket == null){
			Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
			this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
		}
		else{
			OutputStream opstream = null;
			try {
				opstream = btsocket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			outputStream = opstream;

			//print command
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				outputStream = btsocket.getOutputStream();

				byte[] printformat = { 0x1B, 0*21, FONT_TYPE };
				//outputStream.write(printformat);

				printText("मतदान केंद्र पत्ता :".getBytes(Charset.forName("UTF-8")));

				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void printPhoto() {
		try {
			Bitmap bmp = getImage("मतदान केंद्र पत्ता :");
			if(bmp!=null){
				byte[] command = Utils.decodeBitmap(bmp);


				outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
				printText(command);
			}else{
				Log.e("Print Photo error", "the file isn't exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("PrintTools", "the file isn't exists");
		}
	}

	private Bitmap getImage(String msg) {
			final Rect bounds = new Rect();
			TextPaint textPaint = new TextPaint() {
				{
					setColor(Color.BLACK);
					setTextAlign(Align.LEFT);
					setTextSize(28f);
					setAntiAlias(true);
				}
			};


			Spannable wordtoSpan = new SpannableString(msg);

			wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			textPaint.getTextBounds(wordtoSpan.toString(), 0, msg.length(), bounds);
			StaticLayout mTextLayout = new StaticLayout(Html.fromHtml(msg), textPaint,
					bounds.width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

			int maxWidth = -1;
			for (int i = 0; i < mTextLayout.getLineCount(); i++) {
				if (maxWidth < mTextLayout.getLineWidth(i)) {
					maxWidth = (int) mTextLayout.getLineWidth(i);
				}
			}
			final Bitmap bmp_text = Bitmap.createBitmap(maxWidth, mTextLayout.getHeight(),
					Bitmap.Config.ARGB_8888);
			bmp_text.eraseColor(Color.WHITE);// just adding black background
			final Canvas canvas = new Canvas(bmp_text);
			mTextLayout.draw(canvas);
			return bmp_text;


	}

	private void printText(byte[] msg) {
		try {
			// Print normal text
			outputStream.write(msg);
			printNewLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printNewLine() {
		try {
			outputStream.write(PrinterCommands.FEED_LINE);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Override
	    public void onDestroy() {
	    	 
	        super.onDestroy();
	        if(PrinterService != null)
	        {	
	         PrinterService.Disconnect(); 
	         PrinterService = null;
	        }
	    }   

}
