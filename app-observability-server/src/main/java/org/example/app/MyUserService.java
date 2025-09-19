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

    private static final Logger log = LoggerFactory.getLogger(MyUserService.class);

    private final ObservationRegistry observationRegistry;

    private final Random random = new Random();

    public MyUserService(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
        this.observationRegistry.observationConfig().observationHandler(new LoggingObservationHandler());
    }

    /*
    // Example of using an annotation to observe methods
    // <user.name> will be used as a metric name
    // <getting-user-name> will be used as a span  name
    // <userType=userType2> will be set as a tag for both metric & span
    @Observed(name = "user.name",
            contextualName = "getting-user-name",
            lowCardinalityKeyValues = {"userType", "userType2"})
     */
    String userName(String userId) {
        var observation = Observation.createNotStarted("my-observation", this.observationRegistry);
        observation.contextualName("get-user-name");
        observation.highCardinalityKeyValue("userId", userId);
        return observation.observe(() -> {
            log.info("Getting user name for user with id <{}>", userId);
            observation.lowCardinalityKeyValue("userType", "userType2");
            observation.highCardinalityKeyValue("userAge", userId);
            try {
                Thread.sleep(random.nextLong(200L)); // simulates latency
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "foo";
        });
    }
}
