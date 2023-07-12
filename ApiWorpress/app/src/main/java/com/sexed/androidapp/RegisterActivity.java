package com.sexed.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foysaltech.wptoandroidapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    Button b1;
    TextView btl;
    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;

    FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    ProgressBar pb1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        b1=findViewById(R.id.register);
        e1=findViewById(R.id.nombre);
        e2=findViewById(R.id.apellido);
        e3=findViewById(R.id.username);
        e4=findViewById(R.id.password);
        pb1=findViewById(R.id.loading);
        btl=findViewById(R.id.login);

        View.OnClickListener Register = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pb1.setVisibility(View.VISIBLE);
                String nombre=e1.getText().toString();
                String apellido=e2.getText().toString();
                String email=e3.getText().toString();
                String password=e4.getText().toString();

                if (TextUtils.isEmpty(nombre)){
                    Toast.makeText(RegisterActivity.this, "Ingresa su nombre", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(apellido)){
                    Toast.makeText(RegisterActivity.this, "Ingresa su apellido", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Ingresa su email", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Ingresa su contraseña", Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pb1.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Cuenta creada",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error al registrar",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        };

        btl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });
        b1.setOnClickListener(Register);

    }
}