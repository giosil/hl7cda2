package org.dew.cda;

import org.dew.hl7.ICodeSystem;

public 
class LOINC_IT implements ICodeSystem
{
  // Affinity Domain INI
  public static final String sDOC_PRESCRIZIONE_FARM      = "57833-6";
  public static final String sDOC_PROFILO_SANITARIO_SIN  = "60591-5";
  public static final String sDOC_REFERTO_LABORATORIO    = "11502-2";
  public static final String sDOC_PRESCRIZIONE_APPAR_MED = "57829-4";
  public static final String sDOC_LETTERA_DIM_OSP        = "34105-7";
  public static final String sDOC_VERBALE_PRONTO_SOCC    = "59258-4";
  public static final String sDOC_REFERTO_RADIOLOGIA     = "68604-8";
  public static final String sDOC_REFERTO_ANATOMIA_PAT   = "11526-1";
  public static final String sDOC_REGISTRAZIONE_CONSENSO = "59284-0";
  public static final String sDOC_CERTIFICATO_MALATTIA   = "28653-4";
  public static final String sDOC_PRESCRIZIONE_SPEC      = "57832-8";
  public static final String sDOC_EROGAZIONE_FARM        = "29304-3";
  public static final String sDOC_REFERTO_SPECIALISTICO  = "11488-4";
  public static final String sDOC_ESENZIONE_DA_REDDITO   = "57827-8";
  public static final String sDOC_REFERTO_AMBULATORIALE  = "83798-9";
  public static final String sDOC_EROGAZIONE_SPEC        = "81223-0";
  // Documenti aggiunti / gestiti da Regione Lazio
  public static final String sDOC_PROMEMORIA_PREN_CUP    = "86530-3"; // (28636-9 Obsoleto)
  public static final String sDOC_ANNULLAMENTO           = "11506-3";
  // Altre tipologie di documento non presenti nell'Affinity Domain INI
  public static final String sDOC_REFERTO_GENERICO       = "47045-0";
  public static final String sDOC_EMERGENCY_DATA_SET     = "60592-3";
  public static final String sDOC_RICHIESTA_DI_RICOVERO  = "57830-2";
  public static final String sDOC_RICHIESTA_TRASPORTO    = "57834-4";
  public static final String sDOC_VACCINAZIONI           = "11369-6";
  // Scheda paziente 118
  public static final String sDOC_SCHEDA_PAZIENTE_118    = "78341-5";
  // Certificato e scheda vaccinale
  public static final String sDOC_CERTIFICATO_VACCINALE  = "82593-5";
  public static final String sDOC_SCHEDA_VACCINALE       = "87273-9";
  
  @Override
  public String getCodeSystem() {
    return "2.16.840.1.113883.6.1";
  }
  
  @Override
  public String getCodeSystemName() {
    return "LOINC";
  }
  
  @Override
  public String getCode(String displayName) {
    if(displayName == null || displayName.length() == 0) {
      return "";
    }
    
    String displayNameLC = displayName.toLowerCase();
    if(displayNameLC.startsWith("scheda")) {
      return sDOC_SCHEDA_PAZIENTE_118;
    }
    if(displayNameLC.startsWith("restriz")) {
      return sDOC_REGISTRAZIONE_CONSENSO;
    }
    if(displayNameLC.indexOf("annul") >= 0 || displayNameLC.indexOf("disd") >= 0 || displayNameLC.indexOf("revoc") >= 0) {
      return sDOC_ANNULLAMENTO;
    }
    if(displayNameLC.indexOf("pren") >= 0) {
      return sDOC_PROMEMORIA_PREN_CUP;
    }
    if(displayNameLC.indexOf("ldo") >= 0 || displayNameLC.indexOf("lettera") >= 0 || displayNameLC.indexOf("dimiss") >= 0) {
      return sDOC_LETTERA_DIM_OSP;
    }
    if(displayNameLC.indexOf("verb") >= 0 || displayNameLC.indexOf("pron") >= 0 || displayNameLC.indexOf("ps") >= 0) { 
      return sDOC_VERBALE_PRONTO_SOCC;
    }
    if(displayNameLC.indexOf("rad") >= 0 || displayNameLC.indexOf("immag") >= 0) {
      return sDOC_REFERTO_RADIOLOGIA;
    }
    if(displayNameLC.indexOf("isto") >= 0 || displayNameLC.indexOf("anato") >= 0 || displayNameLC.indexOf("patolo") >= 0) {
      return sDOC_REFERTO_ANATOMIA_PAT;
    }
    if(displayNameLC.indexOf("labor") >= 0 || displayNameLC.indexOf("anal") >= 0) {
      return sDOC_REFERTO_LABORATORIO;
    }
    if(displayNameLC.indexOf("rico") >= 0) {
      return sDOC_RICHIESTA_DI_RICOVERO;
    }
    if(displayNameLC.indexOf("trasp") >= 0) {
      return sDOC_RICHIESTA_TRASPORTO;
    }
    if(displayNameLC.indexOf("presid") >= 0 || displayNameLC.indexOf("ausil") >= 0 || displayNameLC.indexOf("medical") >= 0) {
      return sDOC_PRESCRIZIONE_APPAR_MED;
    }
    if(displayNameLC.indexOf("presc") >= 0 && displayNameLC.indexOf("farm") >= 0) {
      return sDOC_PRESCRIZIONE_FARM;
    }
    if(displayNameLC.indexOf("presc") >= 0 && displayNameLC.indexOf("spec") >= 0) {
      return sDOC_PRESCRIZIONE_SPEC;
    }
    if(displayNameLC.indexOf("summ") >= 0 || displayNameLC.indexOf("prof") >= 0 || displayNameLC.equals("ssi") || displayNameLC.equals("pss")) {
      return sDOC_PROFILO_SANITARIO_SIN;
    }
    if(displayNameLC.indexOf("emerg") >= 0 || displayNameLC.equals("eds")) {
      return sDOC_EMERGENCY_DATA_SET;
    }
    if(displayNameLC.indexOf("vaccin") >= 0 || displayNameLC.indexOf("sched") >= 0) {
      return sDOC_SCHEDA_VACCINALE;
    }
    if(displayNameLC.indexOf("vaccin") >= 0 || displayNameLC.indexOf("cert") >= 0) {
      return sDOC_CERTIFICATO_VACCINALE;
    }
    if(displayNameLC.indexOf("vaccin") >= 0 || displayNameLC.indexOf("immun") >= 0) {
      return sDOC_VACCINAZIONI;
    }
    if(displayNameLC.indexOf("erog") >= 0 || displayNameLC.indexOf("disp") >= 0) {
      return sDOC_EROGAZIONE_FARM;
    }
    if(displayNameLC.indexOf("ref") >= 0 && displayNameLC.indexOf("amb") >= 0) {
      return sDOC_REFERTO_AMBULATORIALE;
    }
    
    return "";
  }
  
  @Override
  public String getDisplayName(String code) {
    if(code == null || code.length() == 0) {
      return "";
    }
    
    // Documenti
    if(code.equals(sDOC_PRESCRIZIONE_FARM))      return "Prescrizione farmaceutica";
    if(code.equals(sDOC_PROFILO_SANITARIO_SIN))  return "Profilo Sanitario Sintetico";
    if(code.equals(sDOC_REFERTO_LABORATORIO))    return "Referto di Laboratorio";
    if(code.equals(sDOC_PRESCRIZIONE_APPAR_MED)) return "Prescrizione apparecchiature medicali";
    if(code.equals(sDOC_LETTERA_DIM_OSP))        return "Lettera di dimissione ospedaliera";
    if(code.equals(sDOC_VERBALE_PRONTO_SOCC))    return "Referto di Pronto Soccorso";
    if(code.equals(sDOC_REFERTO_RADIOLOGIA))     return "Referto di Radiologia";
    if(code.equals(sDOC_REFERTO_ANATOMIA_PAT))   return "Referto di Anatomia Patologica";
    if(code.equals(sDOC_REGISTRAZIONE_CONSENSO)) return "Registrazione consenso";
    if(code.equals(sDOC_CERTIFICATO_MALATTIA))   return "Certificato di malattia";
    if(code.equals(sDOC_PRESCRIZIONE_SPEC))      return "Prescrizione specialistica";
    if(code.equals(sDOC_EROGAZIONE_FARM))        return "Erogazione farmaceutica";
    if(code.equals(sDOC_REFERTO_SPECIALISTICO))  return "Referto specialistico";
    if(code.equals(sDOC_ESENZIONE_DA_REDDITO))   return "Esenzione da reddito";
    if(code.equals(sDOC_REFERTO_AMBULATORIALE))  return "Referto ambulatoriale";
    if(code.equals(sDOC_EROGAZIONE_SPEC))        return "Erogazione specialistica";
    if(code.equals(sDOC_PROMEMORIA_PREN_CUP))    return "Promemoria di prenotazione";
    if(code.equals(sDOC_ANNULLAMENTO))           return "Annullamento";
    if(code.equals(sDOC_REFERTO_GENERICO))       return "Referto Generico";
    if(code.equals(sDOC_EMERGENCY_DATA_SET))     return "Emergency Data Set";
    if(code.equals(sDOC_RICHIESTA_DI_RICOVERO))  return "Richiesta di ricovero";
    if(code.equals(sDOC_RICHIESTA_TRASPORTO))    return "Richiesta di trasporto";
    if(code.equals(sDOC_VACCINAZIONI))           return "Vaccinazioni";
    if(code.equals(sDOC_SCHEDA_PAZIENTE_118))    return "Scheda paziente 118";
    if(code.equals(sDOC_CERTIFICATO_VACCINALE))  return "Certificato vaccinale";
    if(code.equals(sDOC_SCHEDA_VACCINALE))       return "Scheda vaccinale";
    
    // Sezioni
    if(code.equals("54094-8")) return "Note Triage";
    if(code.equals("78337-3")) return "Inquadramento clinico iniziale";
    if(code.equals("47420-5")) return "Inquadramento clinico";
    if(code.equals("56817-0")) return "Valutazione Sanitaria";
    if(code.equals("47519-4")) return "Storia di Procedure";
    if(code.equals("8716-3"))  return "Parametri vitali";
    if(code.equals("62387-6")) return "Interventi";
    if(code.equals("29548-5")) return "Diagnosi";
    if(code.equals("10160-0")) return "Terapie farmacologiche";
    
    // Parametri
    if(code.equals("11378-7")) return "Pressione arteriosa sistolica iniziale";
    if(code.equals("11377-9")) return "Pressione arteriosa diastolica iniziale";
    if(code.equals("11289-6")) return "Temperatura corporea iniziale";
    if(code.equals("18708-8")) return "Frequenza cardiaca iniziale";
    if(code.equals("18686-6")) return "Frequenza respiratoria iniziale";
    
    if(code.equals("11324-1")) return "Glasgow coma score apertura occhi iniziale";
    if(code.equals("67847-4")) return "Glasgow coma score apertura occhi";
    if(code.equals("11326-6")) return "Glasgow coma score risposta verbale iniziale";
    if(code.equals("67846-6")) return "Glasgow coma score risposta verbale";
    if(code.equals("11325-8")) return "Glasgow coma score risposta motoria iniziale";
    if(code.equals("67845-8")) return "Glasgow coma score risposta motoria";
    
    if(code.equals("11376-1")) return "Aree anatomiche interessate dal trauma";
    if(code.equals("74291-6")) return "Revised Trauma Score";
    
    if(code.equals("8480-6"))  return "Pressione arteriosa sistolica";
    if(code.equals("8462-4"))  return "Pressione arteriosa diastolica";
    if(code.equals("8310-5"))  return "Temperatura corporea";
    if(code.equals("8867-4"))  return "Frequenza cardiaca";
    if(code.equals("9279-1"))  return "Frequenza respiratoria";
    
    if(code.equals("29084-1")) return "Stato pupille";
    if(code.equals("59413-5")) return "Saturazione ossigeno";
    if(code.equals("14743-9")) return "Glicemia su sangue capillare";
    if(code.equals("38214-3")) return "Scala dolore Visual Analog Score";
    if(code.equals("56849-3")) return "Ritmo al monitor";
    if(code.equals("22029-3")) return "Esame obiettivo";
    
    return "";
  }
  
  @Override
  public String getTemplateIdRoot(String code) {
    if(code == null || code.length() == 0) {
      return "";
    }
    
    if(code.equals(sDOC_PRESCRIZIONE_FARM))      return "2.16.840.1.113883.2.9.10.2.6";
    if(code.equals(sDOC_PROFILO_SANITARIO_SIN))  return "2.16.840.1.113883.2.9.10.2.4";
    if(code.equals(sDOC_REFERTO_LABORATORIO))    return "2.16.840.1.113883.2.9.10.2.16";
    if(code.equals(sDOC_PRESCRIZIONE_APPAR_MED)) return "2.16.840.1.113883.2.9.10.2.7";
    if(code.equals(sDOC_LETTERA_DIM_OSP))        return "2.16.840.1.113883.2.9.10.2.10.99";
    if(code.equals(sDOC_VERBALE_PRONTO_SOCC))    return "2.16.840.1.113883.2.9.10.1.6.1";
    if(code.equals(sDOC_REFERTO_RADIOLOGIA))     return "2.16.840.1.113883.2.9.10.2.16";
    if(code.equals(sDOC_REFERTO_ANATOMIA_PAT))   return "2.16.840.1.113883.2.9.10.2.16";
    if(code.equals(sDOC_REGISTRAZIONE_CONSENSO)) return "2.16.840.1.113883.2.9.10.2.27";
    if(code.equals(sDOC_CERTIFICATO_MALATTIA))   return "2.16.840.1.113883.2.9.10.2.4";
    if(code.equals(sDOC_PRESCRIZIONE_SPEC))      return "2.16.840.1.113883.2.9.10.2.7";
    if(code.equals(sDOC_EROGAZIONE_FARM))        return "2.16.840.1.113883.2.9.10.2.6";
    if(code.equals(sDOC_REFERTO_SPECIALISTICO))  return "2.16.840.1.113883.2.9.10.2.19";
    if(code.equals(sDOC_ESENZIONE_DA_REDDITO))   return "2.16.840.1.113883.2.9.10.2.4";
    if(code.equals(sDOC_REFERTO_AMBULATORIALE))  return "2.16.840.1.113883.2.9.10.1.9.1";
    if(code.equals(sDOC_EROGAZIONE_SPEC))        return "2.16.840.1.113883.2.9.10.2.7";
    if(code.equals(sDOC_PROMEMORIA_PREN_CUP))    return "2.16.840.1.113883.2.9.10.2.24";
    if(code.equals(sDOC_ANNULLAMENTO))           return "2.16.840.1.113883.2.9.10.2.25";
    if(code.equals(sDOC_REFERTO_GENERICO))       return "2.16.840.1.113883.2.9.10.1.9.1";
    if(code.equals(sDOC_EMERGENCY_DATA_SET))     return "2.16.840.1.113883.2.9.10.2.4";
    if(code.equals(sDOC_RICHIESTA_DI_RICOVERO))  return "2.16.840.1.113883.2.9.10.2.7";
    if(code.equals(sDOC_RICHIESTA_TRASPORTO))    return "2.16.840.1.113883.2.9.10.2.7";
    if(code.equals(sDOC_VACCINAZIONI))           return "2.16.840.1.113883.2.9.10.2.6";
    if(code.equals(sDOC_SCHEDA_PAZIENTE_118))    return "2.16.840.1.113883.2.9.10.1.6.1";
    if(code.equals(sDOC_SCHEDA_VACCINALE))       return "2.16.840.1.113883.2.9.10.1.6.1";
    if(code.equals(sDOC_CERTIFICATO_VACCINALE))  return "2.16.840.1.113883.2.9.10.1.6.1";
    
    return "";
  }
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof ICodeSystem) {
      String objeCodeSystem = ((ICodeSystem) object).getCodeSystem();
      String thisCodeSystem = getCodeSystem();
      if(objeCodeSystem == null && thisCodeSystem == null) return true;
      return objeCodeSystem != null && thisCodeSystem.equals(objeCodeSystem);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    String thisCodeSystem = getCodeSystem();
    return thisCodeSystem != null ? thisCodeSystem.hashCode() : 0;
  }
  
  @Override
  public String toString() {
    return getCodeSystemName();
  }

  @Override
  public String getTemplateId(String code) {
    if(code == null || code.length() == 0) {
      return "";
    }
    
    if(code.equals(sDOC_PRESCRIZIONE_FARM))      return "ITPRF_PRESC_FARMA-001";
    if(code.equals(sDOC_PROFILO_SANITARIO_SIN))  return "ITPRF_PSUM_SSI-001";
    if(code.equals(sDOC_REFERTO_LABORATORIO))    return "ITPRF_REF_LABCH-001";
    if(code.equals(sDOC_PRESCRIZIONE_APPAR_MED)) return "ITPRF_PRESC_MED-001";
    if(code.equals(sDOC_LETTERA_DIM_OSP))        return "ITPRF_LETTDIM-001";
    if(code.equals(sDOC_VERBALE_PRONTO_SOCC))    return "ITPRF_REFER_PS-001";
    if(code.equals(sDOC_REFERTO_RADIOLOGIA))     return "ITPRF_REF_RADIO-001";
    if(code.equals(sDOC_REFERTO_ANATOMIA_PAT))   return "ITPRF_REF_LABIST-001";
    if(code.equals(sDOC_REGISTRAZIONE_CONSENSO)) return "ITPRF_GEST_CONS-001";
    if(code.equals(sDOC_CERTIFICATO_MALATTIA))   return "ITPRF_CERT_INPS-001";
    if(code.equals(sDOC_PRESCRIZIONE_SPEC))      return "ITPRF_PRESC_SPEC-001";
    if(code.equals(sDOC_EROGAZIONE_FARM))        return "ITPRF_EROG_FARMA-001";
    if(code.equals(sDOC_REFERTO_SPECIALISTICO))  return "ITPRF_REF_SPEC-001";
    if(code.equals(sDOC_ESENZIONE_DA_REDDITO))   return "ITPRF_ESE_RED-001";
    if(code.equals(sDOC_REFERTO_AMBULATORIALE))  return "ITPRF_REF_AMB-001";
    if(code.equals(sDOC_EROGAZIONE_SPEC))        return "ITPRF_EROG_SPEC-001";
    if(code.equals(sDOC_PROMEMORIA_PREN_CUP))    return "ITPRF_PRENOTAZIONE-001";
    if(code.equals(sDOC_ANNULLAMENTO))           return "ITPRF_ANNULLAMENTO-001";
    if(code.equals(sDOC_REFERTO_GENERICO))       return "ITPRF_REF_GEN-001";
    if(code.equals(sDOC_EMERGENCY_DATA_SET))     return "ITPRF_PSUM_EDS-001";
    if(code.equals(sDOC_RICHIESTA_DI_RICOVERO))  return "ITPRF_PRESC_RICO-001";
    if(code.equals(sDOC_RICHIESTA_TRASPORTO))    return "ITPRF_PRESC_TRAS-001";
    if(code.equals(sDOC_VACCINAZIONI))           return "ITPRF_VACC-001";
    if(code.equals(sDOC_SCHEDA_PAZIENTE_118))    return "ITPRF_SCHEDA_PAZ-001";
    if(code.equals(sDOC_SCHEDA_VACCINALE))       return "ITPRF_SCHEDA_VAC-001";
    if(code.equals(sDOC_CERTIFICATO_VACCINALE))  return "ITPRF_CERT_VAC-001";
    
    return "";
  }
}
