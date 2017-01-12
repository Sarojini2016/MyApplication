package com.justin.medical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.justin.medical.Utility.MyUtility;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Newproduct extends AppCompatActivity implements TaskListner {
    TextView tv_path;
    String picturePath="";
    MyUtility utility;

    Button edit;
    Button add;
    LinearLayout upload;
    private Bitmap bitmap;
    JsonRequester requester;
    String className;
    String TOKEN;
    ProgressDialog loading;
    EditText productname, price, description;
    private int PICK_IMAGE_REQUEST = 1;
    String addProductURL = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/dispensary/addproduct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproduct);
//        edit = (Button) findViewById(R.id.btnedit);

        utility = new MyUtility(Newproduct.this);
        requester = new JsonRequester(this);
        className = getLocalClassName();

        SharedPreferences prefs = getSharedPreferences("MEDICAL_SHARED_PREFS", MODE_PRIVATE);
        TOKEN = prefs.getString("TOKEN", null);

       /* edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/


        tv_path= (TextView) findViewById(R.id.tv_path);
        upload = (LinearLayout) findViewById(R.id.btnuploads);
        productname = (EditText) findViewById(R.id.productedit);
        price = (EditText) findViewById(R.id.priceedit);
        description = (EditText) findViewById(R.id.descriptionedit);
        add = (Button) findViewById(R.id.btnadd);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromCard();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (productname.getText().toString().equals("")) {


                     productname.setError("Enter Valid Product Name");
                    utility.editTextWatcher(productname);
                } else if (price.getText().toString().equals("")) {


                     price.setError("Enter Price");
                    utility.editTextWatcher(price);
                } else if (description.getText().toString().equals("")) {


                     description.setError("Enter description");
                    utility.editTextWatcher(description);
                } else if (picturePath.equals("")) {

utility.setAlert("Product photo","Please choose product photo");

                }

                else
                 {
                     addProduct();

                 }


            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void addProduct() {
        loading = ProgressDialog.show(this, "Adding Product...", "Please wait...", false, false);
        Map<String, String> params = new HashMap<String, String>();
//        params.put("title", "");
        params.put("name", productname.getText().toString());
        params.put("description", description.getText().toString());
        params.put("price", price.getText().toString());
        params.put("quantity", "2");
        params.put("image", getEncoded64ImageStringFromBitmap(bitmap));
        Map<String, String>  paramsHeaders = new HashMap<String, String>();
        paramsHeaders.put("Authorization", TOKEN);
        requester.StringRequesterFormValuesWithHeaders(addProductURL, Request.Method.POST, className, "ADD_NEW_PRODUCT", params, "PRODUCT_LIST_TAG", paramsHeaders);
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("ADD_NEW_PRODUCT")) {
                    loading = ProgressDialog.show(this, "Product Status", "Product added successfully.", false, false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            loading.dismiss();
                            Intent intent = new Intent(Newproduct.this, Productlist.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, 5000);
                }
            }
        } catch (Exception e) {

        }
    }

    public void FromCard() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();


            bitmap = BitmapFactory.decodeFile(picturePath);


            if (bitmap != null) {

                tv_path.setVisibility(View.VISIBLE);
                tv_path.setText(picturePath);
            }

        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
                case -1:

                    break;

            }

        }

    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }





}



