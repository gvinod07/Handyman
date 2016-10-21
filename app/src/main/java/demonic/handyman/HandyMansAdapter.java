package demonic.handyman;

/**
 * Created by demonic on 13/10/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import demonic.handyman.HandyMan;
import demonic.handyman.HandyMan_view;
import demonic.handyman.R;

public class HandyMansAdapter extends
        RecyclerView.Adapter<HandyMansAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView distanceTextView;
        public RatingBar ratingBar;
        public TextView ratingTextView;
        public TextView idtext;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.Name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            distanceTextView = (TextView) itemView.findViewById(R.id.dist);
            ratingTextView = (TextView) itemView.findViewById(R.id.rates);
            idtext = (TextView) itemView.findViewById(R.id.idtext);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(itemView.getContext(), HandyMan_view.class);
            intent.putExtra("Id", idtext.getText().toString());
            itemView.getContext().startActivity(intent);
        }
    }

    private List<HandyMan> mHandyMans;

    // Pass in the contact array into the constructor
    public HandyMansAdapter(List<HandyMan> HandyMans) {
        mHandyMans = HandyMans;
    }

    @Override
    public HandyMansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.handymanbadge, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(HandyMansAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final HandyMan contact = mHandyMans.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(contact.getName());

        RatingBar button = viewHolder.ratingBar;
        button.setRating((float) contact.getRating() + 1);

        TextView textView1 = viewHolder.distanceTextView;
        textView1.setText(String.valueOf(contact.getDist() / 10000) + " km");

        TextView textView2 = viewHolder.ratingTextView;
        textView2.setText("(" + contact.getrates() + ") ratings");

        TextView textView3 = viewHolder.idtext;
        textView3.setText(String.valueOf(contact.getId()));
    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        return mHandyMans.size();
    }

}
