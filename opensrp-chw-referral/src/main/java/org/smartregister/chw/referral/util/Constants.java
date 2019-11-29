package org.smartregister.chw.referral.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";
    String IGNORE = "ignore";
    String IS_PROBLEM = "is_problem";
    String FOCUS = "focus";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
    }

    interface EVENT_TYPE {
        String REGISTRATION = "Referral Registration";
        String REFERRAL_FOLLOW_UP_VISIT = "Followup Visit";
    }

    interface FORMS {
        String REFERRAL_REGISTRATION = "general_referral_form";
        String REFERRAL_FOLLOW_UP_VISIT = "referral_followup_visit";
    }

    interface TABLES {
        String REFERRAL = "ec_referral";
        String REFERRAL_SERVICE = "ec_referral_service";
        String REFERRA_SERVICE_INDICATOR = "ec_referral_service_indicator";
        String REFERRAL_FOLLOW_UP = "ec_referral_follow_up_visit";
        String FAMILY_MEMBER = "ec_family_member";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String MEMBER_OBJECT = "MEMBER_OBJECT";
        String ACTION = "ACTION";
        String REFERRAL_FORM_NAME = "REFERRAL_FORM_NAME";
        String REFERRAL_FOLLOWUP_FORM_NAME = "REFERRAL_FOLLOWUP_FORM_NAME";
        String REFERRAL_SERVICE_IDS = "REFERRAL_SERVICE_IDS";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String ISSUE_REFERRAL = "issue_referral";
    }

    interface REFERRAL_MEMBER_OBJECT {
        String MEMBER_OBJECT = "memberObject";
    }

    interface REFERRAL_STATUS {
        String PENDING = "PENDING";
        String SUCCESSFUL = "SUCCESSFUL";
        String FAILED = "FAILED";
    }


    interface REFERRAL_TYPE {
        String COMMUNITY_TO_FACILITY_REFERRAL = "community_to_facility_referral";
        String FACILITY_TO_COMMUNITY_REFERRAL = "facility_to_community_referral";
    }

    interface REFERRAL_TASK {

        String REFERRAL_PLAN_ID = "5270285b-5a3b-4647-b772-c0b3c52e2b71";
        String REFERRAL_CODE = "Referral";

        interface BUSINESS_STATUS {
            String REFERRED = "Referred";
            String IN_PROGRESS = "In-Progress";
            String COMPLETE = "Complete";
            String EXPIRED = "Expired";
            String FAILED = "Failed";
        }

        interface TASKS_FOCUS {
            String SICK_CHILD = "Sick Child";
            String ANC_DANGER_SIGNS = "ANC Danger Signs";
            String PNC_DANGER_SIGNS = "PNC Danger Signs";
        }
    }
}
