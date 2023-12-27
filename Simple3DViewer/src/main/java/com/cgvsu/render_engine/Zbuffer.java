package com.cgvsu.render_engine;

public class Zbuffer {

    private double width;
    private double height;
    private double[][] ZBufferMatrix;

    public Zbuffer(double width, double height) {
        this.width = width;
        this.height = height;
        ZBufferMatrix = new double[(int)width][(int)height];
        clearBuffer();
    }

    public void clearBuffer(){
        for(int i = 0; i< width; i++){
            for(int j = 0; j< height; j++){
                this.ZBufferMatrix[i][j] = Double.MAX_VALUE;
            }
        }
    }
    public boolean bufferCheck(int x, int y, double c){
        if(this.ZBufferMatrix[x][y] > c){
            this.ZBufferMatrix[x][y] = c;
            return true;
        }
        return false;
    }
}
