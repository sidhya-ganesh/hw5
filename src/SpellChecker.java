import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

public class SpellChecker {
    // Use this field everytime you need to read user input
    // Scanner to read user input throughout the program:
    private Scanner inputReader; // DO NOT MODIFY
    // WordRecommender object to suggest corrections for misspelled words
    private WordRecommender recommender;

/**
    public SpellChecker() throws IOException {
        //Instantiate WordRecommender with dictionary file
        //Object will allow to check words and get suggestions for the misspellings
        recommender = new WordRecommender("dictionary.txt");
        inputReader = new Scanner(System.in); // DO NOT MODIFY - must be included in this method
        //try catch here, and reprompting wherever im opening files **************
    }
 **/
    public SpellChecker() {
        inputReader = new Scanner(System.in); // DO NOT MODIFY - must be included in this method

        boolean validFile = false;
        while (!validFile) {
            try {
                // Prompt user for dictionary file
                System.out.printf(Util.DICTIONARY_PROMPT);

                String dictionaryFile = inputReader.nextLine().trim();

                // Try to build WordRecommender w this file
                recommender = new WordRecommender(dictionaryFile);

                // If no exception, confirm success and break
                System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, dictionaryFile);
                validFile = true;

            } catch (IOException e) {
                // If the file can’t be opened, show error and re prompt
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
}

/**
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
 **/

/**
    public void start() {
        boolean validFile = false;
        String inputFile = "";
        Scanner fileReader = null;

        // repeatedly ask for a valid file to spell-check
        while (!validFile) {
            try {
                System.out.printf(Util.FILENAME_PROMPT);
                inputFile = inputReader.nextLine().trim();

                fileReader = new Scanner(new FileInputStream(inputFile));
                String outputFile = inputFile.substring(0, inputFile.lastIndexOf('.')) + "_chk.txt";

                System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, inputFile, outputFile);
                validFile = true;   // success — exit loop

            } catch (IOException e) {
                System.out.printf(Util.FILE_OPENING_ERROR);   // show the standard error message
            }
        }

        // now that the file is open, proceed with spell-checking logic…
        // (read words from fileReader, check each against recommender.getDictionary(), etc.)

        inputReader.close(); // DO NOT MODIFY
    }
 **/

    public void start() {
        boolean validFile = false;
        String inputFile = "";
        Scanner fileReader = null;

        // --- STEP 1: Prompt until a valid spell-check file is given ---
        while (!validFile) {
            try {
                System.out.printf(Util.FILENAME_PROMPT);
                inputFile = inputReader.nextLine().trim();

                fileReader = new Scanner(new FileInputStream(inputFile));
                validFile = true;

                String outputFile = inputFile.substring(0, inputFile.lastIndexOf('.')) + "_chk.txt";
                System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, inputFile, outputFile);

                // --- STEP 2: Prepare to write the corrected output ---
                PrintWriter writer = new PrintWriter(outputFile);

                // --- STEP 3: Process each word in the input file ---
                while (fileReader.hasNext()) {
                    String word = fileReader.next();

                    // if word is correctly spelled, just write it back
                    if (recommender.getDictionary().contains(word)) {
                        writer.print(word + " ");
                    } else {
                        // notify user word is misspelled
                        System.out.printf(Util.MISSPELL_NOTIFICATION, word);

                        // get word suggestions (tolerance=2, 50% common, top 4)
                        ArrayList<String> suggestions = recommender.getWordSuggestions(word, 2, 0.5, 4);

                        String replacement = word; // default if user "accepts"

                        if (suggestions.isEmpty()) {
                            // no suggestions case
                            System.out.printf(Util.NO_SUGGESTIONS);
                            System.out.printf(Util.TWO_OPTION_PROMPT);

                            while (true) {
                                String choice = inputReader.nextLine().trim();
                                if (choice.equals("a")) {
                                    break;
                                } else if (choice.equals("t")) {
                                    System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                                    replacement = inputReader.nextLine().trim();
                                    break;
                                } else {
                                    System.out.printf(Util.INVALID_RESPONSE);
                                }
                            }
                        } else {
                            // display suggestions
                            System.out.printf(Util.FOLLOWING_SUGGESTIONS);
                            for (int i = 0; i < suggestions.size(); i++) {
                                System.out.printf(Util.SUGGESTION_ENTRY, i + 1, suggestions.get(i));
                            }

                            System.out.printf(Util.THREE_OPTION_PROMPT);

                            while (true) {
                                String choice = inputReader.nextLine().trim();
                                if (choice.equals("a")) {
                                    break; // accept original word
                                } else if (choice.equals("t")) {
                                    System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                                    replacement = inputReader.nextLine().trim();
                                    break;
                                } else if (choice.equals("r")) {
                                    System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
                                    try {
                                        int pick = Integer.parseInt(inputReader.nextLine().trim());
                                        if (pick >= 1 && pick <= suggestions.size()) {
                                            replacement = suggestions.get(pick - 1);
                                            break;
                                        } else {
                                            System.out.printf(Util.INVALID_RESPONSE);
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.printf(Util.INVALID_RESPONSE);
                                    }
                                } else {
                                    System.out.printf(Util.INVALID_RESPONSE);
                                }
                            }
                        }

                        // write chosen replacement (or accepted original)
                        writer.print(replacement + " ");
                    }
                }

                writer.close();
                fileReader.close();

            } catch (IOException e) {
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }

        // --- STEP 4: Close the main input scanner ---
        inputReader.close(); // DO NOT MODIFY
    }



    // You can of course write other methods as well.
}