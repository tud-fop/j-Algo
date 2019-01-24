package org.jalgo.module.hoare.constants;

/**
 * Representation of all Events of the Hoare module send from the
 * View to the Controller.
 *
 * @author Johannes
 *
 */

public enum Event {
    APPLYRULE,
    EDITPREASSERTION,
    EDITPOSTASSERTION,
    DELETENODE,
    UNDO,
    REDO,
    PARSECODE,
    REINIT
}
