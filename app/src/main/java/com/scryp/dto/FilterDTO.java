package com.scryp.dto;

/**
 * Created by Mayur on 28-11-2015.
 */
public class FilterDTO {
    public String filterRadius;
    public String lat;
    public String lng;
    public String distance;
    public String filterPrice;
    public String priceMin;
    public String priceMax;
    public String filterDiscount;
    public String discountMin;
    public String discountMax;
    public String sortvar;
    public String asc;

    public String getFilterRadius() {
        return filterRadius;
    }

    public void setFilterRadius(String filterRadius) {
        this.filterRadius = filterRadius;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFilterPrice() {
        return filterPrice;
    }

    public void setFilterPrice(String filterPrice) {
        this.filterPrice = filterPrice;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public String getFilterDiscount() {
        return filterDiscount;
    }

    public void setFilterDiscount(String filterDiscount) {
        this.filterDiscount = filterDiscount;
    }

    public String getDiscountMin() {
        return discountMin;
    }

    public void setDiscountMin(String discountMin) {
        this.discountMin = discountMin;
    }

    public String getDiscountMax() {
        return discountMax;
    }

    public void setDiscountMax(String discountMax) {
        this.discountMax = discountMax;
    }

    public String getSortvar() {
        return sortvar;
    }

    public void setSortvar(String sortvar) {
        this.sortvar = sortvar;
    }

    public String getAsc() {
        return asc;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }
}
