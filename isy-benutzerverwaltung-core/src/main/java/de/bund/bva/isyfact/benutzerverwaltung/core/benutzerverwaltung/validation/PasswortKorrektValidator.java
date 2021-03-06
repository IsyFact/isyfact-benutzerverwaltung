package de.bund.bva.isyfact.benutzerverwaltung.core.benutzerverwaltung.validation;

/*-
 * #%L
 * IsyFact Benutzerverwaltung Core
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

import de.bund.bva.isyfact.benutzerverwaltung.core.benutzerverwaltung.daten.PasswortAendern;
import de.bund.bva.isyfact.benutzerverwaltung.persistence.basisdaten.dao.BenutzerDao;
import de.bund.bva.isyfact.benutzerverwaltung.persistence.basisdaten.entity.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswortKorrektValidator implements ConstraintValidator<PasswortKorrekt, PasswortAendern> {

    @Autowired
    private BenutzerDao benutzerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(PasswortKorrekt constraintAnnotation) {
    }

    @Override
    public boolean isValid(PasswortAendern value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Benutzer benutzer = benutzerDao.sucheMitBenutzername(value.getBenutzername());
        return passwordEncoder.matches(value.getAltesPasswort(), benutzer.getPasswort());
    }
}
