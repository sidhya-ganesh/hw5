import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;


public class WordRecommender {

    private ArrayList<String> dictionary;           //we will use the WordRecommender constructor to build the dictionary

    /**
     * public WordRecommender(String dictionaryFile) throws IOException {
        dictionary = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(dictionaryFile);
             Scanner inputReader = new Scanner(input)) {     //feeding the contents of dictionaryFile into the scanner while theres a next line
            while (inputReader.hasNextLine()) {
                String line = inputReader.nextLine();
                dictionary.add(line);                   //adding each line into the dictionary object
            }
        }


    }
    **/
    public WordRecommender(String dictionaryFile) throws IOException {
        dictionary = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(dictionaryFile);
             Scanner inputReader = new Scanner(input)) {
            // feeding the contents of dictionaryFile into the scanner while there's a next line
            while (inputReader.hasNextLine()) {
                String line = inputReader.nextLine().trim();
                if (!line.isEmpty()) {
                    dictionary.add(line);
                }
            }
        } /*catch (IOException e) {
            System.out.println("There was an error opening the dictionary file: " + dictionaryFile);
            // rethrow so the caller (SpellChecker) can handle it properly
            throw e;
        } */
    }

    public double getSimilarity(String word1, String word2) {
        int leftcount = 0;
        int min = Math.min(word1.length(), word2.length());  //min will prevent going out of bounds for the smaller word
        for (int i = 0; i < min; i++) {
            if (word1.charAt(i) == word2.charAt(i)) {       //we are aligning the letters in the words from the left, comparing each
                leftcount ++;               //the total number of letter matches after left aligning
            }
        }
        int rightcount = 0;
        int word1pos = word1.length()-1;
        int word2pos = word2.length()-1;
        while (word1pos >= 0 && word2pos >= 0) {       //once either word1pos or word2pos reaches zero, comparison stops within the bounds
            if (word1.charAt(word1pos) == word2.charAt(word2pos)) {     //comparing each letter, aligning the words along the right
                rightcount ++;
            }
            word1pos --;     //whichever word is smaller will have pos =0 when the loop ends
            word2pos --;
        }
        return (leftcount + rightcount) / 2.0;    //the "similarity"
    }

    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        ArrayList<String> alternatives = new ArrayList<>(); //building an arraylist with words satisfying tolerance and commonPercent criterions.
        for (String candidate : dictionary) {
            if ((Math.abs(candidate.length() - word.length()) <= tolerance && commonPercent <= commonPercentComp(candidate, word))) {  //covers both cases where the length of the candidate is less than or greater than the word, within "tolerance" difference.
                alternatives.add(candidate);
            }
        }
        if (alternatives.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<String> alternativesTopN = new ArrayList<>();  //making an ArrayList with the top N choices from the alternatives ArrayList
        int count = 0;
        while (count < topN && !alternatives.isEmpty())  {//we will add the best available option from alternatives to alternativesTopN TopN times
            int pos = 0; //pos will track the position of the best option in alternatives ArrayList
            double bestSimilarity = getSimilarity(word, alternatives.get(0)); //the best similarity is initialized to the first element in alternatives
            for (int j = 1; j < alternatives.size(); j++) { //we are searching through the rest of alternatives for the best candidate
                double newSimilarity = getSimilarity(word, alternatives.get(j));
                if (bestSimilarity < newSimilarity) {
                    bestSimilarity = newSimilarity;    //updating bestSimilarity for each candidate with greater similarity
                    pos = j;            //the position of the best candidate out of the remaining on the list
                }
            }
            alternativesTopN.add(alternatives.remove(pos)); //we are adding the best candidate to alternativesTopN, and removing it from alternatives
            count++;

        }

        return alternativesTopN;
    }
    private HashSet<Character> intersection(HashSet<Character> A, HashSet<Character> B) { //A \cap B
        HashSet<Character> intersection = new HashSet<>();
        for (char c : A) {
            if (B.contains(c)) {
                intersection.add(c);
            }
        }
        return intersection;

    }
    private HashSet<Character> union(HashSet<Character> A, HashSet<Character> B) { //A \cup B
        HashSet<Character> union = new HashSet<>();
        for (char c : A) {
            union.add(c);
        }
        for (char c : B) {
            union.add(c);
        }
        return union;

    }
    private HashSet<Character> stringToCharacter(String s) {  //converting a string to a character HashSet to be used in union and intersection
        HashSet<Character> set = new HashSet<>();
        for (int i = 0; i<s.length(); i++) {
            set.add(s.charAt(i));

        }
        return set;

    }
    private double commonPercentComp(String A, String B) {  // (A \cap B)/(A \cup B) -this will be the comparison against the commonPercent benchmark
        HashSet<Character> a = stringToCharacter(A);
        HashSet<Character> b = stringToCharacter(B);
        int aCapBSize = intersection(a, b).size();
        int aCupBSize = union(a, b).size();
        return (double) aCapBSize/aCupBSize;
    }

    public ArrayList<String> getDictionary() {
        return dictionary;
    }







    // You can of course write other methods as well.
}