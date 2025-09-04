package hu.bhr.crm.scheduler;

import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.sql.Timestamp;
import java.util.UUID;

public class EmailTriggerFactory {

    public static Trigger createTrigger(UUID taskId, Timestamp reminderDate) {
        return TriggerBuilder.newTrigger()
                .withIdentity(EmailSchedulerConstants.EMAIL_TRIGGER_KEY_PREFIX + taskId,
                                    EmailSchedulerConstants.EMAIL_TRIGGER_GROUP)
                .startAt(reminderDate)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withMisfireHandlingInstructionFireNow())
                .build();
    }
}
