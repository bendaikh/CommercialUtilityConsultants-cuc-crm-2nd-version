package mycrm.services;

import mycrm.configuration.LogType;
import mycrm.functions.UserHelper;
import mycrm.models.Broker;
import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.models.MyCallback;
import mycrm.models.User;
import mycrm.models.UtilityContract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Primary
public class MyServiceImpl implements MyService {
    private static final Logger logger = LogManager.getLogger();

    private final GasContractService gasContractService;
    private final ElecContractService elecContractService;
    private final UtilityContractService utilityContractService;
    private final UserHelper userHelper;

    @Autowired
    public MyServiceImpl(GasContractService gasContractService,
                         ElecContractService elecContractService,
                         UtilityContractService utilityContractService,
                         UserHelper userHelper) {
        this.gasContractService = gasContractService;
        this.elecContractService = elecContractService;
        this.utilityContractService = utilityContractService;
        this.userHelper = userHelper;
    }

    @Override
    public List<MyCallback> getMyTodaysCallbacks() {
        User loggedInUser = userHelper.getLoggedInUser();
        if (loggedInUser.getBroker() == null) {
            return new ArrayList<>();
        }

        return buildMyCallbacksForToday(loggedInUser);
    }

    @Override
    public List<MyCallback> getCallbacksForDate(String callbackSearchDate) {

        User loggedInUser = userHelper.getLoggedInUser();

        if (loggedInUser.getBroker() == null) {
            return new ArrayList<>();
        }

        List<MyCallback> myCallbacks = new ArrayList<>();

        Map<Date, MyCallback> mapOfCallbacks = buildMapFromCallbacks(loggedInUser.getBroker(), callbackSearchDate);

        mapOfCallbacks.forEach((time, myCallback) -> myCallbacks.add(MyCallback
                .builder()
                .callbackTime(time)
                .businessName(myCallback != null ? myCallback.getBusinessName() : null)
                .customerSite(myCallback != null ? myCallback.getCustomerSite() : null)
                .build()));

        myCallbacks.sort(Comparator.comparing(MyCallback::getCallbackTime));

        return myCallbacks;
    }

    private Map<Date, MyCallback> buildMapFromCallbacks(Broker broker, String callbackSearchDate) {
        Map<Date, MyCallback> mapOfCallbacks = new HashMap<>();

        getCallbackOpeningTimes(callbackSearchDate).forEach(time -> {
            MyCallback callback = buildMyCallbacksByDate(broker, callbackSearchDate).stream()
                    .filter(myCallback -> time.equals(myCallback.getCallbackTime()))
                    .findAny()
                    .orElse(null);

            mapOfCallbacks.put(time, callback);
        });

        return mapOfCallbacks;
    }

    private Set<MyCallback> buildMyCallbacksByDate(Broker broker, String callbackSearchDate) {
        List<MyCallback> myCallbacks = new ArrayList<>();
        myCallbacks.addAll(buildMyGasCallbacks(getGasCallbacksByDate(broker, getDateFromString(callbackSearchDate))));
        myCallbacks.addAll(buildMyElectricCallbacks(getElectricCallbacksByDate(broker, getDateFromString(callbackSearchDate))));
        myCallbacks.addAll(buildMyUtilityCallbacks(getUtilityCallbacksByDate(broker, getDateFromString(callbackSearchDate))));

        myCallbacks.sort(Comparator.comparing(MyCallback::getCallbackTime).reversed());

        return new HashSet<>(myCallbacks);
    }

    private List<MyCallback> buildMyCallbacksForToday(User loggedInUser) {
        List<MyCallback> myCallbacks = new ArrayList<>();
        myCallbacks.addAll(buildMyGasCallbacks(getGasCallbacksByDate(loggedInUser.getBroker(), new Date())));
        myCallbacks.addAll(buildMyElectricCallbacks(getElectricCallbacksByDate(loggedInUser.getBroker(), new Date())));
        myCallbacks.addAll(buildMyUtilityCallbacks(getUtilityCallbacksByDate(loggedInUser.getBroker(), new Date())));

        myCallbacks.sort(Comparator.comparing(MyCallback::getCallbackTime));

        return myCallbacks;
    }

    private List<MyCallback> buildMyGasCallbacks(List<GasCustomerContract> contracts) {
        List<MyCallback> myCallbacks = new ArrayList<>();
        contracts.forEach(gasCustomerContract -> myCallbacks.add(MyCallback
                .builder()
                .callbackTime(gasCustomerContract.getCallbackDateTime())
                .businessName(gasCustomerContract.getCustomer().getBusinessName())
                .customerSite(gasCustomerContract.getCustomerSite())
                .build()));

        return myCallbacks;
    }

    private List<MyCallback> buildMyElectricCallbacks(List<ElecCustomerContract> contracts) {
        List<MyCallback> myCallbacks = new ArrayList<>();
        contracts.forEach(elecCustomerContract -> myCallbacks.add(MyCallback
                .builder()
                .callbackTime(elecCustomerContract.getCallbackDateTime())
                .businessName(elecCustomerContract.getCustomer().getBusinessName())
                .customerSite(elecCustomerContract.getCustomerSite())
                .build()));

        return myCallbacks;
    }

    private List<MyCallback> buildMyUtilityCallbacks(List<UtilityContract> contracts) {
        List<MyCallback> myCallbacks = new ArrayList<>();
        contracts.forEach(utilityContract -> myCallbacks.add(MyCallback
                .builder()
                .callbackTime(utilityContract.getCallbackDateTime())
                .businessName(utilityContract.getCustomerSite().getCustomer().getBusinessName())
                .customerSite(utilityContract.getCustomerSite())
                .build()));

        return myCallbacks;
    }

    private List<GasCustomerContract> getGasCallbacksByDate(Broker broker, Date callbackSearchDate) {
        return gasContractService.findByLogTypeAndBrokerAndCallbackDate(broker, callbackSearchDate, LogType.CALLBACK);
    }

    private List<ElecCustomerContract> getElectricCallbacksByDate(Broker broker, Date callbackSearchDate) {
        return elecContractService.findByLogTypeAndBrokerAndCallbackDate(broker, callbackSearchDate, LogType.CALLBACK);
    }

    private List<UtilityContract> getUtilityCallbacksByDate(Broker broker, Date callbackSearchDate) {
        return utilityContractService.findByLogTypeAndBrokerAndCallbackDate(broker, callbackSearchDate, LogType.CALLBACK);
    }

    private Date getDateFromString(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ignored) {

        }
        return new Date();
    }

    private Date convertLocalDateTimeToDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public List<Date> getCallbackOpeningTimes(String callbackSearchDate) {

        List<Date> times = new ArrayList<>();

        LocalTime startTime = LocalTime.of(7, 59);
        LocalDate startDate = LocalDate.parse(callbackSearchDate);

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        int minute = startDateTime.getMinute();

        if (minute > 45) {
            startDateTime = startDateTime.plusHours(1L).withMinute(0);
        } else {
            // keep this in should the time change or be passable as an argument
            startDateTime = startDateTime.withMinute(minute < 30 ? minute < 15 ? 15 : 30 : 45);
        }

        Stream.iterate(startDateTime.truncatedTo(ChronoUnit.MINUTES), t -> t.plusMinutes(15L))
                .limit(40)
                .forEach(localDateTime -> times.add(convertLocalDateTimeToDate(localDateTime)));

        return times;
    }
}
