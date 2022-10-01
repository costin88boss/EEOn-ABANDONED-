package com.costin.eeon.game.players;

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
import com.costin.eeon.graphic.scenes.WorldScreen;
import com.costin.eeon.net.packets.player.updates.serverside.ServerMovePacket;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.esotericsoftware.kryonet.Connection;

public class  Player extends GameObject {

    private static final float serverFixSpeed = 1;
    public Item<GameObject> actionCollision;
    public Item<GameObject> innerCollision;
    protected float diffX, diffY;
    protected boolean isGrounded, hitCeiling;
    boolean oldPacket;
    //rainbow stuff, was lazy to put at top
    int rainbowType = 0;
    private float x, y, vY, vX;
    private boolean hasGodMode, isGolden;
    private String username;
    private Animation<TextureRegion> auraAnim;
    private TextureRegion smiley, aura;
    private Color auraColor;
    private boolean rainbowMode;
    private int smileyID, auraID;
    private ServerMovePacket movePacket;

    public Player(String username) {
        super();
        movePacket = new ServerMovePacket();
        actionCollision = new Item<>(this);
        innerCollision = new Item<>(this);
        auraColor = Color.WHITE.cpy();
        setLocalAura(0);
        setLocalUsername(username);
    }

    public void setLocalSmiley(int newSmiley) {
        smiley = isGolden ? SmileyManager.getInstance().getSmileyByID(newSmiley).getGoldenTexture() : SmileyManager.getInstance().getSmileyByID(newSmiley).getTexture();

    }

    public void setLocalAura(int newAura) {
        Aura newAuraShape = SmileyManager.getInstance().getAuraByID(newAura);
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
        if (isGrounded && !hasGodMode) vY += Laws.gravity / 5;
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
            // better interpolate
            x += serverFixSpeed * (movePacket.x - x);
            y += serverFixSpeed * (movePacket.y - y);
            diffX = movePacket.vXDiff;
            diffY = movePacket.vYDiff;
            WorldManager.getInstance().collWorld.update(this, x + 1, y + 1);
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

            if (!hasGodMode) {
                Response.Result res = WorldManager.getInstance().collWorld.move(this, x + vX + 1, y + vY + 1, CollFilter.getInstance().blockFilter);
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
                }
                isGrounded = canGround;
            } else { //640, 480
                x += vX;
                y += vY;
                WorldManager.getInstance().collWorld.update(this, x, y);
            }
            Rect rect = WorldManager.getInstance().collWorld.getRect(this);
            float _x, _y;
            if (!hasGodMode) {
                _x = rect.x - 1;
                _y = rect.y - 1;
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

            if (imx != 0) {
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

            if (imy != 0) {
                moving = true;
            } else if (diffY < 0.1 && diffY > -0.1) {
                float ty = y % 16;
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
            else if (auraAnim != null) {
                batch.draw(auraAnim.getKeyFrame(WorldScreen.getInstance().getAuraTime()), x - 24, y - 24);
            }
            batch.setColor(1, 1, 1, 1);
        }
        batch.draw(smiley, x - 5, y - 5);
    }
}
