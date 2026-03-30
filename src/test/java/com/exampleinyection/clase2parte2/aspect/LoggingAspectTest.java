package com.exampleinyection.clase2parte2.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoggingAspectTest {

    private final LoggingAspect loggingAspect = new LoggingAspect();

    @Test
    void shouldLogAroundAndReturnProceedResult() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Object target = new Object();

        when(joinPoint.getTarget()).thenReturn(target);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"value", 1});
        when(joinPoint.proceed()).thenReturn("ok");

        Object result = loggingAspect.logAround(joinPoint);

        assertEquals("ok", result);
        verify(joinPoint).proceed();
    }

    @Test
    void shouldRethrowExceptionWhenProceedFails() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Object target = new Object();
        RuntimeException expected = new RuntimeException("boom");

        when(joinPoint.getTarget()).thenReturn(target);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"value"});
        when(joinPoint.proceed()).thenThrow(expected);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> loggingAspect.logAround(joinPoint));

        assertSame(expected, thrown);
        verify(joinPoint).proceed();
    }

    @Test
    void shouldCoverPointcutMethod() {
        assertDoesNotThrow(loggingAspect::applicationPackagePointcut);
    }
}

