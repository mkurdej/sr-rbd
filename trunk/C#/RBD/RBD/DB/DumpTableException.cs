using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace RBD.DB
{
    public class DumpTableException : Exception 
    {
 
        private string errorMessage;
        
        public DumpTableException(IOException e)
        {
                errorMessage = e.Message;
        }
        
        public String getErrorMessage()
        {
                return errorMessage;
        }
    }
}
