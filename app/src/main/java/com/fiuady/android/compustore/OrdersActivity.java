package com.fiuady.android.compustore;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView orderRV;
    private OrderAdapter O_adapter;
    private CompuStore compuStore;
    private Spinner clientsSpinner;
    private MultiSpinner orderStateSpinner;
    private EditText LISTA;

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

    boolean[] selected1;
    String textClient = "Todos";
    String textStatus = "Todos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        compuStore = new CompuStore(this);

        //SPINNER DE ESTADO DE ORDEN
        LISTA = (EditText)findViewById(R.id.edittextdescripcion);
        orderStateSpinner = (MultiSpinner) findViewById(R.id.status_order_spinner);

        final List<String> list = new ArrayList<>();
        list.add("Pendiente");
        list.add("Cancelado");
        list.add("Confirmado");
        list.add("En tránsito");
        list.add("Finalizado");

        //String textStatus = orderStateSpinner.getSelectedItem().toString();

        orderStateSpinner.setItems(list, "Todos", new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

                textStatus = orderStateSpinner.getSelectedItem().toString();
                Toast.makeText(OrdersActivity.this,"Seleccionado: "+textStatus, Toast.LENGTH_SHORT).show();

                int i=0;

                for (Boolean b : selected){
                    if (b){
                        Toast.makeText(OrdersActivity.this,"Seleccionado: "+Integer.toString(i), Toast.LENGTH_SHORT).show();
                    }
                    i++;
                }

                selected1 = selected;

                O_adapter = new  OrdersActivity.OrderAdapter(compuStore.filterOrdersByStatus(selected1,textClient,textStatus));
                orderRV.setAdapter(O_adapter);
            }
        });



        //SPINNER DE CLIENTES
        clientsSpinner = (Spinner)findViewById(R.id.client_spinner);
        ArrayAdapter<String> cs_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        clientsSpinner.setAdapter(cs_adapter);

        cs_adapter.add("Todos");

        List<Client> clients = compuStore.getAllClients();
        for(Client client :clients){
            cs_adapter.add(client.getFirstName() + " " + client.getLastName());}


        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textClient = clientsSpinner.getSelectedItem().toString();
                Toast.makeText(OrdersActivity.this,"Seleccionado: "+textClient, Toast.LENGTH_SHORT).show();

                if(orderStateSpinner.getSelectedItem().toString() == "Todos"){

                }else {
                    O_adapter = new  OrdersActivity.OrderAdapter(compuStore.filterOrdersByStatus(selected1,textClient,textStatus));
                    orderRV.setAdapter(O_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orderRV = (RecyclerView)findViewById(R.id.activity_orders_RV); //activity_orders?
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
        Intent i = new Intent(OrdersActivity.this,AgregarOrdenes.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    public void onInicialDateClick (View v) {
        final Calendar c = Calendar.getInstance();
        final TextView InitialDate = (TextView) findViewById(R.id.start_date);

        new DatePickerDialog(OrdersActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                InitialDate.setText("Fecha Inicial: " + Integer.toString(dayOfMonth) + "-" + Integer.toString(month) + "-" +
                        Integer.toString(year) + "-");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onFinishDateClick (View v) {
        final Calendar c = Calendar.getInstance();
        final TextView FinalDate = (TextView) findViewById(R.id.finish_date);

        new DatePickerDialog(OrdersActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                FinalDate.setText("Fecha Final: " + Integer.toString(dayOfMonth) + "-" + Integer.toString(month) + "-" +
                        Integer.toString(year) + "-");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

}
