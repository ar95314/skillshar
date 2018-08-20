package com.example.abhis.sample.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhis.sample.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;
    String uid;
    String user;
    String tit;
    String des;
    String time;
    String uri=null;
    String imgview;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef,MUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;




    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postrow,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogRecyclerAdapter.ViewHolder holder, int position) {
        Blog blog= blogList.get(position);
        String imageUrl= null;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid=mUser.getUid();
        mDatabase= FirebaseDatabase.getInstance();
        MUserRef=mDatabase.getReference().child("Users");
        mRef=mDatabase.getReference().child("Users");
        MUserRef.keepSynced(true);
        MUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Objects.equals(dataSnapshot.child(uid).child("verified").getValue(String.class), "true"))
                {
                    holder.verified.setVisibility(View.VISIBLE);

                }
                else{
                    holder.verified.setVisibility(View.GONE);
                }
                String ImageUrl;
                ImageUrl=dataSnapshot.child(uid).child("image").getValue(String.class);
                Picasso
                        .with(context)
                        .load(ImageUrl)
                        .into(holder.avatar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());
        holder.username.setText(blog.getUsername());
        holder.userid.setText(blog.getUserid());
      /*  if(Objects.equals(dataSnapshot.child(uid).child("verified").getValue(String.class), "true"))
        {
            verified.setVisibility(View.VISIBLE);
        }*/


     //   java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
       // String formatteddate= dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
       holder.v.setText(blog.getTimestamp());
        //holder.v.setReferenceTime(new Date().getTime());
       // time=formatteddate;
        imageUrl=blog.getImage();
        if(imageUrl!=null)
        {

        Picasso.with(context)
                .load(imageUrl)
                .into(holder.img);
        uri=imageUrl;}

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView desc;
        public TextView timestamp;
        public ImageView img;
        public TextView username;
        public TextView userid;
        public ImageView verified;
        public CircleImageView avatar;
        RelativeTimeTextView v;



        String userID,parent,myParentNode;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;
            title= view.findViewById(R.id.postview_titlelistt);
            desc= view.findViewById(R.id.post_text);
            img= view.findViewById(R.id.post_imglist);
           // timestamp=view.findViewById(R.id.timestamp);
            v= view.findViewById(R.id.timestamp);


            username=view.findViewById(R.id.usernameTV);
            userid=view.findViewById(R.id.post_userid);
            verified=view.findViewById(R.id.verifiied);
            avatar=view.findViewById(R.id.avatar);




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next activity
                    ////



}

                    });}}}
