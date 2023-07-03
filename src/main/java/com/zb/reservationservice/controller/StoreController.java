package com.zb.reservationservice.controller;

import com.zb.reservationservice.entity.Store;
import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.StoreForm;
import com.zb.reservationservice.model.*;
import com.zb.reservationservice.repository.StoreRepository;
import com.zb.reservationservice.service.StoreService;
import com.zb.reservationservice.util.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/store")
@RestController
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    /**
     * 매장 등록 - 점주
     */
    @PostMapping
    public ResponseEntity<?> createStore(@RequestHeader("JWT-TOKEN") String token,
                                         @RequestBody @Valid StoreForm storeForm, Errors errors) {

        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_DATA_INPUT);
        }

        TokenInfo tokenInfo = JWTUtils.getOwnerTokenInfo(token);
        storeService.createStore(tokenInfo, storeForm);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 매장 단건 조회 - all
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStore(@PathVariable long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_EXISTS));

        StoreResponse storeResponse = StoreResponse.of(store);

        return ResponseEntity.ok(storeResponse);
    }

    /*
     * 점주 승인 대기 중 예약 가져오기 - 점주
     */
    @GetMapping("/{storeId}/waiting")
    public ResponseEntity<?> getWaitingReservations(@RequestHeader("JWT-TOKEN") String token,
                                                    @PathVariable long storeId) {

        TokenInfo tokenInfo = JWTUtils.getOwnerTokenInfo(token);
        List<ReservationResponse> waitingApprovals = storeService.getWaitingApproval(tokenInfo, storeId);

        return ResponseEntity.ok(waitingApprovals);
    }

    /**
     * 매점 조회(사전 순, 거리 순, 평점 순) - all
     */
    @GetMapping
    public ResponseEntity<?> getStores(@RequestParam(defaultValue = "SCORE") Sort sort,
                                       @RequestParam(defaultValue = "0") double lat,
                                       @RequestParam(defaultValue = "0") double lnt) {

        List<StoreResponse> stores = storeService.getStores(sort, lat, lnt);

        return ResponseEntity.ok(stores);
    }
}
