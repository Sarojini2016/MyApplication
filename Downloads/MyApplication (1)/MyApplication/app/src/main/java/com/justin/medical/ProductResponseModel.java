package com.justin.medical;

import com.google.gson.annotations.SerializedName;

public class ProductResponseModel {

    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("price")
    public String price;
    @SerializedName("quatity")
    public String quatity;
    @SerializedName("image")
    public String image;

}
