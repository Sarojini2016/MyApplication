package com.justin.medical;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.justin.medical.Utility.MyUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity implements TaskListner {

    MyUtility utility;
    ProgressDialog loading;
    private static final String TAG = MainActivity.class.getSimpleName();
    String password;
    String email;
    private EditText usernameEditText1;
    private EditText passwordEditText1;
    private Button buttonlogin;
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;
    JsonRequester requester;
    String className;
    String loginurl = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/authentication/login";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utility = new MyUtility(MainActivity.this);

        pref1 = getApplicationContext().getSharedPreferences("Medical", 0); // 0 - for private mode
        editor1 = pref1.edit();

        requester = new JsonRequester(this);
        className = getLocalClassName();

        usernameEditText1 = (EditText) findViewById(R.id.edusername);
        passwordEditText1 = (EditText) findViewById(R.id.edpassword);
        buttonlogin = (Button) findViewById(R.id.btnlogin);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emailValidator(usernameEditText1.getText().toString().trim())) {



                        usernameEditText1.setError("Enter valid mail id");
                        utility.editTextWatcher(usernameEditText1);


                } else if (passwordEditText1.getText().toString().equals("")) {


                    passwordEditText1.setError("Enter Valid Password");
                    utility.editTextWatcher(passwordEditText1);
                }
                else
                {
                    loginUser();
                }


            }
        });
    }

    private void loginUser() {
        loading = ProgressDialog.show(this, "Login ", "Please wait...", false, false);
        loading.show();
        // final String username =  usernameEditText1.getText().toString().trim();
        email = usernameEditText1.getText().toString().trim();
        password = passwordEditText1.getText().toString().trim();
//        if (email.equals("") || password.equals("")) {
//            Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
//        }
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            usernameEditText1.setError("enter a valid email address");
//            // valid = false;
//        } else {
//            usernameEditText1.setError(null);
//        }

       /* if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText1.setError("between 4 and 10 alphanumeric characters");
            //valid = false;
        } else {
            passwordEditText1.setError(null);
        }*/
        getAuthenticationToken();
    }

    public void getAuthenticationToken() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        requester.StringRequesterFormValues(loginurl, Request.Method.POST, className, "LOGIN", params, "LOGIN_TAG");
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {

            Log.e("response:",response);
            if (cd == 00) {

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("LOGIN")) {
                    try {
                        loading.dismiss();
                        utility.setAlert("Success","Login Successfully");

                        JSONObject jsonObject = new JSONObject(response);
                        SharedPreferences.Editor editor = getSharedPreferences("MEDICAL_SHARED_PREFS", MODE_PRIVATE).edit();
                        editor.putString("TOKEN", "" + jsonObject.getString("token"));
                        editor.commit();




                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loading.dismiss();
                                Intent intent = new Intent(MainActivity.this, Productlist.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        }, 3000);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            else
            {
                utility.setAlert("Error","id and password does not math");
            }
        } catch (Exception e) {
            utility.setAlert("Error","id and password does not math");
        }
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}