# Scheduled Audit Auto-Configuration

`scheduled-audit-autoconfigure` is a Spring Boot auto-configuration module for publishing audit events around `@Scheduled` job executions.

It provides a listener-based extension point for scheduled job lifecycle events such as start, completion, and failure, and includes a default logging listener out of the box.

## Installation

To use the library, add `scheduled-audit-autoconfigure` together with `spring-boot-starter-aop`:

```xml
<dependency>
    <groupId>io.github.mavencrafted</groupId>
    <artifactId>scheduled-audit-autoconfigure</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

`scheduled-audit-autoconfigure` is enabled automatically when it is present in a Spring Boot application. Existing `@Scheduled` methods are intercepted without additional setup, and the default logging listener can be observed through debug logging.

Custom listeners can be added by declaring one or more `ScheduledAuditListener` beans:

```java
@Bean
ScheduledAuditListener databaseScheduledAuditListener() {
    return event -> {
        // handle the audit event
    };
}
```

When multiple listeners are present, each listener receives every scheduled audit event. If one listener fails, the remaining listeners are still invoked and the scheduled job execution is not interrupted.

## Repository Maintenance

This repository is maintained with consistent branch and commit conventions:

- Branch format: `<type>/<scope>-<description>`
- Commit format: `type(scope): description`
- Supported types: `feat`, `fix`, `chore`, `refactor`, `docs`, `test`
