package utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class TimeUtil {

    static LocalDateTime gmtNow = LocalDateTime.now(ZoneId.of("Greenwich"));

    private static LocalDateTime getTestTime() {
        LocalDateTime time = null;
        time = gmtNow.plusHours(3);
        time = time.plusMinutes(30);
        return time;
    }

    private static HashMap<String, Integer> getDate(String timestamp) {
        String[] dateTimeArray = timestamp.split("\\s+");//Split where at least one whitespace is encountered
        String[] dateArray = dateTimeArray[0].split("-");//Split the date values where there is a hyphen

        HashMap<String, Integer> dateMap = new HashMap<>();
        dateMap.put("year", Integer.parseInt(dateArray[0]));
        dateMap.put("month", Integer.parseInt(dateArray[1]));
        dateMap.put("day", Integer.parseInt(dateArray[2]));

        return dateMap;
    }

    private static HashMap<String, Integer> getTime(String timestamp) {
        String[] dateTimeArray = timestamp.split("\\s+");//Split where at least one whitespace is encountered
        String[] timeArray = dateTimeArray[1].split(":");//Split the time values where there is a colon

        HashMap<String, Integer> timeMap = new HashMap<>();
        timeMap.put("hour", Integer.parseInt(timeArray[0]));
        timeMap.put("minute", Integer.parseInt(timeArray[1]));
        timeMap.put("second", Integer.parseInt(timeArray[1]));

        return timeMap;
    }

    public static String getLocalTimeFromTimestamp(String timestamp) {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();//Get the client's timezone
        //Get the offset to see how much the client's timezone deviates from GMT -> divide to convert from ms to hours
        int offset = timeZone.getOffset(System.currentTimeMillis()) / (1000 * 60 * 60);

        HashMap<String, Integer> time = getTime(timestamp);
        HashMap<String, Integer> date = getDate(timestamp);

        LocalDateTime localTime = null;
        LocalDateTime serverTime = LocalDateTime.of(
                date.get("year"), date.get("month"), date.get("day"),
                time.get("hour"), time.get("minute"), time.get("second")
        );

        if (offset > 0) { //For timezones greater than GMT add the
            localTime = serverTime.plusHours(offset);
        } else if (offset < 0) { //For timezones less than GMT
            //Use the absolute value so the negatives don't cancel each other out
            localTime = serverTime.minusHours(Math.abs(offset));
        } else {//If the offset is zero, then the client is at GMT as well
            localTime = serverTime;
        }

        return toUSFormat(localTime); //To convert from LocalDateTime to String
    }

    public static String toUSFormat(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }

    public static String getLocalTimeNow() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();//Get the client's timezone
        //Get the offset to see how much the client's timezone deviates from GMT -> divide to convert from ms to hours
        int offset = timeZone.getOffset(System.currentTimeMillis()) / (1000 * 60 * 60);

        LocalDateTime localTime = null;

        if (offset > 0) { //For timezones greater than GMT add the
            localTime = gmtNow.plusHours(offset);
        } else if (offset < 0) { //For timezones less than GMT
            //Use the absolute value so the negatives don't cancel each other out
            localTime = gmtNow.minusHours(Math.abs(offset));
        } else {//If the offset is zero, then the client is at GMT as well
            localTime = gmtNow;
        }

        return toUSFormat(localTime); //To convert from LocalDateTime to String
    }

    public static int getCurrentTimeStampInMilli() {
        //method 1
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp.getTime());
        return (int) timestamp.getTime();
    }

//    public static void main(String[] args) {
//        System.out.println(getLocalTimeNow());
//    }
}
