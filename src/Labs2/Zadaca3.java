import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}

class ObjectCanNotBeMovedException extends Exception {
    ObjectCanNotBeMovedException(String message) {
        super(message);
    }
}

class MovableObjectNotFittableException extends Exception {
    MovableObjectNotFittableException(String message) {
        super(message);
    }
}

class MovingPoint implements IMovable {

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    MovingPoint (int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int addedYValue = this.y + ySpeed;

        if (addedYValue > MovablesCollection.MAX_Y_VALUE) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", this.x, addedYValue));
        }

        this.y = addedYValue;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int addedXValue = this.x - xSpeed;

        if (addedXValue < 0) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", addedXValue, this.y));
        }

        this.x = addedXValue;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int addedXValue = this.x + xSpeed;

        if (addedXValue > MovablesCollection.MAX_X_VALUE) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", addedXValue, this.y));
        }

        this.x = addedXValue;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int addedYValue = this.y - ySpeed;

        if (addedYValue < 0) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", this.x, addedYValue));
        }

        this.y = addedYValue;
    }

    @Override
    public int getCurrentXPosition() {
        return this.x;
    }

    @Override
    public int getCurrentYPosition() {
        return this.y;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates %s", this.returnCoords());
    }

    public String returnCoords() {
        return String.format("(%d,%d)", this.x, this.y);
    }
}

class MovingCircle implements IMovable {
    private int radius;
    private MovingPoint center;


    MovingCircle(int radius, MovingPoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (this.center.getCurrentYPosition() + this.radius > MovablesCollection.MAX_Y_VALUE) {
            throw new ObjectCanNotBeMovedException(
                    String.format("Point (%d,%d) is out of bounds", this.center.getCurrentXPosition()));
        }
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {

    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {

    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {

    }

    @Override
    public int getCurrentXPosition() {
        return 0;
    }

    @Override
    public int getCurrentYPosition() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates %s and radius %d", this.center.returnCoords(), this.radius);
    }
}

class MovablesCollection {
    public static int MAX_X_VALUE;
    public static int MAX_Y_VALUE;
    private List<IMovable> movables;

    MovablesCollection(int x_MAX, int y_MAX) {
        MovablesCollection.MAX_X_VALUE = x_MAX;
        MovablesCollection.MAX_Y_VALUE = y_MAX;
    }

    public static void setxMax(int value) {
        MovablesCollection.MAX_X_VALUE = value;
    }

    public static void setyMax(int value) {
        MovablesCollection.MAX_Y_VALUE = value;
    }

    public void addMovableObject(IMovable m) throws MovableObjectNotFittableException {
        if (m.getCurrentXPosition() > MovablesCollection.MAX_X_VALUE
                || m.getCurrentYPosition() > MovablesCollection.MAX_Y_VALUE) {
            throw new MovableObjectNotFittableException(m.toString() + "can not be fitted into the collection");
        }

        this.movables.add(m);
    }

    public void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for (IMovable movable: this.movables) {
            switch (type) {
                case CIRCLE:
                    if (movable instanceof MovingCircle) this.moveToDirection(direction, movable);
                case POINT:
                    if (movable instanceof MovingPoint) this.moveToDirection(direction, movable);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Collection of movable objects with size %d", this.movables.size());
    }

    private void moveToDirection(DIRECTION direction, IMovable movable) throws ObjectCanNotBeMovedException {
        switch (direction) {
            case UP:
                movable.moveUp();
            case DOWN:
                movable.moveDown();
            case LEFT:
                movable.moveLeft();
            case RIGHT:
                movable.moveRight();
        }
    }
}

interface IMovable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

    String toString();
}