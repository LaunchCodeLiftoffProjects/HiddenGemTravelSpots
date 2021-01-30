package com.hiddengems.hiddengems.models.data;

import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {
    UserProfile findByUserAccount(UserAccount userAccount);
}