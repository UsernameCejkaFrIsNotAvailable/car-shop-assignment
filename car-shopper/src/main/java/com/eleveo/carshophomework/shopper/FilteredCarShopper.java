package com.eleveo.carshophomework.shopper;

import com.eleveo.carshop.client.ApiClient;
import com.eleveo.carshop.client.DefaultApi;
import com.eleveo.carshop.client.model.Order;

/**
 * Car shopper with swappable filter for wanted cars.
 */
public class FilteredCarShopper implements CarShopper {

    private final CarFilter carFilter;

    public FilteredCarShopper(CarFilter carFilter) {
        this.carFilter = carFilter;
    }

    @Override
    public void selectAndBuyCars(String serverUri) {
        var api = new DefaultApi(new ApiClient().setBasePath(serverUri));

        var availableCars = api.getCars().block().getCars();
        var filteredCars = carFilter.filter(availableCars);

        // TODO: Not sure if this is test for knowledge of spring reactive framework,
        //  surely there is a way how to chain all orders into one block, this is enough for this scenario though.
        filteredCars.forEach(car -> {
            Order order = new Order();
            order.setCarId(car.getCarId());
            api.orderCar(order).block();
        });
    }
}
