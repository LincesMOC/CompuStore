package com.fiuady.android.compustore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Category;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;
import com.fiuady.db.Product;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtClientName;
        private TextView txtDate;
        private TextView txtOrderStatus;

        List<Order> orders;

        //public OrderHolder(View itemView,List<Order> orders) {
          //  super(itemView);
            //this.orders=orders;
            //itemView.setOnClickListener(this);

            //txtClientName = (TextView)itemView.findViewById(R.id.txt_clientName);
            //txtDate = (TextView)findViewById(R.id.txt_orderDate);
            //txtOrderStatus = (TextView)findViewById(R.id.txt_orderStatus);
        //}

        public OrderHolder(View itemView) {
            super(itemView);
            txtClientName = (TextView)itemView.findViewById(R.id.txt_clientName);
            txtDate = (TextView)itemView.findViewById(R.id.txt_orderDate);
            txtOrderStatus = (TextView)itemView.findViewById(R.id.txt_orderStatus);
        }

        public void bindOrder(Order order){

            //List<Client> clients = compuStore.getAllClients();
            //int client_id = order.getCustomer_id();
            //final Client client = clients.get(client_id);

            txtClientName.setText(order.getCustomer_id());
            txtDate.setText(order.getDate());
            txtOrderStatus.setText(order.getStatus_String());
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(OrdersActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();

        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrdersActivity.OrderHolder>{

        private List<Order> orders;

        public OrderAdapter(List<Order> orders) {this.orders = orders;}

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.order_list_item,parent,false);
            //return new OrderHolder(view,orders);
            return new OrderHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {holder.bindOrder(orders.get(position));}

        @Override
        public int getItemCount() {return orders.size();}
    }

    private RecyclerView orderRV;
    private OrderAdapter O_adapter;
    private CompuStore compuStore;
    private Spinner clientsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        compuStore = new CompuStore(this);

        orderRV=(RecyclerView)findViewById(R.id.activity_orders);
        orderRV.setLayoutManager(new LinearLayoutManager(this)); //Porque el recycler view NO es un layout, necesitamos uno.

        O_adapter=new OrderAdapter(compuStore.getAllOrders()); //Creo el adaptador y me pide una lista de empleados.
        orderRV.setAdapter(O_adapter);
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
