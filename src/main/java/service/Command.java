package service;

public enum Command {
    ADD("add"),
    LIST("list"),
    EDIT("edit"),
    FILTER("filter"),
    DELETE("delete"),
    EXIT("exit");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public static Command fromString(String input) {
        String normalizedInput = input.trim().toLowerCase();

        for (Command command : values()) {
            if (command.value.equals(normalizedInput)) {
                return command;
            }
        }
        return null;
    }
}