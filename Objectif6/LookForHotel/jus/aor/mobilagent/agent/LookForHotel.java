package jus.aor.mobilagent.agent;

import java.net.URI;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Hotel;
import jus.aor.mobilagent.kernel.Numero;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Etape;
import jus.aor.mobilagent.kernel._Annuaire;
import jus.aor.mobilagent.kernel._Service;


/**
 * Classe de test de l'objectif 4
 */
public class LookForHotel extends Agent{

	private static final long serialVersionUID = 1L;
	private List<Hotel> _listeHotel;
	private Map<Hotel, Numero> _mapHotelsNumeros;
	private String _localisation;
	
	//RMI registry
	private String _rmi_host;
	private int _rmi_port;

	/**
	 * construction d'un agent de type LookForHotel.
	 * @param args aucun argument n'est requis
	 */ 
	public LookForHotel(Object... args){
		//Importe les parametres
		this._localisation = args[0].toString();
		this._rmi_host = args[1].toString();
		this._rmi_port = Integer.parseInt(args[2].toString());
		_listeHotel = new ArrayList<Hotel>();
		_mapHotelsNumeros=new HashMap<Hotel, Numero>();
	}
	
	
	/**
	 * Action à entreprendre sur le premier serveur
	 */
	protected _Action doSearch = new _Action() {
		@Override
		public void execute(){
			//Importe les hotels
			List<URI> listeChaines = LookForHotel.this.server.askAnnuaire("ServiceHotel");
			for (URI uri : listeChaines) {
				Etape e = new Etape(uri, LookForHotel.this.doHotel);
				LookForHotel.this.addEtape(e);
			}
			
			List<URI> listeAnnuaires = LookForHotel.this.server.askAnnuaire("ServiceAnnuaire");
			if(listeAnnuaires.size() == 0) {
				System.out.println("RMI -> Aucun annuaire enregistré :'(");
				return;
			}
			Etape e = new Etape(listeAnnuaires.get(0), LookForHotel.this.doAnnuaire);
			LookForHotel.this.addEtape(e);
			
		}
	};
        
	
	
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action() {

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			System.err.println("Action doIt !!!");
		}
	};
        
        /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doHotel = new _Action() {

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			_Service<?> toto = LookForHotel.this.getAgentServer().getService("ServiceHotel");
			System.out.println(LookForHotel.this._localisation);
			Object temp = toto.call(LookForHotel.this._localisation);
			LookForHotel.this._listeHotel.addAll((List<Hotel>)temp);
		}
	};
        
        /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doAnnuaire= new _Action() {

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			_Service<?> service = LookForHotel.this.getAgentServer().getService("ServiceAnnuaire");
			for (Hotel h: LookForHotel.this._listeHotel) {
				Numero num = (Numero) (service.call(h.getName()));
				LookForHotel.this._mapHotelsNumeros.put(h, num);
			}
		}
	};
	
	@Override
	public void retour(){  
		System.out.println("La taille de la liste d'hôtels est : ");
		System.out.println(this._listeHotel.size());
	}

}
