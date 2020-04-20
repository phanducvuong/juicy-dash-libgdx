package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ss.HTTPAssetLoader;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.gameLogic.Game;

import java.util.ArrayList;

public class GameScene extends GScreen {

    @Override
    public void dispose() {

    }

    @Override
    public void init() {

      new Game();

      //////////cross assets loading///////////////
      ArrayList<HTTPAssetLoader.LoadItem> loadItems = new ArrayList<>();

      loadItems.add(HTTPAssetLoader.LoadItem.newInst(
              0,
              "https://lh3.googleusercontent.com/DVCX6D5_giH643Kyocc3p-5nVIdLDBjrKUS0ruKJfcpMyys4ImuRScfutdg7fQYgKkRV=s360",
              "Rival Stars Horse Racing",
              "https://play.google.com/store/apps/details?id=com.pikpok.hrc.play",
              "https://play.google.com/store/apps/details?id=com.pikpok.hrc.play",
              "https://play.google.com/store/apps/details?id=com.pikpok.hrc.play"));

      loadItems.add(HTTPAssetLoader.LoadItem.newInst(
              1,
              "https://lh3.googleusercontent.com/QL12zTgp1lezmat5uk555GzuPy_qAA6UkE_yi4kpNGrcueVho9dQ-CnAuiYTf9h0Wii0=s360",
              "Cube Rush Adventure",
              "https://play.google.com/store/apps/details?id=com.ilyon.cuberush",
              "https://play.google.com/store/apps/details?id=com.ilyon.cuberush",
              "https://play.google.com/store/apps/details?id=com.ilyon.cuberush"));

      loadItems.add(HTTPAssetLoader.LoadItem.newInst(
              2,
              "https://lh3.googleusercontent.com/ze3k0oULXqoTbFeV_6pql3wg2fw8W7yRMxFgrTYvyV49Kq8IIoV_XKZQahPMXX_2oHA=s360",
              "Pirate Treasures - Gems Puzzle",
              "https://play.google.com/store/apps/details?id=com.orangeapps.piratetreasure",
              "https://play.google.com/store/apps/details?id=com.orangeapps.piratetreasure",
              "https://play.google.com/store/apps/details?id=com.orangeapps.piratetreasure"));

      loadItems.add(HTTPAssetLoader.LoadItem.newInst(
              3,
              "https://crossgames.s3-ap-southeast-1.amazonaws.com/golf.jpg",
              "Golf Rival",
              "https://play.google.com/store/apps/details?id=com.sports.real.golf.rival.online",
              "https://play.google.com/store/apps/details?id=com.sports.real.golf.rival.online",
              "https://play.google.com/store/apps/details?id=com.sports.real.golf.rival.online"));


      HTTPAssetLoader.inst().init(new HTTPAssetLoader.Listener() {

        @Override
        public void finish(ArrayList<HTTPAssetLoader.LoadItem> loadedItems) {
          float stepX = 0;
          for (HTTPAssetLoader.LoadItem item : loadedItems){

            Image actor = new Image(new TextureRegionDrawable(item.getItemTexture()));
            actor.setPosition(stepX += 100, 0);
            actor.addListener(new ClickListener() {

              @Override
              public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.net.openURI(item.getAndroidStoreURI());
              }
            });

            GStage.getStage().addActor(actor);

          }
        }

        @Override
        public void error(Throwable e) {
          e.printStackTrace();
        }
      }, loadItems);

      //////////cross assets loading///////////////


    }

    @Override
    public void run() {

    }
}
