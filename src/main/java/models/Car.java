package models;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@ToString
public class Car {
    private CarMaker maker;
    private String color;
    private String model;
    private int year;
}
