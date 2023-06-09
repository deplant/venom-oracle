/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Anders Wisch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tech.deplant.osiris.node.tst.unit.cron;

import tech.deplant.osiris.node.io.cron.CronExpression;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static tech.deplant.osiris.node.tst.unit.cron.DateTimes.*;
import static org.junit.jupiter.api.Assertions.*;

public class CronExpressionTest {
    public static final CronExpression.Parser withSecondsField = CronExpression.parser().withSecondsField(true);
    private static final String formatString = "m H d M E yyyy";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatString);
    private CronExpression expression;

    @Test
    public void testHashCode() {
        assertEquals(CronExpression.daily().hashCode(), CronExpression.parse("@daily").hashCode());
        assertEquals(CronExpression.daily().hashCode(), CronExpression.parse("@midnight").hashCode());
        assertEquals(CronExpression.hourly().hashCode(), CronExpression.parse("@hourly").hashCode());
        assertEquals(CronExpression.monthly().hashCode(), CronExpression.parse("@monthly").hashCode());
        assertEquals(CronExpression.weekly().hashCode(), CronExpression.parse("@weekly").hashCode());
        assertEquals(CronExpression.yearly().hashCode(), CronExpression.parse("@annually").hashCode());
        assertEquals(CronExpression.yearly().hashCode(), CronExpression.parse("@yearly").hashCode());
        assertEquals(
                CronExpression.parse("0 0 ? * 5#3,2#2").hashCode(),
                CronExpression.parse("0 0 ? * 5#3,2#2").hashCode());
        assertEquals(
                withSecondsField.parse("0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI 2002-2010").hashCode(),
                withSecondsField.parse("0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI 2002-2010").hashCode());
    }

    @Test
    public void testEquals() {
        assertEquals(CronExpression.daily(), CronExpression.parse("@daily"));
        assertEquals(CronExpression.daily(), CronExpression.parse("@midnight"));
        assertEquals(CronExpression.hourly(), CronExpression.parse("@hourly"));
        assertEquals(CronExpression.monthly(), CronExpression.parse("@monthly"));
        assertEquals(CronExpression.weekly(), CronExpression.parse("@weekly"));
        assertEquals(CronExpression.yearly(), CronExpression.parse("@annually"));
        assertEquals(CronExpression.yearly(), CronExpression.parse("@yearly"));
        assertEquals(
                CronExpression.parse("0 0 ? * 5#3,2#2"),
                CronExpression.parse("0 0 ? * 5#3,2#2"));
        assertEquals(
                withSecondsField.parse("0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI 2002-2010"),
                withSecondsField.parse("0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI 2002-2010"));
    }

    @Test
    public void illegalCharacter() {
        try {
            this.expression = CronExpression.parse("0 0 4X * *");
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad character 'X' at position 5 in string: 0 0 4X * *", e.getMessage());
        }
    }

    @Test
    public void disallowBothDayFields() {
        try {
            this.expression = CronExpression.parser().allowBothDayFields(false).parse("0 0 1 * 5L");
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Day of month and day of week may not both be specified", e.getMessage());
        }
    }

    @Test
    public void nearestWeekdayWithoutNumber() {
        try {
            this.expression = CronExpression.parse("0 0 W * *");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad character 'W' in day of month field: W", e.getMessage());
        }
    }

    @Test
    public void nearestWeekdayOfMonth() {
        this.expression = CronExpression.parse("0 0 5W * *");
        List<ZonedDateTime> times = new ArrayList<>();
        ZonedDateTime t = DateTimes.startOfYear();
        int year = t.getYear();
        do {
            times.add(nearestWeekday(t.withDayOfMonth(5)));
            t = t.plusMonths(1);
        } while (year == t.getYear());
        assertMatchesAll(times);
    }

    @Test
    public void nearestFriday() {
        ZonedDateTime t = now().truncatedTo(ChronoUnit.DAYS).with(DayOfWeek.SATURDAY);
        this.expression = CronExpression.parse("0 0 " + t.getDayOfMonth() + "W * *");
        assertMatches(t.minusDays(1));
    }

    @Test
    public void nearestMonday() {
        ZonedDateTime t = now().truncatedTo(ChronoUnit.DAYS).with(DayOfWeek.SUNDAY);
        this.expression = CronExpression.parse("0 0 " + t.getDayOfMonth() + "W * *");
        assertMatches(t.plusDays(1));
    }

    @Test
    public void nonMatchingNth() {
        this.expression = CronExpression.parse("0 0 ? * 2#2");
        List<ZonedDateTime> times = new ArrayList<>();
        ZonedDateTime t = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).withDayOfYear(1);
        int year = t.getYear();
        while (t.getYear() == year) {
            times.add(nthOfMonth(t, DayOfWeek.TUESDAY, 1));
            t = t.plusMonths(1);
        }
        for (ZonedDateTime time : times) {
            assertFalse(this.expression.matches(time));
        }
    }

    @Test
    public void multipleNth() {
        this.expression = CronExpression.parse("0 0 ? * 5#3,2#2");
        List<ZonedDateTime> times = new ArrayList<>();
        ZonedDateTime t = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).withDayOfYear(1);
        int year = t.getYear();
        while (t.getYear() == year) {
            times.add(nthOfMonth(t, DayOfWeek.FRIDAY, 3));
            times.add(nthOfMonth(t, DayOfWeek.TUESDAY, 2));
            t = t.plusMonths(1);
        }
        assertMatchesAll(times);
    }

    @Test
    public void thirdFriday() {
        String string = "0 0 ? * 5#3";
        List<ZonedDateTime> times = new ArrayList<>();
        ZonedDateTime t = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).withDayOfYear(1);
        int year = t.getYear();
        while (t.getYear() == year) {
            times.add(nthOfMonth(t, DayOfWeek.FRIDAY, 3));
            t = t.plusMonths(1);
        }
        this.expression = CronExpression.parse(string);
        assertMatchesAll(times);
    }

    @Test
    public void reboot() {
        this.expression = CronExpression.parse("@reboot");
        ZonedDateTime now = now();
        assertTrue(this.expression.matches(now));
        assertFalse(this.expression.matches(now));
    }

    @Test
    public void minuteFullRangeExplicit() {
        this.expression = CronExpression.parse("0-59 * * * * *");
        ZonedDateTime time = startOfHour();
        int hour = time.getHour();
        do {
            assertMatches(time);
            time = time.plusMinutes(1);
        } while (time.getHour() == hour);
    }

    @Test
    public void minuteRestrictedRange() {
        this.expression = CronExpression.parse("10-20 * * * * *");
        int first = 10, last = 20;
        ZonedDateTime time = startOfHour();
        int hour = time.getHour();
        do {
            int minute = time.getMinute();
            assertEquals(first <= minute && minute <= last, this.expression.matches(time));
            time = time.plusMinutes(1);
        } while (time.getHour() == hour);
    }

    @Test
    public void minuteFullRangeMod() {
        this.expression = CronExpression.parse("*/5 * * * * *");
        ZonedDateTime time = startOfHour();
        int hour = time.getHour();
        do {
            int minute = time.getMinute();
            assertEquals(minute % 5 == 0, this.expression.matches(time));
            time = time.plusMinutes(1);
        } while (time.getHour() == hour);
    }

    @Test
    public void minuteRestrictedRangeMod() {
        this.expression = CronExpression.parse("10-20/5 * * * * *");
        int first = 10, last = 20;
        ZonedDateTime time = startOfHour();
        int hour = time.getHour();
        do {
            int minute = time.getMinute();
            assertEquals(first <= minute && minute <= last && minute % 5 == 0, this.expression.matches(time));
            time = time.plusMinutes(1);
        } while (time.getHour() == hour);
    }

    @Test
    public void yearly() {
        this.expression = CronExpression.yearly();
        assertYearly();
    }

    @Test
    public void monthly() {
        this.expression = CronExpression.monthly();
        assertMonthly();
    }

    @Test
    public void weekly() {
        this.expression = CronExpression.weekly();
        assertWeekly();
    }

    @Test
    public void daily() {
        this.expression = CronExpression.daily();
        assertDaily();
    }

    @Test
    public void hourly() {
        this.expression = CronExpression.hourly();
        assertHourly();
    }

    @Test
    public void yearlyKeyword() {
        this.expression = CronExpression.parse("@yearly");
        assertYearly();
    }

    @Test
    public void annualKeyword() {
        this.expression = CronExpression.parse("@annually");
        assertYearly();
    }

    @Test
    public void monthlyKeyword() {
        this.expression = CronExpression.parse("@monthly");
        assertMonthly();
    }

    @Test
    public void weeklyKeyword() {
        this.expression = CronExpression.parse("@weekly");
        assertWeekly();
    }

    @Test
    public void dailyKeyword() {
        this.expression = CronExpression.parse("@daily");
        assertDaily();
    }

    @Test
    public void hourlyKeyword() {
        this.expression = CronExpression.parse("@hourly");
        assertHourly();
    }

    @Test
    public void invalid() {
        assertFalse(CronExpression.isValid(null));
        assertFalse(CronExpression.isValid(""));
        assertFalse(CronExpression.isValid("a"));
        assertFalse(CronExpression.isValid("0 0 1 * X"));
        assertFalse(CronExpression.isValid("0 0 1 * 1X"));
    }

    @Test
    public void invalidDueToSecondsField() {
        assertTrue(CronExpression.isValid("0 0 1 * 1"));
        assertFalse(CronExpression.parser().allowBothDayFields(false).isValid("0 0 1 * 1"));
    }

    private void assertWeekly() {
        for (int week = 1; week <= 52; week++) {
            assertMatches(midnight().withDayOfYear(7 * week).with(DayOfWeek.MONDAY).minusDays(1));
        }
    }

    private void assertDaily() {
        for (int day = 1; day <= 365; day++) {
            assertMatches(midnight().withDayOfYear(day));
        }
    }

    private void assertMonthly() {
        for (Month month : Month.values()) {
            assertMatches(midnight().with(month).withDayOfMonth(1));
        }
    }

    private void assertHourly() {
        for (int day = 1; day <= 365; day++) {
            for (int hour = 0; hour <= 23; hour++) {
                assertMatches(midnight().withDayOfYear(day).withHour(hour));
            }
        }
    }

    private void assertYearly() {
        assertMatches(midnight().withDayOfYear(1));
    }

    private void assertMatchesAll(List<ZonedDateTime> times) {
        for (ZonedDateTime time : times) {
            assertMatches(time);
        }
    }

    private void assertMatches(ZonedDateTime time) {
        assertTrue(
                this.expression.matches(time),
                time.format(formatter).toUpperCase() + " doesn't match expression: " + this.expression
        );
    }
}
