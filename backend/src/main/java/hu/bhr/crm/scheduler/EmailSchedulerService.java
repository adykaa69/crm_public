package hu.bhr.crm.scheduler;

import hu.bhr.crm.exception.EmailScheduleException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Service for managing scheduled email jobs using the Quartz Scheduler.
 * <p>
 * This service abstracts the complexity of the Quartz API (Jobs, Triggers, Keys)
 * and provides high-level methods to schedule, reschedule, or cancel email reminders
 * associated with specific tasks. It maps Task IDs to Quartz JobKeys and TriggerKeys
 * to maintain a 1:1 relationship between a task and its reminder job.
 * </p>
 */
@Service
public class EmailSchedulerService {

    private final Scheduler scheduler;

    public EmailSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Schedules a new email reminder job for a specific task.
     * <p>
     * Creates a persistent Quartz {@link org.quartz.JobDetail} and a {@link Trigger}
     * set to fire at the specified time. The job is uniquely identified by the task ID.
     * </p>
     *
     * @param taskId the unique identifier of the task requiring a reminder
     * @param reminderDate the exact date and time when the email should be sent
     * @throws EmailScheduleException if the underlying Quartz scheduler fails to schedule the job
     */
    public void scheduleEmail(UUID taskId, ZonedDateTime reminderDate) {
        try {
            var jobDetail = EmailJobDetailFactory.createEmailJobDetail(taskId);
            var trigger = EmailTriggerFactory.createTrigger(taskId, reminderDate);

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new EmailScheduleException("Failed to schedule email job", e);
        }
    }

    /**
     * Removes an existing email schedule for a task.
     * <p>
     * This method attempts to unschedule the trigger and delete the job associated
     * with the given task ID. It performs existence checks before deletion to ensure
     * safety and idempotency (calling it multiple times won't cause errors).
     * </p>
     *
     * @param taskId the unique identifier of the task whose reminder should be cancelled
     * @throws EmailScheduleException if the Quartz scheduler encounters an error during deletion
     */
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

    /**
     * Reschedules an existing email job to a new time.
     * <p>
     * Updates the trigger associated with the task ID to fire at the new provided time.
     * This is typically used when a task's reminder date is modified by the user.
     * </p>
     *
     * @param taskId the unique identifier of the task
     * @param reminderDate the new date and time for the reminder
     * @throws EmailScheduleException if the Quartz scheduler fails to reschedule the job
     */
    public void updateEmailScheduleTime(UUID taskId, ZonedDateTime reminderDate) {
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
