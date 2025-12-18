import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }

  public static double totalSentiment (String fileNameString1) {
    int i = 0;
    String in = textToString(fileNameString1).replaceAll("\\p{Punct}", "");
    int test = in.indexOf(" ");
    double result = 0;
    while (test != -1){
      result += sentimentVal(in.substring(i, test));
      i = test + 1;
      test = in.indexOf(" ", i);
    }
    result += sentimentVal(in.substring(i));
    return result;
  }
  public static int starRating(String fileNameString1){
    double totalResult = totalSentiment(fileNameString1);
    if (totalResult < 0){
      return 0;
    } else if (totalResult >= 0 && totalResult < 8 ){
      return 1;
    } else if (totalResult >= 8 && totalResult < 16){
      return 2;
    } else if (totalResult >= 16 && totalResult < 24){
      return 3;
    } else if (totalResult >= 24 && totalResult < 34){
      return 4;
    } else {
      return 5;
    }
  }
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }
  
    /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
  
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }

  public static String fakeReview(String fileName){
    String in = textToString(fileName);
    int start = 0;//set start index to 0
    int star = in.indexOf("*");//find the first star
    int space = in.indexOf(" ", star); //find the space after the first star
    String result = " ";
    while (star != -1){ //when there is a *
      result += in.substring(start, star); //1st iter: index 0 --> first *
      result += randomAdjective() + " "; //append an adjective
      start = space + 1; // new starting position is the space after the first *adjective +1
      star = in.indexOf("*", start); //find star after the first adjective
      space = in.indexOf(" ", star); //find the space after the new star
    }
    result += in.substring(start);//Continue after
    return result;
  }
  public static String turnReviewNeg(String fileName){
    String in = textToString(fileName);
    int start = 0;//set start index to 0
    int star = in.indexOf("*");//find the first star
    int space = in.indexOf(" ", star); //find the space after the first star
    String result = " ";
    while (star != -1){ //when there is a *
      if(sentimentVal(removePunctuation(in.toLowerCase().substring(star + 1, space))) > 0){ //if positive connotation
         result += in.substring(start, star); //1st iter: index 0 --> first *
         result += randomNegativeAdj() + " "; //append a neg adjective
      }else{
         result += in.substring(start, star); //1st iter: index 0 --> first *
         result += in.substring(star + 1, space) + " "; //append *+1 --> space
    }  
      start = space + 1; //new starting position is the space after the first *adjective +1
      star = in.indexOf("*", start); //find star after the first adjective
      space = in.indexOf(" ", star); //find the space after the new star
  }
    result += in.substring(start);//Continue after
    return result;
  }
  public static String search (String searchString, String fileRoot) {
  String content = textToString(fileRoot);
  String contentLower = content.toLowerCase();
  String searchLower = searchString.toLowerCase();
  int occurrences = 0;
  double sentimentSum = 0;
  int sentimentCount = 0;
  int value = contentLower.indexOf(searchLower);
  int lastLeft = -1;
  int lastRight = -1;
  while (value != -1) {
    occurrences++;
    int end = value + searchLower.length() - 1;
    int left = value;
    while (left > 0 && Character.isLetter(content.charAt(left - 1))) {
      left--;
    } 
    while (left > 0 && content.charAt(left - 1) != ' ') {
    left--;
}
    int right = end;
    while (right < content.length() - 1 && Character.isLetter(content.charAt(right + 1))) {
      right++;
    }
    if (left != lastLeft) {
      if (right != lastRight) {
        String word = content.substring(left, right + 1);
        word = removePunctuation(word.toLowerCase());
        sentimentSum += sentimentVal(word);
        sentimentCount++;
        lastLeft = left;
        lastRight = right;
  }
}
    value = contentLower.indexOf(searchLower, value + 1);
  }
  double avgSentiment = (sentimentCount == 0) ? 0.0 : (sentimentSum / sentimentCount);
  return "Found occurrence " + occurrences + " in file: " + fileRoot
      + "\nAverage sentiment of matched words: " + avgSentiment;
}
}