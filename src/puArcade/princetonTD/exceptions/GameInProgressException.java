package puArcade.princetonTD.exceptions;

@SuppressWarnings("serial")
public class GameInProgressException extends Exception {

	public GameInProgressException(String message)
    {
        super(message);
    }

}
