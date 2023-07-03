package com.zb.reservationservice.repository;

import com.zb.reservationservice.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    boolean existsByPhone(String phone);
    Optional<Owner> findFirstByPhone(String phone);
}
