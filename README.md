# Spring Scheduled Audit

`spring-scheduled-audit` is a lightweight library for auditing the execution of Spring `@Scheduled` jobs.

It provides a consistent way to capture job lifecycle events such as start, success, failure, and duration, while allowing audit storage to be implemented through extensible backends such as logging or database persistence.

## Repository Maintenance

This repository is maintained with explicit branch and commit conventions to keep history predictable and traceable.

### Branch Names

Format:

`<type>/<scope>-<description>`

Examples:

- `chore/pom-project-structure`
- `docs/readme-maintenance-guidelines`

### Commit Messages

Format:

`type(scope): description`

Examples:

- `chore(pom): initialize base POM with metadata, scm, and compiler setup`
- `docs(readme): refine repository maintenance section`

### Supported Types

`feat`, `fix`, `chore`, `refactor`, `docs`, `test`
