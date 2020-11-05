package tictactoe;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TicTacToeView extends Application
{
	String player, ai;
	protected Button chooseXButton, chooseOButton, newGameButton;
	protected MenuItem easy, medium, hard;
	protected ArrayList<Button> gamefieldButton = new ArrayList<Button>(9);
	
	private static TicTacToe tempTTT; 	// Workaround um die Instanz von TicTacToe in den Application thread zu schieben
	protected TicTacToe t;
	
	int x = 0;
	protected int count = 4, playercount = 0;
	protected int difficulty;
	
	private int tttWidth = 450;
	private int tttHeight = 800;

	public TicTacToeView(TicTacToe t) {
		tempTTT = t;
		launch();
	}
	
	public TicTacToeView() {
		this.t = tempTTT;
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Tic Tac Toe");

		/* Number field */
		GridPane gamefield = new GridPane();

		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(0), 0, 0);

		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(1), 1, 0);
		
		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(2), 2, 0);
		
		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(3), 0, 1);
		
		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(4), 1, 1);

		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(5), 2, 1);
		
		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(6), 0, 2);
		
		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(7), 1, 2);
		
		gamefieldButton.add(new MaxSizeButton(""));
		gamefield.add(gamefieldButton.get(8), 2, 2);
		
		chooseXButton = new MaxSizeButton("Start as X");
		gamefield.add(chooseXButton, 0, 4);
		
		chooseOButton = new MaxSizeButton("Start as O");
		gamefield.add(chooseOButton, 1, 4);
		
		/* Equals Button */
		HBox newGameBox = new HBox();
		newGameButton = new MaxSizeButton("Please Select a Difficulty to Start Game");
		newGameBox.getChildren().add(newGameButton);
		newGameBox.setPadding(new Insets(10));
		HBox.setHgrow(newGameButton, Priority.ALWAYS);
		newGameBox.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
		newGameButton.setDisable(true);
		newGameButton.setStyle("-fx-background-color: grey; -fx-opacity: 1;");
		
		/* Main Pane */
		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(gamefield);
		mainPane.setBottom(newGameBox);
		mainPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		
		/* Menu Bar */
		MenuBar menubar = new MenuBar();

		Menu view = new Menu("Difficulty");

		easy = new MenuItem("Easy");
		easy.setOnAction(ae -> setDifficulty(0));

		medium = new MenuItem("Medium");
		medium.setOnAction(ae -> setDifficulty(1));
		
		hard = new MenuItem("Hard");
		hard.setOnAction(ae -> setDifficulty(2));

		view.getItems().addAll(easy, medium, hard);
		
		menubar.getMenus().addAll(view);

		VBox globalPane = new VBox();
		globalPane.getChildren().addAll(menubar, mainPane);
		
		/* Scene */
		Scene mainScene = new Scene(globalPane, tttWidth, tttHeight);
			
		for(Button n: gamefieldButton) {
			n.setDisable(true);
			n.setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black;");
			n.setOnAction(v -> playerMove(n));
		}
		
		//Choose Starting Symbol
		chooseXButton.setOnAction(ae -> choose(1));
		chooseOButton.setOnAction(ae -> choose(0));
		newGameButton.setOnAction(ae -> reset());
		chooseXButton.setDisable(true);
		chooseOButton.setDisable(true);

		primaryStage.setResizable(false);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	protected void choose(int n) {
		if(n == 1) {
			player = "X";
			ai = "O";
			count = 4;
		} else {
			player = "O";
			ai = "X";
			count = 5;
			chooseDifficulty();
		}
		chooseXButton.setDisable(true);
		chooseOButton.setDisable(true);
		for(Button n1: gamefieldButton) {
			n1.setDisable(false);
		}
	}
	
	protected void setDifficulty(int diff) {
		difficulty = diff;
		easy.setDisable(true);
		medium.setDisable(true);
		hard.setDisable(true);
		newGameButton.setText("New Game");
		newGameButton.setDisable(false);
		chooseXButton.setDisable(false);
		chooseOButton.setDisable(false);
	}
	
	protected void chooseDifficulty() {
		if(difficulty == 0) {
			aiEasy();
		} else if(difficulty == 1) {
			aiMedium();
		} else {
			aiHard();
		}
	}
	
	protected void reset() {
		for(Button n : gamefieldButton) {
			n.setText("");
			n.setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black;");
			n.setDisable(true);
		}
		
		newGameButton.setText("New Game");
		newGameButton.setStyle("-fx-background-color: grey");
		chooseXButton.setDisable(false);
		chooseOButton.setDisable(false);
		easy.setDisable(false);
		medium.setDisable(false);
		hard.setDisable(false);
		playercount = 0;
	}

	private EventHandler<ActionEvent> playerMove(Button n) {
		n.setText(player);
		n.setDisable(true);
		n.setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em; -fx-opacity: 1;");
		if(win(player)) {
			newGameButton.setText("Player won!!\nNew Game?");
			newGameButton.setStyle("-fx-background-color: #63ebe6");
		} else {
			if(count > 0) {
				chooseDifficulty();
			}
		}
		playercount++;
		
	
		return null;
	}
	
	private void aiEasy() {
			
			do {
				x = (int) (Math.random() * 9);
			}while(gamefieldButton.get(x).getText() == "O" || gamefieldButton.get(x).getText() == "X");
			
			gamefieldButton.get(x).setText(ai);
			gamefieldButton.get(x).setDisable(true);
			gamefieldButton.get(x).setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em; -fx-opacity: 1;");
			count--;
			aiWinCondition();
		
	}
	
	private void aiWinCondition() {
		if(win(ai)) {
			newGameButton.setText("AI won!!\nNew Game?");
			newGameButton.setStyle("-fx-background-color: #ff4a4a");
		}
	}

	private void aiMedium() {
		int countering = winningMove();
		
		if(playercount == 0 || countering == 10) {
			do {
				countering = (int) (Math.random() * 9);
			}while(gamefieldButton.get(countering).getText() == "O" || gamefieldButton.get(countering).getText() == "X");
		}

	
		
		gamefieldButton.get(countering).setText(ai);
		gamefieldButton.get(countering).setDisable(true);
		gamefieldButton.get(countering).setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em;  -fx-opacity: 1;");
		count--;
		aiWinCondition();
	}
	
	private void aiHard() {
		int countering = winningMove();
		int tryWin = tryWin();
		int doMove = 0;
		
		if(playercount == 0 && ai == "O") {
			do {
				doMove = (int) (Math.random() * 9);
			} while(gamefieldButton.get(doMove).getText() == "O" || gamefieldButton.get(doMove).getText() == "X");
		} else if(tryWin == 10 && countering == 10) {
			doMove = nextMove();
		} else if (tryWin < 10) {
			doMove = tryWin;
		} else if(countering < 10) {
			doMove = countering;
		}
	
		gamefieldButton.get(doMove).setText(ai);
		gamefieldButton.get(doMove).setDisable(true);
		gamefieldButton.get(doMove).setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em; -fx-opacity: 1;");
		count--;
		aiWinCondition();
	}
	
	private int nextMove() {
		int count = 0;
		int winMove = 0;
		int canSet = 0;
		
		//checks Horizontal move Possibility
		for(int k = 0; k < 9; k += 3) {
			count = 0;
			canSet = 0;
			for(int i = 0; i < 3; i++){
				if(gamefieldButton.get(i + k).getText() == ai) {
					count++;
				} else if(gamefieldButton.get(i + k).getText() == player){
					canSet++;
				} else {
					winMove = (i + k);
				}
			}
			if(count == 1 && canSet == 0) {
				return winMove;
			} 
		}
		
		//Checks Vertical move Possibility
		for(int k = 0; k < 3; k++) {
			count = 0;
			canSet = 0;
			for(int i = 0; i < 9; i += 3) {
				if(gamefieldButton.get(i + k).getText() == ai) {
					count++;
				} else if(gamefieldButton.get(i + k).getText() == player){
					canSet++;
				} else {
					winMove = (i + k);
				}
			}
			if(count == 1 && canSet == 0) {
				return winMove;
			} 
		}
		
		count = 0;
		canSet = 0;
		//Checks Diagonal move Possibility
		for(int i = 0; i < 9; i +=4) {
			if(gamefieldButton.get(i).getText() == ai) {
				count++;
			} else if(gamefieldButton.get(i).getText() == player){
				canSet++;
			} else {
				winMove = i;
			}
		}
		if(count == 1 && canSet == 0) {
			return winMove;
		} 
		
		count = 0;
		canSet = 0;
		//Checks Diagonal move other direction Possibility
		for(int i = 2; i < 7; i +=2) {
			if(gamefieldButton.get(i).getText() == ai) {
				count++;
			} else if(gamefieldButton.get(i).getText() == player){
				canSet++;
			} else {
				winMove = i;
			}
		}
		
		if(count == 1 && canSet == 0) {
			return winMove;
		} 
		
		//When no possibly move to win, then a random move is done
		int doMove = 0;
		do {
			doMove = (int) (Math.random() * 9);
		} while(gamefieldButton.get(doMove).getText() == "O" || gamefieldButton.get(doMove).getText() == "X");
		
		return doMove;
	}
	
	private int tryWin() {
		int count = 0;
		int winMove = 0;
		
		//checks Horizontal Win Possibility
		for(int k = 0; k < 9; k += 3) {
			count = 0;
			for(int i = 0; i < 3; i++){
				if(gamefieldButton.get(i + k).getText() == ai) {
					count++;
				} else {
					winMove = (i + k);
				}
			}
			if(count == 2) {
				if(gamefieldButton.get(winMove).getText() != player) {
					return winMove;
				}
			} 
		}
		
		//Checks Vertical win Possibility
		for(int k = 0; k < 3; k++) {
			count = 0;
			for(int i = 0; i < 9; i += 3) {
				if(gamefieldButton.get(i + k).getText() == ai) {
					count++;
				} else {
					winMove = (i + k);
				}
			}
			if(count == 2) {
				if(gamefieldButton.get(winMove).getText() != player) {
					return winMove;
				}
			} 
		}
		
		count = 0;
		//Checks Diagonal win Possibility
		for(int i = 0; i < 9; i +=4) {
			if(gamefieldButton.get(i).getText() == ai) {
				count++;
			} else {
				winMove = i;
			}
		}
		if(count == 2) {
			if(gamefieldButton.get(winMove).getText() != player) {
				return winMove;
			}
		}
		
		count = 0;
		//Checks Diagonal Win other direction Possibility
		for(int i = 2; i < 7; i +=2) {
			if(gamefieldButton.get(i).getText() == ai) {
				count++;
			} else {
				winMove = i;
			}
		}
		
		if(count == 2) {
			if(gamefieldButton.get(winMove).getText() != player) {
				return winMove;
			}
		}
		return 10;
	}
	
	private int winningMove() {
		int count;
		int counterMove = 0;
		
		//checks Horizontal Win Possibility
		for(int k = 0; k < 9; k += 3) {
			count = 0;
			for(int i = 0; i < 3; i++){
				if(gamefieldButton.get(i + k).getText() == player) {
					count++;
				} else {
					counterMove = (i + k);
				}
			}
			if(count == 2) {
				if(gamefieldButton.get(counterMove).getText() != ai) {
					return counterMove;
				}
			} 
		}
		
		//Checks Vertical win Possibility
		for(int k = 0; k < 3; k++) {
			count = 0;
			for(int i = 0; i < 9; i += 3) {
				if(gamefieldButton.get(i + k).getText() == player) {
					count++;
				} else {
					counterMove = (i + k);
				}
			}
			if(count == 2) {
				if(gamefieldButton.get(counterMove).getText() != ai) {
					return counterMove;
				}
			} 
		}
		
		count = 0;
		//Checks Diagonal win Possibility
		for(int i = 0; i < 9; i +=4) {
			if(gamefieldButton.get(i).getText() == player) {
				count++;
			} else {
				counterMove = i;
			}
		}
		if(count == 2) {
			if(gamefieldButton.get(counterMove).getText() != ai) {
				return counterMove;
			}
		}
		
		
		count = 0;
		//Checks Diagonal Win other direction Possibility
		for(int i = 2; i < 7; i +=2) {
			if(gamefieldButton.get(i).getText() == player) {
				count++;
			} else {
				counterMove = i;
			}
		}
		
		if(count == 2) {
			if(gamefieldButton.get(counterMove).getText() != ai) {
				return counterMove;
			}
		}
		return 10;
	}
	
	private boolean win(String s) {
		int count = 0;
		
		//checks Horizontal Win
		for(int k = 0; k < 9; k += 3) {
			count = 0;
			for(int i = 0; i < 3; i++){
				if(gamefieldButton.get(i + k).getText() == s) {
					count++;
					if(count == 3) {
						disable();
						for(int j = (i + k); j > ((i + k) - 3); j--) {
							gamefieldButton.get(j).setStyle("-fx-background-color: grey; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em;");
						}
						return true;
					}
				}
			}
		}
		
		//Checks Vertical win
		for(int k = 0; k < 3; k++) {
			count = 0;
			for(int i = 0; i < 9; i += 3) {
				if(gamefieldButton.get(i + k).getText() == s) {
					count++;
					if(count == 3) {
						disable();
						for(int j = (i + k); j > ((i + k) - 7); j -= 3) {
							gamefieldButton.get(j).setStyle("-fx-background-color: grey; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em;");
						}
						return true;
					}
				}
			}
		}
		count = 0;
		//Checks Diagonal win
		for(int i = 0; i < 9; i +=4) {
			if(gamefieldButton.get(i).getText() == s) {
				count++;
				if(count == 3) {
					disable();
					for(int j = i; j >= 0; j -= 4) {
						gamefieldButton.get(j).setStyle("-fx-background-color: grey; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em;");
					}
					return true;
				}
			}
		}
		count = 0;
		//Checks Diagonal Win other direction
		for(int i = 2; i < 7; i +=2) {
			if(gamefieldButton.get(i).getText() == s) {
				count++;
				if(count == 3) {
					disable();
					for(int j = i; j >= 1; j -= 2) {
						gamefieldButton.get(j).setStyle("-fx-background-color: grey; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em;");
					}
					return true;
				}
			}
		}
		return false;
	}
	
	//Disable Buttons
	private void disable() {
		for(Button t : gamefieldButton) {
			t.setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black; -fx-font-size: 5em; -fx-opacity: 0.5");
			t.setDisable(true);
		}
	}
	
	private class MaxSizeButton extends Button {
		public MaxSizeButton(String text) {
			super(text);
			setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			setMinSize(150, 150);
		}
	}

}
