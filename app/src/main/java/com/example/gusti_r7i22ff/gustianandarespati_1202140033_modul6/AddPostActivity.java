package com.example.gusti_r7i22ff.gustianandarespati_1202140033_modul6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gusti_r7i22ff.gustianandarespati_1202140033_modul6.model.Post;
import com.example.gusti_r7i22ff.gustianandarespati_1202140033_modul6.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    EditText mTitlePost, mPost;
    ImageView imageView;
    Button mChooseImage;
    DatabaseReference databaseFood;
    FirebaseAuth mAuth;

    private Uri imageUri;

    private StorageReference mStorage;
    Query databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        imageUri = null;

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference().child("images");

        databaseFood = FirebaseDatabase.getInstance().getReference(MainActivity.table1);

        databaseUser = FirebaseDatabase.getInstance().getReference(MainActivity.table3);

        mTitlePost = (EditText) findViewById(R.id.Et_title_post);
        mPost = (EditText) findViewById(R.id.Et_post);
        imageView = findViewById(R.id.img_post);

        mChooseImage = findViewById(R.id.Btn_Choose_Image);
        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    public void Add(View view) {

        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final User user = dataSnapshot.child(mAuth.getUid()).getValue(User.class);

                final String name = user.getUsername();
                final String title = mTitlePost.getText().toString();
                final String postMessage = mPost.getText().toString();
                final String id = databaseFood.push().getKey();
                final String userId = mAuth.getUid();
                final long timestamp = System.currentTimeMillis();

                if (imageUri != null && !TextUtils.isEmpty(name)) {

                    final StorageReference image = mStorage.child(id + ".jpg");

                    image.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {

                            if (uploadTask.isSuccessful()) {

                                String download_url = uploadTask.getResult().getDownloadUrl().toString();
                                Post post = new Post(id, userId, name, download_url, title, postMessage,0-timestamp);
                                databaseFood.child(id).setValue(post);

                            } else {
                                Toast.makeText(AddPostActivity.this, "Error : " + uploadTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    Toast.makeText(AddPostActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddPostActivity.this, MainActivity.class);
                    startActivity(i);
                } else {

                    Toast.makeText(AddPostActivity.this, "Isi Terlebih Dahulu dan Memilih Photo", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
