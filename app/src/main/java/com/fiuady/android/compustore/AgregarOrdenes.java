package com.fiuady.android.compustore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;

import java.util.List;

public class AgregarOrdenes extends AppCompatActivity {

    private RecyclerView assemblyRV;
    private AssemblyAdapter A_adapter;
    private CompuStore compuStore;

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

        compuStore = new CompuStore(this);

        assemblyRV = (RecyclerView)findViewById(R.id.addOrder_assemblies_RV);
        assemblyRV.setLayoutManager(new LinearLayoutManager(this));

        A_adapter = new AssemblyAdapter(compuStore.getAllAssemblies());
        assemblyRV.setAdapter(A_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //AGREGAR ENSAMBLE PARA ÓRDENES
        return super.onOptionsItemSelected(item);
    }
}
