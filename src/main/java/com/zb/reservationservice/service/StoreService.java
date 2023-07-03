package com.zb.reservationservice.service;

import com.zb.reservationservice.entity.Owner;
import com.zb.reservationservice.entity.Reservation;
import com.zb.reservationservice.entity.Store;
import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.StoreForm;
import com.zb.reservationservice.model.ReservationResponse;
import com.zb.reservationservice.model.Sort;
import com.zb.reservationservice.model.StoreResponse;
import com.zb.reservationservice.model.TokenInfo;
import com.zb.reservationservice.repository.OwnerRepository;
import com.zb.reservationservice.repository.ReservationRepository;
import com.zb.reservationservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;


    public void createStore(TokenInfo tokenInfo, StoreForm storeForm) {

        Owner owner = ownerRepository.findById(tokenInfo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        if (!owner.isPartner()) {
            throw new CustomException(ErrorCode.IS_NOT_PARTNER);
        }

        Store store = Store.builder()
                .owner(owner)
                .name(storeForm.getName())
                .description(storeForm.getDescription())
                .lat(storeForm.getLat())
                .lnt(storeForm.getLnt())
                .build();

        storeRepository.save(store);
    }

    /**
     * 점주 승인 대기 중 예약 조회
     */
    public List<ReservationResponse> getWaitingApproval(TokenInfo tokenInfo, long storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_EXISTS));

        if (store.getOwner().getId() != tokenInfo.getId()) {
            throw new CustomException(ErrorCode.HAS_NO_AUTHORIZATION);
        }

        ArrayList<ReservationResponse> responses = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByStore(store);

        for (Reservation reservation : reservations) {
            if (!reservation.isApproved() && !reservation.isRejected() &&
                    reservation.getReservationTime().isAfter(LocalDateTime.now().plusMinutes(20))) {
                responses.add(ReservationResponse.of(reservation));
            }
        }

        return responses;
    }

    /**
     * 매점 조회(사전 순, 거리 순, 평점 순)
     */
    public List<StoreResponse> getStores(Sort sort, double lat, double lnt) {
        List<StoreResponse> responses = new ArrayList<>();
        List<Store> stores = new ArrayList<>();
        if (sort == Sort.ALPHABET) {
            stores = storeRepository.findAllByOrderByNameAsc();
        } else if (sort == Sort.DISTANCE) {
            // 거리순 정렬
        } else if (sort == Sort.SCORE) {
            // 평점 순 정렬
        }

        for (Store store : stores) {
            responses.add(StoreResponse.of(store));
        }

        return responses;
    }
}
