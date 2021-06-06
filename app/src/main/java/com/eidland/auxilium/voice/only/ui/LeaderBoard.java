package com.eidland.auxilium.voice.only.ui;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.eidland.auxilium.voice.only.model.Gift;
import com.eidland.auxilium.voice.only.model.Leader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoard {
    static long time = 60 * 60000;
    List<String> uniqueContributor = new ArrayList<String>();
    List<Leader> contributors = new ArrayList<Leader>();
    List<String> uniqueWinner = new ArrayList<String>();
    List<Leader> winners = new ArrayList<Leader>();
    List<Gift> giftList = new ArrayList<Gift>();

    public LeaderBoard(List<Gift> giftList) {
        uniqueWinner.clear();
        contributors.clear();
        uniqueContributor.clear();
        winners.clear();
        this.giftList.clear();
        for (Gift gift : giftList) {
            if ((gift.time + time) > System.currentTimeMillis()) {
                this.giftList.add(gift);

                if (!uniqueContributor.contains(gift.senderUID)) {
                    uniqueContributor.add(gift.senderUID);
                    contributors.add(new Leader(gift.senderName, gift.giftValue, gift.senderImg, gift.senderUID));
                } else {
                    for (Leader lead : contributors) {
                        if (lead.uid.equals(gift.senderUID)) {
                            lead.coins = lead.coins + gift.giftValue;
                        }
                    }
                }
                if (!uniqueWinner.contains(gift.receiverUID)) {
                    uniqueWinner.add(gift.receiverUID);
                    winners.add(new Leader(gift.receiverName, gift.giftValue, gift.receiverImg, gift.receiverUID));
                } else {
                    for (Leader lead : winners) {
                        if (lead.uid.equals(gift.receiverUID)) {
                            lead.coins = lead.coins + gift.giftValue;
                        }
                    }
                }
            }
        }
    }


    List<Leader> getTopContributor() {

        Collections.sort(contributors, new Comparator<Leader>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public int compare(Leader obj1, Leader obj2) {
                return Long.compare(obj2.coins, obj1.coins);
            }
        });
        return contributors;
    }

    List<Leader> getTopWinner() {

        Collections.sort(winners, new Comparator<Leader>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public int compare(Leader obj1, Leader obj2) {
                return Long.compare(obj2.coins, obj1.coins);
            }
        });
        return winners;
    }

}
