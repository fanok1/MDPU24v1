package com.fanok.mdpu24v1.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fanok.mdpu24v1.NonFocusingScrollView;
import com.fanok.mdpu24v1.NonFocusingScrollViewHoresontal;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.dowland.DowlandJsonMarks;
import com.fanok.mdpu24v1.dowland.ParceJsonMarks;

import static android.content.Context.MODE_PRIVATE;

public class TabMarks extends Fragment {

    private int modul;
    private String lessons;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table_marks, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.modul = bundle.getInt("modul");
            this.lessons = bundle.getString("lesson");
        }
        NonFocusingScrollView scrollVertical = view.findViewById(R.id.scrollV);
        NonFocusingScrollViewHoresontal scrollHoresontal = view.findViewById(R.id.scrollH);




        scrollHoresontal.requestDisallowInterceptTouchEvent(true);
        scrollVertical.setOnTouchListener(new View.OnTouchListener() {
            private float mx, my, curX, curY;
            private boolean started = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                curX = motionEvent.getX();
                curY = motionEvent.getY();
                int dx = (int) (mx - curX);
                int dy = (int) (my - curY);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (started) {
                            scrollVertical.scrollBy(0, dy);
                            scrollHoresontal.scrollBy(dx, 0);
                        } else {
                            started = true;
                        }
                        mx = curX;
                        my = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        scrollVertical.scrollBy(0, dy);
                        scrollHoresontal.scrollBy(dx, 0);
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final String url = getResources().getString(R.string.server_api) + "get_marks.php";
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        DowlandJsonMarks dowlandJson = new DowlandJsonMarks(view, url, modul);
        if (dowlandJson.isOnline()) {
            dowlandJson.setProgressBar(view.findViewById(R.id.progressBar));
            dowlandJson.setData("modul", String.valueOf(modul));
            dowlandJson.setData("lessons", lessons);
            dowlandJson.setData("group", TypeTimeTable.getGroup());
            dowlandJson.execute();
        } else {
            String json = mPref.getString("modul_" + modul, "");
            if (!json.isEmpty()) {
                ParceJsonMarks parceJson = new ParceJsonMarks(json, view, modul);
                parceJson.setProgressBar(view.findViewById(R.id.progressBar));
                parceJson.execute();
            }
        }
    }
}
