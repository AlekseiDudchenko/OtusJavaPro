package de.dudchenko;

public abstract class Fruit {

    private double weight;

    public Fruit(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return this.getClass().toString() + " " + this.getWeight();
    }
}
