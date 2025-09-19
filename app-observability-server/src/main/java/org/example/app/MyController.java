package org.example.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
class MyController {

    private static final Logger log = LoggerFactory.getLogger(MyController.class);
    private final MyUserService myUserService;

    MyController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping("/user/{userId}")
    String userName(@PathVariable("userId") String userId) {
        log.info("GET /user/{}", userId);
        return myUserService.userName(userId);
    }

    @PostMapping("/user/{userId}/ping")
    void pingUser(@PathVariable("userId") String userId) {
        log.info("POST /user/{}/ping", userId);
        myUserService.pingUser(userId);
    }
}
