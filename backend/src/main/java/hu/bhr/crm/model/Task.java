package hu.bhr.crm.model;

import java.time.Instant;
import java.util.UUID;

public record Task(
        UUID id,
        Customer customer,
        String title,
        String description,
        Instant reminder,
        Instant dueDate,
        TaskStatus status,
        Instant createdAt,
        Instant completedAt,
        Instant updatedAt
) {
    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private UUID id;
        private Customer customer;
        private String title;
        private String description;
        private Instant reminder;
        private Instant dueDate;
        private TaskStatus status;
        private Instant createdAt;
        private Instant completedAt;
        private Instant updatedAt;

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

        public TaskBuilder reminder(Instant reminder) {
            this.reminder = reminder;
            return this;
        }

        public TaskBuilder dueDate(Instant dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public TaskBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public TaskBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TaskBuilder completedAt(Instant completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public TaskBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Task build() {
            return new Task(id, customer, title, description, reminder, dueDate, status, createdAt, completedAt, updatedAt);
        }
    }
}

