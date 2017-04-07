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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

            txtFullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popup = new PopupMenu(ClientsActivity.this, txtFullName);
                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

                }
            });



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_add_client, null);
        TextView txtTitle = (TextView) view.findViewById(R.id.add_title);

        final EditText txtAdd_firstName = (EditText) view.findViewById(R.id.add_text_firstName);
        final EditText txtAdd_lastName = (EditText) view.findViewById(R.id.add_text_lastName);
        final EditText txtAdd_address = (EditText) view.findViewById(R.id.add_text_address);
        final EditText txtAdd_email = (EditText) view.findViewById(R.id.add_text_email);
        final EditText txtAdd_phone1 = (EditText) view.findViewById(R.id.add_text_phone1);
        final EditText txtAdd_phone2 = (EditText) view.findViewById(R.id.add_text_phone2);
        final EditText txtAdd_phone3 = (EditText) view.findViewById(R.id.add_text_phone3);

        txtTitle.setText(R.string.client_add);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                AlertDialog.Builder build = new AlertDialog.Builder(ClientsActivity.this);
                build.setCancelable(false);
                build.setTitle(getString(R.string.client_add));
                build.setMessage(R.string.sure_text);

                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Toast.makeText(ClientsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        //Toast.makeText(ClientsActivity.this, R.string.add_msg_test, Toast.LENGTH_SHORT).show();

                        if(compuStore.insertClient(txtAdd_firstName.getText().toString(),txtAdd_lastName.getText().toString(),
                        txtAdd_address.getText().toString(),txtAdd_email.getText().toString(),txtAdd_phone1.getText().toString(),
                        txtAdd_phone2.getText().toString(),txtAdd_phone3.getText().toString())){

                            Toast.makeText(ClientsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                            C_adapter = new ClientsActivity.ClientAdapter(compuStore.getAllClients());
                            clientRV.setAdapter(C_adapter);
                        } else {
                            Toast.makeText(ClientsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                build.create().show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        return super.onOptionsItemSelected(item);
    }


}