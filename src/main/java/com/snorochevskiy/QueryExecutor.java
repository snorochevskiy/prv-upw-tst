package com.snorochevskiy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class that performs actual execution.
 */
class QueryExecutor {

    public Result execute(SelectQueryBuilder query) throws IOException {

        // Table to be joined in SELECT query
        List<JoinTableConf> tablesToJoin = new ArrayList<>();

        // Fake JoinTableConf for a very left table in JOINs chain
        tablesToJoin.add(new JoinTableConf(query.getSrcFile(), null, null, null, false));

        tablesToJoin.addAll(query.getJoins());

        // Actual join
        try (Stream<JoinTableConf> stream = tablesToJoin.stream()) {

            TableView joinResult = stream.map(TableView::new)
                    .reduce(TableView::join)
                    .get();

            return new Result(joinResult.getColumnNames(), joinResult.getRows());
        }

    }

}

