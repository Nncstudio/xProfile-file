package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities.AdjustActivity;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adjust.ParentFilter;

import java.util.ArrayList;
import java.util.Arrays;

public class SliderController extends Fragment {
    int numControl = 1;
    String title = "NO-TITLE";
    ParentFilter currentFilter = null;

    ImageView cancelBtn, confirmBtn;
    TextView adjust_title;

    ArrayList<SliderPack> packList;

    public SliderController() {}

    public SliderController(int index, String t, ParentFilter f) {
        numControl = f.getListConfig().size();
        title = t;
        currentFilter = f;
        currentFilter.setFilterIndex(index);
    }

    public static SliderController newInstance(int i, String t, ParentFilter f) {
        return new SliderController(i, t, f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.btm_seekbar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentFilter.storePreviousFilterIntensity();

        cancelBtn = view.findViewById(R.id.ivClose);
        confirmBtn = view.findViewById(R.id.ivDone);
        adjust_title = view.findViewById(R.id.tvTitle);
        packList = new ArrayList<>(Arrays.asList(
                new SliderPack(view.findViewById(R.id.llSeek1), view.findViewById(R.id.sb_1), view.findViewById(R.id.tvTitle_1), view.findViewById(R.id.tvValue_1)),
                new SliderPack(view.findViewById(R.id.llSeek2), view.findViewById(R.id.sb_2), view.findViewById(R.id.tvTitle_2), view.findViewById(R.id.tvValue_2)),
                new SliderPack(view.findViewById(R.id.llSeek3), view.findViewById(R.id.sb_3), view.findViewById(R.id.tvTitle_3), view.findViewById(R.id.tvValue_3))
        ));

        for (int i=0; i<numControl; ++i) {
            packList.get(i)._pack.setVisibility(View.VISIBLE);
            packList.get(i)._slider.setSeekLength((int) currentFilter.getListConfig().get(i).getMinSlider(), (int) currentFilter.getListConfig().get(i).getMaxSlider(), 0, 1);
            packList.get(i)._slider.setValue(currentFilter.getSliderValue(i));
            packList.get(i)._text.setText(currentFilter.getListParameter().get(i));

            int index = i;
            packList.get(i)._slider.setOnSeekChangeListener(new TwoLineSeekBar.OnSeekChangeListener() {
                @Override
                public void onSeekChanged(float value, float step) {
                    currentFilter.setFilterValue(index, value);
                    packList.get(index)._tv.setText(""+(int) value);
                    ((AdjustActivity) requireActivity()).updateFilterGroup(currentFilter);
                }

                @Override
                public void onSeekStopped(float value, float step) {

                }
            });
        }

        for (int i=numControl; i<3; ++i) packList.get(i)._pack.setVisibility(View.GONE);

        adjust_title.setText(title);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CLICKED", "confirmBtn");
                ((AdjustActivity) requireActivity()).onCloseSimpleFragment(currentFilter);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CLICKED", "cancelBtn");
                currentFilter.undoAllFilterValue();
                ((AdjustActivity) requireActivity()).onCloseSimpleFragment(currentFilter);
            }
        });
    }

    static class SliderPack {
        public LinearLayout _pack;
        public TwoLineSeekBar _slider;
        public TextView _text;
        public TextView _tv;

        SliderPack(LinearLayout p, TwoLineSeekBar s, TextView t, TextView tv) {
            _pack = p;
            _slider = s;
            _text = t;
            _tv = tv;
        }
    }
}