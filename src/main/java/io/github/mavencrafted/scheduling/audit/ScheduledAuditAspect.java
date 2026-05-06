package io.github.mavencrafted.scheduling.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Aspect that emits {@link ScheduledAuditEvent} instances around {@code @Scheduled} method execution.
 */
@Aspect
public class ScheduledAuditAspect {

    private static final Log logger = LogFactory.getLog(ScheduledAuditAspect.class);

    private final List<ScheduledAuditListener> scheduledAuditListeners;

    /**
     * Creates a new aspect backed by the provided listeners.
     *
     * @param scheduledAuditListeners the listeners that receive audit events
     */
    public ScheduledAuditAspect(List<ScheduledAuditListener> scheduledAuditListeners) {
        this.scheduledAuditListeners = List.copyOf(scheduledAuditListeners);
    }

    /**
     * Wraps a scheduled method invocation and publishes audit events for its lifecycle.
     *
     * @param joinPoint the intercepted scheduled method invocation
     * @param scheduled the scheduled annotation that matched the invocation
     * @return the scheduled method result
     * @throws Throwable when the scheduled method fails
     */
    @Around("@annotation(scheduled)")
    public Object audit(ProceedingJoinPoint joinPoint, Scheduled scheduled) throws Throwable {
        UUID executionId = UUID.randomUUID();
        Instant startedAt = Instant.now();
        String taskName = resolveTaskName(joinPoint);
        invokeListenersSafely(ScheduledAuditEvent.started(executionId, taskName, startedAt));
        try {
            Object result = joinPoint.proceed();
            invokeListenersSafely(ScheduledAuditEvent.succeeded(executionId, taskName, startedAt, Instant.now()));
            return result;
        }
        catch (Throwable throwable) {
            invokeListenersSafely(ScheduledAuditEvent.failed(executionId, taskName, startedAt, Instant.now(), throwable));
            throw throwable;
        }
    }

    private void invokeListenersSafely(ScheduledAuditEvent event) {
        for (ScheduledAuditListener listener : scheduledAuditListeners) {
            try {
                listener.onEvent(event);
            }
            catch (RuntimeException ex) {
                logger.warn("Scheduled audit listener failed: " + listener.getClass().getName(), ex);
            }
        }
    }

    private String resolveTaskName(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Class<?> targetClass = (target != null ? AopUtils.getTargetClass(target) : method.getDeclaringClass());
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        return ClassUtils.getQualifiedMethodName(specificMethod, targetClass);
    }
}
