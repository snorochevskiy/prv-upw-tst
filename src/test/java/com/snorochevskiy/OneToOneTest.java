package com.snorochevskiy;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class OneToOneTest {

    @Test
    public void testOneToOneJoinUnindexed() throws URISyntaxException, IOException {

        Db db = new Db();
        Result result = db.selectFrom(fullPath("/one-to-one/users.txt"))
                .join(fullPath("/one-to-one/user-info.txt"), "user_id", "ref_user_id", JoinType.INNER, false)
                .execute();

        printResult(result);

        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.gerRowsCount());
        Assert.assertTrue(Arrays.equals(
                new String[]{"user_id", "login", "passwd", "status", "user_info_id", "ref_user_id", "first_name", "last_name", "birth_year"},
                result.getColumnNames()
        ));
    }

    @Test
    public void testOneToOneJoinIndexed() throws URISyntaxException, IOException {

        Db db = new Db();
        Result result = db.selectFrom(fullPath("/one-to-one/users.txt"))
                .join(fullPath("/one-to-one/user-info.txt"), "user_id", "ref_user_id", JoinType.INNER, true)
                .execute();

        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.gerRowsCount());
        Assert.assertTrue(Arrays.equals(
                new String[]{"user_id", "login", "passwd", "status", "user_info_id", "ref_user_id", "first_name", "last_name", "birth_year"},
                result.getColumnNames()
        ));
    }

    private Path fullPath(String resourceFile) throws URISyntaxException {
        URL resource = this.getClass().getResource(resourceFile);
        return Paths.get(resource.toURI()).toAbsolutePath();
    }

    private void printResult(Result result) {

        for (String columnName : result.getColumnNames()) {
            System.out.print(columnName + " | ");
        }
        System.out.println();

        for (String[] row : result.getRows()) {
            for (String column : row) {
                System.out.print(column + " | ");
            }
            System.out.println();
        }
    }
}
