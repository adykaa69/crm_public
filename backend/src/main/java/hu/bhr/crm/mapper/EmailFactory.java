package hu.bhr.crm.mapper;

import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.model.EmailContent;
import hu.bhr.crm.model.Task;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class EmailFactory {

    public static EmailContent createEmailContent(
            Task task,
            Customer customer,
            List<CustomerDetails> customerDetailsList) {

        EmailContent.EmailContentBuilder builder = EmailContent.builder()
                .taskTitle(task.title())
                .dueDate(task.dueDate())
                .taskDescription(task.description());

        if (customer != null) {
            builder
                .customerFirstName(customer.firstName())
                .customerLastName(customer.lastName())
                .customerPhoneNumber(customer.phoneNumber())
                .customerEmail(customer.email())
                .customerResidence(customer.residence())
                .customerDetailsList(customerDetailsList);
        }

        return builder.build();
    }
}
