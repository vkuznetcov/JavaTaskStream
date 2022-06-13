package utils;

import lombok.var;
import models.Car;
import models.CarMaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CSVReader {
    private static final String DELIMITER = ",";
    private static final String DATA_SOURCE = "src/main/resources/CAR_DATA.csv";

    private static List<Car> parseCarData() throws IOException {
        List<Car> cars = new ArrayList<>();

        getData().stream().skip(1).forEach(string -> {
            String[] carInfo = string.split(DELIMITER);
            String carModel = carInfo[0];

            String makerName = carInfo[1];
            CarMaker carMaker = CarMaker.builder().name(makerName).build();

            int carYear = Integer.parseInt(carInfo[2]);
            String carColor = carInfo[3];

            cars.add(Car.builder().model(carModel).maker(carMaker).year(carYear).color(carColor).build());
        });
        return cars;
    }

    private static List<String> getData() throws IOException {
        return Files.lines(Paths.get(DATA_SOURCE)).collect(Collectors.toList());
    }

    private static Map<String, List<Car>> groupCarsByColor(List<Car> cars) {
        return cars.stream().collect(Collectors.groupingBy(Car::getColor));
    }

    private static Map<CarMaker, List<Car>> groupCarsByCarMaker(List<Car> cars) {
        return cars.stream().collect(Collectors.groupingBy(Car::getMaker));
    }

    private static Map<CarMaker, List<Car>> sortMakers(Map<CarMaker, List<Car>> group) {
        Map<CarMaker, List<Car>> handledMap = new HashMap<>(group);

        handledMap.entrySet().removeIf(carMakerListEntry -> (carMakerListEntry.getValue().size() <= 2
                || carMakerListEntry.getKey().getName().equals("")));

        Map<CarMaker, List<Car>> result = new TreeMap<>();
        SortedSet<CarMaker> keys = new TreeSet<>(handledMap.keySet());
        for (CarMaker carMaker : keys) {
            result.put(carMaker, handledMap.get(carMaker));
        }

        return result;
    }

    private static <T> void printResult(List<T> data){
        data.forEach(System.out::println);
        System.out.println("\n\n\n\n\n");
    }

    private static List<CarMaker> mapSetToList(Map<CarMaker, List<Car>> data){
        Set<Map.Entry<CarMaker, List<Car>>> set = data.entrySet();
        List<CarMaker> result = new ArrayList<>();
        for(Map.Entry<CarMaker, List<Car>> entry : set){
            result.add(entry.getKey());
        }
        return result;
    }

    private static <T> void printResult(Map<T, List<Car>> data) {
        data.forEach(((T key, List<Car> cars) -> {
            System.out.print(key + " -> ");
            System.out.println(cars);
        }));
        System.out.println("\n\n\n\n\n");
    }

    public static void main(String[] args) throws IOException {
        printResult(parseCarData());
        printResult(groupCarsByColor(parseCarData()));
        var a = mapSetToList(groupCarsByCarMaker(parseCarData())).stream().map(String::valueOf).collect(Collectors.joining(DELIMITER));
        System.out.println(Arrays.toString(a.split(DELIMITER)) + "\n\n\n\n\n");
        printResult(sortMakers(groupCarsByCarMaker(parseCarData())));
    }

}
