package org.example.app;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@Component
public class LoggingObservationHandler implements ObservationHandler<Observation.Context> {

    private static final Logger log = LoggerFactory.getLogger(LoggingObservationHandler.class);

    @Override
    public boolean supportsContext(Observation.Context context) {
        return "my-observation".equals(context.getName());
        // return true;
    }

    @Override
    public void onStart(Observation.Context context) {
        log.info("Starting, my-key={}", getKeyValueFromContext(context, "my-key"));
    }

    @Override
    public void onScopeOpened(Observation.Context context) {
        log.info("Scope opened, my-key={}", getKeyValueFromContext(context, "my-key"));
    }

    @Override
    public void onScopeClosed(Observation.Context context) {
        log.info("Scope closed, my-key={}", getKeyValueFromContext(context, "my-key"));
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("Stopping, my-key={}", getKeyValueFromContext(context, "my-key"));
    }

    @Override
    public void onError(Observation.Context context) {
        log.info("Error, my-key={}", getKeyValueFromContext(context, "my-key"));
    }

    private String getKeyValueFromContext(Observation.Context context, String key) {
        return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
                .filter(keyValue -> key.equals(keyValue.getKey()))
                .map(KeyValue::getValue)
                .findFirst()
                .orElse("UNKNOWN");
    }
}
