package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adjust;

import android.content.Context;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHighlightShadowFilter;

public class HighlightShadowFilter extends ParentFilter {
    GPUImageHighlightShadowFilter filter;

    public HighlightShadowFilter(ArrayList<AdjustConfig> listCfg, Context ctx) {
        filterName = ctx.getString(R.string.highlight);
        filter = new GPUImageHighlightShadowFilter();
        listParameter = new ArrayList<>(Arrays.asList(ctx.getString(R.string.highlight), ctx.getString(R.string.shadow)));
        listConfig = listCfg;
    }

    @Override
    public void setFilterValue(int index, float sliderIntensity) {
        float temp = listConfig.get(index).setAndReturnIntensity(sliderIntensity);
        switch (index) {
            case 0: filter.setHighlights(temp); break;
            case 1: filter.setShadows(temp); break;
        }
    }

    @Override
    public void undoAllFilterValue() {
        for (int i=0; i<listConfig.size(); ++i) {
            listConfig.get(i).undoIntensity();
            float temp = listConfig.get(i).getIntensity();
            switch (i) {
                case 0: filter.setHighlights(temp); break;
                case 1: filter.setShadows(temp); break;
            }
        }
    }

    @Override
    public GPUImageFilter getFilter() {
        return filter;
    }
}
