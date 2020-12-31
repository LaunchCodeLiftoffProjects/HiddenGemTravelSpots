package com.hiddengems.hiddengems.controllers;


import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserAccountController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    public UserAccount getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        if (userId == null) { return null; }

        Optional<UserAccount> user = userRepository.findById(userId);

        if (user.isEmpty()) { return null; }

        return user.get();
    }
}
