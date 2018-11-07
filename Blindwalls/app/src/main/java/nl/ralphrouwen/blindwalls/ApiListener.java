package nl.ralphrouwen.blindwalls;

public interface ApiListener {
    public void onMuralAvailable(Mural mural);
    public void onMuralError(Error error);
}
