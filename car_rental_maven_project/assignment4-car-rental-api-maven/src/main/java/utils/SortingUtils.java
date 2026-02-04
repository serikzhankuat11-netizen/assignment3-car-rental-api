package utils;

import model.VehicleBase;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortingUtils {
    public static void sortByPricePerDayAsc(List<VehicleBase> list) {
        list.sort(Comparator.comparingDouble(VehicleBase::getPricePerDay));
    }

    public static List<VehicleBase> filterCheaperThan(List<VehicleBase> list, double maxPrice) {
        return list.stream()
                .filter(v -> v.getPricePerDay() <= maxPrice)
                .collect(Collectors.toList());
    }
}
