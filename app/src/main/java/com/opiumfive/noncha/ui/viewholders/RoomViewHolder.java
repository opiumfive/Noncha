package com.opiumfive.noncha.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.opiumfive.noncha.R;

public class RoomViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public ImageView publicImageView;
    public View mView;

    public RoomViewHolder(View v) {
        super(v);
        mView = v;
        nameTextView = (TextView) itemView.findViewById(R.id.roomNameTextView);
        publicImageView = (ImageView) itemView.findViewById(R.id.roomPublicImageView);
    }
}