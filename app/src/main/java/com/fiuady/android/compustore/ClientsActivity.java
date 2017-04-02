package com.fiuady.android.compustore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;

import java.util.List;

public class ClientsActivity extends AppCompatActivity {

    private RecyclerView clientRV;
    private ClientAdapter C_adapter;
    private CompuStore compuStore;

    private class ClientHolder extends RecyclerView.ViewHolder {

        private TextView txtFullName;
        private TextView txtAddress;
        private TextView txtPhone1;
        private TextView txtPhone2;
        private TextView txtPhone3;
        private TextView txtEmail;
        List<Client> clients;


        public ClientHolder(View itemView,List<Client> clients) {
            super(itemView);
            this.clients=clients;

            txtFullName=(TextView)itemView.findViewById(R.id.client_fullname_text);
            txtAddress=(TextView)itemView.findViewById(R.id.client_address_text);
            txtPhone1=(TextView)itemView.findViewById(R.id.client_phone1_text);
            txtPhone2=(TextView)itemView.findViewById(R.id.client_phone2_text);
            txtPhone3=(TextView)itemView.findViewById(R.id.client_phone3_text);
            txtEmail=(TextView)itemView.findViewById(R.id.client_email_text);
        }

        public void bindClient(Client client){
            txtFullName.setText(client.getLastName()+", "+client.getFirstName());
            txtAddress.setText(client.getAddress());
            txtEmail.setText(client.getEmail());
            txtPhone1.setText(client.getPhone1());
            txtPhone2.setText(client.getPhone2());
            txtPhone3.setText(client.getPhone3());
        }
    }

    private class ClientAdapter extends RecyclerView.Adapter<ClientHolder>{

        private List<Client> clients;

        public ClientAdapter(List<Client> clients){
            this.clients=clients;
        }

        @Override
        public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.client_list_item,parent,false);
            return new ClientHolder(view,clients);
        }

        @Override
        public void onBindViewHolder(ClientHolder holder, int position) {
            holder.bindClient(clients.get(position));
        }

        @Override
        public int getItemCount() {return clients.size();}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        compuStore = new CompuStore(this);

        clientRV=(RecyclerView)findViewById(R.id.activity_clients_RV);
        clientRV.setLayoutManager(new LinearLayoutManager(this)); //Porque el recycler view NO es un layout, necesitamos uno.

        C_adapter=new ClientAdapter(compuStore.getAllClients()); //Creo el adaptador y me pide una lista de empleados.
        clientRV.setAdapter(C_adapter);
    }
}
