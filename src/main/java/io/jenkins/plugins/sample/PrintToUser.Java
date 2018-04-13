//Author Charles Maroudas

package io.jenkins.plugins.sample;
import java.util.Arrays;
import java.io.*;
import java.util.ArrayList;

/*public class PrintArrayInJava1 {
	   private static void printArray(int[] anArray) {
	      System.out.println(anArray.toString());
	   }
	   public static void main(String[] args) {
	      int[] testArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	      printArray(testArray);
	   }*/
	
/*public class PrintArrayInJava2 {
   private static void printArray(int[] anArray) {
      for (int i = 0; i < anArray.length; i++) {
         if (i > 0) {
            System.out.print(", ");
         }
         System.out.print(anArray[i]);
      }
   }
   public static void main(String[] args) {
      int[] testArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
      printArray(testArray);
   }
}*/

/**
 * Program That Prints An Array In Java using Arrays.toString().
 */
public class PrintArrayInJava {
   private static void printArray(int[][] anArray) {
      System.out.println(Arrays.toString(anArray));
   }
   public static void main(String[] args) {
      int[][] testArray = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
      printArray(testArray);
   }
}


