import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

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
        assertNotNull(wr, "Recommender should not be null after init");
        assertTrue(wr.getDictionary().size() > 0, "Dictionary should have some words");
    }

    @Test
    public void testSimilarity_basicCase() throws IOException {
        WordRecommender wr = makeRecommender();
        // “cat” and “car” are similar, “cat” and “dog” are not
        double sim1 = wr.getSimilarity("cat", "car");
        double sim2 = wr.getSimilarity("cat", "dog");

        assertTrue(sim1 > sim2, "cat vs car should be more similar than cat vs dog");
        // just sanity check that values are between 0 and 1
        assertTrue(sim1 >= 0 && sim1 <= 1, "Similarity should be between 0 and 1");
    }

    @Test
    public void testSimilarity_sameWord() throws IOException {
        WordRecommender wr = makeRecommender();
        // identical words should have similarity = 1
        double sim = wr.getSimilarity("test", "test");
        assertEquals(1.0, sim, 0.0001, "Identical words should have max similarity");
    }

    @Test
    public void testSimilarity_completelyDifferent() throws IOException {
        WordRecommender wr = makeRecommender();
        double sim = wr.getSimilarity("hello", "zebra");
        // expecting low similarity, not 1, not negative
        assertTrue(sim < 0.5, "Completely different words should have low similarity");
    }

    @Test
    public void testWordSuggestions_returnsList() throws IOException {
        WordRecommender wr = makeRecommender();
        // test with a slightly misspelled word
        ArrayList<String> suggestions = wr.getWordSuggestions("recieve", 2, 0.3, 5);
        assertNotNull(suggestions, "Suggestions list should not be null");
        // not guaranteed to always return words, but usually should
        assertTrue(suggestions.size() > 0, "Should return some suggestions for a near-miss");
    }

    @Test
    public void testWordSuggestions_respectsLimit() throws IOException {
        WordRecommender wr = makeRecommender();
        // test if maxSuggestions cap works
        ArrayList<String> suggestions = wr.getWordSuggestions("hapy", 2, 0.3, 3);
        assertTrue(suggestions.size() <= 3, "Should not exceed the maxSuggestions limit");
    }

    @Test
    public void testWordSuggestions_withDifferentThresholds() throws IOException {
        WordRecommender wr = makeRecommender();
        // more relaxed threshold = more results
        ArrayList<String> lowThresh = wr.getWordSuggestions("helo", 2, 0.1, 10);
        ArrayList<String> highThresh = wr.getWordSuggestions("helo", 2, 0.8, 10);
        assertTrue(lowThresh.size() >= highThresh.size(), "Lower threshold should give more or equal suggestions");
    }

    @Test
    public void testHandlesEmptyInput() throws IOException {
        WordRecommender wr = makeRecommender();
        ArrayList<String> suggestions = wr.getWordSuggestions("", 2, 0.2, 5);
        // should not crash, just return empty list
        assertNotNull(suggestions, "Empty input should still return a list");
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
        assertTrue(wr.getDictionary().contains("the"), "Dictionary should contain 'the'");
    }

    @Test
    public void testCaseInsensitivity() throws IOException {
        WordRecommender wr = makeRecommender();
        // make sure dictionary lookup works regardless of case
        boolean lower = wr.getDictionary().contains("hello");
        boolean upper = wr.getDictionary().contains("HELLO");
        assertEquals(lower, upper, "Dictionary should be case-insensitive or standardized");
    }
}
