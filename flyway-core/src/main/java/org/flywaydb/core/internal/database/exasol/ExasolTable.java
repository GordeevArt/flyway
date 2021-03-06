/*
 * Copyright 2010-2020 Redgate Software Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.database.exasol;

import java.sql.SQLException;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

/**
 * @author artem
 * @date 14.09.2020
 * @time 16:27
 */
public class ExasolTable extends Table<ExasolDatabase, ExasolSchema> {

    /**
     * Creates a new table.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param database     The database-specific support.
     * @param schema       The schema this table lives in.
     * @param name         The name of the table.
     */
    public ExasolTable(final JdbcTemplate jdbcTemplate,
                       final ExasolDatabase database, final ExasolSchema schema, final String name) {
        super(jdbcTemplate, database, schema, name);
    }

    @Override
    protected boolean doExists() throws SQLException {

        return jdbcTemplate.queryForBoolean(
          "SELECT EXISTS (SELECT 1 FROM EXA_ALL_TABLES WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?)",
          name, schema.getName()
        );
    }

    @Override
    protected void doLock() throws SQLException {
        jdbcTemplate.execute("DELETE FROM " + this + " WHERE FALSE");
    }

    @Override
    protected void doDrop() throws SQLException {
        jdbcTemplate.execute("DROP TABLE " + database.quote(schema.getName(), name) + " CASCADE");
    }
}
