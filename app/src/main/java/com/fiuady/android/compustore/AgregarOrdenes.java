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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;
import com.fiuady.db.OrderAssembly;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AgregarOrdenes extends AppCompatActivity {

    private Spinner AO_clientsSpinner;
    private CompuStore compuStore;
    private RecyclerView assemblyRV;
    private AssemblyAdapter A_adapter;
    ArrayList<OrderAssembly> oa;
    private Boolean Llenarconensambleid = true ;
    private ArrayList<Integer> AssembliesIDs;
    private final String KEY_RecyclerAssemblies1="recyclerAs1";
    ArrayList<Assembly> assemblies;
    OrderAssembly orderAssembly = null;
    int MaxId;
    ArrayList<OrderAssembly> orderAssemblies_TEMP = new ArrayList<>();

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
                    final PopupMenu popup = new PopupMenu(AgregarOrdenes.this, txtDescription);
                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().toString().equalsIgnoreCase("Modificar")) {
                                //MODIFICAR CANTIDAD DE ESTE ENSAMBLE EN LA ORDEN
                                AlertDialog.Builder build = new AlertDialog.Builder(AgregarOrdenes.this);
                                build.setCancelable(false);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_addstock, null);
                                build.setTitle("Cantidad para orden");
                                final Spinner spinstock = (Spinner)view.findViewById(R.id.spinnerstock);
                                ArrayAdapter<String> adapter2= new ArrayAdapter<String>(AgregarOrdenes.this,android.R.layout.simple_spinner_dropdown_item);
                                //Llenado del adaptador con la cantidad de ensambles en la orden
                                int assemblyQTY = 0;

                                //BUSCAR EN LISTA DE OA TEMPORAL el oa buscado, y modificarlo

                                for (OrderAssembly o: orderAssemblies_TEMP) {
                                    if (o.getAssembly_id() == assembly.getId()){
                                        assemblyQTY = o.getQty();
                                    }
                                }

                                for (OrderAssembly o: orderAssemblies_TEMP) {
                                    if (o.getAssembly_id() == assembly.getId()){
                                        assemblyQTY = o.getQty();
                                    }
                                }

                                for(int i=assemblyQTY;i<assemblyQTY+10;i++){
                                    adapter2.add(Integer.toString(i));
                                }
                                spinstock.setAdapter(adapter2);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                       for (OrderAssembly o: orderAssemblies_TEMP) {
                                           if (o.getAssembly_id() == assembly.getId()){
                                               o.setQty(Integer.parseInt(spinstock.getSelectedItem().toString()));
                                           }
                                       }
                                        Toast.makeText(AgregarOrdenes.this,"El valor fue actualizado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                build.setView(view);
                                AlertDialog dialog = build.create();
                                dialog.show();
                            }
                            if (item.getTitle().toString().equalsIgnoreCase("Eliminar")) {
                                //ELIMINAR ESTE ENSAMBLE DE LA ORDEN
                                AlertDialog.Builder build = new AlertDialog.Builder(AgregarOrdenes.this);
                                build.setCancelable(false);
                                build.setTitle("Eliminar ensamble de orden");
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        assemblies.remove(assembly);

                                        Collections.sort(assemblies, new Comparator<Assembly>() {
                                            @Override
                                            public int compare(Assembly o1, Assembly o2) {
                                                return o1.getDescription().compareTo(o2.getDescription());
                                            }
                                        });
                                        A_adapter = new AssemblyAdapter(assemblies);
                                        assemblyRV.setAdapter(A_adapter);
                                        Toast.makeText(AgregarOrdenes.this, "Ensamble eliminado de orden", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ordenes);
        compuStore = new CompuStore(AgregarOrdenes.this);
        assemblyRV = (RecyclerView)findViewById(R.id.addOrder_assemblies_RV);

        //LANDSCAPE
        if (AgregarOrdenes.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            assemblyRV.setLayoutManager(new GridLayoutManager(this,2));
        } else if (AgregarOrdenes.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            assemblyRV.setLayoutManager(new LinearLayoutManager(this));
        }

        //SPINNER DE CLIENTES
        AO_clientsSpinner = (Spinner)findViewById(R.id.AO_client_spinner);
        ArrayAdapter<String> AO_cs_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        AO_clientsSpinner.setAdapter(AO_cs_adapter);
        AO_cs_adapter.add("Todos");
        List<Client> clients = compuStore.getAllClients();
        for(Client client :clients){
            AO_cs_adapter.add(client.getFirstName() + " " + client.getLastName());}

        //assemblyRV.setLayoutManager(new LinearLayoutManager(this));
        A_adapter = new AssemblyAdapter(new ArrayList<Assembly>());
        assemblyRV.setAdapter(A_adapter);
        assemblies = new ArrayList<Assembly>(); //Lista de ensambles para el Recycler View
        AssembliesIDs=new ArrayList<Integer>(); //Lista de IDs de ensambles

        if(savedInstanceState != null){
            AssembliesIDs = savedInstanceState.getIntegerArrayList(KEY_RecyclerAssemblies1);

            for (Integer i:AssembliesIDs) {
                Assembly a = compuStore.getAssemblyFromId(i);
                assemblies.add(a);
            }

            Collections.sort(assemblies, new Comparator<Assembly>() {
                @Override
                public int compare(Assembly o1, Assembly o2) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
            });

            A_adapter = new AssemblyAdapter(assemblies);
            assemblyRV.setAdapter(A_adapter);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //FALTA!
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0){

        } else {
            if (requestCode == 3) {
                int assemblyId = data.getIntExtra("AssemblyId", -1);
                orderAssembly = new OrderAssembly(compuStore.getMaxId(),assemblyId,1);

                //MaxId = compuStore.getMaxId();
//
                //orderAssembly.setAssembly_id(assemblyId);
                //orderAssembly.setQty(1);
                //orderAssembly.setId(MaxId+1);

                orderAssemblies_TEMP.add(orderAssembly); //Lista con las order assemblies!

                Assembly assembly = compuStore.getAssemblyFromId(assemblyId);
                boolean duplicado = false;

                for (Assembly assembly1 : assemblies) {
                    if (assembly1.getId() == assembly.getId()) {
                        duplicado = true;
                    }
                }
                if (duplicado) {
                    Toast.makeText(AgregarOrdenes.this, "El ensamble ya está en la orden", Toast.LENGTH_SHORT).show();
                } else {

                    assemblies.add(assembly);

                    Collections.sort(assemblies, new Comparator<Assembly>() {
                        @Override
                        public int compare(Assembly o1, Assembly o2) {
                            return o1.getDescription().compareTo(o2.getDescription());
                        }
                    });

                    A_adapter = new AssemblyAdapter(assemblies);
                    assemblyRV.setAdapter(A_adapter);
                    Toast.makeText(AgregarOrdenes.this, "Agregado a la orden", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void btnguardar (View v) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String current_date = df.format(c.getTime());


        //PROTEGER SI NO HAY ENSAMBLES EN LA LISTA!!!

        Integer client_id = (compuStore.filterClientsByName(AO_clientsSpinner.getSelectedItem().toString())).getId();

        if (AO_clientsSpinner.getSelectedItem().toString() == "Todos"){
            Toast.makeText(AgregarOrdenes.this, "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show();
        }
        else{
            //Agregar orden
            compuStore.insertOrder(client_id, current_date);

            //Agregar ensambles de orden
            for (OrderAssembly oaTemp : orderAssemblies_TEMP){
                //id de la orden
                compuStore.insertOrderAssembly(compuStore.getMaxIdOrder(),oaTemp.getAssembly_id(),oaTemp.getQty());
            }

            MaxId = compuStore.getMaxIdOrder();

            Toast.makeText(AgregarOrdenes.this, "Orden Agregada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void btnCancelar (View v) {
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            assemblyRV.setLayoutManager(new GridLayoutManager(this,2));
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            assemblyRV.setLayoutManager(new LinearLayoutManager(this));
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AssembliesIDs.clear();
        for (Assembly a:assemblies) {
            AssembliesIDs.add(a.getId());
        }
        outState.putIntegerArrayList(KEY_RecyclerAssemblies1,AssembliesIDs);
    }

}
