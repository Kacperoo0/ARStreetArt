package com.example.arstreetart;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.OnGraffitiClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Graffiti> graffitiList = new ArrayList<>();
        GalleryAdapter adapter = new GalleryAdapter(graffitiList, this);
        recyclerView.setAdapter(adapter);

        // Ładowanie danych z Firestore
        FirebaseFirestore.getInstance().collection("graffiti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        graffitiList.clear();
                        task.getResult().forEach(document -> {
                            Graffiti graffiti = document.toObject(Graffiti.class);
                            graffiti.setId(document.getId());
                            graffitiList.add(graffiti);
                        });
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onGraffitiClick(Graffiti graffiti) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("bitmapBase64", graffiti.getBitmapBase64());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}