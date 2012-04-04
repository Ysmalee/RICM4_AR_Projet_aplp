package jus.aor.mobilagent.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;
import kernel.Hotel;
import kernel.Numero;
import kernel.Parser_Hotel;

/**
 * Classe de test de l'objectif 4
 */
public class LookForHotel extends Agent{

	private static final long serialVersionUID = 1L;
        private List<Hotel> _listeHotel;
        private Map<Hotel, Numero> _mapHotelsNumeros;
        private String _localisation;

	/**
	  * construction d'un agent de type LookForHotel.
	  * @param args aucun argument n'est requis
	  */
        // public LookForHotel(Object... args) {}
	 
           public LookForHotel(Object... args)
           {
               this._localisation = args[0].toString();
               _listeHotel = new ArrayList<Hotel>();
           }
        
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action() {

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			
		}
	};
        
        /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doHotel = new _Action() {

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
                    LookForHotel.this._listeHotel.addAll((List<Hotel>)(LookForHotel.this.getAgentServer().getService("ServiceHotel").call(LookForHotel.this._localisation)));
		}
	};
        
        /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doAnnuaire= new _Action() {

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
                    LookForHotel.this._mapHotelsNumeros = (Map<Hotel, Numero>)(LookForHotel.this.getAgentServer().getService("ServiceAnnuaire").call(LookForHotel.this._listeHotel));
		}
	};
	
	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	//@Override
	protected _Action retour()
        {
		return doIt;
                
	}

}
