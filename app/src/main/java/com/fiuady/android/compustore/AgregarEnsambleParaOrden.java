package com.fiuady.android.compustore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AgregarEnsambleParaOrden extends AppCompatActivity {

    private class AssemblyHolder extends RecyclerView.ViewHolder{

        private TextView txtDescription;

        public AssemblyHolder(View itemView) {
            super(itemView);
            txtDescription = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bindAssembly(final Assembly assembly){
            txtDescription.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    final PopupMenu popup = new PopupMenu(AgregarEnsambleParaOrden.this,txtDescription);
                    popup.getMenuInflater().inflate(R.menu.option1_menu,popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().toString().equalsIgnoreCase("Agregar")) {
                                Intent i = new Intent();
                                i.putExtra("Assemblyid",assembly.getId());
                                setResult(2,i);
                                finish();
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

    private class AssemblyAdapter extends RecyclerView.Adapter<AgregarEnsambleParaOrden.AssemblyHolder>{

        private List<Assembly> assemblies;

        public AssemblyAdapter(List<Assembly> assemblies){
            this.assemblies = assemblies;
        }

        @Override
        public AgregarEnsambleParaOrden.AssemblyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new AgregarEnsambleParaOrden.AssemblyHolder(view);
        }
        @Override
        public void onBindViewHolder(AgregarEnsambleParaOrden.AssemblyHolder holder, int position) {
            holder.bindAssembly(assemblies.get(position));
        }
        @Override
        public int getItemCount() {
            return assemblies.size();
        }
    }

    private AssemblyAdapter A_adapter;
    private RecyclerView assemblyRV;
    private CompuStore compuStore;
    private EditText texto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ensamble_para_orden);
        compuStore = new CompuStore(getApplicationContext());

        texto = (EditText) findViewById(R.id.edittextdescripcion);

        assemblyRV = (RecyclerView) findViewById(R.id.assemblies_forOrder_RV);
        assemblyRV.setLayoutManager(new LinearLayoutManager(this));
        A_adapter = new AgregarEnsambleParaOrden.AssemblyAdapter(new ArrayList<Assembly>());
        assemblyRV.setAdapter(A_adapter);


    }
}
