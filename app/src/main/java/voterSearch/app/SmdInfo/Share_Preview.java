package voterSearch.app.SmdInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore.Images;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Share_Preview extends Activity {

    Bitmap testB;
    ImageView iv;
    Button btnShare;
    String tempUri;

    SharedPreferences pref;
    String Print_Data;
    String Print_Activity;
    String Seperate_printData[];

    String Message_to_share;

    ConnectionDetector cd;
    private int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1001;

    TextToSpeech mTts;
    String tempAudioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_preview);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        iv = (ImageView) findViewById(R.id.imgMainSms);
        btnShare = (Button) findViewById(R.id.btnShare);

        //---------------------------------------------------------------------------------------------------------------------------------
        pref = getApplicationContext().getSharedPreferences("Printer", 0);
        Print_Data = pref.getString("Print_Data", "");
        Print_Activity = pref.getString("Print_Activity", "");

        Seperate_printData = Print_Data.split("----------- Cut Here -----------");

        //---------------------------------------------------------------------------------------------------------------------------------
        if (Print_Activity.equals("Family_Member_Listing")) {
            StringBuilder str = new StringBuilder();
            int len = Seperate_printData.length;
            for (int i = 0; i < len; i++) {
                str.append(Seperate_printData[i]);

                if (i == len - 1) {
                    break;
                }

                str.append("----------- Cut Here -----------\n\n");
            }

            Message_to_share = str.toString();
        } else {
            Message_to_share = Print_Data;
        }

        //--------------------------------------------------------------------

        //check for permission
        if (ContextCompat.checkSelfPermission(Share_Preview.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
            } else {
                setImage();
            }
        } else {
            setImage();
        }
/*
        try {
            String textForSpeech = getIntent().getExtras().getString("textForSpeech");
            HashMap<String, String> myHashRender = new HashMap<String, String>();
            myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textForSpeech);

            String exStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File appTmpPath = new File(exStoragePath + "/temp/");
            appTmpPath.mkdirs();

            String tempFilename = "temp.mp3";

            tempAudioFile = appTmpPath.getAbsolutePath() + "/" + tempFilename;

            new MySpeech(textForSpeech, myHashRender, tempAudioFile);

        }catch (Exception ex){

        }*/

        btnShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                cd = new ConnectionDetector(getApplicationContext());
                if (cd.isConnectingToInternet()) {
                   /* Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    // Uri uri = Uri.parse("android.resource://dhanashree.example.texttoimage/"+R.drawable.background);
                    Uri uri = Uri.parse(tempUri);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                   // shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, Check your details");
                    startActivity(Intent.createChooser(shareIntent, "Send your image"));*/

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                    intent.setType("image/*"); /* This example is sharing jpeg images. */

                    ArrayList<Uri> files = new ArrayList<Uri>();

                    Uri uri3 = Uri.parse(tempUri);
                    files.add(uri3);
                    /*
                    if(!TextUtils.isEmpty(tempAudioFile)){
                        File file4 = new File(tempAudioFile);
                        Uri uri4 = Uri.fromFile(file4);
                        files.add(uri4);
                    }*/

                    File file1 = new File(getFilePath("video"));
                    if(file1.exists()) {
                        Uri uri1 = Uri.fromFile(file1);
                        files.add(uri1);
                    }

                    File file2 = new File(getFilePath("audio"));
                    if(file2.exists()) {
                        Uri uri2 = Uri.fromFile(file2);
                        files.add(uri2);
                    }


                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_LONG);
                }
            }
        });

    }

    private String getFilePath(String fileType) {
        String path = "";
        String vidhanNo = getIntent().getExtras().getString("vidhansabha");
        String path1 = Environment.getExternalStorageDirectory().toString() + File.separator + "slip" + File.separator;
        String path2 = Environment.getExternalStorageDirectory().toString() + File.separator + "Slip" + File.separator;
        String path3 = Environment.getExternalStorageDirectory().toString() + File.separator + "SLIP" + File.separator;

        File directory1 = new File(path1);
        File directory2 = new File(path2);
        File directory3 = new File(path3);
        File directory;
        if (directory1.exists()) {
            directory = directory1;
        } else if (directory2.exists()) {
            directory = directory2;
        } else {
            directory = directory3;
        }

        File selectedImageFile = null;
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                Log.d("Files", "Size: " + files.length);
                for (int i = 0; i < files.length; i++) {
                    Log.d("Files", "FileName:" + files[i].getName());
                    if (files[i].getName().contains(fileType + vidhanNo)) {
                        selectedImageFile = files[i];
                        path = selectedImageFile.getAbsolutePath();
                        break;
                    }
                }
            }
        }
        return path;
    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg";
    }


    //-------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (permissions != null && permissions.length > 0) {
            setImage();
        }
    }

    private void setImage() {
        boolean isAttachSlip = getIntent().getExtras().getBoolean("isSlipAttach");
        String vidhanNo = getIntent().getExtras().getString("vidhansabha");
        if (isAttachSlip) {
            String path1 = Environment.getExternalStorageDirectory().toString() + File.separator + "slip" + File.separator;
            String path2 = Environment.getExternalStorageDirectory().toString() + File.separator + "Slip" + File.separator;
            String path3 = Environment.getExternalStorageDirectory().toString() + File.separator + "SLIP" + File.separator;

            File directory1 = new File(path1);
            File directory2 = new File(path2);
            File directory3 = new File(path3);
            File directory;
            if (directory1.exists()) {
                directory = directory1;
            } else if (directory2.exists()) {
                directory = directory2;
            } else {
                directory = directory3;
            }

            File selectedImageFile = null;
            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    Log.d("Files", "Size: " + files.length);
                    for (int i = 0; i < files.length; i++) {
                        Log.d("Files", "FileName:" + files[i].getName());
                        if (files[i].getName().equalsIgnoreCase(vidhanNo + ".jpg")) {
                            selectedImageFile = files[i];
                            break;
                        }
                    }
                }
            }
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bmp_slip = null;
            if (isAttachSlip) {
               // bmp_slip = BitmapFactory.decodeFile(selectedImageFile.getAbsolutePath(), bmOptions);
                //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
		         bmp_slip = BitmapFactory.decodeResource(getResources(), R.drawable.slip)
				.copy(Bitmap.Config.ARGB_8888, true);
                final Rect bounds = new Rect();
                TextPaint textPaint = new TextPaint() {
                    {
                        setColor(Color.BLACK);
                        setTextAlign(Align.LEFT);
                        setTextSize(28f);
                        setAntiAlias(true);

                    }
                };
                Spannable wordtoSpan = new SpannableString(Message_to_share);

                wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textPaint.getTextBounds(wordtoSpan.toString(), 0, Message_to_share.length(), bounds);
                StaticLayout mTextLayout = new StaticLayout(Html.fromHtml(Message_to_share), textPaint,
                        bounds.width(), Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

                final Bitmap bmp_text = Bitmap.createBitmap(bmp_slip.getWidth(), mTextLayout.getHeight(),
                        Bitmap.Config.ARGB_8888);
                bmp_text.eraseColor(Color.WHITE);// just adding black background
                final Canvas canvas = new Canvas(bmp_text);
                mTextLayout.draw(canvas);


                Bitmap result = Bitmap.createBitmap(bmp_slip.getWidth(), bmp_text.getHeight() + bmp_slip.getHeight(), bmp_slip.getConfig());
                Canvas canvas1 = new Canvas(result);
                canvas1.drawBitmap(bmp_slip, 0f, 0f, null);
                canvas1.drawBitmap(bmp_text, 0f, bmp_slip.getHeight(), null);

                iv.setImageBitmap(result);
                tempUri = getImageUri(Share_Preview.this, result);
            } else {
                final Rect bounds = new Rect();
                TextPaint textPaint = new TextPaint() {
                    {
                        setColor(Color.BLACK);
                        setTextAlign(Align.LEFT);
                        setTextSize(28f);
                        setAntiAlias(true);

                    }
                };
                Spannable wordtoSpan = new SpannableString(Message_to_share);

                wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textPaint.getTextBounds(wordtoSpan.toString(), 0, Message_to_share.length(), bounds);
                StaticLayout mTextLayout = new StaticLayout(Html.fromHtml(Message_to_share), textPaint,
                        bounds.width(), Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

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
                iv.setImageBitmap(bmp_text);
                tempUri = getImageUri(Share_Preview.this, bmp_text);
            }


        } else {
            final Rect bounds = new Rect();
            TextPaint textPaint = new TextPaint() {
                {
                    setColor(Color.BLACK);
                    setTextAlign(Align.LEFT);
                    setTextSize(28f);
                    setAntiAlias(true);
                }
            };


            Spannable wordtoSpan = new SpannableString(Message_to_share);

            wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textPaint.getTextBounds(wordtoSpan.toString(), 0, Message_to_share.length(), bounds);
            StaticLayout mTextLayout = new StaticLayout(Html.fromHtml(Message_to_share), textPaint,
                    bounds.width(), Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

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
            iv.setImageBitmap(bmp_text);
            tempUri = getImageUri(Share_Preview.this, bmp_text);
        }
    }

    class MySpeech implements TextToSpeech.OnInitListener {

        String tts, tempFile;
        HashMap<String, String> myHashRender;

        public MySpeech(String tts, HashMap<String, String> myHashRender, String tempFile) {
            this.tts = tts;
            this.myHashRender = myHashRender;
            this.tempFile = tempFile;
            mTts = new TextToSpeech(Share_Preview.this, this);
        }

        @Override
        public void onInit(int status) {
            Log.v("log", "initi");
            /*String[] splitspeech = tts.split("@");
            for (int j = 0; j < splitspeech.length; j++) {

                if (j == 0) { // Use for the first splited text to flush on audio stream

                    mTts.speak(splitspeech[j].toString().trim(),TextToSpeech.QUEUE_FLUSH, myHashRender);

                } else { // add the new test on previous then play the TTS

                    mTts.speak(splitspeech[j].toString().trim(), TextToSpeech.QUEUE_ADD,myHashRender);
                }

                mTts.playSilence(1000, TextToSpeech.QUEUE_ADD, null);
            }*/

            mTts.setSpeechRate((float) 0.3);

            int i = mTts.synthesizeToFile(tts, myHashRender, tempFile);

            if (i == TextToSpeech.SUCCESS) {
                System.out.println("Result : " + i);
            }

        }
    }

}