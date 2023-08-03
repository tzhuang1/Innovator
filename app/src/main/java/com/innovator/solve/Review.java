package com.innovator.solve;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;

public class Review extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        List<Pair<Integer, String>> list = Arrays.asList(
                new Pair<>(1, "Correct"),
                new Pair<>(2, "Correct"),
                new Pair<>(3, "Correct")
        );
        TableLayout table = createTable(list, this);
        System.out.println(table);
        FrameLayout container = findViewById(R.id.table_container);
        container.addView(table);
    }
    public TableLayout createTable(List<Pair<Integer, String>> list, Context context) {
        TableRow.LayoutParams paramsExample = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,0.5f);

        TableLayout table = new TableLayout(context);
        table.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
        ));
        table.setPadding(16, 16, 16, 16);




        for (Pair<Integer, String> item : list) {
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f
            ));
            Button btn = new Button(context);
            btn.setText(String.valueOf(item.first));
            btn.setTextSize(40);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("sexy");
                }
            });
            btn.setBackgroundResource(R.drawable.bordercolor);
            row.addView(btn);

            System.out.println(btn.getText());

            TextView textView2 = new TextView(context);
            textView2.setText(item.second);
            textView2.setMinimumWidth(250);
            textView2.setTextSize(40);
            textView2.setBackgroundResource(R.drawable.border);
            textView2.setLayoutParams(paramsExample);
//            textView2.setLayoutParams(new LinearLayout.LayoutParams(
//                    0,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    1.0f
//            ));
            row.addView(textView2);
            table.addView(row);
        }
        return table;
    }
}
