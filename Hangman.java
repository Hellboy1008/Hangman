
//龍ONE

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Hangman {

    private static String word; // holds the word the user is trying to guess
    private static int guessedWrongNum = 0; // amount of letters guessed wrong by the user
    private static char[] individualLetters; // holds each individual character of "word"
    private static ArrayList<String> allWords = new ArrayList<String>(); // holds all the words in one given topic
    private static ArrayList<Character> guessed = new ArrayList<Character>(); // holds the letters guessed by user
    private static ArrayList<Character> guessedWrong = new ArrayList<Character>(); // holds the letters guessed wrong by
                                                                                   // user
    private static File hangmanFile; // holds the text file with the list of words
    private static final int MAX_WRONG = 6;
    private static final String INSTRUCTIONS = "Select a topic for the hangman game and enter the topic (exactly as shown below) in the console:"
            + "\nColors, Countries, Elements, League Champions, Marvel Characters, Religions, Sports"
            + "\nType \"exit\" to exit the game";
    private static final String COLOR_FILE = "./Java_Hangman_Colors.txt", COLOR_TOPIC = "colors";
    private static final String COUNTRY_FILE = "./Java_Hangman_Countries.txt", COUNTRY_TOPIC = "countries";
    private static final String ELEMENT_FILE = "./Java_Hangman_Elements.txt", ELEMENT_TOPIC = "elements";
    private static final String LEAGUE_FILE = "./Java_Hangman_League.txt", LEAGUE_TOPIC = "league champions";
    private static final String MARVEL_FILE = "./Java_Hangman_Marvel.txt", MARVEL_TOPIC = "marvel characters";
    private static final String RELIGION_FILE = "./Java_Hangman_Religions.txt", RELIGION_TOPIC = "religions";
    private static final String SPORT_FILE = "./Java_Hangman_Sports.txt", SPORT_TOPIC = "sports";
    private static final String EXIT = "exit";
    private static final String TOPIC_DNE = "This topic does not exist";
    private static final Character SPACE_CHAR = ' ';
    private static final String TOP_POLE = " ________", BOTTOM_POLE = "|___________";
    private static final String HANGMAN_POLE = "|       |", HANGMAN_LEFT_POLE = "|";
    private static final String HANGMAN_HEAD = "|       O";
    private static final String HANGMAN_HAND_LEFT = "|    ---|", HANGMAN_HAND_BOTH = "|    ---|---";
    private static final String HANGMAN_LEFT_LEG_ONE = "|      /", HANGMAN_LEFT_LEG_TWO = "|     /";
    private static final String HANGMAN_BOTH_LEG_ONE = "|      / \\", HANGMAN_BOTH_LEG_TWO = "|     /   \\";

    public static void main(String[] args) throws FileNotFoundException {
        boolean playGame = true; // runs the game until the user wants to stop
        boolean guessingPhase = true; // runs the guessing phase until the user wins or loses
        Scanner scan = new Scanner(System.in);
        // run loop until the user wants to stop playing
        while (playGame == true) {
            // Initial Instructions
            System.out.println(INSTRUCTIONS);
            // Retrieve topic from user
            String topic = scan.next();
            // Determine the word given the topic
            determineWord(topic);
            // run loop until the user either wins or loses
            while (guessingPhase == true) {
                printHangman(); // print hangman figure
                printWord(); // print the word using blanks or letters
                // print the letters guessed by users
                System.out.print("\n\nLetters Guessed: ");
                for (int i = 0; i < guessed.size(); i++) {
                    System.out.print(guessed.get(i) + " ");
                }
                // if the user exceeds 5 wrong guesses, the guessing phase is over and they lose
                if (guessedWrongNum == MAX_WRONG) {
                    System.out.println();
                    guessingPhase = false;
                    break;
                }
                // Retrieve letter from user
                System.out.println("\nPick a letter:");
                Scanner guess = new Scanner(System.in);
                char guessedLetter = guess.next().charAt(0);
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
            if (guessedWrongNum == MAX_WRONG) {
                System.out.println("Game Over. Play Again!");
                System.out.println("Correct Answer: " + word);
            } else {
                System.out.println("Nice Job!");
            }
            // reset all variables for a new round
            guessingPhase = true;
            guessedWrongNum = 0;
            allWords.clear();
            guessedWrong.clear();
            individualLetters = null;
        }
        scan.close();
    }

    // print hangman figure based on wrong guesses
    public static void printHangman() {

        switch (guessedWrongNum) {
        case 0:
            System.out.println(TOP_POLE);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(BOTTOM_POLE);
            break;
        case 1:
            System.out.println(TOP_POLE);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_HEAD);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(BOTTOM_POLE);
            break;
        case MAX_WRONG - 1 - 1 - 1 - 1:
            System.out.println(TOP_POLE);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_HEAD);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(BOTTOM_POLE);
            break;
        case MAX_WRONG - 1 - 1 - 1:
            System.out.println(TOP_POLE);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_HEAD);
            System.out.println(HANGMAN_HAND_LEFT);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(BOTTOM_POLE);
            break;
        case MAX_WRONG - 1 - 1:
            System.out.println(TOP_POLE);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_HEAD);
            System.out.println(HANGMAN_HAND_BOTH);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(HANGMAN_LEFT_POLE);
            System.out.println(BOTTOM_POLE);
            break;
        case MAX_WRONG - 1:
            System.out.println(TOP_POLE);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_HEAD);
            System.out.println(HANGMAN_HAND_BOTH);
            System.out.println(HANGMAN_LEFT_LEG_ONE);
            System.out.println(HANGMAN_LEFT_LEG_TWO);
            System.out.println(BOTTOM_POLE);
            break;
        case MAX_WRONG:
            System.out.println(TOP_POLE);
            System.out.println(HANGMAN_POLE);
            System.out.println(HANGMAN_HEAD);
            System.out.println(HANGMAN_HAND_BOTH);
            System.out.println(HANGMAN_BOTH_LEG_ONE);
            System.out.println(HANGMAN_BOTH_LEG_TWO);
            System.out.println(BOTTOM_POLE);
            break;
        }

    }

    // print the word using blanks and letters
    public static void printWord() {
        System.out.println();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == SPACE_CHAR) {
                System.out.print("  "); // print a space if the word itself has a space
            } else {
                if (guessed.contains(individualLetters[i])) {
                    System.out.print(individualLetters[i]); // print the letter if it has been guessed
                } else {
                    System.out.print("__"); // print a blank if the letter has not been guessed
                }
            }
            System.out.print("  ");
        }
    }

    // determine the word using the given topic
    public static void determineWord(String topic) throws FileNotFoundException {
        switch (topic.toLowerCase()) {
        case COLOR_TOPIC:
            hangmanFile = new File(COLOR_FILE);
            break;
        case COUNTRY_TOPIC:
            hangmanFile = new File(COUNTRY_FILE);
            break;
        case ELEMENT_TOPIC:
            hangmanFile = new File(ELEMENT_FILE);
            break;
        case LEAGUE_TOPIC:
            hangmanFile = new File(LEAGUE_FILE);
            break;
        case MARVEL_TOPIC:
            hangmanFile = new File(MARVEL_FILE);
            break;
        case RELIGION_TOPIC:
            hangmanFile = new File(RELIGION_FILE);
            break;
        case SPORT_TOPIC:
            hangmanFile = new File(SPORT_FILE);
            break;
        case EXIT:
            System.exit(0);
        default:
            System.out.println(TOPIC_DNE);
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
        // create an array holding each letter of the word
        individualLetters = new char[word.length()];
        for (int index = 0; index < word.length(); index++) {
            if (word.charAt(index) == SPACE_CHAR) {
                individualLetters[index] = SPACE_CHAR;
            } else {
                individualLetters[index] = word.charAt(index);
            }
        }
    }

    // check if the user has won the game
    public static boolean checkCompletion() {
        for (int i = 0; i < individualLetters.length; i++) {
            // check if the value of the array null (occurs when there is a space in the
            // word)
            if (individualLetters[i] == SPACE_CHAR) {
                continue;
            }
            if (guessed.contains(individualLetters[i])) {

            } else {
                return false;
            }
        }
        return true;
    }

    // check if the letter guessed is wrong
    public static void wrongGuesses() {
        for (int i = 0; i < guessed.size(); i++) {
            boolean checkLetter = false;
            for (int j = 0; j < individualLetters.length; j++) {
                if (guessed.get(i) == individualLetters[j]) {
                    checkLetter = true;
                    break;
                }
            }
            if (checkLetter == false) {
                if (guessedWrong.contains(guessed.get(i)) == false) {
                    guessedWrong.add(guessed.get(i));
                    guessedWrongNum++;
                }
            }
        }
    }
}