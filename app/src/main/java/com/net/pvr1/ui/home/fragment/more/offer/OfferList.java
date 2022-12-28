package com.net.pvr1.ui.home.fragment.more.offer;

import com.net.pvr1.ui.home.fragment.more.offer.response.MOfferResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfferList implements Serializable {
    String cat;
    ArrayList<MOfferResponse.Output.Offer> list;

    public ArrayList<MOfferResponse.Output.Offer> getList() {
        return list;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getCat() {
        return cat;
    }

    public void setList(ArrayList<MOfferResponse.Output.Offer> list) {
        this.list = list;
    }
}
