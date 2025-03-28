package trivia;

import java.util.ArrayList;
import java.util.List;

public class Game implements GameInterface {
   public static final int NUMBER_OF_COINS_TO_WIN = 6;
   public static final int NUMBER_OF_PLACES = 12;

   private List<Player> players = new ArrayList<>();

   private Questions questions = new Questions();

   private Player currentPlayer;
   private Board board = new Board();

   public boolean add(String playerName) {
      var player = new Player(playerName);
      players.add(player);
      currentPlayer = players.getFirst();

      System.out.println(player.getName() + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public void roll(int roll) {
      System.out.println(currentPlayer.getName() + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (currentPlayer.isInPenaltyBox()) {
         if (roll % 2 != 0) {
            System.out.println(currentPlayer.getName() + " is getting out of the penalty box");
            currentPlayer.leavePenaltyBox();
         } else {
            System.out.println(currentPlayer.getName() + " is not getting out of the penalty box");
            return;
         }
      }
      currentPlayer.advances(roll);

      System.out.println(currentPlayer.getName()
                           + "'s new location is "
                           + currentPlayer.getPosition());
      var category = board.getCategory(currentPlayer);
      System.out.println("The category is " + category);
      System.out.println(questions.nextQuestion(category));
   }

   public boolean handleCorrectAnswer() {
      boolean hasNotWon = true;
      if (currentPlayer.isFree()) {
         System.out.println("Answer was correct!!!!");
         currentPlayer.gainsCoin();
         System.out.println(currentPlayer.getName()
                              + " now has "
                              + currentPlayer.getPurse()
                              + " Gold Coins.");

         hasNotWon = currentPlayer.hasNotWon();
      }
      nextPlayer();
      return hasNotWon;
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(currentPlayer.getName() + " was sent to the penalty box");
      currentPlayer.sendToPenaltyBox();

      nextPlayer();
      return true;
   }

   private void nextPlayer() {
      int currentPlayerIndex = players.indexOf(currentPlayer);
      currentPlayer = currentPlayerIndex < players.size() - 1 ?
         players.get(currentPlayerIndex + 1) : players.get(0);
   }
}
