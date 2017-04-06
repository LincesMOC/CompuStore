package com.fiuady.android.compustore;

import android.content.DialogInterface;
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
                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

                    if (compuStore.deleteProduct(product.getId(), false)) {
                        popup.getMenu().removeItem(R.id.menu_item2);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
                                TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
                                final EditText txtAdd = (EditText) view.findViewById(R.id.add_text);

                                txtTitle.setText(R.string.category_update); // aqui cambiar por string de categories

                                builder.setCancelable(false);
                                builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                                        build.setCancelable(false);
                                        build.setTitle(getString(R.string.category_update));   // aqui cambiar por string de categories
                                        build.setMessage(R.string.sure_text);

                                        build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
//                                                if (compuStore.updateProduct(txtAdd.getText().toString(), product.getId())) {
//                                                    Toast.makeText(ProductsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
//                                                    adapter = new ProductsActivity.ProductAdapter(compuStore.getAllProducts());
//                                                    productRV.setAdapter(adapter);
//                                                } else {
//                                                    Toast.makeText(ProductsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
//                                                }
                                            }
                                        });

                                        build.create().show();
                                    }
                                });
                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            else{
                                AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                                build.setCancelable(false);
                                build.setTitle(getString(R.string.category_delete));
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        Toast.makeText(ProductsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
//                                        compuStore.deleteCategory(product.getId(), true);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        categorispinner = (Spinner)findViewById(R.id.spinnercategories);
        compuStore = new CompuStore(getApplicationContext());

        productRV = (RecyclerView) findViewById(R.id.recyclerviewproductos);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(compuStore.getAllProducts());
        productRV.setAdapter(adapter);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        categorispinner.setAdapter(adapter);

        adapter.add("Todos");
        List<Category> categories = compuStore.getAllCategories();
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
        return super.onOptionsItemSelected(item);
    }
}
