package com.net.pvr1.ui.seatLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.gson.Gson;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivitySeatLayoutBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.food.FoodActivity;
import com.net.pvr1.ui.seatLayout.adapter.ShowsAdapter;
import com.net.pvr1.ui.seatLayout.request.ReserveSeatRequest;
import com.net.pvr1.ui.seatLayout.response.*;
import com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel;
import com.net.pvr1.ui.summery.SummeryActivity;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import java.math.BigDecimal;

@kotlin.Suppress(names = {"DEPRECATION", "NAME_SHADOWING"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u00c8\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u001a2\u0006\u00108\u001a\u00020\u0010H\u0002J(\u00109\u001a\u0002062\u0006\u0010:\u001a\u00020#2\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\r2\u0006\u0010>\u001a\u00020\rH\u0002J(\u0010?\u001a\u0002062\u0006\u0010@\u001a\u00020A2\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\r2\u0006\u0010>\u001a\u00020\rH\u0003J\b\u0010B\u001a\u000206H\u0003J\u0016\u0010C\u001a\u0002062\f\u0010D\u001a\b\u0012\u0004\u0012\u00020 0EH\u0002J.\u0010F\u001a\u0002062\u0006\u0010G\u001a\u00020H2\f\u0010I\u001a\b\u0012\u0004\u0012\u00020J0E2\u0006\u0010K\u001a\u00020\u001a2\u0006\u0010L\u001a\u00020\rH\u0002J\b\u0010M\u001a\u000206H\u0002J\u0012\u0010N\u001a\u0002062\b\u0010O\u001a\u0004\u0018\u00010PH\u0014J\u0010\u0010Q\u001a\u0002062\u0006\u0010@\u001a\u00020AH\u0003J\u0010\u0010R\u001a\u0002062\u0006\u0010:\u001a\u00020#H\u0003J\b\u0010S\u001a\u000206H\u0002J\u0010\u0010T\u001a\u0002062\u0006\u0010U\u001a\u00020VH\u0002J\u0010\u0010W\u001a\u0002062\u0006\u0010X\u001a\u00020YH\u0002J\u0010\u0010Z\u001a\u0002062\u0006\u0010X\u001a\u00020[H\u0002J\b\u0010\\\u001a\u000206H\u0002J \u0010]\u001a\u0002062\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\r2\u0006\u0010>\u001a\u00020\rH\u0002J\b\u0010^\u001a\u000206H\u0002J\u0010\u0010_\u001a\u0002062\u0006\u0010`\u001a\u00020\rH\u0016R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u001e\u001a\u0016\u0012\u0004\u0012\u00020 \u0018\u00010\u001fj\n\u0012\u0004\u0012\u00020 \u0018\u0001`!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010$\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020&\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010)\u001a\u0016\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u001fj\n\u0012\u0004\u0012\u00020\u001a\u0018\u0001`!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010+\u001a\b\u0012\u0004\u0012\u00020,0\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010-\u001a\b\u0012\u0004\u0012\u00020.0\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010/\u001a\u0016\u0012\u0004\u0012\u000200\u0018\u00010\u001fj\n\u0012\u0004\u0012\u000200\u0018\u0001`!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u00101\u001a\u0016\u0012\u0004\u0012\u000200\u0018\u00010\u001fj\n\u0012\u0004\u0012\u000200\u0018\u0001`!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u00103\u001a\b\u0012\u0004\u0012\u0002040\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006a"}, d2 = {"Lcom/net/pvr1/ui/seatLayout/SeatLayoutActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/seatLayout/adapter/ShowsAdapter$RecycleViewItemClickListenerCity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/seatLayout/viewModel/SeatLayoutViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/seatLayout/viewModel/SeatLayoutViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivitySeatLayoutBinding;", "caCount", "", "caCount1", "caSeat", "", "coupleSeat", "flagHc", "flagHc1", "hcCount", "hcCount1", "hcSeat", "hcSeat1", "isDit", "keyData", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "messageText", "noOfRowsSmall", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output$Row;", "Lkotlin/collections/ArrayList;", "noOfSeatsSelected", "Lcom/net/pvr1/ui/seatLayout/response/SeatTagData;", "priceMap", "", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output$PriceList$Price;", "priceVal", "", "seatsN", "selectBuddy", "selectSeatPriceCode", "Lcom/net/pvr1/ui/seatLayout/request/ReserveSeatRequest$Seat;", "selectedSeats", "Lcom/net/pvr1/ui/seatLayout/response/Seat;", "selectedSeats1", "Lcom/net/pvr1/ui/seatLayout/response/SeatHC;", "selectedSeatsBox", "sessionId", "showsArray", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse$Output$Cinema$Child$Sw$S;", "addRowName", "", "rowName", "space", "addSelectedSeats", "seat", "seatView", "Landroid/widget/TextView;", "num1", "num2", "buttonClicked", "context", "Landroid/app/Activity;", "calculatePrice", "drawColumn", "noOfRows", "", "drawRow", "llDrawRow", "Landroid/widget/LinearLayout;", "noSeats", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output$Row$S;", "areaName", "num", "initTrans", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "removeHC", "removeSelectedSeats", "reserveSeat", "retrieveData", "data", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output;", "retrieverInitData", "output", "Lcom/net/pvr1/ui/seatLayout/response/InitResponse$Output;", "retrieverReserveSeatData", "Lcom/net/pvr1/ui/seatLayout/response/ReserveSeatResponse$Output;", "seatLayout", "setClick", "shows", "showsClick", "comingSoonItem", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SeatLayoutActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.seatLayout.adapter.ShowsAdapter.RecycleViewItemClickListenerCity {
    private com.net.pvr1.databinding.ActivitySeatLayoutBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private boolean selectBuddy = false;
    private boolean hcSeat = false;
    private boolean hcSeat1 = false;
    private boolean caSeat = false;
    private boolean flagHc = false;
    private boolean flagHc1 = false;
    private double priceVal = 0.0;
    private int hcCount1 = 0;
    private int caCount1 = 0;
    private int hcCount = 0;
    private int caCount = 0;
    private java.lang.String keyData = "";
    private java.util.ArrayList<java.lang.String> seatsN;
    private int coupleSeat = 0;
    private java.lang.String messageText = "";
    private java.lang.String sessionId = "";
    private boolean isDit = false;
    private java.util.ArrayList<com.net.pvr1.ui.seatLayout.response.SeatResponse.Output.Row> noOfRowsSmall;
    private java.util.Map<java.lang.String, com.net.pvr1.ui.seatLayout.response.SeatResponse.Output.PriceList.Price> priceMap;
    private java.util.ArrayList<com.net.pvr1.ui.seatLayout.response.Seat> selectedSeats;
    private java.util.ArrayList<com.net.pvr1.ui.seatLayout.response.SeatTagData> noOfSeatsSelected;
    private java.util.ArrayList<com.net.pvr1.ui.seatLayout.response.SeatHC> selectedSeatsBox;
    private java.util.ArrayList<com.net.pvr1.ui.seatLayout.response.SeatHC> selectedSeats1;
    private java.util.ArrayList<com.net.pvr1.ui.bookingSession.response.BookingResponse.Output.Cinema.Child.Sw.S> showsArray;
    private java.util.ArrayList<com.net.pvr1.ui.seatLayout.request.ReserveSeatRequest.Seat> selectSeatPriceCode;
    
    public SeatLayoutActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void shows() {
    }
    
    private final void seatLayout() {
    }
    
    private final void reserveSeat() {
    }
    
    private final void retrieverReserveSeatData(com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse.Output output) {
    }
    
    private final void initTrans() {
    }
    
    private final void retrieverInitData(com.net.pvr1.ui.seatLayout.response.InitResponse.Output output) {
    }
    
    private final void retrieveData(com.net.pvr1.ui.seatLayout.response.SeatResponse.Output data) {
    }
    
    private final void drawColumn(java.util.List<com.net.pvr1.ui.seatLayout.response.SeatResponse.Output.Row> noOfRows) {
    }
    
    private final void drawRow(android.widget.LinearLayout llDrawRow, java.util.List<com.net.pvr1.ui.seatLayout.response.SeatResponse.Output.Row.S> noSeats, java.lang.String areaName, int num) {
    }
    
    private final void addRowName(java.lang.String rowName, boolean space) {
    }
    
    private final void setClick(android.widget.TextView seatView, int num1, int num2) {
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    private final void removeSelectedSeats(com.net.pvr1.ui.seatLayout.response.SeatTagData seat) {
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    private final void calculatePrice() {
    }
    
    private final void addSelectedSeats(com.net.pvr1.ui.seatLayout.response.SeatTagData seat, android.widget.TextView seatView, int num1, int num2) {
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    private final void buttonClicked(android.app.Activity context, android.widget.TextView seatView, int num1, int num2) {
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    private final void removeHC(android.app.Activity context) {
    }
    
    @java.lang.Override()
    public void showsClick(int comingSoonItem) {
    }
}