package com.eleveo.carshophomework.shopper;

import com.eleveo.carshop.client.model.Car;

import java.util.List;

/**
 * Car selection algorithm should not be part of whole shopping process. Also, I want to try multiple implementations, out of curiosity.
 */
public interface CarFilter {

    /**
     * Filter list of available cars.
     *
     * @param cars Available cars.
     * @return Filtered cars.
     */
    List<Car> filter(List<Car> cars);
}
