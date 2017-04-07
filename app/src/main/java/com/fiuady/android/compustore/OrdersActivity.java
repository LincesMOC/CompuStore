package com.fiuady.android.compustore;

import android.support.v7.app.AlertDialog;
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

import com.fiuady.db.Assembly;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView orderRV;
    private OrderAdapter O_adapter;
    private CompuStore compuStore;
    private Spinner clientsSpinner;

    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtClientName;
        private TextView txtOrderDate;
        private TextView txtOrderStatus;

        List<Order> orders;

        public OrderHolder(View itemView,List<Order> orders) {
            super(itemView);
            this.orders = orders;
            itemView.setOnClickListener(this);

            txtClientName=(TextView)itemView.findViewById(R.id.txt_clientName);
            txtOrderDate=(TextView)itemView.findViewById(R.id.txt_orderDate);
            txtOrderStatus=(TextView)itemView.findViewById(R.id.txt_orderStatus);
        }

        public void bindOrder(Order order){

            txtClientName.setText(compuStore.getCustomer(order.getCustomer_id()));
            txtOrderDate.setText(order.getDate());
            txtOrderStatus.setText(order.getStatus_String());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(OrdersActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder>{

        private List<Order> orders;

        public OrderAdapter(List<Order> orders){
            this.orders=orders;
        }

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.order_list_item,parent,false);
            return new OrderHolder(view,orders);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {holder.bindOrder(orders.get(position));}

        @Override
        public int getItemCount() {return orders.size();}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        compuStore = new CompuStore(this);

        //SPINNER DE CLIENTES
        clientsSpinner = (Spinner)findViewById(R.id.client_spinner);
        ArrayAdapter<String> cs_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        clientsSpinner.setAdapter(cs_adapter);

        cs_adapter.add("Todos");
        List<Client> clients = compuStore.getAllClients();
        for(Client client :clients){
        cs_adapter.add(client.getFirstName() + " " + client.getLastName());}

        //SPINNER DE ESTADO DE ORDEN
        Spinner spinner = (Spinner)findViewById(R.id.status_order_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status_order_array,android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        spinner.setAdapter(adapter);

        orderRV = (RecyclerView)findViewById(R.id.activity_orders);
        orderRV.setLayoutManager(new LinearLayoutManager(this));

        O_adapter = new OrderAdapter(compuStore.getAllOrders());
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
