package com.inditex.hiring.service.helper;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.utils.DateUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PriceHelper {

    public List<OfferByPartNumber> generateTimeTable(List<Offer> offerList) {
        //handle simple case firstly
        if (offerList == null || offerList.isEmpty()) {
            return new ArrayList<>();
        }

        if (offerList.size() == 1) {
            return offerList.stream().map(offer ->
                    new OfferByPartNumber(offer.getStartDate(), offer.getEndDate(), offer.getPrice(), offer.getCurrencyIso())).collect(Collectors.toList());
        }

        //to handle cases with time overlap
        List<Instant> allDateList = new ArrayList<>();

        offerList.stream().forEach(offer -> {
            allDateList.add(Instant.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(offer.getStartDate()))));
            allDateList.add(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(offer.getEndDate())));
        });


        //sort the date
        allDateList.sort(Comparator.comparing(Instant::toEpochMilli));

        //get the period list (start_date, end_date)
        List<PricingPeriod> periods = splitIntoPeriods(allDateList);

        periods.stream().forEach(period -> setApplicableOffer(period, offerList));

        List<PricingPeriod> mergedPeriods = mergePeriodsWithSamePrice(periods);
        return mergedPeriods.stream().map(pricingPeriod-> pricingPeriod.toOfferByPartNumber()).collect(Collectors.toList());
    }

    private List<PricingPeriod> mergePeriodsWithSamePrice(List<PricingPeriod> periods) {
        List<PricingPeriod> mergedPeriods = new ArrayList<>();
        PricingPeriod previousPeriod = null;
        for (PricingPeriod currentPeriod : periods) {
            if (previousPeriod == null) {
                mergedPeriods.add(currentPeriod);
                previousPeriod = currentPeriod;
                continue;
            }
            if (isSamePrice(previousPeriod.getAppliedOffer().getPrice(), currentPeriod.getAppliedOffer().getPrice())) {
                previousPeriod.setEndDate(currentPeriod.getEndDate());
            } else {//a new price
                mergedPeriods.add(currentPeriod);
                previousPeriod = currentPeriod;
            }
        }
        return mergedPeriods;
    }

    private boolean isSamePrice(BigDecimal price1, BigDecimal price2) {
        return Math.abs(price1.doubleValue() - price2.doubleValue()) < 1e-3;
    }

    private void setApplicableOffer(PricingPeriod period, List<Offer> offerList) {
        offerList.stream().forEach(currentOffer -> {
            Offer appliedOffer = period.getAppliedOffer();
            if (appliedOffer != null) {
                //applied offer's priority is higher => nothing to do
                if (appliedOffer.getPriority() > currentOffer.getPriority()) {
                    return;
                }
            }

            Instant offerStartDate = DateUtil.convertToInstant(currentOffer.getStartDate());
            Instant offerEndDate = DateUtil.convertToInstant(currentOffer.getEndDate());
            //offer's start date and end date are not applicable for current period
            if (!isPeriodApplicable(period, offerStartDate, offerEndDate)) {
                return;
            }

            period.setAppliedOffer(currentOffer);
        });
    }

    private static boolean isPeriodApplicable(PricingPeriod period, Instant offerStartDate, Instant offerEndDate) {
        return offerStartDate.getEpochSecond() <= period.getStartDate().getEpochSecond() &&
                offerEndDate.getEpochSecond() >= period.getEndDate().getEpochSecond();
    }

    private List<PricingPeriod> splitIntoPeriods(List<Instant> sortedDateList) {
        List<PricingPeriod> periods = new ArrayList<>();
        //the first one
        Instant startInstant = sortedDateList.get(0);
        for (int i = 1; i < sortedDateList.size(); i++) {
            Instant currentInstant = sortedDateList.get(i);
            if (startInstant.isBefore(currentInstant)) {
                periods.add(new PricingPeriod(startInstant, currentInstant));
                startInstant = currentInstant;
            }
        }
        return periods;
    }

}
