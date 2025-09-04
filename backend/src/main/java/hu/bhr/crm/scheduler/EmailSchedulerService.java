package hu.bhr.crm.scheduler;

import hu.bhr.crm.exception.EmailScheduleException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class EmailSchedulerService {

    private final Scheduler scheduler;

    public EmailSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleEmail(UUID taskId, Timestamp reminderDate) {
        try {
            var jobDetail = EmailJobDetailFactory.createEmailJobDetail(taskId);
            var trigger = EmailTriggerFactory.createTrigger(taskId, reminderDate);

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new EmailScheduleException("Failed to schedule email job", e);
        }
    }

    public void deleteEmailSchedule(UUID taskId) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(
                    EmailSchedulerConstants.EMAIL_TRIGGER_KEY_PREFIX + taskId,
                    EmailSchedulerConstants.EMAIL_TRIGGER_GROUP);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey);
            }

            JobKey jobkey = JobKey.jobKey(
                    EmailSchedulerConstants.EMAIL_JOB_KEY_PREFIX + taskId,
                    EmailSchedulerConstants.EMAIL_JOB_GROUP);
            if (scheduler.checkExists(jobkey)) {
                scheduler.deleteJob(jobkey);
            }

        } catch (SchedulerException e) {
            throw new EmailScheduleException("Failed to delete email schedule", e);
        }
    }

    public void updateEmailScheduleTime(UUID taskId, Timestamp reminderDate) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(
                    EmailSchedulerConstants.EMAIL_TRIGGER_KEY_PREFIX + taskId,
                    EmailSchedulerConstants.EMAIL_TRIGGER_GROUP);
            Trigger newTrigger = EmailTriggerFactory.createTrigger(taskId, reminderDate);
            scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (SchedulerException e) {
            throw new EmailScheduleException("Failed to update email schedule time", e);
        }
    }
}
