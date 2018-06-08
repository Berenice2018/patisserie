package de.avkterwey.baking.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import de.avkterwey.baking.model.Step;

/*
 * Created by Berenice on 19.05.18.
 */

public class StepsViewModel extends ViewModel {

    private MutableLiveData<List<Step>> mStepListLiveData;
    private MutableLiveData<Integer> mCurrentStepLiveData;

    public StepsViewModel(){
        this.mStepListLiveData = new MutableLiveData<>();
        this.mStepListLiveData.setValue(new ArrayList<Step>());
        this.mCurrentStepLiveData = new MutableLiveData<>();
        this.mCurrentStepLiveData.setValue(0);
    }


    public List<Step> getStepListLiveData() {
        return mStepListLiveData.getValue();
    }

    public void setStepsLiveData(List<Step> stepsLiveData) {
        mStepListLiveData.setValue(stepsLiveData);
    }

    public MutableLiveData<Integer> getCurrentStepLiveData() {
        return mCurrentStepLiveData;
    }

    public int getCurrentStep() {
        return mCurrentStepLiveData.getValue();
    }

    public void setCurrentStep(int currentStepLiveData) {
        mCurrentStepLiveData.setValue(currentStepLiveData);
    }

}
