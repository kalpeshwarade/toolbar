package com.hska.ebusiness.toolbar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Credentials;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.CredentialsMapper;
import com.hska.ebusiness.toolbar.util.ToolbarApplication;
import com.hska.ebusiness.toolbar.util.UserMapper;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText pw;
    private Context context;

    private final String TAG = this.getClass().getSimpleName();
    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        insertMockingUser();

        dialogBuilder = new AlertDialog.Builder(this);
        username = (EditText) findViewById(R.id.login_username);
        pw = (EditText) findViewById(R.id.login_password);

        final Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().matches("")) {
                    Toast.makeText(LoginActivity.this, "Please enter your Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pw.getText().toString().matches("")) {
                    Toast.makeText(LoginActivity.this, "Please enter your Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = getUserByUsername(username.getText().toString());
                if(user == null) {
                    dialogBuilder.setTitle("Username wrong!")
                            .setMessage("Can't find the entered user!");
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                    return;
                }
                if(!checkCredentials(user.getId(), pw.getText().toString())) {
                    dialogBuilder.setTitle("Credentials wrong!")
                            .setMessage("The entered password is wrong!");
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                    return;
                }

                // Global  variable user
                // GET: User user = ((ToolbarApplication) getApplication()).getCurrentUser();
                ((ToolbarApplication) getApplication()).setCurrentUser(user);
                final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final TextView textForgotPassword = (TextView) findViewById(R.id.link_forgot_pw);
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
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
                dialogBuilder.setIcon(R.drawable.ic_lock_black_24dp);
                dialogBuilder.create();
                dialogBuilder.show();
            };
        });

        final TextView registerNow = (TextView) findViewById(R.id.link_register);
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle("Register now!")
                        .setMessage("Under Construction!");
                         //.setMessage(getCredentialsByUserId(getUserByUsername("bbb").getId()).getPassword());
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialogBuilder.setIcon(R.drawable.ic_lock_black_24dp);
                dialogBuilder.create();
                dialogBuilder.show();
            };
        });
    }

    public User getUserByUsername(String username) {
        Cursor cursor = ToolbarDBHelper.getInstance(context).findUserByUsername(username);
        if(cursor != null && cursor.moveToFirst()) {
            return UserMapper.map(cursor);
        }
        cursor.close();
        return null;
    }

    public Credentials getCredentialsByUserId(final long userId) {
        Cursor cursor = ToolbarDBHelper.getInstance(context).findCredentialsByUserId(userId);
        if(cursor != null && cursor.moveToFirst()) {
            return CredentialsMapper.map(cursor);
        }
        cursor.close();
        return null;
    }

    public boolean checkCredentials(final long userId, final String pw) {

        Cursor cursor = ToolbarDBHelper.getInstance(context).findCredentialsByUserId(userId);

        if(cursor != null && cursor.moveToFirst()) {
            Credentials credentials = CredentialsMapper.map(cursor);
            if (credentials.getPassword().equals(pw.toString())) {
                return true;
            } else {
                return false;
            }
        }
        cursor.close();
        return false;
    }

    public void insertMockingUser() {

    }


}