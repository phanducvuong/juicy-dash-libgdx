package com.ss.gameLogic.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GClipGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.config.Strings;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.logic.Logic;

public class StartScene {

  private Effect effect;
  private Game game;
  private Group gParent, gStartScene;

  private Group gBtnStart, gBtnRank, gBtnOtherGame;
  private Image iconTutorial, iconSetting, iconMiniGame, iconGift;

  private Group gPanelTutorial;
  private Image btnXPanelTutorial, blackTutorial;

  private Group gPanelBet;
  private Image blackPanelBet, arrLeft, arrRight;
  private Label lbNumPlayer;

  private int numOfPlayer = 6;
  private long moneyBet = 10000;

  public StartScene(Game game, Group gParent) {

    this.effect = Effect.getInstance(game);
    this.game = game;
    this.gParent = gParent;
    this.gStartScene = new Group();
    this.gStartScene.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    this.gStartScene.setOrigin(Align.center);

    initUI();
    initIcon();
    initPanelTutorial();
    initPanelBet();
    handleClick();
    handleClickIcon();

  }

  private void initPanelBet() {

    blackPanelBet = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackPanelBet.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gPanelBet = new Group();
    Image bgPanel = GUI.createImage(GMain.startSceneAtlas, "panel_bet");
    bgPanel.setScale(1.5f);
    gPanelBet.setSize(bgPanel.getWidth(), bgPanel.getHeight());
    gPanelBet.setPosition(Config.CENTER_X - bgPanel.getWidth()*1.5f/2, Config.CENTER_Y - bgPanel.getHeight()*1.5f/2);
    gPanelBet.addActor(bgPanel);

    Image bgChooseNumPlayer = GUI.createImage(GMain.startSceneAtlas, "bg_panel_bet_number");
    bgChooseNumPlayer.setScale(1.5f);
    bgChooseNumPlayer.setPosition(bgPanel.getWidth()*bgPanel.getScaleX() - bgChooseNumPlayer.getWidth()*bgChooseNumPlayer.getScaleX() - 60, 350);
    gPanelBet.addActor(bgChooseNumPlayer);

    Label lbTxtNumPlayer = new Label("NGƯỜI CHƠI:", new Label.LabelStyle(Config.ALERT_FONT, null));
    lbTxtNumPlayer.setAlignment(Align.center);
    lbTxtNumPlayer.setFontScale(.7f, .8f);
    lbTxtNumPlayer.setPosition(-40, bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY()/2 - lbTxtNumPlayer.getHeight()/2 - 10);
    gPanelBet.addActor(lbTxtNumPlayer);

    arrLeft = GUI.createImage(GMain.startSceneAtlas, "arrow_left");
    arrLeft.setScale(1.5f);
    arrLeft.setOrigin(Align.center);
    arrLeft.setPosition(bgChooseNumPlayer.getX() + arrLeft.getWidth()*arrLeft.getScaleX()/2 - 10,
                            bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY()/2 - arrLeft.getHeight()*arrLeft.getScaleY()/2 + 20);
    gPanelBet.addActor(arrLeft);

    arrRight = GUI.createImage(GMain.startSceneAtlas, "arrow_right");
    arrRight.setScale(1.5f);
    arrRight.setPosition(bgChooseNumPlayer.getX() + bgChooseNumPlayer.getWidth()*bgChooseNumPlayer.getScaleX() - arrRight.getWidth()*arrRight.getScaleX()/2 - 40,
            arrLeft.getY());
    arrRight.setOrigin(Align.center);
    gPanelBet.addActor(arrRight);

    lbNumPlayer = new Label("6", new Label.LabelStyle(Config.ALERT_FONT, null));
    lbNumPlayer.setAlignment(Align.center);
    lbNumPlayer.setFontScale(1.5f);
    lbNumPlayer.setPosition(bgChooseNumPlayer.getX() + bgChooseNumPlayer.getWidth()*bgChooseNumPlayer.getScaleX()/2 - lbNumPlayer.getWidth()/2,
            bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY()/2 - lbNumPlayer.getHeight()/2 - 20);
    gPanelBet.addActor(lbNumPlayer);

    Image bgChooseBet = GUI.createImage(GMain.startSceneAtlas, "bg_panel_bet_chip");
    bgChooseBet.setScale(1.5f);
    bgChooseBet.setPosition(bgChooseNumPlayer.getX() + bgChooseNumPlayer.getWidth()*bgChooseNumPlayer.getScaleX() - bgChooseBet.getWidth()*bgChooseBet.getScaleX(),
                              bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY() + 50);
    gPanelBet.addActor(bgChooseBet);

    Label lbTxtBet = new Label("MỨC CƯỢC:", new Label.LabelStyle(Config.ALERT_FONT, null));
    lbTxtBet.setAlignment(Align.center);
    lbTxtBet.setFontScale(.7f, .8f);
    lbTxtBet.setPosition(-40, bgChooseBet.getY() - lbTxtBet.getHeight()/2 + 20);
    gPanelBet.addActor(lbTxtBet);

    int t = -1;
    for (int i=0; i<3; i++) {
      for (int j=0; j<2; j++) {
        t++;
        String region = Logic.getInstance().getRegionChip(t);

        if (j % 2 == 0) {
          Image chip = GUI.createImage(GMain.liengAtlas, region);
          chip.setScale(2.2f);
          chip.setPosition(bgChooseBet.getX() + bgChooseBet.getWidth()*bgChooseBet.getScaleX()/2 - chip.getWidth()*chip.getScaleX() - 70,
                  bgChooseBet.getY() + 30 + i*140);
          gPanelBet.addActor(chip);
        }
        else {
          Image chip = GUI.createImage(GMain.liengAtlas, region);
          chip.setScale(2.2f);
          chip.setPosition(bgChooseBet.getX() + bgChooseBet.getWidth()*bgChooseBet.getScaleX()/2 + chip.getWidth()*chip.getScaleX() - 40,
                  bgChooseBet.getY() + 30 + i*140);
          gPanelBet.addActor(chip);
        }
      }

    }

    gStartScene.addActor(gPanelBet);

  }

  private void initPanelTutorial() {

    //label: panel tutorial
    blackTutorial = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackTutorial.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gPanelTutorial = new Group();
    Image panelTutorial = GUI.createImage(GMain.startSceneAtlas, "panel_tutorial");
    panelTutorial.setScale(1.5f, 1.5f);
    gPanelTutorial.setSize(panelTutorial.getWidth()*1.5f, panelTutorial.getHeight()*1.5f);
    gPanelTutorial.setPosition(Config.CENTER_X - panelTutorial.getWidth()*1.5f/2, Config.CENTER_Y - panelTutorial.getHeight()*1.5f/2);
    gPanelTutorial.setOrigin(Align.center);
    gPanelTutorial.addActor(panelTutorial);

    Group gLb = new Group();
    gLb.setSize(panelTutorial.getWidth() + 230, panelTutorial.getHeight()*1.5f - 55);
    gLb.setPosition(panelTutorial.getWidth()*1.5f/2 - gLb.getWidth()/2, panelTutorial.getHeight()*1.5f/2 - gLb.getHeight()/2);
    gLb.setOrigin(Align.center);
    gLb.setScale(1, -1);

    Label lbChkWidth = new Label("", new Label.LabelStyle(Config.ALERT_FONT, null));
    lbChkWidth.setAlignment(Align.center);
    lbChkWidth.setFontScale(.8f);

    StringBuffer sBuff = new StringBuffer();
    Table scroll = new Table();
    for (int index=0; index<Strings.aaa.length; index++) {

      String[] s = Strings.aaa[index].split(" ");

      if (index == 0)
        createLb(gLb, Strings.aaa[index], scroll, 0);
      else {

        int i = 0;
        int count = 0;
        for (String ss : s) {
          i += 1;
          count += 1;

          sBuff.append(ss).append(" ");

          if (i >= 10) {

            createLb(gLb, sBuff.toString(), scroll, 1);
            sBuff.delete(0, sBuff.length());
            i=0;

          }
          else if (count == s.length) {
            sBuff.append("\n").append("\n").append("\n");
            createLb(gLb, sBuff.toString(), scroll, 1);
            sBuff.delete(0, sBuff.length());
          }
        }

      }

    }

    ScrollPane scrollPane = new ScrollPane(scroll);
    Table table = new Table();
    table.setFillParent(true);
    table.add(scrollPane).fill().expand();
    gLb.addActor(table);

    btnXPanelTutorial = GUI.createImage(GMain.liengAtlas, "btn_x");
    btnXPanelTutorial.setScale(1.5f);
    btnXPanelTutorial.setPosition(gPanelTutorial.getWidth() - btnXPanelTutorial.getWidth()/2 - 20, -50);
    gPanelTutorial.addActor(btnXPanelTutorial);

    gPanelTutorial.addActor(gLb);
    gPanelTutorial.setScale(0);

  }

  private void createLb(Group gLb, String string, Table scroll, int align) {

    Group g = new Group();
    g.setSize(gLb.getWidth(), 30);
    g.setOrigin(Align.center);
    g.setScale(1f, -1f);

    Label lbTutorial = new Label(string, new Label.LabelStyle(Config.TUTORIAL, null));
    if (align == 0) {
      lbTutorial.setAlignment(Align.center);
      lbTutorial.setFontScale(1.2f);
      lbTutorial.setPosition(g.getWidth()/2 - lbTutorial.getWidth()/2, lbTutorial.getY());
    }
    else {
      lbTutorial.setFontScale(.8f);
      lbTutorial.setAlignment(Align.left);
    }

    g.addActor(lbTutorial);

    scroll.add(g).padBottom(50).padTop(30);
    scroll.row();

  }

  private void initUI() {

    Image bgStart = GUI.createImage(GMain.startSceneAtlas, "bg_start_game");
    bgStart.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    gStartScene.addActor(bgStart);

    Image logo = GUI.createImage(GMain.startSceneAtlas, "logo");
    logo.setPosition(100, GStage.getWorldHeight()/2 - logo.getHeight()/2 - 100);
    gStartScene.addActor(logo);

    //label: button startScene
    gBtnStart = new Group();
    Image btnStart = GUI.createImage(GMain.startSceneAtlas, "btn_start");
    gBtnStart.addActor(btnStart);
    gBtnStart.setSize(btnStart.getWidth(), btnStart.getHeight());
    gBtnStart.setOrigin(Align.center);
    gBtnStart.setPosition(GStage.getWorldWidth()-btnStart.getWidth()-40, 400);

    Label lbStart = new Label(C.lang.startScene, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbStart.setFontScale(.85f);
    lbStart.setAlignment(Align.center);
    lbStart.setPosition(btnStart.getX() + btnStart.getWidth()/2 - lbStart.getWidth()/2 + 40,
                        btnStart.getY() + btnStart.getHeight()/2 - lbStart.getHeight()/2 - 10);
    gBtnStart.addActor(lbStart);

    GClipGroup gClipStart = new GClipGroup();
    gClipStart.setClipArea(btnStart.getX(), btnStart.getY(), btnStart.getWidth() - 70, btnStart.getHeight() - 30);
    gClipStart.setPosition(btnStart.getX() + 40, btnStart.getY() + 25);
    gBtnStart.addActor(gClipStart);

    Image lightBtnStart = GUI.createImage(GMain.startSceneAtlas, "light_button");
    lightBtnStart.setPosition(-lightBtnStart.getWidth(), 25);
    gClipStart.addActor(lightBtnStart);

    Image iconBtnStart = GUI.createImage(GMain.startSceneAtlas, "icon_btn_start");
    iconBtnStart.setPosition(-80, -30);
    gBtnStart.addActor(iconBtnStart);

    float moveToXStart = gClipStart.getX() + gClipStart.getWidth() + lightBtnStart.getWidth() + 160;
    Effect.getInstance(game).moveLight(lightBtnStart, moveToXStart);
    gStartScene.addActor(gBtnStart);

    //label: button rank
    gBtnRank = new Group();
    Image btnRank = GUI.createImage(GMain.startSceneAtlas, "btn_rank");
    gBtnRank.addActor(btnRank);
    gBtnRank.setSize(btnRank.getWidth(), btnRank.getHeight());
    gBtnRank.setOrigin(Align.center);
    gBtnRank.setPosition(gBtnStart.getX(), gBtnStart.getY() + gBtnRank.getHeight() + 70);

    Label lbRank = new Label(C.lang.rank, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbRank.setFontScale(.85f);
    lbRank.setAlignment(Align.center);
    lbRank.setPosition(btnRank.getX() + btnRank.getWidth()/2 - lbRank.getWidth()/2 + 20,
            btnRank.getY() + btnRank.getHeight()/2 - lbRank.getHeight()/2 - 10);
    gBtnRank.addActor(lbRank);

    GClipGroup gClipRank = new GClipGroup();
    gClipRank.setClipArea(btnRank.getX(), btnRank.getY(), btnRank.getWidth() - 70, btnRank.getHeight() - 30);
    gClipRank.setPosition(btnRank.getX() + 40, btnRank.getY() + 25);
    gBtnRank.addActor(gClipRank);

    Image lightBtnRank = GUI.createImage(GMain.startSceneAtlas, "light_button");
    lightBtnRank.setPosition(-lightBtnRank.getWidth(), 25);
    gClipRank.addActor(lightBtnRank);

    Image iconBtnRank = GUI.createImage(GMain.startSceneAtlas, "icon_btn_rank");
    iconBtnRank.setPosition(-60, -50);
    gBtnRank.addActor(iconBtnRank);

    float moveToXRank = gClipRank.getX() + gClipRank.getWidth() + lightBtnRank.getWidth() + 160;
    Effect.getInstance(game).moveLight(lightBtnRank, moveToXRank);
    gStartScene.addActor(gBtnRank);

    //label: button other game
    gBtnOtherGame = new Group();
    Image btnOtherGame = GUI.createImage(GMain.startSceneAtlas, "btn_other_game");
    gBtnOtherGame.addActor(btnOtherGame);
    gBtnOtherGame.setSize(btnOtherGame.getWidth(), btnOtherGame.getHeight());
    gBtnOtherGame.setOrigin(Align.center);
    gBtnOtherGame.setPosition(gBtnRank.getX(), gBtnRank.getY() + gBtnRank.getHeight() + 70);

    Label lbOtherGame = new Label(C.lang.otherGame, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbOtherGame.setFontScale(.8f, .85f);
    lbOtherGame.setAlignment(Align.center);
    lbOtherGame.setPosition(btnOtherGame.getX() + btnOtherGame.getWidth()/2 - lbOtherGame.getWidth()/2 + 30,
            btnOtherGame.getY() + btnOtherGame.getHeight()/2 - lbOtherGame.getHeight()/2 - 10);
    gBtnOtherGame.addActor(lbOtherGame);

    GClipGroup gClipOtherGame = new GClipGroup();
    gClipOtherGame.setClipArea(btnOtherGame.getX(), btnOtherGame.getY(), btnOtherGame.getWidth() - 70, btnOtherGame.getHeight() - 30);
    gClipOtherGame.setPosition(btnOtherGame.getX() + 40, btnOtherGame.getY() + 25);
    gBtnOtherGame.addActor(gClipOtherGame);

    Image lightBtnOtherGame = GUI.createImage(GMain.startSceneAtlas, "light_button");
    lightBtnOtherGame.setPosition(-lightBtnOtherGame.getWidth(), 25);
    gClipOtherGame.addActor(lightBtnOtherGame);

    Image iconBtnOtherGame = GUI.createImage(GMain.startSceneAtlas, "icon_btn_other_game");
    iconBtnOtherGame.setPosition(-60, -30);
    gBtnOtherGame.addActor(iconBtnOtherGame);

    float moveToXOtherGame = gClipOtherGame.getX() + gClipOtherGame.getWidth() + lightBtnOtherGame.getWidth() + 160;
    Effect.getInstance(game).moveLight(lightBtnOtherGame, moveToXOtherGame);
    gStartScene.addActor(gBtnOtherGame);

  }

  private void initIcon() {

    iconTutorial = GUI.createImage(GMain.startSceneAtlas, "icon_tutorial");
    iconTutorial.setPosition(GStage.getWorldWidth() - iconTutorial.getWidth() - 50, 20);
    iconTutorial.setOrigin(Align.center);
    gStartScene.addActor(iconTutorial);

    iconSetting = GUI.createImage(GMain.startSceneAtlas, "icon_setting");
    iconSetting.setPosition(iconTutorial.getX() - iconSetting.getWidth() - 30, iconTutorial.getY());
    iconSetting.setOrigin(Align.center);
    gStartScene.addActor(iconSetting);

    iconMiniGame = GUI.createImage(GMain.startSceneAtlas, "icon_mini_game");
    iconMiniGame.setPosition(iconSetting.getX() - iconMiniGame.getWidth() - 30, iconSetting.getY());
    iconMiniGame.setOrigin(Align.center);
    gStartScene.addActor(iconMiniGame);

    iconGift = GUI.createImage(GMain.startSceneAtlas, "icon_gift");
    iconGift.setPosition(iconMiniGame.getX() - iconGift.getWidth() - 30, iconMiniGame.getY());
    iconGift.setOrigin(Align.center);
    gStartScene.addActor(iconGift);

  }

  private void handleClickIcon() {

    iconTutorial.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          iconTutorial.setTouchable(Touchable.enabled);
          effect.sclMinToMax(gPanelTutorial);

        };

        gStartScene.addActor(blackTutorial);
        gStartScene.addActor(gPanelTutorial);
        iconTutorial.setTouchable(Touchable.disabled);
        effect.click(iconTutorial, run);

      }
    });

    btnXPanelTutorial.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        btnXPanelTutorial.setTouchable(Touchable.disabled);
        effect.sclMaxToMin(gPanelTutorial, () -> {
          btnXPanelTutorial.setTouchable(Touchable.enabled);
          blackTutorial.remove();
          gPanelTutorial.remove();
        });

      }
    });

    arrLeft.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          arrLeft.setTouchable(Touchable.enabled);
          numOfPlayer--;
          if (numOfPlayer < 2)
            numOfPlayer = 2;

          lbNumPlayer.setText(numOfPlayer+"");

        };

        arrLeft.setTouchable(Touchable.disabled);
        effect.click(arrLeft, run);

      }
    });

    arrRight.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          arrRight.setTouchable(Touchable.enabled);
          numOfPlayer++;
          if (numOfPlayer > 6)
            numOfPlayer = 6;

          lbNumPlayer.setText(numOfPlayer+"");

        };

        arrRight.setTouchable(Touchable.disabled);
        effect.click(arrRight, run);

      }
    });

  }

  private void handleClick() {

    gBtnStart.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          gBtnStart.setTouchable(Touchable.enabled);
          Effect.getInstance(game).zoomOut(gStartScene, () -> {
            game.setData(3, 10000);
            gStartScene.remove();
          });

        };


        gBtnStart.setTouchable(Touchable.disabled);
        Effect.getInstance(game).click(gBtnStart, run);

      }
    });

    gBtnRank.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

      }
    });

    gBtnOtherGame.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

      }
    });

  }

  public void addToScene() {
    gParent.addActor(gStartScene);
  }

  public void remove() {
    gParent.remove();
  }

}
