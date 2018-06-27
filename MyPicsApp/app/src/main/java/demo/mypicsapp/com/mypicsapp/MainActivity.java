package demo.mypicsapp.com.mypicsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.Query;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView myInstaList;
    private DatabaseReference mDatabase;
    Uri uri;
    Query myQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myInstaList = (RecyclerView) findViewById(R.id.insta_list);
        myInstaList.setHasFixedSize(true);
        myInstaList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("InstaApp");



    }

    @Override
    protected void onStart() {
        super.onStart();

////       FirebaseRecyclerAdapter<Insta,InstaViewHolder> fbra = new FirebaseRecyclerAdapter<Insta,InstaViewHolder>(
////               Insta.class,
////               R.layout.insta_row,
////               InstaViewHolder.class,
////               mDatabase
////       )
////       {
////            @Override
////           protected void populateViewHolder(InstaViewHolder viewHolder, Insta model,int position){
////                viewHolder.setTitle(model.getTitle());
////                viewHolder.setDesc(model.getDesc());
////                viewHolder.setImage(getApplicationContext(),model.getImage());
////
////           }
////       };
//
        myQuery = getQuery(mDatabase).limitToLast(25);

        FirebaseRecyclerOptions<Insta> options =
                new FirebaseRecyclerOptions.Builder<Insta>()
                        .setQuery(myQuery, Insta.class)
                        .build();

       FirebaseRecyclerAdapter<Insta,InstaViewHolder> fbra = new FirebaseRecyclerAdapter<Insta, InstaViewHolder>(
               options) {
           @Override
           protected void onBindViewHolder(@NonNull InstaViewHolder viewHolder, int position, @NonNull Insta model) {

               viewHolder.setTitle(model.getTitle());
               viewHolder.setDesc(model.getDesc());
               viewHolder.setImage(getApplicationContext(),model.getImage());

           }

           @NonNull
           @Override
           public InstaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.insta_row, parent, false);

               return new InstaViewHolder(view);
           }
       };
        myInstaList.setAdapter(fbra);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class InstaViewHolder extends RecyclerView.ViewHolder{
        public InstaViewHolder(View itemView){
            super(itemView);
            View mView = itemView;
        }

        public  void setTitle(String title){
            TextView post_title = (TextView) itemView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }

        public  void setDesc(String desc){
                TextView post_desc = (TextView) itemView.findViewById(R.id.textDesc);
                post_desc.setText(desc);
        }

        public  void setImage(Context ctx,String image){
            ImageView imageView =(ImageView)itemView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(imageView);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.addIcon){
            Intent intent = new Intent(MainActivity.this,PostActivitry.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
    public  Query getQuery(DatabaseReference databaseReference){
        return  mDatabase;
    }
}
