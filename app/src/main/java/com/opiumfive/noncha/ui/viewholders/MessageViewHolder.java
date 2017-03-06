package com.opiumfive.noncha.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.opiumfive.noncha.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView messageTextView;

    public MessageViewHolder(View v) {
        super(v);
        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
    }
}
