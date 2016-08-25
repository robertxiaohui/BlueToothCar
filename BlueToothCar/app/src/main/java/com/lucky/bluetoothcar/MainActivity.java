package com.lucky.bluetoothcar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @InjectView(R.id.mTvUp)
    TextView mTvUp;
    @InjectView(R.id.mTvLeft)
    TextView mTvLeft;
    @InjectView(R.id.mTvRight)
    TextView mTvRight;
    @InjectView(R.id.mTvDown)
    TextView mTvDown;
    @InjectView(R.id.mTvScan)
    TextView mTvScan;
    @InjectView(R.id.mTvStop)
    TextView mTvStop;

    BluetoothSPP bluetoothSPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        bluetoothSPP = new BluetoothSPP(this);
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        mTvUp.setOnTouchListener(this);
        mTvDown.setOnTouchListener(this);
        mTvLeft.setOnTouchListener(this);
        mTvRight.setOnTouchListener(this);

       // initHandler();

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //   @OnClick({R.id.mTvUp, R.id.mTvLeft, R.id.mTvRight, R.id.mTvDown, R.id.mTvScan,R.id.mTvStop})
    @OnClick({R.id.mTvScan, R.id.mTvStop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvScan:
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                break;
            case R.id.mTvStop:
                bluetoothSPP.send("s", true);
                startTouch = false;
                break;
         /*   case R.id.mTvUp:
                bluetoothSPP.send("u", true);
                break;
            case R.id.mTvLeft:
                bluetoothSPP.send("l", true);
                break;
            case R.id.mTvRight:
                bluetoothSPP.send("r", true);
                break;
            case R.id.mTvDown:
                bluetoothSPP.send("d", true);
                break;
            case R.id.mTvStop:
                bluetoothSPP.send("s", true);
                break;*/
        }
    }

    boolean startTouch = false;



    public void onDestroy() {
        super.onDestroy();
        bluetoothSPP.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!bluetoothSPP.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bluetoothSPP.isServiceAvailable()) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetoothSPP.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
//                setup();
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    case R.id.mTvUp:
                        bluetoothSPP.send("u", true);
                        break;
                    case R.id.mTvLeft:
                        bluetoothSPP.send("l", true);
                        break;
                    case R.id.mTvRight:
                        bluetoothSPP.send("r", true);
                        break;
                    case R.id.mTvDown:
                        bluetoothSPP.send("d", true);
                        break;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                bluetoothSPP.send("s", true);
                break;
        }
        return true;
    }

    public void sendOrder(final View v) {
        final Thread  thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (startTouch) {
                    switch (v.getId()) {
                        case R.id.mTvUp:
                            bluetoothSPP.send("u", true);
                            break;
                        case R.id.mTvLeft:
                            bluetoothSPP.send("l", true);
                            break;
                        case R.id.mTvRight:
                            bluetoothSPP.send("r", true);
                            break;
                        case R.id.mTvDown:
                            bluetoothSPP.send("d", true);
                            break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
