import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {
    // Use this field everytime you need to read user input
    // Scanner to read user input throughout the program:
    private Scanner inputReader; // DO NOT MODIFY
    // WordRecommender object to suggest corrections for misspelled words
    private WordRecommender recommender;


    public SpellChecker() {
        //Instantiate WordRecommender with dictionary file
        //Object will allow to check words and get suggestions for the misspellings
        recommender = new WordRecommender("dictionary.txt");
        inputReader = new Scanner(System.in); // DO NOT MODIFY - must be included in this method
    }

    public void start() {
        //Prompt the user to enter a word or to quit the program
        System.out.print("Enter a word (or type 'quit' to exit): ");
        String word = inputReader.nextLine().trim();

        //Continue until the user types 'quit' (case-insensitive)
        while(!word.equalsIgnoreCase("quit")) {
            // Check if the entered word exists in the dictionary
            if(recommender.getDictionary().contains(word)) {
                System.out.println("Correct.");
            } else {
                System.out.println("Incorrect.");
                //Get suggested corrections using the WordRecommender
                ArrayList<String> suggestions = recommender.getWordSuggestions(word, 2, 0.5, 5);
                //If there are suggestions, display them to the user
                if (suggestions.size() > 0) {
                    System.out.println("Maybe you meant:");
                    for (String s : suggestions) {
                        System.out.println("  " + s); // Print each suggestion
                    }
                }
            }
            //Prompt the user again for the next word:
            System.out.print("Enter a word (or type 'quit' to exit): ");
            word = inputReader.nextLine().trim();
        }
        // TODO: Complete the body of this method, as necessary.
        //Close Scanner at end of the program
        inputReader.close();  // DO NOT MODIFY - must be the last line of this method!
    }

    // You can of course write other methods as well.
}