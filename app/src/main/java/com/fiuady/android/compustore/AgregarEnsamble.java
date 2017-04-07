package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.ArrayList;
import java.util.List;

public class AgregarEnsamble extends AppCompatActivity {

    private RecyclerView productRV;
    private ProductAdapter adapter;
    private CompuStore compuStore;
    private List<Category> categories;
    private EditText texto;
    private ArrayList<Product> products;

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
                    final PopupMenu popup = new PopupMenu(AgregarEnsamble.this, txtDescription);
                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

//                    if (compuStore.deleteProduct(product.getId(), false)) {
//                        popup.getMenu().removeItem(R.id.menu_item2);
//                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().toString().equalsIgnoreCase("Modificar")) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AgregarEnsamble.this);
//                                final View view = getLayoutInflater().inflate(R.layout.dialog_addproduct, null);
//                                TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
//                                txtTitle.setText(R.string.product_update);
//
//                                final EditText txtdescripcion = (EditText) view.findViewById(R.id.add_txtdesc);
//                                final EditText txtprecio = (EditText) view.findViewById(R.id.add_txtprecio);
//                                final Spinner txtcateg = (Spinner)view.findViewById(R.id.addtxtcateg);
//                                ArrayAdapter<String> adapter1= new ArrayAdapter<String>(AgregarEnsamble.this, android.R.layout.simple_spinner_dropdown_item);
//                                txtcateg.setAdapter(adapter1);
//                                for(Category category : categories){
//                                    adapter1.add(category.getDescription());
//                                }
//
//                                builder.setCancelable(false);
//                                builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.dismiss();
//                                    }
//                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        boolean comprecio = false;
//                                        try{
//                                            Integer.parseInt(txtprecio.getText().toString());
//                                        }catch (NumberFormatException nfe){
//                                            comprecio = true;
//                                        }
//
//                                        if(comprecio){
//                                            dialog.dismiss();
//                                            Toast.makeText(AgregarEnsamble.this, R.string.error_msg1, Toast.LENGTH_SHORT).show();
//                                        }else {
//
//                                            AlertDialog.Builder build = new AlertDialog.Builder(AgregarEnsamble.this);
//                                            build.setCancelable(false);
//                                            build.setTitle("Modificar producto");   // aqui cambiar por string de categories
//                                            build.setMessage(R.string.sure_text);
//
//                                            build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.dismiss();
//                                                }
//                                            }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//
//                                                    List<Category> categories = compuStore.getAllCategories();
//
//                                                    if (compuStore.updateProduct(txtdescripcion.getText().toString(),product.getId(),categories.get(txtcateg.getSelectedItemPosition()).getId(),Integer.parseInt(txtprecio.getText().toString()),product.getQuantity())) {
//                                                        Toast.makeText(AgregarEnsamble.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
//                                                        adapter = new ProductAdapter(compuStore.getAllProducts());
//                                                        productRV.setAdapter(adapter);
//                                                    } else {
//                                                        Toast.makeText(AgregarEnsamble.this, R.string.error_msg2, Toast.LENGTH_SHORT).show();
//                                                    }
//
//                                                }
//                                            });
//
//                                            build.create().show();
//                                        }
//                                    }
//                                });
//                                builder.setView(view);
//                                AlertDialog dialog = builder.create();
//                                dialog.show();
                            }

                            if (item.getTitle().toString().equalsIgnoreCase("Eliminar")) {
                                AlertDialog.Builder build = new AlertDialog.Builder(AgregarEnsamble.this);
                                build.setCancelable(false);
                                build.setTitle("Eliminar producto de ensamble");
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        Toast.makeText(AgregarEnsamble.this, R.string.dlt_msg, Toast.LENGTH_SHORT).show();
//                                        compuStore.deleteProduct(product.getId(), true);
//                                        adapter = new ProductsActivity.ProductAdapter(compuStore.getAllProducts());
//                                        productRV.setAdapter(adapter);
                                    }
                                });

                                build.create().show();
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

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {

        private List<Product> products;

        public ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ProductHolder(view);
        }
        @Override
        public void onBindViewHolder(ProductHolder holder, int position) {
            holder.bindCategory(products.get(position));
        }
        @Override
        public int getItemCount() {
            return products.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ensamble);

        compuStore= new CompuStore(AgregarEnsamble.this);
        productRV = (RecyclerView) findViewById(R.id.recyclerviewproductos);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(new ArrayList<Product>());
        productRV.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(AgregarEnsamble.this,AgregarProductoparaEnsamble.class);
        startActivityForResult(i,2);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
            int productid = data.getIntExtra("Productid",-1);
            //TextView labelprueba = (TextView)findViewById(R.id.labelprueba);
            //labelprueba.setText(Integer.toString(productid));
            //products.add() Hacer funcion que regrese un producto de la base de datos
            products.add(compuStore.getProductfromid(productid));
            adapter = new ProductAdapter(products);
            productRV.setAdapter(adapter);
        }
    }
}
