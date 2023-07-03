package com.zb.reservationservice.repository;

import com.zb.reservationservice.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByOrderByNameAsc();
}
