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

import com.google.common.collect.ImmutableSortedSet;

import java.util.NavigableSet;

public class DefaultField implements TimeField {
	public final NavigableSet<Integer> numbers;
	private final boolean fullRange;

	protected DefaultField(Builder b) {
		this.fullRange = b.fullRange;
		this.numbers = this.fullRange ? null : b.numbers.build();
	}

	public static DefaultField parse(Tokens s, int min, int max) {
		return new Builder(min, max).parse(s).build();
	}

	@Override
	public boolean contains(int number) {
		return this.fullRange || this.numbers.contains(number);
	}

	@Override
	public NavigableSet<Integer> numbers() {
		return this.numbers;
	}

	@Override
	public int hashCode() {
		int result = (this.fullRange ? 1 : 0);
		result = 31 * result + (this.numbers != null ? this.numbers.hashCode() : 0);
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
		DefaultField that = (DefaultField) o;
		if (this.fullRange != that.fullRange) {
			return false;
		}
		if (this.numbers != null ? !this.numbers.equals(that.numbers) : that.numbers != null) {
			return false;
		}
		return true;
	}

	public static class Builder {
		private final ImmutableSortedSet.Builder<Integer> numbers;
		private final int min, max;
		private boolean fullRange;

		public Builder(int min, int max) {
			this.min = min;
			this.max = max;
			this.numbers = ImmutableSortedSet.naturalOrder();
		}

		protected Builder parse(Tokens tokens) {
			Token token;
			while (!endOfField(token = tokens.next())) {
				if (parseValue(tokens, token, this.min, this.max)) {
					break;
				}
			}
			return this;
		}

		protected boolean parseValue(Tokens tokens, Token token, int first, int last) {
			if (token == Token.NUMBER) {
				if (parseNumber(tokens, tokens.next(), tokens.number(), last)) {
					return true;
				}
			} else if (token == Token.MATCH_ALL) {
				token = tokens.next();
				if (token == Token.SKIP) {
					rangeSkip(first, last, nextNumber(tokens));
				} else if (token == Token.VALUE_SEPARATOR) {
					range(first, last);
				} else if (endOfField(token)) {
					range(first, last);
					return true;
				}
			}
			return false;
		}

		/**
		 * Returns true if the end of this field has been reached.
		 */
		protected boolean parseNumber(Tokens tokens, Token token, int first, int last) {
			if (token == Token.SKIP) {
				rangeSkip(first, last, nextNumber(tokens));
			} else if (token == Token.RANGE) {
				last = nextNumber(tokens);
				token = tokens.next();
				if (token == Token.SKIP) {
					rangeSkip(first, last, nextNumber(tokens));
				} else if (token == Token.VALUE_SEPARATOR) {
					range(first, last);
				} else if (endOfField(token)) {
					range(first, last);
					return true;
				}
			} else if (token == Token.VALUE_SEPARATOR) {
				add(first);
			} else if (endOfField(token)) {
				add(first);
				return true;
			}
			return false;
		}

		protected int nextNumber(Tokens tokens) {
			if (tokens.next() == Token.NUMBER) {
				return tokens.number();
			}
			throw new IllegalStateException("Expected number");
		}

		private boolean endOfField(Token token) {
			return token == Token.FIELD_SEPARATOR || token == Token.END_OF_INPUT;
		}

		protected void rangeSkip(int first, int last, int skip) {
			for (int i = first; i <= last; i++) {
				if ((i - this.min) % skip == 0) {
					add(i);
				}
			}
		}

		protected void range(int first, int last) {
			if (first == this.min && last == this.max) {
				this.fullRange = true;
			} else {
				for (int i = first; i <= last; i++) {
					add(i);
				}
			}
		}

		protected void add(int value) {
			this.numbers.add(value);
		}

		public DefaultField build() {
			return new DefaultField(this);
		}
	}
}
