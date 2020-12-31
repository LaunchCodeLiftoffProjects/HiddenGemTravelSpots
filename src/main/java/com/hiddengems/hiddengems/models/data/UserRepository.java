package com.hiddengems.hiddengems.models.data;

import com.hiddengems.hiddengems.models.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserAccount, Integer> {
    UserAccount findByUsername(String username);
}
