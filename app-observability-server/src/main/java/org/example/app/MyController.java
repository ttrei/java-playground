package org.example.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
class MyController {

    private final MyUserService myUserService;

    MyController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping("/user/{userId}")
    String getUser(@PathVariable("userId") String userId) {
        return myUserService.getUser(userId);
    }

    @GetMapping("/user/{userId}/full")
    String getFullUser(@PathVariable("userId") String userId) {
        return myUserService.getFullUser(userId);
    }

    @PostMapping("/user/{userId}/ping")
    void pingUser(@PathVariable("userId") String userId) {
        myUserService.pingUser(userId);
    }

    @PostMapping("/user/{userId}/frob")
    void frobUser(@PathVariable("userId") String userId) {
        myUserService.frobUser(userId);
    }
}
