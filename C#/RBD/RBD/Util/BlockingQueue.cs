// do wyrzucenia

using System;
using System.Collections.Generic;
using System.Threading;
using System.Linq;
using System.Text;

namespace RBD.Util
{
    public class BlockingQueue<T>
    {
        Queue<T> que = new Queue<T>();
        Semaphore sem = new Semaphore(0, Int32.MaxValue);

        public void put(T item)
        {
            lock (que)
            {
                que.Enqueue(item);
            }

            sem.Release();
        }

        public T take()
        {
            sem.WaitOne();

            lock (que)
            {
                return que.Dequeue();
            }
        }
    }
}
