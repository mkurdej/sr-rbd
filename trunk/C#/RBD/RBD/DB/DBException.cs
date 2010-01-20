// + TODO check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RBD.Communication;

namespace RBD.DB
{
    public class DBException : System.Exception, BinarySerializable
    {
        String vErrorMessage;
        public String ErrorMessage { get { return vErrorMessage; } }
        String vErrorCode;
        public String ErrorCode { get { return vErrorCode; } }

        public DBException(System.Data.SqlClient.SqlException e)
        {
            vErrorMessage = e.Message;
            vErrorCode = e.ErrorCode.ToString();
        }

        public DBException(String msg)
        {
            vErrorMessage = msg;
            vErrorCode = "Parsing 1";
        }

        public DBException(DataInputStream dis) //throws IOException 
        {
            FromBinary(dis);
        }

        public void FromBinary(DataInputStream dis) //throws IOException 
        {
            vErrorMessage = dis.ReadString();
            vErrorCode = dis.ReadString();
        }

        public void ToBinary(DataOutputStream dos) //throws IOException 
        {
            dos.WriteString(ErrorMessage);
            dos.WriteString(ErrorCode);
        }
    }
}
