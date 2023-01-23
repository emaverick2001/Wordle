import java.util.Scanner;
import java.util.Random;


/*
 * Project 2: Wordle
 * Maverick Espinosa
    mespin11
    10/06/22
 * This project has you create a text based version of Wordle
 * (https://www.nytimes.com/games/wordle/index.html). Wordle is a word guessing
 * game in which you have 6 tries to guess a 5-letter word. You are told whether
 * each letter of your guess is in the word and in the right position, in the
 * word but in the wrong position, or not in the word at all.
 *
 * Some key differences in our version are:
 *
 * - Text menu based with no grid. Players have to scroll up to see their
 * previous guesses.
 *
 * - Support for 4, 5, or 6 letter words
 *
 * - We don't check for whether a guess is a valid word or not. Players can
 * guess anything they want (of the correct length).
 *
 * Fun facts: The original Wordle was developed by Josh Wardle. Wardle's wife
 * chose the official word list for the game.
 *
 * 500.112 Gateway Computing: Java Spring 2022
 */
 
public class Wordle {

   /**
    * Defining the only Random variable you may (and must) use. DO NOT CHANGE
    * THIS LINE OF CODE.
    */
   static Random gen = new Random(0);

   /**
    * Defines the number of guesses the player starts with for each word. DO NOT
    * CHANGE THIS LINE OF CODE.
    */
   static final int MAX_GUESSES = 6;
   /**
    * Defines the number of hints the player starts with for each word. DO NOT
    * CHANGE THIS LINE OF CODE.
    */
   static final int MAX_HINTS = 2;

   /**
    * The main method. This is where most of your menu logic and game logic
    * (i.e. implementation of the rules of the game ) will end up. Feel free to
    * move logic in to smaller subroutines as you see fit.
    *
    * @param args commandline args
    */
   public static void main(String[] args) {
     
      boolean gameRunning = true;
      Scanner kb = new Scanner(System.in);
      int maxGuesses = 6;
      int maxHints = 2; 
      String realWord = newWord();
      
      while (gameRunning) {
         printMenu();
      
         System.out.print("Please enter a choice: ");
         String text = kb.nextLine();
         
         if (text.equals("") || text.length() > 1) {
            System.out.println("Invalid option! Try again!");
            continue;
         }
      
         if (text.equals("n") || text.equals("N")) {
            realWord = newWord();
            maxGuesses = MAX_GUESSES;
            maxHints = MAX_HINTS;
            
         }
         
         else if (text.equals("h") || text.equals("H")) {
            if (maxHints > 0) {
               giveHint(realWord);
               maxHints--;
               if (maxHints == 1) {
                  System.out.println("You have " + maxHints + " hint remaining.");
               }
               else {
                  System.out.println("You have " + maxHints + " hints remaining.");
               }
            }
            else {
               System.out.println("Sorry, you're out of hints!");
            }
         }
         else if (text.equals("g") || text.equals("G")) {
            
            if (maxGuesses > 0) {
               System.out.println("Enter a guess: ");
               text = kb.nextLine();
               if (validateGuess(realWord.length(), text)) { 
                  maxGuesses--;
                  if (checkGuess(realWord, text)) {
                     System.out.println("Congratulations! You found the word!");
                     maxGuesses = 0;
                     maxHints = 0;
                  }
                  else {
                     if (maxGuesses == 0) {
                        System.out.println("Sorry, you're out of guesses! The real word was " 
                        + realWord + ". Use the \"n\"/\"N\" command to play again.");
                     }
                     else if (maxGuesses == 1) {
                        System.out.println("You have " + maxGuesses + " guess remaining");
                     }
                     else {
                        System.out.println("You have " + maxGuesses + " guesses remaining");  
                     }
                        
                  }
               }
            }
                     
               
         }
         else if (text.equals("e") || text.equals("E")) {
            gameRunning = false;
         }
         else {
            System.out.println("Invalid option! Try again!");
         } 
      }
   }

   /**
    * Prints "HINT! The word contains the letter: X" where X is a randomly
    * chosen letter in the word parameter.
    *
    * @param word The word to give a hint for.
    */
   static void giveHint(String word) {
      int wordLength = gen.nextInt(word.length());
      System.out.println("HINT! The word contains the letter: " + word.charAt(wordLength));
   }
   

   /**
    * Checks the players guess for validity. We define a valid guess as one that
    * is the correct length and contains only lower case letters and upper case
    * letters. If either validity condition fails, a message is printed 
    * indicating which condition(s) failed.
    *
    * @param length The length of the current word that the player is trynig to
    *               guess.
    * @param guess  The guess that the player has entered.
    * @return true if the guess is of the correct length and contains only valid
    * characters, otherwise false.
    */
   static boolean validateGuess(int length, String guess) {
      int count = 0;
      boolean isValid = true;
      //checks to see if length of guess matches length of word
      if (guess.length() != length) {
         System.out.println("You must enter a guess of length " + length);
         isValid = false;
      }
      //checks to see if character in guess is a letter
      //only loops until it finds a character that is not a letter
      
      while (count < guess.length()) {
         if (Character.isLetter(guess.charAt(count))) {
            count += 1;
         }
         else {
            System.out.println("Your guess must only contain upper case letters and lower case letters");
            isValid = false;
            break;
         }
      }
      
      return isValid;
   }

   /**
    * Checks the player's guess against the current word. Capitalization is
    * IGNORED for this comparison. This function also prints a string
    * corresponding to the player's guess. An ? indicates a letter that isn't in
    * the word at all. A lower case letter indicates that the letter is in the
    * word but not in the correct position. An upper case letter indicates a
    * correct letter in the correct position. Example:
    *
    * SPLINE (the correct word)
    *
    * SPEARS (the player's guess)
    *
    * SPe??s (the output printed by this function)
    *
    * Suggestion 1: Convert guess to upper case before doing anything else. This
    * can help simplify later logic.
    *
    * Suggestion 2: Consider using String.indexOf
    *
    * @param word  The current word the player is trying to guess.
    * @param guess The guess that a player has entered.
    * @return true if the word and guess match IGNORING CASE, otherwise false.
    */
   static boolean checkGuess(String word, String guess) {
      String feedback = "";
      
      if (word.toUpperCase().equals(guess.toUpperCase())) {
         return true;
      } 
      int pointer = 0;
      guess = guess.toUpperCase();
      while (pointer < word.length()) {
         char character = guess.charAt(pointer);
         int indexOfWord = word.indexOf(character);
         if (pointer == indexOfWord) {
            feedback += Character.toUpperCase(character);
         }
         else {
            if (indexOfWord == -1) {
               feedback += '?';
            }
            else {
               feedback += Character.toLowerCase(character);
            }
         }
         pointer++;
      }
      System.out.println(feedback);
      return false;
   }

   /**
    * Chooses a random word using WordProvider.getWord(int length). This should
    * print "New word length: X" where x is the length of the word.
    *
    * @return the randomly chosen word
    */
   static String newWord() {
      int wordLength = gen.nextInt(3) + 4;
      String newWord = WordProvider.getWord(wordLength);
      System.out.println("New word length: " + wordLength);
      return newWord; 
   }

   /**
    * Prints menu options.
    */
   static void printMenu() {
      System.out.println("n/N: New word");
      System.out.println("h/H: Get a hint");
      System.out.println("g/G: Make a guess");
      System.out.println("e/E: Exit");
      System.out.println("-------------");
   }
}
