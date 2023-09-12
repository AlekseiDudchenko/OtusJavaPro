package de.dudchenko.app;

import de.dudchenko.Apple;
import de.dudchenko.Box;
import de.dudchenko.Fruit;
import de.dudchenko.Orange;

public class Main {
    public static void main(String[] args) {

        Fruit apple1 = new Apple(0.1);
        Fruit apple2 = new Apple(0.2);
        Fruit apple3 = new Apple(0.3);

        Fruit orange1 = new Orange(0.11);
        Fruit orange2 = new Orange(0.22);
        Fruit orange3 = new Orange(0.33);

        Box<Fruit> fruitBox = new Box<>(apple1, apple2, orange1, orange2);

        for (Fruit f : fruitBox.getFruits()) {
            System.out.println(f.toString());
        }

        fruitBox.addFruit(apple3);
        fruitBox.addFruit(orange3);

        for (Fruit f : fruitBox.getFruits()) {
            System.out.println("\n");
            System.out.println(f.toString());
        }

        Box<Apple> appleBox1 = new Box<>(new Apple(0.4), new Apple(0.5));
        Box<Apple> appleBox2 = new Box<>(new Apple(0.4), new Apple(0.5));
        Box<Orange> orangeBox1 = new Box<>(new Orange(0.44), new Orange(0.55));
        Box<Orange> orangeBox2 = new Box<>(new Orange(0.44), new Orange(0.55));

        appleBox1.transferTo(appleBox2);
        orangeBox1.transferTo(orangeBox2);
    }
}
