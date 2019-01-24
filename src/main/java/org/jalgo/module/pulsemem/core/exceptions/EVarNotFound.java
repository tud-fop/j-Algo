package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

public class EVarNotFound extends EExecutionException{
        private static final String LanguageNode = "Exception.EVarNotFound";
        private static final String MessageStart = LanguageNode + ".Message_Start";
        private static final String MessageEnd = LanguageNode + ".Message_End";

        public EVarNotFound(String varName) {
                super(Admin.getLanguageString(MessageStart) + varName
                                + Admin.getLanguageString(MessageEnd));
        }
}
