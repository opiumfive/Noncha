package com.opiumfive.noncha.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.opiumfive.noncha.AuthManager;
import com.opiumfive.noncha.DatabaseManager;
import com.opiumfive.noncha.R;
import com.opiumfive.noncha.model.Room;


public class RoomsActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<Room, RoomViewHolder> mFirebaseAdapter;
    private RecyclerView mRoomsRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        mRoomsRecyclerView = (RecyclerView) findViewById(R.id.roomsRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);

        FirebaseAuth auth = AuthManager.getInstance().getAuth();
        if (auth.getCurrentUser() == null) {
            auth.signInAnonymously();
        }

        final FirebaseDatabase database = DatabaseManager.getInstance().getDatabase();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Room, RoomViewHolder>(
                Room.class,
                R.layout.room_item,
                RoomViewHolder.class,
                database.getReference().child("rooms")) {

            @Override
            protected void populateViewHolder(RoomViewHolder viewHolder, final Room room, int position) {
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (room.mPublic) {
                            goToChat(room);
                        } else {
                            AlertDialog.Builder adb = new AlertDialog.Builder(RoomsActivity.this);
                            LinearLayout dialog_view = (LinearLayout) getLayoutInflater()
                                    .inflate(R.layout.dialog_code, null);
                            adb.setView(dialog_view);
                            final TextInputEditText codeEditText = (TextInputEditText) dialog_view.findViewById(R.id.code_edit_text);

                            adb.setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            adb.setPositiveButton(R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            String code = codeEditText.getText().toString();
                                            if (code.equals(room.mCode)) {
                                                goToChat(room);
                                                dialog.cancel();
                                            }
                                        }
                                    });
                            AlertDialog alert = adb.create();
                            alert.show();
                        }
                    }
                });
                if (room.mName != null) {
                    viewHolder.nameTextView.setText(room.mName);
                } else {
                    viewHolder.nameTextView.setVisibility(TextView.GONE);
                }
                viewHolder.publicImageView.setVisibility(room.mPublic ? View.GONE : View.VISIBLE);
            }

        };

        mRoomsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRoomsRecyclerView.setAdapter(mFirebaseAdapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RoomsActivity.this, AddRoomActivity.class));
            }
        });

        mRoomsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 ||dy<0 && fab.isShown()) {
                    fab.hide();
                }
            }
        });
    }

    private void goToChat(Room room) {
        Intent intent = new Intent(RoomsActivity.this, ChatActivity.class);
        intent.putExtra("room_id", room.mId);
        intent.putExtra("room_name", room.mName);
        intent.putExtra("room_private", !room.mPublic);
        startActivity(intent);
    }

    private static class RoomViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView publicImageView;
        View mView;

        public RoomViewHolder(View v) {
            super(v);
            mView = v;
            nameTextView = (TextView) itemView.findViewById(R.id.roomNameTextView);
            publicImageView = (ImageView) itemView.findViewById(R.id.roomPublicImageView);
        }
    }
}
