package de.dudchenko;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {

    private ArrayList<T> fruits;

    public Box(ArrayList<T> fruits) {
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
        return this.getWeight() == b2.getWeight();
    }

    public void addFruit(T fruit) {
        fruits.add(fruit);
    }

    public void transferTo(Box<T> anotherBox) {
        if (this.equals(anotherBox)) {
            return;
        }

        anotherBox.fruits.addAll(this.fruits);
        this.fruits.clear();
    }

    public ArrayList<T> getFruits() {
        return fruits;
    }

    public double getWeight() {
        return weight();
    }
}
