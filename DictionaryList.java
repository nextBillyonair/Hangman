/** 600.226 Data Structures 
 * William Watson - wwatso13.
 * Joshan Bajaj - jbajaj1.
 * P1
 * DictionaryList.java
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;

/**
 * Dictionary List CLass.
 * William Watson - wwatso13
 * Joshan Bajaj -   jbajaj1
 *
 */
public final class DictionaryList {
    /**
     * minimum amount of letters in a word.
     */
    private int min;
    
    /**
     * max amount of letters in a word.
     */
    private int max;
    
    /**
     * List of words.
     */
    private ArrayList<String> list = new ArrayList<String>();
    
    /**
     * Dictionary List Constructor.
     * @param s String file name
     * @throws IOException ioexception for file
     */
    public DictionaryList(String s) throws IOException {
        Scanner file = new Scanner(new FileReader(s));
        this.min = 1;
        this.max = 2;
        while (file.hasNext()) {
            String word = file.next();
            word = word.toLowerCase();
            this.list.add(word);
            if (word.length() > this.max) {
                this.max = word.length();
            } else if (word.length() < this.min) {
                this.min = word.length();
            }
        }
        file.close();
    } //end master constructor
    
    /**
     * Makes new list based upon word length.
     * @param len length for words
     * @param old old list that is being partioned
     */
    public DictionaryList(int len, DictionaryList old) {
        this.min = len;
        this.max = len;
        for (int i = 0; i < old.getSize(); i++) {
            String str = old.getWordAt(i);
            if (str.length() == len) {
                this.list.add(str);
            }
        }
    } //end length constructor
    
    /**
     * Constructor for smaller lists based upon letter.
     * @param ch The user's guess
     * @param old The previous Dictionary list
     */
    public DictionaryList(char ch, DictionaryList old) {
        this.max = old.getMax();
        this.min = old.getMin();
        ArrayList<String> tmp = new ArrayList<String>();
        int size = 0;
        int index = 0;
        String indexStr = "";
        HashMap<Integer, ArrayList<String>> sort;
        sort = new HashMap<Integer, ArrayList<String>>();
        
        for (int i = 0; i < old.getSize(); i++) {
            ArrayList<String> tmp1 = new ArrayList<String>();
            String str = old.getWordAt(i);
            //creates binary for each word, aka on off for letter
            for (int j = 0; j < str.length(); j++) {
                char charAt = str.charAt(j);
                if (charAt == ch) {
                    indexStr += "1";
                } else {
                    indexStr += "0";
                }
            }
            //converts from binary to decimal
            index = Integer.parseInt(indexStr, 2);
            //sorts words into arraylist in map by index number
            if (!sort.containsKey(index)) {
                sort.put(index, tmp1);
                sort.get(index).add(str);
            } else {
                sort.get(index).add(str);
            }
            indexStr = "";
        }
        //iterates over all keys to find largest 
        //sized one and sets array lsit equal to it
        for (Integer key: sort.keySet()) {
            if (sort.get(key).size() > size) {
                tmp = sort.get(key);
                size = sort.get(key).size();
            }
        }
        this.list = tmp;
    } //end guess constructor
    
    
    /**
     * Adds word to new list.
     * @param str string to be added
     */
    public void addWord(String str) {
        str.toLowerCase();
        this.list.add(str);
    } //end addWord
    
    /**
     * Sets the word length.
     * @return word length
     */
    public int setLength() {
        Random rand = new Random();
        int random = 0;
        random = rand.nextInt(this.list.size());
        return this.list.get(random).length();
    } //end setLength()
    
        
    /**
     * Gets min num of letters.
     * @return min num of letters
     */
    public int getMin() {
        return this.min;
    } //end getMin
    
    /**
     * Return max letters.
     * @return max number;
     */
    public int getMax() {
        return this.max;
    } //end getMax
    
    /**
     * Gets size of list.
     * @return size of list
     */
    public int getSize() {
        return this.list.size();
    } //end getSize
    
    /**
     * Returns the word at a position index in the list.
     * @param index index of word
     * @return word found
     */
    public String getWordAt(int index) {
        return this.list.get(index);
    } //end getWordAt
    
} //end DictionaryList class
