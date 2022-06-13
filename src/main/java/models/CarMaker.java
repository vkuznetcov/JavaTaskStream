package models;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@ToString
public class CarMaker implements Comparable<CarMaker>{
    private String name;


    @Override
    public int compareTo(CarMaker o) {
        return this.name.compareTo(o.getName());
    }
}
