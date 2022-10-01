package com.costin.eeon.game.smileys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.costin.eeon.game.world.items.ItemId;

import java.util.HashMap;
import java.util.Objects;

public class SmileyManager {

    private static SmileyManager singleton;
    public final Texture smileys = new Texture(Gdx.files.internal("smileys/smileys.png"));
    public final Texture auras = new Texture(Gdx.files.internal("smileys/auras.png"));

    private HashMap<Integer, Smiley> smileyTypes;
    private HashMap<Integer, Aura> auraShapes;

    private int smileyCount;
    private int obtainableSmileyCount;
    private int auraCount;
    private int auraImagesI;

    public SmileyManager() {
        smileys.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        auras.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        generateSmileys();
        generateAuras();
        singleton = this;
    }

    public static SmileyManager getInstance() {
        return singleton;
    }

    public int getSmileyCount() {
        return smileyCount;
    }

    public int getObtainableSmileyCount() {
        return obtainableSmileyCount;
    }

    public int getAuraCount() {
        return auraCount;
    }

    private void generateAuras() {
        auraShapes = new HashMap<>();

        addAuraShape(0, "Default", auras, "");
        addAuraShape(1, "Pinwheel", auras, "aurashapepinwheel", 6);
        addAuraShape(2, "Torus", auras, "aurashapetorus");
        addAuraShape(3, "Ornate", auras, "goldmember", 6);
        addAuraShape(4, "Spiral", auras, "aurashapespiral", 6, 0.15f);
        addAuraShape(5, "Star", auras, "aurashapestar");
        addAuraShape(6, "Snowflake", auras, "aurashapesnowflake");
        addAuraShape(7, "Atom", auras, "aurashapeatom", 8, 0.175f);
        addAuraShape(8, "Sawblade", auras, "aurashapesawblade", 6, 0.2f);
        addAuraShape(9, "Target", auras, "aurashapetarget", 6, 0.15f);
        //addAuraShape(10, "Bubble", aurasBubbleBMD, "aurabubble", 8, .1, false, false);
        //addAuraShape(11, "Galaxy", aurasGalaxyBMD, "auragalaxy", 12, .15, false, false);
        addAuraShape(12, "Heart", auras, "aurashapeheart", 10, 0.125f);
        addAuraShape(13, "Flower", auras, "aurashapesunflower");
    }

    private void generateSmileys() {
        smileyTypes = new HashMap<>();

        /*** Define and declare smilies ***/
        //Free
        addSmiley(0, "Smiley", "", smileys, "");
        addSmiley(1, "Grin", "", smileys, "");
        addSmiley(2, "Tongue", "", smileys, "");
        addSmiley(3, "Happy", "", smileys, "");
        addSmiley(4, "Annoyed", "", smileys, "");
        addSmiley(5, "Sad", "", smileys, "");

        //Beta / Pro smilies
        addSmiley(6, "Crying", "", smileys, "pro");
        addSmiley(7, "Wink", "", smileys, "pro");
        addSmiley(8, "Frustrated", "", smileys, "pro");
        addSmiley(9, "Shades", "", smileys, "pro");
        addSmiley(10, "Devil", "", smileys, "pro");
        addSmiley(11, "Inquisitive", "", smileys, "pro");

        //Other
        addSmiley(12, "Ninja", "", smileys, "smileyninja", 0x0);
        addSmiley(13, "Santa", "", smileys, "smileysanta");
        addSmiley(14, "Worker", "", smileys, "");
        addSmiley(15, "Big Spender", "", smileys, "smileybigspender");
        addSmiley(16, "Superman", "", smileys, "smileysuper");
        addSmiley(17, "Surprise", "", smileys, "smileysupprice");
        addSmiley(18, "Indifferent", "", smileys, ""); //In the middle free smiley :D
        addSmiley(19, "Girl", "", smileys, "");
        addSmiley(20, "New Year 2010", "", smileys, "mixednewyear2010");
        addSmiley(21, "Coy", "", smileys, "");
        addSmiley(22, "Wizard", "", smileys, "smileywizard");

        addSmiley(23, "Fan Boy", "", smileys, "smileyfanboy");
        addSmiley(24, "Terminator", "", smileys, "");
        addSmiley(25, "Extra Grin", "", smileys, "smileyxd");

        addSmiley(26, "Bully", "", smileys, "smileybully");
        addSmiley(27, "Commando", "", smileys, "smileycommando");
        addSmiley(28, "Kissing", "", smileys, "smileyvalentines2011");
        addSmiley(29, "Bird", "", smileys, "smileybird");
        addSmiley(30, "Bunny", "", smileys, "smileybunni");

        addSmiley(31, "Diamond Touch", "", smileys, "unobtainable");
        addSmiley(32, "Fire Wizard", "", smileys, "smileywizard2");
        addSmiley(33, "Extra Tongue", "", smileys, "smileyxdp");
        addSmiley(34, "Postman", "", smileys, "smileypostman");
        addSmiley(35, "Templar", "", smileys, "smileytemplar");
        addSmiley(36, "Angel", "", smileys, "");
        addSmiley(37, "Nurse", "", smileys, "smileynurse");
        addSmiley(38, "Vampire", "", smileys, "smileyhw2011vampire");
        addSmiley(39, "Ghost", "", smileys, "smileyhw2011ghost");
        addSmiley(40, "Frankenstein", "", smileys, "smileyhw2011frankenstein");
        addSmiley(41, "Witch", "", smileys, "smileywitch");

        addSmiley(42, "Indian", "", smileys, "smileytg2011indian");
        addSmiley(43, "Pilgrim", "", smileys, "smileytg2011pilgrim");

        addSmiley(44, "Pumpkin", "", smileys, "smileypumpkin1");
        addSmiley(45, "Lit Pumpkin", "", smileys, "smileypumpkin2");

        addSmiley(46, "Snowman", "", smileys, "smileyxmassnowman");
        addSmiley(47, "Reindeer", "", smileys, "smileyxmasreindeer");
        addSmiley(48, "Grinch", "", smileys, "smileyxmasgrinch");
        addSmiley(49, "Maestro", "", smileys, "bricknode");
        addSmiley(50, "DJ", "", smileys, "brickdrums");
        addSmiley(51, "Sigh", "", smileys, "");
        addSmiley(52, "Robber", "", smileys, "", 0x0);
        addSmiley(53, "Police", "", smileys, "", 0xFF0c64f6);
        addSmiley(54, "Purple Ghost", "", smileys, "smileypurpleghost");
        addSmiley(55, "Pirate", "", smileys, "");
        addSmiley(56, "Viking", "", smileys, "");
        addSmiley(57, "Karate", "", smileys, "");
        addSmiley(58, "Cowboy", "", smileys, "");
        addSmiley(59, "Diver", "", smileys, "smileydiver");
        addSmiley(60, "Tanned", "", smileys, "smileytanned");
        addSmiley(61, "Propeller Hat", "", smileys, "");
        addSmiley(62, "Hard Hat", "", smileys, "smileyhardhat");
        addSmiley(63, "Gas Mask", "", smileys, "smileygasmask");
        addSmiley(64, "Robot", "", smileys, "");
        addSmiley(65, "Peasant", "", smileys, "");
        addSmiley(66, "Guard", "", smileys, "");
        addSmiley(67, "Blacksmith", "", smileys, "");
        addSmiley(68, "LOL", "", smileys, "");
        addSmiley(69, "Dog", "", smileys, "");
        addSmiley(70, "Alien", "", smileys, "smileyalien");
        addSmiley(71, "Astronaut", "", smileys, "smileyastronaut");
        addSmiley(72, "PartyOrange", "", smileys, "unobtainable");
        addSmiley(73, "PartyGreen", "", smileys, "unobtainable");
        addSmiley(74, "PartyBlue", "", smileys, "unobtainable");
        addSmiley(75, "PartyRed", "", smileys, "unobtainable");
        addSmiley(76, "Daredevil", "", smileys, "");
        addSmiley(77, "Monster", "", smileys, "smileymonster");
        addSmiley(78, "Skeleton", "", smileys, "smileyskeleton");
        addSmiley(79, "Mad Scientist", "", smileys, "smileymadscientist");
        addSmiley(80, "Headhunter", "", smileys, "smileyheadhunter");
        addSmiley(81, "Safari", "", smileys, "smileysafari");
        addSmiley(82, "Archaeologist", "", smileys, "smileyarchaeologist");
        addSmiley(83, "New Year 2013", "", smileys, "smileynewyear2012");
        addSmiley(84, "Winter Hat", "", smileys, "smileywinter");
        addSmiley(85, "Fire demon", "", smileys, "smileyfiredeamon");
        addSmiley(86, "Bishop", "", smileys, "smileybishop");
        addSmiley(87, "Zombie", "", smileys, "unobtainable");
        addSmiley(88, "Bruce", "", smileys, "smileyzombieslayer");
        addSmiley(89, "Unit", "", smileys, "smileyunit");
        addSmiley(90, "Spartan", "", smileys, "smileyspartan");
        addSmiley(91, "Lady", "", smileys, "smileyhelen");
        addSmiley(92, "Cow", "", smileys, "smileycow");
        addSmiley(93, "Scarecrow", "", smileys, "smileyscarecrow");
        addSmiley(94, "Dark Wizard", "", smileys, "smileydarkwizard");
        addSmiley(95, "Kung Fu Master", "", smileys, "smileykungfumaster");
        addSmiley(96, "Fox", "", smileys, "smileyfox");
        addSmiley(97, "Night Vision", "", smileys, "smileynightvision");
        addSmiley(98, "Summer Girl", "", smileys, "smileysummergirl");
        addSmiley(99, "Fan Boy II", "", smileys, "smileyfanboy2");
        addSmiley(100, "Sci-Fi Hologram", "", smileys, "unobtainable");
        addSmiley(101, "Gingerbread", "", smileys, "smileygingerbread");
        addSmiley(102, "Caroler", "", smileys, "smileycaroler");
        addSmiley(103, "Elf", "", smileys, "smileyelf");
        addSmiley(104, "Nutcracker", "", smileys, "smileynutcracker");
        addSmiley(105, "Blushing", "", smileys, "brickvalentines2015");
        addSmiley(106, "Artist", "", smileys, "smileyartist"); // Loading screen contest prize
        addSmiley(107, "Princess", "", smileys, "");
        addSmiley(108, "Chef", "", smileys, "");
        addSmiley(109, "Clown", "", smileys, "");
        addSmiley(110, "Red Ninja", "", smileys, "smileyninjared");
        addSmiley(111, "3D Glasses", "", smileys, "smiley3dglasses");
        addSmiley(112, "Sunburned", "", smileys, "smileysunburned");
        addSmiley(113, "Tourist", "", smileys, "smileytourist");
        addSmiley(114, "Graduate", "", smileys, "smileygraduate");
        addSmiley(115, "Sombrero", "", smileys, "smileysombrero");
        addSmiley(116, "Cat", "", smileys, "");
        addSmiley(117, "Scared", "", smileys, ""); // FREE!
        addSmiley(118, "Ghoul", "", smileys, "smileyghoul");
        addSmiley(119, "Mummy", "", smileys, "smileymummy");
        addSmiley(120, "Bat", "", smileys, "smileybat");
        addSmiley(121, "Eyeball", "", smileys, "smileyeyeball"); // Halloween campaign prize
        addSmiley(122, "Light Wizard", "", smileys, "smileylightwizard");
        addSmiley(123, "Hooded", "", smileys, "smileyhooded");
        addSmiley(124, "Earmuffs", "", smileys, "smileyearmuffs");
        addSmiley(125, "Penguin", "", smileys, "smileypenguin");
        addSmiley(126, "Gold Smiley", "", smileys, "goldmember");
        addSmiley(127, "Gold Ninja", "", smileys, "goldmember");
        addSmiley(128, "Gold Robot", "", smileys, "goldmember");
        addSmiley(129, "Gold Top Hat", "", smileys, "goldmember");
        addSmiley(130, "Sick", "", smileys, "");
        addSmiley(131, "Unsure", "", smileys, "");
        addSmiley(132, "Goofy", "", smileys, "smileygoofy");
        addSmiley(133, "Raindrop", "", smileys, "smileyraindrop");
        addSmiley(134, "Bee", "", smileys, "smileybee");
        addSmiley(135, "Butterfly", "", smileys, "smileybutterfly");
        addSmiley(136, "Sea Captain", "", smileys, "smileyseacaptain");
        addSmiley(137, "Soda Clerk", "", smileys, "smileysodaclerk");
        addSmiley(138, "Lifeguard", "", smileys, "smileylifeguard");
        addSmiley(139, "Aviator", "", smileys, "smileyaviator");
        addSmiley(140, "Sleepy", "", smileys, "smileysleepy");
        addSmiley(141, "Seagull", "", smileys, "smileyseagull");
        addSmiley(142, "Werewolf", "", smileys, "smileywerewolf");
        addSmiley(143, "Swamp Creature", "", smileys, "smileyswampcreature");
        addSmiley(144, "Fairy", "", smileys, "smileyfairy");
        addSmiley(145, "Firefighter", "", smileys, "smileyfirefighter");
        addSmiley(146, "Spy", "", smileys, "smileyspy", 0x0);
        addSmiley(147, "Devil Skull", "", smileys, "smileydevilskull");
        addSmiley(148, "Clockwork Robot", "", smileys, "smileyclockwork");
        addSmiley(149, "Teddy Bear", "", smileys, "smileyteddybear");
        addSmiley(150, "Christmas Soldier", "", smileys, "smileychristmassoldier");
        addSmiley(151, "Scrooge", "", smileys, "smileyscrooge");
        addSmiley(152, "Boy", "", smileys, "");
        addSmiley(153, "Pigtails", "", smileys, "smileypigtails");
        addSmiley(154, "Doctor", "", smileys, "smileydoctor");
        addSmiley(155, "Turban", "", smileys, "smileyturban");
        addSmiley(156, "Hazmat Suit", "", smileys, "smileyhazmatsuit");
        addSmiley(157, "Leprechaun", "", smileys, "smileyleprechaun");
        addSmiley(158, "Angry", "", smileys, "smileyangry");
        addSmiley(159, "Smirk", "", smileys, "smileysmirk");
        addSmiley(160, "Sweat", "", smileys, "smileysweat");
        addSmiley(161, "Country Singer", "", smileys, "brickguitar");
        addSmiley(162, "Thor", "", smileys, "smileythor");
        addSmiley(163, "Cowgirl", "", smileys, "");
        addSmiley(164, "Raccoon", "", smileys, "smileyraccoon");
        addSmiley(165, "Lion", "", smileys, "smileylion");
        addSmiley(166, "Laika", "", smileys, "smileylaiika");
        addSmiley(167, "Fishbowl", "", smileys, "smileyfishbowl");
        addSmiley(168, "Slime", "", smileys, "smileyslime");
        addSmiley(169, "Designer", "", smileys, "smileydesigner");
        addSmiley(170, "Frozen", "", smileys, "smileyfrozen");
        addSmiley(171, "Masquerade", "", smileys, "smileymasquerade");
        addSmiley(172, "Polar Bear", "", smileys, "smileypolarbear");
        addSmiley(173, "Baseball Cap", "", smileys, "smileybaseball");
        addSmiley(174, "Golfer", "", smileys, "smileygolfer");
        addSmiley(ItemId.SMILEY_PLATINUM_SPENDER, "Platinum Big Spender", "", smileys, "smileyplatinumspender");
        addSmiley(176, "Green Dragon", "", smileys, "smileydragongreen");
        addSmiley(177, "Red Dragon", "", smileys, "smileydragonred");
        addSmiley(178, "Executioner", "", smileys, "smileyexecutioner");
        addSmiley(179, "Gargoyle", "", smileys, "smileygargoyle");
        addSmiley(180, "Banshee", "", smileys, "smileybanshee");
        addSmiley(181, "Golem", "", smileys, "smileygolem");
        addSmiley(182, "Frost Dragon", "", smileys, "smileyfrostdragon");
        addSmiley(183, "Squirrel", "", smileys, "smileysquirrel");
        addSmiley(184, "Golden Dragon", "", smileys, "smileygoldendragon");
        addSmiley(185, "Robot Mk II", "", smileys, "smileyrobot2");
        addSmiley(186, "Black Dragon", "", smileys, "smileydragonblack");
        addSmiley(187, "Silver Dragon", "", smileys, "smileydragonsilver");
        addSmiley(188, "Purple Dragon", "", smileys, "smileydragonpurple");
    }

    private void addSmiley(int id, String name, String desc, Texture originalTexture, String payVaultID, int minimapColor) {
        smileyCount++;
        Smiley smiley = new Smiley();
        smiley.texture = new TextureRegion(originalTexture, id * 26, 0, 26, 26);
        smiley.goldenTexture = new TextureRegion(originalTexture, id * 26, 26, 26, 26);
        smiley.name = name;
        smiley.description = desc;
        smiley.vaultID = payVaultID;
        smiley.minimapColor = minimapColor;
        if (!Objects.equals(payVaultID, "unobtainable")) obtainableSmileyCount++;
        smileyTypes.put(id, smiley); // obtainable smileys
        //else ...
    }

    private void addAuraShape(int id, String name, Texture originalTexture, String payVaultID, int frames, float speed, boolean createRotationAnimation, boolean generate) {
        auraCount++;
        Aura aura = new Aura();
        aura.texture = new TextureRegion(originalTexture, auraImagesI * 64, 0, 64, 64);
        aura.goldenTexture = new TextureRegion(originalTexture, auraImagesI * 64, 64, 64, 64);
        if (frames > 1) {
            TextureRegion[] textureRegions = new TextureRegion[frames];
            TextureRegion[] goldenTextureRegions = new TextureRegion[frames];
            for (int i = 0; i < frames; i++) {
                textureRegions[i] = new TextureRegion(originalTexture, (i + auraImagesI) * 64, 0, 64, 64);
                goldenTextureRegions[i] = new TextureRegion(originalTexture, (i + auraImagesI) * 64, 64, 64, 64);
            }

            aura.animation = new Animation<>(speed / 2, textureRegions);
            aura.animation.setPlayMode(Animation.PlayMode.LOOP);
            aura.goldenAnimation = new Animation<>(speed / 2, goldenTextureRegions);
            aura.goldenAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }
        auraImagesI += frames;
        aura.name = name;
        aura.vaultID = payVaultID;
        auraCount++;
        auraShapes.put(id, aura);
    }

    private void addAuraShape(int id, String name, Texture originalTexture, String payVaultID, int frames, float speed) {
        addAuraShape(id, name, originalTexture, payVaultID, frames, speed, false, true);
    }

    private void addAuraShape(int id, String name, Texture originalTexture, String payVaultID, int frames) {
        addAuraShape(id, name, originalTexture, payVaultID, frames, 0.2f, false, true);
    }

    private void addAuraShape(int id, String name, Texture originalTexture, String payVaultID) {
        addAuraShape(id, name, originalTexture, payVaultID, 1, 0.2f, false, true);
    }

    private void addSmiley(int id, String name, String desc, Texture originalTexture, String payVaultID) {
        addSmiley(id, name, desc, originalTexture, payVaultID, Color.rgba8888(Color.WHITE));
    }

    public Smiley getSmileyByID(int id) { // can return null!
        return smileyTypes.get(id);
    }


    public Aura getAuraByID(int id) {
        return auraShapes.get(id);
    }
}