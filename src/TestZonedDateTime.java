import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ZonedDateTime tests
 */
public class TestZonedDateTime {
    public static void main(String[] args) {
        System.out.println(zonedDateTimeOf());
        System.out.println(zonedDateTimeParse());
        System.out.println(zonedDateTimeFormat());
        System.out.println(toPST());
        System.out.println(sameInstantAs());
        System.out.println(sameLocalAs());
    }

    /**
     * Create a {@link ZonedDateTime} with time of 2015-07-10 2:14:25.000 as Japan Standard Time
     * by using {@link ZonedDateTime#of} and {@link ZoneId#of}
     */
    static ZonedDateTime zonedDateTimeOf() {
    	return ZonedDateTime.of(2015, 7, 10, 2, 14, 25, 000, ZoneId.of("Asia/Tokyo"));
    }

    /**
     * Create a {@link ZonedDateTime} with time of 2015-06-18 23:07:25.000 as Japan Standard Time
     * by using {@link ZonedDateTime#parse}
     */
    static ZonedDateTime zonedDateTimeParse() {
    	/*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd H:m:s.SSS, z");
        return ZonedDateTime.parse("2015-06-18 23:07:25.000, Asia/Tokyo", formatter);
        */
        DateTimeFormatter formatter 
            = DateTimeFormatter.ofPattern("y-MM-dd H:m:s.SSS").withZone(ZoneId.of("Asia/Tokyo"));
    	return ZonedDateTime.parse("2015-06-18 23:07:25.000", formatter);
        
    }

    /**
     * Format {@link zdt} to a {@link String} as "2015_06_18_23_07_30_JST"
     * by using {@link ZonedDateTime#format}
     */
    static String zonedDateTimeFormat() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y_MM_d_H_mm_s_z");
        return zdt.format(formatter);
    }

    /**
     * Create a {@link ZonedDateTime} from {@link ldt} with Pacific Standard Time
     */
    static ZonedDateTime toPST() {
        LocalDateTime ldt = DateAndTimes.LDT_20150618_23073050;
        return ZonedDateTime.of(ldt, ZoneId.of("America/Los_Angeles"));
    }

    /**
     * Create a {@link ZonedDateTime} same instant as {@link zdt} with Pacific Standard Time
     * by using {@link ZonedDateTime#withZoneSameInstant}
     */
    static ZonedDateTime sameInstantAs() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;
        return zdt.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
    }

    /**
     * Create a {@link ZonedDateTime} same local time as {@link zdt} with Pacific Standard Time
     * by using {@link ZonedDateTime#withZoneSameLocal}
     */
    static ZonedDateTime sameLocalAs() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;
        return zdt.withZoneSameLocal(ZoneId.of("America/Los_Angeles"));
    }

    static class DateAndTimes {

        public static final LocalDateTime LDT_20150618_23073050 = LocalDateTime.of(2015, 6, 18, 23, 7, 30, 500000000);
        public static final ZonedDateTime
                ZDT_20150618_23073050 = ZonedDateTime.of(LDT_20150618_23073050, ZoneId.of("Asia/Tokyo"));
    }
}
