package hu.bhr.crm.service;

import hu.bhr.crm.exception.EmailSendingException;
import hu.bhr.crm.mapper.EmailFactory;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.model.EmailContent;
import hu.bhr.crm.model.EmailContentHtmlBuilder;
import hu.bhr.crm.model.Task;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String fromEmail;
    private final String toEmail;

    private final TaskService taskService;
    private final CustomerService customerService;
    private final CustomerDetailsService customerDetailsService;

    public EmailService(JavaMailSender javaMailSender,
                        @Value("${spring.mail.from}") String fromEmail,
                        @Value("${spring.mail.to}") String toEmail,
                        TaskService taskService,
                        CustomerService customerService,
                        CustomerDetailsService customerDetailsService) {
        this.javaMailSender = javaMailSender;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.taskService = taskService;
        this.customerService = customerService;
        this.customerDetailsService = customerDetailsService;
    }

    public void createAndSendEmail(UUID taskId) {
        Task task = taskService.getTaskById(taskId);

        Customer customer = null;
        List<CustomerDetails> customerDetailsList = List.of();
        if (task.customer() != null) {
            customer = customerService.getCustomerById(task.customer().id());
            customerDetailsList = customerDetailsService.getAllCustomerDetails(customer.id());
        }

        try {
            MimeMessage message = createEmail(task, customer, customerDetailsList);
            sendEmail(message);
        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send email", e);
        }
    }

    private void sendEmail(MimeMessage message) {
        javaMailSender.send(message);
    }

        private MimeMessage createEmail(Task task,
                                          Customer customer,
                                          List<CustomerDetails> customerDetailsList) throws MessagingException {

        EmailContent emailContent = EmailFactory.createEmailContent(task, customer, customerDetailsList);
        String htmlContent = EmailContentHtmlBuilder.build(emailContent);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Task Reminder - " + emailContent.taskTitle());
        helper.setText(htmlContent, true);

        return mimeMessage;
    }
}
