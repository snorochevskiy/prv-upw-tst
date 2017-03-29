package com.snorochevskiy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Private API type.
 * Used to store intermediate select states.
 */
class TableView {

    private static final String DELIMITER = "\\s*\\|\\s*";

    // Names of columns
    private String[] columnNames;

    // The name of a column used in join operation from the left table
    private String leftTableJoinColumn;

    // The name of a column used in join operation from the right table
    private String rightTableJoinColumn;

    private ScanStrategy scanStrategy;

    private List<String[]> rows;

    /**
     * From join configuration
     * @param joinTableConf
     * @throws IOException
     */
    TableView(JoinTableConf joinTableConf) {
        this.columnNames = collectColumnNames(joinTableConf.getJoinTable());
        this.leftTableJoinColumn = joinTableConf.getLeftColumnName();
        this.rightTableJoinColumn = joinTableConf.getRightColumnName();
        this.scanStrategy = joinTableConf.isRightColumnIndexed()
                ? ScanStrategy.INDEXED
                : ScanStrategy.FULL_TABLE_SCAN;

        this.rows = collectRows(joinTableConf.getJoinTable());
    }

    /**
     * From previous join operation
     * @param columnNames
     * @param rows
     */
    TableView(String[] columnNames, List<String[]> rows) {
        this.columnNames = columnNames;
        this.rows = rows;
    }

    /**
     * Joins current table view with given one.
     * @param rightTable
     * @return
     */
    public TableView join(TableView rightTable) {

        int leftTableColumnsCount = columnNames.length;

        try (Stream<String[]> stream = rows.stream()) {
            List<String[]> joinedRows = stream.map(row -> joinRow(row, rightTable))
                    .filter(row -> row.length > leftTableColumnsCount)
                    .collect(Collectors.toList());

            return new TableView(Utils.concatArrays(columnNames, rightTable.getColumnNames()), joinedRows);
        }
    }

    /**
     * Transforms row from left table to:
     *
     * @param leftRow row from left table to be joined
     * @param rightTable {@link TableView} object that represents right table
     * @return left row joined iwth right row if corresponding right row exists,
     *  or left row if there's no corresponding right row
     */
    private String[] joinRow(String[] leftRow, TableView rightTable) {
        int leftColumnIndex = this.columnIndex(rightTable.getLeftTableJoinColumn());
        int rightColumnIndex = rightTable.columnIndex(rightTable.getRightTableJoinColumn());

        String leftColumnValue = leftRow[leftColumnIndex];

        String[] rightRow = rightTable.findRow(rightColumnIndex, leftColumnValue);

        return Utils.concatArrays(leftRow, rightRow);
    }

    /**
     * Searches for a row by column number and value.
     * @param columnIndex
     * @param value
     * @return row that contains given value in a column with given index,
     *  or an empty String[] array if the row was not found.
     */
    private String[] findRow(int columnIndex, String value) {

        if (scanStrategy == ScanStrategy.FULL_TABLE_SCAN) {
            try (Stream<String[]> stream = rows.stream()) {

                return stream.filter(row -> value.equals(row[columnIndex]))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException e) {
                return new String[] {};
            }
        } else {
            String[] fakeSearchedRow = new String[columnNames.length];
            fakeSearchedRow[columnIndex] = value;

            int index = Collections.binarySearch(rows, fakeSearchedRow, (o1, o2) -> o1[columnIndex].compareTo(o2[columnIndex]));
            return index >= 0
                    ? rows.get(index)
                    : new String[] {};
        }
    }

    String[] collectColumnNames(Path path) {
        try (Stream<String> stream = Files.lines(path)) {
            return stream
                    .limit(1) // First line in file - table header
                    .map(line -> line.split(DELIMITER))
                    .findFirst()
                    .get();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    List<String[]> collectRows(Path path) {
        try (Stream<String> stream = Files.lines(path)) {
            return stream
                    .skip(1)
                    .map(line -> line.split(DELIMITER))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // XXX Can throw exception
    public int columnIndex(String columnName) {
        int index = 0;
        while(!columnNames[index].equals(columnName)) {
            index++;
        }
        return index;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public String getLeftTableJoinColumn() {
        return leftTableJoinColumn;
    }

    public String getRightTableJoinColumn() {
        return rightTableJoinColumn;
    }

    public List<String[]> getRows() {
        return rows;
    }
}