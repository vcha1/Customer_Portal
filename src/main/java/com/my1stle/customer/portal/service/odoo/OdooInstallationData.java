package com.my1stle.customer.portal.service.odoo;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OdooInstallationData {
    private Object id;
    private String name;
    private String accountId;
    private String contactId;
    private String customerName;
    private String customerEmailAddress;
    private String customerPhoneNumber;
    private Double systemSize;
    private String installStatus;
    private String operationalArea;
    private String address;
    private String state;
    private String city;
    private String zipCode;
    private String designLink;
    private LocalDate contractSignedDate;
    private DateTime psaScheduledDate;
    private LocalDate psaCompleteDate;
    private LocalDate scheduledStartDate;
    private Integer installCompletePendingPto;
    private LocalDate installationDate;
    private LocalDate stopPackageSentDate;
    private String opportunityId;
    private String soldProposalId;
    private String soldProposalPredesignId;
    private String paymentTypeName;
    private String paymentProviderName;
    private String productTypeName;
    private Double totalContractPrice;
    private String soldProposalSrecTypeName;
    private Double soldProposalSrecUpfrontRebate;
    private Double soldProposalSrecSellPrice;
    private Double soldProposalSrecSellDeEscalator;
    private Double soldProposalStateTaxCredit;
    private Double soldProposalFederalTaxCredit;
    private String directSalesCloserName;
    private String directSalesCloserPhone;
    private String directSalesCloserEmail;
    private String accountManagerName;
    private String accountManagerPreformattedContactInfo;
    private String firstLightAgreementId;
    private String firstLightAgreementStatus;
    private String utilityAgreementId;
    private String utilityAgreementStatus;
    private String financingAgreementId;
    private String financingAgreementStatus;
    private String monitoringExternalId;
    private Integer panelCountFromItems;





    private List<Object> idList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> accountIdList = new ArrayList<>();
    private List<String> contactIdList = new ArrayList<>();
    private List<String> customerNameList = new ArrayList<>();
    private List<String> customerEmailAddressList = new ArrayList<>();
    private List<String> customerPhoneNumberList = new ArrayList<>();
    private List<Double> systemSizeList = new ArrayList<>();
    private List<String> installStatusList = new ArrayList<>();
    private List<String> operationalAreaList = new ArrayList<>();
    private List<String> addressList = new ArrayList<>();
    private List<String> stateList = new ArrayList<>();
    private List<String> cityList = new ArrayList<>();
    private List<String> zipCodeList = new ArrayList<>();
    private List<String> designLinkList = new ArrayList<>();
    private List<LocalDate> contractSignedDateList = new ArrayList<>();
    private List<DateTime> psaScheduledDateList = new ArrayList<>();
    private List<LocalDate> psaCompleteDateList = new ArrayList<>();
    private List<LocalDate> scheduledStartDateList = new ArrayList<>();
    private List<Integer> installCompletePendingPtoList = new ArrayList<>();
    private List<LocalDate> installationDateList = new ArrayList<>();
    private List<LocalDate> stopPackageSentDateList = new ArrayList<>();
    private List<String> opportunityIdList = new ArrayList<>();
    private List<String> soldProposalIdList = new ArrayList<>();
    private List<String> soldProposalPredesignIdList = new ArrayList<>();
    private List<String> paymentTypeNameListList = new ArrayList<>();
    private List<String> paymentProviderNameList = new ArrayList<>();
    private List<String> productTypeNameList = new ArrayList<>();
    private List<Double> totalContractPriceList = new ArrayList<>();
    private List<String> soldProposalSrecTypeNameList = new ArrayList<>();
    private List<Double> soldProposalSrecUpfrontRebateList = new ArrayList<>();
    private List<Double> soldProposalSrecSellPriceList = new ArrayList<>();
    private List<Double> soldProposalSrecSellDeEscalatorList = new ArrayList<>();
    private List<Double> soldProposalStateTaxCreditList = new ArrayList<>();
    private List<Double> soldProposalFederalTaxCreditList = new ArrayList<>();
    private List<String> directSalesCloserNameList = new ArrayList<>();
    private List<String> directSalesCloserPhoneList = new ArrayList<>();
    private List<String> directSalesCloserEmailList = new ArrayList<>();
    private List<String> accountManagerNameList = new ArrayList<>();
    private List<String> accountManagerPreformattedContactInfoList = new ArrayList<>();
    private List<String> firstLightAgreementIdList = new ArrayList<>();
    private List<String> firstLightAgreementStatusList = new ArrayList<>();
    private List<String> utilityAgreementIdList = new ArrayList<>();
    private List<String> utilityAgreementStatusList = new ArrayList<>();
    private List<String> financingAgreementIdList = new ArrayList<>();
    private List<String> financingAgreementStatusList = new ArrayList<>();
    private List<String> monitoringExternalIdList = new ArrayList<>();
    private List<Integer> panelCountFromItemsList = new ArrayList<>();


    public OdooInstallationData(String email) {
        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "project.task";
        List<String> fields = Arrays.asList("name",  "x_studio_contract_type_3");
        List<Object> criteria = Arrays.asList(Arrays.asList("x_studio_email_3", "=",email));

        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
        try {
            for (int i = 0; i < results.size(); i++) {
                this.idList.add(results.get(i).get("id"));
                this.nameList.add(odooConnection.findObjects(objectType, Arrays.asList("name"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("name").toString());
                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_customers_name1"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_customers_name1").toString() != "false") {
                    this.customerNameList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_customers_name1"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_customers_name1").toString());
                } else {
                    this.customerNameList.add(null);
                }
                this.customerEmailAddressList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_email_3"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_email_3").toString());
                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_phone_4"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_phone_4").toString() != "false") {
                    this.customerPhoneNumberList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_phone_4"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_phone_4").toString());
                } else {
                    this.customerPhoneNumberList.add(null);
                }
                this.systemSizeList.add(Double.valueOf(odooConnection.findObjects(objectType, Arrays.asList("x_studio_contract_system_size_kw_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_contract_system_size_kw_1").toString()));
                this.installStatusList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_field_ejWNt"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_field_ejWNt").toString());
                this.addressList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_street"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_site_street").toString());
                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_state_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_site_state_1").toString() != "false") {
                    this.stateList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_state_1"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_site_state_1").toString());
                } else {
                    this.stateList.add(null);
                }
                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_city_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_site_city_1").toString() != "false") {
                    this.cityList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_city_1"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_site_city_1").toString());
                } else {
                    this.cityList.add(null);
                }
                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_zip_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_site_zip_1").toString() != "false") {
                    this.zipCodeList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_zip_1"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_site_zip_1").toString());
                } else {
                    this.zipCodeList.add(null);
                }
                this.designLinkList.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_drop_box_url_for_design"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_drop_box_url_for_design").toString());
                this.totalContractPriceList.add(Double.valueOf(odooConnection.findObjects(objectType, Arrays.asList("x_studio_total_contract_cost_2"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_total_contract_cost_2").toString()));

                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_agreement_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_agreement_date").toString() != "false") {
                    this.contractSignedDateList.add(LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_agreement_date"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_agreement_date").toString()));
                }
                DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_scheduled_date_time"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_psa_scheduled_date_time").toString() != "false") {
                    this.psaScheduledDateList.add(dtf.parseDateTime(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_scheduled_date_time"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_psa_scheduled_date_time").toString()));
                }

                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_performed"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_psa_performed").toString() != "false") {
                    this.psaCompleteDateList.add(LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_performed"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_psa_performed").toString()));
                }

                if (odooConnection.findObjects(objectType, Arrays.asList("estimated_start_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("estimated_start_date").toString() != "false") {
                    this.scheduledStartDateList.add(LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("estimated_start_date"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("estimated_start_date").toString()));
                }

                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_dhis_install_complete_pending_pto"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_dhis_install_complete_pending_pto").toString() != "false") {

                    this.installCompletePendingPtoList.add(Integer.parseInt(odooConnection.findObjects(objectType, Arrays.asList("x_studio_dhis_install_complete_pending_pto"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_dhis_install_complete_pending_pto").toString()));
                }

                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_installation_completed_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_installation_completed_date").toString() != "false") {
                    this.installationDateList.add(LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_installation_completed_date"),
                            Arrays.asList(Arrays.asList("id", "=", this.idList.get(i)))).get(0).get("x_studio_installation_completed_date").toString()));
                }
            }
        } catch (NumberFormatException e) {

        }

    }

    public OdooInstallationData(String id, String objectType) {
        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        List<String> fields = Arrays.asList("name");
        List<Object> criteria = Arrays.asList(Arrays.asList("id", "=", id));

        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);

        try {
            this.id = results.get(0).get("id");
            this.name = odooConnection.findObjects(objectType, Arrays.asList("name"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("name").toString();
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_customers_name1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_customers_name1").toString() != "false" ) {
                this.customerName = odooConnection.findObjects(objectType, Arrays.asList("x_studio_customers_name1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_customers_name1").toString();
            }else{
                this.customerName = null;
            }
            this.customerEmailAddress = odooConnection.findObjects(objectType, Arrays.asList("x_studio_email_3"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_email_3").toString();
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_phone_4"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_phone_4").toString() != "false" ) {
                this.customerPhoneNumber = odooConnection.findObjects(objectType, Arrays.asList("x_studio_phone_4"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_phone_4").toString();
            }else{
                this.customerPhoneNumber = null;
            }
            this.systemSize = Double.valueOf(odooConnection.findObjects(objectType, Arrays.asList("x_studio_contract_system_size_kw_1"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_contract_system_size_kw_1").toString());
            this.installStatus = odooConnection.findObjects(objectType, Arrays.asList("x_studio_field_ejWNt"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_field_ejWNt").toString();
            this.address = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_street"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_site_street").toString();
            if ( odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_state_1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_state_1").toString() != "false") {
                this.state = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_state_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_state_1").toString();
            } else {
                this.state = null;
            }
            if(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_city_1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_city_1").toString() != "false") {
                this.city = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_city_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_city_1").toString();
            } else {
                this.city = null;
            }
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_zip_1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_zip_1").toString() != "false") {
                this.zipCode = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_zip_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_zip_1").toString();
            } else {
                this.zipCode = null;
            }
            this.designLink = odooConnection.findObjects(objectType, Arrays.asList("x_studio_drop_box_url_for_design"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_drop_box_url_for_design").toString();
            this.totalContractPrice = Double.valueOf(odooConnection.findObjects(objectType, Arrays.asList("x_studio_total_contract_cost_2"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_total_contract_cost_2").toString());

            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_agreement_date"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_agreement_date").toString() != "false") {
                this.contractSignedDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_agreement_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_agreement_date").toString());
            }

            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_scheduled_date_time"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_scheduled_date_time").toString() != "false") {
                this.psaScheduledDate = dtf.parseDateTime(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_scheduled_date_time"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_scheduled_date_time").toString());
            }

            if(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_performed"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_performed").toString() != "false") {
                this.psaCompleteDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_performed"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_performed").toString());
            }

            if(odooConnection.findObjects(objectType, Arrays.asList("estimated_start_date"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("estimated_start_date").toString() != "false") {
                this.scheduledStartDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("estimated_start_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("estimated_start_date").toString());
            }

            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_dhis_install_complete_pending_pto"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_dhis_install_complete_pending_pto").toString() != "false") {

                this.installCompletePendingPto = Integer.parseInt(odooConnection.findObjects(objectType, Arrays.asList("x_studio_dhis_install_complete_pending_pto"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_dhis_install_complete_pending_pto").toString());
            }

            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_installation_completed_date"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_installation_completed_date").toString() != "false") {
                this.installationDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_installation_completed_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_installation_completed_date").toString());
            }
        } catch (NumberFormatException e) {

        }

    }



    public OdooInstallationData(String userEmail, String id, String details) {
        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "project.task";
        List<String> fields = Arrays.asList("name");
        List<Object> criteria = Arrays.asList(Arrays.asList("id", "=", id));

        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);

        try {
            this.id = results.get(0).get("id");
            this.name = odooConnection.findObjects(objectType, Arrays.asList("name"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("name").toString();
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_customers_name1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_customers_name1").toString() != "false" ) {
                Object[] customerNameObj =(Object[]) odooConnection.findObjects(objectType, Arrays.asList("x_studio_customers_name1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_customers_name1");
                this.customerName = customerNameObj[1].toString();
            }else{
                this.customerName = null;
            }
            this.customerEmailAddress = odooConnection.findObjects(objectType, Arrays.asList("x_studio_email_3"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_email_3").toString();
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_phone_4"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_phone_4").toString() != "false" ) {
                this.customerPhoneNumber = odooConnection.findObjects(objectType, Arrays.asList("x_studio_phone_4"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_phone_4").toString();
            }else{
                this.customerPhoneNumber = null;
            }
            this.systemSize = Double.valueOf(odooConnection.findObjects(objectType, Arrays.asList("x_studio_contract_system_size_kw_1"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_contract_system_size_kw_1").toString());
            this.installStatus = odooConnection.findObjects(objectType, Arrays.asList("x_studio_field_ejWNt"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_field_ejWNt").toString();
            this.address = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_street"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_site_street").toString();
            if ( odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_state_1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_state_1").toString() != "false") {
                this.state = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_state_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_state_1").toString();
            } else {
                this.state = null;
            }
            if(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_city_1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_city_1").toString() != "false") {
                this.city = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_city_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_city_1").toString();
            } else {
                this.city = null;
            }
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_zip_1"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_zip_1").toString() != "false") {
                this.zipCode = odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_zip_1"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_site_zip_1").toString();
            } else {
                this.zipCode = null;
            }
            this.designLink = odooConnection.findObjects(objectType, Arrays.asList("x_studio_drop_box_url_for_design"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_drop_box_url_for_design").toString();
            this.totalContractPrice = Double.valueOf(odooConnection.findObjects(objectType, Arrays.asList("x_studio_total_contract_cost_2"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("x_studio_total_contract_cost_2").toString());

            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_agreement_date"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_agreement_date").toString() != "false") {
                this.contractSignedDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_agreement_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_agreement_date").toString());
            }

            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_scheduled_date_time"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_scheduled_date_time").toString() != "false") {
                this.psaScheduledDate = dtf.parseDateTime(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_scheduled_date_time"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_scheduled_date_time").toString());
            }

            if(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_performed"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_performed").toString() != "false") {
                this.psaCompleteDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_psa_performed"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_psa_performed").toString());
            }

            if(odooConnection.findObjects(objectType, Arrays.asList("estimated_start_date"),
                    Arrays.asList(Arrays.asList("id", "=",this.id))).get(0).get("estimated_start_date").toString() != "false") {
                this.scheduledStartDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("estimated_start_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("estimated_start_date").toString());
            }

            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_dhis_install_complete_pending_pto"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_dhis_install_complete_pending_pto").toString() != "false") {

                this.installCompletePendingPto = Integer.parseInt(odooConnection.findObjects(objectType, Arrays.asList("x_studio_dhis_install_complete_pending_pto"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_dhis_install_complete_pending_pto").toString());
            }

            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_installation_completed_date"),
                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_installation_completed_date").toString() != "false") {
                this.installationDate = LocalDate.parse(odooConnection.findObjects(objectType, Arrays.asList("x_studio_installation_completed_date"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(0).get("x_studio_installation_completed_date").toString());
            }
        } catch (NumberFormatException e) {

        }

    }

    public Object getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return this.customerEmailAddress;
    }

    public String getCustomerPhoneNumber() {
        return this.customerPhoneNumber;
    }

    public Double getSystemSize() {
        return systemSize;
    }

    public String getInstallStatus() {
        return installStatus;
    }

    public String getOperationalArea() {
        return this.operationalArea;
    }

    public String getAddress() {
        return this.address;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getDesignLink() {
        return designLink;
        //return "https://www.dropbox.com/sh/19uqctmlwa7qqzn/AACZ76EWHdxS2b7W0frf_I78a?dl=0";
    }

    public LocalDate getContractSignedDate() {
        return contractSignedDate;
    }

    public DateTime getPsaScheduledDate() {
        return psaScheduledDate;
    }

    public LocalDate getPsaCompleteDate() {
        return psaCompleteDate;
    }

    public LocalDate getScheduledStartDate() {
        return scheduledStartDate;
    }

    public Integer getInstallCompletePendingPto() {
        return installCompletePendingPto;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public LocalDate getStopPackageSentDate() {
        return stopPackageSentDate;
    }

    public String getSoldProposalId() {
        return soldProposalId;
    }

    public String getSoldProposalPredesignId() {
        return soldProposalPredesignId;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public String getPaymentProviderName() {
        return paymentProviderName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public String getDirectSalesCloserName() {
        return directSalesCloserName;
    }

    public String getDirectSalesCloserPhone() {
        return directSalesCloserPhone;
    }

    public String getDirectSalesCloserEmail() {
        return directSalesCloserEmail;
    }

    public String getAccountManagerName() {
        return accountManagerName;
    }

    public String getAccountManagerPreformattedContactInfo() {
        return accountManagerPreformattedContactInfo;
    }

    public Double getTotalContractPrice() {
        return totalContractPrice;
    }

    public String getSoldProposalSrecTypeName() {
        return soldProposalSrecTypeName;
    }

    public Double getSoldProposalSrecUpfrontRebate() {
        return soldProposalSrecUpfrontRebate;
    }

    public Double getSoldProposalSrecSellPrice() {
        return soldProposalSrecSellPrice;
    }

    public Double getSoldProposalSrecSellDeEscalator() {
        return soldProposalSrecSellDeEscalator;
    }

    public Double getSoldProposalStateTaxCredit() {
        return soldProposalStateTaxCredit;
    }

    public Double getSoldProposalFederalTaxCredit() {
        return soldProposalFederalTaxCredit;
    }

    public String getFirstLightAgreementId() {
        return firstLightAgreementId;
    }

    public String getFirstLightAgreementStatus() {
        return firstLightAgreementStatus;
    }

    public String getUtilityAgreementId() {
        return utilityAgreementId;
    }

    public String getUtilityAgreementStatus() {
        return utilityAgreementStatus;
    }

    public String getFinancingAgreementId() {
        return financingAgreementId;
    }

    public String getFinancingAgreementStatus() {
        return financingAgreementStatus;
    }

    public String getMonitoringExternalId() {
        return monitoringExternalId;
    }






    public List<Object> getIdList() {
        return idList;
    }
    public List<String> getNameList() {
        return nameList;
    }
    public List<String> getAccountIdList() {
        return accountIdList;
    }
    public List<String> getContactIdList() {
        return contactIdList;
    }
    public List<String> getCustomerNameList() {
        return customerNameList;
    }
    public List<String> getCustomerEmailAddressList() {
        return customerEmailAddressList;
    }
    public List<String> getCustomerPhoneNumberList() {
        return customerPhoneNumberList;
    }
    public List<Double> getSystemSizeList() {
        return systemSizeList;
    }
    public List<String> getInstallStatusList() {
        return installStatusList;
    }
    public List<String> getOperationalAreaList() {
        return operationalAreaList;
    }
    public List<String> getAddressList() {
        return addressList;
    }
    public List<String> getStateList() {
        return stateList;
    }
    public List<String> getCityList() {
        return cityList;
    }
    public List<String> getZipCodeList() {
        return  zipCodeList;
    }
    public List<String> getDesignLinkList() {
        return designLinkList;
    }
    public List<LocalDate> getContractSignedDateList() {
        return contractSignedDateList;
    }
    public List<DateTime> getPsaScheduledDateList() {
        return psaScheduledDateList;
    }
    public List<LocalDate> getPsaCompleteDateList() {
        return psaCompleteDateList;
    }
    public List<LocalDate> getScheduledStartDateList() {
        return scheduledStartDateList;
    }
    public List<Integer> getInstallCompletePendingPtoList() {
        return installCompletePendingPtoList;
    }
    public List<LocalDate> getInstallationDateList() {
        return installationDateList;
    }
    public List<LocalDate> getStopPackageSentDateList() {
        return  stopPackageSentDateList;
    }
    public List<String> getOpportunityIdList() {
        return opportunityIdList;
    }
    public List<String> getSoldProposalIdList() {
        return soldProposalIdList;
    }
    public List<String> getSoldProposalPredesignIdList() {
        return soldProposalPredesignIdList;
    }
    public List<String> getPaymentTypeNameListList() {
        return paymentTypeNameListList;
    }
    public List<String> getPaymentProviderNameList() {
        return paymentProviderNameList;
    }
    public List<String> getProductTypeNameList() {
        return productTypeNameList;
    }
    public List<Double> getTotalContractPriceList() {
        return totalContractPriceList;
    }
    public List<String> getSoldProposalSrecTypeNameList() {
        return soldProposalSrecTypeNameList;
    }
    public List<Double> getSoldProposalSrecUpfrontRebateList() {
        return soldProposalSrecUpfrontRebateList;
    }
    public List<Double> getSoldProposalSrecSellPriceList() {
        return soldProposalSrecSellPriceList;
    }
    public List<Double> getSoldProposalSrecSellDeEscalatorList() {
        return soldProposalSrecSellDeEscalatorList;
    }
    public List<Double> getSoldProposalStateTaxCreditList() {
        return soldProposalStateTaxCreditList;
    }
    public List<Double> getSoldProposalFederalTaxCreditList() {
        return soldProposalFederalTaxCreditList;
    }
    public List<String> getDirectSalesCloserNameList() {
        return directSalesCloserNameList;
    }
    public List<String> getDirectSalesCloserPhoneList() {
        return directSalesCloserPhoneList;
    }
    public List<String> getDirectSalesCloserEmailList() {
        return directSalesCloserEmailList;
    }
    public List<String> getAccountManagerNameList() {
        return accountManagerNameList;
    }
    public List<String> getAccountManagerPreformattedContactInfoList() {
        return accountManagerPreformattedContactInfoList;
    }
    public List<String> getFirstLightAgreementIdList() {
        return firstLightAgreementIdList;
    }
    public List<String> getFirstLightAgreementStatusList() {
        return firstLightAgreementStatusList;
    }
    public List<String> getUtilityAgreementIdList() {
        return utilityAgreementIdList;
    }
    public List<String> getUtilityAgreementStatusList() {
        return utilityAgreementStatusList;
    }
    public List<String> getFinancingAgreementIdList() {
        return financingAgreementIdList;
    }
    public List<String> getFinancingAgreementStatusList() {
        return financingAgreementStatusList;
    }
    public List<String> getMonitoringExternalIdList() {
        return monitoringExternalIdList;
    }
    public List<Integer> getPanelCountFromItemsList() {
        return panelCountFromItemsList;
    }



}
