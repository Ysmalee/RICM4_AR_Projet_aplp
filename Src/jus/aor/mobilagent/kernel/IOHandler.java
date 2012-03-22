package jus.aor.mobilagent.kernel;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import jus.util.Dialogue;

/**
 * Un Handler permettant d'écrire sur le support IO.
 * @author Morat
 */
class IOHandler extends Handler {
	private Dialogue io;
	/**
	 * la sortie s'effectue sur une fenêtre indépendante.
	 */
	{
		io = new Dialogue();
		io.setOut();
	}
	/* (non-Javadoc)
	 * @see java.util.logging.Handler#close()
	 */
	@Override
	public void close() throws SecurityException{}
	/* (non-Javadoc)
	 * @see java.util.logging.Handler#flush()
	 */
	@Override
	public void flush(){}
	/* (non-Javadoc)
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord record){io.println(record.getMessage());}
}