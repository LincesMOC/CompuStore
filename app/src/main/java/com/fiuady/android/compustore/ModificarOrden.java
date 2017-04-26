package com.fiuady.android.compustore;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;
import com.fiuady.db.OrderAssembly;

import java.util.ArrayList;
import java.util.List;

public class ModificarOrden extends AppCompatActivity {

    private CompuStore compuStore;
    private RecyclerView assemblyRV;
    private AssemblyAdapter A_adapter;

    private Boolean Llenarconensambleid = true ;
    private ArrayList<Integer> AssembliesIDs;

    private final String KEY_RecyclerAssemblies1="recyclerAs1";

    private class AssemblyHolder extends RecyclerView.ViewHolder {

        private TextView txtDescription;

        public AssemblyHolder(View itemView) {
            super(itemView);
            txtDescription = (TextView)itemView.findViewById(android.R.id.text1);
        }

        public void bindAssembly(final Assembly assembly) {
            txtDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(ModificarOrden.this, txtDescription);
                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().toString().equalsIgnoreCase("Modificar")) {

                            }
                            if (item.getTitle().toString().equalsIgnoreCase("Eliminar")) {

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

        public AssemblyAdapter(List<Assembly> assemblies) {
            this.assemblies = assemblies;
        }

        @Override
        public AssemblyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new AssemblyHolder(view);
        }

        @Override
        public void onBindViewHolder(AssemblyHolder holder, int position) {holder.bindAssembly(assemblies.get(position));}

        @Override
        public int getItemCount() {return assemblies.size();}
    }

    ArrayList<Assembly> assemblies2;
    int orderID;
    int status_id;
    int customer_id;
    String date;
    String change_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_orden);

        compuStore= new CompuStore(ModificarOrden.this);
        assemblyRV = (RecyclerView)findViewById(R.id.modifyOrder_assemblies_RV);

        if (ModificarOrden.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            assemblyRV.setLayoutManager(new GridLayoutManager(this,2));
        } else if (ModificarOrden.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            assemblyRV.setLayoutManager(new LinearLayoutManager(this));
        }

        A_adapter = new AssemblyAdapter(new ArrayList<Assembly>()); //Aquí va la lista de los ensambles de cada orden :)
        orderID = getIntent().getExtras().getInt("orderID");
        status_id = getIntent().getExtras().getInt("status_id");
        customer_id = getIntent().getExtras().getInt("customer_id");
        date = getIntent().getExtras().getString("date");
        change_log = getIntent().getExtras().getString("change_log");

        ArrayList<OrderAssembly> oa = compuStore.getEspecificOrderAssembly(orderID);
        assemblies2 = new ArrayList<Assembly>();
        AssembliesIDs=new ArrayList<Integer>();

        if(Llenarconensambleid) {
            for (OrderAssembly ora : oa) {
                for (Assembly a : compuStore.getAllAssemblies()) {

                    if (ora.getAssembly_id() == a.getId()) {
                        assemblies2.add(a);
                    }
                }
            }
            Llenarconensambleid =false;
        }
        else{
            if(savedInstanceState != null){
                for (int i=0;i<assemblies2.size();i++) {
                    assemblies2.remove(i);
                }
                AssembliesIDs = savedInstanceState.getIntegerArrayList(KEY_RecyclerAssemblies1);

                for (Integer i:AssembliesIDs) {
                    Assembly a = compuStore.getAssemblyFromId(i);
                    assemblies2.add(a);
                }
            }
        }

        A_adapter = new AssemblyAdapter(assemblies2);
        assemblyRV.setAdapter(A_adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            Llenarconensambleid=false;
            Toast.makeText(ModificarOrden.this, "Sali sin modificar", Toast.LENGTH_SHORT).show();
        }else {
            if (requestCode == 2) {
                int AssemblyID = data.getIntExtra("AssemblyID", -1);
                Assembly assembly = compuStore.getAssemblyFromId(AssemblyID);
                boolean duplicado = false;
                for (Assembly assembly1 : assemblies2) {
                    if (assembly1.getId() == assembly.getId()) {
                        duplicado = true;
                    }
                }
                if (duplicado) {
                    Toast.makeText(ModificarOrden.this, "El ensamble ya está en la orden", Toast.LENGTH_SHORT).show();
                } else {
                    //product.setQuantity(1);
                    assemblies2.add(assembly);
                    A_adapter = new AssemblyAdapter(assemblies2);
                    assemblyRV.setAdapter(A_adapter);
                    Toast.makeText(ModificarOrden.this, "Agregado a la orden", Toast.LENGTH_SHORT).show();
                }
                Llenarconensambleid=false;
            }
        }

    }

    public void btnguardar (View v){

        //compuStore.deleteOrderAssembly(orderID);
        //compuStore.deleteOrder(orderID, true);
        ////Agregar orden
        //compuStore.insertOrder(status_id,customer_id,date,change_log);
        //int assID = compuStore.getAssemblyid(descrip.getText().toString());
        ////Agregar cada producto del ensamble con cantidades
        //for(Product p :products2){
        //    compuStore.insertAssemblyproducts(idensa,p.getId(),p.getQuantity());
        //}
//
        //Toast.makeText(ModificarEnsamble.this, "Ensamble modificado", Toast.LENGTH_SHORT).show();
        //finish();
    }

}
