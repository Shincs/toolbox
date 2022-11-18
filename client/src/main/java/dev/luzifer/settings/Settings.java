package dev.luzifer.settings;

public class Settings {
    
    public static final Settings DEFAULT_SETTINGS = new Settings(1, 10, true);
    
    public static volatile Settings settings; // TODO: static abuse
    
    public static Settings of(double opacity, int imageSwitchInterval, boolean switchImages) {
        return new Settings(opacity, imageSwitchInterval, switchImages);
    }
    
    private final double opacity;
    private final int imageSwitchInterval;
    private final boolean switchImages;
    
    private Settings(double opacity, int imageSwitchInterval, boolean switchImages) {
        this.opacity = opacity;
        this.imageSwitchInterval = imageSwitchInterval;
        this.switchImages = switchImages;
    }
    
    public double getOpacity() {
        return opacity;
    }
    
    public int getImageSwitchInterval() {
        return imageSwitchInterval;
    }
    
    public boolean isSwitchImages() {
        return switchImages;
    }
}
