package pl.polsl.mathematicalMorphology;

public class RGBHSIPixel {
    private float[] hsi;
    private int rgb;

    public RGBHSIPixel(float[] hsi, int rgb) {
        this.hsi = hsi;
        this.rgb = rgb;
    }

    public float[] getHsi() {
        return hsi;
    }

    public int getRgb() {
        return rgb;
    }
}
