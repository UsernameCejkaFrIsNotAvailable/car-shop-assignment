package com.eleveo.carshophomework.shopper.filter;

import com.eleveo.carshop.client.model.Car;
import com.eleveo.carshophomework.shopper.CarFilter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is just experiment how compact it can be with streams, but with multiple sorting. Didn't write performance test though.
 */
public class SortedStreamTopCarFilter implements CarFilter {

    private final int maxCarCount;
    private final int maxSingleEngineTypeCount;

    public SortedStreamTopCarFilter(int maxCarCount, int maxSingleEngineTypeCount) {
        this.maxCarCount = maxCarCount;
        this.maxSingleEngineTypeCount = maxSingleEngineTypeCount;
    }

    @Override
    public List<Car> filter(List<Car> cars) {
        return cars.stream()
                .collect(Collectors.groupingBy(Car::getEngineType))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparingInt(Car::getPowerWatts).reversed())
                        .limit(maxSingleEngineTypeCount))
                .sorted(Comparator.comparingInt(Car::getPowerWatts).reversed())
                .limit(maxCarCount)
                .collect(Collectors.toList());
    }
}
