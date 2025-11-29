package com.example.assignment1tripplanning;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<Place> places;

    public PlaceAdapter(List<Place> places) {
        this.places = places;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place p = places.get(position);

        holder.tvFromTo.setText(p.getFrom() + " → " + p.getTo());
        holder.tvCategory.setText(p.getCategory());
        holder.tvDateTime.setText(p.getDate() + " at " + p.getTime());
        holder.tvImportant.setText(p.isImportant() ? "Important ✓" : "Not Important");

        switch (p.getCategory()) {
            case "Beach":
                holder.imgCategory.setImageResource(R.drawable.beach);
                break;
            case "City Tour":
                holder.imgCategory.setImageResource(R.drawable.citytour);
                break;
            case "Adventure":
                holder.imgCategory.setImageResource(R.drawable.adventure);
                break;
            default:
                holder.imgCategory.setImageResource(R.drawable.historical);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), ViewPlaceActivity.class);
            i.putExtra("from", p.getFrom());
            i.putExtra("to", p.getTo());
            i.putExtra("date", p.getDate());
            i.putExtra("time", p.getTime());
            i.putExtra("category", p.getCategory());
            i.putExtra("important", p.isImportant());
            i.putExtra("index", position);
            v.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView tvFromTo, tvCategory, tvDateTime, tvImportant;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvFromTo = itemView.findViewById(R.id.tvFromTo);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvImportant = itemView.findViewById(R.id.tvImportant);
        }
    }
}
