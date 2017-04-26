package com.fiuady.db;

import android.app.assist.AssistStructure;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

class AssemblyCursor extends CursorWrapper {
    public AssemblyCursor(Cursor cursor) {
        super(cursor);
    }

    public Assembly getAssembly(){
        Cursor cursor = getWrappedCursor();
        return new Assembly(cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.AssembliesTable.Columns.ID)),cursor.getString(cursor.getColumnIndex(CompuStoreDbSchema.AssembliesTable.Columns.DESCRIPTION)));
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

class OrderAssembliesCursor extends CursorWrapper {
    public OrderAssembliesCursor(Cursor cursor) {
        super(cursor);
    }

    public OrderAssembly getOrderAssembly(){
        Cursor cursor = getWrappedCursor();
        return new OrderAssembly(cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.OrderAssembliesTable.Columns.ID)),cursor.getInt(cursor.getColumnIndex(OrderAssembliesTable.Columns.ASSEMBLY_ID)),
                cursor.getInt(cursor.getColumnIndex(OrderAssembliesTable.Columns.QUANTITY)));
    }
}

class MissingProductCursor extends CursorWrapper {
    public MissingProductCursor(Cursor cursor) { super(cursor);}

    public MissingProduct getMissingProduct(){
        Cursor cursor = getWrappedCursor();
        return new MissingProduct(cursor.getInt(cursor.getColumnIndex(CompuStoreDbSchema.ProductsTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(CompuStoreDbSchema.ProductsTable.Columns.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.QUANTITY)));
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

    public List<Category> getAllCategoriesid() {
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
//            Category c = a.get(a.size()-1);

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

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products ORDER BY description", null));
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

    public boolean updateProductstock(String des, int id, int category_id, int precio, int qty){

            ContentValues values = new ContentValues();
            values.put(ProductsTable.Columns.DESCRIPTION, des);
            values.put(ProductsTable.Columns.CATEGORY_ID, category_id);
            values.put(ProductsTable.Columns.PRICE, precio);
            values.put(ProductsTable.Columns.QUANTITY, qty);

            db.update(ProductsTable.NAME,
                    values,
                    ProductsTable.Columns.ID+ "= ?",
                    new String[] {Integer.toString(id)});

        return true;
    }

    public boolean insertProduct(String text, int category_id, int precio, int qty) {
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

            values.put(ProductsTable.Columns.DESCRIPTION, text);
            values.put(ProductsTable.Columns.CATEGORY_ID, category_id);
            values.put(ProductsTable.Columns.PRICE, precio);
            values.put(ProductsTable.Columns.QUANTITY, qty);

            db.insert(ProductsTable.NAME, null, values);
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

    public List<Product> filterProducts(int categoryid, String texto){
        ArrayList<Product> products = new ArrayList<>();
        if(texto.isEmpty()){
            if(categoryid == -1){ //texto nada categ todas

                ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products ORDER BY description", null));
                while(cursor.moveToNext()) {
                    products.add(cursor.getProduct());
                }
                cursor.close();

            }else{//texto nada categ algo

                ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products WHERE category_id = "+Integer.toString(categoryid) +" ORDER BY description", null));
                while(cursor.moveToNext()) {
                    products.add(cursor.getProduct());
                }
                cursor.close();
            }

        }else{
            if(categoryid == -1){ //texto algo categ todas

                ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products where description like '%"+texto.toString()+"%' ORDER BY description", null));
                while(cursor.moveToNext()) {
                    products.add(cursor.getProduct());
                }
                cursor.close();

            }else{ //texto algo categorias algo

                ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products where description like '%"+texto.toString()+"%' group by description having category_id = "+Integer.toString(categoryid)+" ORDER BY description", null));
                while(cursor.moveToNext()) {
                    products.add(cursor.getProduct());
                }
                cursor.close();
            }
        }

        //casos categoria todas texto nada, categoria algo texto nada
        //categoria todas texto algo, categoria algo texto algo

        return products;
    }

    public int getProductStock(int id){
        int stock = 0;
        ArrayList<Product> products = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products where id like "+Integer.toString(id),null));
        while(cursor.moveToNext()) {
            products.add(cursor.getProduct());
        }
        cursor.close();

        stock = products.get(0).getQuantity();

        return stock;
    }

    public Product getProductfromid(int id){
        Product product;
        ArrayList<Product> products = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products where id = "+Integer.toString(id),null));
        while(cursor.moveToNext()) {
            products.add(cursor.getProduct());
        }
        cursor.close();

        product = products.get(0);

        return product;
    }

    // -------------------------------------------------------- ASSEMBLIES --------------------------------------------------------



    public List<Assembly> getAllAssemblies() {
        ArrayList<Assembly> list = new ArrayList<>();

        AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies ORDER BY description", null));
        while(cursor.moveToNext()){
            list.add(cursor.getAssembly());
        }
        cursor.close();

        return list;
    }

    public boolean updateAssembly(String des, int id) {
        boolean b = true;
        List<Assembly> a = getAllAssemblies();

        if (des.isEmpty()) {
            b = false;
        }

        for(Assembly assembly : a) {
            if (assembly.getDescription().toUpperCase().equals(des.toUpperCase())) {
                b = false;
            }
        }

        if (b) {
            ContentValues values = new ContentValues();
            values.put(AssembliesTable.Columns.DESCRIPTION, des);

            db.update(AssembliesTable.NAME,
                    values,
                    AssembliesTable.Columns.ID+ "= ?",
                    new String[] {Integer.toString(id)});
        }

        return b;
    }

    public boolean insertAssembly(String text) {
        boolean b = true;
        List<Assembly> a = getAllAssemblies();
        ContentValues values = new ContentValues();

        if (text.isEmpty()) {
            b = false;
        }

        for(Assembly assembly : a) {
            if (assembly.getDescription().toUpperCase().equals(text.toUpperCase())) {

                b = false;
            }
        }

        if (b) {
//            Assembly c = a.get(a.size()-1);

            values.put(AssembliesTable.Columns.DESCRIPTION, text);

            db.insert(AssembliesTable.NAME, null, values);
        }

        return b;
    }

    public boolean deleteAssembly(int id, boolean dlt) {
        boolean c = false;
        boolean d = true;
        boolean e = true;
        List<Assembly> a = getAllAssemblies();
        List<OrderAssembly> b = getAllOrderAssemblies();

        for(Assembly assembly : a) {
            if (e) {
                if (assembly.getId() == id) {  // Condicion si la categoria ya exite en categorias
                    e = false;
                    if (d) {
                        for(OrderAssembly orderAssembly : b) {
                            if (orderAssembly.getAssembly_id() == id) {  // Condicion si algún cliente tiene asignado una orden
                                c = true;
                                d = false;
                            }
                            else {
                                if (dlt){  // Quiero elimanrlo?
                                    db.delete(AssembliesTable.NAME, AssembliesTable.Columns.ID + "= ?",
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

    public List<Assembly> filterAssemblies(String texto){
        ArrayList<Assembly> assemblies = new ArrayList<>();

        if (texto.isEmpty()){
            AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies ORDER BY description", null));
            while(cursor.moveToNext()) {
                assemblies.add(cursor.getAssembly());
            }
            cursor.close();
        } else{

            AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies where description like '%"+texto.toString()+"%' ORDER BY description", null));
            while(cursor.moveToNext()) {
                assemblies.add(cursor.getAssembly());
            }
            cursor.close();
        }

        return assemblies;
    }

    public Assembly getAssemblyFromId(int id){
        Assembly assembly;
        ArrayList<Assembly> assemblies = new ArrayList<>();

        AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies where id = "+Integer.toString(id),null));
        while(cursor.moveToNext()) {
            assemblies.add(cursor.getAssembly());
        }
        cursor.close();

        assembly = assemblies.get(0);

        return assembly;
    }

    public int getAssemblyid (String desc){
        int a;
        ArrayList<Assembly> assemblies = new ArrayList<>();

        AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies where description like '"+desc.toString()+"'", null));
        while(cursor.moveToNext()) {
            assemblies.add(cursor.getAssembly());
        }
        cursor.close();
        a = assemblies.get(0).getId();
        return a;
    }

    public String getAssemblydesc (int id){
        String a;
        ArrayList<Assembly> assemblies = new ArrayList<>();

        AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies where id = "+Integer.toString(id), null));
        while(cursor.moveToNext()) {
            assemblies.add(cursor.getAssembly());
        }
        cursor.close();
        a = assemblies.get(0).getDescription();

        return a;
    }


    // ----------------------------------------------- ASSEMBLY PRODUCTS--------------------------------------------------------
    public List<AssemblyProduct> getAllAssemblyProducts(){
        ArrayList<AssemblyProduct> list = new ArrayList<>();

        AssemblyProductCursor cursor = new AssemblyProductCursor(db.rawQuery("select * from assembly_products order by id",null));
        while (cursor.moveToNext()){
            list.add(cursor.getAssemblyProduct());
        }
        cursor.close();

        return list;
    }

    public ArrayList<AssemblyProduct> getEspecificAssemblyProducts(int id){
        ArrayList<AssemblyProduct> list = new ArrayList<>();

        AssemblyProductCursor cursor = new AssemblyProductCursor(db.rawQuery("select * from assembly_products where id = "+Integer.toString(id)+" order by id",null));
        while (cursor.moveToNext()){
            list.add(cursor.getAssemblyProduct());
        }
        cursor.close();
        return list;
    }

    public void insertAssemblyproducts(int idensammble, int productid, int qty) {
        ContentValues values = new ContentValues();

        values.put(AssemblyProductsTable.Columns.ID,idensammble);
        values.put(AssemblyProductsTable.Columns.PRODUCT_ID,productid);
        values.put(AssemblyProductsTable.Columns.QUANTITY,qty);


        db.insert(AssemblyProductsTable.NAME, null, values);
    }

    public void updateAssemblyproducts(int idensammble, int productid, int qty) {
        ContentValues values = new ContentValues();

        values.put(AssemblyProductsTable.Columns.ID,idensammble);
        values.put(AssemblyProductsTable.Columns.PRODUCT_ID,productid);
        values.put(AssemblyProductsTable.Columns.QUANTITY,qty);


//        db.update(AssemblyProductsTable.NAME, values, AssemblyProductsTable.Columns.ID+ "= ?",
//                new String[] {Integer.toString(idensammble)});

    }

    public void deleteAssemblyproducts(int idensammble){
        db.delete(AssemblyProductsTable.NAME, AssemblyProductsTable.Columns.ID + "= ?",
                new String[] {Integer.toString(idensammble)});
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

    public List<Client> filterClients (boolean [] selected, String text){

        ArrayList<Client> clients = new ArrayList<>();
        if(text.isEmpty()){
            ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers ORDER BY last_name", null));
            while(cursor.moveToNext()){
                clients.add(cursor.getClient());
            }
            cursor.close();
        }else{
            if(selected[0]==true){
                ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers where first_name like '%"+text.toString()+"%' ORDER BY last_name", null));
                while(cursor.moveToNext()){
                    clients.add(cursor.getClient());
                }
                cursor.close();
            }

            if(selected[1]==true){
                ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers where last_name like '%"+text.toString()+"%' ORDER BY last_name", null));
                while(cursor.moveToNext()){
                    clients.add(cursor.getClient());
                }
                cursor.close();
            }

            if(selected[2]==true){
                ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers where address like '%"+text.toString()+"%' ORDER BY last_name", null));
                while(cursor.moveToNext()){
                    clients.add(cursor.getClient());
                }
                cursor.close();
            }

            if(selected[3]==true){
                ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers where phone1 like '%"+text.toString()+"%' or phone2 like '%"+text.toString()+"%' " +
                        " or phone3 like '%"+text.toString()+"%'ORDER BY last_name", null));
                while(cursor.moveToNext()){
                    clients.add(cursor.getClient());
                }
                cursor.close();
            }

            if(selected[4]==true){
                ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers where e_mail like '%"+text.toString()+"%' ORDER BY last_name", null));
                while(cursor.moveToNext()){
                    clients.add(cursor.getClient());
                }
                cursor.close();
            }

        }

        //Si no se especifica un texto válido se considera que no hay filtro de texto???

        return clients;
    }


    // -------------------------------------------------------- ORDERS --------------------------------------------------------

    public List<Order> getAllOrders() {
        ArrayList<Order> list = new ArrayList<>();

        OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders order by date(date) desc", null));
        while(cursor.moveToNext()){
            list.add(cursor.getOrder());
        }
        cursor.close();
        return list;
    }

    public String getCustomer(int id){
        //List<Client> clients = getAllClients();
        String clientName = null;

        //for (Client client : clients) {
            //if (client.getId() == id){
              //  clientName = client.getFirstName() + " " + client.getLastName();
            //}
            //else {clientName = null;}
        //}
        //return clientName;

        ArrayList<Client> list = new ArrayList<>();

        ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers WHERE id LIKE "+Integer.toString(id), null));
        while(cursor.moveToNext()){
            list.add(cursor.getClient());
        }
        cursor.close();

        for (Client client : list){
            clientName = client.getFirstName() +" "+ client.getLastName();
        }

        return clientName;
    }

    public boolean insertOrder(int id, int status_id, int customer_id, String date,String change_log) {
        boolean b = true;
        //List<Product> a = getAllProducts();
        //ContentValues values = new ContentValues();
//
        //if (text.isEmpty()) {
        //    b = false;
        //}
//
        //for(Product product : a) {
        //    if (product.getDescription().toUpperCase().equals(text.toUpperCase())) {
        //        b = false;
        //    }
        //}
//
        //if (b) {
        //    Product c = a.get(a.size()-1);
//
        //    values.put(ProductsTable.Columns.DESCRIPTION, text);
        //    values.put(ProductsTable.Columns.CATEGORY_ID, category_id);
        //    values.put(ProductsTable.Columns.PRICE, precio);
        //    values.put(ProductsTable.Columns.QUANTITY, qty);
//
        //    db.insert(ProductsTable.NAME, null, values);
        //}
        return b;
    }

    //public List<OrderAssembly> getOrderAssemblies(int id){ //Me dan el id de la orden y me da las order assemblies

       //ArrayList<Assembly> assemblies = new ArrayList<>();

       //if (texto.isEmpty()){
       //    AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies ORDER BY description", null));
       //    while(cursor.moveToNext()) {
       //        assemblies.add(cursor.getAssembly());
       //    }
       //    cursor.close();
       //} else{

       //    AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies where description like '%"+texto.toString()+"%' ORDER BY description", null));
       //    while(cursor.moveToNext()) {
       //        assemblies.add(cursor.getAssembly());
       //    }
       //    cursor.close();
       //}

       //return assemblies;

      // return null;
    //}

    // -------------------------------------------------------- ORDER ASSEMBLIES --------------------------------------------------------

    public List<OrderAssembly> getAllOrderAssemblies() {
        ArrayList<OrderAssembly> list = new ArrayList<>();

        OrderAssembliesCursor cursor = new OrderAssembliesCursor(db.rawQuery("SELECT * FROM order_assemblies ORDER BY id", null));
        while(cursor.moveToNext()){
            list.add(cursor.getOrderAssembly());
        }
        cursor.close();
        return list;
    }

    public boolean insertOrderAssembly(int assembly_id, int qty) {
        boolean b = true;
        List<OrderAssembly> a = getAllOrderAssemblies();
        ContentValues values = new ContentValues();

            OrderAssembly c = a.get(a.size()-1);

            values.put(OrderAssembliesTable.Columns.ASSEMBLY_ID, assembly_id);
            values.put(OrderAssembliesTable.Columns.QUANTITY, qty);

            db.insert(OrderAssembliesTable.NAME, null, values);

        return b;
    }

    public List<Order> filterOrdersByClient (String textClient, String date1,String date2,Boolean Bdate1, Boolean Bdate2){

        ArrayList<Order> orders = new ArrayList<>();

        if (textClient == "Todos"){

            OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                    "from orders o " +
                    "inner join customers c on (o.customer_id = c.id) order by date(date) desc", null)); //ORDENAE POR FECHA
            while (cursor.moveToNext()) {
                orders.add(cursor.getOrder());
            }
            cursor.close();

        } else {

            if (Bdate1==false && Bdate2 ==false){
                OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                        "from orders o " +
                        "inner join customers c on (o.customer_id = c.id) " +
                        "where c.first_name || ' ' || c.last_name like '" + textClient + "' order by date(date) desc", null)); //ORDENAE POR FECHA
                while (cursor.moveToNext()) {
                    orders.add(cursor.getOrder());
                }
                cursor.close();
            }
            else if (Bdate1==true && Bdate2 ==false){
                OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                        "from orders o " +
                        "inner join customers c on (o.customer_id = c.id) " +
                        "where c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                        "and date >= date('"+ date1 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                while (cursor.moveToNext()) {
                    orders.add(cursor.getOrder());
                }
                cursor.close();
            }
            else if (Bdate1==false && Bdate2 ==true){
                OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                        "from orders o " +
                        "inner join customers c on (o.customer_id = c.id) " +
                        "where c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                        "and date <= date('"+ date2 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                while (cursor.moveToNext()) {
                    orders.add(cursor.getOrder());
                }
                cursor.close();
            }
            else if (Bdate1==true && Bdate2 ==true){
                OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                        "from orders o " +
                        "inner join customers c on (o.customer_id = c.id) " +
                        "where c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                        "and date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                        "order by date(date) desc", null)); //ORDENAE POR FECHA
                while (cursor.moveToNext()) {
                    orders.add(cursor.getOrder());
                }
                cursor.close();
            }
        }

        return orders;
    }

    public List<Order> filterOrdersByEverything (boolean [] selected,String textClient,String date1,String date2,Boolean Bdate1, Boolean Bdate2){ //FILTRAR POR ALL

        ArrayList<Order> orders = new ArrayList<>();

        if (textClient == "Todos") {

            if (Bdate1==false && Bdate2 ==false){ //FILTRO POR ESTADOS
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where status_id = 0 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where status_id = 1 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where status_id = 2 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where status_id = 3 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where status_id = 4 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }
            else if (Bdate1==true && Bdate2 ==false){ //FILTRO POR ESTADOS Y FECHA INICIAL
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date >= date('"+ date1 +"') AND status_id = 0 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date >= date('"+ date1 +"') AND status_id = 1 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date >= date('"+ date1 +"') AND status_id = 2 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date >= date('"+ date1 +"') AND status_id = 3 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date >= date('"+ date1 +"') AND status_id = 4 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }
            else if (Bdate1==false && Bdate2 ==true){ //FILTRO POR ESTADOS Y FECHA FINAL
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date <= date('"+ date2 +"') AND status_id = 0 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date <= date('"+ date2 +"') AND status_id = 1 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date <= date('"+ date2 +"') AND status_id = 2 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date <= date('"+ date2 +"') AND status_id = 3 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders where date <= date('"+ date2 +"') AND status_id = 4 order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }
            else if (Bdate1==true && Bdate2 ==true){ //FILTRO POR ESTADOS Y FECHAS
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders " +
                            "where date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "AND status_id = 0 order by date(date) desc", null));
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders " +
                            "where date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "AND status_id = 1 order by date(date) desc", null));
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders " +
                            "where date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "AND status_id = 2 order by date(date) desc", null));
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders " +
                            "where date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "AND status_id = 3 order by date(date) desc", null));
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders " +
                            "where date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "AND status_id = 4 order by date(date) desc", null));
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }


        }else { //CLIENTES VARÍAN

            if (Bdate1==false && Bdate2 ==false) { //FILTRO POR ESTADOS
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 0 and c.first_name || ' ' || c.last_name like '" + textClient + "' order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 1 and c.first_name || ' ' || c.last_name like '" + textClient + "' order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 2 and c.first_name || ' ' || c.last_name like '" + textClient + "' order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 3 and c.first_name || ' ' || c.last_name like '" + textClient + "' order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 4 and c.first_name || ' ' || c.last_name like '" + textClient + "' order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }
            else if (Bdate1==true && Bdate2 ==false) { //FILTRO POR ESTADOS Y FECHA INICIAL
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 0 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date >= date('"+ date1 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 1 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date >= date('"+ date1 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 2 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date >= date('"+ date1 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 3 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date >= date('"+ date1 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 4 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date >= date('"+ date1 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }
            else if (Bdate1==false && Bdate2 ==true) { //FILTRO POR ESTADOS Y FECHA FINAL
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 0 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date <= date('"+ date2 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 1 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date <= date('"+ date2 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 2 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date <= date('"+ date2 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 3 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date <= date('"+ date2 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 4 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date <= date('"+ date2 +"')order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }
            else if (Bdate1==true && Bdate2 ==true) { //FILTRO POR ESTADOS Y FECHAS
                if (selected[0] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 0 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[1] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 1 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[2] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 2 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[3] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 3 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }

                if (selected[4] == true) {
                    OrderCursor cursor = new OrderCursor(db.rawQuery("select o.id,o.status_id,o.customer_id, o.date,o.change_log " +
                            "from orders o " +
                            "inner join customers c on (o.customer_id = c.id) " +
                            "where o.status_id = 4 and c.first_name || ' ' || c.last_name like '" + textClient + "' " +
                            "and date BETWEEN date('"+ date1 +"') AND date('"+ date2 +"') " +
                            "order by date(date) desc", null)); //ORDENAE POR FECHA
                    while (cursor.moveToNext()) {
                        orders.add(cursor.getOrder());
                    }
                    cursor.close();
                }
            }
        }

        //Si no se especifica un texto válido se considera que no hay filtro de texto???

        return orders;
    }


    // -------------------------------------------------------- REPORTS --------------------------------------------------------

    public List<MissingProduct> getAllMissingProduct() {
        ArrayList<MissingProduct> list = new ArrayList<>();

        MissingProductCursor cursor = new MissingProductCursor(
                db.rawQuery("SELECT s.id, s.description, SUM(q.qty * r.qty) AS qty " +
                        "FROM orders p " +
                        "INNER JOIN order_assemblies q ON (p.id = q.id) " +
                        "INNER JOIN assembly_products r ON (q.assembly_id = r.id) " +
                        "INNER JOIN products s ON (r.product_id = s.id) " +
                        "WHERE p.status_id = 0 OR p.status_id = 2 " +
                        "GROUP BY s.description " +
                        "ORDER BY s.id", null));
        while(cursor.moveToNext()){
            list.add(cursor.getMissingProduct());
        }
        cursor.close();

        return list;
    }
}
