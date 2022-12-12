package voterSearch.app.SmdInfo;

import java.util.ArrayList;
import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Printer_Listing extends Activity{

	/** Called when the activity is first created. */
    ListView listViewPaired;
    ListView listViewDetected;
    
    ArrayList<String> arrayListpaired;
    
    Button buttonSearch,buttonOn,buttonDesc,buttonOff;
    
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> detectedAdapter;
    
    static HandleSeacrh handleSeacrh;
    
    BluetoothDevice bdDevice;
    BluetoothClass bdClass;
    
    ArrayList<BluetoothDevice> arrayListPairedBluetoothDevices;
    
    ListItemClickedonPaired listItemClickedonPaired;
    
    BluetoothAdapter bluetoothAdapter = null;
    
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    
    ListItemClicked listItemClicked;
    private ButtonClicked clicked;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printer_listing);
		
		listViewDetected = (ListView) findViewById(R.id.listViewDetected);
        listViewPaired = (ListView) findViewById(R.id.listViewPaired);
        
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonOn = (Button) findViewById(R.id.buttonOn);
        buttonDesc = (Button) findViewById(R.id.buttonDesc);
        buttonOff = (Button) findViewById(R.id.buttonOff); 
        
        arrayListpaired = new ArrayList<String>();
        
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        clicked = new ButtonClicked();
        handleSeacrh = new HandleSeacrh();
        
        arrayListPairedBluetoothDevices = new ArrayList<BluetoothDevice>();
        /*
         * the above declaration is just for getting the paired bluetooth devices;
         * this helps in the removing the bond between paired devices.
         */
        listItemClickedonPaired = new ListItemClickedonPaired();
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        adapter= new ArrayAdapter<String>(Printer_Listing.this, android.R.layout.simple_list_item_1, arrayListpaired);
        
        //--------------------------------------------------------------------
        detectedAdapter = new ArrayAdapter<String>(Printer_Listing.this, android.R.layout.simple_list_item_single_choice);
        listViewDetected.setAdapter(detectedAdapter);
        detectedAdapter.notifyDataSetChanged();
        //--------------------------------------------------------------------
        
        listItemClicked = new ListItemClicked();
        listViewPaired.setAdapter(adapter);
	}
	//-------------------------------------------------------------------------------------------------------------------------
	  @Override
	    protected void onStart() {
	        // TODO Auto-generated method stub
	        super.onStart();
	        getPairedDevices();
	        buttonOn.setOnClickListener(clicked);
	        buttonSearch.setOnClickListener(clicked);
	        buttonDesc.setOnClickListener(clicked);
	        buttonOff.setOnClickListener(clicked);
	        listViewDetected.setOnItemClickListener(listItemClicked);
	        listViewPaired.setOnItemClickListener(listItemClickedonPaired);
	    }
	//-------------------------------------------------------------------------------------------------------------------------
		private void getPairedDevices() {
			arrayListpaired.clear();
	        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();            
	        if(pairedDevice.size()>0)
	        {
	            for(BluetoothDevice device : pairedDevice)
	            {
	                //arrayListpaired.add(device.getName()+"\n"+device.getAddress());
	            	arrayListpaired.add(device.getName());
	                arrayListPairedBluetoothDevices.add(device);
	            }
	        }
	        
	        adapter.notifyDataSetChanged();
	    }
	    
	    //------- detected listView---------------------------------------------------------
	    class ListItemClicked implements OnItemClickListener
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            // TODO Auto-generated method stub
	        	
	            bdDevice = arrayListBluetoothDevices.get(position);
	            Intent i = new Intent(Printer_Listing.this, Printer_CIE_DYNO_4807.class);
	            i.putExtra("Printer_Add", bdDevice.getAddress());
	            startActivity(i);
	         }       
	    }
	    
	    //-----------paired listView-----------------------------------------------------------
	    class ListItemClickedonPaired implements OnItemClickListener
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	        	
	            bdDevice = arrayListPairedBluetoothDevices.get(position);
	         
	            Intent i = new Intent(Printer_Listing.this, Printer_CIE_DYNO_4807.class);
	            i.putExtra("Printer_Add", bdDevice.getAddress());
	            startActivity(i);
	        }
	    }

	    class ButtonClicked implements OnClickListener
	    {
	        @Override
	        public void onClick(View view) {
	            switch (view.getId()) {
	            //---------------------------------------------------------------------------------------------
	            case R.id.buttonOn:
	            	
	                onBluetooth();
	                getPairedDevices();
	               
	                 break;
	            //---------------------------------------------------------------------------------------------      
	            case R.id.buttonSearch:
	             
	            	if(!bluetoothAdapter.isEnabled())
	     	        {
	            		Toast.makeText(getApplicationContext(), "Bluetooth is not enabled.", Toast.LENGTH_SHORT).show();	            		
	            	}
	            	else
	            	{
	            		startSearching();	
	            	}
	            	 
	                break;
	                
	            //---------------------------------------------------------------------------------------------    
	            case R.id.buttonDesc:
	                makeDiscoverable();
	                break;
	                
	           //---------------------------------------------------------------------------------------------    
	            case R.id.buttonOff:
	                offBluetooth();
	                break;
	           //---------------------------------------------------------------------------------------------    
	            default:
	                break;
	            }
	        }
	    }
	  //-------------------------------------------------------------------------------------------------------------------------  
	    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	
	          //  Message msg = Message.obtain();
	            String action = intent.getAction();
	            
	            if(BluetoothDevice.ACTION_FOUND.equals(action))
	            {
	                Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

	                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        
	                if(arrayListBluetoothDevices.size()<1) // this checks if the size of bluetooth device is 0,then add the
	               {                                           // device to the arraylist.
	                	
	                   //detectedAdapter.add(device.getName()+"\n"+device.getAddress());
	                	detectedAdapter.add(device.getName());
	                    arrayListBluetoothDevices.add(device);
	                    detectedAdapter.notifyDataSetChanged();
	                }
	                else
	                {
	                    boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
	                    
	                    for(int i = 0; i<arrayListBluetoothDevices.size();i++)
	                    {
	                        if(device.getAddress().equals(arrayListBluetoothDevices.get(i).getAddress()))
	                        {
	                            flag = false;
	                        }
	                    }
	                    
	                    if(flag == true)
	                    {
	                        //detectedAdapter.add(device.getName()+"\n"+device.getAddress());
	                    	detectedAdapter.add(device.getName());
	                        arrayListBluetoothDevices.add(device);
	                        detectedAdapter.notifyDataSetChanged();
	                    }
	               }
	            }           
	        }
	    };
	    
	  //-------------------------------------------------------------------------------------------------------------------------    
	    private void startSearching() {
	        Log.i("Log", "in the start searching method");
	        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        Printer_Listing.this.registerReceiver(myReceiver, intentFilter);
	        bluetoothAdapter.startDiscovery();
	    }
	  //-------------------------------------------------------------------------------------------------------------------------
	    private void onBluetooth() {
	        if(!bluetoothAdapter.isEnabled())
	        {
	            bluetoothAdapter.enable();
	            
	            Log.i("Log", "Bluetooth is Enabled");
	        }
	    }
	  //-------------------------------------------------------------------------------------------------------------------------
	    private void offBluetooth() {
	        if(bluetoothAdapter.isEnabled())
	        {
	            bluetoothAdapter.disable();
	        }
	    }
	  //-------------------------------------------------------------------------------------------------------------------------
	    private void makeDiscoverable() {
	        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
	        startActivity(discoverableIntent);
	        Log.i("Log", "Discoverable ");
	    }
	  //-------------------------------------------------------------------------------------------------------------------------
	    class HandleSeacrh extends Handler
	    {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 111:

	                break;

	            default:
	                break;
	            }
	        }
	    }
	  //-------------------------------------------------------------------------------------------------------------------------   
		
}
