
/**
 * Created by: 龍ONE
 * Date Created: October 9, 2018
 * Date Edited: April 28, 2019
 * Purpose: Run a Hangman game on the console using text files with
 *          predetermined words
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

    // maximum number of wrong guesses before game over
    private static final int MAX_WRONG = 6;
    // ascii code for character 'a', the first alphabet
    private static final int A_ASCII = 97;
    // ascii code for character 'z', the last alphabet
    private static final int Z_ASCII = 122;

    // character value for space
    private static final char SPACE_CHAR = ' ';
    // character value for newline
    private static final char NEWLINE_CHAR = '\n';
    // character value for blank alphabets
    private static final char BLANK_CHAR = '_';

    // keeps track of whether the game is over or not
    private static boolean noGame = false;

    // holds the word the user is trying to guess
    private static String word;
    // message holding instructions
    private static final String INSTRUCTIONS = "Select a topic for the hangman game "
            + "and enter the topic (exactly as shown below) in the console:" + NEWLINE_CHAR
            + "Colors, Countries, Elements, League, Marvel, Religions, Sports" + NEWLINE_CHAR
            + "Type \"exit\" to exit the game";
    //

    // holds string representations of topics
    private static final String COLOR_TOPIC = "Colors";
    private static final String COUNTRY_TOPIC = "Countries";
    private static final String ELEMENT_TOPIC = "Elements";
    private static final String LEAGUE_TOPIC = "League";
    private static final String MARVEL_TOPIC = "Marvel";
    private static final String RELIGION_TOPIC = "Religions";
    private static final String SPORT_TOPIC = "Sports";

    // holds all the alphabets guessed by user
    private static ArrayList<Character> guessed = new ArrayList<Character>();
    // holds all the alphabets not guessed by the user
    private static ArrayList<Character> notGuessed = new ArrayList<Character>();
    // holds all the alphabets guessed wrong by user
    private static ArrayList<Character> guessedWrong = new ArrayList<Character>();

    private static final String EXIT = "exit", TOPIC_DNE = "This topic does not exist";
    private static final String TOP_POLE = " ________", BOTTOM_POLE = "|___________";
    private static final String HANGMAN_POLE = "|       |", HANGMAN_LEFT_POLE = "|";
    private static final String HANGMAN_HEAD = "|       O", HANGMAN_BODY = "|       |";
    private static final String HANGMAN_HAND_LEFT = "|    ---|", HANGMAN_HAND_BOTH = "|    ---|---";
    private static final String HANGMAN_LEFT_LEG_ONE = "|      /", HANGMAN_LEFT_LEG_TWO = "|     /";
    private static final String HANGMAN_BOTH_LEG_ONE = "|      / \\", HANGMAN_BOTH_LEG_TWO = "|     /   \\";
    private static final String NOT_GUESSED = "\n\nLetters Not Guessed: ",
            LETTERS_GUESSED = "\nLetters Guessed Wrong: ";
    private static final String PICK_LETTERS = "\nPick a letter:";
    private static final String LOSE_MESSAGE = "Game Over. Play Again!", CORRECT_ANSWER = "Correct Answer: ";
    private static final String WIN_MESSAGE = "Nice Job!";

    public static void main(String[] args) throws FileNotFoundException {
        // true if user still has guesses, false if user wins or loses
        boolean guessingPhase = true;
        // scanner for reading user input
        Scanner scan;
        // run loop until the program is halted
        while (noGame == false) {
            // initialize notGuessed
            for (int ascii = A_ASCII; ascii <= Z_ASCII; ascii++) {
                notGuessed.add((char) ascii);
            }
            // Initial Instructions
            System.out.println(INSTRUCTIONS);
            // Retrieve topic from user
            scan = new Scanner(System.in);
            String topic = scan.next();
            // Determine the word given the topic
            determineWord(topic);
            // run loop until the user either wins or loses
            while (guessingPhase == true) {
                printHangman(); // print hangman figure
                printWord(); // print the word using blanks or letters
                // print the letters not guessed yet
                System.out.print(NOT_GUESSED);
                for (int index = 0; index < notGuessed.size(); index++) {
                    System.out.print("" + notGuessed.get(index) + SPACE_CHAR);
                }
                // print the letters guessed by users
                System.out.print(LETTERS_GUESSED);
                for (int index = 0; index < guessedWrong.size(); index++) {
                    System.out.print("" + guessedWrong.get(index) + SPACE_CHAR);
                }
                // if the user exceeds 6 wrong guesses, the guessing phase is over and they lose
                if (guessedWrong.size() == MAX_WRONG) {
                    System.out.println();
                    guessingPhase = false;
                    break;
                }
                // Retrieve letter from user
                System.out.println(PICK_LETTERS);
                char guessedLetter = scan.next().charAt(0);
                notGuessed.remove((Character) guessedLetter);
                if (guessed.contains(guessedLetter) == false) {
                    guessed.add(guessedLetter);
                }
                wrongGuesses(); // check for wrong guesses
                // check if the user has won
                if (checkCompletion() == true) {
                    printHangman();
                    printWord();
                    System.out.println();
                    guessingPhase = false;
                }
            }
            // print win or lose message after the game
            if (guessedWrong.size() == MAX_WRONG) {
                System.out.println(LOSE_MESSAGE);
                System.out.println(CORRECT_ANSWER + word);
            } else {
                System.out.println(WIN_MESSAGE);
            }
            // reset all variables for a new round
            guessingPhase = true;
            guessedWrong.clear();
            guessed.clear();
            notGuessed.clear();
        }
    }

    // print hangman figure based on wrong guesses
    public static void printHangman() {
        String bodyOne = HANGMAN_LEFT_POLE + NEWLINE_CHAR + HANGMAN_LEFT_POLE + NEWLINE_CHAR + HANGMAN_LEFT_POLE
                + NEWLINE_CHAR + HANGMAN_LEFT_POLE;
        String bodyTwo = HANGMAN_HEAD + NEWLINE_CHAR + HANGMAN_LEFT_POLE + NEWLINE_CHAR + HANGMAN_LEFT_POLE
                + NEWLINE_CHAR + HANGMAN_LEFT_POLE;
        String bodyThree = HANGMAN_HEAD + NEWLINE_CHAR + HANGMAN_BODY + NEWLINE_CHAR + HANGMAN_LEFT_POLE + NEWLINE_CHAR
                + HANGMAN_LEFT_POLE;
        String bodyFour = HANGMAN_HEAD + NEWLINE_CHAR + HANGMAN_HAND_LEFT + NEWLINE_CHAR + HANGMAN_LEFT_POLE
                + NEWLINE_CHAR + HANGMAN_LEFT_POLE;
        String bodyFive = HANGMAN_HEAD + NEWLINE_CHAR + HANGMAN_HAND_BOTH + NEWLINE_CHAR + HANGMAN_LEFT_POLE
                + NEWLINE_CHAR + HANGMAN_LEFT_POLE;
        String bodySix = HANGMAN_HEAD + NEWLINE_CHAR + HANGMAN_HAND_BOTH + NEWLINE_CHAR + HANGMAN_LEFT_LEG_ONE
                + NEWLINE_CHAR + HANGMAN_LEFT_LEG_TWO;
        String bodySeven = HANGMAN_HEAD + NEWLINE_CHAR + HANGMAN_HAND_BOTH + NEWLINE_CHAR + HANGMAN_BOTH_LEG_ONE
                + NEWLINE_CHAR + HANGMAN_BOTH_LEG_TWO;
        String[] bodies = { bodyOne, bodyTwo, bodyThree, bodyFour, bodyFive, bodySix, bodySeven };

        for (int count = 0; count <= MAX_WRONG; count++) {
            if (guessedWrong.size() == count) {
                System.out.println(TOP_POLE);
                System.out.println(HANGMAN_POLE);
                System.out.println(bodies[count]);
                System.out.println(BOTTOM_POLE);
            }
        }
    }

    // print the word using blanks and letters
    public static void printWord() {
        System.out.println();
        for (int index = 0; index < word.length(); index++) {
            if (word.charAt(index) == SPACE_CHAR) {
                System.out.print("" + SPACE_CHAR + SPACE_CHAR); // print a space if the word itself has a space
            } else {
                if (guessed.contains(word.charAt(index))) {
                    System.out.print(word.charAt(index)); // print the letter if it has been guessed
                } else {
                    System.out.print("" + BLANK_CHAR + BLANK_CHAR); // print a blank if the letter has not been guessed
                }
            }
            System.out.print("" + SPACE_CHAR + SPACE_CHAR);
        }
    }

    // determine the word using the given topic
    public static void determineWord(String topic) throws FileNotFoundException {
        ArrayList<String> allWords = new ArrayList<String>(); // holds all the words in one given topic
        File hangmanFile;// holds the text file with the list of possible words
        String filePath = "./Java_Hangman_%s.txt"; // file path for topics
        switch (topic) {
        case COLOR_TOPIC:
        case COUNTRY_TOPIC:
        case ELEMENT_TOPIC:
        case LEAGUE_TOPIC:
        case MARVEL_TOPIC:
        case RELIGION_TOPIC:
        case SPORT_TOPIC:
            filePath = String.format(filePath, topic);
            System.out.println(filePath);
            hangmanFile = new File(filePath);
            break;
        case EXIT:
            noGame = true;
            return;
        default:
            System.out.println(TOPIC_DNE);
            hangmanFile = null;
            System.exit(0); // exit the game if the user types anything other than the topics given
        }
        // scan file for words
        Scanner scan = new Scanner(hangmanFile);
        while (scan.hasNext()) {
            allWords.add(scan.nextLine());
        }
        scan.close();
        // chose a random word
        int random = (int) (Math.random() * allWords.size());
        word = allWords.get(random);
        word = word.toLowerCase();
    }

    // check if the user has won the game
    public static boolean checkCompletion() {
        for (int index = 0; index < word.length(); index++) {
            // skip if the value of the array is a space
            if (word.charAt(index) == SPACE_CHAR) {
                continue;
            }
            if (guessed.contains(word.charAt(index))) {

            } else {
                return false;
            }
        }
        return true;
    }

    // check if the letter guessed is wrong
    public static void wrongGuesses() {
        for (int index = 0; index < guessed.size(); index++) {
            boolean checkLetter = false;
            for (int indexTwo = 0; indexTwo < word.length(); indexTwo++) {
                if (guessed.get(index) == word.charAt(indexTwo)) {
                    checkLetter = true;
                    break;
                }
            }
            if (checkLetter == false) {
                if (guessedWrong.contains(guessed.get(index)) == false) {
                    guessedWrong.add(guessed.get(index));
                }
            }
        }
    }
}