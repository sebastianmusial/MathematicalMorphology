package pl.polsl.mathematicalMorphology;

public final class Formulas {
    //TODO It should be possible to change Href in GUI
    private static final float H_REF = 0f;

    private Formulas() {
    }

    /**
     * Forumula (11)
     */
    public static int comparePixels(RGBHSIPixel pixel1, RGBHSIPixel pixel2) {
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
     * Forumla (10)
     */
    private static float cost(float h, float s, float i) {
        return s * normalizeHue(h) + (1 - s) * i;
    }

    /**
     * Formula (8)
     */
    private static float normalizeHue(float hue) {
        return distance(hue) / (float) Math.PI;
    }

    /**
     * Formula (5)
     */
    private static float distance(float hue) {
        float dist = Math.abs(hue - H_REF);
        return (float) (dist <= Math.PI ? dist : 2f * Math.PI - dist);
    }


    private static int getR(int rgb){
        return (rgb >> 16) & 0xFF;
    }

    private static int getG(int rgb){
        return (rgb >> 8) & 0xFF;
    }

    private static int getB(int rgb){
        return rgb & 0xFF;
    }
}
