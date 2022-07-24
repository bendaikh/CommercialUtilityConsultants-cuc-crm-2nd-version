package mycrm.services;

import mycrm.models.MyCallback;

import java.util.Date;
import java.util.List;

public interface MyService {
    List<MyCallback> getMyTodaysCallbacks();

    List<MyCallback> getCallbacksForDate(String callbackSearchDate);

    List<Date> getCallbackOpeningTimes(String callbackSearchDate);
}
