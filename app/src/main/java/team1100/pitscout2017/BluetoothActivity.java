package team1100.pitscout2017;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class BluetoothActivity extends AppCompatActivity {


    private static final UUID MY_UUID = UUID.fromString("ab8c5e79-47e6-42c9-a4c2-6b3168aa3f9f");

    private ConnectThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra(TeamList.EXTRA_BLUE_DATA);
        thread = new ConnectThread(getDevice(), data);
        thread.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        thread.cancel();
    }
    @Override
    public void onStop(){
        super.onStop();
        thread.cancel();
    }


    private BluetoothDevice getDevice(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                for(ParcelUuid u : device.getUuids()){
                    System.out.println(deviceName);
                    System.out.println(u.getUuid().toString());
                    if(deviceName.equals("1100-PC")){
                        return device;
                    }
                }
            }
        }
        return null;
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final String[] data;

        public ConnectThread(BluetoothDevice device, String[] data) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
            this.data = data;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (Exception e) {
                Snackbar.make(findViewById(android.R.id.content),"Connection failed on "+MY_UUID.toString(), Snackbar.LENGTH_LONG).show();
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                String packet = "";
                for(String d : data){
                    if(d!=null){
                        packet+=d + "\n";
                    }
                }
                mmSocket.getOutputStream().write(packet.getBytes());
                //setDisplayText("Data Sent.");
            } catch (Exception connectException) {
                System.out.println("SERVER CONNECTION FAILED");
                /*// Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (Exception closeException) {
                    //Log.e(TAG, "Could not close the client socket", closeException);
                }*/
                return;
            }
            //cancel();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {

            }
        }
    }
}