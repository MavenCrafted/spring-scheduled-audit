package io.github.mavencrafted.scheduling.audit;

/**
 * Receives audit events emitted for Spring {@code @Scheduled} job executions.
 */
public interface ScheduledAuditListener {

    /**
     * Handles a scheduled job audit event.
     *
     * @param event the audit event to handle
     */
    void onEvent(ScheduledAuditEvent event);
}
