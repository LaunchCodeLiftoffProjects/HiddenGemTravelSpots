package com.hiddengems.hiddengems.models.data;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GemRepository extends CrudRepository<Gem, Integer> {
    Gem findByUserAccount(UserAccount userAccount);
}
