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


    public static List<GiftItem> giftList(){
        List<GiftItem> giftItems = new ArrayList<>();
        giftItems.add(new GiftItem("hearts",1, R.drawable.gift_diamond,"Knowledge"));
        giftItems.add(new GiftItem("like1",1, R.drawable.gift_red,""));
        giftItems.add(new GiftItem("smilereact",1, R.drawable.gift_turquoise,""));
        giftItems.add(new GiftItem("pigions",1, R.drawable.gift_pink,""));
        giftItems.add(new GiftItem("oscar",1, R.drawable.gift_purple,""));
        giftItems.add(new GiftItem("heartcomment",1, R.drawable.gift_yellow,"Wisdom"));
        giftItems.add(new GiftItem("like2",1, R.drawable.gift_daco,"Humour"));
        giftItems.add(new GiftItem("star",1, R.drawable.gift_green,""));
        giftItems.add(new GiftItem("medal",1, R.drawable.gift_cyan,"Brave"));
        giftItems.add(new GiftItem("fire",1, R.drawable.gift_light_red_svg,""));
        giftItems.add(new GiftItem("debate",1, R.drawable.gift_stone,"Love"));
//        giftItems.add(new GiftItem("carousel",1, R.drawable.ic_gift___carousel,""));
//        giftItems.add(new GiftItem("crown",1, R.drawable.ic_gift___crown2,""));
//        giftItems.add(new GiftItem("castle",1, R.drawable.ic_gift___sand_castle,""));
//        giftItems.add(new GiftItem("pigions",1, R.drawable.ic_gift___pigeon2,""));
//        giftItems.add(new GiftItem("oscar",1, R.drawable.ic_gift___oscar2,""));
        return giftItems;
    }
    public static List<AnimationItem> animationItems(){
        List<AnimationItem> animationItemList = new ArrayList<>();

        animationItemList.add(new AnimationItem("hearts", R.drawable.gift_diamond, "gift_stone.json", R.drawable.gift_diamond_svg));
        animationItemList.add(new AnimationItem("like1", R.drawable.gift_red, "gift_red.json", R.drawable.gift_red_svg));
        animationItemList.add(new AnimationItem("smilereact", R.drawable.gift_turquoise, "gift_turquoise.json", R.drawable.gift_turquoise_svg));
        animationItemList.add(new AnimationItem("pigions", R.drawable.gift_pink, "gift_pink.json", R.drawable.gift_pink_svg));
        animationItemList.add(new AnimationItem("oscar", R.drawable.gift_purple, "gift_purple.json", R.drawable.gift_purple_svg));
        animationItemList.add(new AnimationItem("heartcomment", R.drawable.gift_yellow, "gift_yellow.json", R.drawable.gift_yellow_svg));
        animationItemList.add(new AnimationItem("like2", R.drawable.gift_daco, "gift_cyan.json", R.drawable.gift_daco_svg));
        animationItemList.add(new AnimationItem("star", R.drawable.gift_green, "gift_green.json", R.drawable.gift_stone_svg));
        animationItemList.add(new AnimationItem("medal", R.drawable.gift_cyan, "gift_cyan.json", R.drawable.gift_cyan_svg));
        animationItemList.add(new AnimationItem("fire", R.drawable.gift_light_red_svg, "gift_red.json", R.drawable.gift_light_red_svg));
        animationItemList.add(new AnimationItem("debate", R.drawable.gift_stone, "gift_stone.json", R.drawable.gift_stone_svg));
//        animationItemList.add(new AnimationItem("castle", R.drawable.ic_sand_castle, R.drawable.castle_gif));
//        animationItemList.add(new AnimationItem("crown", R.drawable.ic_crown, R.drawable.crown_gif));
//        animationItemList.add(new AnimationItem("carousel", R.drawable.ic_carousel, R.drawable.heart_fill_gif));
//        animationItemList.add(new AnimationItem("championbelt", R.drawable.ic_champion_belt, R.drawable.heart_fill_gif));
//        animationItemList.add(new AnimationItem("clap", R.drawable.ic_clapping, R.drawable.clap_gif));

        return animationItemList;
    }
    public static List<CardDeck> gameList(){
        List<CardDeck> randomDeck = new ArrayList<>();
        randomDeck.add(new CardDeck("In The Shoes", "https://auxiliumlivestreaming.000webhostapp.com/card_decks/10.png"));

        return randomDeck;
    }
}
