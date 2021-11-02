import java.awt.*;
import java.math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class boid {
    Random random = new Random();
    Color color;
    double x;
    double y;
    double degree;
    int numLines = 15;
    double linesX[] = new double[numLines];
    double linesY[] = new double[numLines];

    public boid (Color color, double x, double y, double degree){
        this.color = color;
        this.x = x;
        this.y = y;
        this.degree = degree;
    }

    public void move(double dist){
        double xOffSet = 0;
        double yOffSet = 0;
        double rad = degree * 0.0174533;
        xOffSet = Math.cos(rad) * dist;
        yOffSet = Math.sin(rad) * dist;
        setY(y+yOffSet);
        setX(x+xOffSet);
    }
    public double alignment(boid[] boids, int myIndex){
        return 0;

    }

    public double cohesion(boid[] boids, Point p, int myIndex){
        boid[] nearBoids = getNearest(boids,90,myIndex);
        return 0;
    }



    public double separation(boid[] boids, cord[] cords, int myIndex) {
        if (boids.length==0){
            return 0;
        }
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        double width = size.width;
        int height = size.height;
        double awareness = 45;
        int fov = 45;
        double sep = fov / numLines;
        int pathsAvailable = 0;
        sightLine[] lines = new sightLine[numLines];
        for (int i = 0; i < numLines; i++) {
            double newDegree = degree + ((i * sep)) - (fov/2);
            sightLine prospect = new sightLine(newDegree, awareness, x, y);
            linesX[i] = prospect.cords[(int) awareness - 1].x;
            linesY[i] = prospect.cords[(int) awareness - 1].y;
            boolean collisionCheck1 = (prospect.cords[(int) awareness -1].y < height && prospect.cords[(int)awareness-1].x < width );
            boolean collisionCheck2 = (prospect.cords[(int) awareness -1].y > 0 && prospect.cords[(int)awareness-1].x > 0 );
            boolean canPass1 = collisionCheck1 && collisionCheck2;
            boolean canPass2 = true;

            for (int j = 0; j < cords.length; j++) {
                boolean collisionCheck3 = (cords[j].x < prospect.cords[(int) awareness - 1].x) && (cords[j].x > this.x);
                boolean collisionCheck4 = (cords[j].y < prospect.cords[(int) awareness - 1].y) && (cords[j].y > this.y);
                boolean collisionCheck5 = (cords[j].x > prospect.cords[(int) awareness - 1].x) && (cords[j].x < this.x);
                boolean collisionCheck6 = (cords[j].y > prospect.cords[(int) awareness - 1].y) && (cords[j].y < this.y);
                if (collisionCheck3 && collisionCheck4) {
                    canPass2 = false;
                }
                if (collisionCheck5 && collisionCheck6) {
                    canPass2 = false;
                }

            }
            if (canPass1 && canPass2) {
                lines[pathsAvailable] = prospect;
                pathsAvailable++;
            }
        }
            if (pathsAvailable == 0) {
                return degree;
            } else if (pathsAvailable == numLines) {
                return degree;
            } else {
                double newDeg = weightedRandomDegree(lines, pathsAvailable);
                return newDeg;
            }


    }
    public boid[] getNearest (boid[] allBoids, double radius, int myIndex){
        boid[] nearBoids;
        int counter = 0;
        for (int i = 0; i < allBoids.length; i++){
            if (isInside(x,y,radius,allBoids[i].x,allBoids[i].y) && i!=myIndex){
                counter++;
            }
        }
        nearBoids = new boid[counter];
        counter = 0;
        for (int i = 0; i < allBoids.length; i++){
            if (isInside(x,y,radius,allBoids[i].x,allBoids[i].y ) && i!=myIndex){
                nearBoids[counter] = allBoids[i];
                System.out.println(allBoids[i]);
                counter++;
            }
        }
        return nearBoids;

    }

    public static boolean isInside(double x, double y, double radius, double checkX, double checkY){
        if ((checkX - x) * (checkX - x) + (checkY - y) * (checkY - y) <= radius * radius)
            return true;
        else
            return false;
    }


    public double weightedRandomDegree(sightLine[] lines, int pathsAvailable){
        double[] diffs = new double [pathsAvailable];
        for (int i = 0; i < pathsAvailable; i++) {
            int diff = Math.abs((int) lines[i].degree - (int) degree);
            diffs[i] = diff;
        }
        Arrays.sort(diffs);
        Random random = new Random();
        double sum = 0;
        for (int i = 0; i <pathsAvailable; i++){
            sum += diffs[i];
        }
        double rand = random.nextDouble() * sum;
        int result = 0;
        for (int i = 0; i < pathsAvailable; i++){
            double reverse = sum - diffs[i];
            if (rand<reverse){
                result = i;
            }
            rand -= diffs[i];
        }
        double returner = diffs[result] + degree;
        return returner;
    }

    public void all3(boid[] boids, Point p, cord[] cords, int myIndex){

        double separation = separation(boids,cords,myIndex);

        setDegree(separation);
    }

    public void setDegree(double degree){
        this.degree=degree;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    public String toString (){
        double rad = degree*0.0174533;
        return "[ "+x+" , "+y+" , "+degree+" ]";
    }
}
