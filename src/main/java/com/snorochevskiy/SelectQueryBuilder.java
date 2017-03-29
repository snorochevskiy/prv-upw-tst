package com.snorochevskiy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SelectQueryBuilder {

    // First (very left) table in SELECT query.
    private Path srcFile;

    // Configuration for JOIN operations in SELECT query
    private final List<JoinTableConf> joins = new ArrayList<JoinTableConf>();

    /* package */ SelectQueryBuilder(Path srcFile) {
        this.srcFile = srcFile;
    }

    public SelectQueryBuilder join(Path joinFile, String leftColumnName, String rightColumnName, JoinType joinType, boolean isRightColumnIndexed) {
        joins.add(new JoinTableConf(joinFile, joinType,leftColumnName, rightColumnName, isRightColumnIndexed));

        return this;
    }

    /**
     * Executes SELECT statement and returns queried rows.
     * @return
     * @throws IOException
     */
    public Result execute() throws IOException {
        QueryExecutor executor = new QueryExecutor();
        return executor.execute(this);
    }

    public Path getSrcFile() {
        return srcFile;
    }

    public List<JoinTableConf> getJoins() {
        return joins;
    }
}
