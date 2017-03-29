Java 8 based toy DB
===================

This is a simple toy project with text file based DB engine.

Most interesting places are:
* QueryExecutor # execute
* TableView # join
* TableView # findRow


DB file format
--------------
Each table is stored in separate file.
File structure is:
```
column1  | column2  | column 3 | column4 ...
value1-1 | value1-2 | value1-3 | value1-4 ...
...
valueN-1 | valueN-2 | valueN-3 | valueN-4 ...
```

Functionality
-------------
* Only Select queries with INNER JOINs for One-To-One relation are supported.
* Only String type is supported

Run tests
---------
There's a small test database represented by 2 files comes in src/test/resources/one-to-one :
* user.txt
* user-info.txt

To run tests for it and see the result, executed in command line:
```
gradle -i test
```