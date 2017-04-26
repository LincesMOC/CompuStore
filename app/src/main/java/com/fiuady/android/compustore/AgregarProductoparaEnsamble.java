package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.ArrayList;
import java.util.List;

public class AgregarProductoparaEnsamble extends AppCompatActivity {

    //Variables para landscape..
    private final String BUSCARPRESSED = "buscarpressed";
    private final String INDEXDESPINNER = "indexdespinner";
    private final String STRINGTEXTO = "stringtexto";
    private final String STRINGACTUAL = "stringactual";
    private boolean buscarpressed;

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView txtDescription;

        public ProductHolder(View itemView) {
            super(itemView);
            txtDescription = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bindCategory(final Product product) {
            txtDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(AgregarProductoparaEnsamble.this, txtDescription);
                    popup.getMenuInflater().inflate(R.menu.option1_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().toString().equalsIgnoreCase("Agregar")) {
                                Intent i = new Intent(); //intent para responder con producto a agregar
                                i.putExtra("Productid",product.getId());
                                setResult(2,i);
                                finish();
                            }

                            return true;
                        }
                    });
                    popup.show();
                }
            });
            txtDescription.setText(product.getDescription());
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<AgregarProductoparaEnsamble.ProductHolder> {

        private List<Product> products;

        public ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @Override
        public AgregarProductoparaEnsamble.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new AgregarProductoparaEnsamble.ProductHolder(view);
        }
        @Override
        public void onBindViewHolder(AgregarProductoparaEnsamble.ProductHolder holder, int position) {
            holder.bindCategory(products.get(position));
        }
        @Override
        public int getItemCount() {
            return products.size();
        }
    }

    private ProductAdapter adapter;
    private RecyclerView productRV;
    private Spinner categorispinner;
    private CompuStore compuStore;
    private List<Category> categories;
    private EditText texto;

    private String stringquesebusco = "";
    private String textoactual;
    private int indice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_productopara_ensamble);

        categorispinner = (Spinner)findViewById(R.id.spinnercategories);
        texto = (EditText) findViewById(R.id.edittextdescripcion);
        //txtcateg = (Spinner) findViewById(R.id.add_txtcateg);
        compuStore = new CompuStore(getApplicationContext());

        productRV = (RecyclerView) findViewById(R.id.recyclerviewproductos);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AgregarProductoparaEnsamble.ProductAdapter(new ArrayList<Product>());
        productRV.setAdapter(adapter);


        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        //ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        categorispinner.setAdapter(adapter1);
        //txtcateg.setAdapter(adapter);

        adapter1.add("Todos");
        categories = compuStore.getAllCategoriesid();
        for(Category category :categories){
            adapter1.add(category.getDescription());
        }

        if(savedInstanceState != null){
            buscarpressed = savedInstanceState.getBoolean(BUSCARPRESSED);
            textoactual = savedInstanceState.getString(STRINGACTUAL);
            indice = savedInstanceState.getInt(INDEXDESPINNER);
            stringquesebusco = savedInstanceState.getString(STRINGTEXTO);

            categorispinner.setSelection(indice);
            texto.setText(textoactual);

            if(buscarpressed){
                //Llenar lista si se presiono buscar..
                adapter = new AgregarProductoparaEnsamble.ProductAdapter(compuStore.filterProducts(categorispinner.getSelectedItemPosition()-1,texto.getText().toString()));
                buscarpressed = true;
                stringquesebusco = texto.getText().toString();
                productRV.setAdapter(adapter);
                //Toast.makeText(this, "Se habia presionado falta llenar rv", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onSearchClick(View v){
        adapter = new AgregarProductoparaEnsamble.ProductAdapter(compuStore.filterProducts(categorispinner.getSelectedItemPosition()-1,texto.getText().toString()));
        buscarpressed = true;
        stringquesebusco = texto.getText().toString();
        productRV.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INDEXDESPINNER,categorispinner.getSelectedItemPosition());//
        outState.putString(STRINGACTUAL,texto.getText().toString()); //
        outState.putString(STRINGTEXTO,stringquesebusco);
        outState.putBoolean(BUSCARPRESSED,buscarpressed);
    }
}
