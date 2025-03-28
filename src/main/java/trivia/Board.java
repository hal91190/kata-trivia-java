package trivia;

import java.util.List;

public class Board {
   private final List<Category> categories =
      List.of(
         Category.POP, Category.SCIENCE, Category.SPORTS, Category.ROCK,
         Category.POP, Category.SCIENCE, Category.SPORTS, Category.ROCK,
         Category.POP, Category.SCIENCE, Category.SPORTS, Category.ROCK
         );

   public Category getCategory(Player player) {
      return getCategory(player.getPosition());
   }

   public Category getCategory(int position) {
      position--;
      if (position < 0 || position >= categories.size()) {
         throw new IllegalArgumentException("Invalid index");
      }
      return categories.get(position);
   }
}