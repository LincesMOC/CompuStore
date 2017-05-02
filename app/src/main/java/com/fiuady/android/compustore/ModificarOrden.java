package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.StyleRes;
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
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;
import com.fiuady.db.OrderAssembly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModificarOrden extends AppCompatActivity {

    private CompuStore compuStore;
    private RecyclerView assemblyRV;
    private AssemblyAdapter A_adapter;
    private Assembly current_assembly;
    private Integer current_assembly_qty;
    OrderAssembly orderAssembly = null;

    private Boolean Llenarconensambleid = true ;
    private ArrayList<Integer> AssembliesIDs;

    private final String KEY_RecyclerAssemblies1="recyclerAs1";
    ArrayList<OrderAssembly> orderAssemblies_TEMP = new ArrayList<>();

    ArrayList<Assembly> assemblies2;
    ArrayList<Assembly> deletedAssemblies;

    int orderID;
    int status_id;
    int customer_id;
    String date;
    String change_log;

    ArrayList<OrderAssembly> oa;

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
                                //MODIFICAR CANTIDAD DE ESTE ENSAMBLE EN LA ORDEN
                                AlertDialog.Builder build = new AlertDialog.Builder(ModificarOrden.this);
                              build.setCancelable(false);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_addstock, null);
                                build.setTitle("Cantidad para orden");
                                final Spinner spinstock = (Spinner)view.findViewById(R.id.spinnerstock);
                                ArrayAdapter<String> adapter2= new ArrayAdapter<String>(ModificarOrden.this,android.R.layout.simple_spinner_dropdown_item);
                                //Llenado del adaptador con la cantidad de ensambles en la orden
                                int assemblyQTY=0;

                                //BUSCAR EN LISTA DE OA TEMPORAL el oa buscado, y modificarlo

                                for (OrderAssembly o: orderAssemblies_TEMP) { //LISTA VACÍA EN ESTE MOMENTO
                                    if (o.getAssembly_id() == assembly.getId()){
                                        assemblyQTY = o.getQty();
                                    }
                                }

                                //for (OrderAssembly o: orderAssemblies_TEMP) {
                                //    if (o.getAssembly_id() == assembly.getId()){
                                //        assemblyQTY = o.getQty();
                                //    }
                                //}

                                for(int i=assemblyQTY;i<assemblyQTY+10;i++){
                                    adapter2.add(Integer.toString(i));
                                }
                                spinstock.setAdapter(adapter2);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        for (OrderAssembly o: orderAssemblies_TEMP) {
                                            if (o.getAssembly_id() == assembly.getId()){
                                                o.setQty(Integer.parseInt(spinstock.getSelectedItem().toString()));
                                                current_assembly_qty = Integer.parseInt(spinstock.getSelectedItem().toString());
                                            }
                                        }
                                        Toast.makeText(ModificarOrden.this,"El valor fue actualizado", Toast.LENGTH_SHORT).show();
                                        current_assembly = assembly;

                                    }
                                });
                                build.setView(view);
                                AlertDialog dialog = build.create();
                                dialog.show();
                            }
                            if (item.getTitle().toString().equalsIgnoreCase("Eliminar")) {
                                //ELIMINAR ESTE ENSAMBLE DE LA ORDEN
                                AlertDialog.Builder build = new AlertDialog.Builder(ModificarOrden.this);
                                build.setCancelable(false);
                                build.setTitle("Eliminar ensamble de orden");
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        deletedAssemblies.add(assembly);
                                        assemblies2.remove(assembly);

                                        A_adapter = new AssemblyAdapter(assemblies2);
                                        assemblyRV.setAdapter(A_adapter);
                                        Toast.makeText(ModificarOrden.this, "Ensamble eliminado de orden", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_orden);
        compuStore= new CompuStore(ModificarOrden.this);
        assemblyRV = (RecyclerView)findViewById(R.id.modifyOrder_assemblies_RV);

        //LANDSCAPE
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


        oa = compuStore.getEspecificOrderAssembly(orderID); //Lista de ensambles por orden determinada!

        for (OrderAssembly orderass: oa){
            orderAssemblies_TEMP.add(orderass);
        }


        assemblies2 = new ArrayList<Assembly>(); //Lista de ensambles para el Recycler View
        deletedAssemblies = new ArrayList<Assembly>();
        AssembliesIDs=new ArrayList<Integer>(); //Lista de IDs de ensambles

        if(savedInstanceState != null){

            assemblies2.clear();
            AssembliesIDs = savedInstanceState.getIntegerArrayList(KEY_RecyclerAssemblies1);

            for (Integer i:AssembliesIDs) {
                Assembly a = compuStore.getAssemblyFromId(i);
                assemblies2.add(a);
            }

            Llenarconensambleid = false;
        }

        if(Llenarconensambleid) {
            for (OrderAssembly ora : oa) { //Para cada OrderAssembly en la lista de ensambles por orden determinada
                for (Assembly a : compuStore.getAllAssemblies()) { //Para cada ensamble de la lista de ensambles
                    if (ora.getAssembly_id() == a.getId()) {
                        compuStore.getOrderAssembly(a.getId(),orderID).setQty(ora.getQty());
                        assemblies2.add(a);
                    }
                }
            }
            Llenarconensambleid =false; //Ya la llenaste
        }

        Collections.sort(assemblies2, new Comparator<Assembly>() {
            @Override
            public int compare(Assembly o1, Assembly o2) {
                return o1.getDescription().compareTo(o2.getDescription());
            }
        });

        A_adapter = new AssemblyAdapter(assemblies2);
        assemblyRV.setAdapter(A_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(ModificarOrden.this,AgregarEnsambleParaOrden.class);
        startActivityForResult(i,2);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            Llenarconensambleid=false;
            //Toast.makeText(ModificarEnsamble.this, "Sali sin modificar", Toast.LENGTH_SHORT).show();
        }else {

            if (requestCode == 2) {
                int assemblyId = data.getIntExtra("AssemblyId", -1);
                orderAssembly = new OrderAssembly(orderID,assemblyId,1);

                Assembly assembly = compuStore.getAssemblyFromId(assemblyId);
                orderAssemblies_TEMP.add(orderAssembly);

                boolean duplicado = false;
                for (Assembly assembly1 : assemblies2) {
                    if (assembly1.getId() == assembly.getId()) {
                        duplicado = true;
                    }
                }
                if (duplicado) {
                    Toast.makeText(ModificarOrden.this, "El ensamble ya está en la orden", Toast.LENGTH_SHORT).show();
                } else {
                    assemblies2.add(assembly);

                    Collections.sort(assemblies2, new Comparator<Assembly>() {
                        @Override
                        public int compare(Assembly o1, Assembly o2) {
                            return o1.getDescription().compareTo(o2.getDescription());
                        }
                    });

                    A_adapter = new AssemblyAdapter(assemblies2);
                    assemblyRV.setAdapter(A_adapter);
                    Toast.makeText(ModificarOrden.this, "Agregado a la orden", Toast.LENGTH_SHORT).show();
                }
                Llenarconensambleid=false;
            }
        }
    }

    public void btnguardar (View v){

        //for (OrderAssembly oaTemp : orderAssemblies_TEMP){ //ACTUALIZANDO, PERO NO INSERTANDO
        //    //for (Assembly a: assemblies2) {
        //    //    if (oaTemp.getAssembly_id() == a.getId()) {
        //    //        compuStore.insertOrderAssembly(orderID, oaTemp.getAssembly_id(), oaTemp.getQty());
        //    //
        //    //    }
        //    //}
        //    compuStore.updateOrderAssembly(orderID,oaTemp.getAssembly_id(),oaTemp.getQty());
        //}

        oa = compuStore.getEspecificOrderAssembly(orderID);

        for (OrderAssembly oaTemp : orderAssemblies_TEMP){
            for (OrderAssembly oa2: oa){
                if (oaTemp.getId() == oa2.getId() && oaTemp.getAssembly_id() == oa2.getAssembly_id()){
                    compuStore.updateOrderAssembly(orderID,oaTemp.getAssembly_id(),oaTemp.getQty());
                } else{
                    compuStore.insertOrderAssembly(orderID,oaTemp.getAssembly_id(),oaTemp.getQty());
                }
            }
        }


        for (Assembly a: deletedAssemblies){
            compuStore.deleteOrderAssembly2(orderID,a.getId());
        }

        Toast.makeText(ModificarOrden.this, "Orden modificada", Toast.LENGTH_SHORT).show();
        finish();
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

        for (Assembly a:assemblies2) {
            AssembliesIDs.add(a.getId());
        }

        Llenarconensambleid=false;
        outState.putIntegerArrayList(KEY_RecyclerAssemblies1,AssembliesIDs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Llenarconensambleid=false;
    }

}
