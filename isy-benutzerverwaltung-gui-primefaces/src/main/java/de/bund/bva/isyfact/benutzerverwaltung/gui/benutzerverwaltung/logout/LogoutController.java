package de.bund.bva.isyfact.benutzerverwaltung.gui.benutzerverwaltung.logout;

/*-
 * #%L
 * IsyFact Benutzerverwaltung GUI mit Primefaces
 * %%
 * Copyright (C) 2016 - 2017 Bundesverwaltungsamt (BVA)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.faces.context.FacesContext;

import de.bund.bva.isyfact.benutzerverwaltung.common.exception.BenutzerverwaltungBusinessException;
import de.bund.bva.isyfact.benutzerverwaltung.gui.benutzerverwaltung.awkwrapper.BenutzerverwaltungAwkWrapper;
import de.bund.bva.isyfact.benutzerverwaltung.gui.benutzerverwaltung.common.controller.AbstractBenutzerverwaltungController;
import de.bund.bva.isyfact.benutzerverwaltung.gui.common.konstanten.EreignissSchluessel;
import de.bund.bva.isyfact.benutzerverwaltung.sicherheit.BenutzerverwaltungAufrufKontextImpl;
import de.bund.bva.isyfact.common.web.global.MessageController;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontextVerwalter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

/**
 * Controller des Login Flows
 *
 * @author msg systems ag, Andreas Schubert
 */
@Controller
public class LogoutController extends AbstractBenutzerverwaltungController<LogoutModel> {

    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(LogoutController.class);

    private final AufrufKontextVerwalter<BenutzerverwaltungAufrufKontextImpl> aufrufKontextVerwalter;

    public LogoutController(MessageController messageController, MessageSource messageSource,
        BenutzerverwaltungAwkWrapper awkWrapper,
        AufrufKontextVerwalter<BenutzerverwaltungAufrufKontextImpl> aufrufKontextVerwalter) {
        super(messageController, messageSource, awkWrapper);
        this.aufrufKontextVerwalter = aufrufKontextVerwalter;
    }

    @Override
    public void initialisiereModel(LogoutModel model) {
    }

    /**
     * Führt den Logout-Vorgang aus.
     */
    public void performLogout() {
        LOG.infoFachdaten(LogKategorie.JOURNAL, EreignissSchluessel.MSG_LOGIN_STARTED,
            "Führe Logout aus für Benutzer {}",
            aufrufKontextVerwalter.getAufrufKontext().getDurchfuehrenderSachbearbeiterName());

        try {
            getAwkWrapper().speichereAbmeldung(
                aufrufKontextVerwalter.getAufrufKontext().getDurchfuehrenderBenutzerKennung());
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (BenutzerverwaltungBusinessException validationException) {
            erzeugeNachrichten(validationException);
        }

        aufrufKontextVerwalter.setAufrufKontext(new BenutzerverwaltungAufrufKontextImpl());
    }

    @Override
    protected Class<LogoutModel> getMaskenModelKlasseZuController() {
        return LogoutModel.class;
    }

}
