package com.fiuady.android.compustore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView orderRV;
    private OrderAdapter O_adapter;
    private CompuStore compuStore;

    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        List<Order> orders;

        public OrderHolder(View itemView,List<Order> orders) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrdersActivity.OrderHolder>{

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        compuStore = new CompuStore(this);


        Spinner spinner = (Spinner)findViewById(R.id.status_order_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status_order_array,android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }
}
