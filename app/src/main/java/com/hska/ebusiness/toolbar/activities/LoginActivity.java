package com.hska.ebusiness.toolbar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hska.ebusiness.toolbar.R;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialogBuilder = new AlertDialog.Builder(this);

        final Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final TextView textForgot = (TextView) findViewById(R.id.link_forgot_pw);
        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle("Forgot Password?")
                        .setMessage("Under Construction!");
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialogBuilder.create();
                dialogBuilder.show();
            };
        });

        final TextView registerNow = (TextView) findViewById(R.id.link_register);
        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle("Register now!")
                        .setMessage("Under Construction!");
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialogBuilder.create();
                dialogBuilder.show();
            };
        });
    }
}