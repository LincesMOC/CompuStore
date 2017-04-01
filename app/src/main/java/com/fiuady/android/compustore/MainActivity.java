package com.fiuady.android.compustore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageButton) findViewById(R.id.idImageButton);
    }

    public void onImageClick (View v) {
        Intent i = new Intent(this, CategoriesActivity.class);
        startActivity(i);

        //otro comentario raro

    }
}
