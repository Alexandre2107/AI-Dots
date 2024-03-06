
import java.util.HashMap;

public class Box {

  public enum Position {
    TOP, BOTTOM, LEFT, RIGHT
  }

  HashMap<Position, Boolean> lines = new HashMap<Position, Boolean>();
  public String player = "";

  Box(HashMap<Position, Boolean> lines) {

    this.lines = lines;

  }

  @Override
  public Box clone() {

    Box box = new Box((HashMap<Position, Boolean>) this.lines.clone());

    return box;
  }

  public boolean isBoxOpen() {

    return this.lines.containsValue(Boolean.FALSE);

  }

}