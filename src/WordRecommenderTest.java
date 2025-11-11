import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.io.IOException;

// Tests for WordRecommender.java
// These tests focus on checking that the main methods work logically, handle weird cases gracefully, and return the right results
// (Note: assumes "dictionary.txt" exists in project root with valid words)

public class WordRecommenderTest {

    // helper method to safely create the recommender
    private WordRecommender makeRecommender() throws IOException {
        return new WordRecommender("dictionary.txt");
    }

    @Test
    public void testConstructorLoadsDictionary() throws IOException {
        // check if constructor runs without crashing and dictionary actually loads
        WordRecommender wr = makeRecommender();
        assertNotNull("Recommender should not be null after init", wr);
        assertTrue("Dictionary should have some words", wr.getDictionary().size() > 0);
    }

    @Test
    public void testSimilarity_basicCase() throws IOException {
        WordRecommender wr = makeRecommender();
        // “cat” and “car” are similar, “cat” and “dog” are not
        double sim1 = wr.getSimilarity("cat", "car");
        double sim2 = wr.getSimilarity("cat", "dog");

        assertTrue("cat vs car should be more similar than cat vs dog", sim1 > sim2);
        // just sanity check that values are between 0 and 1
        assertTrue("Similarity should be between 0 and 1", sim1 >= 0 && sim1 <= 1);
    }

    @Test
    public void testSimilarity_sameWord() throws IOException {
        WordRecommender wr = makeRecommender();
        // identical words should have similarity = 1
        double sim = wr.getSimilarity("test", "test");
        assertEquals("Identical words should have max similarity", 1.0, sim, 0.0001);
    }

    @Test
    public void testSimilarity_completelyDifferent() throws IOException {
        WordRecommender wr = makeRecommender();
        double sim = wr.getSimilarity("hello", "zebra");
        // expecting low similarity, not 1, not negative
        assertTrue("Completely different words should have low similarity", sim < 0.5);
    }

    @Test
    public void testWordSuggestions_returnsList() throws IOException {
        WordRecommender wr = makeRecommender();
        // test with a slightly misspelled word
        ArrayList<String> suggestions = wr.getWordSuggestions("recieve", 2, 0.3, 5);
        assertNotNull("Suggestions list should not be null", suggestions);
        // not guaranteed to always return words, but usually should
        assertTrue("Should return some suggestions for a near-miss", suggestions.size() > 0);
    }

    @Test
    public void testWordSuggestions_respectsLimit() throws IOException {
        WordRecommender wr = makeRecommender();
        // test if maxSuggestions cap works
        ArrayList<String> suggestions = wr.getWordSuggestions("hapy", 2, 0.3, 3);
        assertTrue("Should not exceed the maxSuggestions limit", suggestions.size() <= 3);
    }

    @Test
    public void testWordSuggestions_withDifferentThresholds() throws IOException {
        WordRecommender wr = makeRecommender();
        // more relaxed threshold = more results
        ArrayList<String> lowThresh = wr.getWordSuggestions("helo", 2, 0.1, 10);
        ArrayList<String> highThresh = wr.getWordSuggestions("helo", 2, 0.8, 10);
        assertTrue("Lower threshold should give more or equal suggestions", lowThresh.size() >= highThresh.size());
    }

    @Test
    public void testHandlesEmptyInput() throws IOException {
        WordRecommender wr = makeRecommender();
        ArrayList<String> suggestions = wr.getWordSuggestions("", 2, 0.2, 5);
        // should not crash, just return empty list
        assertNotNull("Empty input should still return a list", suggestions);
    }

    @Test
    public void testGracefulHandlingOfInvalidFile() {
        // check that bad file names don’t crash the program brutally
        try {
            new WordRecommender("not_a_real_file.txt");
            fail("Expected IOException for invalid file path");
        } catch (IOException e) {
            // expected, do nothing
        }
    }

    @Test
    public void testDictionaryContainsCommonWord() throws IOException {
        WordRecommender wr = makeRecommender();
        // just a sanity check that dictionary includes something obvious
        assertTrue("Dictionary should contain 'the'", wr.getDictionary().contains("the"));
    }

    @Test
    public void testCaseInsensitivity() throws IOException {
        WordRecommender wr = makeRecommender();
        // make sure dictionary lookup works regardless of case
        boolean lower = wr.getDictionary().contains("hello");
        boolean upper = wr.getDictionary().contains("HELLO");
        assertEquals("Dictionary should be case-insensitive or standardized", lower, upper);
    }
}
