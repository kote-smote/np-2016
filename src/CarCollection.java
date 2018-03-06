import sun.reflect.generics.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by martin on 12/28/16.
 */
public class CarCollection {
    List<Car> cars;
    Map<String, TreeSet<Car>> carsByManufacturer;

    public CarCollection() {
        this.cars = new ArrayList<>();
        this.carsByManufacturer = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public void addCar(Car car) {
        this.cars.add(car);
        this.carsByManufacturer.computeIfPresent(car.getManufacturer(),
                (key, set) -> {
                    set.add(car);
                    return set;
                });
        TreeSet<Car> s = this.carsByManufacturer.computeIfAbsent(car.getManufacturer(),
                key -> new TreeSet<Car>(Comparator.comparing(Car::getModel)));
        if (s != null)
            s.add(car);
    }

    public void sortByPrice(boolean ascending) {
        Comparator<Car> Cmpr = Comparator.comparing(Car::getPrice).thenComparing(Car::getPower);
        if (ascending)
            this.cars.sort(Cmpr);
        else
            this.cars.sort(Cmpr.reversed());
    }

    public List<Car> filterByManufacturer(String manufacturer) {
        return this.carsByManufacturer.get(manufacturer)
                .stream()
                .collect(Collectors.toList());
    }

    public List<Car> getList() {
        return this.cars;
    }
}

class Car {
    private String manufacturer;
    private String model;
    private int price;
    private float power;

    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public float getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d", manufacturer, model, power, price);
    }
}
