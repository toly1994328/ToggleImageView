package com.toly1994.toggleimageview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.toly1994.toggle_imageview.ToggleImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);


        ToggleImageView ctrl = findViewById(R.id.id_tiv);
        ToggleImageView player = findViewById(R.id.id_tiv_player);


        ctrl.setWithScale(true);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(ctrl, "rotation", 0f, 180f).setDuration(300);
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(ctrl, "rotationX", 0f, 180f).setDuration(300);
        ObjectAnimator rotationY = ObjectAnimator.ofFloat(ctrl, "rotationY", 0f, 180f).setDuration(300);
        ctrl.setAnimator(rotation,rotationX,rotationY);

        ctrl.initTwoButton(
                R.drawable.icon_start_3, v -> {
                    Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
                }, R.drawable.icon_stop_3, v -> {
                    Toast.makeText(this, "停止", Toast.LENGTH_SHORT).show();

                });


        player.setResIds(
                R.drawable.icon_play_single, R.drawable.icon_play_single_loop,
                R.drawable.icon_play_loop, R.drawable.icon_play_random);
        player.setOnToggleListener((view, idx) -> {
            switch (idx) {
                case 0:
                    Toast.makeText(this, "单曲播放", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, "循环播放", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
