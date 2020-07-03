package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    List<User> users;
    Context context;

    public FollowAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = LayoutInflater.from(context).inflate(R.layout.item_follow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName = itemView.findViewById(R.id.tvName);
        TextView tvHandle = itemView.findViewById(R.id.tvHandle);
        ImageView ivProfileImage2 = itemView.findViewById(R.id.ivProfileImage2);


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(User user) {

            tvName.setText(user.getName());
            tvHandle.setText(user.getHandle());
            Glide.with(context)
                    .load(user.getProfileImageUrl())
                    .fitCenter()
                    .circleCrop()
                    .into(ivProfileImage2);
        }
    }
}
