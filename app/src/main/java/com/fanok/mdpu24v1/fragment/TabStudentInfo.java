package com.fanok.mdpu24v1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.activity.MainActivity;
import com.fanok.mdpu24v1.dowland.DowlandJsonStudentInfo;
import com.fanok.mdpu24v1.dowland.ParceJsonStudentInfo;

import java.util.Objects;

public class TabStudentInfo extends Fragment {

    private String name;
    private ListView listView;

    public String getName() {
        return name;
    }

    public ListView getListView() {
        return listView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_info, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.name = bundle.getString("name");
        }
        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final String url = getResources().getString(R.string.server_api) + "get_student_info.php";
        super.onViewCreated(view, savedInstanceState);
        DowlandJsonStudentInfo dowlandJson = new DowlandJsonStudentInfo(view, url, listView, name, (MainActivity) Objects.requireNonNull(getActivity()));
        if (dowlandJson.isOnline()) {
            dowlandJson.setProgressBar(view.findViewById(R.id.progressBar));
            dowlandJson.execute();
        } else {
            SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
            String json = mPref.getString("Student_" + name, "");
            if (!json.isEmpty()) {
                ParceJsonStudentInfo parceJson = new ParceJsonStudentInfo((MainActivity) Objects.requireNonNull(getActivity()), json, listView);
                parceJson.setProgressBar(view.findViewById(R.id.progressBar));
                parceJson.execute();
            }

        }
    }
}
