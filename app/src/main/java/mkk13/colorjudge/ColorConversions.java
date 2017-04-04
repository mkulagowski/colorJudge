package mkk13.colorjudge;

import java.util.ArrayList;
import java.util.Collections;

public class ColorConversions {

    // HEX
    public static int[] hex2rgb(String colorStr) {
        if (colorStr.length() == 7) {
            return new int[]{
                    Integer.valueOf(colorStr.substring(1, 3), 16),
                    Integer.valueOf(colorStr.substring(3, 5), 16),
                    Integer.valueOf(colorStr.substring(5, 7), 16)};
        } else {
            return new int[]{
                    Integer.valueOf(colorStr.substring(2, 4), 16),
                    Integer.valueOf(colorStr.substring(4, 6), 16),
                    Integer.valueOf(colorStr.substring(6, 8), 16)};
        }
    }

    public static int hex2int(String colorStr) {
        return android.graphics.Color.parseColor(colorStr);
    }

    public static String int2hex(int color) {
        return "#" + Integer.toHexString(0x10000000 | color).substring(2);
    }

    // XYZ
    private static double XYZprep(int rgbCol) {
        double col = rgbCol / 255D;
        if (col > 0.04045) {
            col = Math.pow(((col + 0.055) / 1.055), 2.4);
        } else {
            col /= 12.92;
        }
        return col * 100;
    }

    public static float[] rgb2xyz(int[] rgb) {
        return rgb2xyz(rgb[0], rgb[1], rgb[2]);
    }

    public static float[] rgb2xyz(int R, int G, int B) {
        double var_R = XYZprep(R);
        double var_G = XYZprep(G);
        double var_B = XYZprep(B);

        //Observer. = 2°, Illuminant = D65
        float x = (float)(var_R * 0.4124 + var_G * 0.3576 + var_B * 0.1805);
        float y = (float)(var_R * 0.2126 + var_G * 0.7152 + var_B * 0.0722);
        float z = (float)(var_R * 0.0193 + var_G * 0.1192 + var_B * 0.9505);
        return new float[]{x, y, z};
    }

    // CIE Lab
    public static float[] rgb2lab(int[] rgb) {
        return rgb2lab(rgb[0], rgb[1], rgb[2]);
    }

    public static float[] rgb2lab(int R, int G, int B) {
        float[] xyz = rgb2xyz(R, G, B);
        return xyz2lab(xyz[0], xyz[1], xyz[2]);
    }

    private static double LABprep(double xyzCol) {
        if (xyzCol > 0.008856) {
            xyzCol = Math.pow(xyzCol, 1/3D);
        } else {
            xyzCol = (7.787 * xyzCol) + (16 / 116D);
        }
        return xyzCol;
    }

    private static final double ref_X =  95.047;
    private static final double ref_Y = 100.000;
    private static final double ref_Z = 108.883;

    public static float[] xyz2lab(float[] xyz) {
        return xyz2lab(xyz[0], xyz[1], xyz[2]);
    }

    public static float[] xyz2lab(float x, float y, float z) {
        //Observer= 2°, Illuminant= D65
        double var_X = x / ref_X;
        double var_Y = y / ref_Y;
        double var_Z = z / ref_Z;

        var_X = LABprep(var_X);
        var_Y = LABprep(var_Y);
        var_Z = LABprep(var_Z);

        float l = (float)(116 * var_Y) - 16;
        float a = (float)(500 * (var_X - var_Y));
        float b = (float)(200 * (var_Y - var_Z));

        return new float[]{l, a, b};
    }

    // HSV

    public static float[] rgb2hsv(int[] rgb) {
        return rgb2hsv(rgb[0], rgb[1], rgb[2]);
    }

    public static float[] rgb2hsv(int R, int G, int B) {
        float H=0,S=0,V=0;
        float var_R = (R / 255);
        float var_G = (G / 255);
        float var_B = (B / 255);

        ArrayList<Float> vars = new ArrayList<Float>();
        vars.add(var_R);
        vars.add(var_G);
        vars.add(var_B);

        float var_Min = Collections.min(vars);    //Min. value of RGB
        float var_Max = Collections.max(vars);    //Max. value of RGB
        float del_Max = var_Max - var_Min;           //Delta RGB value

        V = var_Max;

        if (del_Max == 0)                     //This is a gray, no chroma...
        {
            H = 0;                                //HSV results from 0 to 1
            S = 0;
        }
        else                                    //Chromatic data...
        {
            S = del_Max / var_Max;
            float del_R = (((var_Max - var_R) / 6 ) + ( del_Max / 2 ) ) / del_Max;
            float del_G = (((var_Max - var_G) / 6 ) + ( del_Max / 2 ) ) / del_Max;
            float del_B = (((var_Max - var_B) / 6 ) + ( del_Max / 2 ) ) / del_Max;

            if(var_R == var_Max) {
                H = del_B - del_G;
            } else if (var_G == var_Max) {
                H = (((float)1) / 3) + del_R - del_B;
            } else if ( var_B == var_Max ) {
                H = (((float)2) / 3) + del_G - del_R;
            }

            if (H < 0) {
                H++;
            } else if (H > 1) {
                H--;
            }
        }

        return new float[]{H, S, V};
    }
}