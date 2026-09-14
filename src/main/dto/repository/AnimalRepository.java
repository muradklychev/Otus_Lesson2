package main.dto.repository;

import main.dto.Animal;
import main.dto.Cat;
import main.dto.Dog;
import main.dto.Duck;
import main.dto.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnimalRepository {

    public void save(Animal animal) {
        String sql = """
                INSERT INTO animals (animal_type, name, age, weight, color)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, animal.getType());
            statement.setString(2, animal.getName());
            statement.setInt(3, animal.getAge());
            statement.setDouble(4, animal.getWeight());
            statement.setString(5, animal.getColor());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    animal.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Ошибка при сохранении животного: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    public List<Animal> findAll() {
        String sql = """
                SELECT id, animal_type, name, age, weight, color
                FROM animals
                ORDER BY id
                """;

        List<Animal> animals = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                animals.add(mapRow(resultSet));
            }

            return animals;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Ошибка при получении животных: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    public List<Animal> findByType(String type) {
        String sql = """
                SELECT id, animal_type, name, age, weight, color
                FROM animals
                WHERE animal_type = ?
                ORDER BY id
                """;

        List<Animal> animals = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, type);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    animals.add(mapRow(resultSet));
                }
            }

            return animals;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Ошибка при фильтрации животных: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    public boolean update(Animal animal) {
        String sql = """
                UPDATE animals
                SET animal_type = ?, name = ?, age = ?, weight = ?, color = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, animal.getType());
            statement.setString(2, animal.getName());
            statement.setInt(3, animal.getAge());
            statement.setDouble(4, animal.getWeight());
            statement.setString(5, animal.getColor());
            statement.setInt(6, animal.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Ошибка при изменении животного: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM animals WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Ошибка при удалении животного: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    private Animal mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String type = resultSet.getString("animal_type");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        double weight = resultSet.getDouble("weight");
        String color = resultSet.getString("color");

        return switch (type) {
            case "cat" -> new Cat(id, name, age, weight, color);
            case "dog" -> new Dog(id, name, age, weight, color);
            case "duck" -> new Duck(id, name, age, weight, color);
            default -> throw new IllegalArgumentException(
                    "Неизвестный тип животного в базе: " + type
            );
        };
    }
}
