package com.snorochevskiy;

/**
 * Determines how the table will be scanned during JOIN operation.
 */
public enum ScanStrategy {
    FULL_TABLE_SCAN,
    INDEXED
}
