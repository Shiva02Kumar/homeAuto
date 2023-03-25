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
        public Long Temperature;
        public Long Pressure;
        public Long Power;
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
        DatabaseReference temp = database.getReference("PhysicalData");
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                PhysicalData data = dataSnapshot.getValue(PhysicalData.class);
                System.out.println("temp = " + data.Temperature + " " + data.Power + " " + data.Pressure);
                TextView T1, T2, T3;
                T1 = findViewById(R.id.textView5);
                T2 = findViewById(R.id.textView7);
                T3 = findViewById(R.id.textView8);
                T1.setText(data.Temperature.toString());
                T2.setText(data.Pressure.toString());
                T3.setText(data.Power.toString());
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
    }
}
