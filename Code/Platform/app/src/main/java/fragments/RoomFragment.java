package fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devhack.platform.GroupchatActivity;
import com.devhack.platform.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RoomFragment extends Fragment {
    ImageView creatRoom;
    private DatabaseReference rootref,groupref;
    private View grpview;
    private ListView list_view;
    private ArrayAdapter<String> arrayadapter;
    private ArrayList<String> listofgrps=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        grpview= inflater.inflate(R.layout.fragment_room, container, false);
        creatRoom = grpview.findViewById(R.id.createroom);
        rootref = FirebaseDatabase.getInstance().getReference();
        groupref= FirebaseDatabase.getInstance().getReference().child("Rooms");
        initializefileds(view);
        rootref.child("Rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getContext(), "Got some Rooms", Toast.LENGTH_LONG).show();
                    retriveanddisplaygroup();
                } else {
                    creatRoom.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "No Rooms", Toast.LENGTH_LONG).show();
                    creatRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         requestnewroom();
                        }


                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                 String currentgrpname = parent.getItemAtPosition(position).toString();
                                                 Intent grpchatintent = new Intent(getContext(), GroupchatActivity.class);
                                                 grpchatintent.putExtra("groupname", currentgrpname);
                                                 startActivity(grpchatintent);
                                             }
                                         });

                return grpview;
            }

            private void requestnewroom() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
                builder.setTitle("Enter Group Name");
                final EditText grpname = new EditText(getContext());
                grpname.setHint(" eg.What is Newtons 1st Law");
                builder.setView(grpname);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String grp = grpname.getText().toString();
                        if (TextUtils.isEmpty(grp)) {
                            Toast.makeText(getContext(), "Please enter a group name", Toast.LENGTH_LONG).show();
                        } else {

                            createnewroom(grp);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                    }


            private void createnewroom(final String grp) {
                rootref.child("Rooms").child(grp).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            creatRoom.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), grp + "Created successfully !!", Toast.LENGTH_LONG).show();
                            retriveanddisplaygroup();
                        }

                    }
                });
            }
    private void initializefileds(View view) {
        list_view=view.findViewById(R.id.list_view);
        arrayadapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listofgrps);
        list_view.setAdapter(arrayadapter);


    }
    private void retriveanddisplaygroup() {
        rootref.child("Rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();
                while(iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                listofgrps.clear();
                listofgrps.addAll(set);
                arrayadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

