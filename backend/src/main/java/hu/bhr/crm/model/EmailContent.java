package hu.bhr.crm.model;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record EmailContent(
        String taskTitle,
        Instant dueDate,
        String taskDescription,

        String customerFirstName,
        String customerLastName,
        String customerPhoneNumber,
        String customerEmail,
        Residence customerResidence,

        List<CustomerDetails> customerDetailsList
) {}
