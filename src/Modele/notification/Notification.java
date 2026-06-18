package Modele.notification;

/** Message envoye aux vues : un type et parfois une donnee. */
public class Notification {

    private final TypeNotification type;
    private final Object donnee;

    /**
     * Cree une notification sans donnee.
     *
     * @param type le type de la notification
     */
    public Notification(TypeNotification type) {
        this(type, null);
    }

    /**
     * Cree une notification avec un type et une donnee.
     *
     * @param type le type de la notification
     * @param donnee la donnee attachee (peut etre null)
     * @throws IllegalArgumentException si type est null
     */
    public Notification(TypeNotification type, Object donnee) {
        if (type == null) {
            throw new IllegalArgumentException("Le type d'une Notification ne peut pas etre null.");
        }
        this.type = type;
        this.donnee = donnee;
    }

    /**
     * Donne le type de la notification.
     *
     * @return le type
     */
    public TypeNotification type() {
        return this.type;
    }

    /**
     * Donne la donnee attachee a la notification.
     *
     * @return la donnee, ou null s'il n'y en a pas
     */
    public Object donnee() {
        return this.donnee;
    }

    /**
     * Donne une representation texte de la notification.
     *
     * @return le texte de la notification
     */
    @Override
    public String toString() {
        return this.donnee == null
                ? "Notification[" + this.type + "]"
                : "Notification[" + this.type + " : " + this.donnee + "]";
    }
}
