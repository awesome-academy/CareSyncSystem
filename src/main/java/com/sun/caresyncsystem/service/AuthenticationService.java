package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.model.entity.User;

public interface AuthenticationService {
     void activateAccount(String token);
     Long getCurrentUserId();
     User getCurrentUser();
}
