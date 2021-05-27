package com.example.asyncgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GuessAdapter extends RecyclerView.Adapter<GuessAdapter.ViewHolder> {
    private ArrayList<GuessPage.HintInfo> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView hintTextView;
        private final TextView userTextView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            hintTextView = (TextView) view.findViewById(R.id.hint);
            userTextView = (TextView) view.findViewById(R.id.hintGiver);
        }

        public TextView getHintTextView() {
            return hintTextView;
        }

        public TextView getUserTextView() {
            return userTextView;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public GuessAdapter(ArrayList<GuessPage.HintInfo> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.guess_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getHintTextView().setText(localDataSet.get(position).hint);
        viewHolder.getUserTextView().setText(localDataSet.get(position).user);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
