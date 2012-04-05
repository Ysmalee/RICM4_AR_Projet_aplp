/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;

/**
 * Définit une action à exécuter par un agent.
 * @author  Morat
 */
public interface _Action extends Serializable{
	
	/**
	 * L'action vide
	 */
	public static final _Action NIHIL = new _Action() {

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {}
	}; 
	
	/**
	 * Exécute l'action
	 */
	public void execute();
}
