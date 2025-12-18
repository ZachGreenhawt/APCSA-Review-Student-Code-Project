
public class Main {
  public static void main(String[] args) {   
   System.out.println("fakeReview: ");
   System.out.println(Review.fakeReview("26WestFake.txt"));
   System.out.println("turnReviewNeg: ");
   System.out.println(Review.turnReviewNeg("26WestFake.txt"));
   System.out.println(Review.search("a", "26WestReview.txt"));
  }
}