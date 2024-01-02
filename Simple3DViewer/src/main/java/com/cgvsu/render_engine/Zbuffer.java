package com.cgvsu.render_engine;

public class Zbuffer {

    private static double width;
    private static double height;
    private static double[][] ZBufferMatrix;

    public static void setNewZbuffer(double currWidth, double currHeight) {
        width = currWidth;
        height = currHeight;
        ZBufferMatrix = new double[(int)width][(int)height];
        clearBuffer();
    }

    public static void clearBuffer(){
        if(ZBufferMatrix == null){
            return;
        }
        for(int i = 0; i< width; i++){
            for(int j = 0; j< height; j++){
                ZBufferMatrix[i][j] = Double.MAX_VALUE;
            }
        }
    }
    public static boolean bufferCheck(int x, int y, double c){
        if(ZBufferMatrix == null){
            return false;
        }
        if(ZBufferMatrix[x][y] > c){
            ZBufferMatrix[x][y] = c;
            return true;
        }
        return false;
    }
}
