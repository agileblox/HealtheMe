/**
 * Copyright (C) 2012 KRM Associates, Inc. healtheme@krminc.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krminc.phr.domain.clinical;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author chaz
 */
@Entity
@Table(name = "phr.rec_ccr_immunization_sources")
@Inheritance(strategy = InheritanceType.JOINED)
public class CcrImmunizationSource extends CcrSource implements Serializable {

    private static final long serialVersionUID = 20091128L;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity= com.krminc.phr.domain.clinical.CcrImmunization.class)
    @JoinColumn(name="ccr_immunization_id")
    protected CcrImmunization immunization;
  
    public CcrImmunization getImmunization() {
        return immunization;
    }

    public void setImmunization(CcrImmunization immunization) {
        this.immunization = immunization;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CcrImmunizationSource)) {
            return false;
        }
        CcrImmunizationSource other = (CcrImmunizationSource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


}
