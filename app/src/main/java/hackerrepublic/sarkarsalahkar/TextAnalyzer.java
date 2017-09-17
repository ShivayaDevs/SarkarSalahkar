package hackerrepublic.sarkarsalahkar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Analyzes the idea text to find the domain to which the idea belongs to. The search is done on
 * the basis of keywords. Currently, the keywords are added only for a few domains, but can be
 * easily added for other domains too.
 *
 * @author vermayash8
 */
public class TextAnalyzer {

    private Map<String, List<String>> map;

    /**
     * These keywords will be used to test the tags present in a question which will help the
     * system decide intelligently to which domain experts the idea should be forwarded to.
     */
    private List<String> financeKeywords = Arrays.asList("finance", "money", "cash", "interest",
            "loan", "blockchain", "cryptocurrency", "currency", "credit", "debit card",
            "banking", "bank");
    private List<String> agricultureKeywords = Arrays.asList("kisan", "farmer", "crops",
            "sugarcane", "plough", "drought", "soil", "fertilizer");

    private List<String> roadKeywords = Arrays.asList("traffic", "holes", "pit");
    private List<String> industryKeywords = Arrays.asList("industry", "pollution", "factory",
            "factories");

    private List<String> environmentKeywords = Arrays.asList("pollution", "health");

    public TextAnalyzer() {
        map = new HashMap<>();
        map.put("Finance", financeKeywords);
        map.put("Agriculture", agricultureKeywords);
        map.put("Road", roadKeywords);
        map.put("Industry", industryKeywords);
    }

    /**
     * Analyzes text and returns the associated tags with a score.
     *
     * @param text The text to be analyzed.
     * @return set containing list of tags.
     */
    public ArrayList<String> getTags(String text) {
        text = text.toLowerCase();
        ArrayList<String> tags = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : map.entrySet()) {
            String tag = e.getKey();
            for (String keyword : e.getValue()) {
                if (text.contains(keyword) && !tags.contains(tag)) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }
}
