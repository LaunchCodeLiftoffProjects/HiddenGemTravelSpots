package com.hiddengems.hiddengems.models.data;

import com.hiddengems.hiddengems.models.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends CrudRepository<Guest, Integer> {
}
