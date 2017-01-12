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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.justin.medical.Utility.MyUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupAcitvity extends Activity implements TaskListner {

    MyUtility utility;


    ProgressDialog loading;
    ImageView imgdispensary;
    TextView tv;
    SharedPreferences pref1;
    String typeOfUser;
    SharedPreferences.Editor editor1;
    JsonRequester requester;
    String className;
    int status;
    String token;
    String text, dispensary;
    Button register, login;
    private ProgressDialog pDialog;
    private static final String TAG = SignupAcitvity.class.getSimpleName();
    EditText edfirstname, edlastname, edemail, edpassword, edphone, edaddress;
    String registerurl = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/authentication/register";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRES = "address";
    public static final String KEY_ROLE = "role";
    public static final String KEY_CTYPE = "Content-Type";

    // public static final String KEY_DISPENSARY="dispensary";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_acitvity);
        edfirstname = (EditText) findViewById(R.id.edtxt_firstname);
        edlastname = (EditText) findViewById(R.id.edtxt_lastname);
        edemail = (EditText) findViewById(R.id.edtxt_email);
        edpassword = (EditText) findViewById(R.id.edtxt_password);
        edphone = (EditText) findViewById(R.id.edtxt_phone);
        login = (Button) findViewById(R.id.btnlogin);
        edaddress = (EditText) findViewById(R.id.edaddress);
        register = (Button) findViewById(R.id.btnregister);
//        edfirstname.setText("gaurang");
//        edlastname.setText("chhatbar");
//        edemail.setText("androidtest707@gmail.com");
//        edfirstname.setText("gaurang");
//        edlastname.setText("chhatbar");
//        edpassword.setText("12345");
//        edaddress.setText("abc sng");
//        edphone.setText("8888888888");
        requester = new JsonRequester(this);
        className = getLocalClassName();

//         typeOfUser =  pref1.getString("userType",null);

        utility = new MyUtility(SignupAcitvity.this);

        pref1 = getApplicationContext().getSharedPreferences("Medical", 0); // 0 - for private mode
        editor1 = pref1.edit();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // makePostRequest();



                Toast.makeText(SignupAcitvity.this, "Clicked", Toast.LENGTH_LONG).show();

                if (!emailValidator(edemail.getText().toString().trim())) {



                        edemail.setError("Enter valid mail id");
                        utility.editTextWatcher(edemail);


                } else if (edpassword.getText().toString().equals("")) {


                    edpassword.setError("Enter Valid Password");
                    utility.editTextWatcher(edpassword);
                }

                else if (edfirstname.getText().toString().equals("")) {


                    edfirstname.setError("Enter Valid First Name");
                    utility.editTextWatcher(edfirstname);
                } else if (edlastname.getText().toString().equals("")) {


                    edlastname.setError("Enter Valid Last Name");
                    utility.editTextWatcher(edlastname);
                } else if (edphone.getText().toString().equals("") || !utility.isValidPhone(edphone.getText().toString())) {

                    Log.e("Clicked", "6666");
                    edphone.setError("Enter valid mobile no");
                    utility.editTextWatcher(edphone);

                } else if (edaddress.getText().toString().equals("") || !utility.isValidPhone(edphone.getText().toString())) {

                    Log.e("Clicked", "6666");
                    edaddress.setError("Enter valid Address");
                    utility.editTextWatcher(edaddress);

                } else {


                    registerUser();
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // makePostRequest();
                Intent intent = new Intent(SignupAcitvity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    public void onSignupSuccess() {
        register.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "User registration failed", Toast.LENGTH_LONG).show();
        register.setEnabled(true);
    }

    private void registerUser() {
        // final String username =  usernameEditText1.getText().toString().trim();
        final String emails = edemail.getText().toString().trim();
        final String passwords = edpassword.getText().toString().trim();
        final String firstn = edfirstname.getText().toString().trim();
        final String lastnm = edlastname.getText().toString().trim();
        final String phoneno = edphone.getText().toString().trim();
        final String add = edaddress.getText().toString().trim();


//        if (firstn.isEmpty() || firstn.length() < 3) {
//
//
//            edfirstname.setError("at least 3 characters");
//
//        } else if (lastnm.isEmpty() || lastnm.length() < 3) {
//            edlastname.setError("at least 3 characters");
//
//        } else if (emails.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
//            edemail.setError("enter a valid email address");
//
//        } else if (phoneno.isEmpty() || !Patterns.PHONE.matcher(phoneno).matches()) {
//            edphone.setError("enter a valid phoneno");
//
//        } else if (add.isEmpty() || add.length() < 100 || add.length() > 10) {
//            //edaddress.setError("enter valid address");
//
//        } else if (passwords.isEmpty() || passwords.length() < 4 || passwords.length() > 10) {
//
//            edpassword.setError("between 3 and 10 alphanumeric characters");
//
//        }
        editor1.putString("name", emails);
        editor1.putString("password", passwords);
        editor1.commit();
        //Showing a dialog till we get the route
        loading = ProgressDialog.show(this, "Registering User", "Please wait...", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", emails);
        params.put("password", passwords);
        params.put("first_name", firstn);
        params.put("last_name", lastnm);
        params.put("phone", phoneno);
        params.put("address", add);
        params.put("role", "user");
        requester.StringRequesterFormValues(registerurl, Request.Method.POST, className, "REGISTER", params, "REGISTER_TAG");
    }

    public void onBackPressed() {
        finish();
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {
                loading.dismiss();
                onSignupFailed();
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("REGISTER")) {
                    loading.dismiss();

                    utility.setAlert("Success","Registration Successfully");



                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            loading.dismiss();
                            Intent intent = new Intent(SignupAcitvity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }, 3000);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {

            loading.dismiss();

            utility.setAlert("Error","Server issue");

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