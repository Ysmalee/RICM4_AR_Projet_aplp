import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RegistryServer {

	/**
	 * Lancement dy RMI registry
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 0;

		// récupération des arguments
		if (args.length != 1){
			System.out.println("RegistryServer <registry port>");
			System.exit(1);
		}
		try  {
			port = Integer.parseInt(args[0]);
		}catch(Exception e) {
			System.out.println("RegistryServer <registry port>");
			System.exit(1);
		}
		
		//Creation du RMI-Registry
		System.out.print("Lancement du serveur ... ");
		try {
			Registry reg = LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			System.out.println("Erreur");
			System.out.println("Erreur lors de la creation du registry\n" + e.toString());
			System.exit(1);
		}
		
		System.out.println("OK");
		while(true) {
			
		}

	}

}
