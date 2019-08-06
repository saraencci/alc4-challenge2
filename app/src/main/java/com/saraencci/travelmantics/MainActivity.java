package com.saraencci.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase firebasedb;
    private DatabaseReference firebasedbref;
    private static final int pictsize =42;

    EditText txttittle;
    EditText  txtprice;
    EditText txtdescription;
    TravelDeal deal;
    ImageView imageView;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebasedb =FirebaseUtil.firebasedb;
        firebasedbref = FirebaseUtil.firebasedbref;
        txttittle=(EditText) findViewById(R.id.txttittle);
        txtprice  =(EditText) findViewById(R.id.txtPrice);
        txtdescription=(EditText) findViewById(R.id.txtDescription);
        imageView=(ImageView) findViewById(R.id.image);
        final Intent intent=getIntent();
          TravelDeal deal=(TravelDeal) intent.getSerializableExtra("Deal");
        if(deal==null)
        {
            deal=new TravelDeal();
        }
        this.deal=deal;
        txttittle.setText(deal.getTittle());
        txtdescription.setText(deal.getDescription());
        txtprice.setText(deal.getPrice());
        String temp=deal.getImgUrl();
        showImage(temp);
        Button btnimage=(Button)findViewById(R.id.btnimage);
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/jpeg");
                intent1.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent1.createChooser(intent1,"insert picture"
                ),pictsize);            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_menu:
                delete();
                Toast.makeText(this, "deleted succesfully", Toast.LENGTH_SHORT).show();
                back2list();
                return true;
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "deal saved", Toast.LENGTH_SHORT).show();
                clean();
                back2list();
                return true;

            default:
                    return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);

        if(FirebaseUtil.isAdmin==true){
            menu.findItem(R.id.save_menu).setVisible(true);
            menu.findItem(R.id.delete_menu).setVisible(true);
            enableEditText(true);
        }else {
            menu.findItem(R.id.save_menu).setVisible(false);
            menu.findItem(R.id.delete_menu).setVisible(false);
            enableEditText(false);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pictsize && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            final StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String url = task.getResult().toString();
                            deal.setImgUrl(url);
                            //mTravelDeal.setImageName(taskSnapshot.getStorage().getPath());
                            showImage(url);
                        }
                    });
                }
            });
        }
    }






    private void saveDeal(){
        deal.setTittle(txttittle.getText().toString());
        deal.setPrice(txtprice.getText().toString());
        deal.setDescription(txtdescription.getText().toString());
        if(deal.getId()==null){
            firebasedbref.push().setValue(deal);
        }
        else {
            firebasedbref.child(deal.getId()).setValue(deal);
        }
    }
    private void delete(){
        if(deal==null){
            Toast.makeText(this, "the deal doesnt exist", Toast.LENGTH_SHORT).show();
        }
        firebasedbref.child(deal.getId()).removeValue();

    }
    private void back2list(){
        Intent intent=new Intent(this,listActivity.class);
        startActivity(intent);
    }
    private void clean(){
        txttittle.setText("");
        txtprice.setText("");
        txtdescription.setText("");
        txttittle.requestFocus();


    }
    private  void enableEditText(boolean isEnabled ) {
        txtprice.setEnabled(isEnabled);
        txtdescription.setEnabled(isEnabled);
        txttittle.setEnabled(isEnabled);
    }
    private void showImage(String url) {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Picasso.get()
                .load(url)
                //.resize(67, 67)
                //.centerCrop()
                .into(imageView);
        //if (url != null && url.isEmpty() == false) {
          //  int width = Resources.getSystem().getDisplayMetrics().widthPixels;
//            Picasso.with(this)
//                    .load(url)
//                    .resize(width, width*2/3)
//                    .centerCrop()
//                    .into(imageView);

//            Picasso.get()
//                    .load(url)
//                    .resize(50, 50)
//                    .centerCrop()
//                    .into(imageView);
        }
    }



