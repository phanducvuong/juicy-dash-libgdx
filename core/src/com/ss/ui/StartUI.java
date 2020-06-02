package com.ss.ui;

import static com.badlogic.gdx.math.Interpolation.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.config.Config;
import com.ss.controller.StartUIController;
import com.ss.core.effect.SoundEffects;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.utils.Solid;

public class StartUI extends Group {

  private float CENTER_X = GStage.getWorldWidth()/2;
  private float CENTER_Y = GStage.getWorldHeight()/2;

  private StartUIController controller;
  private CrossPanel        crossPanel;
  private Group             gBackground, gPopup;

  private Image blackScreen;
  private Image dashBottom,
                dashTop,
                title,
                btnRank,
                btnStart;

  public StartUI(StartUIController controller) {

    this.blackScreen  = new Image(Solid.create(new Color(128/255f, 213/255f, 181/255f, .45f)));
    this.blackScreen.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    this.controller   = controller;
    this.gBackground  = new Group();
    this.gPopup       = new Group();
    this.crossPanel   = new CrossPanel(this.blackScreen);

    this.addActor(gBackground);
    this.addActor(gPopup);

    SoundEffects.startMusic();
    initBg();

    Image icon = GUI.createImage(GMain.bgAtlas, "icon_pause");
    icon.setPosition(CENTER_X + 100, 20);
//    gBackground.addActor(icon);

    icon.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        showCrossPanel();

      }
    });
  }

  private void initBg() {

    Image bg = GUI.createImage(GMain.bgAtlas, "bg_home");
    bg.setSize(CENTER_X*2, CENTER_Y*2);
    gBackground.addActor(bg);

    title = GUI.createImage(GMain.bgAtlas, "title");
    title.setPosition(CENTER_X - title.getWidth()/2, -title.getHeight());
    gBackground.addActor(title);

    dashTop = GUI.createImage(GMain.bgAtlas, "dash_top");
    dashTop.setPosition(CENTER_X - dashTop.getWidth()/2, CENTER_Y - dashTop.getHeight()/2 - 80);
    gBackground.addActor(dashTop);

    dashBottom = GUI.createImage(GMain.bgAtlas, "dash_bottom");
    dashBottom.setPosition(dashTop.getX() + 128, dashTop.getY() + dashTop.getHeight() - 120);
    gBackground.addActor(dashBottom);

    btnStart = GUI.createImage(GMain.bgAtlas, "icon_start");
    btnStart.setScale(1.3f);
    btnStart.setOrigin(Align.center);
    btnStart.setPosition(CENTER_X - btnStart.getWidth()/2, CENTER_Y*2 - btnStart.getHeight() - 80);
    gBackground.addActor(btnStart);

    //label: btnSound
    Image soundOn = GUI.createImage(GMain.bgAtlas, "icon_sound_on");
    soundOn.setScale(1.1f);
    soundOn.setOrigin(Align.center);
    soundOn.setPosition(CENTER_X*2 - soundOn.getWidth() - 20, 180);
    gBackground.addActor(soundOn);

    Image soundOff = GUI.createImage(GMain.bgAtlas, "icon_sound_off");
    soundOff.setScale(1.1f);
    soundOff.setOrigin(Align.center);
    soundOff.setPosition(soundOn.getX(), soundOn.getY());
    soundOff.setVisible(false);
    gBackground.addActor(soundOff);

    //label: btnGames
    Image btnGames = GUI.createImage(GMain.bgAtlas, "i_games");
    btnGames.setScale(.8f);
    btnGames.setOrigin(Align.center);
    btnGames.setPosition(soundOn.getX() + soundOn.getWidth()/2 - btnGames.getWidth()/2,
            soundOn.getY() - soundOn.getHeight());
    gBackground.addActor(btnGames);

    //label: btnRank
    Image btnRank = GUI.createImage(GMain.bgAtlas, "icon_rank");
    btnRank.setScale(.8f);
    btnRank.setOrigin(Align.center);
    btnRank.setPosition(soundOn.getX() + soundOn.getWidth()/2 - btnGames.getWidth()/2,
            soundOn.getY() + soundOn.getHeight());
    gBackground.addActor(btnRank);

    //label: event click
    imgClick(soundOn, () -> {
      SoundEffects.isMuteMusic = true;
      SoundEffects.isMuteSound = true;
      SoundEffects.music.stop();

      soundOn.setVisible(false);
      soundOff.setVisible(true);
    });

    imgClick(soundOff, () -> {
      SoundEffects.isMuteMusic = false;
      SoundEffects.isMuteSound = false;
      SoundEffects.music.play();

      soundOn.setVisible(true);
      soundOff.setVisible(false);
    });

    imgClick(btnGames, this::showCrossPanel);

    imgClick(btnStart, () -> {
      resetAnimObject();
      controller.scene.setScreen(GMain.inst.gameScene);
    });

    imgClick(btnRank, () -> {

    });

  }

  private void imgClick(Image btn, Runnable onComplete) {
    btn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.start("click", Config.CLICK_VOLUME);

        btn.setTouchable(Touchable.disabled);
        btn.addAction(
                sequence(
                        Actions.scaleBy(.1f, .1f, .05f, fastSlow),
                        Actions.scaleBy(-.1f, -.1f, .05f, fastSlow),
                        run(() -> btn.setTouchable(Touchable.enabled)),
                        run(onComplete)
                )
        );

      }
    });
  }

  public void animObjectStart() {
    title.addAction(
            moveTo(CENTER_X - title.getWidth()/2, 150, .5f, swingOut)
    );

    dashBottom.addAction(
            sequence(
                    delay(.75f),
                    Actions.moveBy(-50, 20, .35f, slowFast),
                    delay(.05f),
                    run(() -> animBtnStart(btnStart))
            )
    );
  }

  public void resetAnimObject() {
    title.clearActions();
    title.setY(-title.getHeight());

    btnStart.setRotation(0);
    btnStart.clearActions();

    dashBottom.clearActions();
    dashBottom.setPosition(dashTop.getX() + 128, dashTop.getY() + dashTop.getHeight() - 120);
  }

  private void animBtnStart(Image btn) {
    btn.addAction(
            sequence(
                    Actions.rotateBy(15, .25f, fastSlow),
                    Actions.rotateBy(-30, .25f, fastSlow),
                    rotateTo(0, .25f, fastSlow),
                    delay(3f),
                    run(() -> animBtnStart(btn))
            )
    );
  }

  private void showCrossPanel() {
    gPopup.addActor(blackScreen);
    gPopup.addActor(crossPanel);
    crossPanel.addAction(
            parallel(
                    scaleTo(1f, 1f, .25f, fastSlow),
                    alpha(1f, .35f, linear)
            )
    );
  }

}
