// +

using RBD.TPC.Msg;
using RBD;

namespace RBD.TPC.COH
{
    abstract public class CohortState : TimeoutListener
    {
        /**
         * 
         */
        protected Cohort cohort;
        /**
         * Nazwa logera
         */
        private static string LOGGER_NAME = "KOHORT";
        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        virtual public void onPreCommit()
        {
            Logger.getInstance().log("Nie oczekiwano wiadomosci: PreCommit", LOGGER_NAME, Logger.Level.WARNING);
        }

        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        virtual public void onCanCommit(CanCommitMessage message)
        {
            Logger.getInstance().log("Nie oczekiwano wiadomosci: CanCommit", LOGGER_NAME, Logger.Level.WARNING);
        }

        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        virtual public void onAbort()
        {
            this.cohort.endTransaction();
            this.cohort.setState(new AbortState());
        }

        /** 
         * <!-- begin-UML-doc -->
         * <!-- end-UML-doc -->
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        virtual public void onDoCommit()
        {
            Logger.getInstance().log("Nie oczekiwano wiadomosci: DoCommit", LOGGER_NAME, Logger.Level.WARNING);
        }

        virtual public void onTimeout()
        {
            // empty
        }

        public Cohort getCohort()
        {
            return cohort;
        }

        public void setCohort(Cohort cohort)
        {
            this.cohort = cohort;
        }
    }
}