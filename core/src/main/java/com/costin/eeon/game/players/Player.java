package com.costin.eeon.game.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.costin.eeon.game.GameObject;
import com.costin.eeon.game.Laws;
import com.costin.eeon.game.smileys.Aura;
import com.costin.eeon.game.smileys.SmileyManager;
import com.costin.eeon.game.world.WorldManager;
import com.costin.eeon.game.world.items.ItemId;
import com.costin.eeon.net.packets.player.updates.serverside.ServerMovePacket;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.esotericsoftware.kryonet.Connection;

public class Player extends GameObject {

    private static final float serverFixSpeed = 0.1f;
    private final Vector2 serverPos;
    public Item<GameObject> actionCollision;
    protected float diffX, diffY;
    protected boolean isGrounded, hitCeiling, stuckInBlock;
    boolean oldPacket;
    int rainbowType = 0;
    private float x, y, vY, vX;
    private boolean hasGodMode, isGolden;
    private String username;
    private Animation<TextureRegion> auraAnim;
    private Animation<TextureRegion> secondAuraAnim;
    private boolean staffAuraLoaded;
    private float animFrameTime;
    private TextureRegion smiley, aura;
    private Color auraColor;
    private boolean rainbowMode;
    private int smileyID, auraID;
    private ServerMovePacket movePacket;

    public Player(String username) {
        super();
        movePacket = new ServerMovePacket();
        actionCollision = new Item<>(this);
        auraColor = Color.WHITE.cpy();
        setLocalAura(0);
        setLocalUsername(username);
        serverPos = new Vector2();
    }

    public void setLocalSmiley(int newSmiley) {
        smiley = isGolden ? SmileyManager.getInstance().getSmileyByID(newSmiley).getGoldenTexture() : SmileyManager.getInstance().getSmileyByID(newSmiley).getTexture();

    }

    public void setLocalAura(int newAura) {
        resetAuraCurrAnim();
        Aura newAuraShape = SmileyManager.getInstance().getAuraByID(newAura);
        if (newAuraShape.isStaffAura() || newAura == 3) secondAuraAnim = newAuraShape.getGoldenAnimation();
        else secondAuraAnim = null;
        if (!newAuraShape.isAnimated()) {
            if (!isGolden) aura = newAuraShape.getTexture();
            else aura = newAuraShape.getGoldenTexture();
            auraAnim = null;
        } else {
            aura = null;
            if (!isGolden) auraAnim = newAuraShape.getAnimation();
            else auraAnim = newAuraShape.getGoldenAnimation();
        }
        auraID = newAura;
    }

    public void setLocalGolden(boolean gold) {
        isGolden = gold;
    }

    public void setLocalPosition(float x, float y) {
        this.x = x;
        this.y = y;
        movePacket.x = x;
        movePacket.y = y;
    }

    public void setLocalVelocity(float x, float y) {
        this.vX = x;
        this.vY = y;
    }

    public void setLocalUsername(String newName) {
        username = newName;
    }

    public void setLocalGodMode(boolean godMode) {
        resetAuraCurrAnim();
        if (isGrounded && !hasGodMode) {
            vY += Laws.gravity / 5;
            y += 1;
        }
        isGrounded = false;
        hasGodMode = godMode;
    }

    public void setLocalAuraColor(int newColor) {
        Color color = new Color();
        Color.rgba8888ToColor(color, newColor);
        if (color.a > 0) {
            rainbowMode = false;
            auraColor = color;
        } else {
            auraColor.set(0, 0, 0, 0f);
            rainbowMode = true;
        }
    }

    public void resetAuraCurrAnim() {
        staffAuraLoaded = false;
        animFrameTime = 0;
    }

    public boolean isHasGodMode() {
        return hasGodMode;
    }

    public int getAuraID() {
        return auraID;
    }

    public int getSmileyID() {
        return smileyID;
    }

    public boolean isGolden() {
        return isGolden;
    }

    public int getAuraColor() {
        return Color.rgba8888(auraColor);
    }

    public String getUsername() {
        return username;
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public float getCorrectY() {
        return Math.abs(y - 480);
    }

    public void updatePacket(ServerMovePacket packet, Connection connection) {
        movePacket = packet;
        oldPacket = false;
        setLocalPosition(movePacket.x, movePacket.y);
    }

    public void predict() {
        if (!oldPacket) {
            vX = movePacket.vX;
            vY = movePacket.vY;
            //x = movePacket.x;
            //y = movePacket.y;
            // better interpolate serverPos
            serverPos.scl(1 - serverFixSpeed);
            serverPos.add(x * serverFixSpeed, y * serverFixSpeed);
            diffX = movePacket.vXDiff;
            diffY = movePacket.vYDiff;
            WorldManager.getInstance().collWorld.update(this, x, y);
        } else {
            diffX = movePacket.vXDiff;
            if (hasGodMode) {
                diffY = movePacket.vYDiff;
            }

            if (!hasGodMode) {
                if (isGrounded || hitCeiling) {
                    hitCeiling = false;
                    diffY = 0;
                    vY = 0;
                }

                diffY -= Laws.gravity / 5;

                if (movePacket.spaced && isGrounded) {
                    diffY = Laws.jumpHeight / 4;
                    isGrounded = false;
                }
            }

            vX += diffX;
            vY += diffY;

            if (hasGodMode) {
                if (movePacket.vXDiff == 0) vX *= Laws.baseDrag;
                if (movePacket.vYDiff == 0) vY *= Laws.baseDrag;
            } else {
                if (movePacket.vXDiff != 0) {
                    vX *= Laws.baseDrag;
                } else vX *= Laws.noModDrag;
            }

            if (vX > 16) {
                vX = 16;
            } else if (vX < -16) {
                vX = -16;
            } else if (vX < 0.0001 && vX > -0.0001) {
                vX = 0;
            }

            if (vY > 16) {
                vY = 16;
            } else if (vY < -16) {
                vY = -16;
            } else if (vY < 0.0001 && vY > -0.0001) {
                vY = 0;
            }

            boolean stuckInBlock = false;
            if (!hasGodMode) {
                Response.Result res = WorldManager.getInstance().collWorld.move(this, x + vX, y + vY, CollFilter.getInstance().blockFilter);
                boolean canGround = false;
                for (int i = 0; i < res.projectedCollisions.size(); i++) {
                    Collision coll = res.projectedCollisions.get(i);
                    if (coll.normal.y == 1) {
                        canGround = true;
                    }
                    if (coll.normal.y == -1) {
                        hitCeiling = true;
                    }
                    if (coll.normal.x != 0) {
                        vX = coll.normal.x / 1000f;
                    }
                    if (coll.overlaps) {
                        stuckInBlock = true;
                        break;
                    }
                }
                isGrounded = canGround;
            } else { //640, 480
                x += vX;
                y += vY;
                WorldManager.getInstance().collWorld.update(this, x, y);
            }
            if (stuckInBlock) {
                WorldManager.getInstance().collWorld.update(this, x, y);
                vX = 0;
                vY = 0;
            }
            Rect rect = WorldManager.getInstance().collWorld.getRect(this);
            float _x, _y;
            if (!hasGodMode && !stuckInBlock) {
                _x = rect.x;
                _y = rect.y;
            } else {
                _x = x;
                _y = y;
            }
            if (_x >= 0) {
                if (_x + 16 <= WorldManager.getInstance().EEWorld.worldWidth * 16) {
                    x = _x;
                } else {
                    vX = 0;
                    x -= 1;
                }
            } else {
                vX = 0;
                x += 1;
            }
            if (_y <= 480) {
                if (_y + 16 >= -(WorldManager.getInstance().EEWorld.worldHeight * 16 - 480 - 32)) {
                    y = _y;
                } else {
                    vY = 0;
                    y += 1;
                }
            } else {
                vY = 0;
                y -= 1;
            }

            //Auto align to grid. (do not autocorrect in liquid)
            int imx = Math.round(vX) << 8;
            int imy = Math.round(vY) << 8;

            boolean moving = false;

            if (imx != 0 || (ItemId.isLiquid(0) && !hasGodMode)) {
                moving = true;
            } else if (diffX < 0.1 && diffX > -0.1) {
                float tx = x % 16;
                if (tx < 2) {
                    if (tx < .2) {
                        x = (int) x;
                    } else x -= tx / 15;
                } else if (tx > 14) {
                    if (tx > 15.8) {
                        x = (int) x;
                        x++;
                    } else x += (tx - 14) / 15;
                }

            }

            if (imy != 0 || (ItemId.isLiquid(0) && !hasGodMode)) {
                moving = true;
            } else if (diffY < 0.1 && diffY > -0.1) {
                float ty = getCorrectY() % 16;

                if (ty < 2) {
                    if (ty < .2) {
                        y = (int) y;
                    } else y -= ty / 15;
                } else if (ty > 14) {

                    if (ty > 15.8) {
                        y = (int) y;
                        y++;
                    } else y += (ty - 14) / 15;
                }
            }
        }


        //WorldScreen.getInstance().test( ((int) (x / 16) * 16), ((int) (y / 16) * 16), 16, 16, ((int) (x / 16) * 16) + 16, ((int) (y / 16) * 16));
        //WorldScreen.getInstance().test2( ((int) (x / 16) * 16), ((int) (y / 16) * 16), 16, 16, ((int) (x / 16) * 16) + 16, ((int) (y / 16) * 16) - 16);

        diffX = 0;
        diffY = 0;
        oldPacket = true;
    }

    public void draw(SpriteBatch batch) {
        if (hasGodMode) {
            if (rainbowMode) {
                if (auraColor.r >= 0.9 && rainbowType == 0) {
                    rainbowType = 1;
                }
                if (auraColor.g >= 0.9 && rainbowType == 1) {
                    rainbowType = 2;
                }
                if (auraColor.b >= 0.9 && rainbowType == 2) {
                    rainbowType = 0;
                }

                if (rainbowType == 0) auraColor.lerp(1, 0, 0.25f, 0, 0.025f);
                if (rainbowType == 1) auraColor.lerp(0.25f, 1, 0, 0, 0.025f);
                if (rainbowType == 2) auraColor.lerp(0, 0.25f, 1, 0, 0.025f);
            }
            batch.setColor(auraColor.r, auraColor.g, auraColor.b, 1);
            if (aura != null) batch.draw(aura, x - 24, y - 24);
            else if (auraAnim != null || secondAuraAnim != null) {
                animFrameTime += Gdx.graphics.getDeltaTime();
                if (secondAuraAnim != null && auraID != 3) {
                    if (staffAuraLoaded) {
                        batch.draw(secondAuraAnim.getKeyFrame(animFrameTime), x - 24, y - 24);
                    } else {
                        batch.draw(auraAnim.getKeyFrame(animFrameTime), x - 24, y - 24);
                    }
                } else {
                    batch.draw(auraAnim.getKeyFrame(animFrameTime), x - 24, y - 24);
                    if (auraID == 3) { // ornate
                        batch.draw(secondAuraAnim.getKeyFrame(animFrameTime + secondAuraAnim.getAnimationDuration() / 2f), x - 24, y - 24);
                    }
                }
                if (!staffAuraLoaded && auraAnim.isAnimationFinished(animFrameTime) ||
                        secondAuraAnim != null && secondAuraAnim.isAnimationFinished(animFrameTime)) {
                    if (secondAuraAnim != null) staffAuraLoaded = true;
                    animFrameTime = 0;
                }
            }
            batch.setColor(1, 1, 1, 1);
        }
        batch.draw(smiley, x - 5, y - 5);
    }
}
