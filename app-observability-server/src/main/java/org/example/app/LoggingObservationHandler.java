package org.example.app;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.StreamSupport;

public class LoggingObservationHandler implements ObservationHandler<Observation.Context> {

    private static final Logger log = LoggerFactory.getLogger(LoggingObservationHandler.class);

    @Override
    public boolean supportsContext(Observation.Context context) {
        return "my-observation".equals(context.getName());
        // return true;
    }

    @Override
    public void onStart(Observation.Context context) {
        logWithKeyValues("Starting", context);
    }

    @Override
    public void onScopeOpened(Observation.Context context) {
        logWithKeyValues("Scope opened", context);
    }

    @Override
    public void onScopeClosed(Observation.Context context) {
        logWithKeyValues("Scope closed", context);
    }

    @Override
    public void onStop(Observation.Context context) {
        logWithKeyValues("Stopping", context);
    }

    @Override
    public void onError(Observation.Context context) {
        logWithKeyValues("Error", context);
    }

    private void logWithKeyValues(String message, Observation.Context context) {
        log.info("{} lowCardinalityKeyValues={} highCardinalityKeyValues={}", message,
                context.getLowCardinalityKeyValues(), context.getHighCardinalityKeyValues());
    }

    private String getKeyValueFromContext(String key, Observation.Context context) {
        return StreamSupport.stream(context.getHighCardinalityKeyValues().spliterator(), false)
                .filter(keyValue -> key.equals(keyValue.getKey()))
                .map(KeyValue::getValue)
                .findFirst()
                .orElse("UNKNOWN");
    }
}
