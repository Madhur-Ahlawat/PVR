package com.net.pvr1.ui.seatLayout.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R \u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR \u0010\t\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\b\u00a8\u0006\f"}, d2 = {"Lcom/net/pvr1/ui/seatLayout/response/Seat;", "", "()V", "priceCode", "", "getPriceCode", "()Ljava/lang/String;", "setPriceCode", "(Ljava/lang/String;)V", "seatBookingId", "getSeatBookingId", "setSeatBookingId", "app_debug"})
public final class Seat {
    
    /**
     * @param PriceCode
     * The PriceCode
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.Expose()
    @com.google.gson.annotations.SerializedName(value = "priceCode")
    private java.lang.String priceCode;
    
    /**
     * @param SeatBookingId
     * The SeatBookingId
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.Expose()
    @com.google.gson.annotations.SerializedName(value = "seatBookingId")
    private java.lang.String seatBookingId;
    
    public Seat() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPriceCode() {
        return null;
    }
    
    public final void setPriceCode(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSeatBookingId() {
        return null;
    }
    
    public final void setSeatBookingId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
}