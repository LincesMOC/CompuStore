package com.fiuady.android.compustore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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
                        public boolean onMenuItemClick(final MenuItem item) {

                            if (item.getTitle().toString().equalsIgnoreCase("Modificar")) {
                                AlertDialog.Builder build = new AlertDialog.Builder(AgregarEnsamble.this);
                                build.setCancelable(false);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_addstock, null);
                                build.setTitle("Cantidad para ensamble");
                                final Spinner spinstock = (Spinner)view.findViewById(R.id.spinnerstock);
                                ArrayAdapter<String> adapter2= new ArrayAdapter<String>(AgregarEnsamble.this,android.R.layout.simple_spinner_dropdown_item);
                                //funcion para agregar al adapter numeros arriba del valor de stock actual
                                int stock = product.getQuantity();

                                for(int i=stock;i<stock+10;i++){
                                    adapter2.add(Integer.toString(i));
                                }
                                spinstock.setAdapter(adapter2);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        product.setQuantity(Integer.parseInt(spinstock.getSelectedItem().toString()));
                                        Toast.makeText(AgregarEnsamble.this,"El valor fue actualizado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                build.setView(view);
                                AlertDialog dialog = build.create();
                                dialog.show();
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
                                        products.remove(product);
                                        adapter = new ProductAdapter(products);
                                        productRV.setAdapter(adapter);
                                        Toast.makeText(AgregarEnsamble.this, "Eliminado de ensamble", Toast.LENGTH_SHORT).show();
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

    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ensamble);

        compuStore= new CompuStore(AgregarEnsamble.this);
        productRV = (RecyclerView) findViewById(R.id.recyclerviewproductos);
//        productRV.setLayoutManager(new LinearLayoutManager(this));
//        //productRV.setLayoutManager(new GridLayoutManager(this,2));

        if (AgregarEnsamble.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            productRV.setLayoutManager(new GridLayoutManager(this,2));
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (AgregarEnsamble.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            productRV.setLayoutManager(new LinearLayoutManager(this));
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
        adapter = new ProductAdapter(new ArrayList<Product>());
        productRV.setAdapter(adapter);
        products = new ArrayList<Product>();
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
            //products.add(compuStore.getProductfromid(productid));
            Product product = compuStore.getProductfromid(productid);
            boolean duplicado = false;
            for(Product product1: products){
                if(product1.getId() == product.getId()){
                    duplicado = true;
                }
            }
            if(duplicado){
                Toast.makeText(AgregarEnsamble.this, "El producto ya esta en el ensamble", Toast.LENGTH_SHORT).show();
            }else {
                product.setQuantity(1);
                products.add(product);
                adapter = new ProductAdapter(products);
                productRV.setAdapter(adapter);
                Toast.makeText(AgregarEnsamble.this, "Agregado al ensamble", Toast.LENGTH_SHORT).show();
            }
        }
    }

    EditText descrip;
    public void btnguardar (View v) {

        descrip = (EditText)findViewById(R.id.edittextdescripcion);
        if(descrip.getText().toString().isEmpty()){
            Toast.makeText(AgregarEnsamble.this, "Agrega una descripcion", Toast.LENGTH_SHORT).show();
        }else {
            //Agregar ensamble
            compuStore.insertAssembly(descrip.getText().toString());
            int idensa = compuStore.getAssemblyid(descrip.getText().toString());
            //Agregar cada producto del ensamble con cantidades
            for(Product p :products){
                compuStore.insertAssemblyproducts(idensa,p.getId(),p.getQuantity());
            }

            Toast.makeText(AgregarEnsamble.this, "Ensamble agregado", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    public void btnCancelar (View v) {
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            productRV.setLayoutManager(new GridLayoutManager(this,2));
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            productRV.setLayoutManager(new LinearLayoutManager(this));
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }

    }
}
