package com.hiddengems.hiddengems.models.data;

import com.hiddengems.hiddengems.models.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
}
