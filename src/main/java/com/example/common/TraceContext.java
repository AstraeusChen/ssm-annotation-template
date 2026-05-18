package com.example.common;

import org.slf4j.MDC;
import java.util.UUID;

public class TraceContext {
    public static final String TRACE_ID_KEY = "traceId";

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    public static void clear() {
        MDC.clear();
    }
}