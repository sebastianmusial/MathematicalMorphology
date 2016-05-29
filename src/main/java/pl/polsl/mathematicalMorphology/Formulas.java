package pl.polsl.mathematicalMorphology;

public final class Formulas {
    private final float hRef;

    public Formulas(float hRef) {
        this.hRef = hRef;
    }

    /**
     * Formula (11)
     */
    public int comparePixels(RGBHSIPixel pixel1, RGBHSIPixel pixel2) {
        int rgb1 = pixel1.getRgb();
        int rgb2 = pixel2.getRgb();

        float[] hsi1 = pixel1.getHsi();
        float[] hsi2 = pixel2.getHsi();

        Float cost1 = cost(hsi1[0], hsi1[1], hsi1[2]);
        Float cost2 = cost(hsi2[0], hsi2[1], hsi2[2]);

        int result = cost1.compareTo(cost2);

        if(result == 0) {
            result = getR(rgb1) - getR(rgb2);
        }

        if(result == 0) {
            result = getG(rgb1) - getG(rgb2);
        }

        if(result == 0) {
            result = getB(rgb1) - getB(rgb2);
        }

        return result;
    }

    /**
     * Formula (10)
     */
    private float cost(float h, float s, float i) {
        return s * normalizeHue(h) + (1 - s) * i;
    }

    /**
     * Formula (8)
     */
    private float normalizeHue(float hue) {
        return distance(hue) / (float) Math.PI;
    }

    /**
     * Formula (5)
     */
    private float distance(float hue) {
        float dist = Math.abs(hue - hRef);
        return (float) (dist <= Math.PI ? dist : 2f * Math.PI - dist);
    }


    private int getR(int rgb){
        return (rgb >> 16) & 0xFF;
    }

    private int getG(int rgb){
        return (rgb >> 8) & 0xFF;
    }

    private int getB(int rgb){
        return rgb & 0xFF;
    }
}
