package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fanok.mdpu24v1.ClickListnerMarks;
import com.fanok.mdpu24v1.Dates;
import com.fanok.mdpu24v1.Marks;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ParceJsonMarks extends AsyncTask<Void, Void, ArrayList<Marks>> {


    private String json;
    private int modul;
    @SuppressLint("StaticFieldLeak")
    private TableLayout firstColum;
    @SuppressLint("StaticFieldLeak")
    private TableLayout table;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    private Dates dates = new Dates();
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public ParceJsonMarks(String json, View view, int modul) {
        this.json = json;
        this.firstColum = view.findViewById(R.id.firstColum);
        this.table = view.findViewById(R.id.table);
        this.context = table.getContext();
        this.modul = modul;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null) progressBar.setVisibility(ProgressBar.VISIBLE);


    }

    @Override
    protected ArrayList<Marks> doInBackground(Void... voids) {
        ArrayList<Marks> marks = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            boolean flag = false;
            JsonObject jMarks = jsonArray.get(i).getAsJsonObject();
            String name = jMarks.get("name").getAsString();
            for (int j = 0; j < marks.size(); j++) {
                if (marks.get(j).getName().equals(name)) {
                    try {
                        marks.get(j).setMark(jMarks.get("day").getAsString(), jMarks.get("mark").getAsInt());
                        if (!jMarks.get("day").getAsString().equals("0"))
                            dates.setDates(jMarks.get("day").getAsString());
                        flag = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
            if (!flag) {
                Marks mark = new Marks(name);
                try {
                    mark.setMark(jMarks.get("day").getAsString(), jMarks.get("mark").getAsInt());
                    if (!jMarks.get("day").getAsString().equals("0"))
                        dates.setDates(jMarks.get("day").getAsString());
                    marks.add(mark);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        return marks;
    }

    @Override
    protected void onPostExecute(ArrayList<Marks> marks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.forLanguageTag("UA"));
        super.onPostExecute(marks);
        ArrayList<Date> dates = this.dates.sort();
        TableRow row = new TableRow(context);
        TableRow.LayoutParams lpRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lpRow);
        if (!dates.isEmpty()) row.addView(createTextView("Имя/дата"), 0);
        firstColum.addView(row, 0);
        row = new TableRow(context);
        row.setLayoutParams(lpRow);
        for (int i = 0; i < dates.size(); i++) {
            row.addView(createTextView(dateFormat.format(dates.get(i))), i);
        }
        if (dates.size() != 0) {
            row.addView(createTextView("Тек."));
            row.addView(createTextView("Модуль"));
            row.addView(createTextView("Итог"));
        }
        table.addView(row);
        for (int i = 0; i < marks.size(); i++) {
            row = new TableRow(context);
            row.setLayoutParams(lpRow);
            row.addView(createTextView(marks.get(i).getName()), 0);
            firstColum.addView(row);
            row = new TableRow(context);
            row.setLayoutParams(lpRow);
            HashMap<Date, Integer> marksForStudent = marks.get(i).getMarks();
            for (int j = 0; j < dates.size(); j++) {
                TextInputEditText editText;
                try {
                    int mark = marksForStudent.get(dates.get(j));
                    editText = createEditText(mark, 5);
                } catch (RuntimeException ignored) {
                    editText = createEditText(0, 5);
                }
                if (TypeTimeTable.getType() != TypeTimeTable.studentTimeTable) {
                    editText.setOnEditorActionListener(new ClickListnerMarks(marks.get(i).getName(), dates.get(j), String.valueOf(modul)));
                }
                row.addView(editText, j);
            }
            if (dates.size() != 0) {
                row.addView(createTextView(String.valueOf(marks.get(i).abs())));
                TextView textView = createEditText(marks.get(i).getModul(), 30);
                if (TypeTimeTable.getType() != TypeTimeTable.studentTimeTable)
                    textView.setOnEditorActionListener(new ClickListnerMarks(marks.get(i).getName(), null, String.valueOf(modul)));
                row.addView(textView);
                row.addView(createTextView(String.valueOf(marks.get(i).itog())));
            }
            table.addView(row);
        }

        if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);
    }

    private TextView createTextView(String text) {
        TextView textView = textView();
        textView.setText(text);
        return textView;
    }

    private TextInputEditText createEditText(int text, int maxValue) {
        TextInputEditText editText = new TextInputEditText(context);
        TableRow.LayoutParams lpTextView = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lpTextView.setMargins(2, 2, 2, 2);
        editText.setLayoutParams(lpTextView);
        editText.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        editText.setPadding(5, 0, 5, 0);
        editText.setTextSize(20);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(String.valueOf(maxValue).length());
        editText.setFilters(fArray);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setMovementMethod(null);
        if (text != 0) {
            editText.setText(String.valueOf(text));
        } else editText.setText("");

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            private int number;

            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    if (b) {
                        number = Integer.parseInt(editText.getText().toString());
                        editText.setText("");
                    } else {
                        if (editText.getText().toString().isEmpty()) {
                            editText.setText(String.valueOf(number));
                        }
                    }
                } catch (Exception e) {
                    Log.e("Change", e.getMessage());
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            private String oldVal;
            private String newVal;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    oldVal = charSequence.toString();
                } catch (Exception e) {
                    Log.e("before", e.getMessage());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    newVal = charSequence.toString();
                } catch (Exception e) {
                    Log.e("Change", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!newVal.isEmpty()) {
                        int n = Integer.parseInt(newVal);
                        if (n < 1 || n > maxValue) {
                            editText.setText(oldVal);
                        }
                    }
                } catch (Exception e) {
                    Log.e("after", e.getMessage());
                }

            }
        });

        return editText;
    }


    private TextView textView() {
        TextView textView = new TextView(context);
        TableRow.LayoutParams lpTextView = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lpTextView.setMargins(2, 2, 2, 2);
        textView.setLayoutParams(lpTextView);
        textView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        textView.setPadding(5, 0, 5, 0);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setTextSize(20);
        return textView;
    }


}