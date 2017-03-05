package com.opiumfive.noncha.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.opiumfive.noncha.utils.CryptUtils;
import com.opiumfive.noncha.managers.DatabaseManager;
import com.opiumfive.noncha.R;
import com.opiumfive.noncha.model.Message;

public class ChatActivity extends BaseActivity {

    public final static int MSG_LEN_MAX = 100;

    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private View mSendView;
    private EditText mTextEditText;
    private TextView mRoomNameTextView;
    private ImageView mPrivateRoomImageView;

    private String mRoomId;
    private String mRoomName;
    private boolean mRoomPrivate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        if (intent != null) {
            mRoomId = intent.getStringExtra("room_id");
            mRoomName = intent.getStringExtra("room_name");
            mRoomPrivate = intent.getBooleanExtra("room_private", false);
        } else {
            mRoomId = "";
            mRoomName = "";
        }

        mSendView = findViewById(R.id.sendButton);
        mTextEditText = (EditText) findViewById(R.id.messageEditText);
        mPrivateRoomImageView = (ImageView) findViewById(R.id.roomPublicImageView);
        mRoomNameTextView = (TextView) findViewById(R.id.roomNameTextView);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRoomNameTextView.setText(mRoomName);
        mPrivateRoomImageView.setVisibility(mRoomPrivate ? View.VISIBLE : View.GONE);

        mTextEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MSG_LEN_MAX)});

        final FirebaseDatabase database = DatabaseManager.getInstance().getDatabase();

        final String roomConnector = "room-" + mRoomId;

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.message_item,
                MessageViewHolder.class,
                database.getReference().child(roomConnector)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message message, int position) {
                String decString = CryptUtils.decryptString(message.getText());
                viewHolder.messageTextView.setText(decString);
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mTextEditText.getText().toString();
                if (!text.isEmpty()) {
                    String encString = CryptUtils.encryptString(text);
                    database.getReference().child(roomConnector).push().setValue(new Message(encString));
                    mTextEditText.setText("");
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
        }
    }
}
