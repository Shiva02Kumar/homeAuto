package com.example.rghover;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Room extends AppCompatActivity {

    ToggleButton button1, button2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("toggleData");
    String Control = "0000";
    protected void savedata() {
        myRef.setValue(Control);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        DatabaseReference temp = database.getReference("PhysicalData");
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                MainActivity.PhysicalData data = dataSnapshot.getValue(MainActivity.PhysicalData.class);
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

        int room = getIntent().getIntExtra("roomnum", 0);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Control = dataSnapshot.getValue().toString();
                System.out.println(Control);
                Log.d(TAG, "ret is: " + Control);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        button1 = findViewById(R.id.toggleButton);
        button2 = findViewById((R.id.toggleButton2));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button1.isChecked()) {
//                    updateData();
                    Control = Control.substring(0, 2*(room-1)) + '1' + Control.substring(2*(room-1)+1);
//                    System.out.println(Control);
                    savedata();
                } else {
//                    updateData();
                    Control = Control.substring(0, 2*(room-1)) + '0' + Control.substring(2*(room-1)+1);
//                    System.out.println(Control);
                    savedata();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button2.isChecked()) {
                    Control = Control.substring(0, 2*(room-1)+1) + '1' + Control.substring(2*(room-1)+2);
//                    System.out.println(Control);
                    savedata();
                } else {
                    Control = Control.substring(0, 2*(room-1)+1) + '0' + Control.substring(2*(room-1)+2);
//                    System.out.println(Control);
                    savedata();
                }
            }
        });
    }
}