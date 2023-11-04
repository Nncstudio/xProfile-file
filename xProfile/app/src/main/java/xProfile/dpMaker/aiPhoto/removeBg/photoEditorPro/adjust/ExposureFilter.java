package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adjust;

import android.content.Context;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;

import java.util.ArrayList;
import java.util.Collections;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class ExposureFilter extends ParentFilter {
    GPUImageExposureFilter filter;

    public ExposureFilter(ArrayList<AdjustConfig> listCfg, Context ctx) {
        filterName = ctx.getString(R.string.brightness);
        filter = new GPUImageExposureFilter();
        listParameter = new ArrayList<>(Collections.singletonList(ctx.getString(R.string.brightness)));
        listConfig = listCfg;
    }

    @Override
    public void setFilterValue(int index, float sliderIntensity) {
        filter.setExposure(listConfig.get(index).setAndReturnIntensity(sliderIntensity));
    }

    @Override
    public void undoAllFilterValue() {
        for (AdjustConfig cfg: listConfig) {cfg.undoIntensity();}
        filter.setExposure(listConfig.get(0).getIntensity());
    }

    @Override
    public GPUImageFilter getFilter() {
        return filter;
    }
}
