package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.CompuStore;
import com.fiuady.db.MissingProduct;
import com.fiuady.db.Product;

import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private ImageButton imageBtnFaltante;
    private ImageButton imageBtnOrdenes;
    private ImageButton imageBtnVentas;
    private CompuStore compuStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        compuStore = new CompuStore(this);

        imageBtnFaltante = (ImageButton) findViewById(R.id.idImageButtonProducto_Faltante);
        imageBtnOrdenes = (ImageButton) findViewById(R.id.idImageButtonConfirmacion_Ordenes);
        imageBtnVentas = (ImageButton) findViewById(R.id.idImageButtonResumen_Ventas);
    }

    public void onImageClickFaltante (View v) {
        Intent i = new Intent(this, MisssingProductsActivity.class);
        startActivity(i);
    }
}

