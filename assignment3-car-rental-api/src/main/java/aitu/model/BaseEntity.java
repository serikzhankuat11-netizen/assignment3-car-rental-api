package aitu.model;

import aitu.exception.InvalidInputException;

/**
 * Abstract Base Entity (requirement):
 * - fields: id, name
 * - at least 2 abstract methods
 * - at least 1 concrete method
 */
public abstract class BaseEntity {
    protected int id;
    protected String name;

    protected BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Abstract methods (2)
    public abstract String describe();
    public abstract void validate();

    // Concrete method (1)
    public String displayName() {
        return "[" + id + "] " + name;
    }

    // Encapsulation: getters/setters
    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0) throw new InvalidInputException("ID cannot be negative");
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name must not be empty");
        }
        this.name = name.trim();
    }
}
