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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CronSchedule {
	private static final int ONE_MINUTE_IN_MILLISECONDS = 60000,
			ONE_SECOND_IN_MILLISECONDS = 1000;

	private final ScheduledExecutorService executor;
	private final Multimap<CronExpression, Runnable> runnables;
	private final int periodInMilliseconds;

	private ScheduledFuture<?> future;

	public CronSchedule(ScheduledExecutorService s) {
		this(s, false);
	}

	public CronSchedule(ScheduledExecutorService s, boolean seconds) {
		this.executor = s;
		Multimap<CronExpression, Runnable> wrapped = HashMultimap.create();
		this.runnables = Multimaps.synchronizedMultimap(wrapped);
		this.periodInMilliseconds = seconds ? ONE_SECOND_IN_MILLISECONDS : ONE_MINUTE_IN_MILLISECONDS;
	}

	public void add(CronExpression expression, Runnable runnable) {
		this.runnables.put(expression, runnable);
	}

	public void remove(CronExpression expression) {
		this.runnables.removeAll(expression);
	}

	public void remove(CronExpression expression, Runnable runnable) {
		this.runnables.remove(expression, runnable);
	}

	public boolean isStarted() {
		return this.future != null && !this.future.isCancelled() && !this.future.isDone();
	}

	public synchronized void start() {
		if (!isStarted()) {
			long untilNextPeriod = System.currentTimeMillis() % this.periodInMilliseconds;
			this.future = this.executor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					CronSchedule.this.run();
				}
			}, untilNextPeriod, this.periodInMilliseconds, TimeUnit.MILLISECONDS);
		}
	}

	public void run() {
		run(ZonedDateTime.now());
	}

	public void run(ZonedDateTime time) {
		for (CronExpression expression : this.runnables.keySet()) {
			if (expression.matches(time)) {
				for (Runnable runnable : this.runnables.get(expression)) {
					this.executor.submit(runnable);
				}
			}
		}
	}

	public synchronized void stop() {
		if (this.future != null) {
			this.future.cancel(true);
			this.future = null;
		}
	}
}
