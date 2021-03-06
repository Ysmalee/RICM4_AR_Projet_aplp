/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Définit la feuille de route que l'agent va suivre
 * @author  Morat
 */
class Route implements Iterable<Etape>, Serializable{
	private static final long serialVersionUID = 9081294824590167316L;
	/** la liste des étapes à parcourir autres que la dernière */
	protected List<Etape> route;
	/** la dernière étape de la feuille de route de l'agent qui désigne le serveur de départ. */
	protected Etape retour;
	/** permet de savoir s'il y a une étape suivante dans la route*/
	protected boolean hasNext;
	
	/**
	 * Construction d'une route.
	 * @param retour le server initial et de retour.
	 */
	public Route(Etape retour) {
		route = new LinkedList<Etape>();
		this.retour = retour;
		hasNext = true;
	}
	
	/**
	 * Ajoute une étape en fin de route.
	 * @param e l'étape à ajouter
	 */
	public void add(Etape e) {route.add(route.size(),e);}
	
	/**
	 * Restitue la prochaine étape ou la dernière qui est la base de départ.
	 * @return la prochaine étape.
	 */
	Etape get() throws NoSuchElementException {
		if (hasNext()){
			return route.get(0);
		} else {
			return retour;
		}
	}
	
	/**
	 * Restitue la prochaine étape et élimine de la route ou la dernière qui est la base de départ.
	 * @return la prochaine étape.
	 */
	Etape next() throws NoSuchElementException {
		if (hasNext()){
			Etape nextEtape = this.get();
			route.remove(0); //supprime l'étape à venir
			if (route.size()==0){
				hasNext = false;
			}
			return nextEtape;
		} else {
			return retour;
		}
	}
	
	/**
	 * Y a-t-il encore une étape à parcourir?
	 * @return vrai si une étape est possible.
	 */
	public boolean hasNext() {return hasNext;}
	
	/**
	 * La route est-elle vide ?
	 * @return vrai si la route est vide
	 */
	public boolean isFinished(){
		return (route.size()==0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Etape> iterator(){return route.iterator();}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {return route.toString().replaceAll(", ","->");}
}
