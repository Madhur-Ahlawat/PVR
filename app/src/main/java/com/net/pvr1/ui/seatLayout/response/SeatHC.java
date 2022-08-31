package com.net.pvr1.ui.seatLayout.response;


import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeatHC {

    @SerializedName("priceCode")
    @Expose
    private String PriceCode;
    @SerializedName("seatBookingId")
    @Expose
    private String SeatBookingId;
    private Integer st;
    private Integer num1;
    private Integer num2;
    private TextView seatView;

    /**
     *
     * @return
     * The PriceCode
     */
    public String getPriceCode() {
        return PriceCode;
    }

    public TextView getSeatView() {
        return seatView;
    }

    public void setSeatView(TextView seatView) {
        this.seatView = seatView;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    /**
     *
     * @param PriceCode
     * The PriceCode
     */
    public void setPriceCode(String PriceCode) {
        this.PriceCode = PriceCode;
    }

    /**
     *
     * @return
     * The SeatBookingId
     */
    public String getSeatBookingId() {
        return SeatBookingId;
    }

    public Integer getSt() {
        return st;
    }

    public void setSt(Integer st) {
        this.st = st;
    }

    /**
     *
     * @param SeatBookingId
     * The SeatBookingId
     */
    public void setSeatBookingId(String SeatBookingId) {
        this.SeatBookingId = SeatBookingId;
    }

    @Override
    public String toString() {
        return "SeatHC{" +
                "PriceCode='" + PriceCode + '\'' +
                ", SeatBookingId='" + SeatBookingId + '\'' +
                ", st=" + st +
                ", num1=" + num1 +
                ", num2=" + num2 +
                ", seatView=" + seatView +
                '}';
    }
}

