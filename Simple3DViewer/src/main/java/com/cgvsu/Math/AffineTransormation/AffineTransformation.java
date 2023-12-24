package com.cgvsu.Math.AffineTransormation;
import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.Matrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.FourDimensionalVector;
import com.cgvsu.Math.Vectors.NDimensionalVector;

public class AffineTransformation {
    public FourDimensionalMatrix transformationMatrix;

    public AffineTransformation() {
        this.transformationMatrix = new FourDimensionalMatrix(
                new NDimensionalVector(1,1,1,1),
                new NDimensionalVector(1,1,1,1),
                new NDimensionalVector(1,1,1,1),
                new NDimensionalVector(1,1,1,1)
        );
    }

    public Matrix scale(double sX, double sY, double sZ) {
        FourDimensionalMatrix scaleMatrix = new FourDimensionalMatrix(
                new NDimensionalVector(sX,0,0,0),
                new NDimensionalVector(0,sY,0,0),
                new NDimensionalVector(0,0, sZ,0),
                new NDimensionalVector(0,0,0,1)
        );

//        {sX, 0, 0, 0},
//        {0, sY, 0, 0}
//        {0, 0, sZ, 0},
//        {0, 0, 0, 1}
        return scaleMatrix;
    }
    /**
     * Метод для универсального поворота матрицы вокруг осей.
     * @params
     * rX, rY, rZ - значения (в радианах) для поворота по каждой координате
     * **/

    public Matrix rotate(float rX, float rY, float rZ) {
        float cosX = (float) Math.cos(rX);
        float sinX = (float) Math.sin(rX);
        float cosY = (float) Math.cos(rY);
        float sinY = (float) Math.sin(rY);
        float cosZ = (float) Math.cos(rZ);
        float sinZ = (float) Math.sin(rZ);

        FourDimensionalMatrix rotationMatrix = new FourDimensionalMatrix(
                new NDimensionalVector(cosY * cosZ,-cosX * sinZ + sinX * sinY * cosZ ,sinX * sinZ + cosX * sinY * cosZ, 0),
                new NDimensionalVector( cosY * sinZ,cosX * cosZ + sinX * sinY * sinZ,-sinX * cosZ + cosX * sinY * sinZ, 0 ),
                new NDimensionalVector(-sinY, sinX * cosY, cosX * cosY, 0),
                new NDimensionalVector(0, 0,0,1)
        );



//        {cosY * cosZ,         -cosX * sinZ + sinX * sinY * cosZ,    sinX * sinZ + cosX * sinY * cosZ,     0},
//        {cosY * sinZ,         cosX * cosZ + sinX * sinY * sinZ,     -sinX * cosZ + cosX * sinY * sinZ,    0},
//        {-sinY,               sinX * cosY,                          cosX * cosY,                          0},
//        {0,                   0,                                    0,                                    1}
        return rotationMatrix;
    }

    /**
     * Метод для параллельного переноса.
     * @params
     * tX, tY, tz - значения смещения по координатам
     * **/
    public Matrix translate(float tX, float tY, float tZ) {
        FourDimensionalMatrix translationMatrix = new FourDimensionalMatrix(
                new NDimensionalVector(1,0,0,0),
                new NDimensionalVector(0,1,0,0),
                new NDimensionalVector(0,0,1,0),
                new NDimensionalVector(tX, tY, tZ, 1)
        );


//        {1, 0, 0, tX},
//        {0, 1, 0, tY},
//        {0, 0, 1, tZ},
//        {0, 0, 0, 1}

       // transformationMatrix = (NDimensionalMatrix) transformationMatrix.multiplyMatrix(translationMatrix);
        return translationMatrix;
    }

    @Override
    public String toString() {
        return "AffineTransformation{" +
                "transformationMatrix=" + transformationMatrix +
                '}';
    }
}
