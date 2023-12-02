package com.inditex.hiring.repository;

import com.inditex.hiring.controller.dto.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByBrandIdAndProductPartnumber(Integer brandId, String productPartnumber);
}