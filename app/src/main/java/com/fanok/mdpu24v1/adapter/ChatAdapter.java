package com.fanok.mdpu24v1.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.fanok.mdpu24v1.Message;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Message> model;

    public ChatAdapter(Context context, ArrayList<Message> model) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        if (view == null) {
            view = inflater.inflate(R.layout.chat_item, viewGroup, false);
            holder.imageView = view.findViewById(R.id.image_content);
            holder.imageText = view.findViewById(R.id.image_text);
            holder.name = view.findViewById(R.id.name);
            holder.text = view.findViewById(R.id.message);
            holder.time = view.findViewById(R.id.time);
            holder.imageConteiner = view.findViewById(R.id.circle_image);
            holder.background = view.findViewById(R.id.bg);
            holder.space = view.findViewById(R.id.time_gravity_right);
            holder.title = view.findViewById(R.id.title);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        SharedPreferences mPref = context.getSharedPreferences(StartActivity.PREF_NAME, Context.MODE_PRIVATE);
        String name = mPref.getString("name", "");
        String nameValue = model.get(i).getName();

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (!name.equals(nameValue)) {
            String text = "" + nameValue.charAt(0) + nameValue.charAt(nameValue.indexOf(" ") + 1);
            holder.imageText.setText(text.toUpperCase());
            if (!model.get(i).getPhoto().equals("null")) {
                Picasso.get().load(model.get(i).getPhoto()).into(holder.imageView);
            }
            holder.name.setText(model.get(i).getName());
            params2.setMarginEnd(100);
            params2.setMarginStart(30);
            holder.space.setVisibility(View.GONE);
            holder.background.setBackground(context.getResources().getDrawable(R.drawable.bg_radius_white));
            holder.imageConteiner.setVisibility(View.VISIBLE);
            holder.name.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            holder.text.setLayoutParams(params);

        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            holder.text.setLayoutParams(params);
            holder.imageConteiner.setVisibility(View.GONE);
            holder.name.setVisibility(View.GONE);
            holder.background.setBackground(context.getResources().getDrawable(R.drawable.bg_radius_green));
            holder.space.setVisibility(View.VISIBLE);
            params2.setMarginStart(100);
        }
        LinearLayout.LayoutParams titleParam;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.forLanguageTag("UA"));
        holder.time.setText(dateFormat.format(model.get(i).getDate()));
        holder.text.setText(model.get(i).getText());
        holder.background.setLayoutParams(params2);

        if (holder.time.getText().length() + holder.name.getText().length() > holder.text.getText().length())
            titleParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        else
            titleParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.title.setLayoutParams(titleParam);


        return view;
    }

    public class Holder {
        CircleImageView imageView;
        TextView imageText;
        TextView name;
        TextView text;
        TextView time;
        FrameLayout imageConteiner;
        LinearLayout background;
        Space space;
        LinearLayout title;
    }
}
