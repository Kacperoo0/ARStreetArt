package com.example.arstreetart;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.ar.core.Anchor;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;
    private DrawingView drawingView;
    private int currentColor = Color.RED;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupColorButtons();
        setupARScene();
    }

    private void setupColorButtons() {
        Button btnRed = findViewById(R.id.btn_red);
        Button btnBlue = findViewById(R.id.btn_blue);
        Button btnGreen = findViewById(R.id.btn_green);

        btnRed.setOnClickListener(v -> currentColor = Color.RED);
        btnBlue.setOnClickListener(v -> currentColor = Color.BLUE);
        btnGreen.setOnClickListener(v -> currentColor = Color.GREEN);
    }

    private void setupARScene() {
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            if (plane.getType() != Plane.Type.VERTICAL) {
                Toast.makeText(this, "Znajdź pionową powierzchnię", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Żądaj uprawnień jeśli nie są przyznane
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE
                );
                return;
            }

            // Pobierz lokalizację i utwórz powierzchnię do rysowania
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            createDrawingSurface(hitResult.createAnchor(), location);
                        } else {
                            Toast.makeText(this, "Nie można pobrać lokalizacji", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Błąd lokalizacji: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void createDrawingSurface(Anchor anchor, Location location) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Użyj TransformableNode zamiast BaseTransformableNode
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);

        drawingView = new DrawingView(this);
        drawingView.setBackgroundColor(Color.TRANSPARENT);
        drawingView.setDrawingColor(currentColor);

        // Konwersja DrawingView na obiekt AR
        ViewRenderable.builder()
                .setView(this, drawingView)
                .build()
                .thenAccept(renderable -> {
                    node.setRenderable(renderable);
                    node.select();
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Błąd ładowania obiektu AR", Toast.LENGTH_SHORT).show();
                    return null;
                });

        saveGraffitiLocation(anchor, location);
    }

    private void saveGraffitiLocation(Anchor anchor, Location location) {
        Map<String, Object> graffitiData = new HashMap<>();
        graffitiData.put("latitude", location.getLatitude());
        graffitiData.put("longitude", location.getLongitude());
        graffitiData.put("color", currentColor);

        db.collection("graffiti")
                .add(graffitiData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Graffiti zapisane!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Błąd zapisu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Uprawnienia przyznane", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Uprawnienia odrzucone - niektóre funkcje mogą nie działać", Toast.LENGTH_SHORT).show();
            }
        }
    }
}