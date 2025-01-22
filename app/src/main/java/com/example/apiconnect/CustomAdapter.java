package com.example.apiconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.apiconnect.RestClient.CharacterComic;
import com.example.apiconnect.RestClient.Thumbnail;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private CharacterComic[] localDataSet;
    public Thumbnail imagen;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textView2;

        private final ImageView img;


        public TextView getTextView2() {
            return textView2;
        }

        public ImageView getImg() {
            return img;
        }

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textView = (TextView) view.findViewById(R.id.tvCharacterName);
            img = view.findViewById(R.id.imageView);
            textView2 = (TextView) view.findViewById(R.id.Description);
        }
        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(CharacterComic[] dataSet) {

        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.marvelcard, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        CharacterComic character = localDataSet[position];
        viewHolder.getTextView().setText(localDataSet[position].getName());
        viewHolder.getTextView2().setText(localDataSet[position].getDescription());
        if(character.getThumbnail() != null){
            String imageUrl = character.getThumbnail().getFullUrl();
            loadImageFromUrl(imageUrl, viewHolder.getImg());
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
    private void loadImageFromUrl(String urlString, ImageView imageView) {
        new Thread(() -> {
            try {
                InputStream input = new URL(urlString).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                imageView.post(() ->
                        imageView.setImageBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}