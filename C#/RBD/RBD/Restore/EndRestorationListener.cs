namespace RBD.Restore
{
    public interface EndRestorationListener
    {
        void onEndRestoration(RestoreCoordinator coordinator);
    }
}
