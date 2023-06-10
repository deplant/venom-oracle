package tech.deplant.osiris.node.tst.unit.cron;

import tech.deplant.osiris.node.io.cron.CronExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OutCronExpression {

	@Test
	public void Output() {
		String expr = "1 2 3 4 5";
		CronExpression cron = CronExpression.parse(expr);
		assertTrue(expr.equals(cron.toString()));
		System.out.println("MinuteOfCron " + cron.minuteOfCron());
		System.out.println("HourOfCron " + cron.hourOfCron());
		System.out.println("DayOfCron " + cron.dayOfCron());
		System.out.println("MonthOfCron " + cron.monthOfCron());
		System.out.println("DayWeekOfCron " + cron.dayWeekOfCron());
//        if (cron.matches(ZonedDateTime.now())) {
//            executor.submit(
//                    () -> {
//
//
//                    }
//
//
//            );
//        }
	}
}
