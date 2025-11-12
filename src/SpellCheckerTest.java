import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

// Basic JUnit tests for SpellChecker.java
// Focused on verifying setup, interaction with WordRecommender and that main methods don't crash unexpectedly.

public class SpellCheckerTest {

    // helper: create a SpellChecker safely without crashing
    private SpellChecker createChecker() throws IOException {
        return new SpellChecker("engDictionary.txt");
    }

    @Test
    public void testConstructor_runsWithoutError() throws IOException {
        // just checking that it initializes correctly with a valid dictionary
        SpellChecker sc = createChecker();
        assertNotNull(sc, "SpellChecker should not be null after init");
        //MESSAGE SHOULD BE LAST PARAMETER
    }

    /**
    @Test
    public void testSpellChecker_usesRecommenderInternally() throws IOException {
        // we assume SpellChecker internally creates or uses a WordRecommender
        SpellChecker sc = createChecker();
        assertNotNull("SpellChecker should have a WordRecommender", sc.getWordRecommender());
        //I CAN'T ACCESS WORDRECOMMENDER ETC. MAKE THESE METHODS PUBLIC
    }
    **/

    @Test
    public void testCheckWord_validWord() throws IOException {
        SpellChecker sc = createChecker();
        boolean result = sc.isWordValid("hello");
        // should exist in dictionary
        assertTrue(result,"Common word should be recognized");
    }

    @Test
    public void testCheckWord_invalidWord() throws IOException {
        SpellChecker sc = createChecker();
        boolean result = sc.isWordValid("zzzzzzzz");
        // should not exist in dictionary
        assertFalse(result,"Nonsense word should be invalid");
    }

    @Test
    public void testInteractiveCorrection_simulatedInput() throws IOException {
        // simulate user typing in corrected word
        String simulatedUserInput = "word\nexit\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        SpellChecker sc = createChecker();

        // run the main check loop (assuming it takes user input)
        // note: we're not asserting heavy behavior here - just making sure no exceptions
        try {
            sc.runSpellChecker();
        } catch (Exception e) {
            fail("SpellChecker should handle simulated input gracefully");
        }
    }

    @Test
    public void testSuggestWords_returnsList() throws IOException {
        SpellChecker sc = createChecker();
        // pretend we’re calling into its suggestion helper
        var suggestions = sc.suggestCorrections("mispell", 2, 0.2, 3);
        assertNotNull(suggestions, "Should return a suggestion list");
        assertTrue("Should return at least 1 suggestion for a close misspell", suggestions.size() > 0);
    }

    @Test
    public void testToleranceChangesSuggestions() throws IOException {
        SpellChecker sc = createChecker();
        var fewTol = sc.suggestCorrections("sleepy", 1, 0.1, 5);
        var moreTol = sc.suggestCorrections("sleepy", 3, 0.1, 5);
        // more tolerance = more results usually
        assertTrue("Higher tolerance should lead to more or equal results", moreTol.size() >= fewTol.size());
    }

    @Test
    public void testGracefulFailureOnBadFile() {
        // test with a bogus dictionary file name
        try {
            SpellChecker sc = new SpellChecker("fakeFile.txt");
            fail("Expected IOException due to missing file");
        } catch (IOException e) {
            // expected, no action needed
        }
    }
}
