package com.ss.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.HTTPAssetLoader;
import com.ss.config.C;
import com.ss.config.Config;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import java.util.ArrayList;
import java.util.List;

public class CrossPanel extends Group {

  private Image blackScreen;

  public CrossPanel(Image blackScreen) {

    this.blackScreen = blackScreen;

    Image bgCross = GUI.createImage(GMain.popupAtlas, "popup_games");
    this.setSize(bgCross.getWidth(), bgCross.getHeight());
    this.setOrigin(Align.center);
    this.setPosition(GStage.getWorldWidth()/2 - this.getWidth()/2,
                     GStage.getWorldHeight()/2 - this.getHeight()/2);
    this.addActor(bgCross);

    this.getColor().a = 0f;
    this.setScale(0);

    Label lbTitle = new Label(C.lang.locale.get("other_games"), new Label.LabelStyle(Config.brownFont, null));
    lbTitle.setFontScale(.85f);
    lbTitle.setAlignment(Align.center);
    lbTitle.setPosition(bgCross.getX() + bgCross.getWidth()/2 - lbTitle.getWidth()/2, 40);
    this.addActor(lbTitle);

    Image decorLeft = GUI.createImage(GMain.popupAtlas, "decor_left");
    decorLeft.setPosition(bgCross.getX() + 50, 70);
    this.addActor(decorLeft);

    Image decorRight = GUI.createImage(GMain.popupAtlas, "decor_right");
    decorRight.setPosition(bgCross.getX() + bgCross.getWidth() - decorRight.getWidth() - 40, decorLeft.getY());
    this.addActor(decorRight);

    List<Group> lsBoxGames = new ArrayList<>();
    for (int i=0; i<4; i++) {
      Group gBox  = new Group();
      Image box   = GUI.createImage(GMain.popupAtlas, "box_games");
      gBox.setSize(box.getWidth(), box.getHeight());
      gBox.addActor(box);

      if (i <= 1)
        gBox.setPosition(bgCross.getX() + 50 + (box.getWidth() + 55)*i, bgCross.getY() + 170);
      else
        gBox.setPosition(bgCross.getX() + 50 + (box.getWidth() + 55)*(3-i),
                         bgCross.getY() + box.getHeight() + 220);

      lsBoxGames.add(gBox);
    }

    ArrayList<HTTPAssetLoader.LoadItem> loadItems = new ArrayList<>();
    JsonReader  jReader = new JsonReader();
    JsonValue   jValue  = jReader.parse(Config.OTHER_GAME_STRING);

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

          Group gIcon = lsBoxGames.get(loadedItems.indexOf(item));
          Image actor = new Image(new TextureRegionDrawable(item.getItemTexture()));
          actor.setOrigin(Align.center);
          actor.setSize(150, 180);
          actor.setScale(1f, -1f);
          actor.setPosition(gIcon.getWidth()/2 - actor.getWidth()/2, 8);
          gIcon.addActor(actor);
          gIcon.getChildren().get(0).setZIndex(1000);

          Label lbName = new Label(item.getDisplayName(), new Label.LabelStyle(Config.whiteFont, null));
          lbName.setAlignment(Align.center);
          lbName.setFontScale(.6f);
          lbName.setPosition(actor.getX() + actor.getWidth()/2 - lbName.getWidth()/2,
                             actor.getY() + actor.getHeight() + 20);
          gIcon.addActor(lbName);
          CrossPanel.this.addActor(gIcon);

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

    Image btnX = GUI.createImage(GMain.bgAtlas, "icon_exit");
    btnX.setOrigin(Align.center);
    btnX.setPosition(bgCross.getX() + bgCross.getWidth() - btnX.getWidth() + 60, -50);
    this.addActor(btnX);

    btnX.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        btnX.setTouchable(Touchable.disabled);
        blackScreen.remove();
        animEscape(btnX);

      }
    });

  }

  private void animEscape(Image btn) {
    this.addAction(
            sequence(
                    parallel(
                            scaleTo(0f, 0f, .75f, fastSlow),
                            alpha(0f, .2f, linear)
                    ),
                    run(() -> {
                      btn.setTouchable(Touchable.enabled);
                      this.remove();
                    })
            )
    );
  }

}
