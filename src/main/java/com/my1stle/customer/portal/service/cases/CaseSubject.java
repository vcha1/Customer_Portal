package com.my1stle.customer.portal.service.cases;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public final class CaseSubject {

    public static final Map<String, CATEGORY> CATEGORY_MAP= new HashMap<String, CATEGORY>(){{
        put(CATEGORY.OTHER.getViewName(), CATEGORY.OTHER)  ;
        put(CATEGORY.ELECTRICAL.getViewName(), CATEGORY.ELECTRICAL)  ;
        put(CATEGORY.INVERTER.getViewName(), CATEGORY.INVERTER)  ;
        put(CATEGORY.METER.getViewName(), CATEGORY.METER)  ;
        put(CATEGORY.PANELS.getViewName(), CATEGORY.PANELS)  ;
        put(CATEGORY.SYSTEM.getViewName(), CATEGORY.SYSTEM)  ;
        put(CATEGORY.PEST_CONTROL.getViewName(), CATEGORY.PEST_CONTROL)  ;
        put(CATEGORY.ROOF_LEAK.getViewName(), CATEGORY.ROOF_LEAK)  ;
    }};


    public static enum CATEGORY{
        OTHER("Other", "Not sure"),
        ELECTRICAL("Electrical Issue", "Electrical Issue"),
        INVERTER("Inverter Issue", "Inverter Issue"),
        METER("Meter Issue", "Meter Issue"),
        PANELS("Panel Issue", "Panel Issue"),
        SYSTEM("System Check" ,"System Check"),
        PEST_CONTROL("Guards", "Pest Control"),
        ROOF_LEAK("Roof Leak", "Roof Leak", "Repair: Roof Leak");

        private String idName;
        private String viewName;
        private String problemType;

        public String getViewName(){
            return this.viewName;
        }

        public String getIdName(){
            return this.idName;
        }

        public String getProblemType() {
            return this.problemType;
        }

        CATEGORY(String id, String viewName){
            this(id, viewName, null);
        }

        CATEGORY(String id, String viewName, String problemType){
           this.idName = id;
           this.viewName = viewName;
           this.problemType = problemType;
        }
    }

    public static final Map<String, SUBJECT> SUBJECT_MAP= new HashMap<String, SUBJECT>(){{

        put(SUBJECT.BREAKER.getViewName(), SUBJECT.BREAKER);
        put(SUBJECT.WIRING.getViewName(), SUBJECT.WIRING);
        put(SUBJECT.BROKEN_PANELS.getViewName(), SUBJECT.BROKEN_PANELS);
        put(SUBJECT.PANEL_CLEANING.getViewName(), SUBJECT.PANEL_CLEANING);
        put(SUBJECT.ANIMAL.getViewName(), SUBJECT.ANIMAL);
        put(SUBJECT.SNOW.getViewName(), SUBJECT.SNOW);
    }};
    public static enum SUBJECT{

        BREAKER("Breaker",
                CATEGORY.ELECTRICAL,
                "Breaker"),

        WIRING("Wiring Issue",
                CATEGORY.ELECTRICAL,
                "Wiring Issue"),

        BROKEN_PANELS("Broken",
                CATEGORY.PANELS,
                "Broken Panels"),
        PANEL_CLEANING("Panel Cleaning",
                CATEGORY.PANELS,
                "Panel Cleaning"),
        ANIMAL("Animal",
                CATEGORY.PEST_CONTROL,
                "Pest Issue"
        ),
        SNOW("Snow",
               CATEGORY.PEST_CONTROL,
                "Snow Issue"
        );



        private String idName;
        private CATEGORY category;
        private String viewName;

        public String getIdName(){
            return this.idName;
        }

        public CATEGORY getCategory(){
            return this.category;
        }

        public String getViewName(){
            return this.viewName;
        }
        SUBJECT(String idName, CATEGORY category, String viewName){
           this.idName = idName;
           this.category = category;
           this.viewName = viewName;
        }
    }
}
