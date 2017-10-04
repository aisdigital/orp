package example.orp.model;

import org.parceler.Parcel;

/**
 * Created by Jonathan Nobre Ferreira on 07/12/16.
 */
@Parcel(Parcel.Serialization.BEAN)
public class User {

    String name;

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    /* Getters and Setters */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
