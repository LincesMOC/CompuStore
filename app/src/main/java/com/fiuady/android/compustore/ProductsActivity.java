package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
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


public class ProductsActivity extends AppCompatActivity {

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
                    final PopupMenu popup = new PopupMenu(ProductsActivity.this, txtDescription);
                    popup.getMenuInflater().inflate(R.menu.option3_menu, popup.getMenu());

                    if (compuStore.deleteProduct(product.getId(), false)) {
                        popup.getMenu().removeItem(R.id.menu_item2);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().toString().equalsIgnoreCase("Modificar")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_addproduct, null);
                                TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
                                txtTitle.setText("Modificar producto"); // aqui cambiar por string de categories

                                final EditText txtdescripcion = (EditText) view.findViewById(R.id.add_txtdesc);
                                final EditText txtprecio = (EditText) view.findViewById(R.id.add_txtprecio);
                                final Spinner txtcateg = (Spinner)view.findViewById(R.id.addtxtcateg);
                                ArrayAdapter<String> adapter1= new ArrayAdapter<String>(ProductsActivity.this, android.R.layout.simple_spinner_dropdown_item);
                                txtcateg.setAdapter(adapter1);
                                for(Category category :categories){
                                    adapter1.add(category.getDescription());
                                }


                                builder.setCancelable(false);
                                builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        boolean comprecio = false;
                                        try{
                                            Integer.parseInt(txtprecio.getText().toString());
                                        }catch (NumberFormatException nfe){
                                            comprecio = true;
                                        }

                                        if(comprecio){
                                            dialog.dismiss();
                                            Toast.makeText(ProductsActivity.this, R.string.error_msg1, Toast.LENGTH_SHORT).show();
                                        }else {

                                            AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                                            build.setCancelable(false);
                                            build.setTitle("Modificar producto");   // aqui cambiar por string de categories
                                            build.setMessage(R.string.sure_text);

                                            build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    List<Category> categories = compuStore.getAllCategories();

                                                    if (compuStore.updateProduct(txtdescripcion.getText().toString(),product.getId(),categories.get(txtcateg.getSelectedItemPosition()).getId(),Integer.parseInt(txtprecio.getText().toString()),product.getQuantity())) {
                                                        Toast.makeText(ProductsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                                                        adapter = new ProductsActivity.ProductAdapter(compuStore.getAllProducts());
                                                        productRV.setAdapter(adapter);
                                                    } else {
                                                        Toast.makeText(ProductsActivity.this, R.string.error_msg2, Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                            build.create().show();
                                        }
                                    }
                                });
                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            if (item.getTitle().toString().equalsIgnoreCase("Eliminar")) {
                                AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                                build.setCancelable(false);
                                build.setTitle("Eliminar un producto");
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(ProductsActivity.this, R.string.dlt_msg, Toast.LENGTH_SHORT).show();
                                        compuStore.deleteProduct(product.getId(), true);
                                        adapter = new ProductsActivity.ProductAdapter(compuStore.getAllProducts());
                                        productRV.setAdapter(adapter);
                                    }
                                });

                                build.create().show();
                            }

                            if (item.getTitle().toString().equalsIgnoreCase("Agregar stock")) {
                                AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                                build.setCancelable(false);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_addstock, null);
                                build.setTitle("Agregar stock");
                                final Spinner spinstock = (Spinner)view.findViewById(R.id.spinnerstock);
                                ArrayAdapter<String> adapter2= new ArrayAdapter<String>(ProductsActivity.this,android.R.layout.simple_spinner_dropdown_item);
                                //funcion para agregar al adapter numeros arriba del valor de stock actual
                                int stock = compuStore.getProductStock(product.getId());
                                for(int i=stock;i<stock+30;i++){
                                    adapter2.add(Integer.toString(i));
                                }
                                spinstock.setAdapter(adapter2);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (compuStore.updateProduct(product.getDescription(),product.getId(),product.getCategory_id(),product.getPrice(),Integer.parseInt(spinstock.getSelectedItem().toString()))) {//HACER FUNCION ACTUALIZAR STOCK PARA EVITAR COMPROBACIONES
                                            Toast.makeText(ProductsActivity.this,"El valor fue actualizado", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProductsActivity.this, R.string.error_msg2, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                build.setView(view);
                                AlertDialog dialog = build.create();
                                dialog.show();
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

    private class ProductAdapter extends RecyclerView.Adapter<ProductsActivity.ProductHolder> {

        private List<Product> products;

        public ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @Override
        public ProductsActivity.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ProductsActivity.ProductHolder(view);
        }
        @Override
        public void onBindViewHolder(ProductsActivity.ProductHolder holder, int position) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        categorispinner = (Spinner)findViewById(R.id.spinnercategories);
        texto = (EditText) findViewById(R.id.edittextdescripcion);
        //txtcateg = (Spinner) findViewById(R.id.add_txtcateg);
        compuStore = new CompuStore(getApplicationContext());

        productRV = (RecyclerView) findViewById(R.id.recyclerviewproductos);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(new ArrayList<Product>());
        productRV.setAdapter(adapter);


        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        //ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        categorispinner.setAdapter(adapter);
        //txtcateg.setAdapter(adapter);

        adapter.add("Todos");
        categories = compuStore.getAllCategoriesid();
        for(Category category :categories){
            adapter.add(category.getDescription());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final View view = getLayoutInflater().inflate(R.layout.dialog_addproduct, null);
        final EditText txtdescripcion = (EditText) view.findViewById(R.id.add_txtdesc);
        final EditText txtprecio = (EditText) view.findViewById(R.id.add_txtprecio);

        //spinner----------------------------------
        final Spinner txtcateg = (Spinner)view.findViewById(R.id.addtxtcateg);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        txtcateg.setAdapter(adapter1);
        for(Category category :categories){
            adapter1.add(category.getDescription());
        }
        //------------------

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
               dialog.dismiss();
           }
        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                boolean comprecio = false;
                try{
                   Integer.parseInt(txtprecio.getText().toString());
                }catch (NumberFormatException nfe){
                    comprecio = true;
                }

                if(comprecio){
                    dialog.dismiss();
                    Toast.makeText(ProductsActivity.this, R.string.error_msg1, Toast.LENGTH_SHORT).show();
                }else{

                AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                build.setCancelable(false);
                build.setTitle(getString(R.string.product_add));
                build.setMessage(R.string.sure_text);
                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Toast.makeText(ProductsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        List<Category> categories = compuStore.getAllCategories();

                        if (compuStore.insertProduct(txtdescripcion.getText().toString(),categories.get(txtcateg.getSelectedItemPosition()).getId(),Integer.parseInt(txtprecio.getText().toString()),0)) {
                            Toast.makeText(ProductsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                            adapter = new ProductsActivity.ProductAdapter(compuStore.getAllProducts());
                            productRV.setAdapter(adapter);
                        } else {
                            Toast.makeText(ProductsActivity.this, R.string.error_msg2, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                build.create().show();

                }
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        return super.onOptionsItemSelected(item);
    }

    public void onSearchClick(View v){
        adapter = new ProductsActivity.ProductAdapter(compuStore.filterProducts(categorispinner.getSelectedItemPosition()-1,texto.getText().toString()));
        productRV.setAdapter(adapter);
    }
}
