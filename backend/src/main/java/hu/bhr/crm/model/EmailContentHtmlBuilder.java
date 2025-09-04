package hu.bhr.crm.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

public class EmailContentHtmlBuilder {

    public static String build(EmailContent content) {
        StringBuilder html = new StringBuilder();
        html.append("<h2>Task Reminder</h2>");
        html.append("<ul>");

        html.append(String.format("<li><strong>Task Title:</strong> %s</li>", escape(content.taskTitle())));
        if (content.dueDate() != null) {
            html.append(String.format("<li><strong>Due Date:</strong> %s</li>", content.dueDate()));
        }
        if (notBlank(content.taskDescription())) {
            html.append(String.format("<li><strong>Task Description:</strong> %s</li>", escape(content.taskDescription())));
        }
        html.append("<br>");

        boolean hasCustomer =
                    notBlank(content.customerFirstName()) ||
                    notBlank(content.customerLastName()) ||
                    notBlank(content.customerPhoneNumber()) ||
                    notBlank(content.customerEmail()) ||
                    content.customerResidence() != null ||
                    (content.customerDetailsList() != null && !content.customerDetailsList().isEmpty());

        if (hasCustomer) {
            html.append("<li><strong>Customer Information:</strong></li>");
            html.append("<ul>");

            if (notBlank(content.customerFirstName())) {
                html.append(String.format("<li><strong>First Name:</strong> %s</li>", escape(content.customerFirstName())));
            }
            if (notBlank(content.customerLastName())) {
                html.append(String.format("<li><strong>Last Name:</strong> %s</li>", escape(content.customerLastName())));
            }
            if (notBlank(content.customerPhoneNumber())) {
                html.append(String.format("<li><strong>Phone Number:</strong> %s</li>", escape(content.customerPhoneNumber())));
            }
            if (notBlank(content.customerEmail())) {
                html.append(String.format("<li><strong>Email:</strong> %s</li>", escape(content.customerEmail())));
            }
            if (content.customerResidence() != null) {
                html.append("<li><strong>Residence:</strong></li>");
                html.append("<ul>");
                if (notBlank(content.customerResidence().zipCode())) {
                    html.append(String.format("<li>Zip Code: %s</li>", escape(content.customerResidence().zipCode())));
                }
                if (notBlank(content.customerResidence().streetAddress())) {
                    html.append(String.format("<li>Street Address: %s</li>", escape(content.customerResidence().streetAddress())));
                }
                if (notBlank(content.customerResidence().addressLine2())) {
                    html.append(String.format("<li>Address Line 2: %s</li>", escape(content.customerResidence().addressLine2())));
                }
                if (notBlank(content.customerResidence().city())) {
                    html.append(String.format("<li>City: %s</li>", escape(content.customerResidence().city())));
                }
                if (notBlank(content.customerResidence().country())) {
                    html.append(String.format("<li>Country: %s</li>", escape(content.customerResidence().country())));
                }
                html.append("</ul></li>");
            }

            if (content.customerDetailsList() != null && !content.customerDetailsList().isEmpty()) {
                html.append("<li><strong>Additional Details:</strong></li>");
                html.append("<ul>");
                for (CustomerDetails detail : content.customerDetailsList()) {
                    html.append(String.format("<li>%s</li>", escape(detail.note())));
                }
                html.append("</ul></li>");
            }

            html.append("</ul>");
        }

        return html.toString();
    }

    private static String escape(String s) {
        return StringEscapeUtils.escapeHtml4(s);
    }

    private static boolean notBlank(String s) {
        return !StringUtils.isBlank(s);
    }
}

