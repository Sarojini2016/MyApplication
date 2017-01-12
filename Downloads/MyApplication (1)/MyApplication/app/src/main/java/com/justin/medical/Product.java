package com.justin.medical;

/**
 * Created by Saurabhk on 04/01/2017.
 */

public class Product {
    private String image;

    private String pname;
    private String pdesc;
    private String prices;
    private String quantity;
    private String id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public Product() {
    }

    public Product(String image, String pname, String pdesc,String price,String quantity, String id ) {
        this.image = image;
        this.pname = pname;
        this.pdesc = pdesc;
        this.prices = price;
        this.quantity=quantity;
        this.id=id;

    }

    public String getPrice() {
        return prices;
    }

    public void setPrice(String price) {
        this.prices = price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }
}
