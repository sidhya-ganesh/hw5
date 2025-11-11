 public class WordRecommender {

    private ArrayList<String> dictionary;           //we will use the WordRecommender constructor to build the dictionary

    public WordRecommender(String dictionaryFile) throws IOException {
        dictionary = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(dictionaryFile);
        Scanner inputReader = new Scanner(input)) {     //feeding the contents of dictionaryFile into the scanner while theres a next line
            while (inputReader.hasNextLine()) {
                String line = inputReader.nextLine();
                dictionary.add(line);                   //adding each line into the dictionary object
            }
        }


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
        while (word2pos >= 0 && 0 <= word1pos) {       //once either word1pos or word2pos reaches zero, comparison stops within the bounds
            if (word1.charAt(word1pos) == word1.charAt(word2pos)) {     //comparing each letter, aligning the words along the right
                rightcount ++;
                word1pos = word1pos -1;     //whichever word is smaller will have pos =0 when the loop ends
                word2pos = word2pos -1;
            }
        }
      return (leftcount + rightcount) / 2.0;    //the "similarity"
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      // TODO: change this!
      return null;
    }
    private HashSet<Character> intersection(HashSet<Character> A, HashSet<Character> B) {
        HashSet<Character> intersection = new HashSet<>();
        for (char c : A) {
            if (B.contains(c)) {
                intersection.add(c);
            }
        }
        return intersection;

    }
    private HashSet<Character> union(HashSet<Character> A, HashSet<Character> B) {
        HashSet<Character> union = new HashSet<>();
        for (char c : A) {
            union.add(c);
        }
        for (char c : B) {
            union.add(c);
        }
        return union;

    }



  
    // You can of course write other methods as well.
  }