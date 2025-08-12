# Instructions For Setting Up PostgresCluster

## 1. Postgres Database Setup

### 1.1. Start PSQL

Search for the Postgres pods. The name of the pod should start with `crunchy-postgres-dev`.

There should be two pods that match the search criteria. One of them is leader and the other is the follower. Click on one pod and go to the `Logs` tab. The follower has log message: `a secondary, and following a leader`. The leader has log message: `the leader with the lock`.

Go to the leader and click on the `Terminal` tab. Once connected, execute the below command:
```
psql
```

You are now logged in as `postgres`. You can confirm this by executing the below query:
```
select current_user;
```

### 1.2. Create Database

Open file `db_preconditions/database/farms.ddl.create_database.sql`. Replace all occurrences of `${ENV}` with the name of the intended environment such as:
* dev
* qa
* dlvr
* test
* prod

Copy paste the result into the PSQL terminal to create database.

After the database is created, execute the below command to connect to the database:
```
\connect <database_name>;
```

### 1.3. Create Logins

Open file `db_preconditions/logins/farms.ddl.create_login_app_farms.sql`. Replace `${POSTGRES_ADMIN_PASSWORD}` with password of your choosing.

Copy paste the result into the PSQL terminal to create logins.

### 1.4. Create Roles

Open file `db_preconditions/roles/farms.ddl.create_roles.sql`. Copy paste the file content into PSQL terminal to create roles.

### 1.5. Create Schema

Open file `db_preconditions/schema/farms.ddl.create_farms_schema.sql`. Replace all occurrences of `${ENV}` with the name of the intended environment such as:
* dev
* qa
* dlvr
* test
* prod

Copy paste the result into the PSQL terminal to create schema.

### 1.6. Create Extensions

Open file `db_preconditions/extensions/farms.ddl.create_extensions.sql`. Copy paste the file content into PSQL terminal to create extensions.

## 2. Export/Import Schema

### 2.1. Configure `ora2pg`

Execute the below command to create a new `ora2pg` project:
```
cd ~
mkdir -p instance1/ora2pg
ora2pg --project_base instance1/ora2pg --init_project migv1
```

Go to the project directory:
```
cd instance1/ora2pg/migv1
```

Open file `config/ora2pg.conf` and overwrite it with:
```
# Set Oracle database connection (datasource, user, password)
ORACLE_DSN      dbi:Oracle:host=linux1;sid=btdev18c;port=1528
ORACLE_USER     system
ORACLE_PWD      xxxxxxxxxxxx

# Oracle schema/owner to use
SCHEMA HR

# Define the following directive to send export directly to a PostgreSQL
# database. This will disable file output.
PG_DSN          dbi:Pg:dbname=hr;host=localhost;port=5432
PG_USER         btpg10
PG_PWD          xxxxxxx

KEEP_PKEY_NAMES 1
FKEY_DEFERRABLE 1
DROP_FKEY       1
```

Please modify the variables accordingly so that they point to the right Oracle and Postgres databases.

### 2.2. Export Schema

Execute the below command to export Oracle schema objects into their respective folders.
```
./export_schema.sh
```

Note that `ora2pg` tends to hang when exporting Oracle packages. If that happens, press Ctrl+C multiple times to exit the program. We will be manually translating Oracle packages into Postgres compatible format and so it's okay that Oracle packages fail to export. 

### 2.3. Import Schema

Execute the below command to initiate the import process. Note that `<...>` are placeholders that need to be replaced with proper values.
```
./import_all.sh -h <host> -U <database_user> -d <database_name> -p 5432 -o <database_owner>
```

Note that you will be prompted for passwords multiple times. Please enter them accordingly so that the import process will proceed to the next step.

Below are the answers to all the question prompts:
```
Would you like to drop the database farmstest before recreate it? [y/N/q] N
Would you like to import TYPE from ./schema/types/type.sql? [y/N/q] y
Would you like to import SEQUENCE from ./schema/sequences/sequence.sql? [y/N/q] y
Would you like to import SEQUENCE_VALUES from ./schema/sequence_values/sequence_value.sql? [y/N/q] y
Would you like to import TABLE from ./schema/tables/table.sql? [y/N/q] y
Would you like to import PACKAGE from ./schema/packages/package.sql? [y/N/q] N
Would you like to import VIEW from ./schema/views/view.sql? [y/N/q] N
Would you like to import MVIEW from ./schema/mviews/mview.sql? [y/N/q] N
Would you like to import DIRECTORY from ./schema/directories/directorie.sql? [y/N/q] N
Would you like to process indexes and constraints before loading data? [y/N/q] N
Would you like to import GRANT from ./schema/grants/grant.sql? [y/N/q] N
Would you like to import TABLESPACE from ./schema/tablespaces/tablespace.sql? [y/N/q] N
Would you like to import data from Oracle database directly into PostgreSQL? [y/N/q] y
```

### 2.4. Verify Import

Connect to the Postgres database using a database tool such as pgAdmin and verify that the tables are populated.

## 3. Run DDL Scripts

Go to GitHub Actions and invoke the ***Liquibase DDL application*** action to run DDL scripts. This will create the `proxy_farms_rest` role and apply grants and foreign key constraints to tables.