package com.raam.kwizking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raam.kwizking.databinding.RowLeaderboardsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.LeaderboardViewHolder>{

    Context context;
    ArrayList<User> users;


    public LeaderboardsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }


    @NotNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LeaderboardViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.name.setText(user.getName());
        holder.binding.coins.setText(String.valueOf(user.getCoins()));
        holder.binding.index.setText(String.format("#%d", position+1));

        //updating image of profile from this code
        //Glide.with(context)
               // .load(user.getProfile())
               // .into(holder.binding.imageView9);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        RowLeaderboardsBinding binding;
        public LeaderboardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);
        }
    }
}
