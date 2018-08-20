package com.example.abhis.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import mabbas007.tagsedittext.TagsEditText;

public class UserProfile extends AppCompatActivity implements ProgressGenerator.OnCompleteListener {
    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private Uri resultUri = null;
    private final static int gallerycode = 1;
    CircleImageView avatar;
    TagsEditText skills, exp, project;
    Button btnSubmit;
    ProgressDialog mProgressDialog;
    EditText name,phone;
    private DatabaseReference mPosts;

    FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    DatabaseReference mUserbasereference=mDatabase.getReference().child("Users");
    FirebaseAuth mAuth;
    private StorageReference mFirebaseStorage;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;
    String uid,imgurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mProgressDialog = new ProgressDialog(this);
        name = findViewById(R.id.username);
        exp=findViewById(R.id.experienceET);
        project=findViewById(R.id.projectsET);
        skills =findViewById(R.id.skillsET);
        avatar= findViewById(R.id.profile_image);
        phone=findViewById(R.id.user_phone_number);
mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        assert mUser != null;
        uid= mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mPosts = mDatabase.getReference().child("Posts");
        mPosts.keepSynced(true);
        mUserbasereference = mDatabase.getReference().child("Users");
        mUserbasereference.keepSynced(true);
        mFirebaseStorage= FirebaseStorage.getInstance().getReference().child("UserProfilePics");

btnSubmit=findViewById(R.id.btnEdit);

      //  final ProgressGenerator progressGenerator = new ProgressGenerator( this);
       // final ActionProcessButton btnSubmit = (ActionProcessButton) findViewById(R.id.btnSubmit);
        //Bundle extras = getIntent().getExtras();
     /*   if(extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
            btnSubmit.setMode(ActionProcessButton.Mode.ENDLESS);
        } else {
            btnSubmit.setMode(ActionProcessButton.Mode.PROGRESS);
        }
*/



                    String nametxt = getIntent().getExtras().getString("name");
                    String phonetxt = getIntent().getExtras().getString("phone");
                    String skillstxt = getIntent().getExtras().getString("skills");
                    String projectstxt = getIntent().getExtras().getString("projects");
                    String experiencetxt = getIntent().getExtras().getString("experience");
                    final String imgurltxt = getIntent().getExtras().getString("imgurl");
                   /* mUserbasereference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name.setText(dataSnapshot.child(uid).child("name").getValue(String.class));
                            phone.setText(dataSnapshot.child(uid).child("phone").getValue(String.class));
                            skills.setText(dataSnapshot.child(uid).child("skills").getValue(String.class));
                            exp.setText(dataSnapshot.child(uid).child("experince").getValue(String.class));
                            project.setText(dataSnapshot.child(uid).child("projects").getValue(String.class));
                            imgurl=dataSnapshot.child(uid).child("image").getValue(String.class);
                            Picasso.with(set_UserProfile.this)
                                    .load(imgurl)
                                    .into(avatar);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/
                    name.setText(nametxt);
                    phone.setText(phonetxt);
                    skills.setText(skillstxt);
                    exp.setText(experiencetxt);
                    project.setText(projectstxt);

                    Picasso.with(UserProfile.this)
                            .load(imgurltxt)
                            .into(avatar);


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,gallerycode);


            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressGenerator.start(btnSubmit);
                //btnSubmit.setEnabled(false);
               // name.setEnabled(false);
             //   user_skills.setEnabled(false);


                    setUserProfile();


        }

            private void setUserProfile() {

                final String username = name.getText().toString().trim();
                final String skill = skills.getText().toString();
                final String experience = exp.getText().toString();
                final String proj = project.getText().toString();
                final String phonenumb = phone.getText().toString();
                try {
                    if (!TextUtils.isEmpty(username) &&
                            !TextUtils.isEmpty(skill) &&
                            !TextUtils.isEmpty(experience) &&
                            !TextUtils.isEmpty(phonenumb) &&
                            !TextUtils.isEmpty(proj)

                            )


                    {
                        mProgressDialog.setMessage("Updating Profile");
                        mProgressDialog.show();
                        if (resultUri != null) {
                            final StorageReference imagepath = mFirebaseStorage.child("UserProfilePics")
                                    .child(resultUri.getLastPathSegment());

                            imagepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadurl = taskSnapshot.getDownloadUrl();


                                    String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();


                                    DatabaseReference currentuserdb = mUserbasereference.child(userid);
                                    currentuserdb.child("name").setValue(username);
                                    currentuserdb.child("skills").setValue(skill);
                                    if (downloadurl != null)
                                        currentuserdb.child("image").setValue(downloadurl.toString());
                                    else
                                        currentuserdb.child("image").setValue(getIntent().getExtras().getString("imgurl"));
                                    currentuserdb.child("projects").setValue(proj);
                                    currentuserdb.child("phone").setValue(phonenumb);

                                    currentuserdb.child("experince").setValue(experience);
                                    currentuserdb.child("about").setValue("");
                                    mProgressDialog.dismiss();

                                    Toast.makeText(UserProfile.this, "Success", Toast.LENGTH_LONG).show();


                                    Intent intent = new Intent(UserProfile.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }


                            });

                        }else
                        {
                            String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();


                            DatabaseReference currentuserdb = mUserbasereference.child(userid);
                            currentuserdb.child("name").setValue(username);
                            currentuserdb.child("skills").setValue(skill);

                            currentuserdb.child("projects").setValue(proj);
                            currentuserdb.child("phone").setValue(phonenumb);

                            currentuserdb.child("experince").setValue(experience);
                            currentuserdb.child("about").setValue("");
                            mProgressDialog.dismiss();

                            Toast.makeText(UserProfile.this, "Success", Toast.LENGTH_LONG).show();


                            Intent intent = new Intent(UserProfile.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }}
                    }catch(Exception e){
                    }
                }




            });
        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==gallerycode){
            Uri mImageUri= data.getData();
            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(UserProfile.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                avatar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Exception error = result.getError();
                Toast.makeText(UserProfile.this,"Error",Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onComplete() {

    }
}
