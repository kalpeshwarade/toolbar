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
import com.hska.ebusiness.toolbar.dao.DatabaseSchema;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Credentials;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.CredentialsMapper;
import com.hska.ebusiness.toolbar.util.ToolbarApplication;
import com.hska.ebusiness.toolbar.util.UserMapper;

/**
 * Acticity class for the Login Page
 */
public class LoginActivity extends AppCompatActivity {

    // Contains the entered username
    private EditText username;
    // Contains the entered password
    private EditText pw;
    // Context variable
    private Context context;

    // LOG variable
    private final String TAG = this.getClass().getSimpleName();
    //Dialog builder for the pop ups
    private AlertDialog.Builder dialogBuilder;

    /**
     * Method to create activity
     *
     * @param savedInstanceState Bundle object
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        dialogBuilder = new AlertDialog.Builder(this);
        username = (EditText) findViewById(R.id.login_username);
        pw = (EditText) findViewById(R.id.login_password);

        // Functionality and Validations for the login button
        final Button loginButton = (Button) findViewById(R.id.btn_login);
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // username null validation
                    if (username.getText().toString().matches("")) {
                        Toast.makeText(LoginActivity.this, "Please enter your Username!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // password null validation
                    if (pw.getText().toString().matches("")) {
                        Toast.makeText(LoginActivity.this, "Please enter your Password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Check if the entered user name is available
                    User user = getUserByUsername(username.getText().toString());
                    if (user == null) {
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

                    // check the credentials
                    if (!checkCredentials(user.getId(), pw.getText().toString())) {
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
                    // GET: User user = ((MyApplication) getApplication()).getCurrentUser();
                    ((ToolbarApplication) getApplication()).setCurrentUser(user);
                    Log.d(LoginActivity.class.getSimpleName(), "User Login Activity: " + user.getId());

                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Dummy-Functionality for the “Forgot password“ link
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

        // Dummy-Functionality for the “Register now“ link
        final TextView registerNow = (TextView) findViewById(R.id.link_register);
        if (registerNow != null) {
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
                }
            });
        }
    }

    /**
     * Method to search a User regarding the entered username
     *
     * @param username Entered username
     * @return User object
     */
    private User getUserByUsername(String username) {
        Cursor cursor = ToolbarDBHelper.getInstance(context).findUserByUsername(username);
        if (cursor != null && cursor.moveToFirst()) {
            return UserMapper.map(cursor);
        }
        cursor.close();
        return null;
    }

    /**
     * Method to search a Credential regarding the ID of a user
     *
     * @param userId ID of an existing User
     * @return Credentials object
     */
    public Credentials getCredentialsByUserId(final long userId) {
        Cursor cursor = ToolbarDBHelper.getInstance(context).findCredentialsByUserId(userId);
        if (cursor != null && cursor.moveToFirst()) {
            return CredentialsMapper.map(cursor);
        }
        cursor.close();
        return null;
    }

    /**
     * Method to check if the entered credentials are valid
     *
     * @param userId Id of the user
     * @param pw     Entered password
     * @return Boolean value
     */
    public boolean checkCredentials(final long userId, final String pw) {

        Cursor cursor = ToolbarDBHelper.getInstance(context).findCredentialsByUserId(userId);

        if (cursor != null && cursor.moveToFirst() && pw != null) {
            Credentials credentials = CredentialsMapper.map(cursor);
            if (credentials.getPassword().equals(pw)) {
                return true;
            } else {
                return false;
            }
        }
        cursor.close();
        return false;
    }


}

