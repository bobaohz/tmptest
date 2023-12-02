package com.inditex.hiring.repository;


import com.inditex.hiring.TestConstants;
import com.inditex.hiring.controller.dto.Offer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@DataJpaTest
public class OfferRepositoryTest {

    @Autowired
    private JpaRepository<Offer, Long> jpaRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Test
    public void testCreateNewOfferAndGetOfferById() {

        String testProdPartNumber = "100100";
        Long testOfferId = 1001L;
        Offer offer1 = new Offer(testOfferId, TestConstants.BRAND_ID_ZARA, "2020-06-14T00.00.00Z", "2020-06-14T01.00.00Z", 1l,
                testProdPartNumber,
                0, BigDecimal.valueOf(100.99), "EUR");

        jpaRepository.save(offer1);


        Optional<Offer> result = jpaRepository.findById(testOfferId);
        Assertions.assertTrue(result.isPresent());
        Offer offerFromDB = result.get();
        compareOfferDetails(offerFromDB, offer1);
    }

    @Test
    public void testFindByBrandIdAndProductPartnumber() {

        String testProdPartNumber = "100100";
        Long testOfferId = 1001L;
        Offer offer1 = new Offer(testOfferId, TestConstants.BRAND_ID_ZARA, "2020-06-14T00.00.00Z", "2020-06-14T01.00.00Z", 1l,
                testProdPartNumber,
                0, BigDecimal.valueOf(100.99), "EUR");
        jpaRepository.save(offer1);

        Offer offer2 = new Offer(new Random().nextLong(), TestConstants.BRAND_ID_ZARA, "2020-06-14T00.00.00Z", "2020-06-14T01.00.00Z", 1l,
                "999000",
                0, BigDecimal.valueOf(100.99), "EUR");
        jpaRepository.save(offer2);


        List<Offer> result = offerRepository.findByBrandIdAndProductPartnumber(TestConstants.BRAND_ID_ZARA, testProdPartNumber);
        Assertions.assertEquals(1, result.size());
        Offer offerFromDB = result.get(0);
        compareOfferDetails(offerFromDB, offer1);
    }

    private static void compareOfferDetails(Offer offerFromDB, Offer offer1) {
        Assertions.assertEquals(offerFromDB.getOfferId(), offer1.getOfferId());
        Assertions.assertEquals(offerFromDB.getBrandId(), offer1.getBrandId());
        Assertions.assertEquals(offerFromDB.getStartDate(), offer1.getStartDate());
        Assertions.assertEquals(offerFromDB.getEndDate(), offer1.getEndDate());
        Assertions.assertEquals(offerFromDB.getPrice(), offer1.getPrice());
        Assertions.assertEquals(offerFromDB.getPriceListId(), offer1.getPriceListId());
        Assertions.assertEquals(offerFromDB.getProductPartnumber(), offer1.getProductPartnumber());
        Assertions.assertEquals(offerFromDB.getCurrencyIso(), offer1.getCurrencyIso());
    }
}
