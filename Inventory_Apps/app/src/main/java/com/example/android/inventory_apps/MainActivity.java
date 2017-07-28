package com.example.android.inventory_apps;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.android.inventory_apps.data.InventoryDbHelper;
import com.example.android.inventory_apps.data.StockItem;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getCanonicalName();
    InventoryDbHelper dbHelper;
    com.example.android.inventory_apps.StockCursorAdapter adapter;
    int lastVisibleItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new InventoryDbHelper(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        final ListView listView = (ListView) findViewById(R.id.list_view);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        Cursor cursor = dbHelper.readStock();

        adapter = new com.example.android.inventory_apps.StockCursorAdapter(this, cursor);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0) return;
                final int currentFirstVisibleItem = view.getFirstVisiblePosition();
                if (currentFirstVisibleItem > lastVisibleItem) {
                    fab.show();
                } else if (currentFirstVisibleItem < lastVisibleItem) {
                    fab.hide();
                }
                lastVisibleItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.swapCursor(dbHelper.readStock());
    }

    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }

    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        adapter.swapCursor(dbHelper.readStock());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_dummy_data:
                // add dummy data for testing
                addDummyData();
                adapter.swapCursor(dbHelper.readStock());
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDummyData() {
        StockItem mouse = new StockItem(
                "Gaming Mouse",
                "$40",
                3,
                "ABC Supplier",
                "+0123456789",
                "computer@email.com",
                "android.resource://com.example.android.inventory_apps/drawable/mouse");
        dbHelper.insertItem(mouse);

        StockItem keyboard = new StockItem(
                "Keyboard",
                "$105",
                4,
                "ABC Supplier",
                "+0123456789",
                "computer@email.com",
                "android.resource://com.example.android.inventory_apps/drawable/keyboard");
        dbHelper.insertItem(keyboard);

        StockItem speaker = new StockItem(
                "Extra Bass Speaker",
                "$119",
                29,
                "ABC Supplier",
                "+0123456789",
                "computer@email.com",
                "android.resource://com.example.android.inventory_apps/drawable/speaker");
        dbHelper.insertItem(speaker);

        StockItem joystick = new StockItem(
                "Gaming Joystick",
                "$13",
                10,
                "ABC Supplier",
                "+0123456789",
                "computer@email.com",
                "android.resource://com.example.android.inventory_apps/drawable/joystick");
        dbHelper.insertItem(joystick);

        StockItem headphone = new StockItem(
                "Headphone",
                "$50",
                34,
                "ABC Supplier",
                "+0123456789",
                "computer@email.com",
                "android.resource://com.example.android.inventory_apps/drawable/headphone");
        dbHelper.insertItem(headphone);

        StockItem lcd = new StockItem(
                "Ultra HD 4K LCD",
                "$1209",
                9,
                "ABC Supplier",
                "+0123456789",
                "computer@email.com",
                "android.resource://com.example.android.inventory_apps/drawable/lcd");
        dbHelper.insertItem(lcd);

    }
}