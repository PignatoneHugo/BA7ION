package Modele.notification;

/** Message envoye aux vues : un type et parfois une donnee. */
public class Notification {

    private final TypeNotification type;
    private final Object donnee;

    public Notification(TypeNotification type) {
        this(type, null);
    }

    public Notification(TypeNotification type, Object donnee) {
        if (type == null) {
            throw new IllegalArgumentException("Le type d'une Notification ne peut pas etre null.");
        }
        this.type = type;
        this.donnee = donnee;
    }

    public TypeNotification type() {
        return this.type;
    }

    public Object donnee() {
        return this.donnee;
    }

    @Override
    public String toString() {
        return this.donnee == null
                ? "Notification[" + this.type + "]"
                : "Notification[" + this.type + " : " + this.donnee + "]";
    }
}
