//import java.sql.Date;
//import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
//import java.time.temporal.TemporalAmount;

/**
 * LocalDate test
 */
public class TestLocalDate {
    public static void main(String[] args) {
        System.out.println(create());
        System.out.println(parse());
        System.out.println(with().getYear());
        System.out.println(withAdjuster());
        System.out.println(plus());
        System.out.println(minus());
        System.out.println(plusPeriod());
        System.out.println(isAfter());
        System.out.println(until());
    }

    /**
 	* Create a {@link LocalDate} of 2015-06-18 by using {@link LocalDate#of}
 	*/
    static LocalDate create() {
    	return LocalDate.of(2015, 06, 18);
    }

    /**
     * Create a {@link LocalDate} of 2015-06-18 from String by using {@link LocalDate#parse}
     */
    static LocalDate parse() {
        return LocalDate.parse("2015-06-18");
    }

    /**
     * Create a {@link LocalDate} from {@link ld} with year 2015
     * by using {@link LocalDate#withYear} or {@link LocalDate#with}
     */
    static LocalDate with() {
        LocalDate ld = DateAndTimes.LD_20150618;
//        return ld.with(TemporalAdjusters.firstDayOfYear());
      return ld.withYear(2015);
    }

    /**
     * Create a {@link LocalDate} from {@link ld} adjusted into first day of next year
     * by using {@link LocalDate#with} and {@link TemporalAdjusters#firstDayOfNextYear}
     */
    static LocalDate withAdjuster() {
        LocalDate ld = DateAndTimes.LD_20150618;
        return ld.with(TemporalAdjusters.firstDayOfNextYear());
    }

    /**
     * Create a {@link LocalDate} from {@link ld} with 10 month later
     * by using {@link LocalDate#plusMonths} or {@link LocalDate#plus}
     */
    static LocalDate plus() {
        LocalDate ld = DateAndTimes.LD_20150618;
//        return ld.plus(Period.ofMonths(10));
        return ld.plusMonths(10); 
    }

    /**
     * Create a {@link LocalDate} from {@link ld} with 10 days before
     * by using {@link LocalDate#minusDays} or {@link LocalDate#minus}
     */
    static LocalDate minus() {
        LocalDate ld = DateAndTimes.LD_20150618;
//        return ld.minus(Period.ofDays(10));
        return ld.minusDays(10);
    }

    /**
     * Define a {@link Period} of 1 year 2 month 3 days
     * Create a {@link LocalDate} adding the period to {@link ld} by using {@link LocalDate#plus}
     */
    static LocalDate plusPeriod() {
        LocalDate ld = DateAndTimes.LD_20150618;
        Period period = Period.of(1, 2, 3);
        return ld.plus(period);
    }

    /**
     * Check whether {@link ld2} is after {@link ld} or not
     * by using {@link LocalDate#isAfter} or {@link LocalDate#isBefore}
     */
    static boolean isAfter() {
        LocalDate ld = DateAndTimes.LD_20150618;
        LocalDate ld2 = DateAndTimes.LD_20150807;
        return ld2.isAfter(ld);
//        return ld.isBefore(ld2);
    }

    /**
     * Create a period from {@link ld} till {@link ld2}
     * by using {@link LocalDate#until}
     */
    static Period until() {
        LocalDate ld = DateAndTimes.LD_20150618;
        LocalDate ld2 = DateAndTimes.LD_20150807;
        return ld.until(ld2);
    }

}

class DateAndTimes {
    public static final LocalDate LD_20150618 = LocalDate.of(2015, 6, 18);
    public static final LocalDate LD_20150807 = LocalDate.of(2015, 8, 7);
}
