package com.inditex.hiring.service;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.controller.exception.OfferNotFoundException;
import com.inditex.hiring.repository.OfferRepository;
import com.inditex.hiring.service.helper.PriceHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer createNewOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Long count() {
        return offerRepository.count();
    }

    public Offer getOffersById(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> new OfferNotFoundException(id));
    }

    public void deleteOfferById(Long id) {
        offerRepository.deleteById(id);
    }

    public void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    public List<OfferByPartNumber> findByBrandIdAndPartNumber(Integer brandId, String productPartNumber) {
        List<Offer> offers = offerRepository.findByBrandIdAndProductPartnumber(brandId, productPartNumber);
        if (offers == null || offers.isEmpty()) {
            return new ArrayList<>();
        }
        return new PriceHelper().generateTimeTable(offers);
    }

    public List<Offer> getTopNOffer(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        Page<Offer> topNOffers = offerRepository.findAll(pageable);
        return topNOffers.getContent();
    }
}
