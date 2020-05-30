package com.ss.ui;

import static com.badlogic.gdx.math.Interpolation.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.controller.GameUIController;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;

public class PauseUI extends Group {

  private float CENTER_X = GStage.getWorldWidth()/2;
  private float CENTER_Y = GStage.getWorldHeight()/2;

  private GameUIController  controller;
  private Group             gPause;
  private Image             btnResume,
                            btnRestart,
                            btnSoundOn,
                            btnSoundOff,
                            btnHome;

  public PauseUI(GameUIController controller) {
    this.controller = controller;
    initUI();
  }

  private void initUI() {
    gPause = new Group();
    Image bgPause = GUI.createImage(GMain.bgAtlas, "popup_start");
    bgPause.setScale(1.2f);
    gPause.setSize(bgPause.getWidth()*1.2f, bgPause.getHeight()*1.2f);
    gPause.setOrigin(Align.center);
    gPause.setPosition(CENTER_X - gPause.getWidth()/2,CENTER_Y - gPause.getHeight()/2);
    gPause.addActor(bgPause);

    //label: btnResume
    btnResume = GUI.createImage(GMain.bgAtlas, "icon_start");
    btnResume.setOrigin(Align.center);
    btnResume.setPosition(bgPause.getX() + gPause.getWidth()/2 - btnResume.getWidth()*btnResume.getScaleX()/2,
            bgPause.getY() + gPause.getHeight()/2 - btnResume.getWidth()*btnResume.getScaleY()/2 - 150);
    gPause.addActor(btnResume);

    eventClickBtn(btnResume, () -> {
      controller.isPause = false;
      hidePause();
    });

    //label: btnHome
    btnHome = GUI.createImage(GMain.bgAtlas, "icon_home");
    btnHome.setScale(1.1f);
    btnHome.setOrigin(Align.center);
    btnHome.setPosition(bgPause.getX() + gPause.getWidth()/2 - btnHome.getWidth()*btnHome.getScaleX()/2,
            bgPause.getY() + gPause.getHeight() - btnHome.getHeight()*btnHome.getScaleY() - 80);
    gPause.addActor(btnHome);

    eventClickBtn(btnHome, () -> {
      controller.scene.setScreen(GMain.inst.startScene);
    });

    //label: btnRestart
    btnRestart = GUI.createImage(GMain.bgAtlas, "icon_restart");
    btnRestart.setScale(1.1f);
    btnRestart.setOrigin(Align.center);
    btnRestart.setPosition(bgPause.getX() + 80,
            bgPause.getY() + gPause.getHeight()/2 - btnRestart.getHeight()*btnRestart.getScaleY()/2 + 80);
    gPause.addActor(btnRestart);


    eventClickBtn(btnRestart, this::hidePauseAndRestartGame);

    //label: btnSoundOn
    btnSoundOn = GUI.createImage(GMain.bgAtlas, "icon_sound_on");
    btnSoundOn.setScale(1.1f);
    btnSoundOn.setOrigin(Align.center);
    btnSoundOn.setPosition(bgPause.getX() + gPause.getHeight() - btnSoundOn.getWidth()*btnSoundOn.getScaleX() - 80,
            btnRestart.getY());
    gPause.addActor(btnSoundOn);

    eventClickBtn(btnSoundOn, () -> {
      btnSoundOn.setTouchable(Touchable.enabled);
      btnSoundOn.setVisible(false);
      btnSoundOff.setVisible(true);
    });

    //label: btnSoundOff
    btnSoundOff = GUI.createImage(GMain.bgAtlas, "icon_sound_off");
    btnSoundOff.setScale(1.1f);
    btnSoundOff.setOrigin(Align.center);
    btnSoundOff.setPosition(btnSoundOn.getX(), btnSoundOn.getY());
    btnSoundOff.setVisible(false);
    gPause.addActor(btnSoundOff);

    eventClickBtn(btnSoundOff, () -> {
      btnSoundOff.setTouchable(Touchable.enabled);
      btnSoundOff.setVisible(false);
      btnSoundOn.setVisible(true);
    });

    gPause.setScale(2.5f);
    gPause.getColor().a = 0f;
    this.addActor(gPause);
  }

  private void eventClickBtn(Image btn, Runnable onComplete) {

    btn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        btn.setTouchable(Touchable.disabled);
        animClick(btn, onComplete);
      }
    });

  }

  private void animClick(Image btn, Runnable onComplete) {
    btn.addAction(
            sequence(
                    Actions.scaleBy(.1f, .1f, .05f, fastSlow),
                    Actions.scaleBy(-.1f, -.1f, .05f, fastSlow),
                    run(onComplete)
            )
    );
  }

  public void showPause() {
    gPause.addAction(
            sequence(
                    parallel(
                            scaleTo(1f, 1f, .25f, fastSlow),
                            alpha(1f, .5f, linear)
                    ),
                    run(this::unlockTouchable)
            )
    );
  }

  public void hidePause() {
    gPause.addAction(
            sequence(
                    parallel(
                            scaleTo(2.5f, 2.5f, .5f, fastSlow),
                            alpha(0f, .25f)
                    ),
                    run(() -> {
                      controller.blackScreen.remove();
                      controller.isPause = false;
                      this.remove();
                    })
            )
    );
  }

  private void hidePauseAndRestartGame() {
    gPause.addAction(
            sequence(
                    parallel(
                            scaleTo(2.5f, 2.5f, .5f, fastSlow),
                            alpha(0f, .25f)
                    ),
                    run(() -> {
                      controller.blackScreen.remove();
                      controller.isPause = false;
                      controller.newGame();
                      this.remove();
                    })
            )
    );
  }

  private void unlockTouchable() {
    btnResume.setTouchable(Touchable.enabled);
    btnRestart.setTouchable(Touchable.enabled);
    btnHome.setTouchable(Touchable.enabled);
    btnSoundOn.setTouchable(Touchable.enabled);
    btnSoundOff.setTouchable(Touchable.enabled);
  }

}
