package Modele.notification;

/**
 * Message immuable transmis aux Observers du modele. Une notification est
 * toujours composee d'un {@link TypeNotification} (obligatoire, sert au
 * filtrage) et d'une donnee optionnelle (numero de tour, ressource modifiee,
 * etc.) que les vues peuvent consulter si elles en ont besoin.
 */
public class Notification {

    private final TypeNotification type;
    private final Object donnee;

    /**
     * Notification sans donnee associee.
     *
     * @param type categorie de l'evenement, non null
     */
    public Notification(TypeNotification type) {
        this(type, null);
    }

    /**
     * @param type categorie de l'evenement, non null
     * @param donnee charge utile optionnelle (peut etre null)
     * @throws IllegalArgumentException si {@code type} est null
     */
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
