package fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devhack.platform.MainActivity;
import com.devhack.platform.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private Uri pdfUri;
    private TextView textView;
    private ImageButton attach_btn,submit_btn;

    public PostFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        attach_btn = view.findViewById(R.id.attachbtn);
        submit_btn = view.findViewById(R.id.submitbtn);
        attach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check permission first
                if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // request the permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
                else {
                    // has the permission.
                    getFile();
                }

            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfUri!=null)
                    uploadFile(pdfUri);
            }
        });
        return  view;
    }
    private void uploadFile(Uri pdfUri){
        StorageReference storageReference = firebaseStorage.getReference();
        String filename = System.currentTimeMillis()+"";
        storageReference.child("Files").child(filename).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }
    private  void uploadPost() {
        Map<String, Object> post = new HashMap<>();
        post.put("File", "");
        post.put("Genre", "");
        post.put("Image", "");
        post.put("Seminar","");
        post.put("Title", "");
        post.put("Video", "");
        post.put("date", "");
        post.put("time","");
        post.put("timestamp", "");
        post.put("user", "");


        firebaseFirestore.collection("Feed").document("LA")
                .set(post);
        /*
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.w(TAG, "Error writing document", e);
                    }
                });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if((requestCode == 9) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            getFile();
        }

    }

    private void getFile()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,10);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //whether user has selected a file or not
        if(requestCode==10 && resultCode==RESULT_OK && data!=null) {
            pdfUri=data.getData();
        }
    }




}
