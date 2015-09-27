/** 600.226 Data Structures 
 * William Watson - wwatso13.
 * Joshan Bajaj - jbajaj1.
 * P1
 * Hangman.java
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hangman Class.
 * @author William Watson Joshan Bajaj
 */
public final class Hangman {
    /**
     * Fake Constructor.
     */
    private Hangman() {
    }

    /**
     * Main Method.
     * @param args
     *            Arguments
     * @throws IOException
     *             exception
     */
    public static void main(String[] args) throws IOException {

        checkArg(args.length);
        boolean debug = checkDebugOn(args.length, args);
        checkFile(args[0]);

        DictionaryList master = new DictionaryList(args[0]);
        Scanner kb = new Scanner(System.in);
        StringBuffer secret;
        final int gLimit = 6;
        int gameNum = 0;
        int wins = 0;
        int loss = 0;
        char choice = 'y';
        while (choice == 'y') {
            /// while game is going on
            gameNum++;
            int length = master.setLength();
            secret = new StringBuffer(setSecret(length));
            int guessLeft = gLimit;
            DictionaryList list = new DictionaryList(length, master);
            String lastGuess = "";
            String guessSoFar = "";
            // turn while
            while (guessLeft != 0) {
                printDebug(list, debug);
                System.out.println("Guesses so far: " + guessSoFar);
                String guess = getUserInput(guessLeft, secret, kb);
                int gLen = guess.length();
                if (gLen == 1 && Character.isLetter(guess.charAt(0))) {
                    guessSoFar += guess + " ";
                    list = new DictionaryList(guess.charAt(0), list);
                    String test = list.getWordAt(0);
                    adjustSecret(guess, test, secret);
                    //if (!debug) {
                    guessLeft = adjustGuesses(test, guess, guessLeft);
                    //}
                } else if (guess.length() == length) {
                    guessLeft = 0;
                    String test = list.getWordAt(0);
                    if (list.getSize() == 1 && test.equals(guess)) {
                        secret = new StringBuffer("guess");
                    }
                    lastGuess = guess;
                } else {
                    printInvalidGuess();
                }
                // check if there etc. if enter a word, and wrong make guessLeft
                if (!(secret.toString()).contains("-")) {
                    wins++;
                    System.out.println("Congrats. You win this round");
                    guessLeft = 0;
                }
                
            } // turn while end
            loss += checkLoss(secret);
            int i = adjustIndex(lastGuess, list);   
            choice = turn(list.getWordAt(i), gameNum, wins, loss, kb);
        }
        System.out.println("Goodbye! Thanks for Playing.");
        kb.close();
    } // end main

    /**
     * Adjust Index for Secret Word.
     * @param lastGuess last guess is the full word guess
     * @param list list of words
     * @return 0 if okay, 1 if need to take next word.
     */
    public static int adjustIndex(String lastGuess, DictionaryList list) {
        if (lastGuess.equals(list.getWordAt(0)) && list.getSize() != 1) {
            return 1;
        }
        return 0;
    }

    /**
     * Increments losses if need be.
     * @param secret secret word
     * @return 1 for loss, 0 otherwise
     */
    public static int checkLoss(StringBuffer secret) {
        if (secret.toString().contains("-")) {
            System.out.println("Sorry, looks like you lose this round.");
            return 1;
        }
        return 0;
    }

    /**
     * Prints Debug items.
     * @param list list of words
     * @param debug true if debugging, false otherwise
     */
    public static void printDebug(DictionaryList list, boolean debug) {
        if (!debug) {
            return;
        }
        for (int i = 0; i < list.getSize(); i++) {
            System.out.println(list.getWordAt(i));
        }
        System.out.println("Total words in list: " + list.getSize()); 
    } //end printDebug

    /**
     * Prints game stats and asks if you want to continue.
     * @param str secret word
     * @param gNum game number
     * @param w wins
     * @param l losses
     * @param kb scanner
     * @return choice as char
     */
    public static char turn(String str, int gNum, int w, int l, Scanner kb) {
        String choice;
        System.out.println("The secret word was " + str);
        System.out.println("Game Number: " + gNum);
        System.out.print("Wins: " + w);
        System.out.println(" Losses: " + l);
        System.out.println("Would you like to play again? 'y' for yes.");
        choice = kb.next();
        choice.toLowerCase();
        return choice.charAt(0);
    }

    /**
     * Adjusts the string buffer to fill in letters.
     * @param g
     *            the guess
     * @param str
     *            the word(random word from sort list)
     * @param sec
     *            secret buffer word
     */
    public static void adjustSecret(String g, String str, StringBuffer sec) {
        g = String.valueOf(g.charAt(0));
        for (int index = str.indexOf(g); 
             index >= 0; 
             index = str.indexOf(g, index + 1)) {
            sec.setCharAt(index, g.charAt(0));
        }
    }

    /**
     * Gets user guess.
     * @param gNum
     *            how many guesses user has left
     * @param sec
     *            secret word
     * @param kb
     *            scanner for input
     * @return string of guess
     */
    public static String getUserInput(int gNum, StringBuffer sec, Scanner kb) {
        System.out.println("Guesses Remaining: " + gNum);
        System.out.println(sec);
        System.out.print("Your Guess: ");
        String guess = kb.next();
        return guess.toLowerCase();
    }

    /**
     * Adjusts the amount of guesses.
     * 
     * @param test
     *            tmp word to check
     * @param guess
     *            guess from user
     * @param guessLeft
     *            amount of guesses left
     * @return returns adjusted guesses left
     */
    public static int adjustGuesses(String test, String guess, int guessLeft) {
        if (!test.contains(guess)) {
            return guessLeft - 1;
        }
        return guessLeft;
    }

    /**
     * Checks if file is there.
     * 
     * @param args
     *            cc args
     */
    public static void checkFile(String args) {
        File wordList = new File(args);
        if (wordList.exists() && !wordList.isDirectory()) {
            return;
        } else {
            printNoFileFound(args);
        }
    } // end checkFile

    /**
     * Method to set secret string dashes.
     * 
     * @param len
     *            length of word
     * @return a string containing the correct number of dashes
     */
    public static String setSecret(int len) {
        String str = "-";
        for (int i = 0; i < (len - 1); i++) {
            str += "-";
        }
        return str;
    } // end setSecret

    /**
     * Checks if there are arguments in command line.
     * 
     * @param argNum
     *            number of cc args
     */
    public static void checkArg(int argNum) {
        if (argNum == 0) {
            System.out.println("Proper Usage is: java program filename");
            System.exit(0);
        }
    } // end checkArg

    /**
     * Checks Debug mode.
     * 
     * @param argNum
     *            number of args
     * @param args
     *            checks if second arg equals DEBUG code word
     * @return true if debug, flase otherwise
     */
    public static boolean checkDebugOn(int argNum, String[] args) {
        if (argNum == 2 && args[1].equals("DEBUG")) {
            return true;
        }
        return false;
    } // end checkDebug

    /**
     * Prints an error message and exits program.
     * 
     * @param arg
     *            commandline arg for filename
     */
    public static void printNoFileFound(String arg) {
        System.out.print("No file by the name: ");
        System.out.println(arg + " was found.");
        System.exit(0);
    } // end printNoFileFound

    /**
     * prints an invalid guess message.
     */
    public static void printInvalidGuess() {
        System.out.print("Invalid Guess. ");
        System.out.print("Please pick a letter ");
        System.out.println("or word of correct size.");
    }
} // end class Hangman
