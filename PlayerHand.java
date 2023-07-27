/* PLAYER HAND CLASS
A class that creates a hand for players. This class has a constructor, a hand size accessor, and card accessor, an add card method, a remove card method, a remove-at-index method, a poll method, and a method that checks for pairs in the hand and removes them.*/

import java.util.*;
public class PlayerHand {
  //Instance Variables
  private LinkedList<Card> hand = new LinkedList<Card>();
  private int pairCount;
  private int winOrder; 
  private int handNumber;
  private boolean isCpu;
  
  //Constructor
  public PlayerHand() {
    pairCount = 0;
  }
  //Accessors
  public int getPairCount() {
    return this.pairCount;
  }
  public int getHandNumber(){
    return this.handNumber;
  }
  public int getSize() {
    return hand.size();
  }

  //Mutators
  public void setPairCount(int pairCount){
    this.pairCount = pairCount;
  }
  public void setWinOrder(int winOrder){
    this.winOrder = winOrder;
  }
  public void setHandNumber(int handNumber){
    this.handNumber = handNumber;
  }
  public void setCpu(boolean isCpu){
    this.isCpu = isCpu;
  }

  //Print hand method: Prints out the hand
  public void printHand() {
    System.out.println(hand);
  }

  //Add card-at-index: Index determines which card to add to the hand
  public Card getCard(int index) {
    return hand.get(index);
  }

  //Add card method
  public void add(Card card) {
    hand.add(card);
  }
  //Remove card method: Removes the card from the hand
  public void remove(Card card) {
    hand.remove(card);
  }
  
  //Remove-at-index method: Removes the card at the specified index
  public void remove(int index) {
    hand.remove(index);
  }

  //Poll method: Removes card at the top of the hand
  public Card poll() {
    return hand.poll();
  }
  //CPU check method: Checks to see if the player and hand are a CPU
  public boolean isCpu() {
    return this.isCpu;
  }

  //isEmpty method: Checks if the hand is empty
  public boolean isEmpty() {
    if (hand.size() == 0) {
      return true;
    }
    else {
      return false;
    }
  }

  //Pair check and removal method: Checks for pairs in the hand and removes them; also keeps count of the number of pairs for winner determining purposes
  public int pairCheck() {
    for (int i=0;i<hand.size();i++) {
      for (int j=i+1;j<hand.size();j++) {
        Card card1 = hand.get(i);
        Card card2 = hand.get(j);
        if (card1.getRank() == card2.getRank()) {
          pairCount++;
          hand.remove(hand.indexOf(card1));
          hand.remove(hand.indexOf(card2));
          System.out.println("\nPaired Cards: "+card1.toString()+" and "+card2.toString()+" were discarded.");
          if (i!=0) {
            i--;
          }
          break;
        }
      }
    }
    return pairCount;
  }
  
}