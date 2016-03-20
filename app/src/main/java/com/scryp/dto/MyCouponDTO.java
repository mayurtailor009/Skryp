package com.scryp.dto;

import java.io.Serializable;

/**
 * Created by Mayur on 28-11-2015.
 */
public class MyCouponDTO implements Serializable {
    public String merchant_name;
    public String expiration;
    public String downloadDate;
    public String id;
    public String title;
    public String post_date;
    public String post_date_gmt;
    public String discount;
    public String thumb_url;
    public String download;
    public String amount_saved;
    public String max_purchases;
    public String expiration_date;
    public String price;
    public String value;
    public String voucher_expiration_date;

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_date_gmt() {
        return post_date_gmt;
    }

    public void setPost_date_gmt(String post_date_gmt) {
        this.post_date_gmt = post_date_gmt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getAmount_saved() {
        return amount_saved;
    }

    public void setAmount_saved(String amount_saved) {
        this.amount_saved = amount_saved;
    }

    public String getMax_purchases() {
        return max_purchases;
    }

    public void setMax_purchases(String max_purchases) {
        this.max_purchases = max_purchases;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVoucher_expiration_date() {
        return voucher_expiration_date;
    }

    public void setVoucher_expiration_date(String voucher_expiration_date) {
        this.voucher_expiration_date = voucher_expiration_date;
    }
}
