package com.hongx.zhibo;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 直播界面，用于对接直播功能
 */
public class LiveFrag extends Fragment {

    private ImageView img_thumb;
    private VideoView video_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_live, null);
        img_thumb = view.findViewById(R.id.img_thumb);
        img_thumb.setVisibility(View.GONE);
        video_view = view.findViewById(R.id.video_view);
        video_view.setVisibility(View.VISIBLE);
        video_view.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video_1));
        video_view.start();
        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                video_view.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video_1));
                //或 //mVideoView.setVideoPath(Uri.parse(_filePath));
                video_view.start();
            }
        });
        return view;
    }

}