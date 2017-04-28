package com.fiuady.android.compustore;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.AssemblyProduct;
import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.ArrayList;
import java.util.List;

public class ModificarEnsamble extends AppCompatActivity {
    private RecyclerView productRV;
    private ProductAdapter adapter;
    private CompuStore compuStore;
    private Boolean Llenarconensambleid = true ;


    private final String KEY_Recyclerprods1="recyclersprods1";
    private ArrayList<Integer> productsids;

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
                    final PopupMenu popup = new PopupMenu(ModificarEnsamble.this, txtDescription);
                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final MenuItem item) {

                            if (item.getTitle().toString().equalsIgnoreCase("Modificar")) {
                                AlertDialog.Builder build = new AlertDialog.Builder(ModificarEnsamble.this);
                                build.setCancelable(false);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_addstock, null);
                                build.setTitle("Cantidad para ensamble");
                                final Spinner spinstock = (Spinner)view.findViewById(R.id.spinnerstock);
                                ArrayAdapter<String> adapter2= new ArrayAdapter<String>(ModificarEnsamble.this,android.R.layout.simple_spinner_dropdown_item);
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
                                        Toast.makeText(ModificarEnsamble.this,"El valor fue actualizado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                build.setView(view);
                                AlertDialog dialog = build.create();
                                dialog.show();
                            }

                            if (item.getTitle().toString().equalsIgnoreCase("Eliminar")) {
                                AlertDialog.Builder build = new AlertDialog.Builder(ModificarEnsamble.this);
                                build.setCancelable(false);
                                build.setTitle("Eliminar producto de ensamble");
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        products2.remove(product);
                                        adapter = new ProductAdapter(products2);
                                        productRV.setAdapter(adapter);
                                        Toast.makeText(ModificarEnsamble.this, "Eliminado de ensamble", Toast.LENGTH_SHORT).show();
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

//    private ArrayList<Product> products;
    ArrayList<Product> products2;

    int paso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ensamble);

        compuStore= new CompuStore(ModificarEnsamble.this);
        productRV = (RecyclerView) findViewById(R.id.recyclerviewproductos);
        if (ModificarEnsamble.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            productRV.setLayoutManager(new GridLayoutManager(this,2));
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (ModificarEnsamble.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            productRV.setLayoutManager(new LinearLayoutManager(this));
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
        adapter = new ProductAdapter(new ArrayList<Product>());

        paso = getIntent().getExtras().getInt("assemblyid");

        ArrayList<AssemblyProduct> ap = compuStore.getEspecificAssemblyProducts(paso);
         products2 = new ArrayList<Product>();
         productsids=new ArrayList<Integer>();

        if(Llenarconensambleid) {
            for (AssemblyProduct apr : ap) {

                for (Product p : compuStore.getAllProducts()) {

                    if (apr.getProduct_id() == p.getId()) {
                        p.setQuantity(apr.getQty());
                        products2.add(p);
                    }
                }
            }
            Llenarconensambleid =false;
        }
        else{
            if(savedInstanceState != null){
                for (int i=0;i<products2.size();i++) {
                    products2.remove(i);
                }
                productsids = savedInstanceState.getIntegerArrayList(KEY_Recyclerprods1);

                for (Integer i:productsids) {
                    Product p = compuStore.getProductfromid(i);
                    products2.add(p);
                }
//            adapter = new ProductAdapter(products2);
//            productRV.setAdapter(adapter);
            }
        }
//        products = new ArrayList<>();
        adapter = new ProductAdapter(products2);
        productRV.setAdapter(adapter);
        descrip = (EditText)findViewById(R.id.edittextdescripcion);
        descrip.setText(compuStore.getAssemblydesc(paso));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(ModificarEnsamble.this,AgregarProductoparaEnsamble.class);
        startActivityForResult(i,2);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            Llenarconensambleid=false;
            //Toast.makeText(ModificarEnsamble.this, "Sali sin modificar", Toast.LENGTH_SHORT).show();
        }else {
            if (requestCode == 2) {
                int productid = data.getIntExtra("Productid", -1);
                Product product = compuStore.getProductfromid(productid);
                boolean duplicado = false;
                for (Product product1 : products2) {
                    if (product1.getId() == product.getId()) {
                        duplicado = true;
                    }
                }
                if (duplicado) {
                    Toast.makeText(ModificarEnsamble.this, "El producto ya esta en el ensamble", Toast.LENGTH_SHORT).show();
                } else {
                    product.setQuantity(1);
                    products2.add(product);
                    adapter = new ProductAdapter(products2);
                    productRV.setAdapter(adapter);
                    Toast.makeText(ModificarEnsamble.this, "Agregado al ensamble", Toast.LENGTH_SHORT).show();
                }
                Llenarconensambleid=false;
            }
        }
    }

    EditText descrip;
    public void btnguardar (View v) {

            compuStore.deleteAssemblyproducts(paso);
            compuStore.deleteAssembly(paso, true);
            //Agregar ensamble
            compuStore.insertAssembly(descrip.getText().toString());
            int idensa = compuStore.getAssemblyid(descrip.getText().toString());
            //Agregar cada producto del ensamble con cantidades
            for(Product p :products2){
                compuStore.insertAssemblyproducts(idensa,p.getId(),p.getQuantity());
            }

            Toast.makeText(ModificarEnsamble.this, "Ensamble modificado", Toast.LENGTH_SHORT).show();
            finish();

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i=0;i<productsids.size();i++) {
            productsids.remove(i);
        }
        for (Product p:products2) {
            productsids.add(p.getId());
        }
        Llenarconensambleid=false;
        outState.putIntegerArrayList(KEY_Recyclerprods1,productsids);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Llenarconensambleid=false;
    }
}
