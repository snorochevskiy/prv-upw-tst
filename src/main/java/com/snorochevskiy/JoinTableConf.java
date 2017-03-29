package com.snorochevskiy;


import java.nio.file.Path;

/**
 * Represents configuration for join select query.
 * Used by {@link SelectQueryBuilder}.
 */
public class JoinTableConf {

    private Path joinTable;
    private JoinType joinType;
    private String leftColumnName;
    private String rightColumnName;
    private boolean isRightColumnIndexed;

    /**
     *
     * @param joinTable right table in join operation
     * @param joinType type pf join operation: left, right, inner (only inner is currently supported)
     * @param leftColumnName name on join column in left table
     * @param rightColumnName name of join column in right table
     */
    public JoinTableConf(Path joinTable, JoinType joinType, String leftColumnName, String rightColumnName, boolean isRightColumnIndexed) {
        this.joinTable = joinTable;
        this.joinType = joinType;
        this.leftColumnName = leftColumnName;
        this.rightColumnName = rightColumnName;
        this.isRightColumnIndexed = isRightColumnIndexed;
    }

    public Path getJoinTable() {
        return joinTable;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public String getLeftColumnName() {
        return leftColumnName;
    }

    public String getRightColumnName() {
        return rightColumnName;
    }

    public boolean isRightColumnIndexed() {
        return isRightColumnIndexed;
    }
}
