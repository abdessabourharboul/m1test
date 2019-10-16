package com.univangers.tpcontacts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.univangers.tpcontacts.database.ContactContract;
import com.univangers.tpcontacts.database.ContactDBHelper;
import com.univangers.tpcontacts.preference.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MonAdapteur adapteur;
    private ContactDBHelper myDBHelper = new ContactDBHelper(this);
    private SQLiteDatabase my_db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (savedInstanceState != null) {
            adapteur.setData(savedInstanceState.<MonAdapteur.Contact>getParcelableArrayList("my_data"));
        } else {
            prepareDataBase();
        }*/
        prepareDataBase();
        handleRecyclerView();
        handleSwipe();
    }

    public void prepareDataBase() {
        adapteur = new MonAdapteur(this);
        my_db = myDBHelper.getWritableDatabase();
        Cursor cursor = my_db.query(ContactContract.ContactEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            //myDBHelper.onUpgrade(my_db,2,1);
            FakeData.insert_fake_data(my_db);
        }
        my_db = myDBHelper.getReadableDatabase();
        cursor = my_db.query(ContactContract.ContactEntry.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<MonAdapteur.Contact> res = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemNom = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_FIRST_NAME));
            String itemPrenom = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_LAST_NAME));
            String itemTel = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_TEL));
            res.add(new MonAdapteur.Contact(itemNom, itemPrenom, itemTel));
        }
        cursor.close();
        adapteur.setData(res);
    }

    public void handleRecyclerView() {
        recyclerView = findViewById(R.id.rv_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapteur);
    }

    public void handleSwipe() {
        ItemTouchHelper.SimpleCallback item_touch_helper_callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = (int) viewHolder.itemView.getTag();
                my_db = myDBHelper.getWritableDatabase();
                MonAdapteur.Contact current = adapteur.getM_data().get(position);
                if (my_db.delete(ContactContract.ContactEntry.TABLE_NAME,
                        ContactContract.ContactEntry.COLUMN_TEL + " = '" + current.numTel + "'", null) > 0) {
                    adapteur.supprimer(position);
                }
            }
        };
        new ItemTouchHelper(item_touch_helper_callback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapteur.saveBundle(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //((TextView) findViewById(R.id.logId)).setText("Datasize OnStart: " + adapteur.getItemCount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //((TextView) findViewById(R.id.logId)).setText("Datasize onResume: " + adapteur.getItemCount());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //((TextView) findViewById(R.id.logId)).setText("Datasize onPause: " + adapteur.getItemCount());
    }

    @Override
    protected void onStop() {
        my_db.close();
        super.onStop();
        //((TextView) findViewById(R.id.logId)).setText("Datasize onStop: " + adapteur.getItemCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //((TextView) findViewById(R.id.logId)).setText("Datasize onDestroy: " + adapteur.getItemCount());
    }

    public void rechercherBtnClick(View view) {
        System.out.println("call rechercherBtnClick");
        EditText editText = findViewById(R.id.et_saisieNomPrenom);
        String wanted = editText.getText().toString();
        my_db = myDBHelper.getReadableDatabase();
        Cursor cursor = my_db.query(
                ContactContract.ContactEntry.TABLE_NAME,
                null,
                "instr(prenom, ?) != 0 OR instr(nom, ?) != 0",
                new String[] { wanted, wanted },
                null,
                null,
                null);
        ArrayList<MonAdapteur.Contact> res = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemNom = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_FIRST_NAME));
            String itemPrenom = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_LAST_NAME));
            String itemTel = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_TEL));
            System.out.println(itemNom);
            System.out.println(itemPrenom);
            res.add(new MonAdapteur.Contact(itemNom, itemPrenom, itemTel));
        }
        cursor.close();
        adapteur.setData(res);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preference:
                launch_preference();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launch_preference(){
        Intent start_settings_activity = new Intent(this, SettingsActivity.class);
        startActivity(start_settings_activity);
    }
}
