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

import com.google.common.collect.ImmutableSortedSet;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;
import java.util.NavigableSet;

public class Times {
    public final Integers
            seconds,
            minutes,
            hours,
            months,
            daysOfWeek,
            years,
            daysOfMonth;

    public Times() {
        this.seconds = new Integers();
        this.minutes = new Integers();
        this.hours = new Integers();
        this.months = new Integers();
        this.daysOfWeek = new Integers();
        this.years = new Integers();
        this.daysOfMonth = new Integers();
    }

    public NavigableSet<ZonedDateTime> dateTimes() {
        if (this.seconds.isEmpty()) {
            this.seconds.withRange(0, 1);
        }
        if (this.minutes.isEmpty()) {
            this.minutes.withRange(0, 1);
        }
        if (this.hours.isEmpty()) {
            this.hours.withRange(0, 1);
        }
        if (this.months.isEmpty()) {
            this.months.withRange(1, 2);
        }
        if (this.years.isEmpty()) {
            int thisYear = ZonedDateTime.now().getYear();
            this.years.withRange(thisYear, thisYear + 1);
        }
        ImmutableSortedSet.Builder<ZonedDateTime> builder = ImmutableSortedSet.naturalOrder();
        for (int second : this.seconds) {
            for (int minute : this.minutes) {
                for (int hour : this.hours) {
                    for (int month : this.months) {
                        for (int year : this.years) {
                            ZonedDateTime base = ZonedDateTime.now()
                                    .truncatedTo(ChronoUnit.DAYS)
                                    .withSecond(second)
                                    .withMinute(minute)
                                    .withHour(hour)
                                    .withMonth(month)
                                    .withDayOfMonth(1)
                                    .withYear(year);
                            if (!this.daysOfWeek.isEmpty() && !this.daysOfMonth.isEmpty()) {
                                addDaysOfWeek(builder, base);
                                addDaysOfMonth(builder, base);
                            } else if (!this.daysOfWeek.isEmpty()) {
                                addDaysOfWeek(builder, base);
                            } else if (!this.daysOfMonth.isEmpty()) {
                                addDaysOfMonth(builder, base);
                            } else {
                                builder.add(base);
                            }
                        }
                    }
                }
            }
        }
        return builder.build();
    }

    private void addDaysOfWeek(ImmutableSortedSet.Builder<ZonedDateTime> builder, ZonedDateTime base) {
        Month month = base.getMonth();
        Iterator<Integer> iterator = this.daysOfWeek.iterator();
        base = base.with(DayOfWeek.of(iterator.next()));
        if (base.getMonth() != month) {
            base = base.plusWeeks(1);
        }
        do {
            builder.add(base);
            base = base.plusWeeks(1);
        } while (base.getMonth() == month);
    }

    private void addDaysOfMonth(ImmutableSortedSet.Builder<ZonedDateTime> builder, ZonedDateTime base) {
        for (int day : this.daysOfMonth) {
            if (day <= base.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()) {
                builder.add(base.withDayOfMonth(day));
            }
        }
    }
}
