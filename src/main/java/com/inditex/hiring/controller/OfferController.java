package com.inditex.hiring.controller;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.controller.exception.OfferNotFoundException;
import com.inditex.hiring.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * You can change this controller but please do not change ends points signatures & payloads.
 */
@RestController
public class OfferController {

  private OfferService offerService;

  public OfferController(OfferService offerService) {
    this.offerService = offerService;
  }

  @RequestMapping(value = "/offer", method = RequestMethod.POST, consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public void createNewOffer(@RequestBody @Valid Offer offer) {
      offerService.createNewOffer(offer);
  }

  @RequestMapping(value = "/offer", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void deleteAllOffers() {
      offerService.deleteAllOffers();
  }

  @RequestMapping(value = "/offer/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity deleteOfferById(@PathVariable Long id) {

    //to check if the offer with given id exist?
    Offer offer = offerService.getOffersById(id);
    if (offer == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      offerService.deleteOfferById(id);
      return ResponseEntity.ok().build();
    }
  }


  @RequestMapping(value = "/offer", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public List<Offer> getAllOffers() {
    return offerService.getAllOffers();
  }

  @RequestMapping(value = "/offer/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(OfferNotFoundException.class)
  public Offer getOfferById(@PathVariable Long id) {
    return offerService.getOffersById(id);
  }

  @RequestMapping(value = "brand/{brandId}/partnumber/{partnumber}/offer", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public List<OfferByPartNumber> getOfferByPartNumber(@PathVariable Integer brandId, @PathVariable String partnumber) {
    return offerService.findByBrandIdAndPartNumber(brandId, partnumber);
  }
}
