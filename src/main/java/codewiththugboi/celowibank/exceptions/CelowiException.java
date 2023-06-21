package codewiththugboi.celowibank.exceptions;

public class CelowiException extends RuntimeException {
    private int statusCode;


    public CelowiException(String message) {
        super(message);
        this.statusCode = statusCode;

    }
    public int getStatusCode(){return  statusCode;}
}

