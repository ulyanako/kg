import java.util.ArrayList;
import java.util.List;

public class ColorModel implements Subject {
    private int r = 255, g = 0, b = 0;
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }

    public void setRgb(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        notifyObservers();
    }

    public void setCmyk(double c, double m, double y, double k) {
        int[] rgb = ColorConverter.cmykToRgb(c, m, y, k);
        setRgb(rgb[0], rgb[1], rgb[2]);
    }

    public void setHsl(double h, double s, double l) {
        int[] rgb = ColorConverter.hslToRgb(h, s, l);
        setRgb(rgb[0], rgb[1], rgb[2]);
    }
}