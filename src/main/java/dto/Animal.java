package dto;

public abstract class Animal {
    private int id;
    private String name;
    private int age;
    private double weight;
    private String color;

    public Animal() {
    }

    public Animal(String name, int age, double weight, String color) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.color = color;
    }

    public Animal(int id, String name, int age, double weight, String color) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public abstract void say();

    public abstract String getType();

    public void go() {
        System.out.println("Я иду");
    }

    public void drink() {
        System.out.println("Я пью");
    }

    public void eat() {
        System.out.println("Я ем");
    }

    @Override
    public String toString() {
        return "ID"
                + id
                + "Привет! Меня зовут "
                + name
                + ", мне "
                + age
                + " лет, я вешу - "
                + weight
                + " кг, мой цвет - "
                + color
                + ".";
    }

    private String getAgeword(int age) {
        int lastTwoDigits = Math.abs(age) % 100;
        int lastDigit = Math.abs(age) % 10;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 14) {
            return "лет";
        }
        if (lastDigit == 1) {
            return "год";
        }

        if (lastDigit > 2 && lastDigit <= 4) {
            return "года";
        }
        return "лет";
    }
}