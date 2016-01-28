package com.m2dl.ter.term2dlcarretierlodovicimalet;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by CCA3441 on 26/01/2016.
 */
public class FragmentLayout extends Fragment implements View.OnClickListener {

    private View view;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<String> deviceName;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private AcceptThread mSecureAcceptThread;
    private ConnectThread mConnectThread;
    public ConnectedThread mConnectedThread;
    private BroadcastReceiver mReceiver = null;
    private ListView listDevice;
    private TextView text;
    private List<BluetoothDevice> callableDevices = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_layout,container,false);

        listDevice = (ListView) view.findViewById(R.id.listdevice);
        deviceName = new ArrayList<>();

        text = (TextView) view.findViewById(R.id.textView);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Device déjà connus
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                callableDevices.add(device);
                deviceName.add(device.getAddress());
                printAvailableDevice();
            }
        }

        Button b1 = (Button) view.findViewById(R.id.b1);
        b1.setOnClickListener(this);
        Button bServer = (Button) view.findViewById(R.id.bServer);
        bServer.setOnClickListener(this);
        Button bClient = (Button) view.findViewById(R.id.bClient);
        bClient.setOnClickListener(this);
        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String device = (String) listDevice.getItemAtPosition(position);
                BluetoothDevice selectedDevice = null;
                for (BluetoothDevice bluetoothDevice : callableDevices) {
                    if (bluetoothDevice.getAddress().equals(device)) {
                        selectedDevice = bluetoothDevice;
                        break;
                    }
                }
                if (selectedDevice != null) {
                    Toast.makeText(FragmentLayout.this.getActivity(), "j'ai clicker sur " + selectedDevice.getName(), Toast.LENGTH_LONG).show();
                    mConnectThread = new ConnectThread(selectedDevice);
                    mConnectThread.start();
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.b1 :
                searchNewDevice();
                break;
            case R.id.bServer :
//                mConnectThread = new ConnectThread();
                break;
            case R.id.bClient :
                visible();
                mSecureAcceptThread = new AcceptThread();
                mSecureAcceptThread.start();
                break;
        }
    }

    public void searchNewDevice() {
        mBluetoothAdapter.startDiscovery();
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    callableDevices.add(device);
                    deviceName.add(device.getAddress());
                    printAvailableDevice();
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
    }

    private void printAvailableDevice() {
        final ArrayAdapter<String> adapter = new ArrayAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, deviceName);
        listDevice.setAdapter(adapter);
    }

    public void visible() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        startActivity(discoverableIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.cancelDiscovery();
        getActivity().unregisterReceiver(mReceiver);
    }

    public void manageConnectedSocket(BluetoothSocket socket) {
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        mConnectedThread.write(new byte[] {1, 2, 3, 4});
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Server", MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                    Toast.makeText(FragmentLayout.this.getActivity(), "je lance accept()", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    Toast.makeText(FragmentLayout.this.getActivity(), "j'ai le socket ! je vais ecrire", Toast.LENGTH_LONG).show();
                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            Toast.makeText(FragmentLayout.this.getActivity(), "Build connectThread", Toast.LENGTH_LONG).show();

            try {
                if (mmDevice != null) {
                    //Log.i("Device UUID: ", String.valueOf(mmDevice.getUuids()[0].getUuid()));
                    tmp = device.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
                    Toast.makeText(FragmentLayout.this.getActivity(), String.valueOf(mmDevice.getUuids()[0].getUuid()), Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException e) {
                //Log.d("DEFAULT_UUID", " UUID from device is null, Using Default UUID, Device name: " + device.getName());
                Toast.makeText(FragmentLayout.this.getActivity(), "null to createRfComm", Toast.LENGTH_LONG).show();
                try {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            // Toast.makeText(FragmentLayout.this.getActivity(), "je start le connectThraed", Toast.LENGTH_LONG).show();
            Log.e("test", "startRun");
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                //text.append("je tente le connect connectThraed");
                Log.e("test", "connect()");
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.e("test", "pas de connect connectThraed:" + connectException.getMessage());
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
            Log.e("test", "je vais lancer le thread pour ecouter");
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            Log.e("test", "je vais read les byrtes");
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    Log.e("test", "j'ai lu: " + Integer.toString(bytes));
                    // Send the obtained bytes to the UI activity
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                    //        .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                Log.e("test", "j'ai ecrire");

            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
