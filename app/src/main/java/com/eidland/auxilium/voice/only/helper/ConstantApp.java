package com.eidland.auxilium.voice.only.helper;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.AnimationItem;
import com.eidland.auxilium.voice.only.model.CardDeck;
import com.eidland.auxilium.voice.only.model.GiftItem;

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


    public static List<GiftItem> giftList() {
        List<GiftItem> giftItems = new ArrayList<>();

        giftItems.add(new GiftItem("like1",1, R.drawable.gift_red,"Knowledge"));
        giftItems.add(new GiftItem("smilereact",1, R.drawable.gift_turquoise,""));
        giftItems.add(new GiftItem("pigions",1, R.drawable.gift_pink,""));
        giftItems.add(new GiftItem("oscar",1, R.drawable.gift_purple,""));
        giftItems.add(new GiftItem("heartcomment",1, R.drawable.gift_yellow,"Wisdom"));
        giftItems.add(new GiftItem("medal",1, R.drawable.gift_cyan,"Brave"));
        giftItems.add(new GiftItem("debate",1, R.drawable.gift_stone,"Love"));
        giftItems.add(new GiftItem("cake2",1, R.drawable.gift_cake_2,""));
        giftItems.add(new GiftItem("donut",1, R.drawable.gifts_donut,""));
        giftItems.add(new GiftItem("crown2",1, R.drawable.gift_crown_2,""));
        giftItems.add(new GiftItem("crown3",1, R.drawable.gift_crown_3,""));
        giftItems.add(new GiftItem("necklace2",1, R.drawable.gift_necklace_2,""));
//        giftItems.add(new GiftItem("cookie",1, R.drawable.gift_cookie,""));
        giftItems.add(new GiftItem("sweets2",1, R.drawable.gift_sweets_2,""));
        giftItems.add(new GiftItem("trophy",1, R.drawable.gift_trophy,""));

        return giftItems;
    }

    public static List<AnimationItem> animationItems() {
        List<AnimationItem> animationItemList = new ArrayList<>();

        animationItemList.add(new AnimationItem("like1", R.drawable.gift_red, "gift_red.json", R.drawable.gift_red_svg));
        animationItemList.add(new AnimationItem("smilereact", R.drawable.gift_turquoise, "gift_turquoise.json", R.drawable.gift_turquoise_svg));
        animationItemList.add(new AnimationItem("pigions", R.drawable.gift_pink, "gift_pink.json", R.drawable.gift_pink_svg));
        animationItemList.add(new AnimationItem("oscar", R.drawable.gift_purple, "gift_purple.json", R.drawable.gift_purple_svg));
        animationItemList.add(new AnimationItem("heartcomment", R.drawable.gift_yellow, "gift_yellow.json", R.drawable.gift_yellow_svg));
        animationItemList.add(new AnimationItem("medal", R.drawable.gift_cyan, "gift_cyan.json", R.drawable.gift_cyan_svg));
        animationItemList.add(new AnimationItem("debate", R.drawable.gift_stone, "gift_stone.json", R.drawable.gift_stone_svg));
        animationItemList.add(new AnimationItem("cake2", R.drawable.gift_cake_2, "gift_cake_2.json", R.drawable.gift_cake_2_svg));
        animationItemList.add(new AnimationItem("donut", R.drawable.gifts_donut, "gift_donut.json", R.drawable.gifts_donut_svg));
        animationItemList.add(new AnimationItem("crown2", R.drawable.gift_crown_2, "gift_crown_2.json", R.drawable.gift_crown_3_svg));
        animationItemList.add(new AnimationItem("crown3", R.drawable.gift_crown_3, "gift_crown_3.json", R.drawable.gift_crown_3_svg));
        animationItemList.add(new AnimationItem("necklace2", R.drawable.gift_necklace_2, "gift_necklace_2.json", R.drawable.gift_necklace_2_svg));
//        animationItemList.add(new AnimationItem("cookie", R.drawable.gift_cookie, "gift_cookie.json", R.drawable.gift_sweets_2_svg));
        animationItemList.add(new AnimationItem("sweets2", R.drawable.gift_sweets_2, "gift_sweets_2.json", R.drawable.gift_sweets_2_svg));
        animationItemList.add(new AnimationItem("trophy", R.drawable.gift_trophy, "gift_trophy.json", R.drawable.gift_trophy_svg));

        return animationItemList;
    }

    public static List<CardDeck> gameList() {
        List<CardDeck> randomDeck = new ArrayList<>();
        randomDeck.add(new CardDeck("In The Shoes", "https://auxiliumlivestreaming.000webhostapp.com/card_decks/10.png"));

        return randomDeck;
    }
}
