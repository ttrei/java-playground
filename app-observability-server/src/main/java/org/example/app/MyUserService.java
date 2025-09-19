package org.example.app;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
class MyUserService {

    private static final String OBSERVATION_NAME1 = "my.observation.1";
    private static final String OBSERVATION_NAME2 = "my.observation.2";

    private static final Logger log = LoggerFactory.getLogger(MyUserService.class);

    private final ObservationRegistry observationRegistry;

    private final Random random = new Random();

    public MyUserService(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
        this.observationRegistry.observationConfig().observationHandler(new LoggingObservationHandler());
    }

    // Create observation manually
    String getUser(String userId) {
        var observation = Observation.createNotStarted(OBSERVATION_NAME1, this.observationRegistry);
        observation.contextualName("getting-user");
        observation.lowCardinalityKeyValue("callType", "getUser");
        observation.highCardinalityKeyValue("userId", userId);
        return observation.observe(() -> {
            log.info("Getting user by id <{}>", userId);
            observation.highCardinalityKeyValue("userAge", userId);
            try {
                Thread.sleep(random.nextLong(200L));
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "foo";
        });
    }

    // Create observation manually
    String getFullUser(String userId) {
        var observation = Observation.createNotStarted(OBSERVATION_NAME1, this.observationRegistry);
        observation.contextualName("getting-full-user");
        observation.lowCardinalityKeyValue("callType", "getFullUser");
        observation.highCardinalityKeyValue("userId", userId);
        return observation.observe(() -> {
            log.info("Getting full user by id <{}>", userId);
            observation.highCardinalityKeyValue("userAge", userId);
            try {
                Thread.sleep(random.nextLong(200L));
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "foo";
        });
    }

    // Use annotation to observe the method
    // <getting-user-name> will be used as a span name
    // <userType=userType2> will be set as a tag for both metric & span
    //
    // NOTE(reinis): Don't use the same name as when creating the observation manually because this creates the
    // Observation via io.micrometer.observation.aop.ObservedAspectObservationDocumentation.of()
    // which adds low cardinality keys "class" and "name", and prometheus registry doesn't like multiple meters with
    // same name but different tags.
    @Observed(name = OBSERVATION_NAME2,
            contextualName = "pinging-user",
            lowCardinalityKeyValues = {"callType", "pingUser"})
    void pingUser(String userId) {
        log.info("Pinging user by id <{}>", userId);
        try {
            Thread.sleep(random.nextLong(200L));
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
