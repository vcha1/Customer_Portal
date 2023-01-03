package com.my1stle.customer.portal.serviceImpl.adobesign;

import com.adobe.sign.model.agreements.UserAgreement;
import com.my1stle.customer.portal.persistence.model.InstallationSalesforceObject;
import com.my1stle.customer.portal.service.adobe.sign.client.AgreementsClient;
import com.my1stle.customer.portal.service.installation.detail.model.ContractDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultAdobeSign {
    private final AgreementsClient agreementsClient;

    @Autowired
    public DefaultAdobeSign(AgreementsClient agreementsClient) {
        this.agreementsClient = agreementsClient;
    }

    public List<ContractDetail> getAgreementForInstallation(String email, String firstName, String lastName) {
        //get agreement id, utility id, and financing id, also checks to make sure that it is signed from salesforce
        //System.out.println("In Adobe Sign");
        Map<String, String> contractLabelToEsignIdMap = new HashMap<>();
        String fullName = firstName + ' ' + lastName;
        String[] emailList = new String[] {"email:itinfo@1stlightenergy.com","email:WestCoastSales@1stlightenergy.com", "email:EastCoastSales@1stlightenergy.com",
                                        "email:recruiters@1stlightenergy.com", "email:CommercialSolar@1stlightenergy.com"};
        //System.out.println(emailList.length);
        List<UserAgreement> agreement = new ArrayList<>();
        for (int i = 0; i<emailList.length; i++){
            //System.out.println(emailList[i]);
            //agreement = agreementsClient.getAgreement("solarwithme@gmail.com", emailList[i]);
            //agreement = agreementsClient.getAgreement("dkrause@tpensions.com", emailList[i]);
            agreement = agreementsClient.getAgreement(email, emailList[i]);
            //System.out.println(agreement);
            if (agreement != null){
                break;
            }
        }

        //fullName = "crystal krause";
        //fullName = "Mark Curran";

        for (int i = 0; i < agreement.size(); i++) {
            //System.out.println(agreement.get(i).getName());
            //System.out.println(agreement.get(i).getDisplayUserSetInfos().get(0).getDisplayUserSetMemberInfos().get(0).getFullName());
            String firstLight = "1st Light";
            String firstLightSecond = "First Light";
            String utility = "utility";
            String financing = "financing";


            if (agreement.get(i).getName().toLowerCase().contains(firstLight.toLowerCase())) {
                //System.out.println(agreement.get(i).getAgreementId());
                if (agreement.get(i).getName().toLowerCase().contains(fullName.toLowerCase())) {
                    contractLabelToEsignIdMap.put("1st Light", agreement.get(i).getAgreementId());
                }
            }else if (agreement.get(i).getName().toLowerCase().contains(firstLightSecond.toLowerCase())){
                if (agreement.get(i).getName().toLowerCase().contains(fullName.toLowerCase())) {
                    contractLabelToEsignIdMap.put("1st Light", agreement.get(i).getAgreementId());
                }
            }
            if (agreement.get(i).getName().toLowerCase().contains(utility.toLowerCase())) {
                if (agreement.get(i).getName().toLowerCase().contains(fullName.toLowerCase())) {
                    contractLabelToEsignIdMap.put("utility", agreement.get(i).getAgreementId());
                }
            }
            if (agreement.get(i).getName().toLowerCase().contains(financing.toLowerCase())){
                if (agreement.get(i).getName().toLowerCase().contains(fullName.toLowerCase())) {
                    contractLabelToEsignIdMap.put("financing", agreement.get(i).getAgreementId());
                }
            }
        }
        //System.out.println("Mid");
        if (contractLabelToEsignIdMap.isEmpty()){
            for (int i = 0; i < agreement.size(); i++) {
                //System.out.println(agreement.get(i).getName());
                //System.out.println(agreement.get(i).getDisplayUserSetInfos().get(0).getDisplayUserSetMemberInfos().get(0).getFullName());
                String proposalAgreement = "Proposal Agreement";
                if (agreement.get(i).getName().toLowerCase().contains(proposalAgreement.toLowerCase())) {
                    if (agreement.get(i).getName().toLowerCase().contains(fullName.toLowerCase())) {
                        contractLabelToEsignIdMap.put("1st Light", agreement.get(i).getAgreementId());
                        contractLabelToEsignIdMap.put("utility", agreement.get(i).getAgreementId());
                        contractLabelToEsignIdMap.put("financing", agreement.get(i).getAgreementId());
                        break;
                    }
                } else if (agreement.get(i).getName().toLowerCase().contains("Testing Doc Sending".toLowerCase())){
                    contractLabelToEsignIdMap.put("1st Light", agreement.get(i).getAgreementId());
                    contractLabelToEsignIdMap.put("utility", agreement.get(i).getAgreementId());
                    contractLabelToEsignIdMap.put("financing", agreement.get(i).getAgreementId());
                    break;
                }
            }
        }

        //System.out.println(contractLabelToEsignIdMap.size());

        List<ContractDetail> contractDetailsTwo = new ArrayList<>();

        for(Map.Entry<String, String> labelToEsignIdEntry : contractLabelToEsignIdMap.entrySet()) {
            String agreementLabel = labelToEsignIdEntry.getKey();
            String agreementId = labelToEsignIdEntry.getValue();

            String url = agreementsClient.getAgreementUrl(agreementId);

            if(url != null)
                contractDetailsTwo.add(new ContractDetail(agreementLabel, url));
        }
        /*
        System.out.println(contractDetailsTwo);
        if(contractDetailsTwo.size() > 0) {
            for (int i = 0; i < contractDetailsTwo.size(); i++) {
                System.out.println(contractDetailsTwo.get(i).getLink());
                System.out.println(contractDetailsTwo.get(i).getLabel());
            }
        }
        */

        return contractDetailsTwo;
    }
}
