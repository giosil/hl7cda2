<xsl:stylesheet xmlns:n1="urn:hl7-org:v3" xmlns:n2="urn:hl7-org:v3/meta/voc" xmlns:voc="urn:hl7-org:v3/voc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" id="urn:medir:stylesheet:cda:1.2">
  <xsl:output encoding="UTF-8" indent="no" method="xml" omit-xml-declaration="yes" standalone="yes"/>
  <xsl:variable name="tableWidth">50%</xsl:variable>
  <xsl:variable name="codeDocument" select="/n1:ClinicalDocument/n1:code/@code"/>
  <xsl:variable name="title">
    <xsl:call-template name="setTitleDocument">
      <xsl:with-param name="titleCDA" select="/n1:ClinicalDocument/n1:title"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:template match="/">
    <xsl:apply-templates select="n1:ClinicalDocument"/>
    <xsl:apply-templates select="n1:OwnResponsibilityDocument"/>
  </xsl:template>
  <xsl:template match="n1:ClinicalDocument">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <xsl:comment>Do NOT edit this HTML directly, it was generated via an XSLt transformation from the original release 2 CDA Document.</xsl:comment>
        <title>
          <xsl:value-of select="$title"/>
        </title>
        <style media="screen" type="text/css">body {  color: #003366;  font-size: 11px;  line-height: normal;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  margin: 10px;  scrollbar-3dlight-color: #cfeafc;  scrollbar-arrow-color: #003366;  scrollbar-darkshadow-color: #cfeafc;  scrollbar-face-color: #cfeafc;  scrollbar-highlight-color: #003366;  scrollbar-shadow-color: #003366;  scrollbar-track-color: #cfeafc }  a {  color: #003366;  text-decoration: none;  }  table {  font-size: 11px;  border: solid 1px;  border-color: #003366;  border-spacing: 0px;  background-color: #cfeafc;  }   caption {  font-size: 11px;  font-color: #003366;  font-weight: bold;  text-transform: capitalize;  text-align: left;  }  th {  border: solid 1px;  border-color:#003366;  text-transform: capitalize;  font-variant: small-caps;  font-size: 10pt;  text-align: left;  vertical-align: middle;  padding-left: 3pt;  padding-right: 3pt;  padding-bottom: 2px;  padding-top: 1px;  white-space: nowrap;  }  td {  border: solid 1px;  border-color:#003366;  text-transform: capitalize;  font-size: 11px;  text-align: left;  vertical-align: middle;  padding-left: 5pt;  padding-right: 5pt;  }  .input {  color: #003366;  font-size: 10px;  font-family: Arial Unicode MS, Verdana, Arial, sans-serif;  background-color: #cfeafc;  border: solid 1px;   }   h1 {  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  font-size: 14pt;  text-transform: capitalize;  font-variant: small-caps;  }  h2 {  font-size: 11px;  }  div {  color: #003366;  font-size: 11px;  line-height: normal;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  text-align: justify;  margin-right: 50px;  }  span {  color: #003366;  font-size: 12px;  line-height: normal;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  text-transform: capitalize;  font-weight: bolder;  }  center {  margin-left: -50px;  margin-top: 25px;  color: #003366;  font-size: 11px;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  font-weight: bolder;  }</style>
      </head>
      <body>
        <h1>
          <xsl:value-of select="$title"/>
        </h1>
        <xsl:if test="($codeDocument='22034-3' or $codeDocument='11502-2')">
          <xsl:if test="/n1:ClinicalDocument/n1:title/n1:row">
            <table width="100%" cellpadding="0" cellspacing="0">
              <caption>Dati laboratorio</caption>
              <xsl:for-each select="/n1:ClinicalDocument/n1:title/n1:row">
                <tr>
                  <xsl:if test="@displayable='true'">
                    <th width="20%">
                      <xsl:call-template name="titleCode">
                        <xsl:with-param name="code" select="@code"/>
                      </xsl:call-template>
                    </th>
                    <td width="80%">
                      <xsl:value-of select="@value"/>
                    </td>
                  </xsl:if>
                </tr>
              </xsl:for-each>
            </table>
          </xsl:if>
          <hr/>
        </xsl:if>
        <table cellpadding="0" cellspacing="0" width="100%">
          <caption>Caratteristiche generali documento</caption>
          <tr>
            <th width="20%">
              <xsl:text>Codice Identificativo CDA</xsl:text>
            </th>
            <td width="80%" style="font-weight: bold;" colspan="3">
              <xsl:call-template name="getConcatID">
                <xsl:with-param name="root" select="/n1:ClinicalDocument/n1:id/@root"/>
                <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:id/@extension"/>
              </xsl:call-template>
            </td>
          </tr>
          <tr>
            <th width="10%">
              <xsl:text>Livello riservatezza documento</xsl:text>
            </th>
            <td width="40%">
              <xsl:call-template name="getConfidenzialityLevel">
                <xsl:with-param name="confidenziality" select="/n1:ClinicalDocument/n1:confidentialityCode"/>
              </xsl:call-template>
            </td>
            <th style="text-align: right;" width="15%">
              <xsl:text>Versione documento</xsl:text>
            </th>
            <td width="35">
              <xsl:choose>
                <xsl:when test="boolean(/n1:ClinicalDocument/n1:versionNumber/@value = '1')">
                  <xsl:text>Originale</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>Revisione</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </td>
          </tr>
          <xsl:for-each select="/n1:ClinicalDocument/n1:relatedDocument">
            <tr>
              <th width="10%">
                <xsl:call-template name="translateCode">
                  <xsl:with-param name="code" select="/n1:ClinicalDocument/n1:relatedDocument/@typeCode"/>
                </xsl:call-template>
              </th>
              <td width="40%">
                <xsl:call-template name="relatedDocumentType">
                  <xsl:with-param name="parentDocumentID" select="/n1:ClinicalDocument/n1:relatedDocument/n1:parentDocument"/>
                </xsl:call-template>
              </td>
              <th style="text-align: right;" width="15%">
                <xsl:text>Codice Identificativo</xsl:text>
                <br/>
                <xsl:text>documento collegato</xsl:text>
              </th>
              <td width="35%">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:relatedDocument/n1:parentDocument/n1:id/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/n1:ClinicalDocument/n1:inFulfillmentOf">
            <tr>
              <th width="10%">
                <xsl:choose>
                  <xsl:when test="contains('28574-2.34105-7', $codeDocument)">
                    <xsl:text>Documento Accettazione collegato</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:text>Documento Prescrizione collegato (IUP)</xsl:text>
                  </xsl:otherwise>
                </xsl:choose>
              </th>
              <td colspan="3">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:inFulfillmentOf/n1:order/n1:id/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
            <xsl:if test="($codeDocument='22034-3' or $codeDocument='11502-2') and (/n1:ClinicalDocument/n1:inFulfillmentOf/n1:order/n1:priorityCode)">
              <tr>
                <th width="10%">
                  <xsl:text>Priorita' Prescrizioni</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:call-template name="priority">
                    <xsl:with-param name="code" select="/n1:ClinicalDocument/n1:inFulfillmentOf/n1:order/n1:priorityCode/@code"/>
                  </xsl:call-template>
                </td>
              </tr>
            </xsl:if>
          </xsl:for-each>
          <xsl:if test="contains('29304-3.34109-9', $codeDocument)">
            <tr>
              <th width="10%">
                <xsl:text>Codice Identificativo</xsl:text>
                <br/>
                <xsl:text>Prescrizione Cartacea (PLG)</xsl:text>
              </th>
              <td colspan="3">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:setId/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
          </xsl:if>
          <xsl:if test="($codeDocument='22034-3' or $codeDocument='11502-2') and (/n1:ClinicalDocument/n1:priorityCode)">
            <tr>
              <th width="10%">
                <xsl:text>Livello Priorita' Documento </xsl:text>
              </th>
              <td width="40%" style="text-align: left;" colspan="3">
                <xsl:value-of select="/n1:ClinicalDocument/n1:priorityCode/@displayName"/>
              </td>
            </tr>
          </xsl:if>
          <xsl:if test="contains('47039-3.28574-2', $codeDocument)">
            <tr>
              <th width="10%">
                <xsl:text>Codice Nososlogico</xsl:text>
                <br/>
                <xsl:text>di Accettazione</xsl:text>
              </th>
              <td colspan="3">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:setId/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
          </xsl:if>
        </table>
        <hr/>
        <xsl:variable name="nullFlavorID" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:id/@nullFlavor"/>
        <xsl:choose>
          <xsl:when test="$nullFlavorID = 'NA'"/>
          <xsl:otherwise>
            <table cellpadding="0" cellspacing="0" width="100%">
              <caption>Dati relativi al paziente</caption>
              <tr>
                <th width="10%">
                  <xsl:text>Assistito</xsl:text>
                </th>
                <td style="font-weight: bold;" width="40%">
                  <xsl:variable name="nullFlavor" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:name/@nullFlavor"/>
                  <xsl:choose>
                    <xsl:when test="$nullFlavor='MSK'">DATI NON DISPONIBILI</xsl:when>
                    <xsl:otherwise>
                      <xsl:call-template name="getName">
                        <xsl:with-param name="name" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:name"/>
                      </xsl:call-template>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
                <th style="text-align: right;" width="25%">
                  <xsl:text>Codice Identificativo</xsl:text>
                </th>
                <td style="font-weight: bold;" width="25%">
                  <xsl:value-of select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:id/@extension"/>
                </td>
              </tr>
              <xsl:if test="boolean(/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:addr)">
                <tr>
                  <th>
                    <xsl:text>Indirizzo</xsl:text>
                  </th>
                  <td colspan="3">
                    <xsl:call-template name="getContactInfo">
                      <xsl:with-param name="contact" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole"/>
                    </xsl:call-template>
                  </td>
                </tr>
              </xsl:if>
              <tr>
                <th width="10%">
                  <xsl:text>Data/Luogo di nascita</xsl:text>
                </th>
                <td width="40%">
                  <xsl:if test="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthTime">
                    <xsl:call-template name="formatDate">
                      <xsl:with-param name="date" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthTime/@value"/>
                    </xsl:call-template>
                  </xsl:if>
                  <xsl:if test="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthplace">
                    <br/>
                    <xsl:call-template name="getContactInfo">
                      <xsl:with-param name="contact" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:birthplace/n1:place"/>
                    </xsl:call-template>
                  </xsl:if>
                </td>
                <th style="text-align: right;" width="5%">
                  <xsl:text>Sesso</xsl:text>
                </th>
                <td width="45%">
                  <xsl:variable name="sex" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:administrativeGenderCode/@code"/>
                  <xsl:choose>
                    <xsl:when test="$sex='M'">maschile</xsl:when>
                    <xsl:when test="$sex='F'">femminile</xsl:when>
                    <xsl:otherwise>DATO NON DISPONIBILE</xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <xsl:if test="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:id/n1:translation">
                <tr>
                  <th>
                    <xsl:text>Codice Interno Paziente</xsl:text>
                  </th>
                  <td colspan="3">
                    <xsl:value-of select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:id/n1:translation/@code"/>
                  </td>
                </tr>
              </xsl:if>
              <xsl:choose>
                <xsl:when test="contains('[CODE_RETRACTION_DOCTYPE].[CODE_CONSENT_DOCTYPE].[CODE_RESTRICTION_DOCTYPE].34133-9.34878-9', $codeDocument)">
                  <xsl:variable name="nullFlavorUNK" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:guardian/n1:id/@nullFlavor"/>
                  <xsl:choose>
                    <xsl:when test="$nullFlavorUNK='UNK'"/>
                    <xsl:otherwise>
                      <tr>
                        <th colspan="4">
                          <xsl:text>Tutore Legale dell'Assistito</xsl:text>
                        </th>
                      </tr>
                      <tr>
                        <td colspan="2">
                          <xsl:call-template name="getName">
                            <xsl:with-param name="name" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:guardian/n1:guardianPerson/n1:name"/>
                          </xsl:call-template>
                          <xsl:for-each select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:guardian/n1:addr">
                            <br/>
                            <xsl:call-template name="getContactInfo">
                              <xsl:with-param name="contact" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:guardian"/>
                            </xsl:call-template>
                          </xsl:for-each>
                        </td>
                        <th style="text-align: right;" width="25%">
                          <xsl:text>Codice Identificativo</xsl:text>
                        </th>
                        <td style="font-weight: bold;">
                          <xsl:value-of select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:guardian/n1:id/@extension"/>
                        </td>
                      </tr>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:when>
              </xsl:choose>
            </table>
            <xsl:if test="boolean(/n1:ClinicalDocument/n1:participant[@typeCode='IND'])">
              <table cellpadding="0" cellspacing="0" width="100%">
                <xsl:choose>
                  <xsl:when test="contains('34878-9.34133-9', $codeDocument)">
                    <caption>Dati relativi ad contatti di parenti riferiti al paziente</caption>
                  </xsl:when>
                  <xsl:otherwise>
                    <caption>Dati relativi alla ASL di competenza del paziente</caption>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:call-template name="contactTarget"/>
              </table>
            </xsl:if>
            <hr/>
          </xsl:otherwise>
        </xsl:choose>
        <table cellpadding="0" cellspacing="0" width="100%">
          <caption>Dati relativi alla struttura responsabile della conservazione della registrazione originale</caption>
          <tr>
            <th width="20%">
              <xsl:text>Codice Identificativo</xsl:text>
            </th>
            <td style="font-weight: bold;" width="80%">
              <xsl:call-template name="getConcatID">
                <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:id/@extension"/>
              </xsl:call-template>
            </td>
          </tr>
          <tr>
            <th width="20%">
              <xsl:text>Struttura Sanitaria</xsl:text>
              <br/>
              <xsl:text>(ASL/AO, Studio Medico, ecc.)</xsl:text>
            </th>
            <td width="80%">
              <xsl:choose>
                <xsl:when test="boolean(/n1:ClinicalDocument/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name)">
                  <xsl:value-of select="/n1:ClinicalDocument/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name"/>
                  <br/>
                  <xsl:if test="boolean(/n1:ClinicalDocument/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:addr)">
                    <xsl:call-template name="getContactInfo">
                      <xsl:with-param name="contact" select="/n1:ClinicalDocument/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization"/>
                    </xsl:call-template>
                  </xsl:if>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>DATO NON DISPONIBILE</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </td>
          </tr>
          <tr>
            <th width="20%">Documento creato il</th>
            <td width="80%">
              <xsl:call-template name="formatDate">
                <xsl:with-param name="date" select="/n1:ClinicalDocument/n1:effectiveTime/@value"/>
              </xsl:call-template>
            </td>
          </tr>
        </table>
        <xsl:if test="($codeDocument='22034-3' or $codeDocument='11502-2') and (/n1:ClinicalDocument/n1:informationRecipient)">
          <table cellpadding="0" cellspacing="0" width="100%">
            <caption>Dati relativi al Reparto di Refertazione</caption>
            <tr>
              <th width="10%">
                <xsl:text>Codice Identificativo</xsl:text>
              </th>
              <td colspan="3">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="root" select="/n1:ClinicalDocument/n1:informationRecipient/n1:intendedRecipient/n1:receivedOrganization/n1:id/@root"/>
                  <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:informationRecipient/n1:intendedRecipient/n1:receivedOrganization/n1:id/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
            <xsl:if test ="/n1:ClinicalDocument/n1:informationRecipient/n1:intendedRecipient/n1:receivedOrganization/n1:id/@displayable = 'true'">
              <tr>
                <th width="10%">
                  <xsl:text>Reparto</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:value-of select="/n1:ClinicalDocument/n1:informationRecipient/n1:intendedRecipient/n1:receivedOrganization/n1:id/@assigningAuthorityName"/>
                </td>
              </tr>
            </xsl:if>
            <tr>
              <th width="10%">
                <xsl:text>Nome</xsl:text>
              </th>
              <td colspan="3">
                <xsl:value-of select="/n1:ClinicalDocument/n1:informationRecipient/n1:intendedRecipient/n1:receivedOrganization/n1:name"/>
              </td>
            </tr>
          </table>
        </xsl:if>
        <xsl:if test="boolean(/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='LIC'])">
          <hr/>
          <table cellpadding="0" cellspacing="0" width="100%">
            <caption>Dati relativi al medico sostituito</caption>
            <xsl:call-template name="subsituteDoctor"/>
          </table>
        </xsl:if>
        <xsl:if test="boolean(/n1:ClinicalDocument/n1:informant)">
          <hr/>
          <table cellpadding="0" cellspacing="0" width="100%">
            <caption>Dati relativi al medico suggeritore</caption>
            <xsl:call-template name="supportDoctor"/>
          </table>
        </xsl:if>
        <xsl:for-each select="/n1:ClinicalDocument/n1:componentOf">
          <hr/>
          <table cellpadding="0" cellspacing="0" width="100%">
            <caption>Dati relativi iter strutturale della richiesta</caption>
            <xsl:if test="$codeDocument='47039-3'">
              <tr>
                <th width="20%">
                  <xsl:text>Data accettazione ricovero</xsl:text>
                </th>
                <td width="80%">
                  <xsl:call-template name="formatDate">
                    <xsl:with-param name="date" select="n1:encompassingEncounter/n1:effectiveTime/@value"/>
                  </xsl:call-template>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="contains('34105-7.28574-2', $codeDocument)">
              <tr>
                <th width="10%">
                  <xsl:text>Data accettazione ricovero</xsl:text>
                </th>
                <td width="40%">
                  <xsl:call-template name="formatDate">
                    <xsl:with-param name="date" select="n1:encompassingEncounter/n1:effectiveTime/n1:low/@value"/>
                  </xsl:call-template>
                </td>
                <th style="text-align='right'" width="10%">
                  <xsl:text>Data dimissione ricovero</xsl:text>
                </th>
                <td width="40%">
                  <xsl:call-template name="formatDate">
                    <xsl:with-param name="date" select="n1:encompassingEncounter/n1:effectiveTime/n1:high/@value"/>
                  </xsl:call-template>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="contains('18748-4.11502-2', $codeDocument)">
              <tr>
                <th width="10%">
                  <xsl:text>Data esecuzione prestazione</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:call-template name="formatDate">
                    <xsl:with-param name="date" select="n1:encompassingEncounter/n1:effectiveTime/@value"/>
                  </xsl:call-template>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="$codeDocument='22034-3'">
              <tr>
                <th width="10%">
                  <xsl:text>Codice Nosologico</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:value-of select="n1:encompassingEncounter/n1:realmCode/@code"/>
                </td>
              </tr>
              <tr>
                <th width="10%">
                  <xsl:text>Tipologia Esame</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:call-template name="translateCode">
                    <xsl:with-param name="code" select="n1:encompassingEncounter/n1:code/@code"/>
                  </xsl:call-template>
                </td>
              </tr>
              <tr>
                <th width="10%">
                  <xsl:text>Data prelievo esame</xsl:text>
                </th>
                <td width="40%">
                  <xsl:call-template name="formatDate">
                    <xsl:with-param name="date" select="n1:encompassingEncounter/n1:effectiveTime/n1:low/@value"/>
                  </xsl:call-template>
                </td>
                <th style="text-align='right'" width="10%">
                  <xsl:text>Data esaminazione esame</xsl:text>
                </th>
                <td width="40%">
                  <xsl:call-template name="formatDate">
                    <xsl:with-param name="date" select="n1:encompassingEncounter/n1:effectiveTime/n1:high/@value"/>
                  </xsl:call-template>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="$codeDocument='28568-4'">
              <tr>
                <th width="10%">
                  <xsl:text>Data accettazione</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:call-template name="formatDate">
                    <xsl:with-param name="date" select="n1:encompassingEncounter/n1:effectiveTime/@value"/>
                  </xsl:call-template>
                </td>
              </tr>
              <tr>
                <th width="10%">
                  <xsl:text>Struttura Sanitaria</xsl:text>
                  <br/>
                  <xsl:text>(codifica ministeriale)</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:call-template name="getConcatID">
                    <xsl:with-param name="extension" select="/n1:ClinicalDocument/n1:componentOf/n1:encompassingEncounter/n1:location/n1:healthCareFacility/n1:serviceProviderOrganization/n1:id/@extension"/>
                  </xsl:call-template>
                  <xsl:if test="boolean(/n1:ClinicalDocument/n1:componentOf/n1:encompassingEncounter/n1:location/n1:healthCareFacility/n1:serviceProviderOrganization/n1:name)">
                    <br/>
                    <xsl:call-template name="getOnlyNameValue">
                      <xsl:with-param name="name" select="/n1:ClinicalDocument/n1:componentOf/n1:encompassingEncounter/n1:location/n1:healthCareFacility/n1:serviceProviderOrganization/n1:name"/>
                    </xsl:call-template>
                  </xsl:if>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="boolean(n1:encompassingEncounter/n1:location/n1:healthCareFacility/n1:code)">
              <tr>
                <th width="10%">
                  <xsl:text>Disciplina (reparto)</xsl:text>
                  <br/>
                  <xsl:text>codifica Ministero della Salute</xsl:text>
                </th>
                <td colspan="3">
                  <xsl:call-template name="getConcatID">
                    <xsl:with-param name="extension" select="n1:encompassingEncounter/n1:location/n1:healthCareFacility/n1:code/@code"/>
                  </xsl:call-template>
                  <xsl:if test="not(n1:encompassingEncounter/n1:location/n1:healthCareFacility/n1:code/@displayName ='')">
                    <br/>
                    <xsl:call-template name="getDisplayName">
                      <xsl:with-param name="displayName" select="n1:encompassingEncounter/n1:location/n1:healthCareFacility/n1:code/@displayName"/>
                    </xsl:call-template>
                  </xsl:if>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="/n1:ClinicalDocument/n1:componentOf/n1:encompassingEncounter/n1:realmCode">
              <tr>
                <th width="10%">
                  <xsl:text>Codice nosologico</xsl:text>
                </th>
                <td>
                  <xsl:value-of select="/n1:ClinicalDocument/n1:componentOf/n1:encompassingEncounter/n1:realmCode/@code"/>
                </td>
              </tr>
            </xsl:if>
            <xsl:if test="/n1:ClinicalDocument/n1:componentOf/n1:encompassingEncounter/n1:id">
              <tr>
                <th width="10%">
                  <xsl:text>Codice richiesta interna</xsl:text>
                </th>
                <td>
                  <xsl:value-of select="/n1:ClinicalDocument/n1:componentOf/n1:encompassingEncounter/n1:id/@extension"/>
                </td>
              </tr>
            </xsl:if>
          </table>
        </xsl:for-each>
        <xsl:for-each select="/n1:ClinicalDocument/n1:documentationOf">
          <hr/>
          <table cellpadding="0" cellspacing="0" width="100%">
            <xsl:choose>
              <xsl:when test="contains('18748-4.11502-2.22034-3.28568-4', $codeDocument)">
                <caption>Dati relativi al dettaglio sulla prestazione eseguita</caption>
                <tr>
                  <th width="10%">
                    <xsl:text>Codice Identificativo</xsl:text>
                    <br/>
                    <xsl:text>(Catalogo Prestazioni Italia)</xsl:text>
                  </th>
                  <td width="90%">
                    <xsl:call-template name="getConcatID">
                      <xsl:with-param name="extension" select="n1:serviceEvent/n1:code/@code"/>
                    </xsl:call-template>
                  </td>
                </tr>
                <xsl:if test="not(n1:serviceEvent/n1:code/@displayName ='')">
                  <tr>
                    <th width="10%">
                      <xsl:text>Descrizione della prestazione</xsl:text>
                    </th>
                    <td width="90%">
                      <xsl:value-of select="n1:serviceEvent/n1:code/@displayName"/>
                    </td>
                  </tr>
                </xsl:if>
                <tr>
                  <th width="10%">
                    <xsl:text>Data/Ora</xsl:text>
                    <br/>
                    <xsl:text>esecuzione prestazione</xsl:text>
                  </th>
                  <td width="90%">
                    <xsl:call-template name="formatDate">
                      <xsl:with-param name="date" select="n1:serviceEvent/n1:effectiveTime/@value"/>
                    </xsl:call-template>
                  </td>
                </tr>
              </xsl:when>
              <xsl:otherwise>
                <xsl:if test="contains('34878-9.34133-9', $codeDocument)">
                  <caption>Dati relativi al periodo di riferimento del documento di Patient Summary</caption>
                  <tr>
                    <th width="10%">
                      <xsl:text>Inizio periodo</xsl:text>
                      <br/>
                      <xsl:text>raccolta dati clinici</xsl:text>
                    </th>
                    <td width="40%">
                      <xsl:choose>
                        <xsl:when test="boolean(n1:serviceEvent/n1:effectiveTime[@nullFlavor])">
                          <xsl:text>DATO NON DISPONIBILE</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:call-template name="formatDate">
                            <xsl:with-param name="date" select="n1:serviceEvent/n1:effectiveTime/n1:low/@value"/>
                          </xsl:call-template>
                        </xsl:otherwise>
                      </xsl:choose>
                    </td>
                    <th style="font-weight: bold;" width="10%">
                      <xsl:text>Fine periodo</xsl:text>
                      <br/>
                      <xsl:text>raccolta dati clinici</xsl:text>
                    </th>
                    <td style="text-align='right;'" width="40%">
                      <xsl:choose>
                        <xsl:when test="boolean(n1:serviceEvent/n1:effectiveTime[@nullFlavor])">
                          <xsl:text>DATO NON DISPONIBILE</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:call-template name="formatDate">
                            <xsl:with-param name="date" select="n1:serviceEvent/n1:effectiveTime/n1:high/@value"/>
                          </xsl:call-template>
                        </xsl:otherwise>
                      </xsl:choose>
                    </td>
                  </tr>
                </xsl:if>
                <xsl:if test="contains('[CODE_CONSENT_DOCTYPE].[CODE_RETRACTION_DOCTYPE].[CODE_RESTRICTION_DOCTYPE]', $codeDocument)">
                  <caption>Dati relativi alla documentazione dell'evento di raccolta del consenso</caption>
                  <tr>
                    <th width="10%">
                      <xsl:text>Numero Protocollo</xsl:text>
                      <br/>
                      <xsl:text>assegnato al documento cartaceo</xsl:text>
                    </th>
                    <td colspan="3">
                      <xsl:value-of select="n1:serviceEvent/n1:realmCode/@code"/>
                    </td>
                  </tr>
                  <xsl:for-each select="n1:serviceEvent/n1:effectiveTime">
                    <tr>
                      <th width="10%">
                        <xsl:text>Data raccolta consenso</xsl:text>
                      </th>
                      <td colspan="3">
                        <xsl:call-template name="formatDate">
                          <xsl:with-param name="date" select="@value"/>
                        </xsl:call-template>
                      </td>
                    </tr>
                  </xsl:for-each>
                  <tr>
                    <th colspan="2">
                      <xsl:text>Titolare trattamento dati</xsl:text>
                    </th>
                    <th style="font-weight:bold;" width="10%">
                      <xsl:text>Codice Identificativo</xsl:text>
                    </th>
                    <td width="30%">
                      <xsl:call-template name="getConcatID">
                        <xsl:with-param name="extension" select="n1:serviceEvent/n1:performer/n1:assignedEntity/n1:id/@extension"/>
                      </xsl:call-template>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4">
                      <xsl:value-of select="n1:serviceEvent/n1:performer/n1:assignedEntity/n1:representedOrganization/n1:name"/>
                      <br/>
                      <xsl:call-template name="getContactInfo">
                        <xsl:with-param name="contact" select="n1:serviceEvent/n1:performer/n1:assignedEntity/n1:representedOrganization"/>
                      </xsl:call-template>
                    </td>
                  </tr>
                </xsl:if>
              </xsl:otherwise>
            </xsl:choose>
          </table>
        </xsl:for-each>
        <hr/>
        <xsl:apply-templates select="n1:component/n1:structuredBody|n1:component/n1:nonXMLBody"/>
        <hr/>
        <xsl:call-template name="bottomline"/>
        <hr/>
      </body>
    </html>
  </xsl:template>
  <xsl:template match="n1:component/n1:structuredBody">
    <xsl:apply-templates select="n1:component/n1:section"/>
  </xsl:template>
  <xsl:template match="n1:component/n1:nonXMLBody">
    <xsl:if test="boolean(n1:text/@mediaType = 'application/pdf')">
      <hr/>
      <span style="font-weight:bold;" title="PDF Allegato">Contiene allegato</span>
      <br/>
    </xsl:if>
  </xsl:template>
  <xsl:template match="n1:component/n1:section">
    <xsl:apply-templates select="n1:title">
      <xsl:with-param name="code" select="n1:code/@code"/>
    </xsl:apply-templates>
    <ul xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates select="n1:text"/>
      <xsl:if test="n1:component/n1:section">
        <div>
          <br/>
          <xsl:apply-templates select="n1:component/n1:section"/>
        </div>
      </xsl:if>
    </ul>
  </xsl:template>
  <xsl:template match="n1:title">
    <xsl:param name="code" select="''"/>
    <span xmlns="http://www.w3.org/1999/xhtml" style="font-weight:bold;" title="{$code}">
      <xsl:value-of select="."/>
    </span>
    <xsl:if test="contains('[CODE_RETRACTION_DOCTYPE].[CODE_CONSENT_DOCTYPE].[CODE_RESTRICTION_DOCTYPE]', $codeDocument)">
      <sup xmlns="http://www.w3.org/1999/xhtml">
        <xsl:text>*</xsl:text>
      </sup>
    </xsl:if>
  </xsl:template>
  <xsl:template match="n1:text">
    <xsl:apply-templates/>
    <xsl:choose>
      <xsl:when test="contains('[CODE_RETRACTION_DOCTYPE].[CODE_CONSENT_DOCTYPE].[CODE_RESTRICTION_DOCTYPE]', $codeDocument)">
        <center xmlns="http://www.w3.org/1999/xhtml">(*) L'assistito ha letto con attenzione le informazioni riportate nell'informativa per il trattamento dei dati personali in ambito FSE - MEDIR prima di firmare il Modulo del Consenso (Ai sensi del D. Lgs. 196/2003, Ex Art. 76)</center>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="n1:paragraph">
    <div xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </div>
  </xsl:template>
  <xsl:template match="n1:br">
    <xsl:apply-templates/>
    <br xmlns="http://www.w3.org/1999/xhtml"/>
  </xsl:template>
  <xsl:template match="n1:content[@ID]">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="n1:content">
    <span xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </span>
  </xsl:template>
  <xsl:template match="n1:list">
    <xsl:if test="n1:caption">
      <span xmlns="http://www.w3.org/1999/xhtml" style="font-weight:bold; ">
        <xsl:apply-templates select="n1:caption"/>
      </span>
    </xsl:if>
    <xsl:choose>
      <xsl:when test="@listType='ordered'">
        <ol xmlns="http://www.w3.org/1999/xhtml">
          <xsl:for-each select="n1:item">
            <li>
              <xsl:apply-templates/>
            </li>
          </xsl:for-each>
        </ol>
      </xsl:when>
      <xsl:otherwise>
        <ul xmlns="http://www.w3.org/1999/xhtml">
          <xsl:for-each select="n1:item">
            <li>
              <xsl:apply-templates/>
            </li>
          </xsl:for-each>
        </ul>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="n1:caption">
    <xsl:apply-templates/>
    <xsl:text/>
  </xsl:template>
  <xsl:template match="n1:table/@*|n1:thead/@*|n1:tfoot/@*|n1:tbody/@*|n1:colgroup/@*|n1:col/@*|n1:tr/@*|n1:th/@*|n1:td/@*">
    <xsl:copy>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="n1:table">
    <table xmlns="http://www.w3.org/1999/xhtml" cellpadding="0" cellspacing="0">
      <xsl:apply-templates/>
    </table>
  </xsl:template>
  <xsl:template match="n1:thead">
    <thead xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </thead>
  </xsl:template>
  <xsl:template match="n1:tfoot">
    <tfoot xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </tfoot>
  </xsl:template>
  <xsl:template match="n1:tbody">
    <tbody xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </tbody>
  </xsl:template>
  <xsl:template match="n1:colgroup">
    <colgroup xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </colgroup>
  </xsl:template>
  <xsl:template match="n1:col">
    <col xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </col>
  </xsl:template>
  <xsl:template match="n1:tr">
    <tr xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </tr>
  </xsl:template>
  <xsl:template match="n1:th">
    <th xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </th>
  </xsl:template>
  <xsl:template match="n1:td">
    <td xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </td>
  </xsl:template>
  <xsl:template match="n1:table/n1:caption">
    <span xmlns="http://www.w3.org/1999/xhtml">
      <xsl:apply-templates/>
    </span>
  </xsl:template>
  <xsl:template match="n1:renderMultiMedia">
    <xsl:variable name="imageRef" select="@referencedObject"/>
    <xsl:choose>
      <xsl:when test="//n1:regionOfInterest[@ID=$imageRef]">
        <xsl:if test="//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value[@mediaType='image/gif' or @mediaType='image/jpeg']">
          <br xmlns="http://www.w3.org/1999/xhtml" clear="all"/>
          <xsl:element name="img">
            <xsl:attribute name="src">
              <xsl:value-of select="//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value/n1:reference/@value"/>
            </xsl:attribute>
          </xsl:element>
          <br xmlns="http://www.w3.org/1999/xhtml" clear="all"/>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="//n1:observationMedia[@ID=$imageRef]/n1:value/n1:thumbnail[@mediaType='image/gif' or  @mediaType='image/jpeg']">
          <br xmlns="http://www.w3.org/1999/xhtml" clear="all"/>
          <xsl:element name="img">
            <xsl:attribute name="src">
              <xsl:value-of select="//n1:observationMedia[@ID=$imageRef]/n1:value/n1:thumbnail"/>
            </xsl:attribute>
          </xsl:element>
          <br xmlns="http://www.w3.org/1999/xhtml" clear="all"/>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:if test="//n1:observationMedia/n1:entryRelationship/n1:observation">
      <br xmlns="http://www.w3.org/1999/xhtml" clear="all"/>
      <div style="font-weight: bold;">Informazioni correlate all'allegato multimediale</div>
      <table width="25%" cellpadding="0" cellspacing="0">
        <xsl:if test="//n1:observationMedia/n1:entryRelationship/n1:observation/n1:value">
          <tr>
            <th width="50%">Osservazione</th>
            <td width="50%">
              <xsl:value-of select="//n1:observationMedia/n1:entryRelationship/n1:observation/n1:value"/>
            </td>
          </tr>
        </xsl:if>
        <xsl:if test="//n1:observationMedia/n1:entryRelationship/n1:observation/n1:interpretationCode">
          <tr>
            <th width="50%">Indicazione</th>
            <td width="50%">
              <xsl:value-of select="//n1:observationMedia/n1:entryRelationship/n1:observation/n1:interpretationCode/@displayName"/>
            </td>
          </tr>
        </xsl:if>
      </table>
    </xsl:if>
    <br xmlns="http://www.w3.org/1999/xhtml" clear="all"/>
  </xsl:template>
  <xsl:template match="//n1:*[@styleCode]">
    <xsl:if test="@styleCode='Bold'">
      <xsl:element name="b">
        <xsl:apply-templates/>
      </xsl:element>
    </xsl:if>
    <xsl:if test="@styleCode='Italics'">
      <xsl:element name="i">
        <xsl:apply-templates/>
      </xsl:element>
    </xsl:if>
    <xsl:if test="@styleCode='Underline'">
      <xsl:element name="u">
        <xsl:apply-templates/>
      </xsl:element>
    </xsl:if>
    <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Italics') and not (contains(@styleCode, 'Underline'))">
      <xsl:element name="b">
        <xsl:element name="i">
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Italics'))">
      <xsl:element name="b">
        <xsl:element name="u">
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Bold'))">
      <xsl:element name="i">
        <xsl:element name="u">
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and contains(@styleCode, 'Bold')">
      <xsl:element name="b">
        <xsl:element name="i">
          <xsl:element name="u">
            <xsl:apply-templates/>
          </xsl:element>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="not (contains(@styleCode,'Italics') or contains(@styleCode,'Underline') or contains(@styleCode, 'Bold'))">
      <xsl:apply-templates/>
    </xsl:if>
  </xsl:template>
  <xsl:template match="n1:sup">
    <xsl:element name="sup">
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="n1:sub">
    <xsl:element name="sub">
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>
  <xsl:template name="contactTarget">
    <xsl:for-each select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']">
      <xsl:variable name="telecomContact">
        <xsl:call-template name="getContactInfo">
          <xsl:with-param name="contact" select="n1:associatedEntity"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="n1:associatedEntity[@classCode='GUAR']">
          <tr xmlns="http://www.w3.org/1999/xhtml">
            <th width="10%">
              <xsl:text>Codice Identificativo</xsl:text>
            </th>
            <td width="90%">
              <xsl:call-template name="getConcatID">
                <xsl:with-param name="extension" select="n1:associatedEntity/n1:scopingOrganization/n1:id/@extension"/>
              </xsl:call-template>
            </td>
          </tr>
        </xsl:when>
        <xsl:otherwise>
          <xsl:for-each select="n1:associatedEntity[@classCode='NOK']">
            <tr xmlns="http://www.w3.org/1999/xhtml">
              <th colspan="2">
                <xsl:call-template name="translateCode">
                  <xsl:with-param name="code" select="n1:code/@code"/>
                </xsl:call-template>
              </th>
            </tr>
            <tr xmlns="http://www.w3.org/1999/xhtml">
              <th width="10%">
                <xsl:text>Codice Identificativo</xsl:text>
              </th>
              <td width="90%">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="extension" select="n1:id/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
            <xsl:if test="boolean(n1:associatedPerson)">
              <tr xmlns="http://www.w3.org/1999/xhtml">
                <th width="10%">
                  <xsl:text>Dati anagrafici</xsl:text>
                </th>
                <td width="90%">
                  <xsl:call-template name="getName">
                    <xsl:with-param name="name" select="n1:associatedPerson/n1:name"/>
                  </xsl:call-template>
                  <xsl:if test="not($telecomContact = '')">
                    <br/>
                    <xsl:value-of select="$telecomContact"/>
                  </xsl:if>
                </td>
              </tr>
            </xsl:if>
          </xsl:for-each>
          <xsl:for-each select="n1:associatedEntity[@classCode='ECON']">
            <tr xmlns="http://www.w3.org/1999/xhtml">
              <th colspan="2">
                <xsl:text>Contatto per emergenza</xsl:text>
              </th>
            </tr>
            <tr xmlns="http://www.w3.org/1999/xhtml">
              <th width="10%">
                <xsl:text>Codice Identificativo</xsl:text>
              </th>
              <td width="90%">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="extension" select="n1:id/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
            <xsl:if test="boolean(n1:associatedPerson)">
              <tr xmlns="http://www.w3.org/1999/xhtml">
                <th width="10%">
                  <xsl:text>Dati anagrafici</xsl:text>
                </th>
                <td width="90%">
                  <xsl:call-template name="getName">
                    <xsl:with-param name="name" select="n1:associatedPerson/n1:name"/>
                  </xsl:call-template>
                  <xsl:if test="not($telecomContact = '')">
                    <br/>
                    <xsl:value-of select="$telecomContact"/>
                  </xsl:if>
                </td>
              </tr>
            </xsl:if>
          </xsl:for-each>
          <xsl:for-each select="n1:associatedEntity[@classCode='CAREGIVER']">
            <tr xmlns="http://www.w3.org/1999/xhtml">
              <th colspan="2">
                <xsl:text>Contatto per assistenza</xsl:text>
              </th>
            </tr>
            <tr xmlns="http://www.w3.org/1999/xhtml">
              <th width="10%">
                <xsl:text>Codice Identificativo</xsl:text>
              </th>
              <td width="90%">
                <xsl:call-template name="getConcatID">
                  <xsl:with-param name="extension" select="n1:id/@extension"/>
                </xsl:call-template>
              </td>
            </tr>
            <xsl:if test="boolean(n1:associatedPerson)">
              <tr xmlns="http://www.w3.org/1999/xhtml">
                <th width="10%">
                  <xsl:text>Dati anagrafici</xsl:text>
                </th>
                <td width="90%">
                  <xsl:call-template name="getName">
                    <xsl:with-param name="name" select="n1:associatedPerson/n1:name"/>
                  </xsl:call-template>
                  <xsl:if test="not($telecomContact = '')">
                    <br/>
                    <xsl:value-of select="$telecomContact"/>
                  </xsl:if>
                </td>
              </tr>
            </xsl:if>
          </xsl:for-each>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="subsituteDoctor">
    <xsl:for-each select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='LIC']">
      <xsl:variable name="telecomContact">
        <xsl:call-template name="getContactInfo">
          <xsl:with-param name="contact" select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='LIC']"/>
        </xsl:call-template>
      </xsl:variable>
      <tr xmlns="http://www.w3.org/1999/xhtml">
        <th width="10%">
          <xsl:text>Codice Identificativo</xsl:text>
        </th>
        <td width="90%">
          <xsl:call-template name="getConcatID">
            <xsl:with-param name="extension" select="n1:id/@extension"/>
          </xsl:call-template>
        </td>
      </tr>
      <xsl:if test="boolean(n1:associatedPerson)">
        <tr xmlns="http://www.w3.org/1999/xhtml">
          <th width="10%">
            <xsl:text>Medico sostituito</xsl:text>
          </th>
          <td width="90%">
            <xsl:call-template name="getName">
              <xsl:with-param name="name" select="n1:associatedPerson/n1:name"/>
            </xsl:call-template>
            <xsl:if test="not($telecomContact = '')">
              <br/>
              <xsl:value-of select="$telecomContact"/>
            </xsl:if>
          </td>
        </tr>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="supportDoctor">
    <xsl:for-each select="/n1:ClinicalDocument/n1:informant/n1:assignedEntity">
      <tr xmlns="http://www.w3.org/1999/xhtml">
        <th width="10%">
          <xsl:text>Codice Identificativo</xsl:text>
        </th>
        <td width="90%">
          <xsl:call-template name="getConcatID">
            <xsl:with-param name="extension" select="n1:id/@extension"/>
          </xsl:call-template>
        </td>
      </tr>
      <xsl:if test="boolean(n1:assignedPerson)">
        <tr xmlns="http://www.w3.org/1999/xhtml">
          <th width="10%">
            <xsl:text>Medico suggeritore</xsl:text>
          </th>
          <td width="90%">
            <xsl:call-template name="getName">
              <xsl:with-param name="name" select="n1:assignedPerson/n1:name"/>
            </xsl:call-template>
          </td>
        </tr>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="bottomline">
    <table xmlns="http://www.w3.org/1999/xhtml" cellpadding="0" cellspacing="0" width="100%">
      <caption>Dati autore del documento</caption>
      <xsl:for-each select="/n1:ClinicalDocument/n1:author">
        <tr>
          <th width="20%">
            <xsl:text>Codice Identificativo</xsl:text>
          </th>
          <td style="font-weight: bold;" width="80%">
            <xsl:call-template name="getConcatID">
              <xsl:with-param name="extension" select="n1:assignedAuthor/n1:id/@extension"/>
            </xsl:call-template>
          </td>
        </tr>
        <xsl:choose>
          <xsl:when test="boolean(n1:assignedAuthor/n1:assignedPerson/n1:name)">
            <tr>
              <th width="20%">
                <xsl:text>Dati Anagrafici</xsl:text>
              </th>
              <td width="80%">
                <xsl:call-template name="getName">
                  <xsl:with-param name="name" select="n1:assignedAuthor/n1:assignedPerson/n1:name"/>
                </xsl:call-template>
              </td>
            </tr>
          </xsl:when>
        </xsl:choose>
        <tr>
          <th width="20%">
            <xsl:text>Data di produzione del documento</xsl:text>
          </th>
          <td width="80%">
            <xsl:call-template name="formatDate">
              <xsl:with-param name="date" select="n1:time/@value"/>
            </xsl:call-template>
          </td>
        </tr>
      </xsl:for-each>
    </table>
    <table xmlns="http://www.w3.org/1999/xhtml" cellpadding="0" cellspacing="0" width="100%">
      <caption>Dati firmatario del documento</caption>
      <xsl:for-each select="/n1:ClinicalDocument/n1:legalAuthenticator">
        <tr>
          <th width="20%">
            <xsl:text>Codice Identificativo</xsl:text>
          </th>
          <td style="font-weight: bold;" width="80%">
            <xsl:call-template name="getConcatID">
              <xsl:with-param name="extension" select="n1:assignedEntity/n1:id/@extension"/>
            </xsl:call-template>
          </td>
        </tr>
        <xsl:choose>
          <xsl:when test="boolean(n1:assignedEntity/n1:assignedPerson/n1:name)">
            <tr>
              <th width="20%">
                <xsl:text>Dati Anagrafici</xsl:text>
              </th>
              <td width="80%">
                <xsl:call-template name="getName">
                  <xsl:with-param name="name" select="n1:assignedEntity/n1:assignedPerson/n1:name"/>
                </xsl:call-template>
              </td>
            </tr>
          </xsl:when>
        </xsl:choose>
        <tr>
          <th width="20%">
            <xsl:text>Data di firma del documento</xsl:text>
          </th>
          <td width="80%">
            <xsl:call-template name="formatDate">
              <xsl:with-param name="date" select="n1:time/@value"/>
            </xsl:call-template>
          </td>
        </tr>
      </xsl:for-each>
    </table>
    <xsl:for-each select="/n1:ClinicalDocument/n1:dataEnterer">
      <table xmlns="http://www.w3.org/1999/xhtml" cellpadding="0" cellspacing="0" width="100%">
        <caption>Dati incaricato della registrazione del documento</caption>
        <tr>
          <th width="20%">
            <xsl:text>Codice Identificativo</xsl:text>
          </th>
          <td style="font-weight: bold;" width="80%">
            <xsl:call-template name="getConcatID">
              <xsl:with-param name="extension" select="n1:assignedEntity/n1:id/@extension"/>
            </xsl:call-template>
          </td>
        </tr>
        <xsl:choose>
          <xsl:when test="boolean(n1:assignedEntity/n1:assignedPerson/n1:name)">
            <tr>
              <th width="20%">
                <xsl:text>Dati anagrafici</xsl:text>
              </th>
              <td width="80%">
                <xsl:call-template name="getName">
                  <xsl:with-param name="name" select="n1:assignedEntity/n1:assignedPerson/n1:name"/>
                </xsl:call-template>
              </td>
            </tr>
          </xsl:when>
        </xsl:choose>
        <tr>
          <th width="20%">
            <xsl:text>Data di registrazione del documento</xsl:text>
          </th>
          <td width="80%">
            <xsl:call-template name="formatDate">
              <xsl:with-param name="date" select="n1:time/@value"/>
            </xsl:call-template>
          </td>
        </tr>
      </table>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="setTitleDocument">
    <xsl:param name="titleCDA"/>
    <xsl:choose>
      <xsl:when test="boolean($titleCDA)">
        <xsl:value-of select="$titleCDA"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="$codeDocument='29305-0'">
          <xsl:text>Prescrizione Farmaceutica</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='11488-4'">
          <xsl:text>Prescrizione Specialistica</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='34112-3'">
          <xsl:text>Prescrizione Ricovero</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='29304-3'">
          <xsl:text>Erogazione Farmaceutica</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='34109-9'">
          <xsl:text>Erogazione Specialistica</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='28578-3'">
          <xsl:text>Ceritificato INAIL</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='28653-4'">
          <xsl:text>Ceritificato INPS</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='18748-4'">
          <xsl:text>Referto di Radiologia</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='11502-2'">
          <xsl:text>Referto di Laboratorio</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='22034-3'">
          <xsl:text>Referto di Anatomia Patologica</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='28568-4'">
          <xsl:text>Referto di Pronto Soccorso</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='47039-3'">
          <xsl:text>Accettazione Ospedaliera</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='28574-2'">
          <xsl:text>Scheda di Dimissione Ospedaliera</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='34105-7'">
          <xsl:text>Lettera di Dimissione Ospedaliera</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='28636-9'">
          <xsl:text>Prenotazione di Prestazione Specialistica</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='34878-9'">
          <xsl:text>Patient Summary - Emergency Data Set</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='11506-3'">
          <xsl:text>Annullamento Documento</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='34133-9'">
          <xsl:text>Patient Summary - Scheda Sanitaria Individuale</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='[CODE_CONSENT_DOCTYPE]'">
          <xsl:text>Consenso Generale al trattamento dati sensibili</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='[CODE_RETRACTION_DOCTYPE]'">
          <xsl:text>Revoca Generale al trattamento dati sensibili</xsl:text>
        </xsl:if>
        <xsl:if test="$codeDocument='[CODE_RESTRICTION_DOCTYPE]'">
          <xsl:text>Restrizione del Consenso espresso dall'assistito</xsl:text>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="getName">
    <xsl:param name="name"/>
    <xsl:choose>
      <xsl:when test="$name/n1:family">
        <xsl:if test="$name/n1:prefix">
          <xsl:value-of select="$name/n1:prefix"/>
          <xsl:text/>
        </xsl:if>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$name/n1:given"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$name/n1:family"/>
        <xsl:text/>
        <xsl:if test="$name/n1:suffix">
          <xsl:text>, </xsl:text>
          <xsl:value-of select="$name/n1:suffix"/>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$name"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="priority">
    <xsl:param name="code"/>
    <xsl:choose>
      <xsl:when test="$code='U'">
        <xsl:text>Urgente</xsl:text>
      </xsl:when>
      <xsl:when test="$code='B'">
        <xsl:text>Breve</xsl:text>
      </xsl:when>
      <xsl:when test="$code='D'">
        <xsl:text>Differita</xsl:text>
      </xsl:when>
      <xsl:when test="$code='P'">
        <xsl:text>Programmata</xsl:text>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="titleCode">
    <xsl:param name="code"/>
    <xsl:choose>
      <xsl:when test="$code='SS'">
        <xsl:text>Servizio Sanitario</xsl:text>
      </xsl:when>
      <xsl:when test="$code='AS'">
        <xsl:text>Azienda Sanitaria</xsl:text>
      </xsl:when>
      <xsl:when test="$code='DIP'">
        <xsl:text>Dipartimento</xsl:text>
      </xsl:when>
      <xsl:when test="$code='DIS'">
        <xsl:text>Distretto</xsl:text>
      </xsl:when>
      <xsl:when test="$code='PO'">
        <xsl:text>Presidio Ospedaliero</xsl:text>
      </xsl:when>
      <xsl:when test="$code='UO'">
        <xsl:text>Unita' Operativa</xsl:text>
      </xsl:when>
      <xsl:when test="$code='SP'">
        <xsl:text>Specialita' della struttura</xsl:text>
      </xsl:when>
      <xsl:when test="$code='ADD'">
        <xsl:text>Indirizzo</xsl:text>
      </xsl:when>
      <xsl:when test="$code='CON'">
        <xsl:text>Contatti</xsl:text>
      </xsl:when>
      <xsl:when test="$code='RES'">
        <xsl:text>Responsabile struttura</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>{$code}?</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="formatDate">
    <xsl:param name="date"/>
    <xsl:variable name="month" select="substring ($date, 5, 2)"/>
    <xsl:choose>
      <xsl:when test="substring ($date, 7, 1)=&quot;0&quot;">
        <xsl:value-of select="substring ($date, 8, 1)"/>
        <xsl:text/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="substring ($date, 7, 2)"/>
        <xsl:text/>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:choose>
      <xsl:when test="$month='01'">
        <xsl:text> Gennaio </xsl:text>
      </xsl:when>
      <xsl:when test="$month='02'">
        <xsl:text> Febbraio </xsl:text>
      </xsl:when>
      <xsl:when test="$month='03'">
        <xsl:text> Marzo </xsl:text>
      </xsl:when>
      <xsl:when test="$month='04'">
        <xsl:text> Aprile </xsl:text>
      </xsl:when>
      <xsl:when test="$month='05'">
        <xsl:text> Maggio </xsl:text>
      </xsl:when>
      <xsl:when test="$month='06'">
        <xsl:text> Giugno </xsl:text>
      </xsl:when>
      <xsl:when test="$month='07'">
        <xsl:text> Luglio </xsl:text>
      </xsl:when>
      <xsl:when test="$month='08'">
        <xsl:text> Agosto </xsl:text>
      </xsl:when>
      <xsl:when test="$month='09'">
        <xsl:text> Settembre </xsl:text>
      </xsl:when>
      <xsl:when test="$month='10'">
        <xsl:text> Ottobre </xsl:text>
      </xsl:when>
      <xsl:when test="$month='11'">
        <xsl:text> Novembre </xsl:text>
      </xsl:when>
      <xsl:when test="$month='12'">
        <xsl:text> Dicembre </xsl:text>
      </xsl:when>
    </xsl:choose>
    <xsl:text/>
    <xsl:value-of select="substring ($date, 1, 4)"/>
  </xsl:template>
  <xsl:template name="getConcatID">
    <xsl:param name="root"/>
    <xsl:param name="extension"/>
    <xsl:if test="boolean($root)">
      <xsl:value-of select="$root"/>
      <xsl:text>.</xsl:text>
    </xsl:if>
    <xsl:value-of select="$extension"/>
  </xsl:template>
  <xsl:template name="getContactInfo">
    <xsl:param name="contact"/>
    <xsl:if test="boolean($contact/n1:addr)">
      <xsl:apply-templates select="$contact/n1:addr"/>
    </xsl:if>
    <xsl:if test="boolean($contact/n1:telecom)">
      <xsl:apply-templates select="$contact/n1:telecom"/>
    </xsl:if>
  </xsl:template>
  <xsl:template match="n1:addr">
    <xsl:for-each select="n1:streetAddressLine">
      <xsl:value-of select="."/>
      <xsl:text/>
    </xsl:for-each>
    <xsl:if test="n1:streetName">
      <xsl:value-of select="n1:streetName"/>
      <xsl:text>, </xsl:text>
      <xsl:value-of select="n1:houseNumber"/>
      <xsl:text> - </xsl:text>
    </xsl:if>
    <xsl:if test="n1:postalCode">
      <xsl:text/>
      <xsl:value-of select="n1:postalCode"/>
      <xsl:text> </xsl:text>
    </xsl:if>
    <xsl:if test="n1:city">
      <xsl:value-of select="n1:city"/>
      <xsl:if test="n1:country">
        <xsl:text>, </xsl:text>
      </xsl:if>
    </xsl:if>
    <xsl:if test="n1:country">
      <xsl:value-of select="n1:country"/>
      <xsl:text/>
      <xsl:if test="n1:state">
        <xsl:text>(</xsl:text>
        <xsl:value-of select="n1:state"/>
        <xsl:text>)</xsl:text>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  <xsl:template match="n1:telecom">
    <xsl:variable name="type" select="@xsi:type"/>
    <xsl:variable name="value" select="@value"/>
    <br xmlns="http://www.w3.org/1999/xhtml"/>
    <xsl:if test="$type">
      <xsl:call-template name="translateCode">
        <xsl:with-param name="code" select="$type"/>
      </xsl:call-template>
      <xsl:text/>
      <xsl:text/>
      <xsl:value-of select="$value"/>
      <xsl:if test="@use">
        <xsl:text> (</xsl:text>
        <xsl:call-template name="translateCode">
          <xsl:with-param name="code" select="@use"/>
        </xsl:call-template>
        <xsl:text>)</xsl:text>
      </xsl:if>
      <br xmlns="http://www.w3.org/1999/xhtml"/>
    </xsl:if>
  </xsl:template>
  <xsl:template name="getConfidenzialityLevel">
    <xsl:param name="confidenziality"/>
    <xsl:variable name="code" select="$confidenziality/@code"/>
    <xsl:variable name="codeSystem" select="$confidenziality/@codeSystem"/>
    <xsl:if test="$codeSystem='2.16.840.1.113883.5.25'">
      <xsl:choose>
        <xsl:when test="$code='N'">
          <xsl:text>Normal</xsl:text>
        </xsl:when>
        <xsl:when test="$code='R'">
          <xsl:text>Restricted</xsl:text>
        </xsl:when>
        <xsl:when test="$code='V'">
          <xsl:text>Very Restricted</xsl:text>
        </xsl:when>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
  <xsl:template name="relatedDocumentType">
    <xsl:param name="parentDocumentID"/>
    <xsl:variable name="rootID" select="$parentDocumentID/n1:id/@root"/>
    <xsl:if test="$rootID='2.16.840.1.113883.2.9.4.3.6'">
      <xsl:text>Prescrizione</xsl:text>
    </xsl:if>
    <xsl:if test="$rootID='2.16.840.1.113883.2.9.4.3.4'">
      <xsl:text>Erogazione</xsl:text>
    </xsl:if>
    <xsl:if test="$rootID='2.16.840.1.113883.2.9.4.3.5'">
      <xsl:text>Erogazione</xsl:text>
    </xsl:if>
    <xsl:if test="$rootID='2.16.840.1.113883.2.9.2.200.4.9'">
      <xsl:text>Prenotazione</xsl:text>
    </xsl:if>
    <xsl:if test="$rootID='2.16.840.1.113883.2.9.2.200.4.6'">
      <xsl:text>Accettazione/SDO</xsl:text>
    </xsl:if>
    <xsl:if test="$rootID='2.16.840.1.113883.2.9.2.200.4.4'">
      <xsl:if test="$codeDocument='28578-3'">
        <xsl:text>Ceritificato INAIL</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='28653-4'">
        <xsl:text>Ceritificato INPS</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='18748-4'">
        <xsl:text>Referto Radiologia</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='11502-2'">
        <xsl:text>Referto Laboratorio</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='22034-3'">
        <xsl:text>Referto Anatomia Patologica</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='28568-4'">
        <xsl:text>Referto Pronto Soccorso</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='34105-7'">
        <xsl:text>Lettera Dimissione Ospedaliera</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='34878-9'">
        <xsl:text>Patient Summary - Emergency Data Set</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='34133-9'">
        <xsl:text>Patient Summary - Scheda Sanitaria Individuale</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='[CODE_CONSENT_DOCTYPE]'">
        <xsl:text>Consenso Generale al trattamento dati sensibili</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='[CODE_RETRACTION_DOCTYPE]'">
        <xsl:text>Revoca Generale al trattamento dati sensibili</xsl:text>
      </xsl:if>
      <xsl:if test="$codeDocument='[CODE_RESTRICTION_DOCTYPE]'">
        <xsl:text>Restrizione del Consenso espresso dall'assistito</xsl:text>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  <xsl:template name="translateCode">
    <xsl:param name="code"/>
    <xsl:choose>
      <xsl:when test="$code='TEL'">
        <xsl:text>Tel</xsl:text>
      </xsl:when>
      <xsl:when test="$code='FAX'">
        <xsl:text>Fax</xsl:text>
      </xsl:when>
      <xsl:when test="$code='HP'">
        <xsl:text>Abitazione</xsl:text>
      </xsl:when>
      <xsl:when test="$code='WP'">
        <xsl:text>Ufficio</xsl:text>
      </xsl:when>
      <xsl:when test="$code='RPLC'">
        <xsl:text>Documento sostituito</xsl:text>
      </xsl:when>
      <xsl:when test="$code='APND'">
        <xsl:text>Documento a cui</xsl:text>
        <br xmlns="http://www.w3.org/1999/xhtml"/>
        <xsl:text>restringere accesso</xsl:text>
      </xsl:when>
      <xsl:when test="$code='XFRM'">
        <xsl:text>Documento annullato</xsl:text>
      </xsl:when>
      <xsl:when test="$code='B'">
        <xsl:text>Piccole Biopsie</xsl:text>
      </xsl:when>
      <xsl:when test="$code='S'">
        <xsl:text>Surgical (Grandi Pezzi)</xsl:text>
      </xsl:when>
      <xsl:when test="$code='P'">
        <xsl:text>Pap Test</xsl:text>
      </xsl:when>
      <xsl:when test="$code='C'">
        <xsl:text>Citologico</xsl:text>
      </xsl:when>
      <xsl:when test="$code='9947008'">
        <xsl:text>Padre naturale</xsl:text>
      </xsl:when>
      <xsl:when test="$code='65656005'">
        <xsl:text>Madre naturale</xsl:text>
      </xsl:when>
      <xsl:when test="$code='60614009'">
        <xsl:text>Fratello naturale</xsl:text>
      </xsl:when>
      <xsl:when test="$code='73678001'">
        <xsl:text>Sorella naturale</xsl:text>
      </xsl:when>
      <xsl:when test="$code='160461001'">
        <xsl:text>Nonno paterno</xsl:text>
      </xsl:when>
      <xsl:when test="$code='160463003'">
        <xsl:text>Nonna paterna</xsl:text>
      </xsl:when>
      <xsl:when test="$code='160460000'">
        <xsl:text>Nonno materno</xsl:text>
      </xsl:when>
      <xsl:when test="$code='160462008'">
        <xsl:text>Nonna materna</xsl:text>
      </xsl:when>
      <xsl:when test="$code='160465005'">
        <xsl:text>Zio</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>{$code}?</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="getDisplayName">
    <xsl:param name="displayName"/>
    <xsl:value-of select="$displayName"/>
  </xsl:template>
  <xsl:template name="getOnlyNameValue">
    <xsl:param name="name"/>
    <xsl:value-of select="$name"/>
  </xsl:template>
  <xsl:template match="n1:OwnResponsibilityDocument">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Assunzione di Responsabilità </title>
        <style media="screen" type="text/css">body {  color: #003366;  font-size: 11px;  line-height: normal;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  margin: 10px;  scrollbar-3dlight-color: #cfeafc;  scrollbar-arrow-color: #003366;  scrollbar-darkshadow-color: #cfeafc;  scrollbar-face-color: #cfeafc;  scrollbar-highlight-color: #003366;  scrollbar-shadow-color: #003366;  scrollbar-track-color: #cfeafc }  a {  color: #003366;  text-decoration: none;  }  table {  font-size: 11px;  border: solid 1px;  border-color: #003366;  border-spacing: 0px;  background-color: #cfeafc;  }   caption {  font-size: 11px;  font-color: #003366;  font-weight: bold;  text-transform: capitalize;  text-align: left;  }  th {  border: solid 1px;  border-color:#003366;  text-transform: capitalize;  font-variant: small-caps;  font-size: 10pt;  text-align: left;  vertical-align: middle;  padding-left: 3pt;  padding-right: 3pt;  padding-bottom: 2px;  padding-top: 1px;  white-space: nowrap;  }  td {  border: solid 1px;  border-color:#003366;  text-transform: capitalize;  font-size: 11px;  text-align: left;  vertical-align: middle;  padding-left: 5pt;  padding-right: 5pt;  }  .input {  color: #003366;  font-size: 10px;  font-family: Arial Unicode MS, Verdana, Arial, sans-serif;  background-color: #cfeafc;  border: solid 1px;   }   h1 {  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  font-size: 14pt;  text-transform: capitalize;  font-variant: small-caps;  }  h2 {  font-size: 11px;  }  div {  color: #003366;  font-size: 11px;  line-height: normal;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  text-align: justify;  margin-right: 50px;  }  span {  color: #003366;  font-size: 12px;  line-height: normal;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  text-transform: capitalize;  font-weight: bolder;  }  center {  margin-left: -50px;  margin-top: 25px;  color: #003366;  font-size: 11px;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  font-weight: bolder;  }  li {  margin-left: 20px;  margin-top: 3px;  color: #003366;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  font-size: 11px;  white-space: nowrap;  }  p {  margin-bottom: 6px;  margin-top: 3px;  color: #003366;  font-family: Verdana, Arial Unicode MS, Arial, sans-serif;  font-size: 11px;  text-align: justify;  }</style>
      </head>
      <body>
        <h1>
          <center>Dichirazione di Assunzione di Responsabilità </center>
        </h1>
        <p>Il/La firmatario/a del presente documento<xsl:choose>
            <xsl:when test="n1:MedicalRequestor/n1:name">(<xsl:call-template name="getName">
                <xsl:with-param name="name" select="n1:MedicalRequestor/n1:name"/>
              </xsl:call-template>)</xsl:when>
          </xsl:choose>identificato dal codice identificativo<xsl:call-template name="getConcatID">
            <xsl:with-param name="extension" select="n1:MedicalRequestor/n1:id/@extension"/>
          </xsl:call-template>richiede accesso a documenti appartenenti al paziente identificato dal codice identificativo<xsl:call-template name="getConcatID">
            <xsl:with-param name="extension" select="n1:PatientAssociated/n1:id/@extension"/>
          </xsl:call-template>in deroga alle autorizzazioni necessarie e</p>
        <p>
          <h1>
            <center>dichiara</center>
          </h1>
        </p>
        <p>di assumersi la responsabilità per tutti gli eventi derivanti dalla fruzione dei suddetti documenti:</p>
        <p>
          <ul>
            <li>di aver ricevuto assenso alla fruizione dei documenti dall'assistito identificato</li>
            <li>di avere titolo alla fruizione dei documenti per cui richiede accesso in regime di assunzione di responsabilità </li>
            <li>di sollevare da qualsiasi responsabilità la Regione Sardegna in ambito FSE - MEDIR, derivante dalla fruzione dei documenti a seguito di accettazione della presente richiesta.</li>
            <li>che è a conoscenza che il periodo di fruizione dei documenti appartenenti all'assistito identificato è fissato in 24 ore</li>
          </ul>
        </p>
        <br/>
        <p>Data,<xsl:call-template name="formatDate">
            <xsl:with-param name="date" select="n1:MedicalRequestor/n1:time/@value"/>
          </xsl:call-template>
        </p>
        <p style="text-align:right; margin-right: 75px">In fede</p>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>