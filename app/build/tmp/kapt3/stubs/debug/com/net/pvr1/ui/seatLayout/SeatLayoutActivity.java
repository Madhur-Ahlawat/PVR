package com.net.pvr1.ui.seatLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.food.FoodActivity;
import com.net.pvr1.ui.seatLayout.adapter.CinemaShowsAdapter;
import com.net.pvr1.ui.seatLayout.adapter.ShowsAdapter;
import com.net.pvr1.ui.seatLayout.request.ReserveSeatRequest;
import com.net.pvr1.ui.seatLayout.response.*;
import com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel;
import com.net.pvr1.ui.summery.SummeryActivity;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import java.math.BigDecimal;

@kotlin.Suppress(names = {"DEPRECATION", "NAME_SHADOWING"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u00d2\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020\u001e2\u0006\u0010F\u001a\u00020\u0011H\u0002J(\u0010G\u001a\u00020D2\u0006\u0010H\u001a\u00020&2\u0006\u0010I\u001a\u0002092\u0006\u0010J\u001a\u00020\u000e2\u0006\u0010K\u001a\u00020\u000eH\u0002J(\u0010L\u001a\u00020D2\u0006\u0010M\u001a\u00020N2\u0006\u0010I\u001a\u0002092\u0006\u0010J\u001a\u00020\u000e2\u0006\u0010K\u001a\u00020\u000eH\u0003J\b\u0010O\u001a\u00020DH\u0003J\b\u0010P\u001a\u00020DH\u0002J\u0010\u0010Q\u001a\u00020D2\u0006\u0010R\u001a\u00020\u000eH\u0016J\u0016\u0010S\u001a\u00020D2\f\u0010T\u001a\b\u0012\u0004\u0012\u00020#0UH\u0002J.\u0010V\u001a\u00020D2\u0006\u0010W\u001a\u00020X2\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020Z0U2\u0006\u0010[\u001a\u00020\u001e2\u0006\u0010\\\u001a\u00020\u000eH\u0002J\b\u0010]\u001a\u00020DH\u0002J\b\u0010^\u001a\u00020DH\u0002J\u0012\u0010_\u001a\u00020D2\b\u0010`\u001a\u0004\u0018\u00010aH\u0014J\u0010\u0010b\u001a\u00020D2\u0006\u0010M\u001a\u00020NH\u0003J\u0010\u0010c\u001a\u00020D2\u0006\u0010H\u001a\u00020&H\u0003J\b\u0010d\u001a\u00020DH\u0002J\u0010\u0010e\u001a\u00020D2\u0006\u0010f\u001a\u00020gH\u0002J\u0010\u0010h\u001a\u00020D2\u0006\u0010i\u001a\u00020jH\u0002J\u0010\u0010k\u001a\u00020D2\u0006\u0010i\u001a\u00020lH\u0002J\b\u0010m\u001a\u00020DH\u0002J\b\u0010n\u001a\u00020DH\u0002J \u0010o\u001a\u00020D2\u0006\u0010I\u001a\u0002092\u0006\u0010J\u001a\u00020\u000e2\u0006\u0010K\u001a\u00020\u000eH\u0002J\b\u0010p\u001a\u00020DH\u0002J\u0010\u0010q\u001a\u00020D2\u0006\u0010R\u001a\u00020\u000eH\u0016R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\"\u001a\u0016\u0012\u0004\u0012\u00020#\u0018\u00010\u0013j\n\u0012\u0004\u0012\u00020#\u0018\u0001`$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020&0\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\'\u001a\u0010\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020)\u0018\u00010(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010,\u001a\u0016\u0012\u0004\u0012\u00020\u001e\u0018\u00010\u0013j\n\u0012\u0004\u0012\u00020\u001e\u0018\u0001`$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010.\u001a\b\u0012\u0004\u0012\u00020/0\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u00100\u001a\b\u0012\u0004\u0012\u0002010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u00102\u001a\u0016\u0012\u0004\u0012\u000203\u0018\u00010\u0013j\n\u0012\u0004\u0012\u000203\u0018\u0001`$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u00104\u001a\u0016\u0012\u0004\u0012\u000203\u0018\u00010\u0013j\n\u0012\u0004\u0012\u000203\u0018\u0001`$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u00106\u001a\b\u0012\u0004\u0012\u0002070\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u00108\u001a\u0004\u0018\u000109X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b:\u0010;\"\u0004\b<\u0010=R\u001a\u0010>\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010@\"\u0004\bA\u0010B\u00a8\u0006r"}, d2 = {"Lcom/net/pvr1/ui/seatLayout/SeatLayoutActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/seatLayout/adapter/ShowsAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/seatLayout/adapter/CinemaShowsAdapter$RecycleViewItemClickListener;", "()V", "authViewModel", "Lcom/net/pvr1/ui/seatLayout/viewModel/SeatLayoutViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/seatLayout/viewModel/SeatLayoutViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivitySeatLayoutBinding;", "caCount", "", "caCount1", "caSeat", "", "cinemaSessionShows", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse$Child$Mv$Ml$S;", "coupleSeat", "flagHc", "flagHc1", "hcCount", "hcCount1", "hcSeat", "hcSeat1", "isDit", "keyData", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "messageText", "noOfRowsSmall", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output$Row;", "Lkotlin/collections/ArrayList;", "noOfSeatsSelected", "Lcom/net/pvr1/ui/seatLayout/response/SeatTagData;", "priceMap", "", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output$PriceList$Price;", "priceVal", "", "seatsN", "selectBuddy", "selectSeatPriceCode", "Lcom/net/pvr1/ui/seatLayout/request/ReserveSeatRequest$Seat;", "selectedSeats", "Lcom/net/pvr1/ui/seatLayout/response/Seat;", "selectedSeats1", "Lcom/net/pvr1/ui/seatLayout/response/SeatHC;", "selectedSeatsBox", "sessionId", "showsArray", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse$Output$Cinema$Child$Sw$S;", "textTermsAndCondition", "Landroid/widget/TextView;", "getTextTermsAndCondition", "()Landroid/widget/TextView;", "setTextTermsAndCondition", "(Landroid/widget/TextView;)V", "tncValue", "getTncValue", "()I", "setTncValue", "(I)V", "addRowName", "", "rowName", "space", "addSelectedSeats", "seat", "seatView", "num1", "num2", "buttonClicked", "context", "Landroid/app/Activity;", "calculatePrice", "cinemaShows", "cinemaShowsClick", "comingSoonItem", "drawColumn", "noOfRows", "", "drawRow", "llDrawRow", "Landroid/widget/LinearLayout;", "noSeats", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output$Row$S;", "areaName", "num", "initTrans", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "removeHC", "removeSelectedSeats", "reserveSeat", "retrieveData", "data", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse$Output;", "retrieverInitData", "output", "Lcom/net/pvr1/ui/seatLayout/response/InitResponse$Output;", "retrieverReserveSeatData", "Lcom/net/pvr1/ui/seatLayout/response/ReserveSeatResponse$Output;", "seatLayout", "seatTandCDialog", "setClick", "shows", "showsClick", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SeatLayoutActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.seatLayout.adapter.ShowsAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.seatLayout.adapter.CinemaShowsAdapter.RecycleViewItemClickListener {
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
    private java.util.ArrayList<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Child.Mv.Ml.S> cinemaSessionShows;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView textTermsAndCondition;
    private int tncValue = 1;
    
    public SeatLayoutActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel getAuthViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.widget.TextView getTextTermsAndCondition() {
        return null;
    }
    
    public final void setTextTermsAndCondition(@org.jetbrains.annotations.Nullable()
    android.widget.TextView p0) {
    }
    
    public final int getTncValue() {
        return 0;
    }
    
    public final void setTncValue(int p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final void shows() {
    }
    
    private final void cinemaShows() {
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
    
    private final void seatTandCDialog() {
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
    
    @java.lang.Override()
    public void cinemaShowsClick(int comingSoonItem) {
    }
}