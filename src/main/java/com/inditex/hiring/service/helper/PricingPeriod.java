package com.inditex.hiring.service.helper;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.utils.DateUtil;

import java.time.Instant;

/**
 * A bean stands for a time period with a specified offer
 */
public class PricingPeriod {
    private Instant startDate;
    private Instant endDate;
    private Offer appliedOffer;

    public PricingPeriod(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Offer getAppliedOffer() {
        return appliedOffer;
    }

    public void setAppliedOffer(Offer appliedOffer) {
        this.appliedOffer = appliedOffer;
    }

    public OfferByPartNumber toOfferByPartNumber() {
        String startDateString = DateUtil.toISO8601String(this.startDate);
        //The end_date is not included. So, change 2020-06-14T18:30:00Z to 2020-06-14T18:29:59Z
        String endDateString = DateUtil.toISO8601String(this.endDate.minusSeconds(1));
        return new OfferByPartNumber(startDateString, endDateString, this.getAppliedOffer().getPrice(), this.appliedOffer.getCurrencyIso());
    }
}
