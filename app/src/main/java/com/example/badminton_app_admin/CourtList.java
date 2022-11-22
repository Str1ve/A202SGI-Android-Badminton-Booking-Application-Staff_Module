package com.example.badminton_app_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.badminton_app_admin.Common.Common;
import com.example.badminton_app_admin.Interface.ItemClickListener;
import com.example.badminton_app_admin.ViewHolder.CourtViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.example.badminton_app_admin.Model.Court;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.UUID;

public class CourtList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference courtList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";

    FirebaseRecyclerAdapter<Court, CourtViewHolder> adapter;

    FloatingActionButton fab;

    //Add New Court
    MaterialEditText edtName, edtDescription, edtPrice;
    Button btnUpload;
    ImageView imagePreview;

    Court newCourt;

    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_court_list);
        getSupportActionBar().hide();

        //Firebase
        db = FirebaseDatabase.getInstance();
        courtList = db.getReference("Courts");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Init
        recyclerView = (RecyclerView)findViewById(R.id.recycler_court);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCourtDialog();
            }
        });

        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");

        if(!categoryId.isEmpty())
            loadListCourt(categoryId);
    }

    private void showAddCourtDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CourtList.this);
        alertDialog.setTitle("Add New Court");
        alertDialog.setMessage("Enter the court name, description and upload the image URL");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_court_layout, null);

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_add_new_item_24);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        imagePreview = add_menu_layout.findViewById(R.id.image_preview);

        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        //Event for button
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();

                if(saveUri!=null){
                    if (edtName.getText().toString().isEmpty() || edtDescription.getText().toString().isEmpty() ||
                            edtPrice.getText().toString().isEmpty()) {
                        Toast.makeText(CourtList.this, "All fields must not be empty!", Toast.LENGTH_SHORT).show();
                        saveUri = null;
                    } else {
                        ProgressDialog mDialog = new ProgressDialog(CourtList.this);
                        mDialog.setMessage("Uploading...");
                        mDialog.show();

                        String imageName = UUID.randomUUID().toString();
                        StorageReference imageFolder = storageReference.child("images/"+imageName);
                        imageFolder.putFile(saveUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        mDialog.dismiss();;
                                        Toast.makeText(CourtList.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                                        imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                //set value for newCategory if image upload and we can get download link
                                                newCourt = new Court();
                                                newCourt.setName(edtName.getText().toString());
                                                newCourt.setDescription(edtDescription.getText().toString());
                                                newCourt.setPrice(edtPrice.getText().toString());
                                                newCourt.setMenuId(categoryId);
                                                newCourt.setImage(uri.toString());
                                                courtList.push().setValue(newCourt);
                                                Snackbar.make(rootLayout, "New court " + newCourt.getName() + " was added", Snackbar.LENGTH_SHORT).show();
                                                saveUri = null;
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mDialog.dismiss();
                                        Toast.makeText(CourtList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                        long progress = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                        mDialog.setMessage("Uploaded " + progress+"%");
                                    }
                                });
                    }
                } else {
                    Toast.makeText(CourtList.this,"No image was selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadListCourt(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Court, CourtViewHolder>(
                Court.class,
                R.layout.court_item,
                CourtViewHolder.class,
                courtList.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(CourtViewHolder courtViewHolder, Court court, int i) {
                courtViewHolder.court_name.setText(court.getName());
                Glide.with(getBaseContext())
                        .load(court.getImage())
                        .into(courtViewHolder.court_image);
                Court selectedCourt = court;
                courtViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent courtDetail = new Intent(CourtList.this,CourtDetail.class);
                        courtDetail.putExtra("CourtId", adapter.getRef(position).getKey());
                        startActivity(courtDetail);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            imagePreview.setImageURI(saveUri);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateCourtDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE)){
            deleteCourt(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCourt(String key) {
        courtList.child(key).removeValue();
    }

    private void showUpdateCourtDialog(String key, Court item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CourtList.this);
        alertDialog.setTitle("Edit Court");
        alertDialog.setMessage("Enter the court name, description and upload the image URL");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_court_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        imagePreview = add_menu_layout.findViewById(R.id.image_preview);

        //Set default value for view
        edtName.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());
        Glide.with(getBaseContext()).load(item.getImage()).into(imagePreview);

        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        //Event for button
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_add_new_item_24);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();

                if (saveUri != null) {
                    if (edtName.getText().toString().isEmpty() || edtDescription.getText().toString().isEmpty() ||
                            edtPrice.getText().toString().isEmpty()) {
                        Toast.makeText(CourtList.this, "All fields must not be empty!", Toast.LENGTH_SHORT).show();
                        saveUri = null;
                    } else {
                        ProgressDialog mDialog = new ProgressDialog(CourtList.this);
                        mDialog.setMessage("Uploading...");
                        mDialog.show();

                        String imageName = UUID.randomUUID().toString();
                        StorageReference imageFolder = storageReference.child("images/"+imageName);
                        imageFolder.putFile(saveUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        mDialog.dismiss();;
                                        Toast.makeText(CourtList.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                                        imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                double courtPrice = Double.parseDouble(edtPrice.getText().toString());

                                                //set value for newCategory if image upload and we can get download link
                                                item.setName(edtName.getText().toString());
                                                item.setDescription(edtDescription.getText().toString());
                                                item.setPrice(String.format("%.2f",courtPrice));

                                                item.setImage(uri.toString());
                                                courtList.child(key).setValue(item);

                                                Snackbar.make(rootLayout, "Court updated successfully!", Snackbar.LENGTH_SHORT).show();
                                                saveUri = null;
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mDialog.dismiss();
                                        Toast.makeText(CourtList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                        long progress = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                        mDialog.setMessage("Uploaded " + progress+"%");
                                    }
                                });
                    }

                } else {
                    if(edtName.getText().toString().trim().equals(item.getName()) &&
                            edtDescription.getText().toString().trim().equals(item.getDescription()) &&
                            edtPrice.getText().toString().trim().equals(item.getPrice())){
                        Toast.makeText(CourtList.this,"No changes were made", Toast.LENGTH_SHORT).show();

                    } else if (edtName.getText().toString().isEmpty() || edtDescription.getText().toString().isEmpty() ||
                            edtPrice.getText().toString().isEmpty()) {
                        Toast.makeText(CourtList.this, "All fields must not be empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        double courtPrice = Double.parseDouble(edtPrice.getText().toString());

                        item.setName(edtName.getText().toString());
                        item.setDescription(edtDescription.getText().toString());
                        item.setPrice(String.format("%.2f",courtPrice));
                        courtList.child(key).setValue(item);
                        Snackbar.make(rootLayout, "Court updated successfully!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void changeImage(final Court item) {
        if(saveUri!=null){
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();;
                            Toast.makeText(CourtList.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newCategory if image upload and we can get download link
                                    item.setImage(uri.toString());

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(CourtList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " + progress+"%");
                        }
                    });
        }
    }
}