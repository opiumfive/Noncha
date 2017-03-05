package com.opiumfive.noncha.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.opiumfive.noncha.utils.CryptUtils;
import com.opiumfive.noncha.managers.DatabaseManager;
import com.opiumfive.noncha.R;
import com.opiumfive.noncha.utils.SimpleTextWatcher;
import com.opiumfive.noncha.model.Message;
import com.opiumfive.noncha.model.Room;

public class AddRoomActivity extends BaseActivity {

    private static final String DEFAULT_START_MESSAGE = "Hello";

    private CheckBox mPrivateCheckBox;
    private Button mCreateRoomButton;
    private TextInputEditText mRoomNameEditText;
    private TextInputEditText mRoomDescEditText;
    private TextInputEditText mCodeEditText;
    private View mCodeView;
    private View mProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        mPrivateCheckBox = (CheckBox) findViewById(R.id.private_check_box);
        mCreateRoomButton = (Button) findViewById(R.id.add_room_button);
        mRoomNameEditText = (TextInputEditText) findViewById(R.id.name_edit_text);
        mRoomDescEditText = (TextInputEditText) findViewById(R.id.desc_edit_text);
        mCodeEditText = (TextInputEditText) findViewById(R.id.code_edit_text);
        mCodeView = findViewById(R.id.code_view);
        mProgressView = findViewById(R.id.empty_view);

        mPrivateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCodeView.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                if (!isChecked) mCodeEditText.setText("");
                validate();
            }
        });

        mRoomNameEditText.addTextChangedListener(mTextWatcher);
        mCodeEditText.addTextChangedListener(mTextWatcher);

        final FirebaseDatabase database = DatabaseManager.getInstance().getDatabase();

        mCreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableUI(false);
                String name = mRoomNameEditText.getText().toString();
                String code = mCodeEditText.getText().toString();
                final Room room = new Room(name, code.isEmpty() ? code : CryptUtils.StringToMD5(code));
                String desc = mRoomDescEditText.getText().toString();
                if (desc.isEmpty()) desc = DEFAULT_START_MESSAGE;
                final Task<Void> task1 = database.getReference().child("room-" + room.mId).push().setValue(new Message(CryptUtils.encryptString(desc))).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            database.getReference().child("rooms").push().setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    enableUI(true);
                                    if (task.isSuccessful()) {
                                        finish();
                                        goToChat(room);
                                    } else {
                                        Toast.makeText(AddRoomActivity.this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddRoomActivity.this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
                            enableUI(true);
                        }
                    }
                });

            }
        });
    }

    private void enableUI(boolean b) {
        mProgressView.setVisibility(!b ? View.VISIBLE : View.GONE);
        mCreateRoomButton.setEnabled(b);
    }

    TextWatcher mTextWatcher = new SimpleTextWatcher(new SimpleTextWatcher.OnTextChanged() {
        @Override
        public void onTextChanged() {
            validate();
        }
    });

    private void goToChat(Room room) {
        Intent intent = new Intent(AddRoomActivity.this, ChatActivity.class);
        intent.putExtra("room_id", room.mId);
        intent.putExtra("room_name", room.mName);
        intent.putExtra("room_private", !room.mPublic);
        startActivity(intent);
    }

    private void validate() {
        boolean isChecked = mPrivateCheckBox.isChecked();
        String name = mRoomNameEditText.getText().toString();
        String code = mCodeEditText.getText().toString();
        boolean allOk = name.length() > 0 && (code.length() > 0 || !isChecked);
        mCreateRoomButton.setEnabled(allOk);
    }
}
