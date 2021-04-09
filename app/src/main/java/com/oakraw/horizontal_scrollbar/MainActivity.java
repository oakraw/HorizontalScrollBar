package com.oakraw.horizontal_scrollbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.oakraw.library.HorizontalScrollBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView list = findViewById(R.id.list);
        HorizontalScrollBar scrollbar = findViewById(R.id.scrollbar);

        scrollbar.attachRecyclerView(list);

        List<String> items = new ArrayList<String>();
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        list.setAdapter(new ItemsAdapter(items));
    }
}