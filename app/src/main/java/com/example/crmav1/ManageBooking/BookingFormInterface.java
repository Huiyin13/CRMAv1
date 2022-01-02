package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageCar.CarListInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BookingFormInterface extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private ImageView time1, time2, date1, date2;
    private Button book, cancel, calculate, view;
    private TextView timeF, timeT, dateF, dateT, payment;

    boolean to = false, from = true;
    private FirebaseDatabase db;
    private DatabaseReference bookRef;
    private FirebaseUser user;

    private String ttlH, ttlPayment, bid, coId, cid, sId, fromTime, toTime, fromdatestring, todatestring, fromtimestring, totimestring, finalto, finalfrom, fee;
    private int totalHours, paymentF, counter = 0;
    private boolean finalisdateavailable, counterisdateavailable, finalcounterdateavailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form_interface);

        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        timeF = findViewById(R.id.timeF);
        timeT = findViewById(R.id.timeT);
        dateF = findViewById(R.id.dateF);
        dateT = findViewById(R.id.dateT);
        book = findViewById(R.id.book);
        view = findViewById(R.id.viewDate);
        calculate = findViewById(R.id.calculate);
        cancel = findViewById(R.id.cancel);
        payment = findViewById(R.id.payment);
//        testingcheckDate();

        coId = getIntent().getStringExtra("coId");
        cid = getIntent().getStringExtra("cid");
        fee = getIntent().getStringExtra("fee");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(BookingFormInterface.this, SViewBookingDateInterface.class);
                intent2view.putExtra("coId",coId);
                intent2view.putExtra("cid", cid);
                intent2view.putExtra("fee", fee);
                startActivity(intent2view);
            }
        });

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                timePickerDialog = new TimePickerDialog(BookingFormInterface.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String hourstring = String.valueOf(sHour);
                                String minutestring = String.valueOf(sMinute);
                                if (sMinute < 10)
                                {
                                    minutestring = "0" + sMinute;
                                }
                                if (sHour < 10)
                                {
                                    hourstring = "0" + sHour;
                                }
                                fromtimestring = hourstring + ":" + minutestring;
                                System.out.println(fromtimestring);
                                timeF.setText(fromtimestring);
                            }
                        }, hour, minutes, true);
                timePickerDialog.show();
            }
        });

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                timePickerDialog = new TimePickerDialog(BookingFormInterface.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String hourstring = String.valueOf(sHour);
                                String minutestring = String.valueOf(sMinute);
                                if (sMinute < 10)
                                {
                                    minutestring = "0" + sMinute;
                                }
                                if (sHour < 10)
                                {
                                    hourstring = "0" + sHour;
                                }
                                totimestring = hourstring + ":" + minutestring ;
                                System.out.println(totimestring);

                                timeT.setText(totimestring);
                            }
                        }, hour, minutes, true);

                timePickerDialog.show();
            }
        });

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from = true;
                to = false;
                showDateDialog();
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from = false;
                to = true;
                showDateDialog();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(BookingFormInterface.this, SCarDetailsInterface.class);
                intent2view.putExtra("coId", coId);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatabaseReference testdatereference = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking");
                testdatereference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        counter = 0;
//                        counterisdateavailable = true;
                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    Booking booking = dataSnapshot1.getValue(Booking.class);
                                    System.out.println("From " + booking.getDateF() + " to " + booking.getDateT());
                                    counterisdateavailable = checkDate(fromdatestring, todatestring, booking.getDateF(), booking.getDateT());
                                    System.out.println(counterisdateavailable + " counterisavailable");
                                    if (!counterisdateavailable)
                                    {
                                        counter ++;
                                    }

                                }
                            }
                            if (counter > 0)
                            {
                                counter = 0;
                                Toast.makeText(BookingFormInterface.this, "Date is Not Available", Toast.LENGTH_SHORT).show();
                                System.out.println("Cannot Book");
                                payment.setText("Invalid Date");
                            }
                            else
                            {
                                finalfrom = fromdatestring + " " + fromtimestring;
                                finalto = todatestring + " " + totimestring;
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                try {
                                    Date fromdate = simpleDateFormat.parse(finalfrom);
                                    Date todate = simpleDateFormat.parse(finalto);
                                    long difference_in_time = todate.getTime() - fromdate.getTime();
                                    long difference_in_seconds = (difference_in_time / 1000) % 60;
                                    long difference_in_minutes = (difference_in_time / (1000 * 60)) % 60;
                                    long difference_in_hours = (difference_in_time / (1000 * 60 * 60)) % 24;
                                    long difference_in_years = (difference_in_time / (10001 * 60 * 60 * 24 * 365));
                                    long difference_in_days = (difference_in_time / (1000 * 60 * 60 * 24)) % 365;
                                    System.out.println("Difference between two dates is: ");
                                    System.out.println(difference_in_years + " years, " + difference_in_days + " days, " + difference_in_hours + " hours, " + difference_in_minutes + " minutes, ");
                                    long finaltotalhours = difference_in_days * 24 + difference_in_hours + difference_in_minutes / 60;
                                    totalHours = (int) finaltotalhours;
                                    System.out.println(totalHours);
                                    paymentF = totalHours * Integer.parseInt(fee);
                                    payment.setText("RM " + paymentF + " / " + totalHours + " hour(s)");

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            finalfrom = fromdatestring + " " + fromtimestring;
                            finalto = todatestring + " " + totimestring;
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                            try {
                                Date fromdate = simpleDateFormat.parse(finalfrom);
                                Date todate = simpleDateFormat.parse(finalto);
                                long difference_in_time = todate.getTime() - fromdate.getTime();
                                long difference_in_seconds = (difference_in_time / 1000) % 60;
                                long difference_in_minutes = (difference_in_time / (1000 * 60)) % 60;
                                long difference_in_hours = (difference_in_time / (1000 * 60 * 60)) % 24;
                                long difference_in_years = (difference_in_time / (10001 * 60 * 60 * 24 * 365));
                                long difference_in_days = (difference_in_time / (1000 * 60 * 60 * 24)) % 365;
                                System.out.println("Difference between two dates is: ");
                                System.out.println(difference_in_years + " years, " + difference_in_days + " days, " + difference_in_hours + " hours, " + difference_in_minutes + " minutes, ");
                                long finaltotalhours = difference_in_days * 24 + difference_in_hours + difference_in_minutes / 60;
                                totalHours = (int) finaltotalhours;
                                System.out.println(totalHours);
                                paymentF = totalHours * Integer.parseInt(fee);
                                payment.setText("RM " + paymentF + " / " + totalHours + " hour(s)");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkDate();
//                if (finalisdateavailable){
                if (payment.getText().toString().equalsIgnoreCase("Invalid Date"))
                {
                    Toast.makeText(BookingFormInterface.this, "Please Select another Date", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    bookRef = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking");
//
//                    bookRef = FirebaseDatabase.getInstance().getReference("Booking");
                    DatabaseReference book = bookRef.child(user.getUid());
                    bid = book.push().getKey();
                    DatabaseReference booked = book.child(bid);


                    String bid2 = book.push().getKey();
                    DatabaseReference bookRef1 = FirebaseDatabase.getInstance().getReference("Booking").child(user.getUid()).child(bid);


                    fromTime = timeF.getText().toString().trim();
                    toTime = timeT.getText().toString().trim();
                    ttlH = String.valueOf(totalHours);
                    ttlPayment = String.valueOf(paymentF);

                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("bid", bid);
                    hashMap.put("cid", cid);
                    hashMap.put("coId", coId);
                    hashMap.put("sId", user.getUid());
                    hashMap.put("timeF", fromTime);
                    hashMap.put("timeT",fromTime);
                    hashMap.put("memo", "Waiting Car Owner Approve.");
                    hashMap.put("dateF", fromdatestring);
                    hashMap.put("dateT", todatestring);
                    hashMap.put("rentH", ttlH);
                    hashMap.put("ttlFee", ttlPayment);
                    hashMap.put("bStatus", "Applying");
                    hashMap.put("bRejectReason", " ");
                    hashMap.put("bCancelReason", " ");
                    hashMap.put("finalFrom", finalfrom);
                    hashMap.put("finalTo", finalto);

                    booked.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            bookRef1.setValue(hashMap);
                            Toast.makeText(BookingFormInterface.this,"Booking Form Sent.", Toast.LENGTH_SHORT).show();
                            Intent intent2book = new Intent(BookingFormInterface.this, BookingListInterface.class);
                            startActivity(intent2book);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookingFormInterface.this, "Failed to send.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }



//                }else{
//                    Toast.makeText(BookingFormInterface.this, "Selected Date Not Available", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.book:
                        startActivity(new Intent(getApplicationContext(), BookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(BookingFormInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(BookingFormInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    private boolean checkDate(String inputfrom, String inputto, String firebasefrom, String firebaseto) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            boolean isdateavailable = false;
            Date testfromdate = simpleDateFormat.parse(firebasefrom);
            Date testtodate = simpleDateFormat.parse(firebaseto);
            Date inputtestfromdate = simpleDateFormat.parse(inputfrom);
            Date inputesttodate = simpleDateFormat.parse(inputto);
            boolean isDateCrashed = checkisDateCrashed(testfromdate, testtodate, inputtestfromdate, inputesttodate);
            System.out.println(isDateCrashed + " isdatecrashed");
            if (isDateCrashed)
            {
                Toast.makeText(BookingFormInterface.this, "Selected Date Not Available", Toast.LENGTH_SHORT).show();
            }
            else
            {
                isdateavailable = checkIsDateAvailable(testfromdate, testtodate, inputtestfromdate, inputesttodate);
            }
            System.out.println(isdateavailable + " isdateavailable");
            if (!isDateCrashed && isdateavailable)
            {
                finalisdateavailable = true;
            }
            else
            {
                finalisdateavailable = false;
            }

            System.out.println(finalisdateavailable + " finalisdateavailable");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalisdateavailable;
    }

    private boolean checkIsDateAvailable(Date testfromdate, Date testtodate, Date inputtestfromdate, Date inputesttodate) {
        if (inputtestfromdate.before(testfromdate) && inputesttodate.after(testtodate))
        {
            return false;
        }
        else if (inputtestfromdate.after(testfromdate) && inputesttodate.before(testtodate))
        {
            return false;
        }
        else if (inputtestfromdate.after(testfromdate) && inputtestfromdate.before(testtodate))
        {
            return false;
        }
        else if (inputesttodate.after(testfromdate) && inputesttodate.before(testtodate))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean checkisDateCrashed(Date testfromdate, Date testtodate, Date inputtestfromdate, Date inputesttodate) {
        if (inputtestfromdate.equals(testfromdate) || inputesttodate.equals(testtodate))
        {
            return true;
        }
        else if (inputtestfromdate.equals(testtodate) || inputesttodate.equals(testfromdate))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void showDateDialog() {
        datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month +=1;
        String monthstring = "0" + String.valueOf(month);
        String daymonthstring = String.valueOf(dayOfMonth);
        if (dayOfMonth < 10)
        {
            daymonthstring = "0" + daymonthstring;
        }
        String datestring = daymonthstring + "-" + monthstring + "-" + year;
        System.out.println(datestring);
        if (from)
        {
            fromdatestring = datestring;
            dateF.setText(fromdatestring);
        }
        if (to)
        {
            todatestring = datestring;
            dateT.setText(todatestring);
        }
    }
}