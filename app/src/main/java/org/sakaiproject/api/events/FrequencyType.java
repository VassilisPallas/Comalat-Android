package org.sakaiproject.api.events;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by vasilis on 11/5/15.
 * Type of event frequency
 */
public enum FrequencyType implements Serializable {

    MW, STT, SMW, SMTW, MWF, TTh, day, week, month, year;

    @Override
    public String toString(){
        switch(this){
            case MW :
                return "MW";
            case STT :
                return "STT";
            case SMW :
                return "SMW";
            case SMTW :
                return "SMTW";
            case MWF :
                return "MWF";
            case TTh :
                return "TTh";
            case day :
                return "day";
            case week :
                return "week";
            case month :
                return "month";
            case year :
                return "year";
        }
        return null;
    }
}
