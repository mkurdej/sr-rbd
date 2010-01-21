// +

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.DB
{
    class DatabaseStateImpl : DatabaseState
    {
        Dictionary<String, TableState> tables;
        const String LOGGING_NAME = "DatabaseStateImpl";

        private DatabaseStateImpl()
        {
            this.tables = new Dictionary<String, TableState>();
        }

        private static DatabaseStateImpl instance = new DatabaseStateImpl();

        /** 
         * /* (non-Javadoc)
         *  * @see DatabaseState#lockTable(String tableName)
         * 
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void lockTable(String tableName) //throws TableLockedException 
        {
            getTableByName(tableName).lockTable();
        }

        public void setTableVersion(String tableName, int version)
        {
            getTableByName(tableName).setVersion(version);
        }

        public void incrementTableVersion(String tableName)
        {
            getTableByName(tableName).incrementVersion();
        }

        public void addTable(String tableName)
        {
            if (tables[tableName] != null) // TODO check
            {
                Logger.getInstance().log("Adding table that exists", LOGGING_NAME, Logger.Level.WARNING);
                return;
            }

            tables[tableName] = new TableState(tableName);
        }

        /** 
         * /* (non-Javadoc)
         *  * @see DatabaseState#unlockTable(String tableName)
         * 
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void unlockTable(String tableName)
        {
            getTableByName(tableName).unlockTable();
        }

        public int getTableVersion(String tableName)
        {

            return getTableByName(tableName).getVersion();
        }

        protected TableState getTableByName(String tableName)
        {
            TableState ts = tables[tableName];

            if (ts == null)
            {
                ts = new TableState(tableName);
                tables[tableName] = ts;
            }

            return ts;
        }

        public IList<TableVersion> getTableVersions()
        {
            IList<TableVersion> result = new List<TableVersion>(); //new LinkedList<TableVersion>();

            foreach (KeyValuePair<String, TableState> e in tables)
                result.Add(new TableVersion(e.Key, e.Value.getVersion()));

            return result;
        }

        public bool checkSync(IList<TableVersion> tvs)
        {
            // check tables
            foreach (TableVersion tv in tvs)
            {
                TableState ts = getTableByName(tv.getTableName());

                if (ts.getVersion() > tv.getVersion())
                    return false;
            }

            return true;
        }

        public static DatabaseStateImpl getInstance()
        {
            return instance;
        }
    }
}
