package com.oucb303.training.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.oucb303.training.R;
import com.oucb303.training.utils.Constant;


/**
 * 播放视频
 */
public class VideoActivity extends AppCompatActivity
{
//    private VideoView mVideoView;

    private String path = Environment.getExternalStorageDirectory() + "/video.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
//        playFunction();

        Log.d(Constant.LOG_TAG, path);
    }


    void playFunction()
    {
       // mVideoView = (VideoView) findViewById(R.id.surface_view);
        //mVideoView.setVideoPath(path);//设置播放地址
        //MediaController mMediaController = new MediaController(this);//实例化控制器
       // mMediaController.show(5000);//控制器显示5s后自动隐藏
        //mVideoView.setMediaController(mMediaController);//绑定控制器
       // mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//设置播放画质 高画质
        //mVideoView.requestFocus();//取得焦点

        //视频播放器的准备,此时播放器已经准备好了,此处可以设置一下播放速度,播放位置等等
      //  mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
      //  {
      //      @Override
       //     public void onPrepared(MediaPlayer mediaPlayer)
       //     {
                //此处设置播放速度为正常速度1
                //mediaPlayer.setPlaybackSpeed(1.0f);
       //     }
       // /});
        //当播放完成后,从头开始
      // mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
       // {
        //    @Override
        //    public void onCompletion(MediaPlayer mediaPlayer)
        //    {
      //          mediaPlayer.pause();
       //         mediaPlayer.seekTo(0);   //转到第一帧
       //         //mediaPlayer.start();     //开始播放
       //     }
       // });

    }


}
