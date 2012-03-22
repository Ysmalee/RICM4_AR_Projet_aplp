/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Un hotel qui est caractérisé par son nom et sa localisation.
 * @author Morat 
 */
public class Hotel {
	/** la localisation de l'hôtel */
	public String localisation;
	/** le nom de l'hôtel */
	public String name;
	/**
	 * Définition d'un hôtel par son nom et sa localisation.
	 * @param name le nom de l'hôtel
	 * @param localisation la localisation de l'hôtel
	 */
	public Hotel(String name, String localisation) { this.name=name; this.localisation=localisation;}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {return "Hotel{"+name+","+localisation+"}";}
}
