package org.smartregister.chw.referral.util;

import org.joda.time.DateTime;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.ReferralEvent;
import org.smartregister.domain.Task;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;

import java.util.UUID;

public class ReferralUtil {

    public static void createReferralTask(ReferralEvent referralEvent, AllSharedPreferences allSharedPreferences) {
        Task task = new Task();
        task.setIdentifier(UUID.randomUUID().toString());
        //TODO Implement plans remove hard coded plan (in 2020 road-map)
      /*  Iterator<String> iterator = ChwApplication.getInstance().getPlanDefinitionRepository()
                .findAllPlanDefinitionIds().iterator();
        if (iterator.hasNext()) {
            task.setPlanIdentifier(iterator.next());
        } else {

            Timber.e("No plans exist in the server");
        }*/
        task.setPlanIdentifier(Constants.REFERRAl.PLAN_ID);
        task.setGroupIdentifier(referralEvent.getGroupId());
        task.setStatus(Task.TaskStatus.READY);
        task.setBusinessStatus(Constants.BUSINESS_STATUS.REFERRED);
        task.setPriority(3);
        task.setCode(Constants.REFERRAl.CODE);
        task.setDescription(referralEvent.getReferralDescription());
        task.setFocus(referralEvent.getFocus());
        task.setForEntity(referralEvent.getBaseEntityId());
        DateTime now = new DateTime();
        task.setExecutionStartDate(now);
        task.setAuthoredOn(now);
        task.setLastModified(now);
        task.setOwner(allSharedPreferences.fetchRegisteredANM());
        task.setSyncStatus(BaseRepository.TYPE_Created);
        task.setRequester(allSharedPreferences.getANMPreferredName(allSharedPreferences.fetchRegisteredANM()));
        task.setLocation(allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM()));
        ReferralLibrary.getInstance().getTaskRepository().addOrUpdate(task);
    }
}
