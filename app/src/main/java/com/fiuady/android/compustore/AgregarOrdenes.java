package com.fiuady.android.compustore;

import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;

import java.util.ArrayList;
import java.util.List;

public class AgregarOrdenes extends AppCompatActivity {

    private RecyclerView assemblyRV;
    private AssemblyAdapter A_adapter;
    private CompuStore compuStore;
    private Spinner AO_clientsSpinner;


    private class AssemblyHolder extends RecyclerView.ViewHolder {

        private TextView txtDescription;

        public AssemblyHolder(View itemView) {
            super(itemView);
            txtDescription = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bindAssembly(final Assembly assembly) {

            txtDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CUANDO DAS CLICK A UN ASSEMBLY

                    //final PopupMenu popup = new PopupMenu(AgregarOrdenes.this, txtDescription);
                    //popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());
//
                    //popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
//
                    //    @Override
                    //    public boolean onMenuItemClick(MenuItem item) {
//
                    //        if ((item.getTitle().toString()).equalsIgnoreCase("Modificar")){
//
                    //            AlertDialog.Builder build = new AlertDialog.Builder(AgregarOrdenes.this);
                    //            build.setCancelable(false);
                    //            final View view = getLayoutInflater().inflate(R.layout.dialog_add_assemblyfororder,null);
                    //            build.setTitle("Cantidad de ensambles");
//
                    //            final Spinner spin_assemblyQty = (Spinner)view.findViewById(R.id.add_assemblyQty);
                    //            ArrayAdapter<String> AQ_adapter= new ArrayAdapter<String>(AgregarOrdenes.this,android.R.layout.simple_spinner_dropdown_item);
//
                    //            //funcion para agregar al adapter numeros arriba del valor de stock actual
                    //        }
//
                    //        return true;
                    //    }
                    //});

                }
            });

            txtDescription.setText(assembly.getDescription());
        }
    }

    private class AssemblyAdapter extends RecyclerView.Adapter<AssemblyHolder>{

        private List<Assembly> assemblies;

        public AssemblyAdapter (List<Assembly> assemblies){
            this.assemblies = assemblies;
        }

        @Override
        public AssemblyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new AssemblyHolder(view);
        }

        @Override
        public void onBindViewHolder(AssemblyHolder holder, int position) {
            holder.bindAssembly(assemblies.get(position));
        }

        @Override
        public int getItemCount() {
            return assemblies.size();
        }
    }

    private ArrayList<Assembly> assemblies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ordenes);
        compuStore = new CompuStore(this);

        //SPINNER DE CLIENTES
        AO_clientsSpinner = (Spinner)findViewById(R.id.AO_client_spinner);
        ArrayAdapter<String> AO_cs_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        AO_clientsSpinner.setAdapter(AO_cs_adapter);

        AO_cs_adapter.add("Todos");
        List<Client> clients = compuStore.getAllClients();
        for(Client client :clients){
            AO_cs_adapter.add(client.getFirstName() + " " + client.getLastName());}

        assemblyRV = (RecyclerView)findViewById(R.id.addOrder_assemblies_RV);
        assemblyRV.setLayoutManager(new LinearLayoutManager(this));

        A_adapter = new AssemblyAdapter(new ArrayList<Assembly>());
        assemblyRV.setAdapter(A_adapter);

        assemblies = new ArrayList<Assembly>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //AGREGAR ENSAMBLE PARA ÓRDENES

        Intent i = new Intent(AgregarOrdenes.this,AgregarEnsambleParaOrden.class);
        startActivityForResult(i,3);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
