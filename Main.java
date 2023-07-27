/*OLD MAID GAME
The user will participate in a game of Olda mIad with 3 other players (that are cpus). Their player number is randomized, and they wiill be given the chance to pick which hand to draw a card from, and which card from that hand to take. The objective is to keep taking cards from the opponents until 3 players' hands are empty and someone remains with one Queen. This means that they have the Old Maid, and they lose.*/

import java.util.*;
class Main {
  public static void main(String[] args) {
    
    try {
      LinkedList <PlayerHand> hands = new LinkedList<PlayerHand>(); //holds the player hands
      LinkedList <PlayerHand> potentialWinners = new LinkedList<PlayerHand>(); //holds the potential winners (no loser in this list)

      Thread.sleep(1000);
      playGame(hands,potentialWinners);//method that runs the game
    }
    catch (InterruptedException e){
      Thread.currentThread().interrupt();
    }
  }

  //this method runs the game - it uses the dealHands, cpuTurn, playerTurn, loseCheck and winnerCheck methods
  public static void playGame(LinkedList <PlayerHand> hands, LinkedList <PlayerHand> potentialWinners) {
    dealHands(hands); //deals thecards to each of the players
    while (hands.size()>1) {
      //The for loop to run through the game
      for (int i=0;i<hands.size();i++) {        
        if (i >= hands.size()){ // used to catch cases where index is greater than or equal to the number of hands/players
          break;
        }
        if (hands.get(i).isCpu()==false) { //checks to see if the player is a cpu or not - if they aren't then the playerTurn method is run, else, the cpuTurn is run
          hands.get(i).printHand();
          playerTurn(hands, i, potentialWinners);
          if (hands.size()<1){
            break;
          }
          System.out.println("___________________________________________________");
          System.out.println();
        }
        else { 
          cpuTurn(hands, i, potentialWinners);
          if (hands.size()<1){
            break;
          }
          System.out.println("___________________________________________________");
          System.out.println();

        }
      }
      
    }
    loseCheck(hands); //Checks for a loser
    winnerCheck(potentialWinners); // Prints out the winners of the game
  }
  
  public static void cpuTurn(LinkedList<PlayerHand> hands, int handNumber, LinkedList <PlayerHand> potentialWinners) { //Method used to run through the cpu's turn
    System.out.println("It is now player "+hands.get(handNumber).getHandNumber()+"'s turn.");
    Random randomNum = new Random();
    int handPick = handNumber;
    //ensures that the random hand picking is valid
    while (handPick == hands.get(handNumber).getHandNumber()) {
      handPick = randomNum.nextInt(hands.size());
    }
    //removes card from picked hand and adds to theirs
    Card card = hands.get(handPick).poll();
    hands.get(handNumber).add(card);
    int numberPairs = hands.get(handNumber).pairCheck();
    emptyCheck(hands,potentialWinners); //checks if any hands are empty
  }

  public static void playerTurn(LinkedList<PlayerHand> hands, int handNumber, LinkedList <PlayerHand> potentialWinners) {//Method used to run through the player's turn
    System.out.println("It's now your turn.");
    PlayerHand handPick = handPick(hands,handNumber,potentialWinners);// calls the handPick method which picks a valid hand
    cardPick(hands, handPick, handNumber, potentialWinners); // calls the cardPick method which picks a card from the chosen hand
    int numberPairs = hands.get(handNumber).pairCheck();
    emptyCheck(hands,potentialWinners); //checks if any hands are empty
    if (handNumber < hands.size()){
      int pairCount = hands.get(handNumber).getPairCount();
      int accumulatedPairs = pairCount + numberPairs;
      hands.get(handNumber).setPairCount(pairCount + accumulatedPairs);
    }
  }

  public static PlayerHand handPick(LinkedList<PlayerHand> hands, int handNumber, LinkedList <PlayerHand> potentialWinners) { //enables user to pick a hand to choose a card from
    LinkedList<Integer> availableHands = new LinkedList<Integer>();
    //checks for if the hand picking is valid
    for (int i=0;i<hands.size();i++){
      availableHands.add(hands.get(i).getHandNumber());
    }
    if (availableHands.indexOf(hands.get(handNumber).getHandNumber())!=-1){
      availableHands.remove(availableHands.indexOf(hands.get(handNumber).getHandNumber()));
    }
    System.out.println("");
    Scanner scn = new Scanner(System.in);
    int chosenHand = -1;
    int handIndex = -1;
    while (availableHands.indexOf(chosenHand) == -1 ){
      System.out.println("Please pick a valid hand.");
      System.out.println("Available hands are: "+availableHands);
      chosenHand = scn.nextInt();
      for (int i=0;i<hands.size();i++){
        if (chosenHand == hands.get(i).getHandNumber()){ 
          handIndex = i;
          break;
        }
      }
    }
    return hands.get(handIndex); //returns user hand pick
  }

  public static void cardPick(LinkedList<PlayerHand> hands, PlayerHand handPick, int handNumber, LinkedList <PlayerHand> potentialWinners) {// enables user to pick a card from the chosen hand from the handPick method
    Scanner scn = new Scanner(System.in);
    int cardIndex = -1;
    //ensures the card pick is a valid index/card
    while (0>cardIndex||cardIndex>handPick.getSize()-1) {
      System.out.println("This opponent has "+handPick.getSize()+" cards in their hand. Input which card (0-"+(handPick.getSize()-1)+") you'd like to take.");
      cardIndex = scn.nextInt();
    }
    //removes card from opponent hand and adds to player hand
    Card card = handPick.getCard(cardIndex);
    handPick.remove(card);
    hands.get(handNumber).add(card);
  }

  public static void dealHands(LinkedList<PlayerHand> hands) {//deals a valid deck of cards with one queen removed, initializes the playerHands
    Random randomNum = new Random();
    Deck deck = new Deck();
    deck.shuffle();
    for (int i=0;i<4;i++) {
      hands.add(new PlayerHand());
    }
    // deals cards to players and cpus and sets the player type (cpu or player)
    while (deck.isEmpty()!=true) {
      for (int i=0;i<4;i++) {
        if (deck.isEmpty()!=true){
          hands.get(i).setCpu(true);
          hands.get(i).add(deck.draw());
          hands.get(i).setHandNumber(i);
        }
      }
    }
    int makePlayer = randomNum.nextInt(4);
    hands.get(makePlayer).setCpu(false);
  }

  //Checks if player hands are empty - if so, they are safe from the Old Maid and spectate the game instead
  public static void emptyCheck(LinkedList<PlayerHand> hands, LinkedList <PlayerHand> potentialWinners) {
    LinkedList <Integer> removedHands = new LinkedList<Integer>();
    for (int i =0;i<hands.size();i++){
      if (hands.get(i).isEmpty()){
        System.out.println("Player "+hands.get(i).getHandNumber()+"'s hand is empty. They are safe from the Old Maid.");
        potentialWinners.add(hands.get(i)); //If they are safe from the Old Maid, then they are a winner
        hands.remove(i);
      }
    }
  }
  
  public static void loseCheck(LinkedList<PlayerHand> hands) {//prints out the loser
    System.out.println("Player "+hands.get(0).getHandNumber()+" has the Old Maid. They lose.");
  }
  
  public static void winnerCheck(LinkedList <PlayerHand> potentialWinners) {//prints out all the winners
    System.out.println("The winners are: ");
    for (int i=0;i<potentialWinners.size();i++) {
      System.out.println("Player "+i);
    }
    
  }
}