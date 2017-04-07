package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

public class AssembliesActivity extends AppCompatActivity {

//    private class AssembliesHolder extends RecyclerView.ViewHolder {
//
//        private TextView txtDescription;
//
//        public AssembliesHolder(View itemView) {
//            super(itemView);
//            txtDescription = (TextView) itemView.findViewById(android.R.id.text1);
//        }
//
//        public void bindAssembly(final Assembly assembly) {
//
//            txtDescription.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    final PopupMenu popup = new PopupMenu(AssembliesActivity.this, txtDescription);
//                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());
//
////                    if (compuStore.deleteCategory(category.getId(), false)) {
////                        popup.getMenu().removeItem(R.id.menu_item2);
////                    }
//
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//
//                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AssembliesActivity.this);
//                                final View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
//                                TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
//                                final EditText txtAdd = (EditText) view.findViewById(R.id.add_text);
//
//                                txtTitle.setText(R.string.category_update);
//
//                                builder.setCancelable(false);
//                                builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.dismiss();
//                                    }
//                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        AlertDialog.Builder build = new AlertDialog.Builder(CategoriesActivity.this);
//                                        build.setCancelable(false);
//                                        build.setTitle(getString(R.string.category_update));
//                                        build.setMessage(R.string.sure_text);
//
//                                        build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.dismiss();
//                                            }
//                                        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                if (compuStore.updateCategory(txtAdd.getText().toString(), category.getId())) {
//                                                    Toast.makeText(CategoriesActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
//                                                    adapter = new CategoriesActivity.CategoryAdapter(compuStore.getAllCategories());
//                                                    categoryRV.setAdapter(adapter);
//                                                } else {
//                                                    Toast.makeText(CategoriesActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//
//                                        build.create().show();
//                                    }
//                                });
//                                builder.setView(view);
//                                AlertDialog dialog = builder.create();
//                                dialog.show();
//                            }
//
//                            else{
//                                AlertDialog.Builder build = new AlertDialog.Builder(CategoriesActivity.this);
//                                build.setCancelable(false);
//                                build.setTitle(getString(R.string.category_delete));
//                                build.setMessage(R.string.sure_text);
//
//                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.dismiss();
//                                    }
//                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        Toast.makeText(CategoriesActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
//                                        compuStore.deleteCategory(category.getId(), true);
//                                        adapter = new CategoriesActivity.CategoryAdapter(compuStore.getAllCategories());
//                                        categoryRV.setAdapter(adapter);
//                                    }
//                                });
//
//                                build.create().show();
//                            }
//                            return true;
//                        }
//                    });
//                    popup.show();
//
//
//                }
//            });
//
//            txtDescription.setText(category.getDescription());
//        }
//    }
//
//    private CompuStore compuStore;
//    private RecyclerView categoryRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemblies);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Aqui va agregar ensamble en otra activity
                //Esa otra activity lleva a otra activity

        return super.onOptionsItemSelected(item);
    }
}