import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SideButtonTester extends Application {
  private static class Counter {
    private int count = 0;
    public final Text display = new Text(String.valueOf(count));

    public void increment() {
      ++count;
      updateDisplay();
    }

    public void reset() {
      count = 0;
      updateDisplay();
    }

    private void updateDisplay() {
      display.setText(String.valueOf(count));
    }
  }

  private enum ScrollDirection {
    UP,
    DOWN
  }

  private final double WINDOW_WIDTH = 350, WINDOW_HEIGHT = 350;

  private final GridPane mainGrid = new GridPane();

  final Counter
    leftClickCounter = new Counter(),
    scrollClickCounter = new Counter(),
    rightClickCounter = new Counter(),
    forwardClickCounter = new Counter(),
    backClickCounter = new Counter(),
    scrollUpCounter = new Counter(),
    scrollDownCounter = new Counter();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    {
      final double gap = 10;
      mainGrid.setHgap(gap);
      mainGrid.setVgap(gap);
      mainGrid.setPadding(new Insets(gap));
    }
    mainGrid.setAlignment(Pos.CENTER);

    {
      final Button resetBtn = new Button("Reset");
      resetBtn.setOnAction(event -> {
        leftClickCounter.reset();
        scrollClickCounter.reset();
        rightClickCounter.reset();
        forwardClickCounter.reset();
        backClickCounter.reset();
        scrollUpCounter.reset();
        scrollDownCounter.reset();
      });
      mainGrid.add(resetBtn, 2, 0);
      GridPane.setHalignment(resetBtn, HPos.CENTER);
    }

    //                                                                             x  y
    createScrollBox("Scroll Up",     ScrollDirection.UP,     scrollUpCounter,      1, 0);
    createClickBox( "Left Click",    MouseButton.PRIMARY,    leftClickCounter,     0, 1);
    createClickBox( "Scroll Click",  MouseButton.MIDDLE,     scrollClickCounter,   1, 1);
    createClickBox( "Right Click",   MouseButton.SECONDARY,  rightClickCounter,    2, 1);
    createClickBox( "Forward Click", MouseButton.FORWARD,    forwardClickCounter,  0, 2);
    createScrollBox("Scroll Down",   ScrollDirection.DOWN,   scrollDownCounter,    1, 2);
    createClickBox( "Back Click",    MouseButton.BACK,       backClickCounter,     0, 3);

    primaryStage.setTitle("Side Button Tester");
    primaryStage.setScene(new Scene(mainGrid));
    primaryStage.setWidth(WINDOW_WIDTH);
    primaryStage.setHeight(WINDOW_HEIGHT);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private void createClickBox(
    final String heading,
    final MouseButton button,
    final Counter counter,
    final int xPos,
    final int yPos
  ) {
    final GridPane grid = createGridPane();
    final Label label = new Label(heading);

    grid.add(label, 0, 0);
    grid.add(counter.display, 0, 1);

    GridPane.setHalignment(label, HPos.CENTER);
    GridPane.setHalignment(counter.display, HPos.CENTER);

    grid.setOnMouseClicked(event -> {
      if (event.getButton() == button) {
        counter.increment();
      }
    });

    mainGrid.add(grid, xPos, yPos);
  }

  private void createScrollBox(
    final String heading,
    final ScrollDirection scrollDir,
    final Counter counter,
    final int xPos,
    final int yPos
  ) {
    final GridPane grid = createGridPane();
    final Label label = new Label(heading);

    grid.add(label, 0, 0);
    grid.add(counter.display, 0, 1);

    GridPane.setHalignment(label, HPos.CENTER);
    GridPane.setHalignment(counter.display, HPos.CENTER);

    grid.setOnScroll(event -> {
      final var deltaY = event.getDeltaY();
      if (
        (scrollDir == ScrollDirection.UP && deltaY > 0) ||
        (scrollDir == ScrollDirection.DOWN && deltaY < 0)
      ) {
        counter.increment();
      }
    });

    mainGrid.add(grid, xPos, yPos);
  }

  private GridPane createGridPane() {
    final GridPane grid = new GridPane();

    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(10));
    grid.setStyle("-fx-border-width: 1; -fx-border-color: black");

    return grid;
  }
}
