import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
Space Invaders
 */
public class SpaceInvaders extends Application{
	//Useful constants
	//Screen size
	public static final double SCREEN_WIDTH = 1280;
	public static final double SCREEN_HEIGHT = 800;

	//Speeds
	public static final double PLAYER_SPEED = 6;
	public static final double PLAYER_BULLET_SPEED = 8;
	public static double ENEMY_SPEED = 0.5;
	public static final double ENEMY_VERTICAL_SPEED = 45;
	public static final double ENEMY1_BULLET_SPEED = 3;
	public static final double ENEMY2_BULLET_SPEED = 5;
	public static final double ENEMY3_BULLET_SPEED = 7;
	public static final double RELOAD_TIME_ns = 5e8;

	//global variables
	public static int numScore = 0;
	public static int numLives = 3;
	public static int numLevel = 1;

	//sounds
	AudioClip shootSound = new AudioClip("file:src/sounds/shoot.wav");
	AudioClip invaderDeath = new AudioClip("file:src/sounds/invaderkilled.wav");
	AudioClip playerDeath = new AudioClip("file:src/sounds/explosion.wav");
	AudioClip menuBG = new AudioClip("file:src/sounds/FTLmaintheme.m4a");
	AudioClip battleBG = new AudioClip("file:src/sounds/FTLmilkyway.m4a");

	@Override
	public void start(Stage stage) {
		stage.setTitle("Space Invaders");
		stage.setResizable(false);

		Scene startScreen = initializeStart();

		//common header between levels
		Text score = new Text("Score: " + numScore);
		score.setFont(Font.loadFont("file:src/fonts/Fipps-Regular.otf", 24));
		Text lives = new Text("Lives: " + numLives);
		lives.setFont(Font.loadFont("file:src/fonts/Fipps-Regular.otf", 24));
		Text currLevel = new Text("Level: " + numLevel);
		currLevel.setFont(Font.loadFont("file:src/fonts/Fipps-Regular.otf", 24));
		GridPane header = new GridPane();
		header.getColumnConstraints().add(new ColumnConstraints(15));
		header.add(score, 1, 0);
		header.getColumnConstraints().add(new ColumnConstraints(900));
		header.add(lives, 2, 0);
		header.getColumnConstraints().add(new ColumnConstraints(200));
		header.add(currLevel, 3, 0);

		double[] reloadTimer = {System.nanoTime()};

		//level 1
		Level lvl1 = new Level();
		lvl1.g.getChildren().add(header);

		Scene level1 = new Scene(lvl1.g, SCREEN_WIDTH, SCREEN_HEIGHT, Color.WHITE);

		//level 2
		Level lvl2 = new Level();

		Scene level2 = new Scene(lvl2.g, SCREEN_WIDTH, SCREEN_HEIGHT, Color.GRAY);


		//level 3
		Level lvl3 = new Level();

		Scene level3 = new Scene(lvl3.g, SCREEN_WIDTH, SCREEN_HEIGHT, Color.DARKBLUE);

		//win screen
		Text youWin = new Text("You won!");
		youWin.setFont(Font.loadFont("file:src/fonts/Fipps-Regular.otf", 72));
		Text yourScore = new Text();
		yourScore.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 36));
		Text quitPrompt = new Text("Q: Quit Game");
		quitPrompt.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		Text restartPrompt = new Text("ENTER: Restart Game");
		restartPrompt.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));

		VBox winLayout = new VBox();
		winLayout.setAlignment(Pos.CENTER);
		winLayout.setPadding(new Insets(200, 0, 0, 0));
		winLayout.getChildren().addAll(youWin, yourScore, quitPrompt, restartPrompt);
		winLayout.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

		Scene winScreen = new Scene(winLayout, SCREEN_WIDTH, SCREEN_HEIGHT);

		//game over screen
		Text youLost = new Text("Game Over");
		youLost.setFont(Font.loadFont("file:src/fonts/Fipps-Regular.otf", 72));
		Text yourScore2 = new Text();
		yourScore2.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 36));
		Text quitPrompt2 = new Text("Q: Quit Game");
		quitPrompt2.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		Text restartPrompt2 = new Text("ENTER: Restart Game");
		restartPrompt2.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		VBox lossLayout = new VBox();
		lossLayout.setAlignment(Pos.CENTER);
		lossLayout.setPadding(new Insets(200, 0, 0, 0));
		lossLayout.getChildren().addAll(youLost, yourScore2, quitPrompt2, restartPrompt2);
		lossLayout.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

		Scene gameOver = new Scene(lossLayout, SCREEN_WIDTH, SCREEN_HEIGHT);

		//animation loop
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(stage.getScene() == level1) {
					move(lvl1, score, lives, currLevel);
				} else if(stage.getScene() == level2) {
					move(lvl2, score, lives, currLevel);
				} else if(stage.getScene() == level3) {
					move(lvl3, score, lives, currLevel);
				}

				if(numLives == 0) {
					lvl1.clear();
					lvl2.clear();
					lvl3.clear();

					yourScore2.setText("Your Score: " + numScore);

					numLives = 3;
					numLevel = 1;
					numScore = 0;

					stage.setScene(gameOver);
				}

				if(stage.getScene() == level1 && lvl1.checkAlienLanding() ||
						stage.getScene() == level2 && lvl2.checkAlienLanding() ||
						stage.getScene() == level3 && lvl3.checkAlienLanding()) {

					lvl1.clear();
					lvl2.clear();
					lvl3.clear();

					yourScore2.setText("Your Score: " + numScore);
					playerDeath.play();

					numLives = 3;
					numLevel = 1;
					numScore = 0;

					stage.setScene(gameOver);
				}

				if(stage.getScene() == level1 && lvl1.aliens.isEmpty()) {
					numLevel++;
					lvl2.restart();
					lvl2.g.getChildren().add(header);
					currLevel.setText("Level: " + numLevel);
					ENEMY_SPEED = 0.5;
					stage.setScene(level2);
				} else if(stage.getScene() == level2 && lvl2.aliens.isEmpty()) {
					numLevel++;
					lvl3.restart();
					lvl3.g.getChildren().add(header);
					currLevel.setText("Level: " + numLevel);
					ENEMY_SPEED = 0.5;
					stage.setScene(level3);
				} else if(stage.getScene() == level3 && lvl3.aliens.isEmpty()) {
					lvl1.clear();
					lvl2.clear();
					lvl3.clear();

					yourScore.setText("Your Score: " + numScore);
					ENEMY_SPEED = 0.5;

					numLives = 3;
					numLevel = 1;
					numScore = 0;

					stage.setScene(winScreen);
				}
			}
		};
		timer.start();

		//event handlers
		startScreen.setOnKeyPressed(keyEvent ->
			{
				if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.NUMPAD1) {
					stage.setScene(level1);
					menuBG.stop();
					battleBG.setVolume(0.5f);
					battleBG.setCycleCount(AudioClip.INDEFINITE);
					battleBG.play();
				} else if(keyEvent.getCode() == KeyCode.Q) {
					System.exit(0);
				} else if(keyEvent.getCode() == KeyCode.NUMPAD2) {
					lvl2.g.getChildren().add(header);
					numLevel = 2;
					currLevel.setText("Level: " + numLevel);
					stage.setScene(level2);
					menuBG.stop();
					battleBG.setVolume(0.5f);
					battleBG.setCycleCount(AudioClip.INDEFINITE);
					battleBG.play();
				} else if(keyEvent.getCode() == KeyCode.NUMPAD3) {
					lvl3.g.getChildren().add(header);
					numLevel = 3;
					currLevel.setText("Level: " + numLevel);
					stage.setScene(level3);
					menuBG.stop();
					battleBG.setVolume(0.5f);
					battleBG.setCycleCount(AudioClip.INDEFINITE);
					battleBG.play();
				}
			});

		level1.setOnKeyPressed(keyEvent ->
		{
			if(keyEvent.getCode() == KeyCode.A) {
				if(lvl1.p.x >= 0.0f) {
					lvl1.p.x -= PLAYER_SPEED;
				}
			} else if(keyEvent.getCode() == KeyCode.D) {
				if(lvl1.p.x <= SCREEN_WIDTH - lvl1.p.sprite.getFitWidth()) {
					lvl1.p.x += PLAYER_SPEED;
				}
			} else if(keyEvent.getCode() == KeyCode.SPACE) {
				if(System.nanoTime() - reloadTimer[0] >= RELOAD_TIME_ns) {
					reloadTimer[0] = System.nanoTime();
					Bullet b = new Bullet(lvl1.p.x + lvl1.p.sprite.getFitWidth() / 2, lvl1.p.y);
					lvl1.playerBullets.add(b);
					lvl1.g.getChildren().add(b.sprite);
					shootSound.play();
				}
			} else if(keyEvent.getCode() == KeyCode.Q) {
				System.exit(0);
			}
		});

		level2.setOnKeyPressed(keyEvent ->
		{
			if(keyEvent.getCode() == KeyCode.A) {
				if(lvl2.p.x >= (0.0f + lvl2.p.sprite.getFitWidth() / 2)) {
					lvl2.p.x -= PLAYER_SPEED;
				}
			} else if(keyEvent.getCode() == KeyCode.D) {
				if(lvl2.p.x <= (SCREEN_WIDTH - lvl2.p.sprite.getFitWidth() / 2)) {
					lvl2.p.x += PLAYER_SPEED;
				}
			} else if(keyEvent.getCode() == KeyCode.SPACE) {
				if(System.nanoTime() - reloadTimer[0] >= RELOAD_TIME_ns) {
					reloadTimer[0] = System.nanoTime();
					Bullet b = new Bullet(lvl2.p.x + lvl2.p.sprite.getFitWidth() / 2, lvl2.p.y);
					lvl2.playerBullets.add(b);
					lvl2.g.getChildren().add(b.sprite);
					shootSound.play();
				}
			} else if(keyEvent.getCode() == KeyCode.Q) {
				System.exit(0);
			}
		});

		level3.setOnKeyPressed(keyEvent ->
		{
			if(keyEvent.getCode() == KeyCode.A) {
				if(lvl3.p.x >= (0.0f + lvl3.p.sprite.getFitWidth() / 2)) {
					lvl3.p.x -= PLAYER_SPEED;
				}
			} else if(keyEvent.getCode() == KeyCode.D) {
				if(lvl3.p.x <= (SCREEN_WIDTH - lvl3.p.sprite.getFitWidth() / 2)) {
					lvl3.p.x += PLAYER_SPEED;
				}
			} else if(keyEvent.getCode() == KeyCode.SPACE) {
				if(System.nanoTime() - reloadTimer[0] >= RELOAD_TIME_ns) {
					reloadTimer[0] = System.nanoTime();
					Bullet b = new Bullet(lvl3.p.x + lvl3.p.sprite.getFitWidth() / 2, lvl3.p.y);
					lvl3.playerBullets.add(b);
					lvl3.g.getChildren().add(b.sprite);
					shootSound.play();
				}
			} else if(keyEvent.getCode() == KeyCode.Q) {
				System.exit(0);
			}
		});

		winScreen.setOnKeyPressed(keyEvent ->
		{
			if(keyEvent.getCode() == KeyCode.ENTER) {
				lvl1.restart();
				score.setText("Score: " + numScore);
				lives.setText("Lives: " + numLives);
				currLevel.setText("Level: " + numLevel);
				lvl1.g.getChildren().add(header);
				ENEMY_SPEED = 0.5;
				stage.setScene(level1);
			} else if(keyEvent.getCode() == KeyCode.Q) {
				System.exit(0);
			}
		});

		gameOver.setOnKeyPressed(keyEvent ->
		{
			if(keyEvent.getCode() == KeyCode.ENTER) {
				lvl1.restart();
				score.setText("Score: " + numScore);
				lives.setText("Lives: " + numLives);
				currLevel.setText("Level: " + numLevel);
				lvl1.g.getChildren().add(header);
				ENEMY_SPEED = 0.5;
				stage.setScene(level1);
			} else if(keyEvent.getCode() == KeyCode.Q) {
				System.exit(0);
			}
		});

		// show starting scene
		stage.setScene(startScreen);
		stage.show();
	}

	Scene initializeStart() {
		Label spaceInvaders = new Label("Space Invaders");
		spaceInvaders.setFont(Font.loadFont("file:src/fonts/Fipps-Regular.otf", 72));
		Label instructions = new Label("\nInstructions");
		instructions.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 36));
		Label startGame = new Label("ENTER: Start Game");
		startGame.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		Label move = new Label("A: Move Ship Left       D: Move Ship Right");
		move.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		Label fire = new Label("SPACE: Fire Weapon");
		fire.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		Label quit = new Label("Q: Quit Game");
		quit.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		Label level = new Label("1, 2, 3: Start the Game at a Specific Level");
		level.setFont(Font.loadFont("file:src/fonts/retrogaming.ttf", 18));
		Label credits = new Label("\n\n\n\nJames Zhong, 20750528");

		VBox layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(200, 0, 0, 0));
		layout.getChildren().addAll(spaceInvaders, instructions, startGame, move, fire, quit, level, credits);
		layout.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

		menuBG.setVolume(0.5f);
		menuBG.setCycleCount(AudioClip.INDEFINITE);
		menuBG.play();

		return new Scene(layout, SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	void move(Level lvl, Text score, Text lives, Text currLevel) {
		movePlayer(lvl.p);
		alienFire(lvl.aliens, lvl.alienBullets, lvl.g);
		moveBullets(lvl.playerBullets, lvl.g, PLAYER_BULLET_SPEED);
		moveBullets(lvl.alienBullets, lvl.g, ENEMY1_BULLET_SPEED);
		moveAliens(lvl.aliens, lvl.alienBullets, lvl.g);
		checkEnemyCollisions(lvl.aliens, lvl.playerBullets, lvl.g, score);
		checkPlayerCollisions(lvl.p, lvl.alienBullets, lvl.g, lives);
	}

	void movePlayer(Player p) {
		p.sprite.setX(p.x);
		p.sprite.setY(p.y);
	}

	void moveAliens(ArrayList<Alien> aliens, ArrayList<Bullet> bullets, Group g) {
		for(Alien a : aliens) {
			if (a.movingRight) {
				if(numLevel == 1) a.x += ENEMY_SPEED;
				else if(numLevel == 2) a.x += ENEMY_SPEED * 1.5;
				else if(numLevel == 3) a.x += ENEMY_SPEED * 2;
			} else {
				if(numLevel == 1) a.x -= ENEMY_SPEED;
				else if(numLevel == 2) a.x -= ENEMY_SPEED * 1.5;
				else if(numLevel == 3) a.x -= ENEMY_SPEED * 2;
			}
		}

		for(Alien a : aliens) {
			if (a.x > SCREEN_WIDTH - a.sprite.getFitWidth()) {
				for (Alien a1 : aliens) {
					a1.y += ENEMY_VERTICAL_SPEED;
					a1.movingRight = false;
				}
				Random rand = new Random();
				Alien fireAlien = aliens.get(rand.nextInt(aliens.size()));
				Bullet b = new Bullet(fireAlien.x + fireAlien.sprite.getFitWidth() / 2, fireAlien.y + fireAlien.sprite.getFitHeight());
				bullets.add(b);
				g.getChildren().add(b.sprite);
				break;
			} else if (a.x < 0.0f) {
				for (Alien a1 : aliens) {
					a1.y += ENEMY_VERTICAL_SPEED;
					a1.movingRight = true;
				}
				Random rand = new Random();
				Alien fireAlien = aliens.get(rand.nextInt(aliens.size()));
				Bullet b = new Bullet(fireAlien.x + fireAlien.sprite.getFitWidth() / 2, fireAlien.y + fireAlien.sprite.getFitHeight());
				bullets.add(b);
				g.getChildren().add(b.sprite);
				break;
			}
		}

		for(Alien a : aliens) {
			a.sprite.setX(a.x);
			a.sprite.setY(a.y);
		}
	}

	void moveBullets(ArrayList<Bullet> bullets, Group g, double speed) {
		for(Bullet b : bullets) {
			if(speed == PLAYER_BULLET_SPEED) {
				b.b_y -= speed;
			} else {
				if(numLevel == 1) b.b_y += ENEMY1_BULLET_SPEED;
				else if(numLevel == 2) b.b_y += ENEMY2_BULLET_SPEED;
				else if(numLevel == 3) b.b_y += ENEMY3_BULLET_SPEED;
			}
			b.sprite.setCenterY(b.b_y);

			if (b.b_y < 0) {
				g.getChildren().remove(b.sprite);
				bullets.remove(b);
			}
		}
	}

	void checkEnemyCollisions(ArrayList<Alien> aliens, ArrayList<Bullet> bullets, Group g, Text score) {
		for(Bullet b : bullets) {
			for(Alien a : aliens) {
				if(b.sprite.intersects(a.sprite.getLayoutBounds())) {
					g.getChildren().remove(a.sprite);
					aliens.remove(a);
					g.getChildren().remove(b.sprite);
					bullets.remove(b);
					numScore += 100;
					score.setText("Score: " + numScore);
					invaderDeath.play();
					if(numLevel == 1) ENEMY_SPEED += 0.02;
					else if(numLevel == 2) ENEMY_SPEED += 0.025;
					else if(numLevel == 3) ENEMY_SPEED += 0.03;
				}
			}
		}
	}

	void checkPlayerCollisions(Player p, ArrayList<Bullet> bullets, Group g, Text lives) {
		for(Bullet b : bullets) {
			if(b.sprite.intersects(p.sprite.getLayoutBounds())) {
				g.getChildren().remove(b.sprite);
				bullets.remove(b);
				numLives -= 1;
				lives.setText("Lives: " + numLives);
				p.x = SCREEN_WIDTH / 2;
				p.y = SCREEN_HEIGHT - 30;
				p.sprite.setX(p.x);
				p.sprite.setY(p.y);
				playerDeath.play();
			}
		}
	}

	void alienFire(ArrayList<Alien> aliens, ArrayList<Bullet> bullets, Group g) {
		for(Alien a : aliens) {
			Random rand = new Random();
			int willFire = rand.nextInt(1800);
			if(willFire == 0) {
				Bullet b = new Bullet(a.x + a.sprite.getFitWidth() / 2, a.y + a.sprite.getFitHeight());
				bullets.add(b);
				g.getChildren().add(b.sprite);
			}
		}
	}
}