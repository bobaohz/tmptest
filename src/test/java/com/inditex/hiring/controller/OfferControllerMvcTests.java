package com.inditex.hiring.controller;

import com.inditex.hiring.TestConstants;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.service.OfferService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfferController.class)
class OfferControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;


    @Test
    void whenCreateNewOfferThenShouldReturn201() throws Exception {

        String testOfferId = Long.toString(new Random().nextLong());
        String requestBody = String.format("{\n" +
                "    \"offerId\": %s,\n" +
                "    \"brandId\": 1,\n" +
                "    \"startDate\": \"2020-06-14T00.00.00Z\",\n" +
                "    \"endDate\": \"2020-12-31T23.59.59Z\",\n" +
                "    \"priceListId\": 1,\n" +
                "    \"productPartnumber\": \"0001002\",\n" +
                "    \"priority\": 0,\n" +
                "    \"price\": 35.50,\n" +
                "    \"currencyIso\": \"EUR\"\n" +
                "}", testOfferId);
        mockMvc.perform(post("/offer").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        Offer mockedOffer = new Offer(Long.valueOf(testOfferId), TestConstants.BRAND_ID_ZARA, "2020-06-14T00.00.00Z", "2020-06-14T01.00.00Z", 1l,
                "0001002",
                0, BigDecimal.valueOf(100.99), "EUR");
        given(offerService.getOffersById(Long.valueOf(testOfferId)))
                .willReturn(mockedOffer);
        MvcResult getOfferResult = mockMvc.perform(get("/offer/" + testOfferId))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = getOfferResult.getResponse().getContentAsString();
        Assertions.assertTrue(responseString.contains(testOfferId));

    }

//    @Test
//    void whenDeleteBookWithEmployeeRoleThenShouldReturn204()
//            throws Exception
//    {
//        var isbn = "7373731394";
//        mockMvc
//      .perform(MockMvcRequestBuilders.delete("/books/" + isbn)
//            .with(SecurityMockMvcRequestPostProcessors.jwt()
//                    .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
//            .andExpect(MockMvcResultMatchers.status().isNoContent());
//    }
//
//    @Test
//    void whenGetBooksWithEmployeeRoleThenShouldReturn204()
//            throws Exception
//    {
//        mockMvc
//                .perform(MockMvcRequestBuilders.get("/books")
//                        .with(SecurityMockMvcRequestPostProcessors.jwt()
//                                .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        mockMvc
//                .perform(MockMvcRequestBuilders.get("/books")
//                        .with(SecurityMockMvcRequestPostProcessors.jwt()
//                                .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        mockMvc
//                .perform(MockMvcRequestBuilders.get("/books"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    void whenDeleteBookWithCustomerRoleThenShouldReturn403()
//            throws Exception
//    {
//        var isbn = "7373731394";
//        mockMvc
//      .perform(MockMvcRequestBuilders.delete("/books/" + isbn)
//            .with(SecurityMockMvcRequestPostProcessors.jwt()
//                    .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
//            .andExpect(MockMvcResultMatchers.status().isForbidden());
//    }
//
//    @Test
//    void whenDeleteBookNotAuthenticatedThenShouldReturn401()
//            throws Exception
//    {
//        var isbn = "7373731394";
//        mockMvc
//                .perform(MockMvcRequestBuilders.delete("/books/" + isbn))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
//    }
}
