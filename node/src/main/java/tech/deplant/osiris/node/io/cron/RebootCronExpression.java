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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Matches once only.
 */
final class RebootCronExpression extends CronExpression {
	private final AtomicBoolean matchOnce;

	protected RebootCronExpression() {
		this.matchOnce = new AtomicBoolean(true);
	}

	@Override
	public boolean matches(ZonedDateTime t) {
		return this.matchOnce.getAndSet(false);
	}

	@Override
	public String minuteOfCron() {
		return this.minuteOfCron();
	}

	@Override
	public String hourOfCron() {
		return this.hourOfCron();
	}

	@Override
	public String dayOfCron() {
		return this.dayOfCron();
	}

	@Override
	public String monthOfCron() {
		return this.monthOfCron();
	}

	@Override
	public String dayWeekOfCron() {
		return this.dayWeekOfCron();
	}
}
