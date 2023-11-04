package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adjust;

import android.content.Context;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;

import java.util.ArrayList;
import java.util.Collections;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class ContrastFilter extends ParentFilter {
    GPUImageContrastFilter filter;

    public ContrastFilter(ArrayList<AdjustConfig> listCfg, Context ctx) {
        filterName = ctx.getResources().getString(R.string.contrast);
        filter = new GPUImageContrastFilter();
        listParameter = new ArrayList<>(Collections.singletonList(ctx.getResources().getString(R.string.contrast)));
        listConfig = listCfg;
    }

    @Override
    public void setFilterValue(int index, float sliderIntensity) {
        filter.setContrast(listConfig.get(index).setAndReturnIntensity(sliderIntensity));
    }

    @Override
    public void undoAllFilterValue() {
        for (AdjustConfig cfg: listConfig) {cfg.undoIntensity();}
        filter.setContrast(listConfig.get(0).getIntensity());
    }

    @Override
    public GPUImageFilter getFilter() {
        return filter;
    }
}
