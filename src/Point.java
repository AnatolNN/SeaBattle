/**
 * Created by Анадер on 22.05.2018.
 */
public class Point {
    private int coordinateX;//координата точки по вертикали
    private int coordinateY;// координата точки по горизонтали
    private char value;//хранит состояние ячейки

    //конструктор для точки
    Point(int coordinateX, int coordinateY, char value) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.value = value;
    }

    int getCoordinateX() {
        return coordinateX;
    }

    int getCoordinateY() {
        return coordinateY;
    }

    char getValue() {
        return value;
    }

    void setValue(char value) {
        this.value = value;
    }


}
