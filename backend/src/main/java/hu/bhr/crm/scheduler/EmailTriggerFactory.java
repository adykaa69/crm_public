package hu.bhr.crm.scheduler;

import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class EmailTriggerFactory {

    public static Trigger createTrigger(UUID taskId, ZonedDateTime reminderDate) {
        return TriggerBuilder.newTrigger()
                .withIdentity(EmailSchedulerConstants.EMAIL_TRIGGER_KEY_PREFIX + taskId,
                                    EmailSchedulerConstants.EMAIL_TRIGGER_GROUP)
                .startAt(Date.from(reminderDate.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withMisfireHandlingInstructionFireNow())
                .build();
    }
}
