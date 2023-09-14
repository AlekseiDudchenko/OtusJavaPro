package de.dudchenko;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {

    private List<T> fruits;

    public Box(List<T> fruits) {
        this.fruits = fruits;
    }

    @SafeVarargs
    public Box(T... fruits) {
        this.fruits = new ArrayList<>(List.of(fruits));
    }

    public double weight() {
        double weight = 0;
        for (T s : fruits) {
            weight += s.getWeight();
        }

        return weight;
    }

    public boolean compare(Box<T> b2) {
        if (b2 == null) {
            return false;
        }

        double epsilon = 0.0001;
        return Math.abs(this.getWeight() - b2.getWeight()) < epsilon;
    }

    public void addFruit(T fruit) {
        fruits.add(fruit);
    }

    public void transferTo(Box<T> anotherBox) {
        if (anotherBox == null || this == anotherBox) {
            return;
        }

        anotherBox.fruits.addAll(this.fruits);
        this.fruits.clear();
    }

    public List<T> getFruits() {
        return fruits;
    }

    public double getWeight() {
        return weight();
    }
}
