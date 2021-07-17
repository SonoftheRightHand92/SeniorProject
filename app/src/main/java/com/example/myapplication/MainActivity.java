package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView emailView;
    private TextView passwordView;
    private ProgressBar progressBar;
    private String id;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.editTextTextEmailAddress);
        passwordView = findViewById(R.id.editTextTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void signIn(View view) {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (email.isEmpty()) {
            emailView.setError("Email address is required");
            emailView.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordView.setError("Password is required");
            passwordView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailView.setError("A valid email address is required");
            emailView.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this, InvoiceActivity.class));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Failed to login! Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signUp(View view) {
        email = emailView.getText().toString();
        password = passwordView.getText().toString();
        id = "";

        if (email.isEmpty()) {
            emailView.setError("Email Address is required");
            emailView.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordView.setError("Password is required");
            passwordView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailView.setError("A valid email address is required");
            emailView.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordView.setError("Password length must be greater than 6 characters");
            passwordView.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View confirmationView = getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
        final EditText passwordCheck = (EditText) confirmationView.findViewById(R.id.confirmPassword);
        final EditText idInput = (EditText) confirmationView.findViewById(R.id.employeeID);
        Button okButton = (Button) confirmationView.findViewById(R.id.button);
        Button cancelButton = (Button) confirmationView.findViewById(R.id.button2);
        builder.setView(confirmationView);

        AlertDialog alert = builder.create();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordCheck.getText().toString().equals(password)) {
                    passwordCheck.setError("Passwords do not match. Try again.");
                    passwordCheck.requestFocus();
                    return;
                }
                if(!idInput.getText().toString().isEmpty()) {
                    id = idInput.getText().toString();
                    alert.dismiss();
                    createNewUser();
                }else {
                    idInput.setError("Employee ID field is required.");
                    idInput.requestFocus();
                    return;
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }

    public void createNewUser() {
        String invoice = "0";
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(email, password, id, invoice);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }else {
                                        Toast.makeText(getApplicationContext(), "User has failed to register. Try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "User has failed to register. Try again.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
}