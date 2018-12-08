package projekatc;

import java.time.Duration;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Translate;
import static projekatc.ProjekatC.isBetween;

/**
 *
 * @author Oskar
 */
public class Krugovi extends Group {
    public static Random random = new Random();
    double scaleIndex = randomInRange(0.1, 0.5);
    
    public Krugovi(double x){
        
        Group g1 = new Group ();
        Circle krug1 = new Circle(50);
        krug1.setFill(Color.BLACK);
        Circle krug2 = new Circle(35);
        krug2.setFill(Color.CYAN);
        Circle krug3 = new Circle(20);
        krug3.setFill(Color.RED);
        Circle krug4 = new Circle(8);
        krug4.setFill(Color.YELLOW);
        
        g1.getChildren().addAll(krug1, krug2, krug3, krug4);


        Polyline putanja = new Polyline(60, x*250, 940, x*250, 60, x*250);        
        PathTransition pathTransition = new PathTransition();        
        pathTransition.setDuration(javafx.util.Duration.seconds(randomInRange(2, 5)));
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setPath(putanja);
        pathTransition.setNode(g1);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.play();
        
        ScaleTransition scaleTransition = new ScaleTransition(javafx.util.Duration.seconds(randomInRange(1, 3)), g1);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setByY(scaleIndex);
        scaleTransition.setByX(scaleIndex);
        scaleTransition.play();
        
        
        getChildren().add(g1);
        
    }
    
      public static double randomInRange(double min, double max) {
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }
    
      public int bonus (double index){
          
        if (isBetween(index, 0.1, 0.2)) return 100 ;
        if (isBetween(index, 0.21, 0.3)) return 70;
        if (isBetween(index, 0.31, 0.4)) return 40;
        if (isBetween(index, 0.41, 0.5)) return 10;
        
        return 0;
      }
    
}
