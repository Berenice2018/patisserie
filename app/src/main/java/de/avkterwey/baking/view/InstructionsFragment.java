package de.avkterwey.baking.view;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import de.avkterwey.baking.R;
import de.avkterwey.baking.databinding.InstructionsFragmentBinding;
import de.avkterwey.baking.viewmodel.SingleRecipeViewModel;
import de.avkterwey.baking.viewmodel.StepsViewModel;

/*
 * Created by Berenice on 23.05.18.
 */

public class InstructionsFragment extends Fragment {

    private static final String TAG = InstructionsFragment.class.getSimpleName();

    private StepsViewModel mStepsViewModel;
    private SingleRecipeViewModel mRecipeViewModel;
    private InstructionsFragmentBinding mBinding;

    private SimpleExoPlayer mPlayer;
    private int mResumeWindow;
    private long mResumePos;
    private Dialog mFullScreenDialog;

    private static InstructionsFragment sInstance;
    private boolean mExoPlayerFullscreen;
    private ImageView mFullScreenIcon;
    private FrameLayout mFullScreenButton;

    public static InstructionsFragment getInstance(){
        if (sInstance == null){
            sInstance = new InstructionsFragment();
        }
        return sInstance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ON CREATE");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mBinding = InstructionsFragmentBinding.inflate(inflater, container, false);
        mRecipeViewModel = ViewModelProviders.of(getActivity()).get(SingleRecipeViewModel.class);
        mStepsViewModel = ViewModelProviders.of(getActivity()).get(StepsViewModel.class);

        setButtonListener();

        mStepsViewModel.getCurrentStepLiveData().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer currentStepPosition) {
                // show or hide the prev/ next button
                updateButtonVisibility(currentStepPosition);

                // update the instructions text
                String currentInstruction = mRecipeViewModel.getStepsLiveData().getValue()
                        .get(currentStepPosition)
                        .getDescription();
                if(mBinding != null)
                    mBinding.instruction.setText(currentInstruction);

                // update and play the respective video
                showVideo(currentStepPosition);
            }
        });


        // setup the ExoPlayer
        mBinding.videoPlayer.setDefaultArtwork(BitmapFactory.decodeResource(
                getResources(), R.drawable.default_picture)
        );

        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer == null) {
            int currentStep = mStepsViewModel.getCurrentStep();
            String url = mStepsViewModel.getStepListLiveData().get(currentStep).getVideoUrl();
            Log.d(TAG, "VIDEO Url = " + url);

            if (TextUtils.isEmpty(url)) {
                return;
            }

            initializePlayerWithUri(Uri.parse(url));
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }



    private void showVideo(int currentStep){
        String videoUrl = mStepsViewModel.getStepListLiveData().get(currentStep).getVideoUrl();
        String thumbUrl = mStepsViewModel.getStepListLiveData().get(currentStep).getThumbnailUrl();

        releasePlayer();
        clearResumePosition();

        if(TextUtils.isEmpty(videoUrl)){
            if(!TextUtils.isEmpty(thumbUrl) && thumbUrl.endsWith("mp4")){
                videoUrl = thumbUrl;
            } else{
                mBinding.videoPlayer.setVisibility(View.INVISIBLE);
                return;
            }
        } else{
            mBinding.videoPlayer.setVisibility(View.VISIBLE);
        }

        initializePlayerWithUri(Uri.parse(videoUrl));

    }

    private void initializePlayerWithUri(Uri uri) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        if (getContext() != null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    trackSelector, new DefaultLoadControl());

            String userAgent = Util.getUserAgent(getContext(), "Baking");
            MediaSource mediaSource = new ExtractorMediaSource(
                    uri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            mBinding.videoPlayer.setPlayer(mPlayer);
            mPlayer.prepare(mediaSource);

            mPlayer.setPlayWhenReady(true);

            boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                mPlayer.seekTo(mResumeWindow, mResumePos);
            }
        }


    }


    private void releasePlayer() {
        if (mPlayer == null)
            return;

        updateResumePosition();
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    private void updateResumePosition() {
        mResumeWindow = mPlayer.getCurrentWindowIndex();
        mResumePos = Math.max(0, mPlayer.getContentPosition());
    }

    private void clearResumePosition() {
        mResumeWindow = C.INDEX_UNSET;
        mResumePos = C.TIME_UNSET;
    }


    private void setButtonListener(){
        mBinding.nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepsViewModel.setCurrentStep(mStepsViewModel.getCurrentStep() + 1);
            }
        });

        mBinding.previousStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepsViewModel.setCurrentStep(mStepsViewModel.getCurrentStep() - 1);
            }
        });
    }



    private void updateButtonVisibility(int currentStepPosition) {
        int visibilityPrevious = currentStepPosition == 0 ? View.INVISIBLE : View.VISIBLE;
        mBinding.previousStepBtn.setVisibility(visibilityPrevious);

        int numberOfSteps = mRecipeViewModel.getSteps().size();
        int visibilityNext = currentStepPosition ==  numberOfSteps-1 ? View.INVISIBLE : View.VISIBLE;
        mBinding.nextStepBtn.setVisibility(visibilityNext);
    }


}

