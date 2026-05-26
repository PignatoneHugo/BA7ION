package Modele.notification;

/**
 * Payload typee transmis comme argument de Observable#notifyObservers(Object).
 *
 * Permet aux vues de filtrer rapidement les notifications recues :
 *
 *     public void update(Observable o, Object arg) {
 *         if (arg instanceof Notification n {@literal &&} n.type() == TypeNotification.TRESOR_CHANGE) {
 *             rafraichirAffichageRessources();
 *         }
 *     }
 *
 * Le champ {@code donnee} est optionnel : il sert quand la vue a besoin d'un
 * detail supplementaire (par exemple le numero du tour pour PHASE_CHANGEE).
 */
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
