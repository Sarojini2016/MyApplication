package com.justin.medical;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Productlist extends AppCompatActivity implements TaskListner,AdapterView.OnItemClickListener{

    public List<ProductPojo> feedItemList1 = new ArrayList<ProductPojo>();
    public List<Product> movieList = new ArrayList<Product>();
    private ListView mRecyclerView;
    JsonRequester requester;
    String className;
    private CustomAdapter mAdapter;
    String productListURL;
    String TOKEN;
    int width;
    LinearLayout.LayoutParams parms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        Display display = getWindowManager().getDefaultDisplay();

         width = display.getWidth()/4; // ((display.getWidth()*20)/100)
        parms = new LinearLayout.LayoutParams(width,width);

        requester = new JsonRequester(this);
        className = getLocalClassName();
        SharedPreferences prefs = getSharedPreferences("MEDICAL_SHARED_PREFS", MODE_PRIVATE);
        TOKEN = prefs.getString("TOKEN", null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Newproduct.class);
                startActivity(in);
            }
        });
        productListURL = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/dispensary/product";
        getProductList();
    }

    public void getProductList() {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String>  paramsHeaders = new HashMap<String, String>();
        paramsHeaders.put("Authorization", TOKEN);
        requester.StringRequesterFormValuesWithHeadersOnly(productListURL, Request.Method.GET, className, "PRODUCTLIST", "PRODUCT_LIST_TAG",paramsHeaders );
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("PRODUCTLIST")) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String names = jsonObject.getString("name");
                            String id = jsonObject.getString("id");
                            String desc = jsonObject.getString("description");
                            String pprice = jsonObject.getString("price");
                            String qty = jsonObject.getString("quatity");
                            String img = jsonObject.getString("image");
                            Product product = new Product();
                            product.setPname(names);
                            product.setPdesc(desc);
                            product.setImage(img);
                            product.setQuantity(qty);
                            product.setPrice(pprice);
                            product.setId(id);
                            movieList.add(product);
                        }
                        mRecyclerView = (ListView) findViewById(R.id.recycler_view);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


                        mAdapter = new CustomAdapter(Productlist.this, movieList);
                        mRecyclerView.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {

        }
    }

    public class CustomAdapter extends BaseAdapter {
        private List<Product> feedItemList;
        protected CategoryItemClickListener listener;
        private Productlist mContext;
        int posi;
        String ss = "<font color=#101010>";
        String ss2 = " <font color=#bf0e14>";
        String ss4 = "</font>";

        private LayoutInflater inflater=null;
        public CustomAdapter(final Productlist context, List<Product> feedItemList) {
            // TODO Auto-generated constructor stub
            this.feedItemList = feedItemList;
            this.mContext = context;
            listener = new CategoryItemClickListener() {
                @Override
                public void onCategoryClick(int position) {
                    Toast.makeText(context, "Clicked POsition:"+position, Toast.LENGTH_LONG);
                }
            };

            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return feedItemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            protected ImageView thumbnail;
            protected TextView name, desc, price, qty;
            protected Button editProduct;

        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            convertView = inflater.inflate(R.layout.product_list_row, null);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.imgv);
            holder.desc = (TextView) convertView.findViewById(R.id.tvpdesc);
            holder.name = (TextView) convertView.findViewById(R.id.tvpname);
            holder.price = (TextView) convertView.findViewById(R.id.tvprice);
            holder.qty = (TextView) convertView.findViewById(R.id.tvquantity);
            holder.editProduct = (Button) convertView.findViewById(R.id.btnedit);



            Product feedItem = feedItemList.get(position);

            if (!(feedItem.getImage().toString().equalsIgnoreCase("")))

            holder.name.setText("Product Name: "+feedItem.getPname());
            holder.price.setText("Product Price: "+feedItem.getPrice());
            holder.qty.setText("Product Quantity: "+feedItem.getQuantity());
            holder.desc.setText("Product Description: "+feedItem.getPdesc());

            Log.e("thumbnail:",feedItem.getImage());


            Picasso.
                    with(Productlist.this).
                    load(feedItem.getImage())
            .placeholder(mContext.getResources().getDrawable(R.drawable.icon)).
                    into(holder.thumbnail);

            holder.editProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, EditProduct.class);
                    intent.putExtra("id", feedItemList.get(position).getId());
                    intent.putExtra("name", feedItemList.get(position).getPname());
                    intent.putExtra("price", feedItemList.get(position).getPrice());
                    intent.putExtra("image", feedItemList.get(position).getImage());
                    intent.putExtra("desc", feedItemList.get(position).getPdesc());
                    intent.putExtra("quantity", feedItemList.get(position).getQuantity());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            return convertView;
        }

    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("","MEDICAL: Clicked here at "+position);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();




    }
}
/*
 */