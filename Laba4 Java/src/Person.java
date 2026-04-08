import java.util.List;

public class Person {
    private String name;
    private List<Person> friends;

    public Person(String name, List<Person> friends) { this.name = name; this.friends = friends; }
    public String getName() { return name; }
    public List<Person> getFriends() { return friends; }
}