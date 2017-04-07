package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ReportsActivity extends AppCompatActivity {

    private ImageButton imageBtnFaltante;
    private ImageButton imageBtnOrdenes;
    private ImageButton imageBtnVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        imageBtnFaltante = (ImageButton) findViewById(R.id.idImageButtonProducto_Faltante);
        imageBtnOrdenes = (ImageButton) findViewById(R.id.idImageButtonConfirmacion_Ordenes);
        imageBtnVentas = (ImageButton) findViewById(R.id.idImageButtonResumen_Ventas);
    }

    public void onImageClickFaltante(View V) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
        TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
        final EditText txtAdd = (EditText) view.findViewById(R.id.add_text);
        txtTitle.setText(R.string.category_add);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog.Builder build = new AlertDialog.Builder(ReportsActivity.this);
                build.setCancelable(false);
                build.setTitle(getString(R.string.category_add));
                build.setMessage(R.string.sure_text);

                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                build.create().show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
