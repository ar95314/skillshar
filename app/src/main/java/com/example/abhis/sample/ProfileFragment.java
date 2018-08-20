package com.example.abhis.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context thiscontext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView name,phone,skills,experience,projects;
    CircleImageView img;
    Button edit;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser mUser=mAuth.getCurrentUser();
    String uid= mUser.getUid(),imgurl;
    FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();;
    DatabaseReference mRef=mDatabase.getReference().child("Users");

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View v= inflater.inflate(R.layout.fragment_profile, container, false);
        name= v.findViewById(R.id.username);
        phone= v.findViewById(R.id.user_phone_number);
        skills= v.findViewById(R.id.skillsET);
        experience= v.findViewById(R.id.experienceET);
        projects= v.findViewById(R.id.projectsET);
        img=v.findViewById(R.id.profile_image);
        thiscontext = container.getContext();
        edit=v.findViewById(R.id.btnEdit);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child(uid).child("name").getValue(String.class));
                phone.setText(dataSnapshot.child(uid).child("phone").getValue(String.class));
                skills.setText(dataSnapshot.child(uid).child("skills").getValue(String.class));
                experience.setText(dataSnapshot.child(uid).child("experince").getValue(String.class));
                projects.setText(dataSnapshot.child(uid).child("projects").getValue(String.class));
                imgurl=dataSnapshot.child(uid).child("image").getValue(String.class);
                Picasso
                        .with(thiscontext)
                        .load(imgurl)
                        .into(img);
                mRef.removeEventListener(this);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent edit = new Intent(thiscontext, UserProfile.class);
        edit.putExtra("name", name.getText().toString());
        edit.putExtra("phone", phone.getText().toString());
        edit.putExtra("skills", skills.getText().toString());
        edit.putExtra("projects", projects.getText().toString());
        edit.putExtra("experience", experience.getText().toString());
        edit.putExtra("imgurl", imgurl);

        startActivity(edit);
    }
});

return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*   if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
