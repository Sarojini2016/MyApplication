package com.justin.medical;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vidyaranya on 10/3/16.
 */
public class ProductPojo implements Serializable{
    private static final long serialVersionUID = -901346947603853609L;
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
