package com.hiddengems.hiddengems.models.data;

import com.hiddengems.hiddengems.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
