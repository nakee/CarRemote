package carmotes.org.open.carremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zerokol.views.JoystickView;

import java.io.InputStream;
import java.io.OutputStreamWriter;

public class ControlRobot extends AppCompatActivity {

    private TextView directionTextView;
    // Importing as others views
    private JoystickView joystick;


    private BluetoothSocket socket;
    private OutputStreamWriter os;
    private InputStream is;
    private BluetoothDevice remoteDevice;
    private BroadcastReceiver discoveryResult = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            android.util.Log.e("TrackingFlow", "WWWTTTFFF");
            unregisterReceiver(this);
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
           // new Thread(reader).start();
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    protected BluetoothAdapter BA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_robot);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        BA = BluetoothAdapter.getDefaultAdapter();

        if (! BA.enable()) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (! BA.enable()) {
                directionTextView.setText(R.string.errorNoBT);
            }
            startActivity(i);
        }

        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        if(BA.isDiscovering()){
            BA.cancelDiscovery();
        }
        BA.startDiscovery();
        // referring as others views
        joystick = (JoystickView) findViewById(R.id.joystickView);

        // Listener of events, it'll return the angle in graus and power in percents
        // return to the direction of the moviment
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                //      angleTextView.setText(" " + String.valueOf(angle) + "°");
                //      powerTextView.setText(" " + String.valueOf(power) + "%");
                switch (direction) {
                    case JoystickView.FRONT:
                        directionTextView.setText(R.string.front);
                        break;

                    case JoystickView.FRONT_RIGHT:
                        directionTextView.setText(R.string.front_right);
                        break;

                    case JoystickView.RIGHT:
                        directionTextView.setText(R.string.right);
                        break;

                    case JoystickView.RIGHT_BOTTOM:
                        directionTextView.setText(R.string.right_bottom);
                        break;

                    case JoystickView.BOTTOM:
                        directionTextView.setText(R.string.bottom);
                        break;

                    case JoystickView.BOTTOM_LEFT:
                        directionTextView.setText(R.string.bottom_left);
                        break;

                    case JoystickView.LEFT:
                        directionTextView.setText(R.string.left);
                        break;

                    case JoystickView.LEFT_FRONT:
                        directionTextView.setText(R.string.left_front);
                        break;

                    default:
                        directionTextView.setText(R.string.center);
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

    }

    public void find_devices(View view) {
        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{unregisterReceiver(discoveryResult);}catch(Exception e){e.printStackTrace();}
        if(socket != null){
            try{
                is.close();
                os.close();
                socket.close();
            }catch(Exception e){}
        }
    }
}
