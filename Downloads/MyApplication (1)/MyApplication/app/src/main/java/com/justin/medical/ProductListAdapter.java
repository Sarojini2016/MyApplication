package com.justin.medical;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.FeedListRowHolder> {

    private List<Product> feedItemList;
    protected CategoryItemClickListener listener;
    private Productlist mContext;
    int posi;
    String ss = "<font color=#101010>";
    String ss2 = " <font color=#bf0e14>";
    String ss4 = "</font>";

    public ProductListAdapter(final Productlist context, List<Product> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        listener = new CategoryItemClickListener() {
            @Override
            public void onCategoryClick(int position) {
                Toast.makeText(context, "Clicked POsition:"+position, Toast.LENGTH_LONG);
            }
        };
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_row, null);

        FeedListRowHolder mh = new FeedListRowHolder(v, i);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        Product feedItem = feedItemList.get(i);
        posi = i;
        if (!(feedItem.getImage().toString().equalsIgnoreCase("")))
            Picasso.with(mContext).load(feedItem.getImage())
                    .into(feedListRowHolder.thumbnail);
        feedListRowHolder.name.setText("Product Name: "+feedItem.getPname());
        feedListRowHolder.price.setText("Product Price: "+feedItem.getPrice());
        feedListRowHolder.qty.setText("Product Quantity: "+feedItem.getQuantity());
        feedListRowHolder.desc.setText("Product Description: "+feedItem.getPdesc());
        feedListRowHolder.posit = i;

    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class FeedListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView thumbnail;
        protected TextView name, desc, price, qty;
        protected Button editProduct;
        View colorStyle_TV;
        int posit;

        public FeedListRowHolder(View view, int pos) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.imgv);
            this.desc = (TextView) view.findViewById(R.id.tvpdesc);
            this.name = (TextView) view.findViewById(R.id.tvpname);
            this.price = (TextView) view.findViewById(R.id.tvprice);
            this.qty = (TextView) view.findViewById(R.id.tvquantity);
            this.editProduct = (Button) view.findViewById(R.id.btnedit);
            final int poss = pos;
            this.editProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, EditProduct.class);
                    intent.putExtra("id", feedItemList.get(poss).getId());
                    intent.putExtra("name", feedItemList.get(poss).getPname());
                    intent.putExtra("price", feedItemList.get(poss).getPrice());
                    intent.putExtra("desc", feedItemList.get(poss).getPdesc());
                    intent.putExtra("quantity", feedItemList.get(poss).getQuantity());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("","MEDICAL: Clicked here at ");
        }
    }
}