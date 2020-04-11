package com.ss.core.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class GClipGroup extends Group {
   float clipH;
   float clipW;
   float clipX;
   float clipY;
   boolean isClipping;

   public void draw(Batch batch, float dt) {

    if(this.clipW >= 1.0F && this.clipH >= 1.0F) {
      batch.end();
      batch.begin();
//      boolean var3;
//      var3 = this.isClipping() && this.clipBegin(this.clipX + this.getX(), this.clipY + this.getY(), this.clipW, this.clipH);
      this.clipBegin(this.getX(), this.getY(), this.clipW, this.clipH);

      super.draw(batch, dt);
      batch.end();
      this.clipEnd();
      batch.begin();
    }

   }

   private boolean isClipping() {
      return this.isClipping;
   }

   public void resetClipArea() {
      this.isClipping = false;
   }

   public void setClipArea(float var1, float var2, float var3, float var4) {
      this.clipX = var1;
      this.clipY = var2;
      this.clipW = var3;
      this.clipH = var4;
      this.isClipping = true;
   }
}
