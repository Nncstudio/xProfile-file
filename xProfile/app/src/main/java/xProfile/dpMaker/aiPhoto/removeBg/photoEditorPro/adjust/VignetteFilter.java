package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adjust;

import android.content.Context;
import android.graphics.PointF;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;

public class VignetteFilter extends ParentFilter {
    GPUImageVignetteFilter filter;

    public VignetteFilter(ArrayList<AdjustConfig> listCfg, Context ctx) {
        filterName = ctx.getString(R.string.vignette);
        filter = new GPUImageVignetteFilter();
        listParameter = new ArrayList<>(Arrays.asList(ctx.getString(R.string.start_position), ctx.getString(R.string.end_position)));
        listConfig = listCfg;
        filter.setVignetteCenter(new PointF(0.5f, 0.5f));
        filter.setVignetteStart(listConfig.get(0).setAndReturnIntensity(listConfig.get(0).getOriginSlider()));
        filter.setVignetteEnd(listConfig.get(1).setAndReturnIntensity(listConfig.get(1).getOriginSlider()));
    }

    @Override
    public void setFilterValue(int index, float sliderIntensity) {
        float temp = listConfig.get(index).setAndReturnIntensity(sliderIntensity);
        switch (index) {
            case 0: filter.setVignetteStart(temp); break;
            case 1: filter.setVignetteEnd(temp); break;
        }
    }

    @Override
    public void undoAllFilterValue() {
        for (int i=0; i<listConfig.size(); ++i) {
            listConfig.get(i).undoIntensity();
            float temp = listConfig.get(i).getIntensity();
            switch (i) {
                case 0: filter.setVignetteStart(temp); break;
                case 1: filter.setVignetteEnd(temp); break;
            }
        }
    }

    @Override
    public GPUImageFilter getFilter() {
        return filter;
    }
}
