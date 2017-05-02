package com.fiuady.android.compustore;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;
import com.fiuady.db.CompuStoreDbSchema;
import com.fiuady.db.MissingProduct;
import com.fiuady.db.Sale;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SalesActivity extends AppCompatActivity {

    class ListViewAdapter extends BaseAdapter {
        private ArrayList<Sale> arrayList;
        private Context context;
        private LayoutInflater layoutInflater;

        private ListViewAdapter(ArrayList<Sale> arrayList, Context context) {
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
            TextView txtEnsamble = (TextView) view.findViewById(R.id.txt_ensamble);
            TextView txtFecha = (TextView) view.findViewById(R.id.txt_fecha);
            TextView txtMonto = (TextView) view.findViewById(R.id.txt_monto);

            txtOrden.setTextSize(20);
            txtOrden.setText("Orden: " + Integer.toString(arrayList.get(position).getOrder_id()));
            txtFecha.setText("Fecha: " + arrayList.get(position).getDate());
            txtEnsamble.setText("Ensam: " + Integer.toString(arrayList.get(position).getAssembly_id()));
            txtMonto.setText("$ " + Double.toString(arrayList.get(position).getPrice()*.01));

            return view;
        }
    }


    private class SalesHolder extends RecyclerView.ViewHolder {
        private TextView txtMes;
        private TextView txtAño;
        private TextView txtMonto;
        private CardView cardView;

        public SalesHolder(View itemView) {
            super(itemView);
            txtMes = (TextView) itemView.findViewById(R.id.month_of_year);
            txtMonto = (TextView) itemView.findViewById(R.id.sales_of_month);
            txtAño = (TextView) itemView.findViewById(R.id.year);
            cardView = (CardView) itemView.findViewById(R.id.cv);
        }

        public void bindSalesHolder(final VentaMensual ventaMensual) {

            txtMes.setText(Mes(ventaMensual.getMes()));
            txtAño.setText(Integer.toString(ventaMensual.getAño()));
            txtMonto.setText("$ " + Double.toString(ventaMensual.getMonto()*.01));
            cardView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListViewAdapter adapter = null;

                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
                    final View view = getLayoutInflater().inflate(android.R.layout.list_content, null);

                    adapter = new ListViewAdapter(getVentasPorMes(ventaMensual.getMes(), ventaMensual.getAño()), SalesActivity.this);
                    builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                        }
                    });


        }
    }

    private class SalesAdapter extends RecyclerView.Adapter<SalesActivity.SalesHolder> {

        private List<VentaMensual> salesList;

        public SalesAdapter(List<VentaMensual> salesList) {
            this.salesList = salesList;
        }

        @Override
        public void onBindViewHolder(SalesActivity.SalesHolder holder, int position) {
            holder.bindSalesHolder(salesList.get(position));
        }

        @Override
        public SalesActivity.SalesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cardview_item, parent, false);
            return new SalesHolder(view);
        }

        @Override
        public int getItemCount() {
            return salesList.size();
        }
    }

    private CompuStore compuStore;
    private SalesAdapter salesAdapter;
    private RecyclerView mpRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        compuStore = new CompuStore(this);

        mpRV = (RecyclerView) findViewById(R.id.activity_sales);
        mpRV.setLayoutManager(new LinearLayoutManager(this));
        salesAdapter = new SalesAdapter(getVentasMensuales());
        mpRV.setAdapter(salesAdapter);
    }

    public ArrayList<VentaMensual> getVentasMensuales() {
        List<Sale> s = compuStore.getAllSales();
        ArrayList<VentaMensual> vm = new ArrayList<>();
        VentaMensual ventaMensual;

        int m = 13;
        int p = 0;
        int y = 0;

        for (Sale sale : s) {
            String[] array = sale.getDate().split("-",3);
            int year=Integer.parseInt(array[0]);
            int month=Integer.parseInt(array[1]);

            if (m == month || m == 13) {
                m = month;
                y = year;
                p = p + sale.getPrice();

                if (s.get(s.size()-1) == sale) {
                    ventaMensual = new VentaMensual(y, m, p);
                    vm.add(ventaMensual);
                }
            }
            else {
                ventaMensual = new VentaMensual(y, m, p);
                vm.add(ventaMensual);
                m = month;
                y = year;
                p = sale.getPrice();

                if (s.get(s.size()-1) == sale) {
                    ventaMensual = new VentaMensual(y, m, p);
                    vm.add(ventaMensual);
                }
            }
        }

        return vm;
    }

    public ArrayList<Sale> getVentasPorMes(int mes, int año) {
        List<Sale> s = compuStore.getAllSales();
        ArrayList<Sale> vm = new ArrayList<>();

        for (Sale sale : s) {
            String[] array = sale.getDate().split("-",3);
            int year = Integer.parseInt(array[0]);
            int month = Integer.parseInt(array[1]);

            if (mes == month && año == year) {
               vm.add(sale);
            }
        }

        return vm;
    }

    public String Mes(int i) {
        String mes;
        switch (i) {
            case 1:
                mes = "Enero";
            break;

            case 2:
                mes = "Febrero";
            break;

            case 3:
                mes = "Marzo";
            break;

            case 4:
                mes = "Abril";
            break;

            case 5:
                mes = "Mayo";
            break;

            case 6:
                mes = "Junio";
            break;

            case 7:
                mes = "Julio";
            break;

            case 8:
                mes = "Agosto";
            break;

            case 9:
                mes = "Septiembre";
            break;

            case 10:
                mes = "Octubre";
            break;

            case 11:
                mes = "Noviembre";
            break;

            case 12:
                mes = "Diciembre";
            break;

            default:
                mes = "n/a";
                break;
        }
        return mes;
    }


}

class VentaMensual {
    private int año;
    private int mes;
    private int monto;

    public VentaMensual (int año, int mes, int monto) {
        this.año = año;
        this.mes = mes;
        this.monto = monto;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }
}
