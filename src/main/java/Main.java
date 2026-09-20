package main;

import service.Command;
import repository.AnimalRepository;
import dto.*;

import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        AnimalRepository repository = new AnimalRepository();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String input = scanner.nextLine();
                Command command = Command.fromString(input);
                if (command == null) {
                    System.out.println("Неизвестная команда");
                    continue;
                }
                try {
                    switch (command) {
                        case ADD:
                            addAnimal(scanner, repository);
                            break;
                        case LIST:
                            printAnimals(repository.findAll());
                            break;
                        case EDIT:
                            editAnimal(scanner, repository);
                            break;
                        case FILTER:
                            filterAnimals(scanner, repository);
                            break;
                        case DELETE:
                            deleteAnimal(scanner, repository);
                            break;
                        case EXIT:
                            running = false;
                            System.out.println("Программа завершена");
                            break;
                    }
                } catch (RuntimeException exception) {
                    System.out.println(exception.getMessage());

                    if (exception.getCause() != null) {
                        System.out.println("Причина: " + exception.getCause().getMessage());
                    }

                    exception.printStackTrace();
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Введите команду:");
        System.out.println("add    - добавить животное");
        System.out.println("list   - показать всех животных");
        System.out.println("edit   - изменить животное");
        System.out.println("filter - найти по типу");
        System.out.println("delete - удалить животное");
        System.out.println("exit   - выйти");
    }

    private static void addAnimal(
            Scanner scanner,
            AnimalRepository repository
    ) {
        Animal animal = readAnimalWithoutId(scanner);
        repository.save(animal);
        System.out.println("Животное сохранено");
        System.out.println("Присвоенный ID: " + animal.getId());
        animal.say();
    }

    private static void editAnimal(
            Scanner scanner,
            AnimalRepository repository
    ) {
        int id = readInt(scanner, "Введите ID животного:");
        System.out.println("Введите новые данные");
        Animal newAnimal = readAnimalWithoutId(scanner);
        newAnimal.setId(id);
        boolean updated = repository.update(newAnimal);
        if (updated) {
            System.out.println("Животное изменено");
        } else {
            System.out.println("Животное с таким ID не найдено");
        }
    }

    private static void filterAnimals(
            Scanner scanner,
            AnimalRepository repository
    ) {
        String type = readAnimalType(scanner);
        List<Animal> animals = repository.findByType(type);
        printAnimals(animals);
    }

    private static void deleteAnimal(
            Scanner scanner,
            AnimalRepository repository
    ) {
        int id = readInt(scanner, "Введите ID животного:");
        boolean deleted = repository.deleteById(id);
        if (deleted) {
            System.out.println("Животное удалено");
        } else {
            System.out.println("Животное с таким ID не найдено");
        }
    }

    private static Animal readAnimalWithoutId(Scanner scanner) {
        String type = readAnimalType(scanner);
        String name = readString(scanner, "Введите имя:");
        int age = readInt(scanner, "Введите возраст:");
        double weight = readDouble(scanner, "Введите вес:");
        String color = readString(scanner, "Введите цвет:");
        validateAge(age);
        validateWeight(weight);
        return switch (type) {
            case "cat" -> new Cat(name, age, weight, color);
            case "dog" -> new Dog(name, age, weight, color);
            case "duck" -> new Duck(name, age, weight, color);
            default -> throw new IllegalArgumentException(
                    "Неизвестный тип животного"
            );
        };
    }

    private static String readAnimalType(Scanner scanner) {
        while (true) {
            String type = readString(
                    scanner,
                    "Введите тип животного: cat/dog/duck"
            ).toLowerCase();
            if (type.equals("cat")
                    || type.equals("dog")
                    || type.equals("duck")) {
                return type;
            }
            System.out.println("Можно выбрать только cat, dog или duck");
        }
    }

    private static String readString(
            Scanner scanner,
            String message
    ) {
        System.out.println(message);
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                    "Строковое значение не может быть пустым"
            );
        }
        return value;
    }

    private static int readInt(
            Scanner scanner,
            String message
    ) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Введите целое число");
            }
        }
    }

    private static double readDouble(
            Scanner scanner,
            String message
    ) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine()
                    .trim()
                    .replace(',', '.');
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException exception) {
                System.out.println("Введите число");
            }
        }
    }

    private static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException(
                    "Возраст не может быть отрицательным"
            );
        }
    }

    private static void validateWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException(
                    "Вес должен быть больше нуля"
            );
        }
    }

    private static void printAnimals(List<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("Животные не найдены");
            return;
        }
        for (Animal animal : animals) {
            System.out.println(animal);
        }
    }
}