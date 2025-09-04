package hu.bhr.crm.scheduler;

import hu.bhr.crm.service.EmailService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.UUID;

public class EmailJob implements Job {

    private final EmailService emailService;

    public EmailJob(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        UUID taskId = UUID.fromString(context.getMergedJobDataMap().getString(EmailSchedulerConstants.TASK_ID_KEY));
        emailService.createAndSendEmail(taskId);
    }
}
