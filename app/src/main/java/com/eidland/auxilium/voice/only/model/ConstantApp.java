package com.eidland.auxilium.voice.only.model;

import com.eidland.auxilium.voice.only.R;

import java.util.ArrayList;
import java.util.List;

public class ConstantApp {
    public static final String APP_BUILD_DATE = "today";

    public static final int BASE_VALUE_PERMISSION = 0X0001;
    public static final int PERMISSION_REQ_ID_RECORD_AUDIO = BASE_VALUE_PERMISSION + 1;
    public static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = BASE_VALUE_PERMISSION + 3;

    public static class PrefManager {
        public static final String PREF_PROPERTY_UID = "pOCXx_uid";
    }

    public static final String ACTION_KEY_CROLE = "C_Role";
    public static final String ACTION_KEY_ROOM_NAME = "ecHANEL";

    public static class AppError {
        public static final int NO_NETWORK_CONNECTION = 3;
    }

    public static List<GiftItem> giftList(){
        List<GiftItem> giftItems = new ArrayList<>();
        giftItems.add(new GiftItem("smilereact",3, R.drawable.ic_gift___heart_1));
        giftItems.add(new GiftItem("hearts",30, R.drawable.ic_gift___heart_3));
        giftItems.add(new GiftItem("heartcomment",15, R.drawable.ic_gift___heart_2));
        giftItems.add(new GiftItem("like1",1, R.drawable.ic_gift___like_1));
        giftItems.add(new GiftItem("like2",5, R.drawable.ic_gift___like_2));
        giftItems.add(new GiftItem("clap",10, R.drawable.ic_gift___clapping));
        giftItems.add(new GiftItem("star",50, R.drawable.ic_gift___star));
        giftItems.add(new GiftItem("medal",20, R.drawable.ic_gift___medal));
        giftItems.add(new GiftItem("fire",75, R.drawable.ic_gift___fire));
        giftItems.add(new GiftItem("debate",25, R.drawable.ic_gift___debate));
        giftItems.add(new GiftItem("championbelt",100, R.drawable.ic_gift___champion_belt));
        giftItems.add(new GiftItem("carousel",69, R.drawable.ic_gift___carousel));
        giftItems.add(new GiftItem("crown",1000, R.drawable.ic_gift___crown2));
        giftItems.add(new GiftItem("castle",999, R.drawable.ic_gift___sand_castle));
        giftItems.add(new GiftItem("pigions",99, R.drawable.ic_gift___pigeon2));
        giftItems.add(new GiftItem("oscar",500, R.drawable.ic_gift___oscar2));
        return giftItems;
    }
}
