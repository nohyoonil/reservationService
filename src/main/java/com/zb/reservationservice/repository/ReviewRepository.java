package com.zb.reservationservice.repository;

import com.zb.reservationservice.entity.Review;
import com.zb.reservationservice.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStore(Store store);

}
