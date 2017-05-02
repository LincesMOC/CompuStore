package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.MissingProduct;
import com.fiuady.db.Product;

import java.util.ArrayList;
import java.util.List;

public class MisssingProductsActivity extends AppCompatActivity {

    private class MissingProductHolder extends RecyclerView.ViewHolder {

        private TextView txtDescription;
        private TextView txtQuantity;

        public MissingProductHolder(View itemView) {
            super(itemView);
            txtDescription = (TextView) itemView.findViewById(android.R.id.text1);
            txtQuantity = (TextView) itemView.findViewById(android.R.id.text2);
        }

        public void bindMissingProducts(final MissingProduct missingProduct) {
            txtDescription.setText(missingProduct.getDescription());
            txtQuantity.setText(Integer.toString(missingProduct.getQuantity()));
            txtQuantity.setGravity(1);
            txtQuantity.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private class MissingProductAdapter extends RecyclerView.Adapter<MissingProductHolder> {

        private List<MissingProduct> mProducts;

        public MissingProductAdapter(List<MissingProduct> mProducts) { this.mProducts = mProducts; }

        @Override
        public void onBindViewHolder(MissingProductHolder holder, int position) {
            holder.bindMissingProducts(mProducts.get(position));
        }

        @Override
        public MissingProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.two_line_list_item, parent, false);
            return new MissingProductHolder(view);
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }
    }

    private CompuStore compuStore;
    private RecyclerView mpRV;
    private MisssingProductsActivity.MissingProductAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        compuStore = new CompuStore(this);

        mpRV = (RecyclerView) findViewById(R.id.activity_categories);
        mpRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MisssingProductsActivity.MissingProductAdapter(productosFaltantes());

        mpRV.setAdapter(adapter);
    }

    List<MissingProduct> productosFaltantes() {
        List<Product> P = compuStore.getAllProducts();
        List<MissingProduct> M = compuStore.getAllMissingProduct();
        List<MissingProduct> MP = new ArrayList<MissingProduct>();

        for (MissingProduct missingProduct : M) {
            for (Product product : P) {
                if (missingProduct.getId() == product.getId() && missingProduct.getQuantity() > product.getQuantity()) {
                    MissingProduct mp = new MissingProduct(product.getId(), product.getDescription(), (missingProduct.getQuantity() - product.getQuantity()));
                    MP.add(mp);
                }
            }
        }
        return MP;
    }
}
