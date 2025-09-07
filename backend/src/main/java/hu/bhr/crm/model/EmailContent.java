package hu.bhr.crm.model;

import java.time.ZonedDateTime;
import java.util.List;

public record EmailContent(
        String taskTitle,
        ZonedDateTime dueDate,
        String taskDescription,

        String customerFirstName,
        String customerLastName,
        String customerPhoneNumber,
        String customerEmail,
        Residence customerResidence,

        List<CustomerDetails> customerDetailsList
) {
    public static EmailContentBuilder builder() {
        return new EmailContentBuilder();
    }

    public static class EmailContentBuilder {
        private String taskTitle;
        private ZonedDateTime dueDate;
        private String taskDescription;

        private String customerFirstName;
        private String customerLastName;
        private String customerPhoneNumber;
        private String customerEmail;
        private Residence customerResidence;

        private List<CustomerDetails> customerDetailsList;

        public EmailContentBuilder taskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
            return this;
        }

        public EmailContentBuilder dueDate(ZonedDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public EmailContentBuilder taskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
            return this;
        }

        public EmailContentBuilder customerFirstName(String customerFirstName) {
            this.customerFirstName = customerFirstName;
            return this;
        }

        public EmailContentBuilder customerLastName(String customerLastName) {
            this.customerLastName = customerLastName;
            return this;
        }

        public EmailContentBuilder customerPhoneNumber(String customerPhoneNumber) {
            this.customerPhoneNumber = customerPhoneNumber;
            return this;
        }

        public EmailContentBuilder customerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public EmailContentBuilder customerResidence(Residence customerResidence) {
            this.customerResidence = customerResidence;
            return this;
        }

        public EmailContentBuilder customerDetailsList(List<CustomerDetails> customerDetailsList) {
            this.customerDetailsList = customerDetailsList;
            return this;
        }

        public EmailContent build() {
            return new EmailContent(
                    taskTitle,
                    dueDate,
                    taskDescription,
                    customerFirstName,
                    customerLastName,
                    customerPhoneNumber,
                    customerEmail,
                    customerResidence,
                    customerDetailsList
            );
        }
    }
}
