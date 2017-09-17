package hackerrepublic.sarkarsalahkar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class User {

    public String name;
    public String bio;
    public Map<String, Integer> rating;

    public User() {
        // Required by firebase.
    }
}
