package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Player {
   private final String name;
   private int position;
   private int purse;
   private boolean inPenaltyBox;

   public Player(String name) {
      this.name = name;
      this.position = 0;
      this.purse = 0;
      this.inPenaltyBox = false;
   }

   public String getName() {
      return name;
   }

   public boolean isInPenaltyBox() {
      return inPenaltyBox;
   }

   public void advances(int roll) {
      this.position = (this.position + roll) % Game.NUMBER_OF_PLACES;
   }

   public int getPosition() {
      return position + 1;
   }

   public void gainsCoin() {
      this.purse++;
   }

   public int getPurse() {
      return purse;
   }

   public void sendToPenaltyBox() {
      this.inPenaltyBox = true;
   }
}

public class Game implements GameInterface {
   private static final int NUMBER_OF_COINS_TO_WIN = 6;

   public static final int NUMBER_OF_PLACES = 12;

   List<Player> players = new ArrayList<>();

   List<String> popQuestions = new LinkedList<>();
   List<String> scienceQuestions = new LinkedList<>();
   List<String> sportsQuestions = new LinkedList<>();
   List<String> rockQuestions = new LinkedList<>();

   int currentPlayerIndex = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      for (int i = 0; i < 50; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   public boolean isPlayable() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      var player = new Player(playerName);
      players.add(player);

      System.out.println(player.getName() + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      var currentPlayer = players.get(currentPlayerIndex);
      System.out.println(currentPlayer.getName() + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (currentPlayer.isInPenaltyBox()) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(currentPlayer.getName() + " is getting out of the penalty box");
            currentPlayer.advances(roll);

            System.out.println(currentPlayer.getName()
                               + "'s new location is "
                               + currentPlayer.getPosition());
            System.out.println("The category is " + currentCategory());
            askQuestion();
         } else {
            System.out.println(currentPlayer.getName() + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {
         currentPlayer.advances(roll);

         System.out.println(currentPlayer.getName()
                              + "'s new location is "
                              + currentPlayer.getPosition());
         System.out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      if (currentCategory() == "Pop")
         System.out.println(popQuestions.removeFirst());
      if (currentCategory() == "Science")
         System.out.println(scienceQuestions.removeFirst());
      if (currentCategory() == "Sports")
         System.out.println(sportsQuestions.removeFirst());
      if (currentCategory() == "Rock")
         System.out.println(rockQuestions.removeFirst());
   }


   private String currentCategory() {
      var currentPlayer = players.get(currentPlayerIndex);

      if (currentPlayer.getPosition() - 1 == 0) return "Pop";
      if (currentPlayer.getPosition() - 1 == 4) return "Pop";
      if (currentPlayer.getPosition() - 1 == 8) return "Pop";
      if (currentPlayer.getPosition() - 1 == 1) return "Science";
      if (currentPlayer.getPosition() - 1 == 5) return "Science";
      if (currentPlayer.getPosition() - 1 == 9) return "Science";
      if (currentPlayer.getPosition() - 1 == 2) return "Sports";
      if (currentPlayer.getPosition() - 1 == 6) return "Sports";
      if (currentPlayer.getPosition() - 1 == 10) return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      var currentPlayer = players.get(currentPlayerIndex);
      if (currentPlayer.isInPenaltyBox()) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            currentPlayer.gainsCoin();
            System.out.println(currentPlayer.getName()
                               + " now has "
                               + currentPlayer.getPurse()
                               + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayerIndex++;
            if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;

            return winner;
         } else {
            currentPlayerIndex++;
            if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
            return true;
         }


      } else {

         System.out.println("Answer was corrent!!!!");
         currentPlayer.gainsCoin();
         System.out.println(currentPlayer.getName()
                           + " now has "
                           + currentPlayer.getPurse()
                           + " Gold Coins.");

         boolean winner = didPlayerWin();
         currentPlayerIndex++;
         if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      var currentPlayer = players.get(currentPlayerIndex);
      System.out.println("Question was incorrectly answered");
      System.out.println(currentPlayer.getName() + " was sent to the penalty box");
      currentPlayer.sendToPenaltyBox();

      currentPlayerIndex++;
      if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
      return true;
   }


   private boolean didPlayerWin() {
      return !(players.get(currentPlayerIndex).getPurse() == NUMBER_OF_COINS_TO_WIN);
   }
}
