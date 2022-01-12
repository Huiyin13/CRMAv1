package com.example.crmav1.ManagePayment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageBooking.BookingFormInterface;
import com.example.crmav1.ManageBooking.BookingListInterface;
import com.example.crmav1.ManageBooking.SBookingDetailsInterface;
import com.example.crmav1.ManageLoginandRegistration.EmailVerificationInterface;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentSelectionInterface extends AppCompatActivity {

    private TextView amount;
    private RadioButton cash, card;
    private Button payment;

    ProgressDialog progressDialog;
    private CardInputWidget cardInputWidget;
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private OkHttpClient httpClient;
    private static final String BACKEND_URL = "https://crma-v01.herokuapp.com/";
    Double amount1 = null;

    private String bid, fee, coId, cid;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection_interface);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Paying");
        amount = findViewById(R.id.amount);
        cash = findViewById(R.id.cash);
        card = findViewById(R.id.card);
        payment = findViewById(R.id.pay);
        cardInputWidget = findViewById(R.id.stripe);
        stripe =new Stripe(getApplicationContext(), Objects.requireNonNull("pk_test_51J2GkAISOyY8mOZzW6eUAoHIrRnOyojBM6YbqQB7y7sOPRLuchBCm7yMNPYo1RQaeP0GWMiluEEq9Wp3G8ZJS1KZ0061elItLv"));
        httpClient = new OkHttpClient();

        user = FirebaseAuth.getInstance().getCurrentUser();
        bid = getIntent().getStringExtra("bid");
        fee = getIntent().getStringExtra("fee");
        coId = getIntent().getStringExtra("coId");
        cid = getIntent().getStringExtra("cid");
        amount.setText("Your Total Payment is RM "+ fee );

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardInputWidget.setVisibility(View.VISIBLE);
            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardInputWidget.setVisibility(View.GONE);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cash.isChecked()){
                    DatabaseReference cashref = FirebaseDatabase.getInstance().getReference("Payment");
                    String pid = cashref.push().getKey();
                    DatabaseReference pay = cashref.child(pid);

                    DatabaseReference updateB = FirebaseDatabase.getInstance().getReference("Booking").child(user.getUid()).child(bid);
                    DatabaseReference updateB2 = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking").child(user.getUid()).child(bid);
                    updateB.child("bStatus").setValue("Not yet receive cash payment.");
                    updateB2.child("bStatus").setValue("Not yet receive cash payment.");

                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("pid", pid);
                    hashMap.put("paymentStatus", "NOT YET PAY");
                    hashMap.put("bid", bid);
                    hashMap.put("paymentType", "Cash");

                    pay.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(PaymentSelectionInterface.this,"Updated", Toast.LENGTH_SHORT).show();
                            Intent intent2back = new Intent(PaymentSelectionInterface.this, SBookingDetailsInterface.class);
                            intent2back.putExtra("coId", coId);
                            intent2back.putExtra("bid", bid);
                            startActivity(intent2back);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PaymentSelectionInterface.this, "Failed to update.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    paybyCard();
                }
            }
        });


    }

    private void paybyCard() {
        progressDialog.show();
        amount1 = Double.parseDouble(fee);
        startCheckOut();
    }

    private void startCheckOut() {
        {
            // Create a PaymentIntent by calling the server's endpoint.
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        String json = "{"
//                + "\"currency\":\"myr\","
//                + "\"items\":["
//                + "{\"id\":\"photo_subscription\"}"
//                + "]"
//                + "}";
            amount1 = amount1*100;
            Map<String,Object> payMap=new HashMap<>();
            Map<String,Object> itemMap=new HashMap<>();
            List<Map<String,Object>> itemList =new ArrayList<>();
            payMap.put("currency","myr");
            itemMap.put("id","photo_subscription");
            itemMap.put("amount",amount1);
            itemList.add(itemMap);
            payMap.put("items",itemList);
            String json = new Gson().toJson(payMap);
            RequestBody body = RequestBody.create(json, mediaType);
            Request request = new Request.Builder()
                    .url(BACKEND_URL + "create-payment-intent")
                    .post(body)
                    .build();
            httpClient.newCall(request)
                    .enqueue(new PayCallback(this));

        }
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<PaymentSelectionInterface> activityRef;
        PayCallback(@NonNull PaymentSelectionInterface activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

            final PaymentSelectionInterface activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final PaymentSelectionInterface activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
        progressDialog.show();
        //once you get the payment client secret start transaction
        //get card detail
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null) {
            //now use paymentIntentClientSecret to start transaction
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
            //start payment
            stripe.confirmPayment(PaymentSelectionInterface.this, confirmParams);
        }
        Log.i("TAG", "onPaymentSuccess: "+paymentIntentClientSecret);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<PaymentSelectionInterface> activityRef;
        PaymentResultCallback(@NonNull PaymentSelectionInterface activity) {
            activityRef = new WeakReference<>(activity);
        }
        //If Payment is successful
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {

            final PaymentSelectionInterface activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast toast =Toast.makeText(activity, "Paid Successful", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                DatabaseReference updateB = FirebaseDatabase.getInstance().getReference("Booking").child(user.getUid()).child(bid);
                DatabaseReference updateB2 = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking").child(user.getUid()).child(bid);
                updateB.child("bStatus").setValue("Paid");
                updateB2.child("bStatus").setValue("Paid");

                DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("Payment");
                String pid = cardRef.push().getKey();
                DatabaseReference cardRef2 = cardRef.child(pid);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("pid", pid);
                hashMap.put("paymentStatus", "Paid with Card");
                hashMap.put("bid", bid);
                hashMap.put("paymentType", "Credit Card/Debit Card");

                cardRef2.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(PaymentSelectionInterface.this, "Paid", Toast.LENGTH_SHORT).show();
                        Intent intent2back = new Intent(PaymentSelectionInterface.this, SBookingDetailsInterface.class);
                        intent2back.putExtra("coId", coId);
                        intent2back.putExtra("bid", bid);
                        startActivity(intent2back);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentSelectionInterface.this, "Failed to paid.", Toast.LENGTH_SHORT).show();
                    }
                });
                System.out.println(paymentIntent.getId());
//                activity.displayAlert(
//                        "Payment completed",
//                        gson.toJson(paymentIntent)
//                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        //If Payment is not successful
        @Override
        public void onError(@NonNull Exception e) {
            progressDialog.show();
            final PaymentSelectionInterface activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
}