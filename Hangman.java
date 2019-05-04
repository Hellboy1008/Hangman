
/**
 * Created by: 龍ONE
 * Date Created: October 9, 2018
 * Date Edited: May 3, 2019
 * Purpose: Run a Hangman game on the console using text files with
 *          predetermined words.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * The class runs the main method that implements other methods to make a
 * successful Hangman game.
 */
public class Hangman {

    // ascii code for character 'a', the first alphabet
    private static final int A_ASCII = 97;
    // maximum number of wrong guesses before game over
    private static final int MAX_WRONG = 6;
    // ascii code for character 'z', the last alphabet
    private static final int Z_ASCII = 122;

    // character value for blank alphabets
    private static final char BLANK_CHAR = '_';
    // character value for newline
    private static final char NEWLINE_CHAR = '\n';
    // character value for space
    private static final char SPACE_CHAR = ' ';

    // value for whether the guessing phase is still in progress
    private static boolean guessingPhase = true;
    // keeps track of whether the game is over or not
    private static boolean playGame = true;

    // file path for possible words
    private static String filePath = "./Java_Hangman_%s.txt";
    // word the user is trying to guess
    private static String word;
    // message for letters guessed wrong by user
    private static final String GUESSED_WRONG = "\nLetters Guessed Wrong: ";
    // message holding instruction
    private static final String INSTRUCTION = "Select a topic for the hangman game "
            + "and enter the topic (exactly as shown below) in the console:\n"
            + "Colors, Countries, Elements, League, Marvel, Religions, Sports\n" + "Type \"exit\" to exit the game";
    // message for letters not guessed by user
    private static final String NOT_GUESSED = "\n\nLetters Not Guessed: ";
    // message for users when they chose their letters
    private static final String PICK_LETTERS = "\nPick a letter:";
    // message for invalid topic
    private static final String TOPIC_DNE = "This topic does not exist. Please enter a valid topic.";

    // holds all the topics and edge cases
    private static final String[] TOPICS = { "Colors", "Countries", "Elements", "League", "Marvel", "Religions",
            "Sports", "exit" };

    // holds all the alphabets guessed by user
    private static ArrayList<Character> guessed = new ArrayList<Character>();
    // holds all the alphabets guessed wrong by user
    private static ArrayList<Character> guessedWrong = new ArrayList<Character>();
    // holds all the alphabets not guessed by the user
    private static ArrayList<Character> notGuessed = new ArrayList<Character>();

    // scanner used for reading user input and files
    private static Scanner scan;

    private static String hangmanBase = " ________\n" + "|       |\n" + "|       %s\n" + "|    %s\n" + "|      %s\n"
            + "|     %s\n" + "|___________\n";
    private static final String[] HANGMAN_HEAD = { "", "O", "O", "O", "O", "O", "O" };
    private static final String[] HANGMAN_BODY = { "", "", "   |", "---|", "---|---", "---|---", "---|---" };
    private static final String[] HANGMAN_TOP_LEG = { "", "", "", "", "", "/", "/ \\" };
    private static final String[] HANGMAN_BOTTOM_LEG = { "", "", "", "", "", "/", "/   \\" };

    private static final String LOSE_MESSAGE = "\nGame Over. Play Again!", CORRECT_ANSWER = "Correct Answer: ";
    private static final String WIN_MESSAGE = "Nice Job!";

    /**
     * The main method that runs user input for the hangman game.
     * 
     * @param args The argument given to the main method
     * @throws FileNotFoundException
     * @return None
     */
    public static void main(String[] args) throws FileNotFoundException {
        // holds the topic inputted by user
        String topic = "";

        // runs the game until the user exits the game
        while (playGame == true) {
            // get user input
            scan = new Scanner(System.in);
            do {
                System.out.println(INSTRUCTION);
                topic = scan.next();
            } while (validTopic(topic) == false);
            determineWord(topic);
            // run guessing phase
            guessingPhase();
            // reset variables for a new round
            topic = "";
            guessingPhase = true;
            guessedWrong.clear();
            guessed.clear();
            notGuessed.clear();
        }
        scan.close();
    }

    private static void guessingPhase() {
        // exit if guessing phase is not in progress
        if (guessingPhase == false) {
            return;
        }
        // initialize the characters not guessed by user
        for (int ascii = A_ASCII; ascii <= Z_ASCII; ascii++) {
            notGuessed.add((char) ascii);
        }

        // run loop until the user wins or loses
        while (guessingPhase == true) {
            // print hangman display
            printHangman();
            // print the letters guessed and not guessed by user
            printCharArrayList(NOT_GUESSED, notGuessed);
            printCharArrayList(GUESSED_WRONG, guessedWrong);
            // if the user exceeds 6 wrong guesses, the guessing phase is over and they lose
            if (guessedWrong.size() == MAX_WRONG) {
                guessingPhase = false;
                continue;
            }
            // Retrieve letter from user and check for wrong guesses
            getCharacter();
            wrongGuesses();
            // check if the user has won
            if (checkCompletion() == true) {
                printHangman();
                guessingPhase = false;
            }
        }

        // print win or lose message
        if (guessedWrong.size() == MAX_WRONG) {
            System.out.println(LOSE_MESSAGE);
            System.out.println(CORRECT_ANSWER + word + NEWLINE_CHAR);
        } else {
            System.out.println(WIN_MESSAGE + NEWLINE_CHAR);
        }
    }

    private static void printCharArrayList(String message, ArrayList<Character> list) {
        System.out.print(message);
        for (int index = 0; index < list.size(); index++) {
            System.out.print("" + list.get(index) + SPACE_CHAR);
        }
    }

    private static void getCharacter() {
        // the character guessed by the user
        char guessedLetter;
        // scanner for user input
        scan = new Scanner(System.in);

        // get user input
        System.out.println(PICK_LETTERS);
        guessedLetter = scan.next().charAt(0);
        // remove character from the notGuessed list and add it to the guessed list
        notGuessed.remove((Character) guessedLetter);
        if (guessed.contains(guessedLetter) == false) {
            guessed.add(guessedLetter);
        }
    }

    // print hangman figure based on wrong guesses
    public static void printHangman() {
        // the number of wrong guesses
        int wrongNum = guessedWrong.size();
        // the update hangman base
        String hangman;

        // format and print the hangman base
        hangman = String.format(hangmanBase, HANGMAN_HEAD[wrongNum], HANGMAN_BODY[wrongNum], HANGMAN_TOP_LEG[wrongNum],
                HANGMAN_BOTTOM_LEG[wrongNum]);
        System.out.println(hangman);
        // print word under the hangman
        printWord();
    }

    // print the word using blanks and letters
    public static void printWord() {
        // print the letter if guessed, blank if not
        for (int index = 0; index < word.length(); index++) {
            // check if the word contains a space
            if (word.charAt(index) == SPACE_CHAR) {
                System.out.print("" + SPACE_CHAR + SPACE_CHAR);
            } else if (guessed.contains(word.charAt(index))) {
                // print the letter if it was guessed
                System.out.print(word.charAt(index));
            } else {
                // print a blank if letter has not been guessed
                System.out.print("" + BLANK_CHAR + BLANK_CHAR);
            }
            // print a space in between each letter
            System.out.print("" + SPACE_CHAR + SPACE_CHAR);
        }
    }

    // check if the user has won the game
    public static boolean checkCompletion() {
        for (int index = 0; index < word.length(); index++) {
            // skip if the value of the array is a space
            if (word.charAt(index) == SPACE_CHAR) {
                continue;
            }
            // return false if character has not been guessed yet
            if (!guessed.contains(word.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    // check if the letter guessed is wrong
    public static void wrongGuesses() {
        // whether the letter is a letter in the word being guessed
        boolean checkLetter;
        // character guessed by user
        char guessedChar;

        // check to see if the characters guessed by the user is wrong
        for (int indexOne = 0; indexOne < guessed.size(); indexOne++) {
            checkLetter = false;
            guessedChar = guessed.get(indexOne);
            // check if the character exists in word
            for (int indexTwo = 0; indexTwo < word.length(); indexTwo++) {
                if (guessedChar == word.charAt(indexTwo)) {
                    checkLetter = true;
                    break;
                }
            }
            // check if the character is a wrong letter
            if (checkLetter == false && isLetter(guessedChar)) {
                // check if the arrayList already contains the letter
                if (guessedWrong.contains(guessedChar) == false) {
                    guessedWrong.add(guessedChar);
                }
            }
        }
    }

    private static boolean isLetter(char character) {
        // check if character is a letter
        if ((int) character < A_ASCII || (int) character > Z_ASCII) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether or not the topic inputted by user is valid.
     * 
     * @param topic The topic inputted by the user
     * @return True if the topic is valid, false if not
     */
    private static boolean validTopic(String topic) {
        // check if the topic is valid
        for (int index = 0; index < TOPICS.length - 1; index++) {
            if (topic.equals(TOPICS[index])) {
                return true;
            }
        }

        // check if the topic is "exit"
        if (topic.equals(TOPICS[TOPICS.length - 1])) {
            guessingPhase = false;
            playGame = false;
            return true;
        }

        System.out.println(TOPIC_DNE + NEWLINE_CHAR);
        return false;
    }

    /**
     * Determine the word for the Hangman game based on the parameter topic
     * 
     * @param topic The topic chosen by the user
     * @throws FileNotFoundException
     * @return None
     */
    public static void determineWord(String topic) throws FileNotFoundException {
        // all the words in one given topic
        ArrayList<String> allWords = new ArrayList<String>();
        // text file with the list of possible words
        File hangmanFile;

        // exit method if the game is not being played
        if (playGame == false) {
            return;
        }
        // determine file path for the topic
        hangmanFile = new File(String.format(filePath, topic));
        // scan file for words
        scan = new Scanner(hangmanFile);
        while (scan.hasNextLine()) {
            allWords.add(scan.nextLine());
        }
        // chose a random word
        word = allWords.get((int) (Math.random() * allWords.size()));
        word = word.toLowerCase();
    }
}