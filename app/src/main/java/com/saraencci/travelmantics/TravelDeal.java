package com.saraencci.travelmantics;

import java.io.Serializable;

public class TravelDeal implements Serializable {

    private String id;
    private String tittle;
    private String description;
    private String price;
    private String imgUrl;

    public TravelDeal(){    };

    public TravelDeal( String tittle, String description, String price, String imgUrl) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
