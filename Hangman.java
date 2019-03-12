
//龍ONE

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Hangman {

    private static String word = ""; // holds the word the user is trying to guess
    private static String topic = ""; // holds the topic chosen by the user
    private static int guessedWrong = 0; // amount of letters guessed wrong by the user
    private static char[] individualLetters; // holds each individual character of "word"
    private static ArrayList<String> allWords = new ArrayList<String>(); // holds all the words in one given topic
    private static ArrayList<Character> lettersGuessed = new ArrayList<Character>(); // holds the letters guessed by
                                                                                     // user
    private static ArrayList<Character> lettersGuessedWrong = new ArrayList<Character>(); // holds the letters guessed
                                                                                          // wrong by user
    private static File hangmanFile; // holds the text file with the list of words
    private static final int MAX_WRONG = 6;

    public static void main(String[] args) throws FileNotFoundException {
        boolean playGame = true; // runs the game until the user wants to stop
        boolean guessingPhase = true; // runs the guessing phase until the user wins or loses

        // run loop until the user wants to stop playing
        while (playGame == true) {
            // Initial Instructions
            System.out.println("Select a topic for the hangman game and enter the topic in the console");
            System.out.println("Colors, Countries, Elements, League Champions, Marvel Characters, Religions, Sports");
            System.out.println("Type anything other than the topics to stop the game");
            // Retrieve topic from user
            Scanner topicChoice = new Scanner(System.in);
            topic = topicChoice.next();
            // Determine the word given the topic
            determineWord();

            // run loop until the user either wins or loses
            while (guessingPhase == true) {
                printHangman(); // print hangman figure
                printWord(); // print the word using blanks or letters
                // print the letters guessed by users
                System.out.print("\n\nLetters Guessed: ");
                for (int i = 0; i < lettersGuessed.size(); i++) {
                    System.out.print(lettersGuessed.get(i) + " ");
                }
                // if the user exceeds 5 wrong guesses, the guessing phase is over and they lose
                if (guessedWrong == MAX_WRONG) {
                    System.out.println();
                    guessingPhase = false;
                    break;
                }
                // Retrieve letter from user
                System.out.println("\nPick a letter:");
                Scanner guess = new Scanner(System.in);
                char guessedLetter = guess.next().charAt(0);
                if (lettersGuessed.contains(guessedLetter) == false) {
                    lettersGuessed.add(guessedLetter);
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
            if (guessedWrong == MAX_WRONG) {
                System.out.println("Game Over. Play Again!");
                System.out.println("Correct Answer: " + word);
            } else {
                System.out.println("Nice Job!");
            }
            // reset all variables for a new round
            guessingPhase = true;
            guessedWrong = 0;
            allWords.clear();
            lettersGuessed.clear();
            lettersGuessedWrong.clear();
            individualLetters = null;
        }
    }

    // print hangman figure based on wrong guesses
    public static void printHangman() {

        switch (guessedWrong) {
        case 0:
            System.out.println(" ________");
            System.out.println("|       |");
            System.out.println("|       ");
            System.out.println("|    ");
            System.out.println("|    ");
            System.out.println("|    ");
            System.out.println("|_______________");
            break;
        case 1:
            System.out.println(" ________");
            System.out.println("|       |");
            System.out.println("|       O");
            System.out.println("|    ");
            System.out.println("|    ");
            System.out.println("|    ");
            System.out.println("|_______________");
            break;
        case 2:
            System.out.println(" ________");
            System.out.println("|       |");
            System.out.println("|       O");
            System.out.println("|       |");
            System.out.println("|    ");
            System.out.println("|    ");
            System.out.println("|_______________");
            break;
        case 3:
            System.out.println(" ________");
            System.out.println("|       |");
            System.out.println("|       O");
            System.out.println("|    ---|");
            System.out.println("|    ");
            System.out.println("|    ");
            System.out.println("|_______________");
            break;
        case 4:
            System.out.println(" ________");
            System.out.println("|       |");
            System.out.println("|       O");
            System.out.println("|    ---|---");
            System.out.println("|    ");
            System.out.println("|    ");
            System.out.println("|_______________");
            break;
        case 5:
            System.out.println(" ________");
            System.out.println("|       |");
            System.out.println("|       O");
            System.out.println("|    ---|---");
            System.out.println("|      /");
            System.out.println("|     /");
            System.out.println("|_______________");
            break;
        case 6:
            System.out.println(" ________");
            System.out.println("|       |");
            System.out.println("|       O");
            System.out.println("|    ---|---");
            System.out.println("|      / \\");
            System.out.println("|     /   \\");
            System.out.println("|_______________");
            break;
        }

    }

    // print the word using blanks and letters
    public static void printWord() {
        System.out.println();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ') {
                System.out.print("  "); // print a space if the word itself has a spacex
            } else {
                if (lettersGuessed.contains(individualLetters[i])) {
                    System.out.print(individualLetters[i]); // print the letter if it has been guessed
                } else {
                    System.out.print("__"); // print a blank if the letter has not been guessed
                }
            }
            System.out.print("  ");
        }
    }

    // determine the word using the given topic
    public static void determineWord() throws FileNotFoundException {
        topic = topic.toLowerCase();
        switch (topic) {
        case "color":
        case "colors":
            hangmanFile = new File("./Java_Hangman_Colors.txt");
            break;
        case "countries":
            hangmanFile = new File("./Java_Hangman_Countries.txt");
            break;
        case "element":
        case "elements":
            hangmanFile = new File("./Java_Hangman_Elements.txt");
            break;
        case "league":
        case "league champions":
            hangmanFile = new File("./Java_Hangman_League.txt");
            break;
        case "marvel":
        case "marvel characters":
            hangmanFile = new File("./Java_Hangman_Marvel.txt");
            break;
        case "religion":
        case "religions":
            hangmanFile = new File("./Java_Hangman_Religions.txt");
            break;
        case "sport":
        case "sports":
            hangmanFile = new File("./Java_Hangman_Sports.txt");
            break;
        default:
            System.exit(0); // exit the game if the user types anything other than the topics given
        }
        Scanner scan_words = new Scanner(hangmanFile);
        int numberOfWords = scan_words.nextInt(); // determine the number of words in the text file
        for (int i = 0; i <= numberOfWords; i++) {
            allWords.add(scan_words.nextLine()); // add all the words in the text file to the arraylist
        }
        for (int j = 0; j < allWords.size(); j++) {
            if (allWords.get(j).equals("")) {
                allWords.remove(j); // remove words that are just blank spaces
            }
        }
        // chose a random word
        int random = (int) (Math.random() * allWords.size());
        word = allWords.get(random);
        word = word.toLowerCase();
        // create a array holding each letter of the word
        individualLetters = new char[word.length()];
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ') {

            } else {
                individualLetters[i] = word.charAt(i);
            }
        }
    }

    // check if the user has won the game
    public static boolean checkCompletion() {
        for (int i = 0; i < individualLetters.length; i++) {
            // check if the value of the array null (occurs when there is a space in the
            // word)
            if (individualLetters[i] == '\u0000') {
                continue;
            }
            if (lettersGuessed.contains(individualLetters[i])) {

            } else {
                return false;
            }
        }
        return true;
    }

    // check if the letter guessed is wrong
    public static void wrongGuesses() {
        for (int i = 0; i < lettersGuessed.size(); i++) {
            boolean checkLetter = false;
            for (int j = 0; j < individualLetters.length; j++) {
                if (lettersGuessed.get(i) == individualLetters[j]) {
                    checkLetter = true;
                    break;
                }
            }
            if (checkLetter == false) {
                if (lettersGuessedWrong.contains(lettersGuessed.get(i)) == false) {
                    lettersGuessedWrong.add(lettersGuessed.get(i));
                    guessedWrong++;
                }
            }
        }
    }
}