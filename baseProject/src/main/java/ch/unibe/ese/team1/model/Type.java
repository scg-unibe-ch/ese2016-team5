package ch.unibe.ese.team1.model;

public enum Type {
    room("room"),
    studio("studio"),
    property("property");

    private final String name;

    private Type(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
