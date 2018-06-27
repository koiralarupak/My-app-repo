package demo.mypicsapp.com.mypicsapp;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivitry extends AppCompatActivity {

    private  static  final  int PICK_IMAGE_REQUEST  = 71;
    private Uri uri = null;
    private ImageButton imagebutton;
    private EditText editName;
    private EditText editDesc;
  private StorageReference storageReference;
    DatabaseReference databaseReference;
   // FirebaseDatabase fbdb;
  // FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_activitry);
        editName = (EditText) findViewById(R.id.name);
        editDesc = (EditText) findViewById(R.id.desc);

    }

    public void addImage(View view){

        Intent galleryIntent =  new Intent(Intent.ACTION_GET_CONTENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Picture"),PICK_IMAGE_REQUEST );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST  && resultCode== RESULT_OK){
        uri = data.getData();
        imagebutton = (ImageButton) findViewById(R.id.imageButton);
        imagebutton.setImageURI(uri);
        }
    }

    public void submitButtonClicked(View view){
        final String titlename = editName.getText().toString().trim();
        final   String titlevalue = editName.getText().toString().trim();

         storageReference = FirebaseStorage.getInstance().getReference();

        if(!TextUtils.isEmpty(titlename) && !TextUtils.isEmpty(titlevalue)){
            StorageReference filepath = storageReference.child("PostImage").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(PostActivitry.this,"Upload Complete",Toast.LENGTH_SHORT).show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    databaseReference = database.getReference();
                    DatabaseReference newPost = databaseReference.child("InstaApp");
                    newPost.child("title").setValue(titlename);
                    newPost.child("desc").setValue(titlevalue);
                    newPost.child("image").setValue(downloadurl.toString());

                }
            });

        }
    }
}
