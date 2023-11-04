package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adjust;

import android.content.Context;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;

import java.util.ArrayList;
import java.util.Collections;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;

public class HueFilter extends ParentFilter {
    GPUImageHueFilter filter;

    public HueFilter(ArrayList<AdjustConfig> listCfg, Context ctx) {
        filterName = ctx.getString(R.string.hue);
        filter = new GPUImageHueFilter();
        listParameter = new ArrayList<>(Collections.singletonList(ctx.getString(R.string.hue)));
        listConfig = listCfg;
    }

    @Override
    public void setFilterValue(int index, float sliderIntensity) {
        filter.setHue(listConfig.get(index).setAndReturnIntensity(sliderIntensity));
    }

    @Override
    public void undoAllFilterValue() {
        for (AdjustConfig cfg: listConfig) {cfg.undoIntensity();}
        filter.setHue(listConfig.get(0).getIntensity());
    }

    @Override
    public GPUImageFilter getFilter() {
        return filter;
    }
}
