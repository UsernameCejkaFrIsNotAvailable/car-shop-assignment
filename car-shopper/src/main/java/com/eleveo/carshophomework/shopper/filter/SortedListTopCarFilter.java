package com.eleveo.carshophomework.shopper.filter;

import com.eleveo.carshop.client.model.Car;
import com.eleveo.carshophomework.shopper.CarFilter;

import java.util.*;

/**
 * This is first solution that came to mind.
 *
 * TODO: There may be optimized variant with no sort at all. It would require to check topCars list though. May be faster for huge lists.
 */
public class SortedListTopCarFilter implements CarFilter {

    private final int maxCarCount;
    private final int maxSingleEngineTypeCount;

    public SortedListTopCarFilter(int maxCarCount, int maxSingleEngineTypeCount) {
        this.maxCarCount = maxCarCount;
        this.maxSingleEngineTypeCount = maxSingleEngineTypeCount;
    }

    @Override
    public List<Car> filter(List<Car> cars) {
        // Sort cars by power.
        cars.sort(Comparator.comparingInt(Car::getPowerWatts).reversed());
        // Prepare counter for engine types.
        Map<Car.EngineTypeEnum, Integer> engineTypeCounts = new EnumMap<>(Car.EngineTypeEnum.class);
        // Let's just expect there will be always max cars. I could leave it to default or make it half if I was expecting huge numbers.
        List<Car> topCars = new ArrayList<>(maxCarCount);

        for (Car car : cars) {
            // Check current count of this engine type and skip if were over limit.
            int currentCount = engineTypeCounts.getOrDefault(car.getEngineType(), 0);
            if (currentCount >= maxSingleEngineTypeCount) {
                continue;
            }

            // Add car to the list and increment engine count in the map.
            topCars.add(car);
            engineTypeCounts.put(car.getEngineType(), currentCount + 1);

            // Break if we have enough cars.
            if (topCars.size() == maxCarCount) {
                break;
            }
        }

        return topCars;
    }
}
