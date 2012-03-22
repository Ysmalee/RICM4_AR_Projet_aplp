/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Définit un annuaire téléphonique élémentaire permettant étant donnée un abonné d'obtenir son numéro de téléphone.
 * @author Morat 
 */
public interface _Annuaire {
	/**
	 * restitue le numero de téléphone de l'abonné
	 * @param abonne l'abonné
	 * @return le numéro de télephone de l'abonné
	 */
	public Numero get(String abonne);
}