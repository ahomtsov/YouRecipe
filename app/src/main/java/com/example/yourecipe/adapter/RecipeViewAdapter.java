package com.example.yourecipe.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import com.example.yourecipe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewAdapter.RecycleViewHolder> {

    private List<String> recipeList = new ArrayList<>();
    private List<String> imgList = new ArrayList<>();
    private StorageReference storageRef;

    public void setItems(Collection recipes, Collection images) {
        recipeList.addAll(recipes);
        imgList.addAll(images);
        System.out.println("setItems: " + recipeList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        recipeList.clear();
        imgList.clear();
        System.out.println("clearItems: " + recipeList);
        notifyDataSetChanged();
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder: " + recipeList);

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item_view, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        System.out.println("getItemCount: " + recipeList.size());
        return recipeList.size();
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        try {
            System.out.println("onBindViewHolder recipeList: " + recipeList);
            System.out.println("onBindViewHolder imgList: " + imgList);
            //holder.bind(position, recipeList.get(position), imgList.get(position));
            holder.bind(position, recipeList.get(position), null);
        } catch (Exception ex) {
            System.out.println("ERROR IN onBindViewHolder: ");
            ex.printStackTrace();
        }
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {

        private ImageButton recipeImageButtonView;
        private TextView recipeTextView;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            System.out.println("constructor: " + recipeList);
            recipeImageButtonView = itemView.findViewById(R.id.imageButtonRecipeView);
            recipeTextView = itemView.findViewById(R.id.textViewRecipeView);
        }

        public void bind(final int position, String product, String image) throws IOException {
            System.out.println("bind: " + recipeList);
            storageRef = FirebaseStorage.getInstance().getReference();

            StorageReference imageRef = storageRef.child("recipeFirstCourse/1.jpg");

            final File localFile = File.createTempFile("images", "jpg");

            imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Uri uri = Uri.parse(Uri.decode(localFile.toString()));
                    recipeImageButtonView.setImageURI(uri);
                    recipeImageButtonView.setId(position);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("Error: " + exception.getStackTrace());
                }
            });


            recipeTextView.setText(product);
        }

    }

}