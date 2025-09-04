package hu.bhr.crm.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.UUID;

public class EmailJobDetailFactory {

public static JobDetail createEmailJobDetail(UUID taskId) {
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put(EmailSchedulerConstants.TASK_ID_KEY, taskId.toString());

    return JobBuilder.newJob(EmailJob.class)
            .withIdentity(EmailSchedulerConstants.EMAIL_JOB_KEY_PREFIX + taskId,
                                EmailSchedulerConstants.EMAIL_JOB_GROUP)
            .usingJobData(jobDataMap)
            .storeDurably()
            .build();
    }
}
