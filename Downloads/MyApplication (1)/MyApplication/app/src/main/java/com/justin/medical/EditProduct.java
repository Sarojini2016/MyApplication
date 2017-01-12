package com.justin.medical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProduct extends AppCompatActivity implements TaskListner {
    String picturePath="";
    TextView tv_path;
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
    String addProductURL = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/dispensary/editproduct";

    MyUtility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
//        edit = (Button) findViewById(R.id.btnedit);
        tv_path= (TextView) findViewById(R.id.tv_path);
        utility = new MyUtility(EditProduct.this);
        requester = new JsonRequester(this);
        className = getLocalClassName();

        SharedPreferences prefs = getSharedPreferences("MEDICAL_SHARED_PREFS", MODE_PRIVATE);
        TOKEN = prefs.getString("TOKEN", null);

       /* edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
        /*camera = (ImageView) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });*/


        upload = (LinearLayout) findViewById(R.id.btnuploads);




        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FromCard();

            }
        });

        picturePath=getIntent().getExtras().getString("image");

        productname = (EditText) findViewById(R.id.productedit_EP);
        productname.setText(getIntent().getExtras().getString("name"));
        price = (EditText) findViewById(R.id.priceedit_EP);
        price.setText(getIntent().getExtras().getString("price"));
        description = (EditText) findViewById(R.id.descriptionedit_EP);
        description.setText(getIntent().getExtras().getString("desc"));
        add = (Button) findViewById(R.id.btn_update);
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
                    updateProduct();

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

    private void updateProduct() {
        loading = ProgressDialog.show(this, "Updating Product...", "Please wait...", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", "");
        params.put("id", getIntent().getExtras().getString("id"));
        params.put("name", productname.getText().toString());
        params.put("description", description.getText().toString());
        params.put("price", price.getText().toString());
        params.put("quantity", "2");
        Map<String, String>  paramsHeaders = new HashMap<String, String>();
        paramsHeaders.put("Authorization", TOKEN);
        requester.StringRequesterFormValuesWithHeaders(addProductURL, Request.Method.POST, className, "EDIT_PRODUCT", params, "PRODUCT_LIST_TAG", paramsHeaders);
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("EDIT_PRODUCT")) {
                    loading = ProgressDialog.show(this, "Product Status", "Product updated successfully.", false, false);

                    loading.dismiss();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {



                            loading.dismiss();
                            Intent intent = new Intent(EditProduct.this, Productlist.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                        }
                    }, 5000);
                }
            }
        } catch (Exception e) {

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                //Getting the Bitmap from Gallery
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                //Setting the Bitmap to ImageView
//                camera1.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();

        loading.dismiss();

    }

    public void FromCamera() {

        Log.i("camera", "startCameraActivity()");
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),toString());
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, 1);

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

//    protected void onPhotoTaken() {
//        // Log message
//        Log.i("SonaSys", "onPhotoTaken");
//        taken = true;
//        imgCapFlag = true;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//        bitmap = BitmapFactory.decodeFile(path, options);
//        image.setImageBitmap(bitmap);
//
//
//    }


}



