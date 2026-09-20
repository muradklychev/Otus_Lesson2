package dto;

import inteface.Flying;

public class Duck extends Animal implements Flying {
    public Duck() {
        super();
    }

    public Duck(String name, int age, double weight, String color) {
        super(name, age, weight, color);
    }

    public Duck(int id, String name, int age, double weight, String color) {
        super(id, name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Кря");
    }

    @Override
    public void fly() {
        System.out.println("Я лечу");
    }

    @Override
    public String getType() {
        return "duck";
    }
}