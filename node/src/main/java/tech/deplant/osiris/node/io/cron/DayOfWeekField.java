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

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

public class DayOfWeekField extends DefaultField {
	public static final long SECONDS_PER_WEEK = 604800l;
	private final Multimap<Integer, Integer> nth;
	private final Set<Integer> last;
	private final boolean hasNth, hasLast, unspecified;

	private DayOfWeekField(Builder b) {
		super(b);
		this.nth = b.nth.build();
		this.hasNth = !this.nth.isEmpty();
		this.last = b.last.build();
		this.hasLast = !this.last.isEmpty();
		this.unspecified = b.unspecified;
	}

	public static DayOfWeekField parse(Tokens s, boolean oneBased) {
		return new Builder(oneBased).parse(s).build();
	}

	public boolean isUnspecified() {
		return this.unspecified;
	}

	public boolean matches(ZonedDateTime time) {
		if (this.unspecified) {
			return true;
		}
		DayOfWeek dayOfWeek = time.getDayOfWeek();
		int number = number(dayOfWeek.getValue());
		if (this.hasLast) {
			return this.last.contains(number) && time.getMonth() != time.plusWeeks(1).getMonth();
		} else if (this.hasNth) {
			int dayOfYear = time.getDayOfYear();
			if (this.nth.containsKey(number)) {
				for (int possibleMatch : this.nth.get(number)) {
					if (dayOfYear ==
					    time.with(TemporalAdjusters.dayOfWeekInMonth(possibleMatch, dayOfWeek)).getDayOfYear()) {
						return true;
					}
				}
			}
		}
		return contains(number);
	}

	private int number(int dayOfWeek) {
		return dayOfWeek % 7;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + this.nth.hashCode();
		result = 31 * result + this.last.hashCode();
		result = 31 * result + (this.hasNth ? 1 : 0);
		result = 31 * result + (this.hasLast ? 1 : 0);
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
		if (!super.equals(o)) {
			return false;
		}
		DayOfWeekField that = (DayOfWeekField) o;
		if (this.hasLast != that.hasLast) {
			return false;
		}
		if (this.hasNth != that.hasNth) {
			return false;
		}
		if (!this.last.equals(that.last)) {
			return false;
		}
		if (!this.nth.equals(that.nth)) {
			return false;
		}
		return true;
	}

	public static class Builder extends DefaultField.Builder {
		public static final Keywords KEYWORDS = new Keywords();

		static {
			KEYWORDS.put("SUN", 0);
			KEYWORDS.put("MON", 1);
			KEYWORDS.put("TUE", 2);
			KEYWORDS.put("WED", 3);
			KEYWORDS.put("THU", 4);
			KEYWORDS.put("FRI", 5);
			KEYWORDS.put("SAT", 6);
		}

		private final ImmutableSet.Builder<Integer> last;
		private final ImmutableMultimap.Builder<Integer, Integer> nth;
		private boolean oneBased, unspecified;

		public Builder(boolean oneBased) {
			super(0, 6);
			this.oneBased = oneBased;
			this.last = ImmutableSet.builder();
			this.nth = ImmutableMultimap.builder();
		}

		@Override
		protected Builder parse(Tokens tokens) {
			tokens.keywords(KEYWORDS);
			if (this.oneBased) {
				tokens.offset(1);
			}
			super.parse(tokens);
			tokens.reset();
			return this;
		}

		@Override
		protected boolean parseValue(Tokens tokens, Token token, int first, int last) {
			if (token == Token.MATCH_ONE) {
				this.unspecified = true;
				return false;
			} else {
				return super.parseValue(tokens, token, first, last);
			}
		}

		@Override
		protected boolean parseNumber(Tokens tokens, Token token, int first, int last) {
			if (token == Token.LAST) {
				this.last.add(first);
			} else if (token == Token.NTH) {
				int number = nextNumber(tokens);
				if (this.oneBased) {
					number += 1;
				}
				this.nth.put(first, number);
			} else {
				return super.parseNumber(tokens, token, first, last);
			}
			return false;
		}

		@Override
		public DayOfWeekField build() {
			return new DayOfWeekField(this);
		}
	}
}
