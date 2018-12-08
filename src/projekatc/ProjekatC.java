package projekatc;

import java.io.File;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import static projekatc.Krugovi.randomInRange;

/**
 *
 * @author Oskar
 */
public class ProjekatC extends Application {
	private Krugovi meta1, meta2, meta3;
	public BorderPane root;
	Button podaci = new Button();
	int attempts, score;
	boolean canTheGameStart = true;
	boolean isPlayedMoreThatOnce = false;
	long startingtime;
	long endingtime;

	@Override
	public void start(Stage primaryStage) {

		String musicFile = "hit.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());

		root = new BorderPane();
		VBox topContainer = new VBox();
		ToolBar toolBar = new ToolBar();
		topContainer.getChildren().add(toolBar);

		Button startButt = new Button();
		Button exitButt = new Button();
		Button scoreButt = new Button();
		Label labela = new Label("Oskar Vatic 527/13" + "\n" + "etf.rs");
		labela.setTextFill(Color.TEAL);
		labela.setTranslateX(450);
		attempts = 10;
		score = 0;
		podaci.setText(" attempts: " + Integer.toString(attempts) + "\n score: " + Integer.toString(score));

		scoreButt.setText("High scores: \n 1: \n 2: \n 3: \n 4: \n 5: \n 6: ");

		startButt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (canTheGameStart)
					pokreni();
			}
		});

		exitButt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(1);
			}
		});

		startButt.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/start.png"))));
		exitButt.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/exit.png"))));
//        scoreButt.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/score.png"))));        
		toolBar.getItems().addAll(startButt, exitButt, podaci, labela);

		ImageView pozadina = new ImageView(new Image(getClass().getResourceAsStream("/pozadina.png")));

		root.getChildren().addAll(pozadina);
		root.setTop(topContainer);
		Scene scene = new Scene(root, 1000, 600);

		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				metak(mouseEvent.getX(), mouseEvent.getY()); // getSource() za objekat
				MediaPlayer mediaPlayer = new MediaPlayer(sound);
				mediaPlayer.play();
			}
		});

		primaryStage.setTitle("Mete2D");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public void pokreni() {

		if (isPlayedMoreThatOnce) {
			root.getChildren().remove(meta1);
			root.getChildren().remove(meta2);
			root.getChildren().remove(meta3);

			attempts = 10;
			score = 0;
			podaci.setText(" attempts: " + attempts + "\n score: " + score);
		}

		startingtime = System.currentTimeMillis();
		canTheGameStart = false;
		isPlayedMoreThatOnce = true;
		meta1 = new Krugovi(1);
		meta2 = new Krugovi(1.5);
		meta3 = new Krugovi(2);

		root.getChildren().addAll(meta1, meta2, meta3);

	}

	public static boolean isBetween(double x, double lower, double upper) {
		return lower <= x && x <= upper;
	}

	public void metak(double d1, double d2) {

		Bounds boundsInScene1 = meta1.localToScene(meta1.getBoundsInLocal());
		Bounds boundsInScene2 = meta2.localToScene(meta2.getBoundsInLocal());
		Bounds boundsInScene3 = meta3.localToScene(meta3.getBoundsInLocal());

		// STRELA begin
		Polyline p1 = new Polyline(10, 5, 8, 3.5, 10, 2, 8, 3.5, 3, 3.5, 3, 5, 0, 3.5, 3, 2, 3, 3.5);
		Scale scale1 = new Scale(3, 3);
		Rotate rotate1 = new Rotate(180);
		p1.getTransforms().addAll(scale1, rotate1);

		// putanja strele
		Polyline putanja = new Polyline(950, 580, d1, d2);
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(javafx.util.Duration.seconds(0.1));
		pathTransition.setInterpolator(Interpolator.LINEAR);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setPath(putanja);
		pathTransition.setNode(p1);
		pathTransition.setCycleCount(1);

		// nestajanje strele
		FadeTransition fade = new FadeTransition(Duration.seconds(1), p1);
		fade.setFromValue(1);
		fade.setToValue(0);
		// STRELA end

		if (isBetween(d2, boundsInScene1.getMinY(), boundsInScene1.getMaxY())
				&& isBetween(d1, boundsInScene1.getMinX(), boundsInScene1.getMaxX())) {
			pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					root.getChildren().remove(meta1);
					brojPoena(d2, boundsInScene1.getMinY(), boundsInScene1.getMaxY(), d1, boundsInScene1.getMinX(),
							boundsInScene1.getMaxX(), meta1);
					meta1 = new Krugovi(1);
					root.getChildren().add(meta1);
					fade.play();
				}
			});
		} else if (isBetween(d2, boundsInScene2.getMinY(), boundsInScene2.getMaxY())
				&& isBetween(d1, boundsInScene2.getMinX(), boundsInScene2.getMaxX())) {
			pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					root.getChildren().remove(meta2);
					brojPoena(d2, boundsInScene2.getMinY(), boundsInScene2.getMaxY(), d1, boundsInScene2.getMinX(),
							boundsInScene2.getMaxX(), meta2);
					meta2 = new Krugovi(1.5);
					root.getChildren().add(meta2);
					fade.play();
				}
			});
		} else if (isBetween(d2, boundsInScene3.getMinY(), boundsInScene3.getMaxY())
				&& isBetween(d1, boundsInScene3.getMinX(), boundsInScene3.getMaxX())) {
			pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					root.getChildren().remove(meta3);
					brojPoena(d2, boundsInScene3.getMinY(), boundsInScene3.getMaxY(), d1, boundsInScene3.getMinX(),
							boundsInScene3.getMaxX(), meta3);
					meta3 = new Krugovi(2);
					root.getChildren().add(meta3);
					fade.play();
				}
			});
		} else {
			fade.play();
		}

		pathTransition.play();

		root.getChildren().add(p1);

		// SCORE AND ATTEMPTS begin
		attempts--;
		podaci.setText(" attempts: " + String.valueOf(attempts) + "\n score: " + String.valueOf(score));
		if (attempts == 0) {
			endingtime = System.currentTimeMillis() - startingtime;
			showStage();
		}
		// SCORE AND ATTEMPTS end

	}

	public void brojPoena(double yy, double yymin, double yymax, double xx, double xxmin, double xxmax, Krugovi meta) {
		double centary = yymin + (yymax - yymin) / 2;
		double centarx = xxmin + (xxmax - xxmin) / 2;
		double udaljenost = Math.sqrt(Math.pow(centarx - xx, 2) + Math.pow(centary - yy, 2));

		Text text = new Text();
		double poeni;

		if (isBetween(udaljenost, 0, 8)) {
			score += 100 + meta.bonus(meta.scaleIndex);
			poeni = 100 + meta.bonus(meta.scaleIndex);
			text.setText(String.valueOf(poeni));
		} else if (isBetween(udaljenost, 8.01, 20)) {
			score += 60 + meta.bonus(meta.scaleIndex);
			poeni = 60 + meta.bonus(meta.scaleIndex);
			text.setText(String.valueOf(poeni));
		} else if (isBetween(udaljenost, 20.01, 35)) {
			score += 30 + meta.bonus(meta.scaleIndex);
			poeni = 30 + meta.bonus(meta.scaleIndex);
			text.setText(String.valueOf(poeni));
		} else if (isBetween(udaljenost, 35.01, 50)) {
			score += 10 + meta.bonus(meta.scaleIndex);
			poeni = 10 + meta.bonus(meta.scaleIndex);
			text.setText(String.valueOf(poeni));
		} else {
			poeni = 0;
			text.setText(String.valueOf(poeni));
		}

		text.setX(xx);
		text.setY(yy);
		root.getChildren().add(text);
		FadeTransition fadePoeni = new FadeTransition(Duration.seconds(1), text);
		fadePoeni.setFromValue(1);
		fadePoeni.setToValue(0);
		fadePoeni.play();

	};

	public void showStage() {

		Stage newStage = new Stage();
		Group comp = new Group();
		Rectangle reck = new Rectangle(0, 0, 300, 300);
		reck.setFill(Color.TEAL);
		Text nameField = new Text(
				"GAME OVER!" + "\n" + "Score: " + score + "\n" + "Time: " + (double) endingtime / 1000 + "s");
		nameField.setFont(Font.font("Verdana", 20));
		nameField.setFill(Color.WHITE);
		nameField.setTranslateX(80);
		nameField.setTranslateY(120);
		comp.getChildren().addAll(reck, nameField);
		Scene stageScene = new Scene(comp, 300, 300);
		newStage.setScene(stageScene);
		newStage.setTitle("Mete2D");
		newStage.show();
		attempts = 10;
		score = 0;
		podaci.setText(" attempts: " + attempts + "\n score: " + score);

		startingtime = System.currentTimeMillis();
		canTheGameStart = true;
	}

}
