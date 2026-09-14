package main.dto;

public class Cat extends Animal {
    public Cat() {
        super();
    }

    public Cat(String name, int age, double weight, String color) {
        super(name, age, weight, color);
    }

    public Cat(int id, String name, int age, double weight, String color) {
        super(id, name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("мяу");
    }

    @Override
    public String getType() {
        return "cat";
    }
}