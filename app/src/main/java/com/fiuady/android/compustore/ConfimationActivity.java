package com.fiuady.android.compustore;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;
import com.fiuady.db.Sale;

import java.util.ArrayList;
import java.util.List;

public class ConfimationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public class ListViewAdapter extends BaseAdapter {
        private ArrayList<Product> arrayList;
        private Context context;
        private LayoutInflater layoutInflater;

        public ListViewAdapter(ArrayList<Product> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =  layoutInflater.inflate(R.layout.ventas_mensuales_list_item, parent, false);

            TextView txtOrden = (TextView) view.findViewById(R.id.txt_orden);
            TextView txtFecha = (TextView) view.findViewById(R.id.txt_fecha);
            TextView txtMonto = (TextView) view.findViewById(R.id.txt_monto);

            txtOrden.setTextSize(12);
            txtFecha.setTextSize(10);
            txtMonto.setTextSize(10);

            txtOrden.setText("Producto: " + arrayList.get(position).getDescription());
            txtOrden.setText("Producto: " + arrayList.get(position).getDescription());
            txtFecha.setText("En Existencia: " + Integer.toString(arrayList.get(position).getQuantity()));
            txtMonto.setText("Se Requiere: " + Integer.toString(arrayList.get(position).getPrice()));
            if (arrayList.get(position).getQuantity()-arrayList.get(position).getPrice() >= 0) {
                txtMonto.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
            else {
                txtMonto.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            return view;
        }
    }

    private class SalesHolder extends RecyclerView.ViewHolder {
        private TextView txtFecha;
        private TextView txtNombre;
        private TextView txtOrder_ID;
        private TextView txtMonto;
        private CardView cardView;

        public SalesHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            txtNombre = (TextView) itemView.findViewById(R.id.month_of_year);
            txtMonto = (TextView) itemView.findViewById(R.id.sales_of_month);
            txtFecha = (TextView) itemView.findViewById(R.id.txt_name);
            txtOrder_ID = (TextView) itemView.findViewById(R.id.year);
        }

        public void bindSalesHolder(final Sale sale) {
            txtNombre.setText(sale.getName());
            txtFecha.setText("Fecha: " + sale.getDate());
            txtMonto.setText("$ " + Double.toString(sale.getPrice()*.01));
            txtOrder_ID.setText("Orden: 0" + Integer.toString(sale.getOrder_id()));

            switch (sale.getAssembly_id()) {
                case 0:
                    cardView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    break;

                case 1:
                    cardView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;

                case 2:
                    cardView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;

                case 3:
                    cardView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;

                case 4:
                    cardView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    break;
                default:
                    break;
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (sale.getAssembly_id()) {
                        case 0:
                            ConfimationActivity.ListViewAdapter adapter = null;

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfimationActivity.this);
                            final View view = getLayoutInflater().inflate(android.R.layout.list_content, null);

                            adapter = new ListViewAdapter(getProductoFaltantePorOrden(sale.getOrder_id()), ConfimationActivity.this);
                            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {


                                public void onClick(DialogInterface dialog, int id) {
                                    int i = 1;
                                    ArrayList<Product> productArrayList = getProductoFaltantePorOrden(sale.getOrder_id());

                                    for (Product product : productArrayList) {
                                        if (product.getQuantity()-product.getPrice() < 0) {
                                            if (product == productArrayList.get(productArrayList.size()-1)) {
                                                Toast.makeText(getApplicationContext(), "Faltan Productos", Toast.LENGTH_SHORT).show();
                                            }
                                            i = 0;
                                        }
                                        else {
                                            if (product == productArrayList.get(productArrayList.size()-1) && i == 1) {
                                                Toast.makeText(getApplicationContext(), "Orden Confirmada", Toast.LENGTH_SHORT).show();
                                                confirmarOrden(sale.getOrder_id());
                                            }
                                            else {
                                                if (product == productArrayList.get(productArrayList.size()-1)) {
                                                    Toast.makeText(getApplicationContext(), "Faltan Productos", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }


                                }
                            });

                            builder.create().show();
                            break;

                        case 1:
                            Toast.makeText(getApplicationContext(), "Orden Cancelada", Toast.LENGTH_SHORT).show();
                            break;

                        case 2:
                            Toast.makeText(getApplicationContext(), "Orden Confirmada", Toast.LENGTH_SHORT).show();
                            break;

                        case 3:
                            Toast.makeText(getApplicationContext(), "Orden En Transito", Toast.LENGTH_SHORT).show();
                            break;

                        case 4:
                            Toast.makeText(getApplicationContext(), "Orden Finalizada", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private class SalesAdapter extends RecyclerView.Adapter<ConfimationActivity.SalesHolder> {

        private List<Sale> salesList;

        public SalesAdapter(List<Sale> salesList) {
            this.salesList = salesList;
        }

        @Override
        public void onBindViewHolder(ConfimationActivity.SalesHolder holder, int position) {
            holder.bindSalesHolder(salesList.get(position));
        }

        @Override
        public ConfimationActivity.SalesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cardview_item, parent, false);
            return new ConfimationActivity.SalesHolder(view);
        }

        @Override
        public int getItemCount() {
            return salesList.size();
        }
    }

    private CompuStore compuStore;
    private ConfimationActivity.SalesAdapter confirmationAdapter;
    private RecyclerView mpRV;
    private Spinner spinner;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                confirmationAdapter = new ConfimationActivity.SalesAdapter(compuStore.getSalesConfimationbyName());
                mpRV.setAdapter(confirmationAdapter);
                break;

            case 1:
                confirmationAdapter = new ConfimationActivity.SalesAdapter(compuStore.getSalesConfimationbyDate());
                mpRV.setAdapter(confirmationAdapter);
                break;

            case 2:
                confirmationAdapter = new ConfimationActivity.SalesAdapter(compuStore.getSalesConfimationbyPrice());
                mpRV.setAdapter(confirmationAdapter);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // vacio
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_activity);

        compuStore = new CompuStore(this);

        mpRV = (RecyclerView) findViewById(R.id.activity_confimation);
        mpRV.setLayoutManager(new LinearLayoutManager(this));

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filtro, android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(adapter);

        confirmationAdapter = new ConfimationActivity.SalesAdapter(compuStore.getSalesConfimationbyName());
        mpRV.setAdapter(confirmationAdapter);
    }

    public ArrayList<Product> getProductoFaltantePorOrden(int orden) {
        List<Sale> s = compuStore.getConfirmationSalesByOrder(orden);
        ArrayList<Product> vm = new ArrayList<>();

        for (Sale sale : s) {
            Product product = compuStore.getProductfromid(sale.getAssembly_id());
            product.setPrice(sale.getPrice());
            vm.add(product);
        }

        return vm;
    }

    public void confirmarOrden(int orden) {
        List<Sale> s = compuStore.getConfirmationSalesByOrder(orden);
        ArrayList<Product> vm = new ArrayList<>();

        for (Sale sale : s) {
            Product product = compuStore.getProductfromid(sale.getAssembly_id());
            compuStore.updateProductstock(product.getDescription(), product.getId(), product.getCategory_id(),
                    product.getPrice(), product.getQuantity()-sale.getPrice());
        }

        // compuStore.updateOrder(orden, 2, "Modificado desde el simulador de ordenes");
    }
}
