package com.example.startend;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    SensorManager mSM;

    Sensor RotationVector; //
    Sensor Accelerometer;    //
    Sensor LinearAccelerometer;    //

    TextView messageView;
    TextView mText;
    TextView linearText;

    boolean now = true;

    Button bt_start;
   // Button bt_stop;
    Button bt_delete;

    float[] orientation = new float[3];
    float[] mRotationMatrix = new float[9];

    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Acc");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageView = (TextView)findViewById(R.id.text_message);
        mText = (TextView)findViewById(R.id.textView);
        linearText = (TextView)findViewById(R.id.linearTextView);

        mSM = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        RotationVector = mSM.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        Accelerometer = mSM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        LinearAccelerometer = mSM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        bt_start = (Button) findViewById(R.id.start);
        bt_delete = (Button) findViewById(R.id.delete);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(now){
                    now = false;
                    bt_start.setText("stop");

                    if(RotationVector != null){
                        mSM.registerListener(mListener, RotationVector, SensorManager.SENSOR_DELAY_GAME);
                    }else{
                        messageView.setText("device doesn't have TYPE_ROTATION_VECTOR");
                    }

                    if(Accelerometer != null){
                        mSM.registerListener(mListener, Accelerometer, SensorManager.SENSOR_DELAY_GAME);
                    }else{
                        mText.setText("device doesn't have TYPE_ACCELEROMETER");
                    }

                    if(LinearAccelerometer != null){
                        mSM.registerListener(mListener, LinearAccelerometer, SensorManager.SENSOR_DELAY_GAME);
                    }else{
                        linearText.setText("device doesn't have TYPE_LINEAR_ACCELERATION");
                    }

                }else{
                    now = true;
                    bt_start.setText("start");
                    if(RotationVector != null || Accelerometer != null || LinearAccelerometer != null){
                        mSM.unregisterListener(mListener);
                    }
                }

            }
        });


    }

/*   @Override
    protected void onStart() {
        super.onStart();
        if(RotationVector != null){
            mSM.registerListener(mListener, RotationVector, SensorManager.SENSOR_DELAY_GAME);
        }else{
            messageView.setText("device doesn't have TYPE_ROTATION_VECTOR");
        }

        if(Accelerometer != null){
            mSM.registerListener(mListener, Accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }else{
            mText.setText("device doesn't have TYPE_ACCELEROMETER");
        }

        if(LinearAccelerometer != null){
            mSM.registerListener(mListener, LinearAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }else{
            linearText.setText("device doesn't have TYPE_LINEAR_ACCELERATION");
        }

    } */

    @Override
    protected void onStop() {
        super.onStop();
        if(RotationVector != null || Accelerometer != null || LinearAccelerometer != null){
            mSM.unregisterListener(mListener);
        }
    }

    SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch(event.sensor.getType()){
                case Sensor.TYPE_ROTATION_VECTOR:
                    SensorManager.getRotationMatrixFromVector(mRotationMatrix,event.values);
                    SensorManager.getOrientation(mRotationMatrix,orientation);
                    messageView.setText("RotationVector\n\nazimuth: "+orientation[0] + "\npitch: "+orientation[1]
                            + "\nroll: "+ orientation[2]);

                case Sensor.TYPE_ACCELEROMETER:
                    mText.setText("Accelerometer\n\nx : " + event.values[0]
                            + "\ny: " + event.values[1] + "\nz: " + event.values[2]);
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    linearText.setText("LinearAccelerometer\n\nx: " + event.values[0]
                            + "\ny: " + event.values[1] + "\nz: " + event.values[2]);
                    break;

            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}// MainActivity class
