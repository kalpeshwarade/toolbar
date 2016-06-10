package com.hska.ebusiness.toolbar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Credentials;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.CredentialsMapper;
import com.hska.ebusiness.toolbar.util.ToolbarApplication;
import com.hska.ebusiness.toolbar.util.UserMapper;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private EditText username;
    private EditText password;
    private Context context;
    private AlertDialog.Builder dialogBuilder;

    /**
     * Method to create activity
     *
     * @param savedInstanceState for re-initialization of saved instance
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        dialogBuilder = new AlertDialog.Builder(this);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

        final Button loginButton = (Button) findViewById(R.id.btn_login);
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (username.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter your Username!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter your Password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final User user = getUserByUsername(username.getText().toString());
                    if (user == null) {
                        Toast.makeText(LoginActivity.this, "Can't find the entered user!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!checkCredentials(user.getId(), password.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "Credentials are wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ((ToolbarApplication) getApplication()).setCurrentUser(user);
                    Log.d(TAG, "User with ID " + user.getId() + " logged in.");

                    final Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            });
        }

        final TextView textForgotPassword = (TextView) findViewById(R.id.link_forgot_pw);
        if (textForgotPassword != null) {
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
                }

                ;
            });
        }

        final TextView registerNow = (TextView) findViewById(R.id.link_register);
        if (registerNow != null) {
            registerNow.setOnClickListener(new View.OnClickListener() {
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
                    dialogBuilder.setIcon(R.drawable.ic_lock_black_24dp);
                    dialogBuilder.create();
                    dialogBuilder.show();
                }
            });
        }
    }

    /**
     * Method to find a user regarding the entered username
     *
     * @param username the entered username
     * @return user object
     */
    private User getUserByUsername(final String username) {
        final Cursor cursor = ToolbarDBHelper.getInstance(context).findUserByUsername(username);
        if (cursor != null && cursor.moveToFirst()) {
            return UserMapper.map(cursor);
        }
        cursor.close();
        return null;
    }

    /**
     * Method to check if the entered credentials are valid
     *
     * @param userId Id of the user
     * @param password     Entered password
     * @return true if credentials are valid
     */
    public boolean checkCredentials(final long userId, final String password) {
        final Cursor cursor = ToolbarDBHelper.getInstance(context).findCredentialsByUserId(userId);
        if (cursor != null && cursor.moveToFirst() && password != null) {
            final Credentials credentials = CredentialsMapper.map(cursor);
            return credentials.getPassword().equals(password);
        }
        cursor.close();
        return false;
    }
}

