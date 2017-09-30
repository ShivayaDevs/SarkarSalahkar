package hackerrepublic.sarkarsalahkar.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Data model for storing user's posts.
 */
@IgnoreExtraProperties
public class Post {

    public String author;
    public String title;
    public String description;
    public String imageUrl;
    public String creationTimestamp;
    public int numStars = 0;
    public int escalationLevel = 0;

    public Post() {
        // Required for call to DataSnapshot.getValue(Post.class);
    }

    /**
     * Posts will be build using builder pattern.
     */
    public static class Builder {
        Post post;

        public Builder() {
            post = new Post();
        }

        public Builder setAuthor(String author) {
            this.post.author = author;
            return this;
        }

        public Builder setCreationTimestamp(String timestamp) {
            this.post.creationTimestamp = timestamp;
            return this;
        }

        public Builder setTitle(String title) {
            post.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            post.description = description;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            post.imageUrl = imageUrl;
            return this;
        }

        public Builder setNumStars(int numStars) {
            post.numStars = numStars;
            return this;
        }

        public Builder setEscalationLevel(int escalationLevel) {
            post.escalationLevel = escalationLevel;
            return this;
        }

        public Post build() {
            return this.post;
        }
    }
}
