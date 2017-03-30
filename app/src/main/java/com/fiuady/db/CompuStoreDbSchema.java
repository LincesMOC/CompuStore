package com.fiuady.db;

public final class CompuStoreDbSchema {
    public static final class CategoriesTable {
        public static final String NAME = "product_categories";

        public static final class Columns {
            public static final String ID = "id";
            public static final String DESCRIPTION = "description";
        }
    }

    public static final class ProductsTable {
        public static final String NAME = "products";

        public static final class Columns {
            public static final String ID = "id";
            public static final String CATEGORY_ID = "category_id";
            public static final String DESCRIPTION = "description";
            public static final String PRICE = "price";
            public static final String QUANTITY = "qty";
        }
    }
}
