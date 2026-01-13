package hu.bhr.crm.service;

import hu.bhr.crm.config.MailProperties;
import hu.bhr.crm.exception.EmailSendingException;
import hu.bhr.crm.mapper.EmailFactory;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.model.EmailContent;
import hu.bhr.crm.model.EmailContentHtmlBuilder;
import hu.bhr.crm.model.Task;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    private final TaskService taskService;
    private final CustomerService customerService;
    private final CustomerDetailsService customerDetailsService;

    public void createAndSendEmail(UUID taskId) {
        Task task = taskService.getTaskById(taskId);

        Customer customer = null;
        List<CustomerDetails> customerDetailsList = List.of();
        if (task.customerId() != null) {
            customer = customerService.getCustomerById(task.customerId());
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
        helper.setFrom(mailProperties.getFrom());
        helper.setTo(mailProperties.getTo());
        helper.setSubject("Task Reminder - " + emailContent.taskTitle());
        helper.setText(htmlContent, true);

        return mimeMessage;
    }
}
