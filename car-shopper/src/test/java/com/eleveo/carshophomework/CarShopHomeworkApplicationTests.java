package com.eleveo.carshophomework;

import com.eleveo.carshophomework.shopper.CarFilter;
import com.eleveo.carshophomework.shopper.CarShopper;
import com.eleveo.carshophomework.shopper.FilteredCarShopper;
import com.eleveo.carshophomework.shopper.filter.SortedListTopCarFilter;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@WireMockTest(httpPort = 8444)
class CarShopHomeworkApplicationTests {

    private static final String BASE_URL = "http://localhost:8444/";

    // I put configuration variables out, perhaps they can be modified for test by outside environment or something.
    private static final int MAX_CAR_COUNT = 5;
    private static final int MAX_SINGLE_ENGINE_TYPE_COUNT = 2;

    private final CarFilter filter = new SortedListTopCarFilter(MAX_CAR_COUNT, MAX_SINGLE_ENGINE_TYPE_COUNT);
    private final CarShopper carShopper = new FilteredCarShopper(filter);

    @Test
    void singleCarOrder() {
        stubFor(get("/cars").willReturn(ok()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("single-car.json")
        ));
        stubFor(post("/orders").willReturn(noContent()));

        carShopper.selectAndBuyCars(BASE_URL);

        verify(1, postRequestedFor(urlPathEqualTo("/orders")));
        verify(postRequestedFor(urlPathEqualTo("/orders"))
                .withRequestBody(equalToJson("{\"carId\": \"79c7faaf-d0af-304e-94aa-6e426ae1d07a\"}")));

    }

    @Test
    void multipleCarOrder() {
        stubFor(get("/cars").willReturn(ok()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("cars.json")
        ));
        stubFor(post("/orders").willReturn(noContent()));

        carShopper.selectAndBuyCars(BASE_URL);

        verify(5, postRequestedFor(urlPathEqualTo("/orders")));
        verify(postRequestedFor(urlPathEqualTo("/orders"))
                .withRequestBody(equalToJson("{\"carId\": \"23c49169-c73a-328e-9b92-d5018d8a235e\"}")));
        verify(postRequestedFor(urlPathEqualTo("/orders"))
                .withRequestBody(equalToJson("{\"carId\": \"29b64088-2805-368f-b50a-b35b7a6b9655\"}")));
        verify(postRequestedFor(urlPathEqualTo("/orders"))
                .withRequestBody(equalToJson("{\"carId\": \"94733846-bfa6-3aaf-8b1d-199a1dc187b7\"}")));
        verify(postRequestedFor(urlPathEqualTo("/orders"))
                .withRequestBody(equalToJson("{\"carId\": \"c5e70584-9505-3acb-a23a-1891fec4d223\"}")));
        verify(postRequestedFor(urlPathEqualTo("/orders"))
                .withRequestBody(equalToJson("{\"carId\": \"26a2664c-a44f-3b86-91d5-bbd4cf3cdda0\"}")));
    }
}
