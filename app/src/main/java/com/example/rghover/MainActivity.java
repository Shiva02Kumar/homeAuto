package com.example.rghover;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button room1, room2;
    ToggleButton buzzer;


    public static class PhysicalData {
        public Long c;
        public Long v;
//        public Long Power;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notiChannnel", "securitychannel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channel to detect movements");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notiChannel")
                .setSmallIcon(R.drawable.baseline_message)
                .setContentTitle("Movement Detected")
                .setContentText("Something is moving")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        room1 = findViewById(R.id.button1);
        room2 = findViewById(R.id.button2);
        buzzer = findViewById(R.id.toggleButton6);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference temp = database.getReference("homeAuto/recieve");
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                PhysicalData data = dataSnapshot.getValue(PhysicalData.class);
                System.out.println("temp = " + data.c + " " + data.v);
                TextView T1, T2, T3;
                T1 = findViewById(R.id.textView5);
                T2 = findViewById(R.id.textView7);
//                T3 = findViewById(R.id.textView8);
                T1.setText(data.c.toString() + " C");
                T2.setText(data.v.toString() + " Pa");
//                T3.setText(data.Power.toString());
                Log.d(TAG, "ret is: ");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Room.class);
                intent.putExtra("roomnum", 1);
                startActivity(intent);
            }
        });

        room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Room.class);
                intent.putExtra("roomnum", 2);
                startActivity(intent);
            }
        });


        buzzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buzzer.isChecked()) {
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Pir");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String Pir = dataSnapshot.getValue().toString();
                            System.out.println("temp = " + Pir);
                            Log.d(TAG, "ret is: " + Pir);
                            if (Pir.equals("1")) {
                                notificationManager.notify(1, builder.build());
                                System.out.println("sent");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
            }
        });

        ImageView door = findViewById(R.id.imageView2);
        door.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(MainActivity.this, GateActivity.class);
                                        startActivity(intent);
                                    }
                                }
        );}

//        room4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("Pir");
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        String Pir = dataSnapshot.getValue().toString();
//                        System.out.println("temp = " + Pir);
//                        Log.d(TAG, "ret is: " + Pir);
//                        if (Pir == "1") {
//                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "BuzzerNoti")
//                                    .setSmallIcon(R.drawable.hoverrr)
//                                    .setContentTitle("Movement Detected")
//                                    .setContentText("Something is moving")
//                                    .setAutoCancel(true)
//                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
//
//                            notificationManager.notify(1, builder.build());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Log.w(TAG, "Failed to read value.", error.toException());
//                    }
//                });
//            }
//        });
//        button1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                    Control = '1' + Control.substring(1);
//                    System.out.println(Control);
//                }
//                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//                    Control = '0' + Control.substring(1);
//                    System.out.println(Control);
//                }
//                return false;
//            }
//        });


//        button1 = findViewById(R.id.toggleButton);
//        button2 = findViewById(R.id.toggleButton2);
//        button3 = findViewById(R.id.toggleButton3);
//        button4 = findViewById(R.id.toggleButton4);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (button1.isChecked()) {
//                    Control = Control.substring(0, 0) + '1' + Control.substring(1);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 0) + '0' + Control.substring(1);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (button2.isChecked()) {
//                    Control = Control.substring(0, 1) + '1' + Control.substring(2);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 1) + '0' + Control.substring(2);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (button3.isChecked()) {
//                    Control = Control.substring(0, 2) + '1' + Control.substring(3);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 2) + '0' + Control.substring(3);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (button4.isChecked()) {
//                    Control = Control.substring(0, 3) + '1' + Control.substring(4);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 3) + '0' + Control.substring(4);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });

//        switch1 = findViewById(R.id.switch2);
//        switch2 = findViewById(R.id.switch3);
//        switch3 = findViewById(R.id.switch4);
//        switch4 = findViewById(R.id.switch5);
//
//        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Control = Control.substring(0, 0) + '1' + Control.substring(1);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 0) + '0' + Control.substring(1);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });
//        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Control = Control.substring(0, 1) + '1' + Control.substring(2);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 1) + '0' + Control.substring(2);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });
//        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Control = Control.substring(0, 2) + '1' + Control.substring(3);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 2) + '0' + Control.substring(3);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });
//        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Control = Control.substring(0, 3) + '1' + Control.substring(4);
//                    System.out.println(Control);
//                    savedata();
//                } else {
//                    Control = Control.substring(0, 3) + '0' + Control.substring(4);
//                    System.out.println(Control);
//                    savedata();
//                }
//            }
//        });


    }
}
