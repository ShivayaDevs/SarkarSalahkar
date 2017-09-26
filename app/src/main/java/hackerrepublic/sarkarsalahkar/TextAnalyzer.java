package hackerrepublic.sarkarsalahkar;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Analyzes the idea text to find the domain to which the idea belongs to. The search is done on
 * the basis of keywords. Currently, the keywords are added only for a few domains, but can be
 * easily added for other domains too.
 *
 * @author vermayash8
 */
public class TextAnalyzer {

    /**
     * These keywords will be used to test the tags present in a question which will help the
     * system decide intelligently to which domain experts the idea should be forwarded to.
     */
    private Map<String, List<String>> keywordsMap;

    private static final String TAG = TextAnalyzer.class.getSimpleName();


    public TextAnalyzer(Context context) {
        setup(context);
    }

    /**
     * Sets up the text analyzer to be ready.
     *
     * @param context
     */
    private void setup(Context context) {
        String jsonString = null;
        try (Scanner sc = new Scanner(context.getResources().openRawResource(R.raw
                .analyzer_dataset))) {
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(sc.nextLine());
                sb.append('\n');
            }
            jsonString = sb.toString();
            initializeKeywordsMap(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the read json and makes it usable.
     *
     * @param json json string from which data will be retrieved.
     */
    private void initializeKeywordsMap(String json) throws Exception {
        Log.d("TAG", "onCreate: " + json);
        JSONArray jsonArray = new JSONArray(json);
        keywordsMap = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONArray arrayKeywords = jsonObject.getJSONArray("tags");
            String tagName = jsonObject.getString("tagName");
            ArrayList<String> keywords = new ArrayList<>();
            for (int j = 0; j < arrayKeywords.length(); ++j) {
                keywords.add(arrayKeywords.getString(i));
            }
            keywordsMap.put(tagName, keywords);
        }
        Log.d(TAG, " " + keywordsMap);
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
        for (Map.Entry<String, List<String>> e : keywordsMap.entrySet()) {
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
