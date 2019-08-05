package com.saraencci.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class listActivity extends AppCompatActivity {
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase firebasedb;
    private DatabaseReference firebasedbref;
    private ChildEventListener mchildlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu,menu);
        MenuItem inserMenu= menu.findItem(R.id.insert_menu);

        if(FirebaseUtil.isAdmin==true){
            inserMenu.setVisible(true);
        }else {
            inserMenu.setVisible(false);
        }
       return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListener();
                return true;

            case R.id.insert_menu:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                return  true;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("Traveldeals", this);
        RecyclerView rvDeals=(RecyclerView) findViewById(R.id.rvDeals);
        final DealAdapter adaptor =new DealAdapter();
        rvDeals.setAdapter(adaptor);
        LinearLayoutManager dealsLayoutManager=
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvDeals.setLayoutManager(dealsLayoutManager);
        FirebaseUtil.attachListener();
    }
    public void showMenu(){
        invalidateOptionsMenu();
    }
}
