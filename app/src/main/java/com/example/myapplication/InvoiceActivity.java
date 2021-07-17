package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InvoiceActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView employeeIDText;
    private TextView invoiceNumText;
    private TextView date;
    private EditText leftComments;
    private EditText leftEstimate;
    private EditText topComments;
    private EditText topEstimate;
    private EditText rightComments;
    private EditText rightEstimate;
    private EditText noseComments;
    private EditText noseEstimate;
    private EditText rearComments;
    private EditText rearEstimate;
    private EditText carStockNum;
    private EditText carVinNum;
    private EditText carYear;
    private EditText carColor;
    private EditText carMake;
    private EditText careModel;
    private EditText totalEstimate;
    private EditText totalTax;
    private EditText total;
    private EditText dealershipName;
    private EditText dealershipEmail;
    private EditText authorization;
    private boolean swapped;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String today;
    private String id;
    private String invoiceNum;
    private float x1,x2;
    static final int MIN_DISTANCE = 250;
    private ImageView car;
    private ImageView suv;
    private ImageView truck;
    private ImageView van;
    private List<ImageView> vehicles;
    private SearchView search;
    private int vehicle;
    private String result;
    private EmailData form;

    public static String EXTRA_FORM = "com.example.myapplication.EXTRA_FORM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        form = new EmailData();
        swapped = false;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        employeeIDText = (TextView) findViewById(R.id.riotIDText);
        invoiceNumText = (TextView) findViewById(R.id.invoiceNum);

        car = (ImageView) findViewById(R.id.carImage);
        suv = (ImageView) findViewById(R.id.suvImage);
        truck = (ImageView) findViewById(R.id.truckImage);
        van = (ImageView) findViewById(R.id.vanImage);

        vehicles = new ArrayList<ImageView>();

        vehicles.add(car);
        vehicles.add(suv);
        vehicles.add(truck);
        vehicles.add(van);

        vehicle = 0;

        leftComments = (EditText) findViewById(R.id.leftSideCommentsEditText);
        leftEstimate = (EditText) findViewById(R.id.leftSideEstimateEditText);
        topComments = (EditText) findViewById(R.id.topCommentsEditText);
        topEstimate = (EditText) findViewById(R.id.topEstimateEditText);
        rightComments = (EditText) findViewById(R.id.rightSideCommentsEditText);
        rightEstimate = (EditText) findViewById(R.id.rightSideEstimateEditText);
        noseComments = (EditText) findViewById(R.id.noseCommentsEditText);
        noseEstimate = (EditText) findViewById(R.id.noseEstimateEditText);
        rearComments = (EditText) findViewById(R.id.rearCommentsEditText);
        rearEstimate = (EditText) findViewById(R.id.rearEstimateEditText);
        carStockNum = (EditText) findViewById(R.id.stockEditText);
        carVinNum = (EditText) findViewById(R.id.vinEditText);
        carYear = (EditText) findViewById(R.id.yearEditText);
        carColor = (EditText) findViewById(R.id.colorEditText);
        carMake = (EditText) findViewById(R.id.makeEditText);
        careModel = (EditText) findViewById(R.id.modelEditText);
        totalEstimate = (EditText) findViewById(R.id.totalEstimateEditText);
        totalTax = (EditText) findViewById(R.id.taxEditText);
        total = (EditText) findViewById(R.id.totalEditText);
        dealershipName = (EditText) findViewById(R.id.dealershipNameEditText);
        dealershipEmail = (EditText) findViewById(R.id.dealershipEmailEditText);
        authorization = (EditText) findViewById(R.id.authorizationEditText);

        date = (TextView) findViewById(R.id.dateTextView);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        today = dateFormat.format(calendar.getTime());
        date.setText("Date: " + today);

        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("id").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    id = String.valueOf(task.getResult().getValue());
                    employeeIDText.setText("Riot Employee ID#: " + id);
                }
            }
        });

        getInvoiceNumber();


    }

    public void getInvoiceNumber() {
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invoice").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if ((String.valueOf(task.getResult().getValue())) != null) {
                        invoiceNum = String.valueOf(task.getResult().getValue());
                        long temp = Long.parseLong(invoiceNum);
                        temp++;
                        invoiceNum = String.valueOf(temp);
                        invoiceNumText.setText("Invoice #: " + invoiceNum);
                    }
                }
            }
        });
    }

    public void submit(View view) {
        EmailData email = new EmailData(leftComments.getText().toString(), leftEstimate.getText().toString(), topComments.getText().toString(), topEstimate.getText().toString(), rightComments.getText().toString(), rightEstimate.getText().toString(), noseComments.getText().toString(), noseEstimate.getText().toString(), rearComments.getText().toString(), rearEstimate.getText().toString(), carStockNum.getText().toString(), carVinNum.getText().toString(), carYear.getText().toString(), carColor.getText().toString(), carMake.getText().toString(), careModel.getText().toString(), totalEstimate.getText().toString(), totalTax.getText().toString(), total.getText().toString(),dealershipName.getText().toString(), dealershipEmail.getText().toString(), authorization.getText().toString(), invoiceNum, id, today);

        if (email.dealershipEmail.isEmpty()) {
            dealershipEmail.setError("Email address is required");
            dealershipEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.dealershipEmail).matches()) {
            dealershipEmail.setError("A valid email address is required");
            dealershipEmail.requestFocus();
            return;
        }

        String message = "Riot Dents\n" +
                today + "\n" +
                "Vehicle Records: \n" +
                "Stock #: " + email.carStockNum + "\n" +
                "Vin #: " + email.carVinNum + "\n" +
                "Year: " + email.carYear + "\n" +
                "Color: " + email.carColor + "\n" +
                "Make: " + email.carMake + "\n" +
                "Model: " + email.carModel + "\n" +
                "\nBilling and Damages to Vehicle\n" +
                "Left Side\n" +
                "Description of damages: " + email.leftSideComments + "\n" +
                "Estimated Cost: $" + email.leftSideEstimate + "\n" +
                "Right Side\n" +
                "Description of damages: " + email.rightSideComments + "\n" +
                "Estimated Cost: $" + email.rightSideEstimate + "\n" +
                "Top of Vehicle\n" +
                "Description of damages: " + email.topComments + "\n" +
                "Estimated Cost: $" + email.topEstimate + "\n" +
                "Nose of Vehicle\n" +
                "Description of damages: " + email.noseComments + "\n" +
                "Estimated Cost: $" + email.noseEstimate + "\n" +
                "Rear of Vehicle\n" +
                "Description of damages: " + email.rearComments + "\n" +
                "Estimated Cost: $" + email.rearEstimate + "\n" +
                "\nTotals\n" +
                "Estimated Price: " + email.topEstimate + "\n" +
                "Tax: " + email.totalTax + "\n" +
                "Final Total: " + email.total + "\n" +
                "Authorized by: " + email.authorization + "\n" +
                "Invoice #: " + invoiceNum + "\n" +
                "Riot Employee ID#: " + id;


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.dealershipEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Riot Dents Invoice");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(getApplicationContext(), "There is no application that supports this action",Toast.LENGTH_LONG).show();
            return;
        }
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invoice").setValue(invoiceNum);

        saveFormData(email);

        leftComments.getText().clear();
        leftEstimate.getText().clear();
        topComments.getText().clear();
        topEstimate.getText().clear();
        rightComments.getText().clear();
        rightEstimate.getText().clear();
        noseComments.getText().clear();
        noseEstimate.getText().clear();
        rearComments.getText().clear();
        rearEstimate.getText().clear();
        carStockNum.getText().clear();
        carVinNum.getText().clear();
        carYear.getText().clear();
        carColor.getText().clear();
        carMake.getText().clear();
        careModel.getText().clear();
        totalTax.getText().clear();
        total.getText().clear();
        totalEstimate.getText().clear();
        authorization.getText().clear();
        dealershipName.getText().clear();
        dealershipEmail.getText().clear();
        getInvoiceNumber();
    }

    public void saveFormData(EmailData email) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Forms").child("Form Data " + invoiceNum)
                .setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Form data has been registered successfully!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Form data has failed to register. Try again.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        this.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(swapped){
            /*Make sure you don't swap twice,
            since the dispatchTouchEvent might dispatch your touch event to this function again!*/
            swapped = false;
            return super.onTouchEvent(event);
        }

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    //you already swapped, set flag swapped = true
                    swapped = true;
                    if (x2 > x1) {
                        Log.i("Swipe", "Left To Right Swipe");
                        vehicles.get(vehicle).setVisibility(View.GONE);
                        if (vehicle == 0) {
                            vehicle = 3;
                        } else {
                            vehicle--;
                        }

                    } else {
                        Log.i("Swipe", "Right to Left Swipe");
                        vehicles.get(vehicle).setVisibility(View.GONE);
                        if (vehicle == 3) {
                            vehicle = 0;
                        } else {
                            vehicle++;
                        }
                    }
                    vehicles.get(vehicle).setVisibility(View.VISIBLE);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.seach_bar, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

        search = (SearchView) item.getActionView();
        search.setQueryHint("Search By Vin Number");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("Search", "A Query was submitted!");

                Query query2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Forms").orderByChild("carVinNum").equalTo(query);

                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                form.setAuthorization(String.valueOf(child.child("authorization").getValue()));
                                form.setCarColor(String.valueOf(child.child("carColor").getValue()));
                                form.setCarMake(String.valueOf(child.child("carMake").getValue()));
                                form.setCarModel(String.valueOf(child.child("carModel").getValue()));
                                form.setCarStockNum(String.valueOf(child.child("carStockNum").getValue()));
                                form.setCarVinNum(String.valueOf(child.child("carVinNum").getValue()));
                                form.setCarYear(String.valueOf(child.child("carYear").getValue()));
                                form.setDate(String.valueOf(child.child("date").getValue()));
                                form.setDealershipEmail(String.valueOf(child.child("dealershipEmail").getValue()));
                                form.setDealershipName(String.valueOf(child.child("dealershipName").getValue()));
                                form.setEmployeeID(String.valueOf(child.child("employeeID").getValue()));
                                form.setInvoiceNum(String.valueOf(child.child("invoiceNum").getValue()));
                                form.setLeftSideComments(String.valueOf(child.child("leftSideComments").getValue()));
                                form.setLeftSideEstimate(String.valueOf(child.child("leftSideEstimate").getValue()));
                                form.setNoseComments(String.valueOf(child.child("noseComments").getValue()));
                                form.setNoseEstimate(String.valueOf(child.child("noseEstimate").getValue()));
                                form.setRearComments(String.valueOf(child.child("rearComments").getValue()));
                                form.setRearEstimate(String.valueOf(child.child("rearEstimate").getValue()));
                                form.setRightSideComments(String.valueOf(child.child("rightSideComments").getValue()));
                                form.setRightSideEstimate(String.valueOf(child.child("rightSideEstimate").getValue()));
                                form.setTopComments(String.valueOf(child.child("topComments").getValue()));
                                form.setTopEstimate(String.valueOf(child.child("topEstimate").getValue()));
                                form.setTotal(String.valueOf(child.child("total").getValue()));
                                form.setTotalEstimate(String.valueOf(child.child("totalEstimate").getValue()));
                                form.setTotalTax(String.valueOf(child.child("totalTax").getValue()));
                            }

                            displayFormData();
                            Intent intent = new Intent(InvoiceActivity.this, FormActivity.class);
                            intent.putExtra(EXTRA_FORM, result);
                            startActivity(intent);
                        } else {
                            Log.e("Search", "The query failed and no results were found.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    public void displayFormData() {
        result = "Riot Dents\n" +
                form.date + "\n" +
                "Vehicle Records: \n" +
                "\tStock #: " + form.carStockNum + "\n" +
                "\tVin #: " + form.carVinNum + "\n" +
                "\tYear: " + form.carYear + "\n" +
                "\tColor: " + form.carColor + "\n" +
                "\tMake: " + form.carMake + "\n" +
                "\tModel: " + form.carModel + "\n" +
                "\nBilling and Damages to Vehicle\n" +
                "\tLeft Side\n" +
                "\t\tDescription of damages: " + form.leftSideComments + "\n" +
                "\t\tEstimated Cost: $" + form.leftSideEstimate + "\n" +
                "\tRight Side\n" +
                "\t\tDescription of damages: " + form.rightSideComments + "\n" +
                "\t\tEstimated Cost: $" + form.rightSideEstimate + "\n" +
                "\tTop of Vehicle\n" +
                "\t\tDescription of damages: " + form.topComments + "\n" +
                "\t\tEstimated Cost: $" + form.topEstimate + "\n" +
                "\tNose of Vehicle\n" +
                "\t\tDescription of damages: " + form.noseComments + "\n" +
                "\t\tEstimated Cost: $" + form.noseEstimate + "\n" +
                "\tRear of Vehicle\n" +
                "\t\tDescription of damages: " + form.rearComments + "\n" +
                "\t\tEstimated Cost: $" + form.rearEstimate + "\n" +
                "\nTotals\n" +
                "\tEstimated Price: " + form.topEstimate + "\n" +
                "\tTax: " + form.totalTax + "\n" +
                "\tFinal Total: " + form.total + "\n" +
                "\tAuthorized by: " + form.authorization + "\n" +
                "\tInvoice #: " + form.invoiceNum + "\n" +
                "\tRiot Employee ID#: " + form.employeeID;
    }
}