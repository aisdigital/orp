package example.orp.model;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */

@Parcel
public class User implements Serializable {

    private static final long serialVersionUID = 7339177667648583823L;

    String name;
    Integer age;
    String stream;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public User(String name, Integer age, String stream) {
        this(name, age);
        this.stream = stream;
    }

    /* Getters and Setters */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", stream=" + stream +
                '}';
    }
}
