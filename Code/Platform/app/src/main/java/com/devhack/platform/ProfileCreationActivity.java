package com.devhack.platform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileCreationActivity extends AppCompatActivity {
    TextInputEditText name,prof,email,pass;
    AutoCompleteTextView interests;
    Button signUp,addImage;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView login;

    String[] list=new String[]{"Engineering","Medical","Humanities","Law"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);
        name=findViewById(R.id.name);
        prof=findViewById(R.id.about);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        interests=findViewById(R.id.interest);
        signUp=findViewById(R.id.signUp);
        login=findViewById(R.id.login);
        addImage=findViewById(R.id.addImage);

        //Initializing FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<String>adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,list);
        interests.setAdapter(adapter);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email=email.getText().toString().trim();
                final String Pass=pass.getText().toString();
                final String Name=name.getText().toString();
                final String Interest=interests.getText().toString();
                final String Prof=prof.getText().toString();
                if(!Email.equals("") || !Pass.equals("")||!Name.equals("")|| !Interest.equals("")||!Prof.equals("")){
                    mAuth.createUserWithEmailAndPassword(Email,Pass)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                         while(user==null);
                                        createUser(user,Email,Pass,Name,Interest,Prof);
                                    } else {
                                        Toast.makeText(ProfileCreationActivity.this, ""+task, Toast.LENGTH_SHORT).show();
                                        email.setText("");
                                        pass.setText("");
                                        interests.setText("");
                                        prof.setText("");
                                        name.setText("");
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(ProfileCreationActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileCreationActivity.this,MainActivity.class));
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }




    public void createUser(FirebaseUser user,String Email,String Pass,String name,String interest,String prof ){
        Map<String, String> params = new HashMap<>();
        params.put("uid", user.getUid());
        params.put("email",Email);
        params.put("pass",Pass);
        params.put("name",name);
        params.put("interest",interest);
        params.put("prof",prof);

        db.collection("users")
                .document(user.getUid())
                .set(params)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(ProfileCreationActivity.this,HomePageActivity.class));
                        email.setText("");
                        pass.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileCreationActivity.this, "Error:"+e, Toast.LENGTH_LONG).show();
                    }
                });

    }
}
