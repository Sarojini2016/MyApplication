package com.justin.medical;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Saurabhk on 04/01/2017.
 */

public class ProductsAdapter  extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>  {

    private List<Product> productList;
    private ImageLoader imageLoader;
    private Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productname, prices, productdescription, quantity;
        public ImageView iv;

        public MyViewHolder(View view) {
            super(view);

            quantity=(TextView)view.findViewById(R.id.tvquantity) ;
            productname = (TextView) view.findViewById(R.id.tvpname);
            prices = (TextView) view.findViewById(R.id.tvprice);
            productdescription = (TextView) view.findViewById(R.id.tvpdesc);
            iv= (ImageView) view.findViewById(R.id.imgv);
        }
    }


    public ProductsAdapter(List<Product> productList, Context ctx) {
        this.productList = productList;
        this.context=ctx;
    }


    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product products = productList.get(position);
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(products.getImage(), ImageLoader
                .getImageListener(holder.iv, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        holder.productname.setText(products.getPname());
        holder.productdescription.setText(products.getPdesc());
        holder.prices.setText(products.getPrice());
        holder.quantity.setText(products.getQuantity());

    }

    public int getItemCount() {
        return productList.size();
    }
}