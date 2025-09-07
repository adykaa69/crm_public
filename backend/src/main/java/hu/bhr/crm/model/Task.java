package hu.bhr.crm.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Task(
        UUID id,
        Customer customer,
        String title,
        String description,
        ZonedDateTime reminder,
        ZonedDateTime dueDate,
        TaskStatus status,
        ZonedDateTime createdAt,
        ZonedDateTime completedAt,
        ZonedDateTime updatedAt
) {
    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private UUID id;
        private Customer customer;
        private String title;
        private String description;
        private ZonedDateTime reminder;
        private ZonedDateTime dueDate;
        private TaskStatus status;
        private ZonedDateTime createdAt;
        private ZonedDateTime completedAt;
        private ZonedDateTime updatedAt;

        public TaskBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public TaskBuilder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder reminder(ZonedDateTime reminder) {
            this.reminder = reminder;
            return this;
        }

        public TaskBuilder dueDate(ZonedDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public TaskBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public TaskBuilder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TaskBuilder completedAt(ZonedDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public TaskBuilder updatedAt(ZonedDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Task build() {
            return new Task(id, customer, title, description, reminder, dueDate, status, createdAt, completedAt, updatedAt);
        }
    }
}

