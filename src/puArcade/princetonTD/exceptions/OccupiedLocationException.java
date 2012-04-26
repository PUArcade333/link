package puArcade.princetonTD.exceptions;

public class OccupiedLocationException extends Exception {
	
	private static final long serialVersionUID = -7348304222504565854L;

    public OccupiedLocationException(String cause)
    {
            super(cause);
    }

}
