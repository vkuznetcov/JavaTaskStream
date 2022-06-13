package utils;

import models.Car;
import models.CarMaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVReader {
    private static final String DELIMITER = ",";
    private static final String DATA_SOURCE = "src/main/resources/CAR_DATA.csv";
    private static final String RECORD_SPACE = "\n\n\n\n\n";
    private static final int MINIMUM_CARS_PER_MAKER = 3;

    private static List<Car> parseCarData(List<String> data) {
        List<Car> cars = new ArrayList<>();

        data.forEach(string -> {
            String[] carInfo = string.split(DELIMITER);
            String carModel = carInfo[0];

            String makerName = carInfo[1];
            CarMaker carMaker = CarMaker.builder()
                                .name(makerName)
                                .build();

            int carYear = Integer.parseInt(carInfo[2]);
            String carColor = carInfo[3];

            cars.add(Car.builder()
                    .model(carModel)
                    .maker(carMaker)
                    .year(carYear)
                    .color(carColor)
                    .build());
        });
        return cars;
    }

    private static List<String> getData() throws IOException {
        return Files.lines(Paths.get(DATA_SOURCE)).collect(Collectors.toList()).stream().skip(1).collect(Collectors.toList());
    }

    private static <T> Map<T, List<Car>> groupCars(Function<Car, T> function, List<Car> cars){
        return cars.stream().collect(Collectors.groupingBy(function));
    }

    private static Map<CarMaker, List<Car>> sortMakers(Map<CarMaker, List<Car>> group) {
        Map<CarMaker, List<Car>> handledMap = new HashMap<>(group);

        handledMap.entrySet().removeIf(carMakerListEntry ->
                (carMakerListEntry.getValue().size() < MINIMUM_CARS_PER_MAKER
                || "".equals(carMakerListEntry.getKey().getName())));

        Map<CarMaker, List<Car>> result = new TreeMap<>();
        SortedSet<CarMaker> keys = new TreeSet<>(handledMap.keySet());
        keys.forEach(key -> result.put(key, handledMap.get(key)));
        return result;
    }

    private static <T> void printResult(List<T> data) {
        data.forEach(System.out::println);
        System.out.println(RECORD_SPACE);
    }

    private static List<CarMaker> mapSetToList(Map<CarMaker, List<Car>> data) {
        Set<Map.Entry<CarMaker, List<Car>>> set = data.entrySet();
        List<CarMaker> result = new ArrayList<>();
        for (Map.Entry<CarMaker, List<Car>> entry : set) {
            result.add(entry.getKey());
        }
        return result;
    }

    private static <T> void printResult(Map<T, List<Car>> data) {
        data.forEach(((T key, List<Car> cars) -> {
            System.out.print(key + " -> ");
            System.out.println(cars);
        }));
        System.out.println(RECORD_SPACE);
    }

    public static void main(String[] args) throws IOException {
        List<String> data = getData();

        List<Car> parsedData = parseCarData(data);

        printResult(parsedData);

        printResult(groupCars(Car::getColor, parsedData));

        String makersList = mapSetToList(groupCars((Car::getMaker),parsedData)).stream().map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
        System.out.println(Arrays.toString(makersList.split(DELIMITER)) + RECORD_SPACE);

        printResult(sortMakers(groupCars(Car::getMaker, parsedData)));
    }

}
