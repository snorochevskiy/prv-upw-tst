package com.snorochevskiy;

import java.nio.file.Path;

/**
 * Main class for for interaction with our text file-based database.
 *
 * Each table is stored in separate file.
 * Column values are separated with "|" symbol.
 * Only String type is available.
 * First row contains column numbers.
 *
 */
public class Db {

    /**
     * Creates SELECT query builder.
     *
     * @param file path to text file with
     * @return builder object for further query configuration
     */
    public SelectQueryBuilder selectFrom(Path file) {
        SelectQueryBuilder query = new SelectQueryBuilder(file);
        return query;
    }
}