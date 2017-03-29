package com.snorochevskiy;

import java.util.List;

/**
 * Represents the result of SELECT operation
 */
public class Result {

    private String[] columnNames;
    private List<String[]> rows;

    public Result(String[] columnNames, List<String[]> rows) {
        this.columnNames = columnNames;
        this.rows = rows;
    }

    public int gerRowsCount() {
        return rows.size();
    }

    /**
     * Array of column names in result
     * @return
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
     * List of selected rows.
     * @return
     */
    public List<String[]> getRows() {
        return rows;
    }
}
