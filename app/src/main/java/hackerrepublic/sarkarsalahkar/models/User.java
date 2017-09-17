package hackerrepublic.sarkarsalahkar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class User {

    public String name;
    public String bio;
    public String dpUri;

    public int level;
    public Map<String, Integer> rating;

    public User() {
        // Required by firebase.
    }

    public User(String name, String bio, String dpUri, int level, Map<String, Integer> rating) {
        this.name = name;
        this.bio = bio;
        this.dpUri = dpUri;
        this.level = level;
        this.rating = rating;
    }

}
