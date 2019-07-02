package com.example.elessar1992.friendsrating;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    StorageReference storageReference; //storage
    String storePath = "Users_Profile_Cover_Imgs/"; //the address where images and cover picutre will be stored


    ImageView UserPicture, coverPicture;
    TextView nameId, emailId, phoneId;
    FloatingActionButton floatingActionButton;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    String cameraPermissions[];
    String storagePermissions[];

    Uri image_uri; //address of the user Image

    String profileandCoverPhote;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference(); //firebase Storage reference

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        UserPicture = view.findViewById(R.id.userPicture);
        nameId = view.findViewById(R.id.name_id);
        emailId = view.findViewById(R.id.email_id);
        phoneId = view.findViewById(R.id.phone_id);
        coverPicture = view.findViewById(R.id.coverImageId);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String name = ""+ ds.child("name").getValue();
                    String email = ""+ ds.child("email").getValue();
                    String phone = ""+ ds.child("phone").getValue();
                    String image = ""+ ds.child("image").getValue();
                    String cover = ""+ds.child("cover").getValue();
                    nameId.setText(name);
                    emailId.setText(email);
                    phoneId.setText(phone);
                    try
                    {
                        Picasso.get().load(image).into(UserPicture);
                    }
                    catch (Exception e)
                    {
                        Picasso.get().load(R.drawable.ic_def_picture).into(UserPicture);
                    }

                    try
                    {
                        Picasso.get().load(cover).into(coverPicture);
                    }
                    catch (Exception e)
                    {
                        //Picasso.get().load(R.drawable.ic_def_picture).into(UserPicture);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });


        return view;
    }

    private boolean checkStoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission()
    {
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission()
    {
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog()
    {
        String options[] = {"Edit Picture", "Edit Cover Photo", "Edit Name", "Edit Phone"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Option");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    //changing profile picture
                    Toast.makeText(getContext(), "Profile Pciture Updated", Toast.LENGTH_LONG).show();
                    profileandCoverPhote = "image";
                    showImageDialog();
                }
                else if (which == 1)
                {
                    //changing profile cover
                    Toast.makeText(getContext(), "Profile Cover Updated", Toast.LENGTH_LONG).show();
                    profileandCoverPhote = "image";
                    showImageDialog();
                }
                else if (which == 2)
                {
                    Toast.makeText(getContext(), "Profile Name Updated", Toast.LENGTH_LONG).show();
                    //calling method and pass key "name" as parameter to update the value in database
                    showNamePhoneUpdated("name");
                }
                else if (which == 3)
                {
                    Toast.makeText(getContext(), "Profile Phone Updated", Toast.LENGTH_LONG).show();
                    //edit phone clicked
                    showNamePhoneUpdated("phone");
                }
            }
        });
        //showing dialog
        builder.create().show();
    }

    private void showNamePhoneUpdated(final String key)
    {
        //key will contain either "name" or "phone"
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("update "+ key); //update either name or phone

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //editing UI
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter "+key); //name or phone
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String value = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    Toast.makeText(getActivity(),"Enter "+key, Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(getActivity(),"Enter "+key, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showImageDialog()
    {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0) // after clicking on camera
                {
                    //Toast.makeText(getContext(), "Profile Pciture Updated", Toast.LENGTH_LONG).show();
                    if(!checkCameraPermission())
                    {
                        requestCameraPermission();
                    }
                    else
                    {
                        pickFromCamera();
                    }
                }
                else if (which == 1) // after clicking on Gallery
                {
                    //Toast.makeText(getContext(), "Profile Cover Updated", Toast.LENGTH_LONG).show();
                    if(!checkStoragePermission())
                    {
                        requestStoragePermission();
                    }
                    else
                    {
                        pickFromGallery();
                    }
                }

            }
        });
        //showing dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        //this method is when user allow or deny the persmission dialog
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
            {
                if (grantResults.length > 0)
                {
                    boolean cameraPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStoragePermissionAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraPermissionAccepted && writeStoragePermissionAccepted)
                    {
                        pickFromCamera();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Do you want to enable Camera and Storage Permission",Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            //picking from phones gallery,getting permission from user
            case STORAGE_REQUEST_CODE:
            {
                if (grantResults.length > 0)
                {
                    boolean writeStoragePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStoragePermissionAccepted)
                    {
                        pickFromGallery();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Do you want to enable Storage Permission",Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // we used this method after user pick image from camera or gallery
        if(resultCode == RESULT_OK)
        {
            if(requestCode == IMAGE_PICK_GALLERY_CODE)
            {
                //image already picked from gallery, now we are getting the URI
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                //image already picked from camera, now we are getting the URI

                uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri image_uri)
    {
        //address and name of the image that is stored in firebase
        String filePathandName = storePath+ ""+ profileandCoverPhote +"_"+ user.getUid();
        StorageReference storageReference2 = storageReference.child(filePathandName);
        storageReference2.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //uploaded image is in storage, now we are getting the address of URL
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri dowloadUri = uriTask.getResult();

                //checking if image upload is sucessful or not
                if(uriTask.isSuccessful())
                {
                    //image uploaded, adding and updating url in users database
                    HashMap<String,Object> results = new HashMap<>();

                    //First parameter --> profileandcoer that is the image or it can be the cover
                    // that are keys in user's databse and the rul of image will be saved
                    //second parameter contains the url of the image stored in firebase storage
                    //this url will be saved as value againts key image or key cover

                    results.put(profileandCoverPhote,dowloadUri.toString());
                    databaseReference.child(user.getUid()).updateChildren(results)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(getActivity(),"Image has been updated",Toast.LENGTH_LONG).show();
                            }
                        })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e)
                             {
                                 Toast.makeText(getActivity(),"Error has occured",Toast.LENGTH_LONG).show();
                             }
                         });
                }
                else
                {
                    Toast.makeText(getActivity(),"error has occured", Toast.LENGTH_LONG).show();
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void pickFromCamera()
    {
        //picking image from device Camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temperory picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temperory Description");

        // Image Uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        // Starting Camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery()
    {
        //picking image from Gallery
        Intent galleryInent = new Intent(Intent.ACTION_PICK);
        galleryInent.setType("image/*");
        startActivityForResult(galleryInent, IMAGE_PICK_GALLERY_CODE);

    }



    private void checkingUsers()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            //myProfile.setText(user.getEmail());
        }
        else
        {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true); //in order to show menu in option Fragment
        super.onCreate(savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkingUsers();
        }
        if(id == R.id.action_add_post)
        {
            startActivity(new Intent(getActivity(),AddPostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
