package com.ss.gameLogic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.platform.IPlatform;
import com.ss.GMain;
import com.ss.HTTPAssetLoader;
import com.ss.core.effect.SoundEffects;
import com.ss.core.util.GClipGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.data.WheelData;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.config.Strings;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.objects.Button;
import com.ss.gameLogic.objects.WheelMiniGame;
import com.ss.minigames.Wheel;
import com.ss.scenes.LDBFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StartScene {

  private Effect effect;
  private Game game;
  private IPlatform plf = GMain.platform;
  private Group gParent, gStartScene;

  private Group gBtnStart, gBtnRank, gBtnOtherGame;
  private Image iconTutorial, iconSetting, iconMiniGame, iconGift;

  private Group gPanelTutorial;
  private Image btnXPanelTutorial, blackTutorial;

  private Group gPanelBet;
  private Image blackPanelBet, arrLeft, arrRight, btnXPanelBet, flareChip;
  private Button btnStartPanelBet;
  private Label lbNumPlayer, lbMoneyPresent;

  public Group gPanelSetting;
  public Image blackSetting, iconSound, iconMusic;

  private Group gMiniGame;
  private Image blackMiniGame, btnXMiniGame;
  private int countSpin = 0;
  private Label lbMoneySpin, lbTitle;

  private Group gRank;
  private Image blackRank, btnXRank;

  private Group gCrossPanel;
  private Image blackCrossPanel, btnXCrossPanel;
  private List<Group> lsBgIcon;

  private Group gAlertAdsDonateStart;
  private Image blackDonateStart;

  private int numOfPlayer = 6;
  private long moneyBet = 20000;

  //////////////////////////////////////////////////////////////////////////////////////////////////

  public StartScene(Game game, Group gParent) {

    this.effect = Effect.getInstance(game);
    this.game = game;
    this.gParent = gParent;
    this.gStartScene = new Group();
    this.gStartScene.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    this.gStartScene.setOrigin(Align.center);

    SoundEffects.startMusic();

    initUI();
    initIcon();
    initPanelTutorial();
    initPanelBet();
    initMiniGame();
    initPanelSetting();
    initRank();
    initCrossPanel();
    initDonateStart();
    handleClick();
    handleClickIcon();

    setMoneyForLb();

//    testBitmap();

//    int idNotify = plf.GetNotifyId();

  }

  private void testBitmap() {

//    BitmapFont bitmap = Config.ALERT_FONT;
//    BitmapFont.Glyph glyph = bitmap.getData().getGlyph('A');
//    Sprite s = new Sprite(bitmap.getRegion().getTexture(), glyph.srcX, glyph.srcY, glyph.width, glyph.height);
//    s.flip(false, true);
//
//    System.out.println(glyph.srcX + "  " + glyph.srcY + "  " + glyph.width + "  " + glyph.height);
//
//    System.out.println(bitmap.getRegion().getRegionX() + " " +
//            bitmap.getRegion().getRegionX() + " " +
//            bitmap.getRegion().getRegionWidth() + " " +
//            bitmap.getRegion().getRegionHeight());
//
//    Image i = new Image(s);
//
//    game.gTest.addActor(i);

  }

  private void initDonateStart() {

    blackDonateStart = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackDonateStart.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gAlertAdsDonateStart = new Group();
    Image donateStart = GUI.createImage(GMain.liengAtlas, "panel_ads");
    gAlertAdsDonateStart.setSize(donateStart.getWidth(), donateStart.getHeight());
    gAlertAdsDonateStart.setOrigin(Align.center);
    gAlertAdsDonateStart.setPosition(GStage.getWorldWidth()/2 - gAlertAdsDonateStart.getWidth()/2,
            GStage.getWorldHeight()/2 - gAlertAdsDonateStart.getHeight()/2);
    gAlertAdsDonateStart.addActor(donateStart);

    String moneyDonate = Logic.getInstance().convertMoneyBet(Config.ADS_DONATE_START);
    Label lbDonateStart = new Label(C.lang.adsDonateStart + "\n" + moneyDonate, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbDonateStart.setAlignment(Align.center);
    lbDonateStart.setFontScale(1f);
    lbDonateStart.setPosition(donateStart.getX() + donateStart.getWidth()/2 - lbDonateStart.getWidth()/2,
            donateStart.getY() + donateStart.getHeight()/2 - lbDonateStart.getHeight()/2 - 150);
    gAlertAdsDonateStart.addActor(lbDonateStart);

    Button btnOK = new Button(GMain.startSceneAtlas, "btn_get", C.lang.yes, Config.BUTTON_FONT);
    btnOK.setPosition(donateStart.getX() + donateStart.getWidth()/2 - btnOK.getWidth()/2,
            donateStart.getY() + donateStart.getHeight() - btnOK.getHeight() - 5);
    btnOK.addToGroup(gAlertAdsDonateStart);

    Image btnX = GUI.createImage(GMain.liengAtlas, "btn_x");
    btnX.setPosition(donateStart.getX() + donateStart.getWidth() - btnX.getWidth()/2,
            donateStart.getX() - btnX.getHeight()/2);
    gAlertAdsDonateStart.addActor(btnX);

    gAlertAdsDonateStart.setScale(0);

    //label: click
    btnOK.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          btnOK.setTouchable(Touchable.enabled);
          if (plf.isVideoRewardReady())
            plf.ShowVideoReward((boolean success) -> {

              if (success) {

                long money = GMain.pref.getLong("money") + Config.ADS_DONATE_START;
                Logic.getInstance().saveMoney(money);

                effect.zoomOut(gAlertAdsDonateStart, 2f, 2f, () -> {
                  blackDonateStart.remove();
                  gAlertAdsDonateStart.remove();
                });

              }
              else
                game.gamePlayUI.showAlertFailNetwork();

            });
          else
            game.gamePlayUI.showAlertFailNetwork();

        };

        SoundEffects.startSound("btn_click");

        btnOK.setTouchable(Touchable.disabled);
        effect.click(btnOK, run);

      }
    });

    btnX.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        btnX.setTouchable(Touchable.disabled);
        effect.zoomOut(gAlertAdsDonateStart, 2f, 2f, () -> {
          btnX.setTouchable(Touchable.enabled);
          blackDonateStart.remove();
          gAlertAdsDonateStart.remove();
        });

      }
    });

  }

  private void initRank() {

    blackRank = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackRank.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gRank = new Group();
    Image bgRank = GUI.createImage(GMain.startSceneAtlas, "bg_rank");
    gRank.setSize(bgRank.getWidth(), bgRank.getHeight());
    gRank.setOrigin(Align.center);
    gRank.setPosition(GStage.getWorldWidth()/2 - gRank.getWidth()/2, GStage.getWorldHeight()/2 - gRank.getHeight()/2);
    gRank.addActor(bgRank);

    btnXRank = GUI.createImage(GMain.startSceneAtlas, "icon_exit");
    btnXRank.setScale(1.3f);
    btnXRank.setPosition(20, 20);

    Image banner = GUI.createImage(GMain.startSceneAtlas, "banner_rank");
    banner.setPosition(gRank.getWidth()/2 - banner.getWidth()/2, -banner.getHeight()/2);

    Group gPlayer = new Group();
    gPlayer.setSize(bgRank.getWidth() - 50, bgRank.getHeight()/2 + 230);
    gPlayer.setPosition(gRank.getWidth()/2 - gPlayer.getWidth()/2,
                        gRank.getHeight() - gPlayer.getHeight() - 25);
    gPlayer.setOrigin(Align.center);
    gPlayer.setScale(1, -1);
    gRank.addActor(gPlayer);
    gRank.addActor(banner);

    Table scroll = new Table();
    for (int i=0; i < 100; i++) {

      Group gItem = new Group();
      gItem.setSize(gPlayer.getWidth() - 100, 133);
      gItem.setPosition(gPlayer.getWidth()/2 - gItem.getWidth()/2, 0);

      Image iconRank;
      int id = i + 1;

      //label: label
      Label lbRank = new Label(id + "", new Label.LabelStyle(Config.ALERT_FONT, null));
      Label lbName = new Label("player " + id, new Label.LabelStyle(Config.ALERT_FONT, null));
      Label lbMoney = new Label("2M", new Label.LabelStyle(Config.ALERT_FONT, null));

      if (i < 3)
        iconRank = GUI.createImage(GMain.startSceneAtlas, "icon_rank_" + id);
      else
        iconRank = GUI.createImage(GMain.startSceneAtlas, "icon_rank_4");
      iconRank.setPosition(50, 0);
      gItem.addActor(iconRank);

      if (i == 0)
        setColorRank(Color.GOLD, i, lbRank, lbName, lbMoney);
      else if (i == 1)
        setColorRank(Color.LIGHT_GRAY, i, lbRank, lbName, lbMoney);
      else if (i == 2)
        setColorRank(Color.BROWN, i, lbRank, lbName, lbMoney);

      //label: background rank
      Image bgIconRank = GUI.createImage(GMain.startSceneAtlas, "bg_icon_rank");
      bgIconRank.setPosition(iconRank.getX(), iconRank.getY());
      gItem.addActor(bgIconRank);

      lbRank.setPosition(iconRank.getX() + iconRank.getWidth()/2 - lbRank.getWidth()/2,
                            iconRank.getY() + iconRank.getHeight()/2 - lbRank.getHeight()/2 + 25);
      lbRank.setAlignment(Align.center);
      lbRank.setFontScale(1, -1);
      gItem.addActor(lbRank);

      lbName.setAlignment(Align.left);
      lbName.setPosition(iconRank.getX() + iconRank.getWidth() + 50,
                          bgIconRank.getY() + bgIconRank.getHeight()/2 - lbRank.getHeight()/2 + 20);
      lbName.setFontScale(1, -1);
      gItem.addActor(lbName);

      lbMoney.setAlignment(Align.right);
      lbMoney.setPosition(bgIconRank.getX() + bgIconRank.getWidth() - lbMoney.getWidth() - 170,
                          lbName.getY());
      lbMoney.setFontScale(1, -1);
      gItem.addActor(lbMoney);

      scroll.add(gItem).padBottom(10);
      scroll.row();

    }

    ScrollPane scrollPane = new ScrollPane(scroll);
    Table table = new Table();
    table.setFillParent(true);
    table.add(scrollPane).fill().expand();
    gPlayer.addActor(table);
    gRank.setScale(0);

    btnXRank.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          btnXRank.setTouchable(Touchable.enabled);
          gRank.remove();
          blackRank.remove();

        };

        SoundEffects.startSound("btn_click");

        btnXRank.setTouchable(Touchable.disabled);
        btnXRank.remove();
        effect.zoomOut(gRank, 2f, 2f, run);

      }
    });

  }

  private void initWheel() {

    chkDayToDonateSpin();

    try {

      List<WheelData> datas = new ArrayList<>();
      JsonReader jReader = new JsonReader();
      JsonValue jValue = jReader.parse(Config.wheelData);

      for (JsonValue value : jValue) {
        String region = value.get("region").asString();
        int id = value.get("id").asInt();
        int qty = value.get("qty").asInt();
        String qtyText = value.get("qty_text").asString();
        int percent = value.get("percent").asInt();

        datas.add(new WheelData(region, id, qty, qtyText, percent));
      }

      System.out.println("JSON " + datas.size());

      WheelMiniGame wheel = WheelMiniGame.getInstance(datas);
      wheel.setScale(.8f, .8f);
      wheel.setPosition(GStage.getWorldWidth()/2 - wheel.getWidth()/2, GStage.getWorldHeight()/2 - wheel.getHeight()/2 + 30);
      wheel.addToScene(gMiniGame);

      Group gLbMoneyWheel = new Group();

      lbMoneySpin = new Label("Bạn nhận được: ", new Label.LabelStyle(Config.PLUS_MONEY_FONT, null));
      lbMoneySpin.setFontScale(1.5f);
      lbMoneySpin.setAlignment(Align.center);
//      lbMoneySpin.setPosition(GStage.getWorldWidth()/2 - lbMoneySpin.getWidth()/2,
//                              GStage.getWorldHeight()/2 - lbMoneySpin.getHeight()/2);

      Label lbRemain = new Label(C.lang.remain + " " + countSpin, new Label.LabelStyle(Config.ALERT_FONT, null));
      lbRemain.setAlignment(Align.center);
      lbRemain.setFontScale(.7f);
      lbRemain.setPosition(GStage.getWorldWidth()/2 - lbRemain.getWidth()/2,
                              GStage.getWorldHeight() - lbRemain.getHeight() - 50);
      gMiniGame.addActor(lbRemain);

      gLbMoneyWheel.addActor(lbMoneySpin);
      gLbMoneyWheel.setSize(lbMoneySpin.getWidth(), lbMoneySpin.getHeight());
      gLbMoneyWheel.setOrigin(Align.center);
      gLbMoneyWheel.setPosition(GStage.getWorldWidth()/2 - lbMoneySpin.getWidth()/2,
              GStage.getWorldHeight()/2 - lbMoneySpin.getHeight()/2);
      gLbMoneyWheel.setScale(0);

      //label: panel ads to get countSpin
      Group gAdsSpin = new Group();
      Image panel = GUI.createImage(GMain.liengAtlas, "panel_ads");
      gAdsSpin.setSize(panel.getWidth(), panel.getHeight());
      gAdsSpin.setOrigin(Align.center);
      gAdsSpin.setPosition(GStage.getWorldWidth()/2 - gAdsSpin.getWidth()/2, GStage.getWorldHeight()/2 - gAdsSpin.getHeight()/2);
      gAdsSpin.addActor(panel);

      Label lbAds = new Label(C.lang.timeSpinWheelAds, new Label.LabelStyle(Config.ALERT_FONT, null));
      lbAds.setAlignment(Align.center);
      lbAds.setFontScale(1.2f);
      lbAds.setPosition(gAdsSpin.getWidth()/2 - lbAds.getWidth()/2,
              gAdsSpin.getHeight()/2 - lbAds.getHeight()/2 - 170);
      gAdsSpin.addActor(lbAds);

      Button btnGet = new Button(GMain.startSceneAtlas, "btn_get", C.lang.yes, Config.ALERT_FONT);
//      btnGet.setFontScale(1f, 1f);
      btnGet.setPosition(gAdsSpin.getWidth()/2 - btnGet.getWidth()/2,
              gAdsSpin.getHeight() - btnGet.getHeight() - 10);
      btnGet.moveByLb(0, -5);
      btnGet.addToGroup(gAdsSpin);

      Image btnXAdsSpin = GUI.createImage(GMain.liengAtlas, "btn_x");
      btnXAdsSpin.setPosition(panel.getX() + panel.getWidth() - btnXAdsSpin.getWidth()/2,
              panel.getX() - btnXAdsSpin.getHeight()/2);
      btnXAdsSpin.setOrigin(Align.center);
      gAdsSpin.addActor(btnXAdsSpin);

      gAdsSpin.setScale(0); //scale group parent

      //label: add listener
      btnGet.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          super.clicked(event, x, y);

          //todo: check ads in platform, visible lbtitle, lbRemain, enable btnXMiniGame
          Runnable run = () -> {

            btnGet.setTouchable(Touchable.enabled);
            if (plf.isVideoRewardReady())
              plf.ShowVideoReward((boolean success) -> {

                if (success) {
                  countSpin += 1;
                  Logic.getInstance().saveSpin(countSpin);
                  lbRemain.setText(C.lang.remain + " " + countSpin);

                  lbTitle.setVisible(true);
                  lbRemain.setVisible(true);

                  effect.sclMaxToMin(gAdsSpin, () -> btnXMiniGame.setTouchable(Touchable.enabled));
                }
                else
                  game.gamePlayUI.showAlertFailNetwork();

              });
            else
              game.gamePlayUI.showAlertFailNetwork();

          };

          SoundEffects.startSound("btn_click");

          btnGet.setTouchable(Touchable.disabled);
          effect.click(btnGet, run);

        }
      });

      btnXAdsSpin.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          super.clicked(event, x, y);

          SoundEffects.startSound("btn_click");

          lbTitle.setVisible(true);
          lbRemain.setVisible(true);

          effect.sclMaxToMin(gAdsSpin, () -> btnXMiniGame.setTouchable(Touchable.enabled));

        }
      });

      //label: drag listener wheel
      wheel.addListener(new Wheel.EventListener() {
        @Override
        public boolean start() {

          if (countSpin > 0) {

            int t = countSpin - 1;
            lbRemain.setText(C.lang.remain + " " + t);
            btnXMiniGame.setTouchable(Touchable.disabled);

            return true;

          }
          else {

            lbRemain.setText(C.lang.remain + " " + 0);
            lbTitle.setVisible(false);
            lbRemain.setVisible(false);
            btnXMiniGame.setTouchable(Touchable.disabled);

            gMiniGame.addActor(gAdsSpin);
            effect.sclMinToMax(gAdsSpin);

            return false;
          }
        }

        @Override
        public void end(Wheel.WheelItem item) {

          countSpin -= 1;
          Logic.getInstance().saveSpin(countSpin);

          effect.sclMinToMax(gLbMoneyWheel);
          game.gamePlayUI.pMoneyWheel.start(GStage.getWorldWidth()/2, GStage.getWorldHeight()/2, .8f);

          //save money
          long moneyy = GMain.pref.getLong("money");
          moneyy += item.getQty();
          Logic.getInstance().saveMoney(moneyy);

          btnXMiniGame.setTouchable(Touchable.enabled);
          String money = Logic.getInstance().convertMoneyBet(item.getQty());
          lbMoneySpin.setText("+" + money);
          gMiniGame.addActor(gLbMoneyWheel);

          game.gEffect.addAction(
                  sequence(
                          delay(3f),
                          run(() -> {
                            game.gamePlayUI.pMoneyWheel.remove();
                            effect.sclMaxToMin(gLbMoneyWheel, () -> {});
                          })
                  )
          );

        }

        @Override
        public void error(String msg) {

          System.out.println("ERR");

        }
      });

    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void initMiniGame() {

    blackMiniGame = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackMiniGame.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gMiniGame = new Group();
    gMiniGame.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    initWheel();

    lbTitle = new Label(C.lang.titleMiniGame, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbTitle.setFontScale(.8f);
    lbTitle.setAlignment(Align.center);
    lbTitle.setPosition(gMiniGame.getWidth()/2 - lbTitle.getWidth()/2, 20);
    gMiniGame.addActor(lbTitle);

    btnXMiniGame = GUI.createImage(GMain.startSceneAtlas, "icon_exit");
    btnXMiniGame.setPosition(20, 20);
    gMiniGame.addActor(btnXMiniGame);

    gMiniGame.setOrigin(Align.center);
    gMiniGame.setScale(0);

    btnXMiniGame.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        game.gamePlayUI.pMoneyWheel.remove(); //remove particle
        effect.zoomOut(gMiniGame, 2f, 2f, () -> {
          blackMiniGame.remove();
          gMiniGame.remove();
        });

      }
    });

  }

  private void initPanelSetting() {

    blackSetting = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackSetting.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gPanelSetting = new Group();
    Image bgPanelSetting = GUI.createImage(GMain.startSceneAtlas, "panel_setting");
    gPanelSetting.setSize(bgPanelSetting.getWidth(), bgPanelSetting.getHeight());
    gPanelSetting.setOrigin(Align.center);
    gPanelSetting.setPosition(GStage.getWorldWidth()/2 - gPanelSetting.getWidth()/2, GStage.getWorldHeight()/2 - gPanelSetting.getHeight()/2);
    gPanelSetting.addActor(bgPanelSetting);
    gPanelSetting.setScale(0f);

    Label lbTitle = new Label(C.lang.titleSetting, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbTitle.setAlignment(Align.center);
    lbTitle.setFontScale(1.2f);
    lbTitle.setPosition(gPanelSetting.getWidth()/2 - lbTitle.getWidth()/2, 50);
    gPanelSetting.addActor(lbTitle);

    iconMusic = GUI.createImage(GMain.startSceneAtlas, "music_on");
    iconMusic.setPosition(gPanelSetting.getWidth()/2 - iconMusic.getWidth() - 130,
            gPanelSetting.getHeight()/2 - iconMusic.getHeight()/2);
    gPanelSetting.addActor(iconMusic);

    Label lbMusic = new Label(C.lang.music, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbMusic.setAlignment(Align.center);
    lbMusic.setFontScale(.6f);
    lbMusic.setPosition(iconMusic.getX() + iconMusic.getWidth()/2 - lbMusic.getWidth()/2,
                        iconMusic.getY() + iconMusic.getHeight() + lbMusic.getHeight()/2);
    gPanelSetting.addActor(lbMusic);

    iconSound = GUI.createImage(GMain.startSceneAtlas, "sound_on");
    iconSound.setPosition(gPanelSetting.getWidth()/2 + iconMusic.getWidth()/2 + 50,
            gPanelSetting.getHeight()/2 - iconSound.getHeight()/2);
    gPanelSetting.addActor(iconSound);

    Label lbSound = new Label(C.lang.sound, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbSound.setAlignment(Align.center);
    lbSound.setFontScale(.6f);
    lbSound.setPosition(iconSound.getX() + iconSound.getWidth()/2 - lbMusic.getWidth()/2,
            iconSound.getY() + iconSound.getHeight() + lbSound.getHeight()/2);
    gPanelSetting.addActor(lbSound);

    Image btnX = GUI.createImage(GMain.liengAtlas, "btn_x");
    btnX.setPosition(gPanelSetting.getWidth() - btnX.getWidth()/2, -btnX.getHeight()/2);
    gPanelSetting.addActor(btnX);

    //label: event click
    iconMusic.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        if (!SoundEffects.isMuteMusic) {
          SoundEffects.isMuteMusic = true;
          iconMusic.setDrawable(new TextureRegionDrawable(GMain.startSceneAtlas.findRegion("music_off")));
        }
        else {
          SoundEffects.isMuteMusic = false;
          iconMusic.setDrawable(new TextureRegionDrawable(GMain.startSceneAtlas.findRegion("music_on")));
        }

        SoundEffects.startSound("btn_click");
        SoundEffects.startMusic();

      }
    });

    iconSound.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        if (!SoundEffects.isMuteSound) {
          SoundEffects.isMuteSound = true;
          iconSound.setDrawable(new TextureRegionDrawable(GMain.startSceneAtlas.findRegion("sound_off")));
        }
        else {
          SoundEffects.isMuteSound = false;
          iconSound.setDrawable(new TextureRegionDrawable(GMain.startSceneAtlas.findRegion("sound_on")));
        }

        SoundEffects.startSound("btn_click");

      }
    });

    btnX.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        btnX.setTouchable(Touchable.disabled);
        effect.sclMaxToMin(gPanelSetting, () -> {
          btnX.setTouchable(Touchable.enabled);
          gPanelSetting.remove();
          blackSetting.remove();
        });

      }
    });

  }

  private void initPanelBet() {

    blackPanelBet = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackPanelBet.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gPanelBet = new Group();
    Image bgPanel = GUI.createImage(GMain.startSceneAtlas, "panel_bet");
    gPanelBet.setSize(bgPanel.getWidth(), bgPanel.getHeight());
    gPanelBet.setOrigin(bgPanel.getWidth()/2, bgPanel.getHeight()/2);
    gPanelBet.setPosition(GStage.getWorldWidth()/2 - bgPanel.getWidth()/2, GStage.getWorldHeight()/2 - bgPanel.getHeight()/2);
    gPanelBet.addActor(bgPanel);

    //label: button start
    btnStartPanelBet = new Button(GMain.startSceneAtlas,"btn_start_panel_bet", C.lang.startPanelBet, Config.ALERT_FONT);
    btnStartPanelBet.setFontScale(.6f, .6f);
    btnStartPanelBet.setPosition(15, gPanelBet.getHeight() - btnStartPanelBet.getHeight() - 40);
    btnStartPanelBet.moveByLb(-5, 0);

    Image flareStart = GUI.createImage(GMain.startSceneAtlas, "flare");
    flareStart.setPosition(btnStartPanelBet.getX() + btnStartPanelBet.getWidth()/2 - flareStart.getWidth()/2,
                            btnStartPanelBet.getY() + btnStartPanelBet.getHeight()/2 - flareStart.getHeight()/2);
    flareStart.setOrigin(Align.center);
    effect.rotate(flareStart);
    gPanelBet.addActor(flareStart);
    btnStartPanelBet.addToGroup(gPanelBet);

    //label: choose number of player
    Image bgChooseNumPlayer = GUI.createImage(GMain.startSceneAtlas, "bg_panel_bet_number");
    bgChooseNumPlayer.setPosition(bgPanel.getWidth() - bgChooseNumPlayer.getWidth() - 20, 250);
    gPanelBet.addActor(bgChooseNumPlayer);

    Label lbTxtNumPlayer = new Label(C.lang.players, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbTxtNumPlayer.setFontScale(.7f, .7f);
    lbTxtNumPlayer.setAlignment(Align.center);
    lbTxtNumPlayer.setPosition(-20, bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY()/2 - lbTxtNumPlayer.getHeight()/2 - 5);
    gPanelBet.addActor(lbTxtNumPlayer);

    arrLeft = GUI.createImage(GMain.startSceneAtlas, "arrow_left");
    arrLeft.setOrigin(Align.center);
    arrLeft.setPosition(bgChooseNumPlayer.getX() + arrLeft.getWidth()*arrLeft.getScaleX()/2 - 15,
                            bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY()/2 - arrLeft.getHeight()*arrLeft.getScaleY()/2);
    gPanelBet.addActor(arrLeft);

    arrRight = GUI.createImage(GMain.startSceneAtlas, "arrow_right");
    arrRight.setPosition(bgChooseNumPlayer.getX() + bgChooseNumPlayer.getWidth()*bgChooseNumPlayer.getScaleX() - arrRight.getWidth() - 5,
            arrLeft.getY());
    arrRight.setOrigin(Align.center);
    gPanelBet.addActor(arrRight);

    lbNumPlayer = new Label("6", new Label.LabelStyle(Config.ALERT_FONT, null));
    lbNumPlayer.setFontScale(1.1f);
    lbNumPlayer.setAlignment(Align.center);
    lbNumPlayer.setPosition(bgChooseNumPlayer.getX() + bgChooseNumPlayer.getWidth()*bgChooseNumPlayer.getScaleX()/2 - lbNumPlayer.getWidth()/2,
            bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY()/2 - lbNumPlayer.getHeight()/2 - 5);
    gPanelBet.addActor(lbNumPlayer);

    //label: button x
    btnXPanelBet = GUI.createImage(GMain.startSceneAtlas, "icon_exit");
    btnXPanelBet.setPosition(20, 20);

    //label: money player
    Image bgMoneyPlayer = GUI.createImage(GMain.startSceneAtlas, "bg_panel_bet_number");
    bgMoneyPlayer.setPosition(bgChooseNumPlayer.getX(), bgChooseNumPlayer.getY() - bgMoneyPlayer.getHeight()*bgMoneyPlayer.getScaleY() - 20);
    gPanelBet.addActor(bgMoneyPlayer);

    Label lbMoneyPlayer = new Label(C.lang.moneyPlayer, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbMoneyPlayer.setFontScale(.7f, .7f);
    lbMoneyPlayer.setAlignment(Align.center);
    lbMoneyPlayer.setPosition(lbTxtNumPlayer.getX() - 10,
                              bgMoneyPlayer.getY() + bgMoneyPlayer.getHeight()*bgMoneyPlayer.getScaleY()/2 - lbMoneyPlayer.getHeight()/2 - 5);
    gPanelBet.addActor(lbMoneyPlayer);

    lbMoneyPresent = new Label("$32,000", new Label.LabelStyle(Config.ALERT_FONT, null));
    lbMoneyPresent.setFontScale(.9f);
    lbMoneyPresent.setAlignment(Align.center);
    lbMoneyPresent.setPosition(bgMoneyPlayer.getX() + bgMoneyPlayer.getWidth()*bgMoneyPlayer.getScaleX()/2 - lbMoneyPresent.getWidth()/2,
                                bgMoneyPlayer.getY() + bgMoneyPlayer.getHeight()*bgMoneyPlayer.getScaleY()/2 - lbMoneyPresent.getHeight()/2);
    gPanelBet.addActor(lbMoneyPresent);

    //label: bet
    Image bgChooseBet = GUI.createImage(GMain.startSceneAtlas, "bg_panel_bet_chip");
    bgChooseBet.setPosition(bgChooseNumPlayer.getX() + bgChooseNumPlayer.getWidth()*bgChooseNumPlayer.getScaleX() - bgChooseBet.getWidth()*bgChooseBet.getScaleX(),
                              bgChooseNumPlayer.getY() + bgChooseNumPlayer.getHeight()*bgChooseNumPlayer.getScaleY() + 20);
    gPanelBet.addActor(bgChooseBet);

    Label lbTxtBet = new Label(C.lang.bet, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbTxtBet.setFontScale(.7f, .7f);
    lbTxtBet.setAlignment(Align.left);
    lbTxtBet.setPosition(lbTxtNumPlayer.getX() + 40, bgChooseBet.getY() - lbTxtBet.getHeight()/2 + 20);
    gPanelBet.addActor(lbTxtBet);

    flareChip = GUI.createImage(GMain.liengAtlas, "flare_chip");
    gPanelBet.addActor(flareChip);

    int t = -1;
    for (int i=0; i<3; i++) {
      for (int j=0; j<2; j++) {
        t++;
        String region = Logic.getInstance().getRegionChip(t);
        Image chip = GUI.createImage(GMain.liengAtlas, region);
        chip.setScale(1.2f);

        if (j % 2 == 0)
          chip.setPosition(bgChooseBet.getX() + bgChooseBet.getWidth()/2 - chip.getWidth() - 50,
                  bgChooseBet.getY() + 10 + i*80);
        else
          chip.setPosition(bgChooseBet.getX() + bgChooseBet.getWidth()/2 + chip.getWidth() - 20,
                  bgChooseBet.getY() + 10 + i*80);

        clickChipBet(chip, region);
        gPanelBet.addActor(chip);

        if (i == 0)
          flareChip.setPosition(chip.getX() + chip.getWidth()*1.2f/2 - flareChip.getWidth()/2,
                  chip.getY() + chip.getHeight()*1.2f/2 - flareChip.getHeight()/2);
      }

    }

  }

  private void clickChipBet(Image chip, String id) {
    chip.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        moneyBet = Logic.getInstance().getMoneyBuyId(id);
        flareChip.setPosition(chip.getX() + chip.getWidth()*1.2f/2 - flareChip.getWidth()/2,
                chip.getY() + chip.getHeight()*1.2f/2 - flareChip.getHeight()/2);


      }
    });
  }

  private void initPanelTutorial() {

    //label: panel tutorial
    blackTutorial = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackTutorial.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gPanelTutorial = new Group();
    Image panelTutorial = GUI.createImage(GMain.liengAtlas, "panel_ads");
    gPanelTutorial.setSize(panelTutorial.getWidth(), panelTutorial.getHeight());
    gPanelTutorial.setPosition(GStage.getWorldWidth()/2 - panelTutorial.getWidth()/2, GStage.getWorldHeight()/2 - panelTutorial.getHeight()/2);
    gPanelTutorial.setOrigin(Align.center);
    gPanelTutorial.addActor(panelTutorial);

    Group gLb = new Group();
    gLb.setSize(panelTutorial.getWidth() - 120, panelTutorial.getHeight() - 32);
    gLb.setPosition(panelTutorial.getWidth()/2 - gLb.getWidth()/2, panelTutorial.getHeight()/2 - gLb.getHeight()/2);
    gLb.setOrigin(Align.center);
    gLb.setScale(1, -1);

    Label lbChkWidth = new Label("", new Label.LabelStyle(Config.ALERT_FONT, null));
    lbChkWidth.setAlignment(Align.center);
    lbChkWidth.setFontScale(.55f);

    StringBuffer sBuff = new StringBuffer();
    Table scroll = new Table();
    for (int index=0; index<Strings.aaa.length; index++) {

      String[] s = Strings.aaa[index].split(" ");

      if (index == 0) {
        Group g = createLb(gLb, Strings.aaa[index], scroll, 0);
        scroll.add(g).padBottom(80).padTop(30);
      }
      else {

        int i = 0;
        int count = 0;
        for (String ss : s) {
          i += 1;
          count += 1;

          sBuff.append(ss).append(" ");

          if (i >= 12) {

            Group g = createLb(gLb, sBuff.toString(), scroll, 1);
            sBuff.delete(0, sBuff.length());
            i=0;

            scroll.add(g).padBottom(30);

          }
          else if (count == s.length) {
            sBuff.append("\n").append("\n").append("\n");
            Group g = createLb(gLb, sBuff.toString(), scroll, 1);
            sBuff.delete(0, sBuff.length());

            scroll.add(g).padTop(40);

          }

          scroll.row();
        }

      }

    }

    ScrollPane scrollPane = new ScrollPane(scroll);
    Table table = new Table();
    table.setFillParent(true);
    table.add(scrollPane).fill().expand();
    gLb.addActor(table);

    btnXPanelTutorial = GUI.createImage(GMain.liengAtlas, "btn_x");
    btnXPanelTutorial.setPosition(gPanelTutorial.getWidth() - btnXPanelTutorial.getWidth()/2 - 20, -50);
    gPanelTutorial.addActor(btnXPanelTutorial);

    gPanelTutorial.addActor(gLb);
    gPanelTutorial.setScale(0);

  }

  private Group createLb(Group gLb, String string, Table scroll, int align) {

    Group g = new Group();
    g.setSize(gLb.getWidth(), 30);
    g.setOrigin(Align.center);
    g.setScale(1f, -1f);

    Label lbTutorial = new Label(string, new Label.LabelStyle(Config.ALERT_FONT, null));
    if (align == 0) {
      lbTutorial.setAlignment(Align.center);
      lbTutorial.setFontScale(1.2f);
      lbTutorial.setPosition(g.getWidth()/2 - lbTutorial.getWidth()/2, lbTutorial.getY());
    }
    else {
      lbTutorial.setFontScale(.6f);
      lbTutorial.setAlignment(Align.left);
    }

    g.addActor(lbTutorial);
    return g;

  }

  private void initUI() {

    Image bgStart = GUI.createImage(GMain.startSceneAtlas, "bg_start_game");
    bgStart.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    gStartScene.addActor(bgStart);

    Image logo = GUI.createImage(GMain.startSceneAtlas, "logo");
    logo.setPosition(50, GStage.getWorldHeight()/2 - logo.getHeight()/2);
    gStartScene.addActor(logo);

    //label: button startScene
    gBtnStart = new Group();
    Image btnStart = GUI.createImage(GMain.startSceneAtlas, "btn_start");
    gBtnStart.addActor(btnStart);
    gBtnStart.setSize(btnStart.getWidth(), btnStart.getHeight());
    gBtnStart.setOrigin(Align.center);
    gBtnStart.setPosition(GStage.getWorldWidth()-btnStart.getWidth()-40, 200);

    Label lbStart = new Label(C.lang.startScene, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbStart.setFontScale(.8f);
    lbStart.setAlignment(Align.center);
    lbStart.setPosition(btnStart.getX() + btnStart.getWidth()/2 - lbStart.getWidth()/2 + 25,
                        btnStart.getY() + btnStart.getHeight()/2 - lbStart.getHeight()/2 - 5);
    gBtnStart.addActor(lbStart);

    GClipGroup gClipStart = new GClipGroup();
    gClipStart.setClipArea(btnStart.getX(), btnStart.getY(), btnStart.getWidth() - 50, btnStart.getHeight() - 35);
    gClipStart.setPosition(btnStart.getX() + 30, btnStart.getY() + 18);
    gBtnStart.addActor(gClipStart);

    Image lightBtnStart = GUI.createImage(GMain.startSceneAtlas, "light_button");
    lightBtnStart.setPosition(-lightBtnStart.getWidth(), 25);
    gClipStart.addActor(lightBtnStart);

    Image iconBtnStart = GUI.createImage(GMain.startSceneAtlas, "icon_btn_start");
    iconBtnStart.setPosition(-60, -15);
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
    gBtnRank.setPosition(gBtnStart.getX(), gBtnStart.getY() + gBtnRank.getHeight() + 50);

    Label lbRank = new Label(C.lang.titleRank, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbRank.setFontScale(.8f);
    lbRank.setAlignment(Align.center);
    lbRank.setPosition(btnRank.getX() + btnRank.getWidth()/2 - lbRank.getWidth()/2 + 20,
            btnRank.getY() + btnRank.getHeight()/2 - lbRank.getHeight()/2 - 5);
    gBtnRank.addActor(lbRank);

    GClipGroup gClipRank = new GClipGroup();
    gClipRank.setClipArea(btnRank.getX(), btnRank.getY(), btnRank.getWidth() - 50, btnRank.getHeight() - 35);
    gClipRank.setPosition(btnRank.getX() + 30, btnRank.getY() + 18);
    gBtnRank.addActor(gClipRank);

    Image lightBtnRank = GUI.createImage(GMain.startSceneAtlas, "light_button");
    lightBtnRank.setPosition(-lightBtnRank.getWidth(), 25);
    gClipRank.addActor(lightBtnRank);

    Image iconBtnRank = GUI.createImage(GMain.startSceneAtlas, "icon_btn_rank");
    iconBtnRank.setPosition(-40, -25);
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
    gBtnOtherGame.setPosition(gBtnRank.getX(), gBtnRank.getY() + gBtnRank.getHeight() + 50);

    Label lbOtherGame = new Label(C.lang.otherGame, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbOtherGame.setFontScale(.8f);
    lbOtherGame.setAlignment(Align.center);
    lbOtherGame.setPosition(btnOtherGame.getX() + btnOtherGame.getWidth()/2 - lbOtherGame.getWidth()/2 + 30,
            btnOtherGame.getY() + btnOtherGame.getHeight()/2 - lbOtherGame.getHeight()/2 - 5);
    gBtnOtherGame.addActor(lbOtherGame);

    GClipGroup gClipOtherGame = new GClipGroup();
    gClipOtherGame.setClipArea(btnOtherGame.getX(), btnOtherGame.getY(), btnOtherGame.getWidth() - 50, btnOtherGame.getHeight() - 35);
    gClipOtherGame.setPosition(btnOtherGame.getX() + 40, btnOtherGame.getY() + 25);
    gBtnOtherGame.addActor(gClipOtherGame);

    Image lightBtnOtherGame = GUI.createImage(GMain.startSceneAtlas, "light_button");
    lightBtnOtherGame.setPosition(-lightBtnOtherGame.getWidth(), 25);
    gClipOtherGame.addActor(lightBtnOtherGame);

    Image iconBtnOtherGame = GUI.createImage(GMain.startSceneAtlas, "icon_btn_other_game");
    iconBtnOtherGame.setPosition(-40, -20);
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
          effect.sclMinToMaxAndRotate(gPanelTutorial);

        };

        SoundEffects.startSound("btn_click");

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

        SoundEffects.startSound("btn_click");

        btnXPanelTutorial.setTouchable(Touchable.disabled);
        effect.sclMaxToMinAndRotate(gPanelTutorial, () -> {
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

        SoundEffects.startSound("btn_click");

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

        SoundEffects.startSound("btn_click");

        arrRight.setTouchable(Touchable.disabled);
        effect.click(arrRight, run);

      }
    });

    btnXPanelBet.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        btnXPanelBet.setTouchable(Touchable.disabled);
        btnXPanelBet.remove();
        effect.zoomOut(gPanelBet, 2.5f, 2.5f, () -> {

          btnXPanelBet.setTouchable(Touchable.enabled);
          gPanelBet.getColor().a = 1f;
          gPanelBet.remove();
          blackPanelBet.remove();

        });

      }
    });

    iconSetting.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {
          iconSetting.setTouchable(Touchable.enabled);
          gStartScene.addActor(blackSetting);
          gStartScene.addActor(gPanelSetting);
          effect.sclMinToMax(gPanelSetting);
        };

        SoundEffects.startSound("btn_click");

        iconSetting.setTouchable(Touchable.disabled);
        effect.click(iconSetting, run);

      }
    });

    iconMiniGame.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          iconMiniGame.setTouchable(Touchable.enabled);
          effect.zoomIn(gMiniGame, 1f, 1f);

        };

        SoundEffects.startSound("btn_click");

        iconMiniGame.setTouchable(Touchable.disabled);
        gStartScene.addActor(blackMiniGame);
        gStartScene.addActor(gMiniGame);
        effect.click(iconMiniGame, run);

      }
    });

    iconGift.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          iconGift.setTouchable(Touchable.enabled);
          effect.zoomIn(gAlertAdsDonateStart, 1f, 1f);

        };

        SoundEffects.startSound("btn_click");

        iconGift.setTouchable(Touchable.disabled);
        game.gAlert.addActor(blackDonateStart);
        game.gAlert.addActor(gAlertAdsDonateStart);
        effect.click(iconGift, run);

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
          gStartScene.addActor(blackPanelBet);
          gStartScene.addActor(btnXPanelBet);
          gStartScene.addActor(gPanelBet);
          effect.zoomIn(gPanelBet, 1f, 1f);

        };

        SoundEffects.startSound("btn_click");

        lbMoneyPresent.setText(Logic.getInstance().convertMoneyBot(GMain.pref.getLong("money")));
        gBtnStart.setTouchable(Touchable.disabled);
        Effect.getInstance(game).click(gBtnStart, run);

      }
    });

    gBtnRank.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          gBtnRank.setTouchable(Touchable.enabled);
          GMain.inst.setScreen(LDBFactory.getLDB());

        };

        SoundEffects.startSound("btn_click");

        gBtnRank.setTouchable(Touchable.disabled);
        effect.click(gBtnRank, run);

      }
    });

    gBtnOtherGame.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          gBtnOtherGame.setTouchable(Touchable.enabled);
          effect.zoomIn(gCrossPanel, 1f, 1f);

        };

        SoundEffects.startSound("btn_click");

        gBtnOtherGame.setTouchable(Touchable.disabled);
        gStartScene.addActor(blackCrossPanel);
        gStartScene.addActor(btnXCrossPanel);
        gStartScene.addActor(gCrossPanel);
        effect.click(gBtnOtherGame, run);

      }
    });

    btnStartPanelBet.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          Effect.getInstance(game).zoomOut(gStartScene, 2f, 2f, () -> {

            btnStartPanelBet.setTouchable(Touchable.enabled);
            game.setData(numOfPlayer, moneyBet);

            btnXPanelBet.remove();
            blackPanelBet.remove();
            gPanelBet.remove();
            gStartScene.remove();

          });

        };

        SoundEffects.startSound("btn_click");

        btnStartPanelBet.setTouchable(Touchable.disabled);
        game.gamePlayUI.addToScene();
        effect.click(btnStartPanelBet, run);

      }
    });

  }

  public void showStartScene() {

    game.gStartScene.addActor(gStartScene);
    effect.zoomIn(gStartScene, 1f, 1f);

    //todo: get money player in preference
    long money = GMain.pref.getLong("money");
    lbMoneyPresent.setText(Logic.getInstance().convertMoneyBot(money));

  }

  public void setMoneyForLb() {
    long money = GMain.pref.getLong("money");
    lbMoneyPresent.setText(Logic.getInstance().convertMoneyBot(money));
  }

  public void addToScene() {
    gParent.addActor(gStartScene);
  }

  public void remove() {
    gParent.remove();
  }

  private void setColorRank(Color color, int id, Label ...lb) {

    lb[0].setColor(color);
    lb[1].setColor(color);
    lb[2].setColor(color);

  }

  private void initCrossPanel() {

    lsBgIcon = new ArrayList<>();

    blackCrossPanel = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackCrossPanel.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    btnXCrossPanel = GUI.createImage(GMain.startSceneAtlas, "icon_exit");
    btnXCrossPanel.setPosition(20, 20);

    gCrossPanel = new Group();
    Image bgCross = GUI.createImage(GMain.startSceneAtlas, "bg_cross_panel");
    gCrossPanel.setSize(bgCross.getWidth(), bgCross.getHeight());
    gCrossPanel.setOrigin(Align.center);
    gCrossPanel.setPosition(GStage.getWorldWidth()/2 - gCrossPanel.getWidth()/2,
                            GStage.getWorldHeight()/2 - gCrossPanel.getHeight()/2);
    gCrossPanel.addActor(bgCross);

    for (int i=0; i<4; i++) {

      Group g = new Group();
      Image bgIcon = GUI.createImage(GMain.startSceneAtlas, "bg_icon_cross");
      g.setSize(bgIcon.getWidth(), bgIcon.getHeight());
      g.setPosition(bgCross.getX() + 40 + 215*i, bgCross.getY() + bgCross.getHeight()/2 - bgIcon.getHeight()/2);
      g.addActor(bgIcon);
      lsBgIcon.add(g);

    }

    ArrayList<HTTPAssetLoader.LoadItem> loadItems = new ArrayList<>();

    JsonReader jReader = new JsonReader();
    JsonValue jValue = jReader.parse(Config.otherGameData);

    for (JsonValue v : jValue)
      loadItems.add(HTTPAssetLoader.LoadItem.newInst(
              v.get("id").asInt(),
              v.get("url").asString(),
              v.get("display_name").asString(),
              v.get("android_store_uri").asString(),
              v.get("ios_store_uri").asString(),
              v.get("fi_store_uri").asString()
      ));

    HTTPAssetLoader.inst().init(new HTTPAssetLoader.Listener() {

      @Override
      public void finish(ArrayList<HTTPAssetLoader.LoadItem> loadedItems) {

        for (HTTPAssetLoader.LoadItem item : loadedItems){

          Group gIcon = lsBgIcon.get(loadedItems.indexOf(item));
          Image actor = new Image(new TextureRegionDrawable(item.getItemTexture()));
          actor.setOrigin(Align.center);
          actor.setSize(gIcon.getWidth(), gIcon.getHeight()/1.8f);
          actor.setScale(1f, -1f);
          actor.setPosition(0, -38);
          gIcon.addActor(actor);

          Label lbName = new Label(item.getDisplayName(), new Label.LabelStyle(Config.ALERT_FONT, null));
          lbName.setAlignment(Align.center);
          lbName.setFontScale(.5f);
          lbName.setPosition(actor.getX() + actor.getWidth()/2 - lbName.getWidth()/2,
                                actor.getY() + actor.getHeight() + 60);
          gIcon.addActor(lbName);
          gCrossPanel.addActor(gIcon);

          gIcon.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
              super.clicked(event, x, y);
              Gdx.net.openURI(item.getAndroidStoreURI());
            }
          });

        }
      }

      @Override
      public void error(Throwable e) {
        e.printStackTrace();
      }
    }, loadItems);

    gCrossPanel.setScale(0);

    btnXCrossPanel.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        btnXCrossPanel.setTouchable(Touchable.disabled);
        btnXCrossPanel.remove();
        effect.zoomOut(gCrossPanel, 2f, 2f, () -> {

          btnXCrossPanel.setTouchable(Touchable.enabled);
          gCrossPanel.remove();
          blackCrossPanel.remove();

        });

      }
    });

  }

  private void chkDayToDonateSpin() {

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
    try {

      Date dayInPre = formatter.parse(GMain.pref.getString("day"));
      Date dayNow = Calendar.getInstance().getTime();

      if (!Logic.getInstance().isSameDay(dayInPre, dayNow)) {
        countSpin = Config.SPIN_TIME;
        Logic.getInstance().saveSpin(countSpin);
        Logic.getInstance().saveDay(formatter.format(dayNow));
      }
      else
        countSpin = GMain.pref.getInteger("spin");

    } catch (ParseException e) {
      e.printStackTrace();
    }

  }

  private void resetPosLbMoneySpin() {
    lbMoneySpin.setPosition(GStage.getWorldWidth()/2 - lbMoneySpin.getWidth()/2,
            GStage.getWorldHeight()/2 - lbMoneySpin.getHeight()/2);
  }

}
