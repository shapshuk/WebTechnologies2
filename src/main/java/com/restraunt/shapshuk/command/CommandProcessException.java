package com.restraunt.shapshuk.command;

class CommandProcessException extends RuntimeException {

    private static final long serialVersionUID = -2231934072516221301L;

    CommandProcessException(String message, Exception root) {
        super(message, root);
    }
}
