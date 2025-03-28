package trivia;

public class Player {
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

   public int getPosition() {
      return position + 1;
   }

   public void advances(int roll) {
      this.position = (this.position + roll) % Game.NUMBER_OF_PLACES;
   }

   public int getPurse() {
      return purse;
   }

   public void gainsCoin() {
      this.purse++;
   }

   public boolean hasNotWon() {
      return getPurse() < Game.NUMBER_OF_COINS_TO_WIN;
   }

   public boolean isInPenaltyBox() {
      return inPenaltyBox;
   }

   public boolean isFree() {
      return !isInPenaltyBox();
   }

   public void sendToPenaltyBox() {
      this.inPenaltyBox = true;
   }

   public void leavePenaltyBox() {
      if (isFree()) {
         throw new IllegalStateException(name + " is not in the penalty box");
      }
      this.inPenaltyBox = false;
   }
}
