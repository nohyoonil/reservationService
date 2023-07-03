package com.zb.reservationservice.repository;

import com.zb.reservationservice.entity.Reservation;
import com.zb.reservationservice.entity.Store;
import com.zb.reservationservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStore(Store store);

    List<Reservation> findByUser(User user);

}
