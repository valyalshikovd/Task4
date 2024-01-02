package com.cgvsu.lines;

import com.cgvsu.render_engine.Zbuffer;
import javafx.scene.image.PixelWriter;

import javafx.scene.paint.Color;



public class Bresenham {
    private int x1, x2, y1, y2; // координаты начала и конца
    private Color sC, eC;


    public Bresenham(int x1, int x2, int y1, int y2, Color sC, Color eC) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.sC = sC;
        this.eC = eC;
    }

    public void draw(PixelWriter pw, double poligonDepth, int lineWidth) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int ddx = sign(x2 - x1);
        int ddy = sign(y2 - y1);
        int lala = x2 - x1;
        int qqq = y2 - y1;

        int x = x1; // текущие координаты
        int y = y1;

        if (dx > dy) {
            int e = 2 * dx - dy;

            while (x != x2) {
                x += ddx;
                e -= 2 * dy;
                if (e < 0) {
                    y += ddy;
                    e += 2 * dx;
                }

                float k = (float) (x - x1) / lala;
                Color currentColor = interpolateColor(sC, eC, k);

                // Draw pixels in a range to simulate line width
                for (int i = -lineWidth / 2; i <= lineWidth / 2; i++) {
                    for (int j = -lineWidth / 2; j <= lineWidth / 2; j++) {
                        try {
                            if (Zbuffer.bufferCheck(x + i, y + j, poligonDepth)) {
                                pw.setColor(x + i, y + j, currentColor);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        } else {
            int e = 2 * dy - dx;

            while (y != y2) {
                y += ddy;
                e -= 2 * dx;
                if (e < 0) {
                    x += ddx;
                    e += 2 * dy;
                }

                float k = (float) (y - y1) / qqq;
                Color currentColor = interpolateColor(sC, eC, k);

                // Draw pixels in a range to simulate line width
                for (int i = -lineWidth / 2; i <= lineWidth / 2; i++) {
                    for (int j = -lineWidth / 2; j <= lineWidth / 2; j++) {
                        try {
                            if (Zbuffer.bufferCheck(x + i, y + j, poligonDepth)) {
                                pw.setColor(x + i, y + j, currentColor);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }

    private int sign(int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
    }

    private Color interpolateColor(Color s, Color e, float k) {
        double red = (s.getRed() + (e.getRed() - s.getRed()) * k);
        double green = (s.getGreen() + (e.getGreen() - s.getGreen()) * k);
        double blue = (s.getBlue() + (e.getBlue() - s.getBlue()) * k);;
        return new Color(red, green, blue, 1);
    }
}