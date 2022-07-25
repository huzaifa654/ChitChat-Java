package com.example.chitchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chitchat.Adapters.ChatAdapter;
import com.example.chitchat.Models.MessagesModel;
import com.example.chitchat.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final String senderId= FirebaseAuth.getInstance().getUid();
        binding.chatUsername.setText("Friends Group");
        final ChatAdapter adapter = new ChatAdapter(messagesModels, this);
        binding.chatDetailRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatDetailRecyclerView.setLayoutManager(layoutManager);

        database.getReference("Group Chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         messagesModels.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                            messagesModels.add(model);

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(v -> {

            if (binding.etMessage.getText().toString().isEmpty()){
                binding.etMessage.setError("Enter Message");
                return;
            }
            final String message = binding.etMessage.getText().toString();
            final MessagesModel model = new MessagesModel(senderId, message);
            model.setTimeStamp(new Date().getTime());
            binding.etMessage.setText("");
            database.getReference().child("Group Chat")
                    .push()
                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

        });
    }
}