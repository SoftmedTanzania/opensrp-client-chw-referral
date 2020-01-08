package org.smartregister.chw.referral;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.ReferralMetadata;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.chw.referral.repository.FollowupFeedbackRepository;
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository;
import org.smartregister.chw.referral.repository.ReferralServiceRepository;
import org.smartregister.domain.Location;
import org.smartregister.domain.LocationProperty;
import org.smartregister.repository.LocationRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.TaskNotesRepository;
import org.smartregister.repository.TaskRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.AssetHandler;

import id.zelory.compressor.Compressor;
import timber.log.Timber;

public class ReferralLibrary {
    private static ReferralLibrary instance;

    private final Context context;
    private final Repository repository;

    private int applicationVersion;
    private int databaseVersion;

    private ECSyncHelper syncHelper;

    private ClientProcessorForJava clientProcessorForJava;
    private Compressor compressor;
    private TaskRepository taskRepository;
    private ReferralMetadata referralMetadata;

    private ReferralLibrary(Context contextArg, Repository repositoryArg, ReferralMetadata referralMetadata,  int applicationVersion, int databaseVersion) {
        this.context = contextArg;
        this.repository = repositoryArg;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
        this.referralMetadata = referralMetadata;
    }

    public static void init(Context context, Repository repository,  ReferralMetadata referralMetadata, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ReferralLibrary(context, repository, referralMetadata, applicationVersion, databaseVersion);
        }
    }

    public static ReferralLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call "
                    + CoreLibrary.class.getName()
                    + ".init method in the onCreate method of "
                    + "your Application class ");
        }
        return instance;
    }

    /**
     * Use this method when testing.
     * It should replace org.smartregister.Context#setInstance(org.smartregister.Context, org.smartregister.repository.Repository) which has been removed
     *
     * @param context
     */
    public static void reset(Context context, Repository repository, ReferralMetadata referralMetadata, int applicationVersion, int databaseVersion) {
        if (context != null) {
            instance = new ReferralLibrary(context, repository, referralMetadata, applicationVersion, databaseVersion);
        }
    }

    public Context context() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public ECSyncHelper getEcSyncHelper() {
        if (syncHelper == null) {
            syncHelper = ECSyncHelper.getInstance(context().applicationContext());
        }
        return syncHelper;
    }

    public ClientProcessorForJava getClientProcessorForJava() {
        if (clientProcessorForJava == null) {
            clientProcessorForJava = ClientProcessorForJava.getInstance(context().applicationContext());
        }
        return clientProcessorForJava;
    }

    public void setClientProcessorForJava(ClientProcessorForJava clientProcessorForJava) {
        this.clientProcessorForJava = clientProcessorForJava;
    }

    public Compressor getCompressor() {
        if (compressor == null) {
            compressor = Compressor.getDefault(context().applicationContext());
        }
        return compressor;
    }

    /**
     * Use this method for testing purposes ONLY.
     * It seeds various data required by the module
     * It should be replaced by data synchronized from the server
     */
    public void seedSampleReferralServicesAndIndicators() {
        //initializing repositories
        ReferralServiceRepository referralServiceRepository = new ReferralServiceRepository(repository);
        ReferralServiceIndicatorRepository indicatorRepository = new ReferralServiceIndicatorRepository(repository);
        LocationRepository locationRepository = new LocationRepository(repository);
        FollowupFeedbackRepository followupFeedbackRepository = new FollowupFeedbackRepository(repository);

        if (context != null && referralServiceRepository.getReferralServices() == null) {
            //seeding referral services and indicators
            try {

                String referralServicesAndIndicatorsJsonString = AssetHandler.readFileFromAssetsFolder("ec_referral_services_and_indicators.json", context.applicationContext());
                JSONObject referralServicesJSONObject = new JSONObject(referralServicesAndIndicatorsJsonString);
                JSONArray services = referralServicesJSONObject.getJSONArray("services");

                for (int i = 0; i < services.length(); i++) {
                    JSONObject serviceObj = services.getJSONObject(i);
                    ReferralServiceObject referralServiceObject = new Gson().fromJson(serviceObj.toString(), ReferralServiceObject.class);
                    referralServiceRepository.saveReferralService(referralServiceObject);

                    JSONArray indicatorsArray = serviceObj.getJSONArray("indicators");
                    for (int j = 0; j < indicatorsArray.length(); j++) {
                        JSONObject indicatorObj = indicatorsArray.getJSONObject(j);
                        ReferralServiceIndicatorObject referralServiceIndicatorObject = new Gson().fromJson(indicatorObj.toString(), ReferralServiceIndicatorObject.class);

                        Timber.i("Before saving indicator = %s", new Gson().toJson(referralServiceIndicatorObject));
                        indicatorRepository.saveReferralServiceIndicator(referralServiceIndicatorObject);
                    }
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }

        try {
            if (locationRepository.getAllLocations().size() == 0) {
                //Seeding test health facility locations

                Location testFacilityA = new Location();
                testFacilityA.setId("1");

                LocationProperty property = new LocationProperty();
                property.setUid("718b2864-7d6a-44c8-b5b6-bb375f82654e");
                property.setParentId("");
                property.setName("Facility A");
                testFacilityA.setProperties(property);

                locationRepository.addOrUpdate(testFacilityA);

                Location testFacilityB = new Location();
                testFacilityB.setId("2");

                LocationProperty facilityBproperty = new LocationProperty();
                facilityBproperty.setUid("718b2864-7d6a-44c8-b5b6-bb375f826549");
                facilityBproperty.setParentId("");
                facilityBproperty.setName("Facility B");
                testFacilityB.setProperties(facilityBproperty);

                locationRepository.addOrUpdate(testFacilityB);
            }
        } catch (NullPointerException e) {
            Timber.e(e);
        }

        try {
            if (followupFeedbackRepository.getFollowupFeedbacks().size() == 0) {
                String followupFeedbackJsonString = AssetHandler.readFileFromAssetsFolder("ec_referral_feedback.json", context.applicationContext());

                JSONArray followupFeedbackJSONArrayList = new JSONArray(followupFeedbackJsonString);

                for (int i = 0; i < followupFeedbackJSONArrayList.length(); i++) {
                    JSONObject feedbackObj = followupFeedbackJSONArrayList.getJSONObject(i);
                    FollowupFeedbackObject followupFeedbackObject = new Gson().fromJson(feedbackObj.toString(), FollowupFeedbackObject.class);
                    followupFeedbackRepository.saveFollowupFeedback(followupFeedbackObject);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    public TaskRepository getTaskRepository() {
        if (taskRepository == null) {
            taskRepository = new TaskRepository(getRepository(), new TaskNotesRepository(getRepository()));
        }
        return taskRepository;
    }

    public ReferralMetadata getReferralMetadata() {
        return referralMetadata;
    }
}
