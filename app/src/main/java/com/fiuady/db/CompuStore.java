package com.fiuady.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.fiuady.db.CompuStoreDbSchema.*;

class CategoryCursor extends CursorWrapper {
    public CategoryCursor(Cursor cursor) {
        super(cursor);
    }

    public Category getCategory(){
        Cursor cursor = getWrappedCursor();
        return new Category(cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.CategoriesTable.Columns.ID)),cursor.getString(cursor.getColumnIndex(CompuStoreDbSchema.CategoriesTable.Columns.DESCRIPTION)));
    }
}

class ProductCursor extends CursorWrapper {
    public ProductCursor(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct(){
        Cursor cursor = getWrappedCursor();
        return new Product(cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.ProductsTable.Columns.ID)),cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndex(CompuStoreDbSchema.ProductsTable.Columns.DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.PRICE)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.QUANTITY)));
    }
}

class AssemblyProductCursor extends CursorWrapper{
    public AssemblyProductCursor(Cursor cursor){super(cursor);}

    public AssemblyProduct getAssemblyProduct(){
        Cursor cursor = getWrappedCursor();
        return new AssemblyProduct(cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.PRODUCT_ID)),
                cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.QUANTITY)));
    }
}

class ClientCursor extends CursorWrapper {
    public ClientCursor(Cursor cursor) {
        super(cursor);
    }

    public Client getClient(){
        Cursor cursor = getWrappedCursor();
        return new Client(cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.CustomersTable.Columns.ID)),cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.LAST_NAME)),cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.ADDRESS)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.E_MAIL)),cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.PHONE1)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.PHONE2)),cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.PHONE3)));
    }
}

class OrderCursor extends CursorWrapper {
    public OrderCursor(Cursor cursor) {
        super(cursor);
    }

    public Order getOrder(){
        Cursor cursor = getWrappedCursor();
        return new Order(cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.OrdersTable.Columns.ID)),cursor.getInt(cursor.getColumnIndex(OrdersTable.Columns.STATUS_ID)),
                cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.OrdersTable.Columns.CUSTOMER_ID)),cursor.getString(cursor.getColumnIndex(OrdersTable.Columns.DATE)),
                cursor.getString(cursor.getColumnIndex(OrdersTable.Columns.CHANGE_LOG)));
    }
}

public final class CompuStore {
    private CompuStoreHelper compuStoreHelper;
    private SQLiteDatabase db;

    private List<Category> categories;

    public CompuStore(Context context) {
        compuStoreHelper = new CompuStoreHelper(context);
        db = compuStoreHelper.getWritableDatabase();
        compuStoreHelper.backupDatabasefile(context);
    }

    // ------------------------------------------------------ CATEGORIES --------------------------------------------------------

    public List<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();

        CategoryCursor cursor = new CategoryCursor(db.rawQuery("SELECT * FROM product_categories ORDER BY id", null));
        while(cursor.moveToNext()){
            list.add(cursor.getCategory());
        }
        cursor.close();

        return list;
    }

    public boolean updateCategory(String des, int id) {
        boolean b = true;
        List<Category> a = getAllCategories();

        if (des.isEmpty()) {
            b = false;
        }

        for(Category category : a) {
            if (category.getDescription().toUpperCase().equals(des.toUpperCase())) {
                b = false;
            }
        }

        if (b) {
            ContentValues values = new ContentValues();
            values.put(CategoriesTable.Columns.DESCRIPTION, des);

            db.update(CategoriesTable.NAME,
                    values,
                    CategoriesTable.Columns.ID+ "= ?",
                    new String[] {Integer.toString(id)});
        }

        return b;
    }

    public boolean insertCategory(String text) {
        boolean b = true;
        List<Category> a = getAllCategories();
        ContentValues values = new ContentValues();

        if (text.isEmpty()) {
            b = false;
        }

        for(Category category : a) {
            if (category.getDescription().toUpperCase().equals(text.toUpperCase())) {

                b = false;
            }
        }

        if (b) {
            Category c = a.get(a.size()-1);

            values.put(CategoriesTable.Columns.DESCRIPTION, text);

            db.insert(CategoriesTable.NAME, null, values);
        }

        return b;
    }

    public boolean deleteCategory(int id, boolean dlt) {
        boolean c = false;
        boolean d = true;
        boolean e = true;
        List<Category> a = getAllCategories();
        List<Product> b = getAllProducts();

        for(Category category : a) {
            if (e) {
                if (category.getId() == id) {  // Condicion si la categoria ya exite en categorias
                    e = false;
                    if (d) {
                        for(Product product : b) {
                            if (product.getCategory_id() == id) {  // Condicion si algun producto tiene asignado la categoria
                                c = true;
                                d = false;
                            }
                            else {
                                if (dlt){  // Quiero elimanrlo?
                                    db.delete(CategoriesTable.NAME, CategoriesTable.Columns.ID + "= ?",
                                            new String[] {Integer.toString(id)});
                                }
                            }
                        }
                    }
                }
            }
        }

        return  c;
    }

    // -------------------------------------------------------- PRODUCTS --------------------------------------------------------

    public List<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products ORDER BY id", null));
        while(cursor.moveToNext()) {
            list.add(cursor.getProduct());
        }
        cursor.close();

        return list;
    }

    public boolean updateProduct(String des, int id, int category_id, int precio, int qty) {
        boolean b = true;
        List<Product> a = getAllProducts();

        if (des.isEmpty()) {
            b = false;
        }

        for(Product product : a) {
            if (product.getDescription().toUpperCase().equals(des.toUpperCase())) {
                b = false;
            }
        }

        if (b) {
            ContentValues values = new ContentValues();
            values.put(ProductsTable.Columns.DESCRIPTION, des);
            values.put(ProductsTable.Columns.CATEGORY_ID, category_id);
            values.put(ProductsTable.Columns.PRICE, precio);
            values.put(ProductsTable.Columns.QUANTITY, qty);

            db.update(ProductsTable.NAME,
                    values,
                    ProductsTable.Columns.ID+ "= ?",
                    new String[] {Integer.toString(id)});
        }

        return b;
    }

    public boolean insertProduct(String text, int id, int category_id, int precio, int qty) {
        boolean b = true;
        List<Product> a = getAllProducts();
        ContentValues values = new ContentValues();

        if (text.isEmpty()) {
            b = false;
        }

        for(Product product : a) {
            if (product.getDescription().toUpperCase().equals(text.toUpperCase())) {
                b = false;
            }
        }

        if (b) {
            Product c = a.get(a.size()-1);

            values.put(CategoriesTable.Columns.DESCRIPTION, text);
            values.put(ProductsTable.Columns.CATEGORY_ID, category_id);
            values.put(ProductsTable.Columns.PRICE, precio);
            values.put(ProductsTable.Columns.QUANTITY, qty);

            db.insert(CategoriesTable.NAME, null, values);
        }

        return b;
    }

    public boolean deleteProduct(int id, boolean dlt) {
        boolean c = false;
        boolean d = true;
        boolean e = true;
        List<Product> a = getAllProducts();
        List<AssemblyProduct> b = getAllAssemblyProducts();

        for(Product product : a) {
            if (e) {
                if (product.getId() == id) {  // Condicion si la categoria ya exite en categorias
                    e = false;
                    if (d) {
                        for(AssemblyProduct assemblyProduct : b) {
                            if (assemblyProduct.getProduct_id() == id) {  // Condicion si algun producto tiene asignado la categoria
                                c = true;
                                d = false;
                            }
                            else {
                                if (dlt){  // Quiero elimanrlo?
                                    db.delete(ProductsTable.NAME, ProductsTable.Columns.ID + "= ?",
                                            new String[] {Integer.toString(id)});
                                }
                            }
                        }
                    }
                }
            }
        }

        return  c;
    }

    // ----------------------------------------------- AssemblyProducts --------------------------------------------------------
    public List<AssemblyProduct> getAllAssemblyProducts(){
        ArrayList<AssemblyProduct> list = new ArrayList<>();

        AssemblyProductCursor cursor = new AssemblyProductCursor(db.rawQuery("select * from assembly_products order by id",null));
        while (cursor.moveToNext()){
            list.add(cursor.getAssemblyProduct());
        }
        cursor.close();

        return list;
    }



    // -------------------------------------------------------- CLIENTS --------------------------------------------------------

    public List<Client> getAllClients() {
        ArrayList<Client> list = new ArrayList<>();

        ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers ORDER BY last_name", null));
        while(cursor.moveToNext()){
            list.add(cursor.getClient());
        }
        cursor.close();

        return list;
    }

    public boolean updateClient(String firstName, String lastName, String address, String email, String phone1,
                                String phone2, String phone3,int id) {
        boolean b = true;
        List<Client> a = getAllClients();

        if (firstName.isEmpty()||lastName.isEmpty()||address.isEmpty()) {
            b = false;
        }

        if (b) {
            ContentValues values = new ContentValues();

            values.put(CustomersTable.Columns.FIRST_NAME, firstName);
            values.put(CustomersTable.Columns.LAST_NAME, lastName);
            values.put(CustomersTable.Columns.ADDRESS,address);
            values.put(CustomersTable.Columns.E_MAIL, email);
            values.put(CustomersTable.Columns.PHONE1, phone1);
            values.put(CustomersTable.Columns.PHONE2,phone2);
            values.put(CustomersTable.Columns.PHONE3,phone3);

            db.update(CustomersTable.NAME,
                    values,
                    CustomersTable.Columns.ID+ "= ?",
                    new String[] {Integer.toString(id)});
        }

        return b;
    }

    public boolean insertClient(String firstName, String lastName, String address, String email, String phone1,
                                String phone2, String phone3) {
        boolean b = true;
        List<Client> a = getAllClients();
        ContentValues values = new ContentValues();

        if (firstName.isEmpty()||lastName.isEmpty()||address.isEmpty()) {
            b = false;
        }

        for(Client client : a) {

            if(client.getFirstName().toUpperCase().equals(firstName.toUpperCase()) &&
                    client.getLastName().toUpperCase().equals(lastName.toUpperCase())){
                b=false;
            }

        }

        if (b) {
            Client c = a.get(a.size()-1);

            values.put(CustomersTable.Columns.FIRST_NAME, firstName);
            values.put(CustomersTable.Columns.LAST_NAME, lastName);
            values.put(CustomersTable.Columns.ADDRESS,address);
            values.put(CustomersTable.Columns.E_MAIL, email);
            values.put(CustomersTable.Columns.PHONE1, phone1);
            values.put(CustomersTable.Columns.PHONE2,phone2);
            values.put(CustomersTable.Columns.PHONE3,phone3);

            db.insert(CustomersTable.NAME,null,values);
        }

        return b;
    }

    public boolean deleteClient(int id, boolean dlt) {
        boolean c = false;
        boolean d = true;
        boolean e = true;

        List<Client> a = getAllClients();
        List<Order> b = getAllOrders();

        for(Client client : a) {
            if (e) {
                if (client.getId() == id) {  // Condición si la categoría ya existe en categorias
                    e = false;
                    if (d) {
                        for(Order order : b) {
                            if (order.getCustomer_id() == id) {  // Condicion si algún cliente tiene asignado una orden
                                c = true;
                                d = false;
                            }
                            else {
                                if (dlt){  // Quiero elimanrlo?
                                    db.delete(CustomersTable.NAME, CustomersTable.Columns.ID + "= ?",
                                            new String[] {Integer.toString(id)});
                                }
                            }
                        }
                    }
                }
            }
        }

        return  c;
    }


    // -------------------------------------------------------- ORDERS --------------------------------------------------------

    public List<Order> getAllOrders() {
        ArrayList<Order> list = new ArrayList<>();

        OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders ORDER BY id", null));
        while(cursor.moveToNext()){
            list.add(cursor.getOrder());
        }
        cursor.close();
        return list;
    }


}
