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
package tech.deplant.osiris.node.io.cron;

import java.time.ZonedDateTime;

public class DefaultCronExpression extends CronExpression {
	public final String string;
	public final TimeField second,
			minute,
			hour,
			month,
			year;
	public final DayOfWeekField dayOfWeek;
	public final DayOfMonthField dayOfMonth;

	protected DefaultCronExpression(String s, boolean seconds, boolean oneBasedDayOfWeek, boolean allowBothDayFields) {
		this.string = s;
		s = s.toUpperCase();
		Tokens tokens = new Tokens(s);
		if (seconds) {
			this.second = DefaultField.parse(tokens, 0, 59);
		} else {
			this.second = MatchAllField.instance;
		}
		this.minute = DefaultField.parse(tokens, 0, 59);
		this.hour = DefaultField.parse(tokens, 0, 23);
		this.dayOfMonth = DayOfMonthField.parse(tokens);
		this.month = MonthField.parse(tokens);
		this.dayOfWeek = DayOfWeekField.parse(tokens, oneBasedDayOfWeek);
		if (tokens.hasNext()) {
			this.year = DefaultField.parse(tokens, 0, 0);
		} else {
			this.year = MatchAllField.instance;
		}
		if (!allowBothDayFields && !this.dayOfMonth.isUnspecified() && !this.dayOfWeek.isUnspecified()) {
			throw new IllegalArgumentException("Day of month and day of week may not both be specified");
		}
	}

	@Override
	public boolean matches(ZonedDateTime t) {
		return this.second.contains(t.getSecond())
		       && this.minute.contains(t.getMinute())
		       && this.hour.contains(t.getHour())
		       && this.month.contains(t.getMonthValue())
		       && this.year.contains(t.getYear())
		       && this.dayOfWeek.matches(t)
		       && this.dayOfMonth.matches(t);
	}

	@Override
	public String minuteOfCron() {
		return this.minute.numbers().toString();
	}

	@Override
	public String hourOfCron() {
		return this.hour.numbers().toString();
	}

	@Override
	public String dayOfCron() {
		return this.dayOfMonth.numbers.toString();
	}

	@Override
	public String monthOfCron() {
		return this.month.numbers().toString();
	}

	@Override
	public String dayWeekOfCron() {
		return this.dayOfWeek.numbers.toString();
	}

	@Override
	public int hashCode() {
		int result = this.string.hashCode();
		result = 31 * result + (this.second != null ? this.second.hashCode() : 0);
		result = 31 * result + (this.minute != null ? this.minute.hashCode() : 0);
		result = 31 * result + (this.hour != null ? this.hour.hashCode() : 0);
		result = 31 * result + (this.month != null ? this.month.hashCode() : 0);
		result = 31 * result + (this.year != null ? this.year.hashCode() : 0);
		result = 31 * result + (this.dayOfWeek != null ? this.dayOfWeek.hashCode() : 0);
		result = 31 * result + (this.dayOfMonth != null ? this.dayOfMonth.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DefaultCronExpression that = (DefaultCronExpression) o;
		if (this.dayOfMonth != null ? !this.dayOfMonth.equals(that.dayOfMonth) : that.dayOfMonth != null) {
			return false;
		}
		if (this.dayOfWeek != null ? !this.dayOfWeek.equals(that.dayOfWeek) : that.dayOfWeek != null) {
			return false;
		}
		if (this.hour != null ? !this.hour.equals(that.hour) : that.hour != null) {
			return false;
		}
		if (this.minute != null ? !this.minute.equals(that.minute) : that.minute != null) {
			return false;
		}
		if (this.month != null ? !this.month.equals(that.month) : that.month != null) {
			return false;
		}
		if (this.second != null ? !this.second.equals(that.second) : that.second != null) {
			return false;
		}
		if (!this.string.equals(that.string)) {
			return false;
		}
		if (this.year != null ? !this.year.equals(that.year) : that.year != null) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.string;
	}


}
