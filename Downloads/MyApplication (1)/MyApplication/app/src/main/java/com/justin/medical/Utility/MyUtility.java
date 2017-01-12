package com.justin.medical.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtility {

    Activity activity;
    Matcher matcher;
    boolean isConnected;

    //Default Constructur
    public MyUtility(Activity context)
    {
        this.activity = context;
    }


    //For Checking Internet Available
    @SuppressWarnings("deprecation")
    public boolean isInternetAvailable(){
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        isConnected = info != null && info.isConnectedOrConnecting() && info.isConnected();
        return  isConnected;
    }

    public void isHide(View v) {
        if(activity != null){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public boolean isValidName(String Name){
        String UName="[a-zA-Z0-9]{9}";
        Pattern pattern = Pattern.compile(UName);
        matcher = pattern.matcher(Name);
        return matcher.matches();
    }

    public boolean isNotBlank(String Name){
        if(Name==null || Name.isEmpty() || Name.equals("")){
            return true;
        }
        return false;
    }

    public boolean isValidUserName(String Name){
        String UName="[a-zA-Z ]+";
        Pattern pattern = Pattern.compile(UName);
        matcher = pattern.matcher(Name);
        return matcher.matches();
    }

    public boolean isValidMName(String Name){
        String UName="[a-zA-Z ]{1}";
        Pattern pattern = Pattern.compile(UName);
        matcher = pattern.matcher(Name);
        return matcher.matches();
    }

    public boolean isValidEmail(String Email){
        String email = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(email);
        matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    public boolean isValidPhone(String Phone){
        String No = "[0-9]{10}";
        Pattern pattern = Pattern.compile(No);
        matcher = pattern.matcher(Phone);
        return matcher.matches();
    }

    public void setMessage(String msg, int duration) {
        if (activity != null)
        {
             Toast.makeText(activity, msg, duration).show();
        }
    }

    public void setInternetAlert(){
        if(activity != null){
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Opps !......");
            alert.setMessage("You Are Not Connect With Internet");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
            alert.show();
        }
    }

    public void setAlert(String title, String msg){
        if(activity != null){
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle(title);
            alert.setMessage(msg);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    public void editTextWatcher(final EditText editText){

        if(activity != null){
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    editText.setError(null);
                }
            });
        }
    }

    public void addHintTextWatcher(final EditText editText){

        if (activity != null){
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (s.length() == 0) {
                        // No entered text so will show hint
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        // No entered text so will show hint
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    } else {
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void clickToHide1(LinearLayout linearLayout){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    public void clickToHide2(RelativeLayout relativeLayout){
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity != null){
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
            }
        });
    }

}
