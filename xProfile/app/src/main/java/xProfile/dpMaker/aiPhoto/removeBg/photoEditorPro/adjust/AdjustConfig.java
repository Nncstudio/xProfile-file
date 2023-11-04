package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adjust;

public class AdjustConfig {
    float intensity, sliderIntensity;
    float previousIntensity, previousSlider;
    float originIntensity, originSlider;

    float minValue, maxValue;
    float sliderMin, sliderMax;

    public float getSliderIntensity() { return sliderIntensity; }
    public float getIntensity() { return intensity; }
    public float getOriginSlider() { return originSlider; }
    public float getMinSlider() { return sliderMin; }
    public float getMaxSlider() { return sliderMax; }

    public AdjustConfig(float _minValue, float _originValue, float _maxValue, float _sliderStartPos, float _sliderMin, float _sliderMax) {
        minValue = _minValue;
        maxValue = _maxValue;
        originIntensity = _originValue;
        previousIntensity = _originValue;
        intensity = _originValue;
        originSlider = _sliderStartPos;
        previousSlider = _sliderStartPos;
        sliderIntensity = _sliderStartPos;
        sliderMin = _sliderMin;
        sliderMax = _sliderMax;
    }

    public float calcIntensity(float _intensity) {
        float result;
        if (_intensity <= sliderMin + 0.08)        return minValue;
        else if (_intensity >= sliderMax - 0.08)   return maxValue;
        else {
            if (_intensity <= originSlider) {
                result = minValue + (_intensity - sliderMin) / (originSlider - sliderMin) * (originIntensity - minValue);
            } else {
                result = originIntensity + (_intensity - originSlider) / (sliderMax - originSlider) * (maxValue - originIntensity);
            }
            return result;
        }
    }

    public float setAndReturnIntensity(float _intensity) {
        sliderIntensity = _intensity;
        intensity = calcIntensity(_intensity);
        return intensity;
    }

    public void undoIntensity() {
        sliderIntensity = previousSlider;
        intensity = previousIntensity;
    }

    public void rewritePreviousIntensity() {
        previousSlider = sliderIntensity;
        previousIntensity = intensity;
    }

}
