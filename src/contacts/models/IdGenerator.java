package contacts.models;

public class IdGenerator {
    private static IdGenerator instance = new IdGenerator();
    private long lastId = 0;

    private IdGenerator() {

    }

    public static IdGenerator getInstance() {
        return instance;
    }

    public long getNextId() {
        return ++lastId;
    }
}
