package com.merttoptas.bringit.Activity.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.merttoptas.bringit.Activity.Activity.LoginActivity;
import com.merttoptas.bringit.Activity.Activity.MainActivity;
import com.merttoptas.bringit.Activity.Model.User;
import com.merttoptas.bringit.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    private TextView tvIlanSayisi;
    private TextView tvMail, tvIlanlar;
    private TextView tvPhone;
    private TextView tvWebSite;
    private TextView tvLogout;
    private CircleImageView navUserPhoto;
    private TextView tvUserNameSurname;
    private ImageView ivNightMode;
    private Switch mySwitch;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ImageView ivMail,ivPhone;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor editor;
    Context mContext;
    DatabaseReference dbRef;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    Activity mActivity;




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.darktheme);
        }else {
            getActivity().setTheme(R.style.AppTheme);
        }
        View v= inflater.inflate(R.layout.fragment_account, container, false);

        tvUserNameSurname = v.findViewById(R.id.tvUserNameSurname);
        tvIlanlar = v.findViewById(R.id.tvIlanlar);
        TextView tvTasima = v.findViewById(R.id.tvTasima);
        tvIlanSayisi =v.findViewById(R.id.tvIlanSayisi);
        TextView tvTasimaSayisi = v.findViewById(R.id.tvTasimaSayisi);
        tvMail = v.findViewById(R.id.tvMail);
        tvPhone=v.findViewById(R.id.tvPhone);
        tvWebSite=v.findViewById(R.id.tvWebSite);
        ivMail = v.findViewById(R.id.ivMail);
        ivPhone = v.findViewById(R.id.ivPhone);
        navUserPhoto = v.findViewById(R.id.navOfferPhoto);
        ivNightMode = v.findViewById(R.id.ivNightMode);
        mySwitch = v.findViewById(R.id.mySwitch);
        tvLogout = v.findViewById(R.id.tvLogout);

        Button btnSetAccount = v.findViewById(R.id.btnSetAccount);

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            mySwitch.setChecked(true);

        }

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        myPrefs =getActivity().getSharedPreferences("nightMode",Context.MODE_PRIVATE );
        editor = myPrefs.edit();
        mySwitch.setChecked(myPrefs.getBoolean("nightOpen", false)
        );
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("nightOpen", true);

                }else{
                    editor.putBoolean("nightOpen", false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                editor.apply();
            }
        });

        logOut();

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        setAccountUser();
        updateUser();
        btnSetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialog();
            }
        });

        navUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openImage();
            }
        });

        return v;

    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.yukleme));
        pd.show();

        if(imageUri !=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw  task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri =task.getResult();
                        String mUri = downloadUri.toString();

                        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        dbRef.updateChildren(map);
                        pd.dismiss();
                    }else {
                        Toast.makeText(getContext(), R.string.hata, Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        }else {
            Toast.makeText(getContext(), R.string.fotograf_secilmedi , Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                        && data !=null && data.getData() !=null){
            imageUri =data.getData();

            if (uploadTask !=null && uploadTask.isInProgress() ) {
                Toast.makeText(getContext(), R.string.yukleme_devam, Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }
        }
    }

    private void createDialog(){
        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.custom_dialog_style1);

        final EditText etMail = d.findViewById(R.id.etMailAdress);
        final EditText etPhoneNo = d.findViewById(R.id.etPhoneNo);
        final EditText etWebSite = d.findViewById(R.id.etUserWebSite);

        // dialog u var firebaseden gelen verilerle doldurduk

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(tvMail.getText().toString().matches("")){
            etMail.setText(currentUser.getEmail());

        }else {
            etMail.setText(myPrefs.getString("email",""));
        }
        etPhoneNo.setText(myPrefs.getString("phone",""));
        etWebSite.setText(myPrefs.getString("webSite",""));

        Button btnPositive = d.findViewById(R.id.btnSave_1);
        Button btnNegative = d.findViewById(R.id.btnCikis_2);
        btnPositive.setText(getString(R.string.kaydet));
        btnNegative.setText(getString(R.string.kapat));


        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvMail.setText(etMail.getText().toString());
                tvPhone.setText(etPhoneNo.getText().toString());
                tvWebSite.setText(etWebSite.getText().toString());

                myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                editor =myPrefs.edit();

                editor.putString("email", etMail.getText().toString());
                editor.putString("phone", etPhoneNo.getText().toString());
                editor.putString("webSite", etWebSite.getText().toString());
                editor.apply();

                d.dismiss();

            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.setCancelable(false); // Boş bir kısıma tıklanınca kapatamasın.
        //dismiss ederse kapansın
        d.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Log.d("dbCurrentUser", "currentUser" + currentUser.getDisplayName() +currentUser.getEmail());

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void updateUser(){
        try {

            if(tvMail.getText().toString().matches("")){
                tvMail.setText(currentUser.getEmail());

            }else{
                tvMail.setText(currentUser.getEmail());

            }
            if(tvPhone.getText().toString().matches("")){

                tvPhone.setText(currentUser.getPhoneNumber());

            }else {
                Log.d("tvPhone", tvPhone.getText().toString());
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setAccountUser(){

        currentUser = mAuth.getCurrentUser();

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(tvMail.getText().toString().matches("")){

            Log.d("tvMail", tvMail.getText().toString());

        }else {
            tvMail.setText(myPrefs.getString("email", ""));
        }
        if (tvPhone.getText().toString().matches("")){

            tvPhone.setText(currentUser.getPhoneNumber());
        }else {
            tvPhone.setText(myPrefs.getString("phone", ""));
        }
        if(tvUserNameSurname.getText().toString().matches("")){
            tvUserNameSurname.setText(myPrefs.getString("username",""));
        }
        tvWebSite.setText(myPrefs.getString("webSite", ""));


        dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        try {
            myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        if(isAdded()){
                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;
                            editor =myPrefs.edit();
                            tvUserNameSurname.setText(user.getUsername());
                            tvIlanSayisi.setText(user.getLeads());
                            editor.putString("username", tvUserNameSurname.getText().toString());
                            editor.apply();
                            if(user.getImageURL().isEmpty()){
                                Log.d("ImageUrl", "ImgURl: " + user.getImageURL());
                            }else {
                                if(user.getImageURL().equals("default")){
                                    navUserPhoto.setImageResource(R.drawable.userphoto);
                                }else{
                                    String userImg=user.getImageURL();
                                    Log.d("ImageUrl", "ImgURl: " + userImg);
                                    Glide.with(getActivity()).load(userImg).centerCrop().circleCrop().into(navUserPhoto);
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext =null;
        mActivity =null;


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void logOut(){

        //logout
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                    Toast.makeText(getContext(), R.string.cikis_yapildi, Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}